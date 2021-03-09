import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class grpMsgGUI extends JFrame implements ActionListener, MsgListener {

    Container container=getContentPane();
    private JLabel nameLabel;
    private JButton leaveButton=new JButton("Leave");
    private JList<String> msgList;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private JTextField inputField=new JTextField("Message");
    private serverClient client;
    private String topic;


    public grpMsgGUI(serverClient client, String topic) throws IOException{
    	this.client=client;
    	this.topic=topic;
		this.client.addMsgListener(this);
		
		nameLabel=new JLabel("Topic: "+topic);
		listModel=new DefaultListModel<>();
		msgList = new JList<>(listModel);
		scrollPane = new JScrollPane(msgList);
	
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String text=inputField.getText();
					client.msg(topic, text);
					listModel.addElement("You: "+text);
					inputField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
    }
    
   public void setLayoutManager()
   {
       container.setLayout(null);
   }
   
   public void setLocationAndSize()
   {
	   nameLabel.setBounds(20,10,100,30);
	   leaveButton.setBounds(360,10,100,30);
	   scrollPane.setBounds(20,50,440,500);
	   inputField.setBounds(20,550,440,30);
   }
   
   public void addComponentsToContainer()
   {
       container.add(nameLabel);
       container.add(leaveButton);
       container.add(scrollPane);
       container.add(inputField);
   }
   
   public void addActionEvent()
   {
	   leaveButton.addActionListener(this);
   }
   
   public void actionPerformed(ActionEvent e) {
	   if (e.getSource() == leaveButton) {
    	   try {
    		   String response=client.leave(topic);
    		   JOptionPane.showMessageDialog(this, response);
    		   
    	   } catch (HeadlessException | IOException e1) {
			e1.printStackTrace();
    	   }
    	   setVisible(false);
       }
   }

	public void onMessage(String send, String msgBody) {
		String line=send+": "+msgBody;
		listModel.addElement(line);
	}
}