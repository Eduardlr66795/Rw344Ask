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

	// Global Variables--------------------------------
	// Frames
	public JFrame frame_main;
	public JFrame frame_choice;
	public JFrame frame_CreateGame;
	public JFrame frame_Welcome;
	
	// Buttons
	public JButton button_sendMessage_in;
	public JButton button_sendMessage_out;
	public JButton button_history;
	public JButton button_clear;
	public JButton button_logoff;
	public JButton button_listPlayers;
	public JButton button_listGames;
	public JButton button_createGame;
	public JButton button_login;
	public JButton button_createNewGame;
	public JButton button_newGame_out;
	public JButton button_joinGame_out;
	public JButton button_clearText_out;
	public JButton[][] button_cards;

	// Lists
	public JList jlist_contactsMain;
	public JList jlist_contactsOutsideMain;

	// Panels
	public JPanel panel_mycards;
	public JPanel panel_game;
	public JPanel panel_main;
	public JPanel panel_welcome;
	public JPanel panel_CreateGame;
	public JPanel[] panel_bigframe;
	public JTabbedPane tabs;
	
	// TextArea
	public JTextArea textArea_display_in;
	public JTextArea textArea_display_out;

	// Labels
	public JLabel label_enterNewGameName;
	public JLabel label_gameName;
	public JLabel[] plabel_layers;

	// TextFields
	public JTextField text_loginTextfieldName;
	public JTextField text_loginTextfielIp;
	public JTextField text_message_in;
	public JTextField text_message_out;
	public JTextField text_FieldEnterNewGameName;
	
	// Strings
	public String string_userName;
	public String[] string_games;
	
	// Other
	private Socket client;
	private int port = 9119;
	private ObjectInputStream objectInput;
	private ObjectOutputStream objectOutput;
	public boolean bol_mainFrameActive;
	// END--------------------------------

	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Client() {
		bol_mainFrameActive = false;
		welcomeScreen();
	}

	@SuppressWarnings("serial")
	void welcomeScreen() {
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

		frame_Welcome = new JFrame();
		frame_Welcome.setSize(400, 300);
		frame_Welcome.setLocation(400, 100);
		frame_Welcome.setEnabled(true);

		panel_welcome = new JPanel();
		panel_welcome.setSize(frame_Welcome.getWidth(), frame_Welcome.getHeight());
		panel_welcome.setLayout(null);
		panel_welcome.setBackground(Color.white);

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
		heading.setFont(new Font("Serif", Font.BOLD, 45));
		heading.setBounds(100, 20, panel_welcome.getWidth(), 100);

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

		panel_welcome.add(heading);
		panel_welcome.add(created);
		panel_welcome.add(loginName);
		panel_welcome.add(IP);
		panel_welcome.add(text_loginTextfieldName);
		panel_welcome.add(text_loginTextfielIp);
		panel_welcome.add(button_login);

		frame_Welcome.add(panel_welcome);
		frame_Welcome.setResizable(false);
		frame_Welcome.setVisible(true);

		frame_Welcome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_Welcome.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO: create window event
				frame_Welcome.dispose();
				System.exit(0);
			}
		});
	}

	@SuppressWarnings("static-access")
	public void afterLoginScreen() {

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

		frame_choice = new JFrame();
		frame_choice.setLayout(null);
		frame_choice.setSize(580, 400);

		// Main Panel
		panel_main = new JPanel();
		panel_main.setLayout(null);
		panel_main.setSize(frame_choice.getWidth(), frame_choice.getHeight());

		// Button: Create New Game
		button_newGame_out = new JButton("Create New Game");
		button_newGame_out.setBounds(15, 15, 170, 30);
		button_newGame_out.addActionListener(this);
		panel_main.add(button_newGame_out);

		// Button: Join Existing Game
		button_joinGame_out = new JButton("Join Existing Game");
		button_joinGame_out.setBounds(15, 50, 170, 30);
		button_joinGame_out.addActionListener(this);
		panel_main.add(button_joinGame_out);

		// Button: Join Existing Game
		button_sendMessage_out = new JButton("Send Message");
		button_sendMessage_out.setBounds(265, 300, 150, 30);
		button_sendMessage_out.addActionListener(this);
		panel_main.add(button_sendMessage_out);

		// Button: Join Existing Game
		button_clearText_out = new JButton("Clear Text");
		button_clearText_out.setBounds(265, 330, 150, 30);
		button_clearText_out.addActionListener(this);
		panel_main.add(button_clearText_out);

		// TextArea: Join Existing Game
		textArea_display_out = new JTextArea();
		textArea_display_out.setEditable(false);
		textArea_display_out.setBounds(15, 90, 400, 180);
		panel_main.add(textArea_display_out);

		// Scrolepane: Join Existing Game
		JScrollPane sp1 = new JScrollPane(
				jlist_contactsOutsideMain = new JList());
		jlist_contactsOutsideMain.setVisibleRowCount(4);
		jlist_contactsOutsideMain.setBackground(Color.LIGHT_GRAY);
		sp1.setBounds(420, 90, 150, 270);
		panel_main.add(sp1);

		// TextField: Join Existing Game
		text_message_out = new JTextField();
		text_message_out.setBounds(15, 270, 400, 30);
		panel_main.add(text_message_out);

		frame_choice.add(panel_main);
		frame_choice.setVisible(true);
		frame_choice.setDefaultCloseOperation(frame_choice.EXIT_ON_CLOSE);
	}

	void clientGui() {
		bol_mainFrameActive = true;
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

		string_games = new String[15];
		for (int k = 0; k < 15; k++) {
			string_games[k] = "empty";
		}

		button_cards = new JButton[15][10];
		panel_bigframe = new JPanel[40];
		tabs = new JTabbedPane();
		frame_main = new JFrame();

		frame_main.setSize(1000, 390);
		frame_main.setLayout(null);
		frame_main.setResizable(false);
		frame_main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_main.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bol_mainFrameActive = false;
				frame_main.dispose();
				// TODO : Create an exit event
			}
		});

		frame_main.setTitle("Example GUI");

		button_sendMessage_in = new JButton("Send Msg");
		button_sendMessage_in.setBounds(709, 360, 93, 30);
		button_sendMessage_in.addActionListener(this);
		frame_main.add(button_sendMessage_in);

		// TODO public JButton button_clear;

		text_message_in = new JTextField();
		text_message_in.setSize(700, 30);
		text_message_in.setLocation(3, 360);
		frame_main.add(text_message_in);

		JScrollPane spMain = new JScrollPane(textArea_display_in = new JTextArea());
		spMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spMain.setBounds(3, 230, 800, 130);
		textArea_display_in.setEditable(false);
		frame_main.add(spMain);

		button_listPlayers = new JButton("List of Players");
		button_listPlayers.setBounds(820, 230, 160, 25);
		button_listPlayers.addActionListener(this);
		frame_main.add(button_listPlayers);

		button_listGames = new JButton("List Games");
		button_listGames.setBounds(820, 250, 160, 25);
		button_listGames.addActionListener(this);
		frame_main.add(button_listGames);

		button_history = new JButton("History");
		button_history.setBounds(820, 270, 160, 25);
		button_history.addActionListener(this);
		frame_main.add(button_history);

		button_logoff = new JButton("Log Off");
		button_logoff.setBounds(820, 290, 160, 25);
		button_logoff.addActionListener(this);
		frame_main.add(button_logoff);

		button_createGame = new JButton("Create Game");
		button_createGame.setBounds(820, 310, 160, 25);
		button_createGame.addActionListener(this);
		frame_main.add(button_createGame);

		tabs.setLocation(3, 8);
		tabs.setSize(990, 200);

		frame_main.add(tabs);
		frame_main.setLocationRelativeTo(null);
		frame_main.setVisible(true);

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
			if (string_games[i].equals("empty")) {
				string_games[i] = gName;
				gameNumber = i;
				break;
			}
		}

		// Initialise
		panel_bigframe[gameNumber] = new JPanel();
		panel_bigframe[gameNumber].setSize(990, 200);
		panel_bigframe[gameNumber].setLayout(null);
		panel_bigframe[gameNumber].setName(gName);
		button_cards[gameNumber] = new JButton[10];
		plabel_layers = new JLabel[7];

		// Add game name label
		label_gameName = new JLabel(gName);
		label_gameName.setFont(new java.awt.Font("Tahoma", 0, 24));
		label_gameName.setBounds(340, 2, 180, 40);
		panel_bigframe[gameNumber].add(label_gameName);
		// Add Player Names
		for (int k = 0; k < pNames.length; k++) {
			plabel_layers[k] = new JLabel(pNames[k]);
			plabel_layers[k].setBounds(820, (k * 20), 50, 15);
			panel_bigframe[gameNumber].add(plabel_layers[k]);
		}
		// Add 10 facedown cards
		for (int k = 0; k < 10; k++) {
			Icon icon = new ImageIcon("src/cards/back.gif");
			button_cards[gameNumber][k] = new JButton(icon);
			button_cards[gameNumber][k].setBounds((80 * k), 70, 75, 100);
			button_cards[gameNumber][k].addActionListener(this);
			button_cards[gameNumber][k].setEnabled(false);
			panel_bigframe[gameNumber].add(button_cards[gameNumber][k]);
		}
		// Add new game panel
		panel_bigframe[gameNumber].setName(gName);
		tabs.addTab(gName, panel_bigframe[gameNumber]);
	}

	public void updateGame(String gName, String[] pCards, int[] pScores) {
		// Find gameNumber
		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (string_games[i].equals(gName)) {
				gameNumber = i;
				break;
			}
		}

		for (int k = 0; k < pCards.length; k++) {
			button_cards[gameNumber][k].setEnabled(true);
			Icon icon = new ImageIcon("src/cards/" + pCards[k] + ".gif");
			button_cards[gameNumber][k].setIcon(icon);
			button_cards[gameNumber][k].setName(pCards[k] + ""
					+ panel_bigframe[gameNumber].getName());
			button_cards[gameNumber][k].setBounds((80 * k), 70, 75, 100);
			panel_bigframe[gameNumber].add(button_cards[gameNumber][k]);
		}
		for (int k = 9; k > pCards.length - 1; k--) {
			button_cards[gameNumber][k].setVisible(false);
		}
	}

	public void endGame(String gName) {
		// find gameNumber
		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (string_games[i].equals(gName)) {
				gameNumber = i;
				string_games[gameNumber] = "empty";
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
			string_userName = text_loginTextfieldName.getText();
			client = new Socket(servername, port);

			objectInput = new ObjectInputStream(client.getInputStream());
			objectOutput = new ObjectOutputStream(client.getOutputStream());

			String serverMsg = (String) objectInput.readObject();

			if (serverMsg.compareTo("RD") == 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("LI");
				sb.append(string_userName);
				sb.append(":password");

				objectOutput.writeObject(sb.toString());
				String msg = (String) objectInput.readObject();
				if (msg.compareTo("LK") == 0) {
//					 clientGui();
					afterLoginScreen();
					frame_Welcome.dispose();
					start();
				} else {
					// new Client();//TODO
					System.out
							.println("ERROR IN CLIENT connectToServer method");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Server is not ready");
				// quit(); //TODO
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void send_Message() {
		String text = text_message_in.getText();
		if (text.length() > 0) {
			try {
				textArea_display_in.append("<- " + text + " ->\n");
				text_message_in.setText("");
				objectOutput.writeObject(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			text_message_in.setText("");
		}
	}

	public void run() {
		while (true) {
			try {
				String line = (String) objectInput.readObject();
				System.out.println("mess rcvd: " + line.toString());
				if (line != null) {
					if(!bol_mainFrameActive) {
						textArea_display_out.append("Server--> " + line + "\n");
					} else {
						textArea_display_in.append("Server--> " + line + "\n");
					}
				}
			} catch (Exception e) {
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

		frame_CreateGame = new JFrame();
		frame_CreateGame.setSize(400, 140);
		frame_CreateGame.setLocation(frame_main.getX(), frame_main.getY());
		frame_CreateGame.setEnabled(true);

		panel_CreateGame = new JPanel();
		panel_CreateGame.setSize(frame_CreateGame.getWidth(),
				frame_CreateGame.getHeight());
		panel_CreateGame.setLayout(null);
		panel_CreateGame.setBackground(Color.white);

		text_FieldEnterNewGameName = new JTextField();
		text_FieldEnterNewGameName.setSize(200, 30);
		text_FieldEnterNewGameName.setLocation(150, 10);

		label_enterNewGameName = new JLabel();
		label_enterNewGameName.setSize(150, 30);
		label_enterNewGameName.setLocation(10, 10);
		label_enterNewGameName.setText("Enter game name:");

		button_createNewGame = new JButton();
		button_createNewGame.setSize(140, 40);
		button_createNewGame.setLocation(130, 65);
		button_createNewGame.setText("Create Game");

		panel_CreateGame.add(text_FieldEnterNewGameName);
		panel_CreateGame.add(label_enterNewGameName);
		panel_CreateGame.add(button_createNewGame);
		frame_CreateGame.add(panel_CreateGame);
		frame_CreateGame.setVisible(true);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == button_login) {
			connectToServer();
		} 
		
		
		else if (evt.getSource() == button_sendMessage_in) {
			String text = text_message_in.getText();
			if (text.length() > 0) {
				textArea_display_in.append("<- " + text + " ->\n");
				StringBuilder sb = new StringBuilder();
				sb.append("CA");
				sb.append(tabs.getSelectedComponent().getName());
				sb.append(":");
				sb.append(text);
				sb.append(";");
				try {
					objectOutput.writeObject(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				text_message_in.setText("");
			} else {
				text_message_in.setText("");
			}
		} 

		else if (evt.getSource() == button_listPlayers) {
			textArea_display_in.append("List Of Players Button Pressed\n");
		} 
		
		
		//TODO: what command should we send to the server?
		else if (evt.getSource() == button_sendMessage_out) {
			if (text_message_out.getText().length() > 0) {
				String text = text_message_out.getText();
				textArea_display_out.append("<- " + text + " ->\n");
				StringBuilder sb = new StringBuilder();
				sb.append("CA");
//				sb.append(tabs.getSelectedComponent().getName());
				sb.append(":");
				sb.append(text);
				sb.append(";");
				try {
					objectOutput.writeObject(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				text_message_out.setText("");
				
			} else {
				text_message_out.setText("");
			}
		}

		else if (evt.getSource() == button_listGames) {
			textArea_display_in.append("button_listGames Pressed\n");
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
			textArea_display_in.append("History Pressed\n");
		} else if (evt.getSource() == button_createGame) {
			textArea_display_in.append("Create new Game Screen\n");
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
			textArea_display_in.append("button_logoff Pressed\n");
			
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
