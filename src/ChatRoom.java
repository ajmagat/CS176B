import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class ChatRoom extends JFrame
{
    private static final int NUM_PIXELS = 600;
    private ChatView newView = new ChatView( );
    private ChatBox newBox = new ChatBox(newView);

    // Default Constructor
    ChatRoom( )
    {
        setTitle( "ChatRoom" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
      //  imageJPanel.setModelJPanel( modelJPanel );
                
        add( newView, BorderLayout.NORTH );
        add( newBox, BorderLayout.SOUTH );
        
        setSize( NUM_PIXELS, NUM_PIXELS );
        setVisible( true );
    }
}
