import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatBox extends JPanel implements ActionListener {
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";

    ChatBox(ChatView newView) {
        this.setLayout( new GridLayout(2, 1) );

        textField = new JTextField(20);
        textField.addActionListener(this);
        
        this.add(textField);
        this.textArea = newView.getTextArea();
    }

    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        textArea.append(text + newline);
        textField.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}

