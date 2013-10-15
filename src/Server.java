import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * @author 
 *
 */
public class Server implements ActionListener {

	private ServerSocket ss;
	private Hashtable outputStreams = new Hashtable(); // socket, outputstream
	Hashtable<String, Game> gamesList = new Hashtable<String, Game>(); // Gamename,														
	private Hashtable<ObjectOutputStream, String> clientList = new Hashtable<ObjectOutputStream, String>(); // outputstream,
	private final int MAX_PLAYERS = 7;

	
	
	// Gui
		private JFrame frameMain;
		private JTextArea textAreaServer;
		private JTextArea textAreaClient;
		private JTextArea textAreaGame;
		private JPanel panelClient;
		private JPanel panelServer;
		private JPanel panelGame;
		private JTextField textField;
		private JButton button;
		public int EddieTest = 1;
		
	

	public static void main(String[] args) throws Exception {
		int port = 9119;
		Server server = new Server(port);
	}
	
	
	public Server(int port) throws IOException {
//		buildGui();
		listen(port);
	}

	
	
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
		textAreaServer.setSize(frameMain.getWidth(), frameMain.getHeight());

		// TextArea for clients
		textAreaClient = new JTextArea();
		textAreaClient.setEditable(false);
		textAreaClient.setSize(frameMain.getWidth(), frameMain.getHeight());

		// TextArea for games
		textAreaGame = new JTextArea();
		textAreaGame.setEditable(false);
		textAreaGame.setSize(frameMain.getWidth(), frameMain.getHeight());

		// Scrolepane for server
		JScrollPane spMain = new JScrollPane(textAreaServer,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spMain.setSize(frameMain.getWidth(), frameMain.getHeight());
		spMain.setLocation(0, 0);
		panelServer.add(spMain);

		// Scrolepane for clients
		JScrollPane spClient = new JScrollPane(textAreaClient,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spClient.setSize(frameMain.getWidth(), frameMain.getHeight());
		spClient.setLocation(0, 0);
		panelClient.add(spClient);

		// Scrolepane for Game
		JScrollPane spGame = new JScrollPane(textAreaGame,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spGame.setSize(frameMain.getWidth(), frameMain.getHeight());
		spGame.setLocation(0, 0);
		panelGame.add(spGame);

		tab.add("Server", panelServer);
		tab.add("Clients", panelClient);
		tab.add("Games", panelGame);

		if (EddieTest == 1) {
			textField = new JTextField();
			textField.setBounds(200, 200, 100, 70);
			button = new JButton("TEST");
			button.addActionListener(this);
			frameMain.add(textField, BorderLayout.SOUTH);
			frameMain.add(button, BorderLayout.WEST);
		}

		frameMain.add(tab, BorderLayout.CENTER);
		frameMain.setVisible(true);
		frameMain.setResizable(false);
		frameMain.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		frameMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
//				quit();
			}
		});
	}


	
	@SuppressWarnings("unchecked")
	private void listen(int port) throws IOException {
		ServerSocket server_Socket = new ServerSocket(port);
		System.out.println("Server started. Listening on " + server_Socket);

		while (true) {
			Socket server = server_Socket.accept();
			System.out.println("New connection from socket: " + server);
			ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
			
			output.writeObject("RD");
			
			outputStreams.put(server, output);
			new HandleClient(this, server);
		}
	}

	
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	/**
	 * Removes a connection from the hashtable
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

	
	// Sends a message to all clients connected to server
	public void sendToAll(String message, Socket s) {
		ObjectOutputStream sender = (ObjectOutputStream) outputStreams.get(s);
		synchronized (outputStreams) {
			for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {
				ObjectOutputStream output = (ObjectOutputStream) e
						.nextElement();
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
	
	
	// Sends a message to a specific client
	public void sendToOne(String message, String username) {
		// totest
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

	
	// Adds a username to the hashtable
	public void addUsername(String username, Socket s) {
		synchronized (outputStreams) {
			ObjectOutputStream output = (ObjectOutputStream) outputStreams.get(s);
			synchronized (clientList) {
				clientList.put(output, username);
			}
		}
	}

	
	// Removes a username from the hashtable
	public void removeUsername(Socket s) {
		synchronized (outputStreams) {
			ObjectOutputStream output = (ObjectOutputStream) outputStreams.get(s);
			synchronized (clientList) {
				clientList.remove(output);
			}
		}
	}
	
	
	// Gets a username from a socket
	public String getUsername(Socket s) {
		ObjectOutputStream output = (ObjectOutputStream) outputStreams.get(s);
		return (String) clientList.get(output);
	}

	// Sends a list of all clientList connected to server to all clients
	// connected to server
	public void sendclientList() {
		Collection Users = clientList.values();
		String usernamelist = ".COMMAND_WHOSINTHEROOOM";
		Iterator itr = Users.iterator();
		while (itr.hasNext()) {
			usernamelist += " " + itr.next();
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

	// END OF OLD METHODS: //

	// ----------- ///

	// START OF NEW METHODS: //

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
	public void createGame(String gameName, Socket client) {
		// handle the creation of a game object and add to the HashSet.
		Game newGame = new Game(gameName, client, this, MAX_PLAYERS);
		ObjectOutputStream o = null
				;
		o = (ObjectOutputStream) outputStreams.get(client);
		if (gamesList.contains(gameName)) {
			// game is unique and was created
			gamesList.put(gameName, newGame);
			try {
				o.writeObject("GK");
				o.flush();
			} catch (IOException e) {
				System.out.println("Error in createGame " + e);
			}
		} else {
			// send error message to client
			try {
				o.writeObject("ER120");
				o.flush();
			} catch (IOException e) {
				System.out.println("Error in createGame " + e);
			}
		}
	}

	// Remove a player from a game, return false if player not part of game TODO
	public boolean kickPlayerFromGame(String gameName, String playerName) {
		Game game = gamesList.get(gameName);
		if (game.containsPlayer(playerName)) {
			game.removePlayer(playerName);
		} else {
			return false;
		}
		return true;
	}

	
	// Start the game TODO
	public boolean startGame(String gameName) {
		return true;
	}

	
	public int getTotalPlayers(String gameName) {
		Game game = gamesList.get(gameName);
		return game.amountOfPlayers();
	}

	// This is for the GN command, and because its synchronous
	// EDIT: Shouldnt return boolean if game is full or not
	public void allowAddPlayerToGame(String gameName, Socket socket) {
		Game game = gamesList.get(gameName);
		if (!game.hasNewPlayer()) {
			if (!game.gameIsFull()) {
				game.setAddAnotherPlayer(true);
			} else {
				ObjectOutputStream o = null;
				o = (ObjectOutputStream) outputStreams.get(socket);
				try {
					o.writeObject("ER130");// game is full.
					o.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			game.addPlayerToGame();
		}
		gamesList.put(gameName, game);
	}
;
	
	public ObjectOutputStream getPlayerAdded(String gameName) {
		return gamesList.get(gameName).getAddedPlayer();
	}

	
	public boolean hasNewPlayer(String gameName) {
		return gamesList.get(gameName).hasNewPlayer();
	}

	
	// Adding a player to game
	// Not using this anymore, protocol changed
	/*
	 * public boolean addPlayerToGame(String gameName, String playerName) { Game
	 * game = gamesList.get(gameName); game.playerList.put(playerName,
	 * game.playerCount); game.playerCount++; game.addAnotherPlayer = false;
	 * gamesList.put(gameName, game); return true; }
	 */
	public boolean joinGame(String gameName, String playerName) {
		return (gamesList.get(gameName).addPlayer(playerName));
	}
	// Get games list as string array for client to choose a game
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

	public void logoff(Socket socket) {
		ObjectOutputStream o = null;
		o = (ObjectOutputStream) outputStreams.get(socket);
		if (clientList.contains(o)) {
			try {
				o.writeObject("LM");
				o.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			removeUsername(socket);
			removeConnection(socket);
		} else {
			try {
				o.writeObject("ER102");
				o.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void login(String username, String password, Socket socket) {
		Collection Users = clientList.values();
		ObjectOutputStream o = null;
		o = (ObjectOutputStream) outputStreams.get(socket);
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
			o.writeObject("LK");
			o.flush();
			return;
		} catch (IOException e) {
			System.out.println("Exception in login " + e);
		}
	}

	public String getName(ObjectOutputStream o) {
		return clientList.get(o);
	}

	
	public void gameIsFull(String gameName, Socket socket) {
		ObjectOutputStream o = null;
		o = (ObjectOutputStream) outputStreams.get(socket);
		if (gamesList.get(gameName).amountOfPlayers() == MAX_PLAYERS) {
			try {
				o.writeObject("GM");
				o.flush();
			} catch (IOException e) {
				System.out.println("Exception in login " + e);
			}
		}
	}


	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
