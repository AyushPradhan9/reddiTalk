import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;

public class userListGUI extends JPanel implements statusListener {
	private serverClient client;
	private JList<String> userList;
	private DefaultListModel<String> listModel;
	
	public userListGUI(serverClient client) {
		this.client=client;
		this.client.addStatusListener(this);
		
		listModel=new DefaultListModel<>();
		userList = new JList<>(listModel);
		setLayout(new BorderLayout());
		add(new JScrollPane(userList),BorderLayout.CENTER);
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>1) {
					String login = userList.getSelectedValue();
					MsgGUI msgGUI = new MsgGUI(client,login);
					
					JFrame frame =new JFrame("Message: "+login);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setSize(500,500);
					frame.getContentPane().add(msgGUI,BorderLayout.CENTER);
					frame.setVisible(true);
				}
			}
		});
	}
	public static void main(String[] args) {
		serverClient client = new serverClient("localhost",8818);
		userListGUI list=new userListGUI(client);
		JFrame frame=new JFrame("People online");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,400);
		frame.getContentPane().add(list, BorderLayout.CENTER);
		frame.setVisible(true);
		
//		if(client.connect()) {
//			try {
//				client.login("ayush","ayush");
//			}
//			catch(IOException e){
//				e.printStackTrace();
//			}
//		}
	}
	@Override
	public void online(String login) {
		listModel.addElement(login);
	}
	@Override
	public void offline(String login) {
		listModel.removeElement(login);
		
	}
}
