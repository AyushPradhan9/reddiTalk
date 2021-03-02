import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class LoginGUI extends JFrame implements ActionListener {
	 Container container=getContentPane();
	 JLabel userLabel=new JLabel("Username:");
	 JLabel passwordLabel=new JLabel("Password:");
	 JTextField loginField=new JTextField();
	 JPasswordField passwordField=new JPasswordField();
	 JButton loginButton=new JButton("Login");
	 JButton signupButton=new JButton("Signup");
	 JCheckBox showPassword=new JCheckBox("Show Password");
	private serverClient client;
	
	public LoginGUI() {
		this.client=new serverClient("localhost",8818);
		client.connect();
		
		setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }
	
	public void setLayoutManager(){
       container.setLayout(null);
	}
   
	public void setLocationAndSize(){
		userLabel.setBounds(50,100,100,30);
		passwordLabel.setBounds(50,150,100,30);
		loginField.setBounds(150,100,150,30);
		passwordField.setBounds(150,150,150,30);
		showPassword.setBounds(150,190,150,30);
		signupButton.setBounds(50,240,100,30);
		loginButton.setBounds(200,240,100,30);
	}
   
	public void addComponentsToContainer(){
		container.add(userLabel);
		container.add(passwordLabel);
		container.add(loginField);
		container.add(passwordField);
		container.add(showPassword);
		container.add(loginButton);
		container.add(signupButton);
	}
	
	public void addActionEvent() {
		System.out.println("here");
		loginButton.addActionListener(this);
		signupButton.addActionListener(this);
		showPassword.addActionListener(this);
	}
		
   public void actionPerformed(ActionEvent e) {
	   
       if (e.getSource() == loginButton) {
           doLogin();
       }
       
       if (e.getSource() == signupButton) {
           doSignup();
       }
       
       	if (e.getSource() == showPassword) {
           	if (showPassword.isSelected()) {
               	passwordField.setEchoChar((char) 0);
           	} else {
           		passwordField.setEchoChar('*');
           	}
       	}
   	}
   
   	private void doSignup() {
		setVisible(false);
		signupGUI signup=new signupGUI();
		signup.initiate();
	}

	protected void doLogin() {
		String login=loginField.getText();
		String password=passwordField.getText();
		try {
			if(client.login(login, password)) {
				userListGUI list=new userListGUI(client);
				JFrame frame=new JFrame("User List");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(300,400);
				frame.getContentPane().add(list, BorderLayout.CENTER);
				frame.setVisible(true);
				
				setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(this, "Invalid login/password");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		LoginGUI frame = new LoginGUI();
        frame.setTitle("ReddiTalk");
        frame.setVisible(true);
        frame.setBounds(10, 10, 370, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
	}
}
