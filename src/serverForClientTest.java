import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class serverForClientTest extends Thread{
	public ServerSocket socket;
	public boolean running = true; 
	public LinkedList<Socket> clientsockets = new LinkedList<Socket>();
	public LinkedList<ObjectInputStream> clientinputs = new LinkedList<ObjectInputStream>();
	public LinkedList<ObjectOutputStream> clientoutputs = new LinkedList<ObjectOutputStream>();;
	
	public serverForClientTest() throws IOException{
		socket = new ServerSocket(3000);
	}
	
	public void run(){
		while(running){
			Socket server = null;
			try {
				server = socket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clientsockets.add(server);
			try {
				clientinputs.add(new ObjectInputStream(server.getInputStream()));
				clientoutputs.add(new ObjectOutputStream(server.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
