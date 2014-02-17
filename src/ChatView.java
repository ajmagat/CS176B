// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ChatView extends JPanel
{
    protected JTextPane textPane;
    protected StyleContext context;
    protected StyledDocument doc;

    private final static String newline = "\n";

    ChatView( )
    {
        super(new GridLayout(1, 1));
        context = new StyleContext( );
        doc = new DefaultStyledDocument(context);
        textPane = new JTextPane(doc);
        textPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textPane);

        //Add Components to this panel.
//        GridBagConstraints c = new GridBagConstraints();
      //  c.gridwidth = GridBagConstraints.REMAINDER;
    //    c.gridwidth = 500;

//        c.fill = GridBagConstraints.BOTH;
      //  c.gridx = 0;
      //  c.gridy = 0;
      //  c.weightx = 1.0;
      //  c.weighty = 1.0;
        this.add(scrollPane);
    }

    public JTextPane getTextPane( )
    {
        return this.textPane;
    }
}
