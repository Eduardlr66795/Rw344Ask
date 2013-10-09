// 16561295
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.*;

/**
 * 
 * @author //Ask
 * 
 */
@SuppressWarnings("serial")
public class Server extends JDialog implements ActionListener {

    
	private ServerSocket server;
	private Socket socket;
	private int port = 9119;
	private boolean islistening;

	HashSet<HandleClient> clientsList = new HashSet<HandleClient>();
	Hashtable<String, Game> gamesList = new Hashtable<String, Game>();
	HandleClient client;

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
	

	
        private final int MAX_PLAYERS = 7;

        public static void main(String[] args) {
		try {
			new Server().process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method : buildGui Builds the interface of server, clients and games. This
	 * is a graphical representation of activity on server, clients and the
	 * different games.
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
				quit();
			}
		});
	}

	public void process() {
		// Build the GUI
		buildGui();

		// Open server port for communication
		try {
			islistening = true;
			server = new ServerSocket(getPort());
			textAreaServer.append("Server Successfully Started - port# : "
					+ getPort() + "\n");
			textAreaClient.append("Clients Connected to server:\n");
			textAreaGame.append("Active games  : # Clients:\n");

			// Run - accept the opening of server port
			run();

		} catch (IOException ex) {
			ex.printStackTrace();
			islistening = false;
			Logger.getLogger(Server.class.getName())
					.log(Level.SEVERE, null, ex);

			// Print Error to server text Area
			textAreaServer
					.append("Server not Started close all Connections and try again \n");
		}
	}

	/**
	 * Method : Run.
         * This method accepts the connections and handles the clients
	 */
	public void run() {
		while (isListening()) {
			try {
				socket = getServer().accept();

				// Send msg to client that server is ready
				PrintWriter output = new PrintWriter(getSocket().getOutputStream(),
						true);
				output.println("RD");

				// Handle clients in game
				client = new HandleClient(getSocket(), this);

				if (clientsList.contains(client) == false) {
					clientsList.add(client);
					textAreaClient.append(client.getName() + "\n");
					textAreaServer.append("new client : " + client.getName()
							+ " : " + getTimeAndDate() + "\n");
				} else {
					JOptionPane.showMessageDialog(null,
							"Server : Client NOT!! Unique\n");
					client.closeConnection();
				}
			} catch (IOException ex) {
                            textAreaClient.append("Error handling a socket connection request\n");
			}
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

	public void quit() {
		try {
			getServer().close();
			islistening = false;
			frameMain.dispose();
			System.exit(0);
		} catch (IOException e1) {
                    //If there is something wrong, quit anyway.
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == button) {
			String text = textField.getText();
			text = text + "sad";
			System.out.println(text);
			textField.setText(" ");
		}
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
    
    //Same as above function but takes in a prefix parameter
    String[] getGamesList(String prefix) {
        String[] stringGamesList;
        int length = gamesList.size();
        int counter = 0;
        stringGamesList = new String[length];
        Iterator<String> itr = gamesList.keySet().iterator();
        String tmpGameName;
        while (itr.hasNext()) {
            tmpGameName = itr.next();
            if (tmpGameName.startsWith(prefix)) {
            	stringGamesList[counter] = tmpGameName;
            	counter++;
            }
            
        }
        if (counter == 0) {
        	stringGamesList[counter] = "";
        }
        return stringGamesList;
    }

    /**
     * @return the server
     */
    public ServerSocket getServer() {
        return server;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the islistening
     */
    public boolean isListening() {
        return islistening;
    }
    
    /**
     * @return the Number of games currently in gamesList HashTable
     */
    public int getGamesCount() {
        return gamesList.size();
    }

    Game getGame(String gameName) {
        return gamesList.get(gameName);
    }
    
	public boolean logoffClient(String userName) {
		HandleClient client = getHandle(userName);
		if(clientsList.contains(client)){
			clientsList.remove(client);
			return true;
		}
		return false;
	}
	
	public HandleClient getHandle(String playerName){
		Iterator<HandleClient> itr = clientsList.iterator();
		HandleClient tempClient, client = null;
		
		while(itr.hasNext()){
			tempClient = itr.next();
			if(tempClient.getName().equals(playerName)){
				//there is such a user that can be kicked.
				client = tempClient;
				break;
			}
		}
		return client;
	}
}
