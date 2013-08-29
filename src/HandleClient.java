import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class HandleClient extends Thread {
	private Server server;
	private String gameName;
	private Socket socket;
	private String userName;
	private boolean listening;
	public BufferedReader input;
	public PrintWriter output;
	private int EddieTest = 0;

	public HandleClient(Socket socket, Server server) {
		this.server = server;
		//Removed the game from the constructor, thought that at the moment a client
		//connects to server it wont yet know what game it will join/create
		this.socket = socket;

		try {
			input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			this.userName = input.readLine();
			start();

			if (!server.clientsList.contains(userName)) {
				if (EddieTest == 1) {
					JOptionPane.showMessageDialog(null,
							"DOES NOT CONTAIN - HANDLE CLIENT");
				}
				listening = true;
				output.println("LK");
				start();
			} else {
				if (EddieTest == 1) {
					JOptionPane.showMessageDialog(null,
							"CONTAINS  - HANDLE CLIENT");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(int value, String uname, String msg) {
		output.println(uname + " : " + msg);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket Socket) {
		this.socket = Socket;
	}
	

	public String getUserName() {
		return userName;
	}

	public String getGame() {
		return gameName;
	}

	public void setGame(String game) {
		this.gameName = game;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void closeConnection() {
		// close streams and set listening to false;
		listening = false;
	}

	public void run() {
		while (listening) {
			String line;
			try {
				line = input.readLine();
				
				if (EddieTest == 1) {
					JOptionPane.showMessageDialog(null, line);
				}
				
				if(line.equals("GL;")){
					
					//No games are av. so user has to create a game
					if(server.gamesCount == 0){
						//Send "no games available, do u want to start a new game?"
			
						
						
					//User should have options (join or create)
					} else {
						//Client can join a game. Send a list of games 
						for (int x = 0; x<server.gamesCount; x++) {
//							output.println(game.getName(x));
//							HashSet<Game> f = server.gamesList;
							
							
						}	
					}
									
				}
				
				//Create a game for the player
				else if (line.substring(0,2).equals("GS")) {
					if (server.gameNameExists(line.substring(2, line.length()-1))) {
						//send error 120 to client
						System.out.println("Error, game name taken");
					} else {
						gameName = line.substring(2, line.length()-1);
						server.createGame(gameName, userName);
					}
				}
				
				//Allow another player to join the game
				else if (line.equals("GN;")) {
					server.allowAddPlayerToGame(gameName);
					
				}
				
				//Game is full, game will start
				else if (line.equals("GF;")) {
					
				}
				
				//Kick a player out
				else if (line.substring(0,2).equals("GO")) {
					
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
