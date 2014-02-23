import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.TextArea;
import java.awt.TextField;
import javax.swing.JTextField;
import javax.swing.text.*;


/**
 * This represents the overall chat applications
 */

public class ChatRoom extends JFrame
{
    private static final int NUM_PIXELS = 600;

   // Create the objects of this chat
   private ChatView newView = new ChatView( );
   private ChatBox newBox = new ChatBox(newView);
   private ChatOptions colorOptions = new ChatOptions("Color");
   private ChatOptions sizeOptions = new ChatOptions("Size");

    /**
     * Default Constructor
     */
    ChatRoom( )
    {
        // Set some box related stuff
        setTitle( "ChatRoom" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( NUM_PIXELS, NUM_PIXELS );
        
        JButton btnSubmit = new JButton("Submit");
        
        // ChatBox should be uneditable for now
        // newBox.turnOff();

        /*************************************************/
        /* GUI layout stuff. Probably should never touch */
        /*************************************************/ 
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addComponent(newView, GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(newBox, GroupLayout.PREFERRED_SIZE, 448, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        			.addGap(22))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(colorOptions, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(sizeOptions, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
        			.addGap(333))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(newView, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
        			.addGap(25)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(colorOptions, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
        				.addComponent(sizeOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(29)
        					.addComponent(newBox, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(66)
        					.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())))
        );
        getContentPane().setLayout(groupLayout);
        setVisible( true );

        ChatConvo newConvo = new ChatConvo();
        switchConversation(newConvo);
    }

    /**
     * Method that will switch to a conversation between two or more people
     */ 
    public void switchConversation(ChatConvo newConvo) 
    {
        newView.setChatConvo(newConvo);
        newBox.setChatConvo(newConvo);
        colorOptions.setChatConvo(newConvo);
        sizeOptions.setChatConvo(newConvo);
    }
}
