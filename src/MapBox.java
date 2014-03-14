import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

import sample2_waypoints.RoutePainter;

public class MapBox extends JPanel
{
	private JXMapViewer mapViewer;
	private List<GeoPosition> track;
	private TileFactoryInfo info;
	private DefaultTileFactory tileFactory;
	
	/*
	 * Default constructor
	 */
	MapBox()
	{
		// Set Layout
		super(new GridLayout(1, 1));
	}
	
	/*
	 * Constructor with list of GeoPositions passed in
	 */
	MapBox(List<GeoPosition> theList)
	{
		// Set Layout
		super(new GridLayout(1, 1));
		
		track = theList;
		
	}
	
	/*
	 * Draw the path, given the GeoPositions
	 */
	public void drawPath()
	{
		// Create a TileFactoryInfo for OpenStreetMap
		info = new OSMTileFactoryInfo();
		tileFactory = new DefaultTileFactory(info);
		tileFactory.setThreadPoolSize(8);
		mapViewer.setTileFactory(tileFactory);
		
		RoutePainter routePainter = new RoutePainter(track);
		
		// Set the focus
		mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

		// Create waypoints from the geo-positions
		List<Waypoint> trackWaypoints = new ArrayList<Waypoint>();
		for(int i = 0; i < track.size(); i++)
		{
			DefaultWaypoint temp = new DefaultWaypoint(track.get(i));
			trackWaypoints.add(temp);
		}
		Set<Waypoint> waypoints = new HashSet<Waypoint>(trackWaypoints);

		// Create a waypoint painter that takes all the waypoints
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setWaypoints(waypoints);
				
		// Create a compound painter that uses both the route-painter and the waypoint-painter
		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(routePainter);
		painters.add(waypointPainter);
				
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);

	}
}
