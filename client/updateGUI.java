import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class updateGUI extends JFrame implements ActionListener {

    Container container=getContentPane();
    private JLabel updateLabel=new JLabel("Update Password:");
    private JLabel userLabel=new JLabel("Username:");
    private JLabel oldLabel=new JLabel("Old Password:");
    private JLabel newLabel=new JLabel("New Password:");
    private JTextField usernameField=new JTextField();
    private JPasswordField oldField=new JPasswordField();  
    private JPasswordField newField=new JPasswordField();
    private JButton signupButton=new JButton("Update");
    private JCheckBox showPassword=new JCheckBox("Show Password");
    private serverClient client;

    updateGUI()
    {
    	this.client=new serverClient("localhost",8818);
		client.connect();
		
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
	   updateLabel.setBounds(50,10,150,30);
       userLabel.setBounds(50,50,100,30);
       oldLabel.setBounds(50,90,100,30);
       newLabel.setBounds(50,140,100,30);
       usernameField.setBounds(150,50,150,30);
       oldField.setBounds(150,90,150,30);
       newField.setBounds(150, 140, 150, 30);
       showPassword.setBounds(150,180,150,30);
       signupButton.setBounds(125,230,100,30);
   }
   
   public void addComponentsToContainer()
   {
	   container.add(updateLabel);
       container.add(userLabel);
       container.add(oldLabel);
       container.add(newLabel);
       container.add(usernameField);
       container.add(oldField);
       container.add(newField);
       container.add(showPassword);
       container.add(signupButton);
   }
   
   public void addActionEvent()
   {
       signupButton.addActionListener(this);
       showPassword.addActionListener(this);
   }

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == signupButton) {
        	 String login=usernameField.getText();
     		 String oldPass=oldField.getText();
     		 String newPass=newField.getText();
     		 try {
				if(client.login(login, oldPass)) {
					 try {
						String response=client.update(login, oldPass, newPass);
						JOptionPane.showMessageDialog(this, response);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				 }
				 else {
					JOptionPane.showMessageDialog(this, "Current password is incorrect. Try again.");
				 }
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
         }
         
         if (e.getSource() == showPassword) {
             	if (showPassword.isSelected()) {
                 	oldField.setEchoChar((char) 0);
                 	newField.setEchoChar((char) 0);;
             	} else {
             		oldField.setEchoChar('*');
             		newField.setEchoChar('*');
             	}
         }
    }
    
    public void initiate(){
    	updateGUI frame=new updateGUI();
        frame.setTitle("Setting");
        frame.setVisible(true);
        frame.setBounds(10,10,370,330);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
}