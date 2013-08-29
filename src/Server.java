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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

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
	public static int gamesCount;

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
	

	public static void main(String[] args) {
		try {
			gamesCount = 0;
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
			server = new ServerSocket(port);
			textAreaServer.append("Server Successfully Started - port# : "
					+ port + "\n");
			textAreaClient.append("Clients Connected to server:\n");
			textAreaGame.append("Active games  : # Clients:\n");

			// Run - accept the connection
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
	 * Method : Run This method accepts the connections and handles the clients
	 */
	public void run() {
		while (islistening) {
			try {
				socket = server.accept();

				// Send msg to client that server is ready
				PrintWriter output = new PrintWriter(socket.getOutputStream(),
						true);
				output.println("RD");

				// Handle clients in game
				client = new HandleClient(socket, this);

				if (clientsList.contains(client) == false) {
					clientsList.add(client);
					textAreaClient.append(client.getName() + "\n");
					textAreaServer.append("new client : " + client.getName()
							+ " : " + getTimeAndDate() + "\n");
				} else {
					JOptionPane.showMessageDialog(null,
							"Server : Client NOT!! Unique");
					client.closeConnection();
				}
			} catch (IOException ex) {
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
	public Game createGame(String gameName, String creator) {
		// handle the creation of a game object and add to the HashSet.
		Game newGame = new Game(gameName, creator, this, gamesCount);

		// game is unique and was created
		gamesList.put(gameName, newGame);
			
		// Add one game to global game count
		gamesCount++;
		
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
		return false;
	}
	
	//This is for the GN command, and because its synchronous
	public void allowAddPlayerToGame(String gameName) {
		Game game = gamesList.get(gameName);
		game.addAnotherPlayer = true;
		gamesList.put(gameName, game);
	}
	
	//Adding a player to game
	public boolean addPlayerToGame(String gameName, String playerName) {
		Game game = gamesList.get(gameName);
		game.playerList.put(playerName, game.playerCount);
		game.playerCount++;
		game.addAnotherPlayer = false;
		gamesList.put(gameName, game);
		return true;
	}

	public void quit() {
		try {
			server.close();
			islistening = false;
			frameMain.dispose();
			System.exit(0);
		} catch (IOException e1) {
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
}
