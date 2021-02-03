import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class serverThreader extends Thread {

    public final Socket clientSocket;
    public final Server server;
    private String login = null;
    private OutputStream outputStream;
    private serverDatabase data = new serverDatabase();

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
                	handleJoin(outputStream, tokens);
                }
                else if("leave".equalsIgnoreCase(cmd)) {
                	handleLeave(outputStream, tokens);
                }
                else {
                    String msg = "Unknown Command: " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }
    
    private void handleLeave(OutputStream outputStream, String[] tokens) throws IOException {
		String msg;
		if(tokens.length==2 && data.userExist(login)) {
			String topic = tokens[1];
			if(data.topicExist(topic)) {
				data.deleteTopicUser(login,topic);
				msg="Successfully left "+topic+'\n';
				outputStream.write(msg.getBytes());
			}
			else {
				msg="You are not joined in "+topic+'\n';
				outputStream.write(msg.getBytes());
			}
		}
	}

    private void handleJoin(OutputStream outputStream,String[] tokens) throws IOException {
    	String msg;
		if(tokens.length==2 && data.userExist(login)) {
			String topic = tokens[1];
			
			if(!data.topicExist(topic)) {
				data.topicSignup(topic);
				data.setTopicUser(login, topic);
				
				msg = "Opened new topic!\nWelcome to "+topic+"\n";
	            outputStream.write(msg.getBytes());
			}
			
			else if(data.topicExist(topic) && !data.checkTopicUser(login,topic)) {
				data.setTopicUser(login, topic);
				
				msg = "Joined existing topic!\nWelcome to "+topic+"\n";
				outputStream.write(msg.getBytes());
			}
			
			else if(data.topicExist(topic) && data.checkTopicUser(login,topic)) {
				
				msg = "Welcome back to "+topic+"\n";
				outputStream.write(msg.getBytes());
			}
		}
		else if(!data.userExist(login)) {
			msg = "Try logining before joining a topic!\n";
			outputStream.write(msg.getBytes());
		}
	}

	private void handleUpdate(OutputStream outputStream, String[] tokens) throws IOException {
		String msg;
		if(tokens.length==4) {
			String name=tokens[1];
			String pass=tokens[2];
			String newpass=tokens[3];
			if(data.userExist(name) && data.userExist(login)) {
				data.userUpdate(name, pass, newpass);
				msg="Successsfully changed to new password\n";
				outputStream.write(msg.getBytes());
			}
			else if(!data.userExist(name) || login!=name) {
				msg="Matching user not found!\nTry again.\n";
				outputStream.write(msg.getBytes());
			}
		}
		else if(!data.userExist(login)) {
			msg = "Try logining before updating password!\n";
			outputStream.write(msg.getBytes());
		}
	}

	private void handleSignup(OutputStream outputStream,String[] tokens) throws IOException {
		if(tokens.length==3) {
			String name = tokens[1];
	        String password = tokens[2];
	        String msg;
	            
	        if(!data.userExist(name) && login==null) {
	        	data.userSignup(name, password);
	        	msg = "Signup successful\n";
	            outputStream.write(msg.getBytes());
	        }
	        else if(!data.userExist(name) && login!=null) {
	        	msg = "Logoff from your previous account to make a new one!\n";
	            outputStream.write(msg.getBytes());
	        }
	        else if(data.userExist(name)) {
	        	msg = "User already exist! Try logging in.\n";
	        	outputStream.write(msg.getBytes());
	        }
		}
	}

	private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];
        boolean isTopic = (sendTo.charAt(0) == '#') ;
        
        if(login!=null && data.checkOnline(login)) {
	        List<serverThreader> threadList = server.getThreadList();
	        for(serverThreader thread : threadList) {
	        	
	        	if(isTopic && data.checkTopicUser(login,sendTo)) { 
	        		data.sendTopicMess(login, sendTo, body);
	        		String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
		            thread.send(outMsg);
	        	}
	        	
	        	else if (sendTo.equalsIgnoreCase(thread.getLogin())) {
		            data.sendDirectMess(login, sendTo, body);
		            String outMsg = "msg " + login + " " + body + "\n";
		            thread.send(outMsg);
		        }
	        }
        }
        else {
			String msg = "Try logining before sending a message!\n";
			outputStream.write(msg.getBytes());
		}
    }

    private void handleLogoff() throws IOException {
    	data.userOffline(login);
        server.removeThreader(this);
        List<serverThreader> threadList = server.getThreadList();
        String offlineMsg = "Offline " + login + "\n";
        for(serverThreader threader : threadList) {
            if (!data.checkOnline(login)) {
                threader.send(offlineMsg);
            }
        }
        login=null;
        clientSocket.close();
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];
            serverDatabase data = new serverDatabase();
            
            if (data.userLogin(login,password) && !data.checkOnline(login) && this.login==null) {
            	data.userOnline(login);
                String msg = "Login successful\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("Welcome " + login);
                
                List<serverThreader> threadList = server.getThreadList();
                
                // send current user all other online logins
                for(serverThreader threader : threadList) {
                    if (threader.getLogin() != null) {
                        if (!login.equals(threader.getLogin())) {
                            String online = "Online " + threader.getLogin() + "\n";
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
            else if (data.userLogin(login,password) && data.checkOnline(login)) {
            	String msg = "Logging off from previous session!\nPlease try logging in once again\n";
                outputStream.write(msg.getBytes());
                data.userOffline(login);
            }
            else{
                String msg = "No such user found!\n";
                outputStream.write(msg.getBytes());
            }
        }
    }
    
    public String getLogin() {
        return login;
    } 

    private void send(String online) throws IOException {
        if (login != null){
            outputStream.write(online.getBytes()); 
        }
    }
}