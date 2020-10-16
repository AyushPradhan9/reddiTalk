import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class serverThreader extends Thread {

    public final Socket clientSocket;
    public final Server server;
    private String login = null;
    private OutputStream outputStream;
    private HashSet<String> topicSet = new HashSet<>();

    public serverThreader(Server server,Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleClientSocket() throws Exception {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equalsIgnoreCase(cmd) || "exit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                }
                
                else if("signup".equalsIgnoreCase(cmd)) {
                	handleSignup(outputStream, tokens);
                }
                
                else if("update".equalsIgnoreCase(cmd)) {
                	handleUpdate(outputStream, tokens);
                }
                
                else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                }
                
                else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line,null,3);
                    handleMessage(tokensMsg);
                }
                else if("join".equalsIgnoreCase(cmd)) {
                	handleJoin(tokens);
                }
                else {
                    String msg = "Unknown Command: " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }
    
    public boolean isMemberOfTopic(String topic) {
    	return topicSet.contains(topic);
    }

    private void handleJoin(String[] tokens) {
		if(tokens.length>2) {
			String topic = tokens[1];
			topicSet.add(topic);
		}
		
	}

	private void handleUpdate(OutputStream outputStream, String[] tokens) {
		
		
	}

	private void handleSignup(OutputStream outputStream,String[] tokens) throws IOException {
		if(tokens.length==3) {
			String name = tokens[1];
	        String password = tokens[2];
	        String msg;
	        serverDatabase data = new serverDatabase();
	            
	        if(data.userExist(name,password)==1) {
	        	msg = "Signup successful\n";
	            outputStream.write(msg.getBytes());
	        }
	        else if(data.userExist(name, password)==0) {
	        	msg = "User already exist. Try logging in.\n";
	        	outputStream.write(msg.getBytes());
	        }
		}
	}

	private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];
        boolean isTopic = (sendTo.charAt(0) == '#') ;

        List<serverThreader> threadList = server.getThreadList();
        for(serverThreader thread : threadList) {
        	if(isTopic) {
        		System.out.println("2");
        		if(thread.isMemberOfTopic(sendTo)) {
        			System.out.println("1");
        			String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
	                thread.send(outMsg);
        		}
        	}
        	else {
	            if (sendTo.equalsIgnoreCase(thread.getLogin())) {
	                String outMsg = "msg " + login + " " + body + "\n";
	                thread.send(outMsg);
	            }
        	}
        }
    }

    private void handleLogoff() throws IOException {
        server.removeThreader(this);
        List<serverThreader> threadList = server.getThreadList();
        String onlineMsg = "Offline " + login + "\n";
        for(serverThreader threader : threadList) {
            if (!login.equals(threader.getLogin())) {
                threader.send(onlineMsg);
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    } 

    private void send(String online) throws IOException {
        if (login != null){
            outputStream.write(online.getBytes()); 
        }
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];
            serverDatabase data = new serverDatabase();

            if (data.userLogin(login,password)==1) {
                String msg = "Login successful\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("Welcome " + login);
                
                List<serverThreader> threadList = server.getThreadList();
                
                // send current user all other online logins
                for(serverThreader threader : threadList) {
                    if (threader.getLogin() != null) {
                        if (!login.equals(threader.getLogin())) {
                            String online = "Online " + threader.getLogin();
                            send(online);
                        }
                    }
                } 
                // send other online users current user's status
                String onlineMsg = "Online " + login + "\n";
                for(serverThreader threader : threadList) {
                    if (!login.equals(threader.getLogin())) {
                        threader.send(onlineMsg);
                    }
                }
            }
            else{
                String msg = "Error login\n";
                outputStream.write(msg.getBytes());
            }
        }
    }
}