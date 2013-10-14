import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {

	private ServerSocket ss;

	private Hashtable outputStreams = new Hashtable(); //socket, outputstream
	Hashtable<String, Game> gamesList = new Hashtable<String, Game>(); //Gamename, game
	private Hashtable <ObjectOutputStream,String> clientList = new Hashtable <ObjectOutputStream,String>();     //outputstream, username
	
	private final int MAX_PLAYERS = 7;

	public Server (int port) throws IOException{

		listen(port);
	}

	public static void main (String [] args) throws Exception{

		int port = 3000;

		Server server = new Server(port);

	}

	private void listen(int port) throws IOException{

		ServerSocket ss = new ServerSocket(port);

		System.out.println("Server started. Listening on " + ss);

		// Keep listening on port and create a new thread for each socket accepted
		while (true) {

			Socket s = ss.accept();
			
			System.out.println("New connection from socket: "+ s);
			
			ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
			output.writeObject("RD;");

			outputStreams.put(s, output);
			
			new HandleClient(this, s);
		}



	}

	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	// Removes a connection from the hashtable
	public void removeConnection(Socket s) {
		synchronized (outputStreams) {
		
			System.out.println("Removing connection to " + s);
		// Remove it from the hashtable
			outputStreams.remove(s);
			try {
				s.close();
			} catch (IOException ie) {
				System.out.println("Error closing " + s);
				ie.printStackTrace();
			}
		}
	}

	//Sends a message to all clients connected to server
	public void sendToAll(String message, Socket s) {

		ObjectOutputStream sender = (ObjectOutputStream) outputStreams.get(s);

		synchronized (outputStreams) {

			for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {

				ObjectOutputStream output = (ObjectOutputStream)e.nextElement();
				if (output != sender) {

					try {
						output.writeObject(message);
						output.flush();
					} catch (Exception e2) { 
						System.out.println("IO exception in sendtoAll " + e2); 
					}
				}
			}
		}
	}

	//Sends a message to a specific client
	public void sendToOne(String message, String username) {

		//totest
		ObjectOutputStream output = null;

		synchronized (clientList) {
			for (Enumeration e = clientList.keys(); e.hasMoreElements(); ) {
				ObjectOutputStream o = (ObjectOutputStream) e.nextElement();
				String user = (String) clientList.get(o);
				if (user.equals(username)) {
					output = o;
					break;
				}
			}
		}
		try {
			output.writeObject(message);
			output.flush();
		} catch (Exception e) { 
			System.out.println("Exception in sendtoOne " + e); 
		}
	}

	// Adds a username to the hashtable
	public void addUsername(String username, Socket s) {

		synchronized (outputStreams) {
		
			ObjectOutputStream output  = (ObjectOutputStream)outputStreams.get(s);	
	
			synchronized (clientList) {

				clientList.put(output, username);

			}
		}
	
	}

	// Removes a username from the hashtable
	public void removeUsername(Socket s) {

		synchronized (outputStreams) {
		
			ObjectOutputStream output  = (ObjectOutputStream)outputStreams.get(s);
		
			synchronized (clientList) {

				clientList.remove(output);

			}
		}
	
	}

	// Gets a username from a socket
	public String getUsername(Socket s) {

		ObjectOutputStream output  = (ObjectOutputStream)outputStreams.get(s);

		return (String) clientList.get(output);

	}

	//Sends a list of all clientList connected to server to all clients connected to server
	public void sendclientList() {

		Collection Users = clientList.values();
		String usernamelist = ".COMMAND_WHOSINTHEROOOM";
		Iterator itr = Users.iterator();
		while (itr.hasNext()) {
			usernamelist += " " + itr.next();
		}

		for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {

			ObjectOutputStream output = (ObjectOutputStream)e.nextElement();
			

			try {
				output.writeObject(usernamelist);
				output.flush();
				
			} catch (Exception e2) { 
				System.out.println("IO exception in send clientList " + e2); 
			}
			
		}
	}
	
	
	
// END OF OLD METHODS: //
	
	
	
	
	
	// ----------- ///
	
	
	
	
	
	
// START OF NEW METHODS: //
	
	public void login(String username, String password, Socket socket) {
		
		Collection Users = clientList.values();
		ObjectOutputStream o = null;
		
		try {
			o = (ObjectOutputStream) socket.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Exception in login " + e1);
		}
		
		Iterator itr = Users.iterator();
		
		while (itr.hasNext()) {
			if (username.equals(itr.next())) {
				try {
					o.writeObject("ER100;");
					o.flush();
					return;
				} catch (IOException e) {
					
					System.out.println("Exception in login " + e);
				}
			}
		}
		
		try {
			
			clientList.put(o, username);
			
			o.writeObject("LK;");
			o.flush();
			return;
		} catch (IOException e) {
			
			System.out.println("Exception in login " + e);
		}
		
	}
	
	public String getTimeAndDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm");
		Calendar cal = Calendar.getInstance();
		String dateAndTime = dateFormat.format(cal.getTime());
		return dateAndTime;
	}

	
	/**
	 * 
	 * @param gameName
	 * @param creator
	 * @return
	 */
	public Game createGame(String gameName, String creator, HandleClient client) {
		// handle the creation of a game object and add to the HashSet.
		Game newGame = new Game(gameName, creator, client ,this);

		// game is unique and was created
		gamesList.put(gameName, newGame);
		//removed gamesCount variable, replaced with method call of 
                //getGamesCount() Directly related to the HashTable		
		return newGame;
		
	}
	
	//Checking if game name exists
	public boolean gameNameExists(String gameName) {
		if (gamesList.contains(gameName)) {
			return true;
		}
		
		return false;
	}
	
	//Remove a player from a game, return false if player not part of game TODO
	public boolean kickPlayerFromGame(String gameName, String playerName) {
		Game game = gamesList.get(gameName);
		
		if (game.containsPlayer(playerName)) {
			game.removePlayer(playerName);
		} else {
			return false;
		}
		return true;
	}
	
	//Start the game TODO
	public boolean startGame(String gameName) {
		return true;
	}
	
	public int getTotalPlayers(String gameName) {
		Game game  = gamesList.get(gameName);
		
		return game.amountOfPlayers();
	}
	
	//This is for the GN command, and because its synchronous
        //EDIT: Shouldnt return boolean if game is full or not
	public void allowAddPlayerToGame(String gameName) {
            Game game = gamesList.get(gameName);
            if (!game.hasNewPlayer()) {
            	game.setAddAnotherPlayer(true);
            } else {
            	game.addPlayerToGame();
            }
            gamesList.put(gameName, game);
            
	}
        
    public boolean gameIsFull(String gameName){
        return (gamesList.get(gameName).amountOfPlayers() <= MAX_PLAYERS);
    }
    
    public String getPlayerAdded(String gameName){
    	return gamesList.get(gameName).getAddedPlayer();
    }
    
    public boolean hasNewPlayer(String gameName){
    	return gamesList.get(gameName).hasNewPlayer();
    }
    
	
	//Adding a player to game
        //Not using this anymore, protocol changed
	/*public boolean addPlayerToGame(String gameName, String playerName) {
		Game game = gamesList.get(gameName);
		game.playerList.put(playerName, game.playerCount);
		game.playerCount++;
		game.addAnotherPlayer = false;
		gamesList.put(gameName, game);
		return true;
	}*/
	
	public boolean joinGame(String gameName, String playerName) {
		return (gamesList.get(gameName).addPlayer(playerName));
	}
	
	//Get games list as string array for client to choose a game
    String[] getGamesList() {
        String[] stringGamesList;
        int length = gamesList.size();
        int counter = 0;
        stringGamesList = new String[length];
        Iterator<String> itr = gamesList.keySet().iterator();
        while (itr.hasNext()) {
            stringGamesList[counter] = itr.next();
            counter++;
        }
        return stringGamesList;
    }


}


