/*
 * Source: https://github.com/mgodave/Jpcap/sample
 */
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Traceroute 
{
	private String destination; // Destination that needs to be tracerouted
	private List<String> routeInfo; // Stores route information to get to destination
	
	Traceroute()
	{
		routeInfo = new ArrayList<String>();
	}
	
	Traceroute(String target)
	{
		destination = target;
		routeInfo = new ArrayList<String>();
	}
	
	public void setDestination(String target)
	{
		destination = target;
	}
	
	public String getDestination()
	{
		return destination;
	}
	
	public List<String> getRouteInfo()
	{
		return routeInfo;
	}
	
	public void trace()
	{		
		//initialize Jpcap
		NetworkInterface device=JpcapCaptor.getDeviceList()[new Integer(0)];
		JpcapCaptor captor = null;
		try {
			captor = JpcapCaptor.openDevice(device,2000,false,5000);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		InetAddress thisIP=null;
		for(NetworkInterfaceAddress addr:device.addresses)
		if(addr.address instanceof Inet4Address)
		{
				thisIP=addr.address;
				break;
		}
				
		//obtain MAC address of the default gateway
		InetAddress pingAddr = null;
		try {
			pingAddr = InetAddress.getByName("www.microsoft.com");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			captor.setFilter("tcp and dst host "+pingAddr.getHostAddress(),true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] gwmac=null;
		while(true)
		{
			try {
				new URL("http://www.microsoft.com").openStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Packet ping=captor.getPacket();
			if(ping==null)
			{
				System.out.println("cannot obtain MAC address of default gateway.");
				System.exit(-1);
			}
			else if(Arrays.equals(((EthernetPacket)ping.datalink).dst_mac,device.mac_address))
				continue;
			gwmac=((EthernetPacket)ping.datalink).dst_mac;
			break;
		}
				
		//create ICMP packet
		ICMPPacket icmp=new ICMPPacket();
		icmp.type=ICMPPacket.ICMP_ECHO;
		icmp.seq=100;
		icmp.id=0;
		try {
			icmp.setIPv4Parameter(0,false,false,false,0,false,false,false,0,0,0,IPPacket.IPPROTO_ICMP,
							thisIP,InetAddress.getByName(destination));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		icmp.data="data".getBytes();
				
		EthernetPacket ether=new EthernetPacket();
		ether.frametype=EthernetPacket.ETHERTYPE_IP;
		ether.src_mac=device.mac_address;
		ether.dst_mac=gwmac;
		icmp.datalink=ether;
				
		try 
		{
			captor.setFilter("icmp and dst host "+thisIP.getHostAddress(),true);
		} catch (IOException e) {
					e.printStackTrace();
		}

		JpcapSender sender=captor.getJpcapSenderInstance();
		//JpcapSender sender=JpcapSender.openDevice(device);
		sender.sendPacket(icmp);
		while(true)
		{
			ICMPPacket p=(ICMPPacket) captor.getPacket();
			//System.out.println("received "+p);
			if(p==null)
			{
				System.out.println("Timeout");
			}
			else if(p.type==ICMPPacket.ICMP_TIMXCEED)
			{
				routeInfo.add(p.src_ip.getHostName());
				System.out.println(icmp.hop_limit+": "+p.src_ip);
				icmp.hop_limit++;
			}
			else if(p.type==ICMPPacket.ICMP_UNREACH)
			{
				routeInfo.add(p.src_ip.getHostName());
				System.out.println(icmp.hop_limit+": "+p.src_ip);
				System.exit(0);
			}
			else if(p.type==ICMPPacket.ICMP_ECHOREPLY)
			{
				routeInfo.add(p.src_ip.getHostName());
				System.out.println(icmp.hop_limit+": "+p.src_ip);
				System.exit(0);
			}
			else continue;
			sender.sendPacket(icmp);
		}
		
	}
	
}
