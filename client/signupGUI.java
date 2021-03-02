import java.awt.Container;
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

public class signupGUI extends JFrame implements ActionListener {

    Container container=getContentPane();
    JLabel userLabel=new JLabel("Username:");
    JLabel passwordLabel=new JLabel("Password:");
    JLabel confirmLabel=new JLabel("Confirm:");
    JTextField usernameField=new JTextField();
    JPasswordField passwordField=new JPasswordField();  
    JPasswordField confirmField=new JPasswordField();
    JButton signupButton=new JButton("Signup");
    JCheckBox showPassword=new JCheckBox("Show Password");
    private serverClient client;


    signupGUI()
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
       userLabel.setBounds(50,100,100,30);
       passwordLabel.setBounds(50,150,100,30);
       confirmLabel.setBounds(50,200,100,30);
       usernameField.setBounds(150,100,150,30);
       passwordField.setBounds(150,150,150,30);
       confirmField.setBounds(150, 200, 150, 30);
       showPassword.setBounds(150,240,150,30);
       signupButton.setBounds(125,290,100,30);


   }
   public void addComponentsToContainer()
   {
       container.add(userLabel);
       container.add(passwordLabel);
       container.add(confirmLabel);
       container.add(usernameField);
       container.add(passwordField);
       container.add(confirmField);
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
     		 String password=passwordField.getText();
     		 String confirm=passwordField.getText();
     		 if(password.compareTo(confirm)==0) {
     			 try {
					String response=client.signup(login, password);
					JOptionPane.showMessageDialog(this, response);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
     		 }
     		 else {
     			JOptionPane.showMessageDialog(this, "Entered passwords doesn't match. Try again.");
     		 }
         }
         
         if (e.getSource() == showPassword) {
             	if (showPassword.isSelected()) {
                 	passwordField.setEchoChar((char) 0);
                 	confirmField.setEchoChar((char) 0);;
             	} else {
             		passwordField.setEchoChar('*');
             		confirmField.setEchoChar('*');
             	}
         }
    }
    public void initiate(){
    	signupGUI frame=new signupGUI();
        frame.setTitle("Create Account");
        frame.setVisible(true);
        frame.setBounds(10,10,370,450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }
}