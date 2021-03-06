import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MsgGUI extends JFrame implements MsgListener {

    Container container=getContentPane();
    private JLabel nameLabel;
    private JList<String> msgList;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private JTextField inputField=new JTextField("Message");
    private serverClient client;
    private String sendTo;


    public MsgGUI(serverClient client, String sendTo)
    {
    	this.client=client;
    	this.sendTo=sendTo;
		this.client.addMsgListener(this);
		
		nameLabel=new JLabel("User: "+sendTo);
		listModel=new DefaultListModel<>();
		msgList = new JList<>(listModel);
		scrollPane = new JScrollPane(msgList);
		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String text=inputField.getText();
					client.msg(sendTo, text);
					listModel.addElement("You: "+text);
					inputField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
    }
    
   public void setLayoutManager()
   {
       container.setLayout(null);
   }
   
   public void setLocationAndSize()
   {
	   nameLabel.setBounds(20,10,100,30);
	   scrollPane.setBounds(20,50,440,500);
	   inputField.setBounds(20,550,440,30);
   }
   
   public void addComponentsToContainer()
   {
       container.add(nameLabel);
       container.add(scrollPane);
       container.add(inputField);
   }

	@Override
	public void onMessage(String send, String msgBody) {
		if(sendTo.equalsIgnoreCase(send)) {
			String line=send+": "+msgBody;
			listModel.addElement(line);
		}
	}
	
}
