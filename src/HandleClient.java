import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

public class HandleClient extends Thread {
	private Server server;
	private Game game;
	private Socket Soket;
	private String userName;
	private boolean listening;
	public BufferedReader input;
	public PrintWriter output;
	private int EddieTest = 0;

	public HandleClient(Socket Soket, Game game, Server server) {
		this.server = server;
		this.game = game;
		this.Soket = Soket;

		try {
			input = new BufferedReader(new InputStreamReader(
					Soket.getInputStream()));
			output = new PrintWriter(Soket.getOutputStream(), true);
			this.userName = input.readLine();

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

	public Socket getSoket() {
		return Soket;
	}

	public void setSoket(Socket Soket) {
		this.Soket = Soket;
	}
	

	public String getUserName() {
		return userName;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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
				
				if(line.equals("GameR")){
					
					//No games are av. so user has to create a game
					if(game.numberOfGames == 0){
						//Send new game
						
						
						
					//User should have options (join or create)
					} else {
						//Client can join a game. Send a list of games 
						for (int x =0; x<game.numberOfGames; x++) {
//							output.println(game.getName(x));
//							HashSet<Game> f = server.gamesList;
							
							
						}
						
					}
					
					
					
				}
				
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
