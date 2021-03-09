import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class userListGUI extends JFrame implements statusListener, ActionListener {

    Container container=getContentPane();
    private JButton settingButton=new JButton("Setting");
    private JButton logoffButton=new JButton("Logoff");
    private JLabel onlineLabel=new JLabel("People Online:");
    private JList<String> userList;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private JLabel groupLabel=new JLabel("Join Group:");
	private JTextField topicField=new JTextField("#");
	private JButton joinButton=new JButton("Join");
    private serverClient client;


    public userListGUI(serverClient client)
    {
    	this.client=client;
		this.client.addStatusListener(this);
		
		listModel=new DefaultListModel<>();
		userList = new JList<>(listModel);
		scrollPane = new JScrollPane(userList);
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>1) {
					String login = userList.getSelectedValue();
					MsgGUI frame = new MsgGUI(client,login);
			        frame.setTitle("Message");
			        frame.setVisible(true);
			        frame.setBounds(10, 10, 500, 630);
			        frame.setResizable(false);
				}
			}
		});
	
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }
    
   public void setLayoutManager()
   {
       container.setLayout(null);
   }
   
   public void setLocationAndSize()
   {
	   settingButton.setBounds(20,10,110,30);
	   logoffButton.setBounds(160,10,110,30);
	   onlineLabel.setBounds(20,40,100,30);
	   scrollPane.setBounds(20,70,250,440);
	   groupLabel.setBounds(20,510,100,30);
	   topicField.setBounds(20,540,160,30);
	   joinButton.setBounds(190,540,80,30);
   }
   
   public void addComponentsToContainer()
   {
       container.add(settingButton);
       container.add(logoffButton);
       container.add(onlineLabel);
       container.add(scrollPane);
       container.add(groupLabel);
       container.add(topicField);
       container.add(joinButton);
   }
   
   public void addActionEvent()
   {
	   settingButton.addActionListener(this);
	   logoffButton.addActionListener(this);
	   joinButton.addActionListener(this);
   }

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == settingButton) {
        	 doUpdate();
         }
         
         if (e.getSource() == logoffButton) {
             	try {
					client.logoff();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
             	JOptionPane.showMessageDialog(this,"Signed off");
             	setVisible(false);
         }
         
         if (e.getSource() == joinButton) {
          	String topic = topicField.getText();
          	if(!topic.isBlank()) {
				try {
					client.join(topic);
					grpMsgGUI frame = new grpMsgGUI(client,topic);
					frame.setTitle("Topic Message");
			        frame.setVisible(true);
			        frame.setBounds(10, 10, 500, 630);
			        frame.setResizable(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
          	}
          	else {
          		JOptionPane.showMessageDialog(this,"Please enter topic name followed by #!");
          	}
         }
    }
    
    private void doUpdate() {
    	updateGUI update=new updateGUI();
		update.initiate();
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