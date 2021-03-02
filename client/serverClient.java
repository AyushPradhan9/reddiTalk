import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class serverClient {
	private String serverName;
	private int serverPort;
	private Socket socket;
	private InputStream serverIn;
	private OutputStream serverOut;
	private BufferedReader bufferIn;
	private ArrayList<statusListener> userStatus= new ArrayList<>();
	private ArrayList<MsgListener> msgListen= new ArrayList<>();
	
	public serverClient(String serverName, int serverPort) {
		this.serverName=serverName;
		this.serverPort=serverPort;
	}
	
	public static void main(String[] args) throws IOException {
		serverClient client = new serverClient("localhost",8818);
		client.addStatusListener(new statusListener() {
			public void online(String login) {
				System.out.println("ONLINE: "+login);
			}
			public void offline(String login) {
				System.out.println("OFFLINE: "+login);
			}
		});
		
		client.addMsgListener(new MsgListener() {
			public void onMessage(String sendTo, String msgBody) {
				System.out.println("Message from: "+sendTo+" - "+msgBody);
			}
		});
		
		if(!client.connect()) {
			System.out.println("Connect failed");
		}
		else {
			System.out.println("Connect successfull");
			if(client.login("ayush","ayush")) {
				System.out.println("Login Successfull");
				client.msg("ayush","hey");
			}
			else {
				System.out.println("Login failed");
			}
			client.logoff();
		}
	}
	
	public void msg(String sendTo, String msgBody) throws IOException {
		String cmd="msg "+sendTo+" "+msgBody+"\n";
		serverOut.write(cmd.getBytes());
	}

	public void logoff() throws IOException {
		String cmd="logoff\n";
		serverOut.write(cmd.getBytes());
	}

	public boolean login(String login,String password) throws IOException {
		String cmd="login "+login+" "+password+"\n";
		serverOut.write(cmd.getBytes());
		
		String response = bufferIn.readLine();
		System.out.println("Response Line:"+response);
		if("Login successful".equalsIgnoreCase(response)) {
			startMsgReader();
			return true;
		}
		else {
			return false;
		}
	}
	
	public String signup(String login,String password) throws IOException {
		String cmd="signup "+login+" "+password+"\n";
		serverOut.write(cmd.getBytes());
		String response = bufferIn.readLine();
		return response;
	}
	
	private void startMsgReader() {
		Thread t = new Thread() {
			public void run() {
				readMsg();
			}
		};
		t.start();
	}
	
	private void readMsg() {
		String line;
		try {
			while((line=bufferIn.readLine())!=null) {
				String[] tokens = StringUtils.split(line);
	            if (tokens != null && tokens.length > 0) {
	                String cmd = tokens[0];
	                if("online".equalsIgnoreCase(cmd)) {
	                	handleOnline(tokens);
	                }
	                else if("offline".equalsIgnoreCase(cmd)){
	                	handleOffline(tokens);
	                }
	                else if("msg".equalsIgnoreCase(cmd)) {
	                	String[] tokensMsg = StringUtils.split(line,null,3);
	                	handleMessage(tokensMsg);
	                }
	            }
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void handleMessage(String[] tokens) {
		String login=tokens[1];
		String msgBody=tokens[2];
		for(MsgListener listener: msgListen) {
			listener.onMessage(login, msgBody);
		}
	}

	private void handleOffline(String[] tokens) {
		String login = tokens[1];
		for(statusListener listener : userStatus) {
			listener.offline(login);
		}
	}

	private void handleOnline(String[] tokens) {
		String login = tokens[1];
		for(statusListener listener : userStatus) {
			listener.online(login);
		}
	}

	public boolean connect() {
		try {
			this.socket = new Socket(serverName,serverPort);
			System.out.println("Client port : "+socket.getLocalPort());
			this.serverOut = socket.getOutputStream();
			this.serverIn = socket.getInputStream();
			this.bufferIn = new BufferedReader(new InputStreamReader(serverIn));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addStatusListener(statusListener listener) {
		userStatus.add(listener);
	}
	
	public void removeStatusListener(statusListener listener) {
		userStatus.remove(listener);
	}
	
	public void addMsgListener(MsgListener listener) {
		msgListen.add(listener);
	}
	
	public void removeMsgListener(MsgListener listener) {
		msgListen.remove(listener);
	}
}
