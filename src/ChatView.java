// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatView extends JPanel
{
    protected JTextArea textArea;
    private final static String newline = "\n";

    ChatView( )
    {
      //  super(new GridLayout(2, 1));
        this.setLayout( new GridLayout(2, 1) );
        textArea = new JTextArea(30, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
      //  c.gridwidth = GridBagConstraints.REMAINDER;
    //    c.gridwidth = 500;

//        c.fill = GridBagConstraints.BOTH;
      //  c.gridx = 0;
      //  c.gridy = 0;
      //  c.weightx = 1.0;
      //  c.weighty = 1.0;
        this.add(scrollPane);
    }

    public JTextArea getTextArea( )
    {
        return this.textArea;
    }
}
