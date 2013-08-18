import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class Server {
	private ServerSocket server;
	private Socket Soket;
	private int port = 9119;
	
	Vector<HandleClient> clientsList = new Vector<HandleClient>();
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Server(){
		try {
			server = new ServerSocket(port);
			Soket = server.accept();
			// Handle clients in game
			
			HandleClient client = new HandleClient(Soket, null, this);
			clientsList.add(client);
			
			Thread thread = new Thread();
			thread.start();
			
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
