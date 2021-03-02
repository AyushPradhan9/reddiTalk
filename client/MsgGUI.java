import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class MsgGUI extends JPanel implements MsgListener {

	private serverClient client;
	private String login;
	private JList<String> messageList=new JList();
	private DefaultListModel<String> listModel = new DefaultListModel<>();
	private JTextField inputField = new JTextField();

	public MsgGUI(serverClient client, String login) {
		this.client=client;
		this.login=login;
		
		client.addMsgListener(this);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(messageList),BorderLayout.CENTER);
		add(inputField, BorderLayout.SOUTH);
		
		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String text=inputField.getText();
					client.msg(login, text);
					listModel.addElement("You: "+text);
					inputField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onMessage(String sendTo, String msgBody) {
		if(login.equalsIgnoreCase(sendTo)) {
			String line=sendTo+": "+msgBody;
			listModel.addElement(line);
		}
	}
	
}
