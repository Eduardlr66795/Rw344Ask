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
	public LinkedList<ObjectOutputStream> clientoutputs = new LinkedList<ObjectOutputStream>();
	Socket server = null;
	
	public serverForClientTest() throws IOException{
		socket = new ServerSocket(3000);
	}
	
	public void run(){
		while(running){
			
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
	public void end() throws IOException{
		int tir = clientsockets.size();
        for(int i = tir-1; i > 0; i--){
        	clientsockets.get(i).close();
        }
        clientsockets.removeAll(clientsockets);
        clientinputs.removeAll(clientinputs);
        clientoutputs.removeAll(clientoutputs);
        socket.close();
	}

}
