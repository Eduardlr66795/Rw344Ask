import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class Client extends Thread implements ActionListener {

	// Global Variables
	private Socket client;
	private int port = 9119;

	private ObjectInputStream objectInput;
	private ObjectOutputStream objectOutput;

	private String userName;

	// WelcomeScreen
	private JFrame frameWelcome;
	private JPanel panel;

	// Create Game
	private JFrame frameCreateGame;
	private JPanel panelCreateGame;
	private JTextField textFieldEnterNewGameName;

	// MainGui
	private String[] games;
	private JPanel[] bigframe;
	// private JTextField chat;
	// private JTextField chatBox;
	private JButton[][] cards;
	private JLabel gameName;
	private JLabel[] players;
	private JTabbedPane tabs;
	private JFrame guiFrame;

	// NEW--------------------------------
	public JButton button_send;
	public JButton button_history;
	public JButton button_clear;
	public JButton button_logoff;
	public JButton button_listPlayers;
	public JButton button_listGames;
	public JButton button_createGame;
	public JButton button_login;
	public JButton button_createNewGame;

	public JList jlist_contactsMain;
	public JPanel panel_mycards;
	public JPanel panel_game;
	public JTextArea textArea_display;

	public JLabel labelEnterNewGameName;

	public JTextField text_loginTextfieldName;
	public JTextField text_loginTextfielIp;
	public JTextField text_message;

	// END--------------------------------

	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Client() {
		welcomeScreen();
		// To test gui, comment out welcomeScreen() and run clientGui()
		//
	}

	@SuppressWarnings("serial")
	/*
         * 
         */
	void welcomeScreen() {
		// This try and catch is just a GUI theme. Looks cool
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

		frameWelcome = new JFrame();
		frameWelcome.setSize(400, 300);
		frameWelcome.setLocation(400, 100);
		frameWelcome.setEnabled(true);

		panel = new JPanel();
		panel.setSize(frameWelcome.getWidth(), frameWelcome.getHeight());
		panel.setLayout(null);
		panel.setBackground(Color.white);

		// ////////////////////////

		// Button
		JLabel heading = new JLabel("") {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (new ImageIcon(
						Client.class.getResource("/images/ask-logo.png")) != null) {
					int width = getWidth();
					int height = getHeight();
					if (width > height)
						width = height;
					else
						height = width;
					g.drawImage(
							new ImageIcon(Client.class
									.getResource("/images/ask-logo.png"))
									.getImage(), 5, 5, width + 20, height + 10,
							null);
				}
			}
		};

		// /////////////////////////////

		// heading.setText("///Ask");
		// heading.setForeground(Color.WHITE);
		heading.setFont(new Font("Serif", Font.BOLD, 45));
		heading.setBounds(100, 20, panel.getWidth(), 100);

		JLabel created = new JLabel();
		created.setText("Welcomes you!");
		created.setFont(new Font("Serif", Font.BOLD, 18));
		created.setForeground(Color.RED);
		created.setBounds(100, 55, 280, 100);

		JLabel loginName = new JLabel();
		loginName.setText("Name: ");
		loginName.setForeground(Color.gray);
		loginName.setFont(new Font("Serif", Font.BOLD, 18));
		loginName.setBounds(60, 160, 75, 25);

		JLabel IP = new JLabel();
		IP.setText("Address:");
		IP.setForeground(Color.gray);
		IP.setFont(new Font("Serif", Font.BOLD, 18));
		IP.setBounds(37, 190, 100, 25);

		text_loginTextfieldName = new JTextField();
		text_loginTextfieldName.setFont(new Font("Serif", Font.BOLD, 16));
		text_loginTextfieldName.setForeground(Color.RED);
		text_loginTextfieldName.setBounds(130, 160, 200, 25);

		text_loginTextfielIp = new JTextField();
		text_loginTextfielIp.setText("localhost");
		text_loginTextfielIp.setFont(new Font("Serif", Font.BOLD, 16));
		text_loginTextfielIp.setForeground(Color.RED);
		text_loginTextfielIp.setBounds(130, 190, 200, 25);

		button_login = new JButton();
		button_login.setText("Login");
		button_login.setFont(new Font("Serif", Font.PLAIN, 20));
		button_login.setBounds(130, 220, 200, 30);
		button_login.setForeground(Color.BLACK);
		button_login.addActionListener(this);

		panel.add(heading);
		panel.add(created);
		panel.add(loginName);
		panel.add(IP);
		panel.add(text_loginTextfieldName);
		panel.add(text_loginTextfielIp);
		panel.add(button_login);

		frameWelcome.add(panel);
		frameWelcome.setResizable(false);
		frameWelcome.setVisible(true);

		frameWelcome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameWelcome.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO: create window event
				frameWelcome.dispose();
				System.exit(0);
			}
		});
	}

	/*
         * 
         */
	void clientGui() {

		// This try and catch is just a GUI theme. Looks cool
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

		games = new String[15];
		for (int k = 0; k < 15; k++) {
			games[k] = "empty";
		}

		cards = new JButton[15][10];
		bigframe = new JPanel[40];
		tabs = new JTabbedPane();
		guiFrame = new JFrame();

		guiFrame.setSize(1000, 390);
		guiFrame.setLayout(null);
		guiFrame.setResizable(false);
		guiFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		guiFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				guiFrame.dispose();
				// TODO : Create an exit event
			}
		});

		guiFrame.setTitle("Example GUI");

		button_send = new JButton("Send Msg");
		button_send.setBounds(709, 360, 93, 30);
		button_send.addActionListener(this);
		guiFrame.add(button_send);

		// TODO public JButton button_clear;

		text_message = new JTextField();
		text_message.setSize(700, 30);
		text_message.setLocation(3, 360);
		guiFrame.add(text_message);

		JScrollPane spMain = new JScrollPane(textArea_display = new JTextArea());
		// spMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spMain.setBounds(3, 230, 800, 130);
		textArea_display.setEditable(false);
		guiFrame.add(spMain);

		button_listPlayers = new JButton("List of Players");
		button_listPlayers.setBounds(820, 230, 160, 25);
		button_listPlayers.addActionListener(this);
		guiFrame.add(button_listPlayers);

		button_listGames = new JButton("List Games");
		button_listGames.setBounds(820, 250, 160, 25);
		button_listGames.addActionListener(this);
		guiFrame.add(button_listGames);

		button_history = new JButton("History");
		button_history.setBounds(820, 270, 160, 25);
		button_history.addActionListener(this);
		guiFrame.add(button_history);

		button_logoff = new JButton("Log Off");
		button_logoff.setBounds(820, 290, 160, 25);
		button_logoff.addActionListener(this);
		guiFrame.add(button_logoff);

		button_createGame = new JButton("Create Game");
		button_createGame.setBounds(820, 310, 160, 25);
		button_createGame.addActionListener(this);
		guiFrame.add(button_createGame);

		tabs.setLocation(3, 8);
		tabs.setSize(990, 200);

		guiFrame.add(tabs);
		guiFrame.setLocationRelativeTo(null);
		guiFrame.setVisible(true);

		// Just for
		// testing--------------------------------------------------------------------
		String[] test1 = { "9s", "th", "2c", "4s", "6c", "7c", "9h", "qd",
				"ks", "as" };
		String[] test2 = { "7s", "3h", "qc", "8s", "2c", "3c", "kh" };
		String[] test3 = { "qs", "th" };
		String[] names1 = { "Paul", "Luke", "Michael", "Kristo", "James",
				"Gerrit", "Jukkie" };
		String[] names2 = { "Michael", "James", "Kristo", "Gerrit", "Jukkie",
				"Luke" };
		String[] names3 = { "Michael", "James", "Kristo" };
		newGameGui("Friendly", names1);
		newGameGui("Fun", names2);
		newGameGui("Hello", names1);
		newGameGui("G3", names3);
		updateGame("Friendly", test3, null);
		updateGame("Fun", test1, null);
		updateGame("G3", test2, null);
		endGame("G3");
		newGameGui("Test2", names2);
		newGameGui("Test3", names1);
		updateGame("Test2", test1, null);
		updateGame("Test3", test2, null);
		// endGame("Test3");
		// endGame("Friendly");
		// ------------------------------------------------------------------------------------
	}

	public void newGameGui(String gName, String[] pNames) {
		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (games[i].equals("empty")) {
				games[i] = gName;
				gameNumber = i;
				break;
			}
		}

		// Initialise
		bigframe[gameNumber] = new JPanel();
		bigframe[gameNumber].setSize(990, 200);
		bigframe[gameNumber].setLayout(null);
		bigframe[gameNumber].setName(gName);
		cards[gameNumber] = new JButton[10];
		players = new JLabel[7];

		// Add game name label
		gameName = new JLabel(gName);
		gameName.setFont(new java.awt.Font("Tahoma", 0, 24));
		gameName.setBounds(340, 2, 180, 40);
		bigframe[gameNumber].add(gameName);
		// Add Player Names
		for (int k = 0; k < pNames.length; k++) {
			players[k] = new JLabel(pNames[k]);
			players[k].setBounds(820, (k * 20), 50, 15);
			bigframe[gameNumber].add(players[k]);
		}
		// Add 10 facedown cards
		for (int k = 0; k < 10; k++) {
			Icon icon = new ImageIcon("src/cards/back.gif");
			cards[gameNumber][k] = new JButton(icon);
			cards[gameNumber][k].setBounds((80 * k), 70, 75, 100);
			cards[gameNumber][k].addActionListener(this);
			cards[gameNumber][k].setEnabled(false);
			bigframe[gameNumber].add(cards[gameNumber][k]);
		}
		// Add new game panel
		bigframe[gameNumber].setName(gName);
		tabs.addTab(gName, bigframe[gameNumber]);
	}

	public void updateGame(String gName, String[] pCards, int[] pScores) {
		// Find gameNumber
		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (games[i].equals(gName)) {
				gameNumber = i;
				break;
			}
		}

		for (int k = 0; k < pCards.length; k++) {
			cards[gameNumber][k].setEnabled(true);
			Icon icon = new ImageIcon("src/cards/" + pCards[k] + ".gif");
			cards[gameNumber][k].setIcon(icon);
			cards[gameNumber][k].setName(pCards[k] + ""
					+ bigframe[gameNumber].getName());
			cards[gameNumber][k].setBounds((80 * k), 70, 75, 100);
			bigframe[gameNumber].add(cards[gameNumber][k]);
		}
		for (int k = 9; k > pCards.length - 1; k--) {
			cards[gameNumber][k].setVisible(false);
		}
	}

	public void endGame(String gName) {
		// find gameNumber
		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (games[i].equals(gName)) {
				gameNumber = i;
				games[gameNumber] = "empty";
			}
		}
		int temp = tabs.getTabCount();
		System.out.println(gName);
		System.out.println(temp);
		for (int i = 0; i < temp; i++) {
			System.out.println(i);
			if (tabs.getComponentAt(i).getName().equals(gName)) {
				tabs.removeTabAt(i);
				break;

			}
		}
	}

	public void quit() {
		try {
			client.close();
			// TODO
			// frameMain.dispose();
			// threadMessage.interrupt();
			System.exit(0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		// threadMessage.destroy()
	}

	public void connectToServer() {
		try {
			/*
			 * TODO Change the servername to IP Address to test over network
			 */

			String servername = text_loginTextfielIp.getText();
			userName = text_loginTextfieldName.getText();
			client = new Socket(servername, port);

			objectInput = new ObjectInputStream(client.getInputStream());
			objectOutput = new ObjectOutputStream(client.getOutputStream());

			String serverMsg = (String) objectInput.readObject();

			if (serverMsg.compareTo("RD") == 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("LI");
				sb.append(userName);
				sb.append(":password");

				objectOutput.writeObject(sb.toString());

				String msg = (String) objectInput.readObject();
				System.out.println(msg);

				if (msg.compareTo("LK") == 0) {
					clientGui();
					frameWelcome.dispose();
					start();

				} else {
					// new Client();//TODO
					System.out
							.println("ERROR IN CLIENT connectToServer method");
				}
			}

			else {
				JOptionPane.showMessageDialog(null, "Server is not ready");
				// quit(); //TODO
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void send_Message() {
		String text = text_message.getText();
		if (text.length() > 0) {
			try {
				textArea_display.append("<- " + text + " ->\n");
				text_message.setText("");
				objectOutput.writeObject(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			text_message.setText("");
		}
	}

	public void run() {
		while (true) {
			try {
				String line = (String) objectInput.readObject();
				System.out.println("mess rcvd: " + line.toString());
				if (line != null) {
					textArea_display.append("Server--> " + line + "\n");
					System.out.println(line.toString());
				}
			}

			catch (Exception e) {
				e.printStackTrace();
				// TODO : What frame are we disposing here??
				// dispose();
				System.out.println("ERROR");
				System.exit(0);
			}

		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create New Game
	void createGame() {
		// Theme
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

		frameCreateGame = new JFrame();
		frameCreateGame.setSize(400, 140);
		frameCreateGame.setLocation(guiFrame.getX(), guiFrame.getY());
		frameCreateGame.setEnabled(true);

		panelCreateGame = new JPanel();
		panelCreateGame.setSize(frameCreateGame.getWidth(),
				frameCreateGame.getHeight());
		panelCreateGame.setLayout(null);
		panelCreateGame.setBackground(Color.white);

		textFieldEnterNewGameName = new JTextField();
		textFieldEnterNewGameName.setSize(200, 30);
		textFieldEnterNewGameName.setLocation(150, 10);

		labelEnterNewGameName = new JLabel();
		labelEnterNewGameName.setSize(150, 30);
		labelEnterNewGameName.setLocation(10, 10);
		labelEnterNewGameName.setText("Enter game name:");

		button_createNewGame = new JButton();
		button_createNewGame.setSize(140, 40);
		button_createNewGame.setLocation(130, 65);
		button_createNewGame.setText("Create Game");

		panelCreateGame.add(textFieldEnterNewGameName);
		panelCreateGame.add(labelEnterNewGameName);
		panelCreateGame.add(button_createNewGame);
		frameCreateGame.add(panelCreateGame);
		frameCreateGame.setVisible(true);

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == button_login) {
			connectToServer();

		}

		else if (evt.getSource() == button_send) {
			String text = text_message.getText();
			if (text.length() > 0) {
				textArea_display.append("<- " + text + " ->\n");
				StringBuilder sb = new StringBuilder();
				sb.append("CA");
				sb.append(tabs.getSelectedComponent().getName());
				sb.append(":");
				sb.append(text);
				sb.append(";");
				System.out.println(sb.toString());
				try {
					objectOutput.writeObject(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				text_message.setText("");
			} else {
				text_message.setText("");
			}
		}

		else if (evt.getSource() == button_listPlayers) {
			textArea_display.append("List Of Players Button Pressed\n");
		}

		else if (evt.getSource() == button_listGames) {
			textArea_display.append("button_listGames Pressed\n");
			StringBuilder sb = new StringBuilder();
			sb.append("GL;");
			try {
				objectOutput.writeObject(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Uncomment this when the server supports GL; function
			/*
			 * String serverMsg=""; try { serverMsg = (String)
			 * objectInput.readObject(); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } String command
			 * = serverMsg.substring(0, 2); String[] arguments =
			 * serverMsg.substring(2).split(":"); for(int
			 * i=0;i<arguments.length;i++){
			 * textArea_display.append(arguments[i]); }
			 * textArea_display.append("\n");
			 */

		}

		else if (evt.getSource() == button_history) {
			textArea_display.append("History Pressed\n");
		} else if (evt.getSource() == button_createGame) {
			textArea_display.append("Create new Game Screen\n");
			createGame();
			/*
			 * try { objectOutput.writeObject("GS:Hello;"); } catch (IOException
			 * e) { e.printStackTrace(); }
			 * 
			 * try { String serverMsg = (String) objectInput.readObject(); if
			 * (serverMsg.compareTo("GK") == 0) {
			 * textArea_display.append("Game created and comfrmed!!"); }
			 * 
			 * 
			 * } catch (Exception e) { e.printStackTrace(); }
			 */

		}

		else if (evt.getSource() == button_logoff) {
			textArea_display.append("button_logoff Pressed\n");
			// Send logoff
			StringBuilder sb = new StringBuilder();
			sb.append("LO:");
			try {
				objectOutput.writeObject(sb.toString());
			} catch (IOException e) {
				System.out.println("Logoff fail");
				e.printStackTrace();
			}
		} else {
			String temp = evt.toString().substring(
					evt.toString().indexOf(" on") + 4);
			// Check if temp equals any special cases, i.e. logoff, get list of
			// games, if not then a card has been clicked
			String card = temp.substring(0, 2);
			String game = temp.substring(2);
			System.out.print(card + " ");
			System.out.println(game);

		}
	}
}
