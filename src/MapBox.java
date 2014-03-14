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
import javax.swing.event.MouseInputListener;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.input.CenterMapListener;
import org.jdesktop.swingx.input.PanKeyListener;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCursor;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

import sample2_waypoints.RoutePainter;
import sample3_interaction.SelectionAdapter;
import sample3_interaction.SelectionPainter;

public class MapBox extends JInternalFrame
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
		
		mapViewer = new JXMapViewer();
		this.add(mapViewer);
		this.setVisible(true);
		
		
		// Create a TileFactoryInfo for OpenStreetMap
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		mapViewer.setTileFactory(tileFactory);
		
		// Use 8 threads in parallel to load the tiles
		tileFactory.setThreadPoolSize(8);

		// Add interactions
		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);
		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
		mapViewer.addKeyListener(new PanKeyListener(mapViewer));
		
		// Add a selection painter
		SelectionAdapter sa = new SelectionAdapter(mapViewer); 
		SelectionPainter sp = new SelectionPainter(sa); 
		mapViewer.addMouseListener(sa); 
		mapViewer.addMouseMotionListener(sa); 
		mapViewer.setOverlayPainter(sp);
		
		
		
		// Set the focus
		GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

		//mapViewer.setZoom(7);
		mapViewer.setAddressLocation(frankfurt);
				
		GeoPosition wiesbaden = new GeoPosition(50,  5, 0, 8, 14, 0);
		GeoPosition mainz     = new GeoPosition(50,  0, 0, 8, 16, 0);
		GeoPosition darmstadt = new GeoPosition(49, 52, 0, 8, 39, 0);
		GeoPosition offenbach = new GeoPosition(50,  6, 0, 8, 46, 0);

		// Create a track from the geo-positions
		track = Arrays.asList(frankfurt, wiesbaden, mainz, darmstadt, offenbach);
		RoutePainter routePainter = new RoutePainter(track);

		Waypoint test = new DefaultWaypoint(frankfurt);
		
		// Set the focus
		//mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 1);
		mapViewer.calculateZoomFrom(new HashSet<GeoPosition>(track));

		System.out.println("track size: " + track.size());
		
		
		// Create waypoints from the geo-positions
		Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
				new DefaultWaypoint(frankfurt),
				new DefaultWaypoint(wiesbaden),
				new DefaultWaypoint(mainz),
				new DefaultWaypoint(darmstadt),
				new DefaultWaypoint(offenbach)));
		
		System.out.println("waypoints size:" + waypoints.size());

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
	
	/*
	 * Constructor with list of GeoPositions passed in
	 */
	MapBox(List<GeoPosition> theList)
	{
		
		//track = theList;
		
	}
}
