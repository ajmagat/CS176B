import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ChatBox extends JPanel implements FocusListener {
    protected JTextArea textField;
    protected JTextPane textPane;
    private final static String newline = "\n";

    ChatBox(ChatView newView) {
        super(new GridLayout(1, 1));
        textField = new JTextArea();
        textField.setEditable(true);
        textField.addFocusListener(this);
 //       
        JScrollPane scrollPane = new JScrollPane(textField);
        this.add(scrollPane);
        this.textPane = newView.getTextPane();
    }

    public void focusLost(FocusEvent evt) {
        String text = textField.getText();
        StyledDocument doc = textPane.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), text + newline, null);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
     //   textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void focusGained(FocusEvent evt) {

    }

}

