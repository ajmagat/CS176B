CS176B
======
f

Project for CS 176B

Super Awesome Chat Application

Project Members: 
Aaron Magat
Alick Xu

Project Description:

For our project we will be implementing a chat room / message board application. It will allow users to chat with one or more people, and conversation history will be saved and remain viewable on subsequent logins. Users will also be able to change the text size, font, color, etc. of their messages, which will be done by modifying the packet headers. For chats between two people, the chat will work on a peer-to-peer connection. For chats between multiple (more than two) people, the chat will work with a typical client-server connection, although if there is time we may try and make this peer-to-peer as well. 
When sending messages, there will also be a visualization of the route the packets take to their destination(s). The visualization is a graphic representation of the nodes that the packets visit on their way to their destination(s). The routes will be overlayed on a Google Map-style map. This map will be visible on a sidebar attached to the chat window. 
Messages will be sent with SSL security features. Users will also have the option of having incoming messages be unencrypted upon delivery or having them remain encrypted until the user decides to have them unencrypted in the future.
	
We are currently deciding between using Java or Python to implement our application. 

Sources we have researched so far:

Find Location by IP:
http://www.geobytes.com/IpLocator.htm?GetLocation
Route visualization:
https://developers.google.com/maps/documentation/javascript/

We have found several sources outlining ways to implement some of our features, both in Python and Java. 
Traceroute Implementation Examples:
http://stackoverflow.com/questions/2627706/how-to-to-icmps-and-traceroutes-in-java (Java)
http://jvns.ca/blog/2013/10/31/day-20-scapy-and-traceroute/ (Python)

Packet Manipulation Libraries:
https://github.com/mgodave/Jpcap (Java)
http://www.secdev.org/projects/scapy/ (Python)

SSL Examples:
http://stilius.net/java/java_ssl.php (Java)
http://docs.python.org/release/2.5.2/lib/socket-example.html (Python)

Deliverables:
Working chat between multiple clients using a client-server connection, possibly peer-to-peer
Working chat between two clients using peer-to-peer connection
Chat will allow for adding font, color, text attributes 
Chat will include route visualization of the packets transferred during chats.
Chat will use SSL

Timeline:
1/31: Turn in Project Description
2/7: Have working chat with two clients and multiple clients using client/server connection
       Add font, color, etc.
2/14: Have either the route visualization or the SSL working, and start thinking about the other one
2/21: Continue working on route visualization/SSL
2/28: If route visualization/SSL is complete, work on P2P connection for two users
Turn in Project Status Report
3/7: Work on P2P connection for two users
        If time permits, start working on P2P for multiple users.
3/14: Turn in Final Project Report
