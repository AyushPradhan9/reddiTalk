import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread{
    
    private final int port;
    private ArrayList<serverThreader> threadList = new ArrayList<>();

    public Server(int port){
        this.port = port;
    }

    public List<serverThreader> getThreadList() {
        return threadList;
    }

    @Override
    public void run () {
        try {
            ServerSocket serverSocket = new ServerSocket(port); 
            while(true){
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                serverThreader thread = new serverThreader(this,clientSocket);
                threadList.add(thread);
                thread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

	public void removeThreader(serverThreader serverThreader) {
        threadList.remove(serverThreader);
	}
}