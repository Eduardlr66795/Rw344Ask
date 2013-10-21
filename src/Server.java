import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * @author Team //Ask
 * 
 */
public class Server implements Runnable {
	@SuppressWarnings("rawtypes")
	private Hashtable<Socket,ObjectOutputStream> outputStreams = new Hashtable();
	Hashtable<String, Game> gamesList = new Hashtable<String, Game>();
	private Hashtable<ObjectOutputStream, String> clientList = new Hashtable<ObjectOutputStream, String>();
	@SuppressWarnings("rawtypes")
	private Hashtable Messages = new Hashtable();
	// TODO

	// Gui
	private JTextArea textAreaServer;
	private JTextArea textAreaClient;
	private JTextArea textAreaGame;
	private JPanel panelClient;
	private JPanel panelServer;
	private JPanel panelGame;
	private JFrame frameMain;
	private boolean listening = true;
	private ServerSocket server_Socket; 
	
	public void closeServer(){
		frameMain.dispose();
		listening = false;
		try {
			server_Socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Server(){
		buildGui();
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		new Thread(server).start();
	}

	/**
	 * 
	 */
	public void buildGui() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JTabbedPane tab = new JTabbedPane();
		frameMain = new JFrame();
		frameMain.setTitle("Server Information");
		frameMain.setBackground(Color.DARK_GRAY);
		frameMain.setSize(400, 400);
		frameMain.setLocation(300, 300);

		panelClient = new JPanel();
		panelClient.setSize(frameMain.getWidth(), frameMain.getHeight());
		panelClient.setLayout(null);

		panelServer = new JPanel();
		panelServer.setSize(frameMain.getWidth(), frameMain.getHeight());
		panelServer.setLayout(null);

		panelGame = new JPanel();
		panelGame.setSize(frameMain.getWidth(), frameMain.getHeight());
		panelGame.setLayout(null);

		// TextArea for server
		textAreaServer = new JTextArea();
		textAreaServer.setEditable(false);

		// TextArea for clients
		textAreaClient = new JTextArea();
		textAreaClient.setEditable(false);

		// TextArea for games
		textAreaGame = new JTextArea();
		textAreaGame.setEditable(false);

		// Scrolepane for server
		JScrollPane spMain = new JScrollPane(textAreaServer,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spMain.setSize(frameMain.getWidth(), frameMain.getHeight());
		spMain.setLocation(0, 0);
		panelServer.add(spMain);

		// Scrolepane for clients
		JScrollPane spClient = new JScrollPane(textAreaClient,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spClient.setSize(frameMain.getWidth(), frameMain.getHeight());
		spClient.setLocation(0, 0);
		panelClient.add(spClient);

		// Scrolepane for Game
		JScrollPane spGame = new JScrollPane(textAreaGame,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spGame.setSize(frameMain.getWidth(), frameMain.getHeight());
		spGame.setLocation(0, 0);
		panelGame.add(spGame);

		tab.add("Server", panelServer);
		tab.add("Clients", panelClient);
		tab.add("Games", panelGame);

		frameMain.add(tab, BorderLayout.CENTER);
		frameMain.setVisible(true);
		frameMain.setResizable(false);

		frameMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * 
	 * @param port
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void run() {
		try {
			server_Socket = new ServerSocket(3000);
			textAreaServer.append("Server started. Listening on Port - # "
					+ server_Socket.getLocalPort() + "\n");
			while (listening) {
				Socket server = server_Socket.accept();
				ObjectOutputStream output = new ObjectOutputStream(
						server.getOutputStream());
				output.writeObject("RD;");
				outputStreams.put(server, output);
				HandleClient handleClient = new HandleClient(this, server);
				textAreaServer.append("New client on Port " + server.getPort());
				textAreaServer.append("\n");
			}
		} catch (BindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	/**
	 * Removes a connection from the hashtable
	 * 
	 * @param s
	 */
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

	/**
	 * Sends a message to all clients connected to server
	 * 
	 * @param message
	 * @param s
	 */
	@SuppressWarnings("rawtypes")
	public void sendToAll(String message, Socket s) {
		ObjectOutputStream sender = (ObjectOutputStream) outputStreams.get(s);
		synchronized (outputStreams) {
			for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {
				ObjectOutputStream output = (ObjectOutputStream) e
						.nextElement();
				if (output != sender) {
					try {
						System.out.println(message);
						output.writeObject(message);
						output.flush();
						System.out.println("sent");
					} catch (Exception e2) {
						System.out.println("IO exception in sendtoAll " + e2);
					}
				}
			}
		}
	}

	/**
	 * Sends a message to a specific client
	 * 
	 * @param message
	 * @param username
	 */
	@SuppressWarnings("rawtypes")
	public void sendToOne(String message, String username) {
		ObjectOutputStream output = null;
		synchronized (clientList) {
			for (Enumeration e = clientList.keys(); e.hasMoreElements();) {
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

	/**
	 * Adds a username to the hashtable
	 * 
	 * @param username
	 * @param s
	 */
	public void addUsername(String username, Socket s) {
		synchronized (outputStreams) {
			ObjectOutputStream output = (ObjectOutputStream) outputStreams
					.get(s);
			synchronized (clientList) {
				clientList.put(output, username);
			}
		}
	}

	/**
	 * Removes a username from the hashtable
	 * 
	 * @param s
	 */
	public void removeUsername(Socket s) {
		synchronized (outputStreams) {
			ObjectOutputStream output = (ObjectOutputStream) outputStreams
					.get(s);
			synchronized (clientList) {
				clientList.remove(output);
			}
		}
	}

	/**
	 * Gets a username from a socket
	 * 
	 * @param s
	 * @return
	 */
	public String getUsername(Socket s) {
		ObjectOutputStream output = (ObjectOutputStream) outputStreams.get(s);
		return (String) clientList.get(output);
	}

	/**
	 * Sends a list of all clientList connected to server to all clients
	 * Connected to server
	 */
	@SuppressWarnings("rawtypes")
	public void sendclientList() {
		Collection Users = clientList.values();
		String usernamelist = "!~:;~!";
		Iterator itr = Users.iterator();
		while (itr.hasNext()) {
			usernamelist += itr.next() + "~";
		}
		
		for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {
			ObjectOutputStream output = (ObjectOutputStream) e.nextElement();
			try {
				output.writeObject(usernamelist);
				output.flush();
			} catch (Exception e2) {
				System.out.println("IO exception in send clientList " + e2);
			}
		}
	}

	// END OF AUXILLARY METHODS: //

	// ----------- ///

	// START OF SERVER METHODS: //

	/**
	 * Get Player Number from Player Name in a game
	 * 
	 * @param gameName
	 * @param playerNumber
	 * @return
	 */
	public String getPlayerNameFromNumber(String gameName, int playerNumber) {
		Game game = gamesList.get(gameName);

		for (Entry<String, Integer> i : game.getPlayerList().entrySet()) {
			if (i.getValue() == playerNumber) {
				return i.getKey();
			}
		}
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public String getTimeAndDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm");
		Calendar cal = Calendar.getInstance();
		String dateAndTime = dateFormat.format(cal.getTime());
		return dateAndTime;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param socket
         * @return true if login was a success
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean login(String username, String password, Socket socket) {
		Collection Users = clientList.values();
		ObjectOutputStream o = null;
                boolean success = false;
		o = (ObjectOutputStream) outputStreams.get(socket);
		Iterator itr = Users.iterator();
		while (itr.hasNext()) {
			if (username.equals(itr.next())) {
				try {
					o.writeObject("ER100;");
					o.flush();
					
				} catch (IOException e) {
					System.out.println("Exception in login " + e);
				}
			}
		}
		try {
			clientList.put(o, username);
			LinkedList<String> messages = new LinkedList<String>();
			Messages.put(username, messages);
			o.writeObject("LK;");
			o.flush();
                        success = true;
			textAreaClient.append("New client: " + username);
			textAreaClient.append(" at " + getTimeAndDate());
			textAreaClient.append("\n");
			
		} catch (IOException e) {
			System.out.println("Exception in login " + e);
		}
                return success;
	}

	/**
	 * 
	 * @param socket
	 */
	public void logoff(Socket socket) {
		ObjectOutputStream o = null;
		o = (ObjectOutputStream) outputStreams.get(socket);
		String username = getUsername(socket);
		
		for (Enumeration e = gamesList.keys(); e.hasMoreElements();) {
			String nxt = (String) e.nextElement();
			Game game = gamesList.get(nxt);
			
			if (game.playerList.containsKey(username)) {
				for (Enumeration e2 = game.getPlayerList().keys(); e.hasMoreElements();) {
					String playerName = (String) e2.nextElement();
					for (Entry<ObjectOutputStream, String> c : clientList
                            .entrySet()) {

	                    if (c.getValue().equals(playerName)) {
	                        ObjectOutputStream tmp = c.getKey();
	                        try {
								tmp.writeObject("QP" + username + ";");
								tmp.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
	                        
	                    }
					}
				}
			}
		}
		if (clientList.containsKey(o)) {
			try {
				o.writeObject("LM;");
				o.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				o.writeObject("ER102;");
				o.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param gameName
	 * @param creator
	 * @return
	 */
	public void createGame(String gameName, Socket socket) {

		// handle the creation of a game object and add to the HashSet.
		Game newGame = new Game(gameName, getUsername(socket));
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);

		try {
			if (!gamesList.containsKey(gameName)) {
				// game is unique and was created
                            synchronized (gamesList){
				gamesList.put(gameName, newGame);
                            }

				o.writeObject("GK;");
				o.flush();
				newGame.state = "joining";
				textAreaGame.append("New client: " + gameName);
				textAreaGame.append("\n");

				// Game is not unique, error code sent
			} else {
				o.writeObject("ER120;");
				o.flush();
			}
		} catch (Exception e) {
			System.out.println("Exception in createGame: " + e);
		}

	}

	/**
	 * 
	 * @param gameName
	 * @param socket
	 */
	public void addPlayerToGame(String gameName, Socket socket) {
		try {
			ObjectOutputStream o = (ObjectOutputStream) outputStreams
					.get(socket);
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);

			// If game is full
			if (game.playerCount > 7) {
				o.writeObject("ER130;");
				o.flush();
				return;
			}

			// Let next player to join join
			if (!game.nextPlayersToJoin.isEmpty()) {
				String player = game.nextPlayersToJoin.pop();
				game.playerCount++;

				o.writeObject("GP" + player + ";");
				o.flush();

				game.getPlayerList().put(player, 999);

				// No player to join at moment
			} else {

				o.writeObject("GZ;");
				o.flush();
				return;

			}

		} catch (Exception e) {
			System.out.println("Exception in addPlayerToGame: " + e);
		}
	}

	/**
	 * 
	 * @param gameName
	 * @param socket
	 */
	@SuppressWarnings("rawtypes")
	public void gameIsFull(String gameName, Socket socket) {
		try {
			ObjectOutputStream o = (ObjectOutputStream) outputStreams
					.get(socket);

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}

			Game game = gamesList.get(gameName);

			// Game has too few players
			if (game.playerCount < 3) {

				o.writeObject("ER131;");
				o.flush();

			} else {
				int i = 0;
				for (Enumeration e = game.getPlayerList().keys(); e
						.hasMoreElements();) {
					String tmpplayer = (String) e.nextElement();
					game.getPlayerList().put(tmpplayer, i);
					i++;
				}

				game.readyToStart = true;

				game.deal();
				// Get a random player to start
				int rnd = (int) (Math.random() * (game.playerCount - 1));
				game.state = "bidding";
				game.nextPlayerToBid = getPlayerNameFromNumber(gameName, rnd);
				game.nextPlayerToPlay = game.nextPlayerToBid;
				o.writeObject("GM;");
				o.flush();
				return;
			}
		} catch (Exception e) {
			System.out.println("Exception in gameIsFull " + e);
		}
	}

	/**
	 * Remove a player from a game
	 * 
	 * @param gameName
	 * @param playerName
	 * @param socket
	 */
	public void kickPlayerFromGame(String gameName, String playerName,
			Socket socket) {
		try {
			ObjectOutputStream o = (ObjectOutputStream) outputStreams
					.get(socket);
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			if(clientList.containsKey(o)){
				
			}
			Game game = gamesList.get(gameName);
			if (game.getPlayerList().containsKey(playerName)) {
				game.getPlayerList().remove(playerName);
				game.playerCount--;
				synchronized(clientList){
					for (Entry<ObjectOutputStream, String> i : clientList
							.entrySet()) {
						if (i.getValue().equals(playerName)) {
							ObjectOutputStream o2 = i.getKey();
							o2.writeObject("ER134;");
							o2.flush();
						}
					}
				}
				o.writeObject("GQ;");
				o.flush();

				return;

			} else {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
		} catch (Exception e) {
			System.out.println("Exception in kickPlayerFromGame " + e);
		}
	}

	/**
	 * Get games list as string array for client to choose a game
	 * 
	 * @param socket
	 * @param prefix
	 */
	@SuppressWarnings("rawtypes")
	public void getGamesList(Socket socket, String prefix) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		String gameslist = "GU";
		int count = 0;
		for (Enumeration e = gamesList.keys(); e.hasMoreElements();) {
			if (count > 0) {
				gameslist += ":";
			}
			String nextgame = (String) e.nextElement();
			if (nextgame.startsWith(prefix)) {
				gameslist += nextgame;
				count++;
			}
		}
		gameslist += ";";

		try {
			o.writeObject(gameslist);
			o.flush();
		} catch (Exception e) {
			System.out.println("Exception in getGameslist " + e);
		}

	}

	/**
	 * 
	 * @param socket
	 * @param gameName
	 */
	public void joinGame(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			String playerName = getUsername(socket);

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			game.nextPlayersToJoin.add(playerName);
			o.writeObject("GX;");
			o.flush();
		} catch (Exception e) {
			System.out.println("Exception in joinGame " + e);
		}
	}

	/**
	 * 
	 * @param socket
	 * @param gameName
	 */
	public void waitForGame(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			if (game.readyToStart) {
				o.writeObject("GB;");
				o.flush();
				return;
			} else {
				o.writeObject("GZ;");
				o.flush();
				return;
			}
		} catch (Exception e) {
			System.out.println("Exception in waitForGame " + e);
		}
	}

	/**
	 * 
	 * @param socket
	 * @param gameName
	 */
	@SuppressWarnings("rawtypes")
	public void playersInGame(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			String message = "GC";
			// TODO if all works then remove
			if (game.getPlayerList().isEmpty()) {
				System.out
						.println("DEFENSIVE CODE: Should never get here, started a game with 0 players?");
				o.writeObject(message + ";");
				o.flush();
				return;
			}
			int count = 0;
			for (Enumeration e = game.getPlayerList().keys(); e.hasMoreElements();) {
				if (count != 0) {
					message += ":";
				}
				message += (String) e.nextElement();
				count++;
			}
			o.writeObject(message + ";");
			o.flush();
		} catch (Exception e) {
			System.out.println("Exception in playersInGame " + e);
		}
	}

	/**
	 * 
	 * @param socket
	 * @param gameName
	 */
	// quit game that hasnt started TODO
	@SuppressWarnings("rawtypes")
	public void quitGame(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			game.getPlayerList().remove(getUsername(socket));
			game.playerCount--;
			o.writeObject("QK;");
			o.flush();

			// Send quit message only to other players in game
			for (Enumeration e = game.getPlayerList().keys(); e.hasMoreElements();) {
				String playerName = (String) e.nextElement();
				for (Entry<ObjectOutputStream, String> i : clientList
						.entrySet()) {

					if (i.getValue().equals(playerName)) {
						ObjectOutputStream o2 = i.getKey();
						o2.writeObject("QP" + getUsername(socket) + ";");
						o2.flush();
						
					}
				}
			}
			
			gamesList.remove(gameName);

		} catch (Exception e) {
			System.out.println("Exception in quitGame " + e);
		}
	}

	/**
	 * 
	 * @param socket
	 * @param gameName
	 */
	public void handRequest(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);

			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			// if game is being played and user requests a hand TODO
			if (!game.restingState) {
				String roundnumber = "" + game.round;
				game.lastCardPlayed = "";
				game.cardsPlayedInTrick = 0;
				String playername = getUsername(socket);
				String cards = "";
				for (int i = 0; i < 10; i++) {
					if (!game.playerCards[game.playerList.get(playername)][i]
							.equals("")) {
						cards += game.playerCards[game.getPlayerList()
								.get(playername)][i] + ":";
					}
				}
				String message = "HI" + roundnumber + ":" + cards
						+ game.trumpSuit + ":" + game.nextPlayerToBid + ";";
				o.writeObject(message);
				o.flush();
			}
		} catch (Exception e) {
			System.out.println("Exception in handRequest " + e);
		}
	}

	public void bidRequest(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			String message = "HC";
			String lastbid = game.lastBid;
			if (lastbid.equals("none:none")) {
				return;
			} else {
				message += lastbid + ":" + game.nextPlayerToBid + ";";
				o.writeObject(message);
				o.flush();
			}
		} catch (Exception e) {
			System.out.println("Exception in bidRequest " + e);
		}
	}

	public void bidPlay(Socket socket, String gameName, String bidstring) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}

			if (!game.nextPlayerToBid.equals(getUsername(socket))) {
				o.writeObject("ER901");
				o.flush();

			} else {
				int bid = Integer.parseInt(bidstring);
				if (bid > game.cardsThisHand) {
					o.writeObject("ER140;");
					o.flush();
					return;
				}
				game.playerBids[game.getPlayerList().get(getUsername(socket))] = bid;

				int nextplayernumber = (game.getPlayerList()
						.get(getUsername(socket)) + 1) % game.playerCount;

				game.nextPlayerToBid = getPlayerNameFromNumber(gameName,
						nextplayernumber);
				if (game.nextPlayerToBid.equals(game.nextPlayerToPlay)) {
					game.state = "playing";
				}
				game.lastBid = getUsername(socket) + ":" + bid;

				o.writeObject("HC" + getUsername(socket) + ":" + bid + ":"
						+ game.nextPlayerToBid + ";");
				o.flush();

			}

		} catch (Exception e) {
			System.out.println("Exception in bidPlay " + e);
		}
	}

	public void cardRequest(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}

			Game game = gamesList.get(gameName);

			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}

			String message = "HL";
			if (game.lastCardPlayed.equals("")) {
				return;
			} else {
				message += game.lastCardPlayed + ":";
				message += game.nextPlayerToPlay + ";";

				o.writeObject(message);
				o.flush();
			}

		} catch (Exception e) {
			System.out.println("Exception in cardRequest " + e);
		}
	}

	public void cardPlay(Socket socket, String gameName, String card) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}

			Game game = gamesList.get(gameName);

			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			String playerName = getUsername(socket);
			int playerNumber = game.getPlayerList().get(playerName);

			// Check if player has this card and find position of card
			boolean hasCard = false;
			int cardposition = 900;
			for (int i = 0; i < 10; i++) {
				if (game.playerCards[playerNumber][i].equals(card)) {
					hasCard = true;
					cardposition = i;
				}
			}

			if (!hasCard) {
				o.writeObject("ER141;");
				o.flush();
				return;
			}

			// Check if card is not led suit and player does have led suit in
			// hand
			if (!game.ledSuit.equals(card.substring(0, 1))) {
				for (int i = 0; i < 10; i++) {
					if (!game.playerCards[playerNumber][i].equals("")) {
						if (game.playerCards[playerNumber][i].substring(0, 1)
								.equals(game.ledSuit)) {
							o.writeObject("ER142;");
							o.flush();
							return;
						}
					}
				}
			}

			game.playerCards[playerNumber][cardposition] = "";

			int nextplayernumber = (playerNumber + 1) % game.playerCount;

			game.nextPlayerToPlay = getPlayerNameFromNumber(gameName,
					nextplayernumber);
			game.lastCardPlayed = playerName + ":" + card;

			// calculate current winner of trick
			if (game.cardsPlayedInTrick == 0) {
				game.trickWinner = game.lastCardPlayed;
				game.ledSuit = game.lastCardPlayed.split(":")[1]
						.substring(0, 1);

			} else {
				String oldtrickwinnersuite = game.trickWinner.split(":")[1]
						.substring(0, 1);
				String oldtrickwinnerdigit = game.trickWinner.split(":")[1]
						.substring(1, 2);
				int oldtrickwinnerface = 0;
				String contendersuite = game.lastCardPlayed.split(":")[1]
						.substring(0, 1);
				String contenderdigit = game.lastCardPlayed.split(":")[1]
						.substring(1, 2);
				int contenderface = 0;

				// Convert card to integer
				if (oldtrickwinnerdigit.equals("T")) {
					oldtrickwinnerface = 10;
				} else if (oldtrickwinnerdigit.equals("J")) {
					oldtrickwinnerface = 11;
				} else if (oldtrickwinnerdigit.equals("Q")) {
					oldtrickwinnerface = 12;
				} else if (oldtrickwinnerdigit.equals("K")) {
					oldtrickwinnerface = 13;
				} else if (oldtrickwinnerdigit.equals("A")) {
					oldtrickwinnerface = 14;
				} else {
					oldtrickwinnerface = Integer.parseInt(oldtrickwinnerdigit);
				}

				// Convert card to integer
				if (contenderdigit.equals("T")) {
					contenderface = 10;
				} else if (contenderdigit.equals("J")) {
					contenderface = 11;
				} else if (contenderdigit.equals("Q")) {
					contenderface = 12;
				} else if (contenderdigit.equals("K")) {
					contenderface = 13;
				} else if (contenderdigit.equals("A")) {
					contenderface = 14;
				} else {
					contenderface = Integer.parseInt(contenderdigit);
				}

				if (oldtrickwinnersuite.equals(game.trumpSuit)) {
					if (contendersuite.equals(game.trumpSuit)) {
						if (contenderface > oldtrickwinnerface) {
							game.trickWinner = game.lastCardPlayed;
						} else {
							// oldtrickwinner stays the same
						}
					} else {
						// oldtrickwinner stays the same
					}
				}

				else if (oldtrickwinnersuite.equals(game.ledSuit)) {

					if (contendersuite.equals(game.trumpSuit)) {
						game.trickWinner = game.lastCardPlayed;

					} else if (contendersuite.equals(game.ledSuit)) {
						if (contenderface > oldtrickwinnerface) {
							game.trickWinner = game.lastCardPlayed;
						} else {
							// oldtrickwinner stays the same
						}
					} else {
						// oldtrickwinner stays the same
					}
				}

			}

			game.cardsPlayedInTrick++;

			if (game.cardsPlayedInTrick == game.playerCount) {
				// Add winner to handsWon
				String trickwinner = game.trickWinner.split(":")[0];
				int trickwinnernumber = game.getPlayerList().get(trickwinner);
				game.handsWon[trickwinnernumber]++;
				game.nextPlayerToPlay = trickwinner;

				game.cardsPlayedInTrick = 0;
				game.handsPlayed++;

				game.ledSuit = "";
			}

			if (game.handsPlayed == game.cardsThisHand) {

				// Update scores
				for (int i = 0; i < game.playerCount; i++) {
					int handswon = game.handsWon[i];
					if (handswon == game.playerBids[i]) {
						game.playerScores[i] += handswon + 10;
					} else {
						game.playerScores[i] += handswon;
					}

				}
				game.round++;
				game.nextPlayerToPlay = "";

				game.restingState = true;
				game.state = "resting";

			}

			o.writeObject("HL" + game.lastCardPlayed + ":"
					+ game.nextPlayerToPlay + ";");
			o.flush();
			return;

		} catch (Exception e) {
			System.out.println("Exception in cardPlay " + e);
		}
	}

	public void anotherHand(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}

			Game game = gamesList.get(gameName);

			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			String playerName = getUsername(socket);
			int playerNumber = game.getPlayerList().get(playerName);

			game.playAnotherRound[playerNumber] = true;

			for (int i = 0; i < game.playerCount; i++) {
				if (!game.playAnotherRound[i]) {
					break;
				}
				if ((i == game.playerCount - 1) && (game.restingState)) {
					game.deal();
					// Get a random player to start
					int rnd = (int) (Math.random() * (game.playerCount - 1));
					game.nextPlayerToBid = getPlayerNameFromNumber(gameName,
							rnd);
					game.nextPlayerToPlay = game.nextPlayerToBid;
					game.lastBid = "none:none";
					game.restingState = false;
					return;
				}
			}

			o.writeObject("HW;");
			o.flush();

		} catch (Exception e) {
			System.out.println("Exception in anotherHand " + e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void scoreRequest(Socket socket, String gameName) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {

			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			String message = "HO";
			boolean firstitr = true;
			for (Enumeration e = game.getPlayerList().keys(); e.hasMoreElements();) {
				if (!firstitr) {
					message += ":";
				}
				String playerName = (String) e.nextElement();
				message += playerName;
				message += ":" + game.handsWon[game.getPlayerList().get(playerName)];
				message += ":"
						+ game.playerScores[game.getPlayerList().get(playerName)];
				firstitr = false;
			}
			o.writeObject(message + ";");
			o.flush();
		} catch (Exception e) {
			System.out.println("Exception in scoreRequest " + e);
		}
	}

	/**
	 * 
	 * @param socket
	 * @param arguments
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void chatToPlayers(Socket socket, String[] arguments) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			for (int i = 0; i < arguments.length - 1; i++) {

				String playerName = arguments[i];
				for (Entry<ObjectOutputStream, String> entry : clientList
						.entrySet()) {

					if (entry.getValue().equals(playerName)) {
						LinkedList link = (LinkedList) Messages.get(playerName);
						link.addLast(getUsername(socket) + ":"
								+ arguments[arguments.length - 1]);
					}
				}
			}
			o.writeObject("CK;");
			o.flush();
		} catch (Exception e) {
			System.out.println("Exception in chatToPlayers " + e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void chatToGame(Socket socket, String gameName, String message) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			// if game does not exist
			if (!gamesList.containsKey(gameName)) {
				o.writeObject("ER133;");
				o.flush();
				return;
			}
			Game game = gamesList.get(gameName);
			// if not part of a game
			if (!game.playerList.containsKey(getUsername(socket))) {
				o.writeObject("ER110;");
				o.flush();
				return;
			}
			// Send message only to other players in game
			for (Enumeration e = game.getPlayerList().keys(); e.hasMoreElements();) {
				String playerName = (String) e.nextElement();
				LinkedList link = (LinkedList) Messages.get(playerName);
				link.addLast(getUsername(socket) + ":" + message);
			}
		} catch (Exception e) {
			System.out.println("Exception in chatToPlayers " + e);
		}
	}
	
	public void modeConfusion(Socket socket) {
		String username = getUsername(socket);
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			boolean isInGame = false;
			
			for (Enumeration e = gamesList.keys(); e.hasMoreElements();) {
				String nxt = (String) e.nextElement();
				Game game = gamesList.get(nxt);
				
				if (game.playerList.containsKey(username)) {
					isInGame = true;
					String state = game.state;
					
					if (state.equals("joining")) {
						if (game.creatorName.equals(username)) {
							o.writeObject("MLGW" + game.gameName + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						} else {
							o.writeObject("MLGN" + game.gameName + ":GF" + game.gameName + ":GO" + game.gameName + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						}
					} else if (state.equals("bidding")) {
						if (game.nextPlayerToBid.equals(username)) {
							o.writeObject("MLHN" + game.gameName + ":HB" + game.gameName + ":HD" + game.gameName +":CA" + game.gameName + ":QT" + game.gameName  + ":LO:CP:CC;");
							o.flush();
							return;
						} else {
							o.writeObject("MLHN" + game.gameName + ":HB" + game.gameName +":CA" + game.gameName  + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						}
					} else if (state.equals("playing")) {
						if (game.nextPlayerToPlay.equals(username)) {
							o.writeObject("MLHN" + game.gameName + ":HP" + game.gameName + ":HR" + game.gameName + ":HS" + game.gameName +":CA" + game.gameName + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						} else {
							o.writeObject("MLHN" + game.gameName + ":HP" + game.gameName + ":HS" + game.gameName +":CA" + game.gameName + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						}
						
					} else if (state.equals("resting")) {
						int playerNumber = game.getPlayerList().get(username);
						
						if (game.playAnotherRound[playerNumber]) {
							o.writeObject("MLHN" + game.gameName + ":CA" + game.gameName + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						} else {
							o.writeObject("MLHA" + game.gameName + ":CA" + game.gameName + ":QT" + game.gameName + ":LO:CP:CC;");
							o.flush();
							return;
						}
					}
				}
			}
			
			if (!isInGame) {
				o.writeObject("MLGS:GL:GG:LO:CP:CC;");
				o.flush();
				return;
			}
		} catch (Exception e) {
			System.out.println("Exception in modeConfusion " + e);
		}
		
	}

	/**
	 * 
	 * @param socket
	 */
	@SuppressWarnings("rawtypes")
	public void collectMessages(Socket socket) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			String playerName = getUsername(socket);
			LinkedList link = (LinkedList) Messages.get(playerName);
			if (link.isEmpty()) {
				o.writeObject("CN;");
				o.flush();
			} else {
				o.writeObject("CM"+(String) link.pop());
				o.flush();
			}
		} catch (Exception e) {
			System.out.println("Exception in collectMessages " + e);
		}
	}

	/**
	 * 
	 * @param socket
	 */
	public void badMessageFormat(Socket socket) {
		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
		try {
			o.writeObject("ER900;");
			o.flush();
		} catch (Exception e) {
			System.out.println("Exception in badMessageFomat " + e);
		}
	}
}
