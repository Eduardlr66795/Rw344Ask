import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.DefaultListModel;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Client extends Thread implements ActionListener,
		ListSelectionListener, MouseListener {

	// new--------------------------------
	public JButton button_startCreatedGame;
	public JList jlist_playersJoiningGame;
	public JLabel label_NameOfGameToBeCreated;
	public JButton button_kickPlayer;
	public JPanel panel_waitingToStartGame;
	public JFrame frame_waitingToStartGame;
	public DefaultListModel defaultList_players;
	public DefaultListModel defaultList_games;
	public JList jlist_availableGames;
	public boolean gameNotStarted = true;
	public String username;
	public String round_number;
	public JPanel panel_enterBid;
	public JTextField text_FieldEnterBid;
	public JLabel label_enterBid;
	public JButton button_enterBid;
	public JLabel pleaseWork = new JLabel("Waiting to Start. . .");
	public JFrame frame_enterBid;
	public JLabel usernameForMainFrame;
	public JLabel turnToPlay;
	public JLabel text_fieldTrumpSuite;
	public int playedCardsPlacekeeper = 0;
	public int playerCountemp = 0;
	public JLabel[] label_playedCards;
	public int cardClickedJ;
	public JScrollPane spScores;
	public int cardClickedI;
	public int totalrounds = 0;
	public int[] tempLastTrickScore = { 0, 0, 0, 0, 0, 0, 0 };
	JScrollPane sp4 = new JScrollPane(jlist_contactsMain = new JList());
	public String nextPlayerToBid = "";
	public String firstPlayerToPlay;
	public String trumpSuite;
	public String tempLastPlayer = "";
	public String[] tempLastGU;
	String[][] data = { { " ", " ", " " }, { " ", " ", " " }, { " ", " ", " " } };
	String colNames[] = { "Username", "Tricks For Hand", "Bid" };
	DefaultTableModel tableModel = new DefaultTableModel();
	public JTable table_scores = new JTable(tableModel);
	public JLabel label_waitingToJoin = new JLabel();
	// Global Variables--------------------------------
	// Frames
	public JFrame frame_main;
	public JFrame frame_choice;
	public JFrame frame_CreateGame;
	public JFrame frame_Welcome;

	public Hashtable<String, String> scores = new Hashtable<String, String>();
	public Hashtable<String, Integer> bids = new Hashtable<String, Integer>();

	// Buttons
	public JButton button_sendPrivateMessage_in;
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
	public JButton[] button_playedCards;
	public JButton button_exitGame = new JButton();
	JLabel[][] text_roundWinner;
	public boolean bol_prefix = false;
	public JTextField text_FieldPrefixOut;
	public JTextField text_FieldPrefixIn;

	// Lists
	public JList jlist_contactsMain;
	public JList jlist_gameMain;
	public JList jlist_contactsOutsideMain;
	public JList jlist_gmesListOutMain;

	// Panels
	public JPanel panel_mycards;
	public JPanel panel_game;
	public JPanel panel_main;
	public JPanel panel_welcome;
	public JPanel panel_CreateGame;
	public JPanel[] panel_bigframe;
	public JTabbedPane tabs;
	public JPanel panel_backRed;
	public JPanel panel_side;


	// TextArea
	public JTextArea textArea_display_in;
	public JTextArea textArea_display_out;

	// TextFields
	public JTextField text_loginTextfieldName;
	public JTextField text_loginTextfieldIp;
	public JTextField text_loginTextfieldPort;
	public JTextField text_messagePrivate_in;
	public JTextField text_message_in;
	public JTextField text_message_out;
	public JTextField text_FieldEnterNewGameName;

	// Labels
	public JLabel label_enterNewGameName;
	public JLabel label_gameName;
	public JLabel[] plabel_players;
	public JLabel[] plabel_playersScores = new JLabel[7];
	public JLabel[] plabel_playersBids = new JLabel[7];
	public JLabel label_usernameScores;
	public JLabel scoreScores;
	public JLabel label_bids = new JLabel("Bid");

	// tabs
	JTabbedPane tabOutside;
	JTabbedPane tabInside;

	// Strings
	public String string_userName;
	public String[] string_games;
	public String names[] = null;
	public String tempGameName;
	public String tempKickPlayer;
	JLabel winnerOfRound = new JLabel();
	public JFrame frame_roundWinner;
	public JPanel panel_roundWinner;
	public JButton button_closeWinner;

	public JButton button_prefixOut;
	public JButton button_prefixIn;

	// Other
	private Socket client;
	private ObjectInputStream objectInput;
	private ObjectOutputStream objectOutput;
	public boolean bol_mainFrameActive;
	public boolean bidding;
	public boolean biddingActive;
	public boolean turnToPlayCard;
	public boolean gameInProgress = false;
	public boolean threadHN = false;
	public boolean lastRound = false;
	public boolean roundWinnerFrame = false;
	boolean onlyEndOfTrick = true;
	boolean connected;
	boolean choice;
	public String clientArr[];

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
		welcomeScreen(0);
//		 clientGui();
	}

	public void startingGame(String GameName) {
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
		defaultList_players = new DefaultListModel();
		button_startCreatedGame = new JButton();
		jlist_playersJoiningGame = new JList(defaultList_players);
		label_NameOfGameToBeCreated = new JLabel();
		button_kickPlayer = new JButton();
		panel_waitingToStartGame = new JPanel();
		frame_waitingToStartGame = new JFrame();
		panel_waitingToStartGame.setLayout(null);

		frame_waitingToStartGame.setSize(300, 350);
		frame_waitingToStartGame.setLocation(frame_main.getX(),
				frame_main.getY());
		frame_waitingToStartGame.setEnabled(true);

		jlist_playersJoiningGame.setLocation(10, 50);
		jlist_playersJoiningGame.setSize(150, 200);
		jlist_playersJoiningGame.repaint();
		jlist_playersJoiningGame.setVisible(true);

		panel_waitingToStartGame.setSize(frame_CreateGame.getWidth(),
				frame_CreateGame.getHeight());
		panel_waitingToStartGame.setLayout(null);
		panel_waitingToStartGame.setBackground(Color.white);

		label_NameOfGameToBeCreated.setSize(150, 30);
		label_NameOfGameToBeCreated.setLocation(
				(frame_waitingToStartGame.getWidth() / 2) - 30, 10);
		label_NameOfGameToBeCreated.setText(tempGameName);

		button_kickPlayer.setSize(120, 40);
		button_kickPlayer.setText("Kick Player");
		button_kickPlayer.setLocation(180, 80);
		button_kickPlayer.addActionListener(this);

		button_startCreatedGame.setSize(140, 40);
		button_startCreatedGame.setLocation(
				(frame_waitingToStartGame.getWidth() / 2)
						- (button_startCreatedGame.getWidth() / 2), 270);
		button_startCreatedGame.setText("Start Game");
		button_startCreatedGame.addActionListener(this);

		panel_waitingToStartGame.add(button_startCreatedGame);
		panel_waitingToStartGame.add(label_NameOfGameToBeCreated);
		panel_waitingToStartGame.add(button_kickPlayer);
		panel_waitingToStartGame.add(jlist_playersJoiningGame);
		frame_waitingToStartGame.add(panel_waitingToStartGame);
		frame_waitingToStartGame.setVisible(true);
	}

	@SuppressWarnings("serial")
	void welcomeScreen(int x) {
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
		// for constantly updating active games
		tempLastGU = new String[30];
		for (int i = 0; i < tempLastGU.length; i++) {
			tempLastGU[i] = "";
		}

		frame_Welcome = new JFrame();
		frame_Welcome.setSize(400, 300);
		frame_Welcome.setLocation(400, 100);
		frame_Welcome.setEnabled(true);
		frame_Welcome.setTitle("//Ask Login");

		panel_welcome = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (new ImageIcon(Client.class.getResource("/images/frame.jpg")) != null) {
					int width = getWidth();
					int height = getHeight();
					if (width > height)
						width = height;
					else
						height = width;
					g.drawImage(
							new ImageIcon(Client.class
									.getResource("/images/frame.jpg"))
									.getImage(), 70, -80, width + 160, height,
							null);
				}
			}
		};

		panel_welcome.setSize(frame_Welcome.getWidth(),
				frame_Welcome.getHeight());
		panel_welcome.setBackground(Color.black);
		panel_welcome.setLayout(null);

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
									.getImage(), 0, 5, width + 20, height + 10,
							null);
				}
			}
		};
		heading.setFont(new Font("Serif", Font.BOLD, 45));
		heading.setBounds(100, 0, panel_welcome.getWidth(), 100);
		heading.setBackground(Color.white);

		JLabel loginName = new JLabel();
		loginName.setText("Name");
		loginName.setForeground(Color.red);
		loginName.setFont(new Font("cmmi10", Font.BOLD, 16));
		loginName.setBounds(165, 130, 75, 25);

		JLabel port = new JLabel();
		port.setText("Port");
		port.setForeground(Color.red);
		port.setFont(new Font("cmmi10", Font.BOLD, 16));
		port.setBounds(165, 162, 100, 25);

		JLabel IP = new JLabel();
		IP.setText("Address");
		IP.setForeground(Color.red);
		IP.setFont(new Font("cmmi10", Font.BOLD, 16));
		IP.setBounds(170, 215, 100, 25);

		text_loginTextfieldName = new JTextField();
		text_loginTextfieldName.setFont(new Font("Serif", Font.BOLD, 16));

		if (x == 1) {
			text_loginTextfieldName.setText("taken..");
			text_loginTextfieldName.setForeground(Color.RED);
		} else if (x == 2) {
			text_loginTextfieldName.setText("too Short..");
			text_loginTextfieldName.setForeground(Color.RED);
		} else if (x == 3) {
			text_loginTextfieldName.setText("invalid..");
			text_loginTextfieldName.setForeground(Color.RED);

		}

		text_loginTextfieldName.setBounds(10, 133, 150, 25);
		text_loginTextfieldPort = new JTextField();
		text_loginTextfieldPort.setText("3000");
		text_loginTextfieldPort.setFont(new Font("Serif", Font.BOLD, 16));
		text_loginTextfieldPort.setBounds(10, 163, 150, 25);

		text_loginTextfieldIp = new JTextField();
		text_loginTextfieldIp.setText("localhost");
		text_loginTextfieldIp.setFont(new Font("Serif", Font.BOLD, 16));
		text_loginTextfieldIp.setBounds(245, 215, 150, 25);

		button_login = new JButton() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (new ImageIcon(Client.class.getResource("/images/enter.png")) != null) {
					int width = getWidth();
					int height = getHeight();
					if (width > height)
						width = height;
					else
						height = width;
					g.drawImage(
							new ImageIcon(Client.class
									.getResource("/images/enter.png"))
									.getImage(), -2, 0, width + 130,
							height + 5, null);
				}
			}
		};
		button_login.setFont(new Font("Serif", Font.PLAIN, 20));
		button_login.setBounds(245, 245, 150, 30);
		button_login.setBackground(Color.black);
		button_login.addActionListener(this);

		panel_welcome.add(heading);
		panel_welcome.add(loginName);
		panel_welcome.add(port);
		panel_welcome.add(IP);
		panel_welcome.add(text_loginTextfieldName);
		panel_welcome.add(text_loginTextfieldIp);
		panel_welcome.add(text_loginTextfieldPort);
		panel_welcome.add(button_login);

		frame_Welcome.add(panel_welcome);
		frame_Welcome.setResizable(false);
		frame_Welcome.setVisible(true);

		frame_Welcome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_Welcome.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame_Welcome.dispose();
				System.exit(0);
			}
		});
	}

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
		frame_choice.setSize(590, 400);

		// Main Panel
		panel_main = new JPanel();
		panel_main.setLayout(null);

		panel_main.setSize(frame_choice.getWidth(), frame_choice.getHeight());
		// Prefix
		button_prefixOut = new JButton("Search prefix");
		button_prefixOut.setBounds(230, 50, 170, 30);
		button_prefixOut.addActionListener(this);
		text_FieldPrefixOut = new JTextField();
		text_FieldPrefixOut.setBounds(180, 50, 45, 30);
		panel_main.add(button_prefixOut);
		panel_main.add(text_FieldPrefixOut);

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
		button_sendMessage_out = new JButton("Send Private Msg");
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

		tabOutside = new JTabbedPane();
		tabOutside.setSize(150, 270);

		JScrollPane sp2 = new JScrollPane(
				jlist_contactsOutsideMain = new JList());
		jlist_contactsOutsideMain.setVisibleRowCount(4);
		jlist_contactsOutsideMain.addListSelectionListener(this);
		jlist_contactsOutsideMain.addMouseListener(this);
		sp2.setBounds(0, 0, 150, 270);
		tabOutside.addTab("Clients", sp2);

		JScrollPane sp3 = new JScrollPane(jlist_gmesListOutMain = new JList());
		jlist_gmesListOutMain.setVisibleRowCount(4);
		jlist_gmesListOutMain.addListSelectionListener(this);
		sp3.setBounds(0, 0, 150, 270);
		tabOutside.addTab("Games", sp3);
		button_playedCards = new JButton[7];
		label_playedCards = new JLabel[7];
		for (int i = 0; i < 7; i++) {
			label_playedCards[i] = new JLabel();
			button_playedCards[i] = new JButton();
		}
		JPanel test = new JPanel();
		test.setLayout(null);
		test.setBounds(420, 40, 150, 870);

		test.add(tabOutside);

		panel_main.add(test);
		getClients();

		// TextField: Join Existing Game
		text_message_out = new JTextField();
		text_message_out.setBounds(15, 270, 400, 30);
		panel_main.add(text_message_out);

		frame_choice.add(panel_main);
		frame_choice.setVisible(true);
		choice = true;

		frame_choice.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_choice.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logoutRequest();
			}
		});

		try {
			if (!bol_mainFrameActive) {
				objectOutput.writeObject("GL;");
				objectOutput.flush();
				objectOutput.writeObject("CC;");
				objectOutput.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Protocol
	// Once a game has begun, a player (either a joining player or the
	// initiating player)
	// may request a list of the other players. The server responds with a list
	// of players.
	// client -> server: GAgame_name;
	// server -> client: GCplayer_name1:player_name2:...;
	public void getClients() {
		// Write to server
		try {
			objectOutput.writeObject("LC;");
			objectOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
         * 
         */

	@SuppressWarnings("serial")
	public void clientGui() {
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
		text_roundWinner = new JLabel[8][5];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 5; j++) {
				text_roundWinner[i][j] = new JLabel();
			}
		}
		label_waitingToJoin.setLocation(200, 600);
		label_waitingToJoin.setSize(200, 80);
		label_waitingToJoin.setForeground(Color.red);
		label_waitingToJoin.setFont(new Font("Serif", Font.BOLD, 32));

		button_cards = new JButton[15][10];
		frame_roundWinner = new JFrame();
		panel_bigframe = new JPanel[40];
		button_closeWinner = new JButton();
		panel_roundWinner = new JPanel();
		tabs = new JTabbedPane();
		frame_main = new JFrame();

		plabel_players = new JLabel[15];

		frame_main.setSize(1300, 520);
		frame_main.setLayout(null);
		frame_main.setResizable(false);
		frame_main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_main.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bol_mainFrameActive = false;
				logoutRequest();
			}
		});

		panel_backRed = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (new ImageIcon(Client.class.getResource("/images/text1.jpg")) != null) {
					int width = getWidth();
					int height = getHeight();
					if (width > height)
						width = height;
					else
						height = width;
					g.drawImage(
							new ImageIcon(Client.class
									.getResource("/images/text1.jpg"))
									.getImage(), 0, 0, width + 415, height,
							null);
				}
			}
		};

		panel_backRed.setSize(frame_main.getWidth(), frame_main.getHeight());
		panel_backRed.setLayout(null);
		frame_main.add(label_waitingToJoin);
		panel_backRed.add(label_waitingToJoin);

		frame_main.add(panel_backRed);
		text_fieldTrumpSuite = new JLabel();

		frame_main.setTitle("Example GUI");

		text_message_in = new JTextField();
		text_message_in.setSize(700, 30);
		text_message_in.setLocation(3, 460);
		panel_backRed.add(text_message_in);

		// frame_main.add(text_message_in);

		JScrollPane spMain = new JScrollPane(
				textArea_display_in = new JTextArea());
		spMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spMain.setBounds(3, 330, 800, 130);
		textArea_display_in.setEditable(false);
		panel_backRed.add(spMain);
		panel_backRed.setLayout(null);

		panel_side = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (new ImageIcon(
						Client.class.getResource("/images/flameB1.jpg")) != null) {
					int width = getWidth();
					int height = getHeight();
					if (width > height)
						width = height;
					else
						height = width;
					g.drawImage(
							new ImageIcon(Client.class
									.getResource("/images/flameB1.jpg"))
									.getImage(), 0, 0, width, height + 120,
							null);
				}
			}
		};
		panel_side.setLayout(null);
		panel_side.setBounds(830, 0, 470, frame_main.getHeight());
		panel_side.setBackground(Color.black);
		label_waitingToJoin.setText("Waiting to join game!");
		label_waitingToJoin.setVisible(true);
		panel_backRed.add(panel_side);

		JScrollPane sp4 = new JScrollPane(jlist_contactsMain = new JList());
		sp4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp4.setBounds(10, 160, 140, 180);

		jlist_contactsMain.setVisibleRowCount(4);
		jlist_contactsMain.addListSelectionListener(this);
		jlist_contactsMain.addMouseListener(this);

		JScrollPane sp3 = new JScrollPane(jlist_gameMain = new JList());
		sp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp3.setBounds(160, 160, 140, 180);
		jlist_gameMain.addListSelectionListener(this);

		jlist_gameMain.addListSelectionListener(this);
		// tabInside.addTab("Games", sp3);
		// tabInside.addTab("Clients", sp4);
		// test.add(tabInside);

		panel_side.add(sp4);
		panel_side.add(sp3);

		panel_side.repaint();
		frame_main.repaint();
		button_prefixIn = new JButton("Search prefix");
		button_prefixIn.setBounds(5, 360, 160, 25);
		button_prefixIn.addActionListener(this);
		text_FieldPrefixIn = new JTextField();
		text_FieldPrefixIn.setBounds(165, 360, 45, 25);
		// table for scores;
		tableModel.setColumnIdentifiers(colNames);
		table_scores.setSize(400, 100);
		table_scores.setLocation(0, 0);
		spScores = new JScrollPane(table_scores);
		spScores.setSize(400, 100);
		spScores.setLocation(0, 0);
		panel_side.add(spScores);

		panel_side.add(button_prefixIn);
		panel_side.add(text_FieldPrefixIn);

		
		pleaseWork.setBounds(200, 100, 400, 30);
		pleaseWork.setFont(new Font("Serif", Font.BOLD, 26));
		pleaseWork.setForeground(Color.RED);
		pleaseWork.setVisible(false);

		panel_backRed.add(pleaseWork);

		button_sendMessage_in = new JButton("Send Msg");
		button_sendMessage_in.setBounds(709, 460, 100, 30);
		button_sendMessage_in.addActionListener(this);
		panel_backRed.add(button_sendMessage_in);

		button_listGames = new JButton("Join Selected Game");
		button_listGames.setBounds(5, 400, 160, 25);
		button_listGames.addActionListener(this);
		panel_side.add(button_listGames);

		button_logoff = new JButton("Log Off");
		button_logoff.setBounds(5, 440, 160, 25);
		button_logoff.addActionListener(this);
		panel_side.add(button_logoff);

		button_createGame = new JButton("Create Game");
		button_createGame.setBounds(5, 420, 160, 25);
		button_createGame.addActionListener(this);
		panel_side.add(button_createGame);

		button_listPlayers = new JButton("List of Players");
		button_listPlayers.setBounds(5, 380, 160, 25);
		button_listPlayers.addActionListener(this);
		panel_side.add(button_listPlayers);

		tabs.setLocation(3, 5);
		tabs.setBackground(Color.blue);
		tabs.setSize(805, 320);
		panel_backRed.add(tabs);

		// frame_main.add(tabs);

		frame_main.setLocationRelativeTo(null);
		frame_main.setVisible(true);

		bol_mainFrameActive = true;

		tempLastGU[0] = "";

	}

	@SuppressWarnings("serial")
	public void newGameGui(String gName) {

		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (string_games[i].equals("empty")) {
				string_games[i] = gName;
				gameNumber = i;
				break;
			}
		}
		// Initialise
		panel_bigframe[gameNumber] = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (new ImageIcon(
						Client.class.getResource("/images/test1.jpg")) != null) {
					int width = getWidth();
					int height = getHeight();
					if (width > height)
						width = height;
					else
						height = width;
					g.drawImage(
							new ImageIcon(Client.class
									.getResource("/images/test1.jpg"))
									.getImage(), 0, 0, width + 650, height+100,
							null);
				}
			}
		};
		
		panel_bigframe[gameNumber].setSize(825, 350);
		panel_bigframe[gameNumber].setLayout(null);
		panel_bigframe[gameNumber].setName(gName);

		button_cards[gameNumber] = new JButton[10];

		// Add game name label
		label_gameName = new JLabel(gName);
		label_gameName.setFont(new java.awt.Font("Tahoma", 0, 24));
		label_gameName.setBounds(340, 2, 180, 40);
		label_gameName.setForeground(Color.white);
		panel_bigframe[gameNumber].add(label_gameName);

		// Add 10 facedown cards
		for (int k = 0; k < 10; k++) {
			Icon icon = new ImageIcon("src/cards/back.gif");
			button_cards[gameNumber][k] = new JButton(icon);
			button_cards[gameNumber][k].setBounds((80 * k), 70, 75, 100);
			button_cards[gameNumber][k].addActionListener(this);
			button_cards[gameNumber][k].setEnabled(false);
			button_cards[gameNumber][k].repaint();
			panel_bigframe[gameNumber].add(button_cards[gameNumber][k]);
		}
		usernameForMainFrame = new JLabel();
		text_fieldTrumpSuite.setText("Trump Suite: ");
		text_fieldTrumpSuite.setSize(150, 20);
		text_fieldTrumpSuite.setLocation(500, 8);
		button_exitGame.setText("Exit Game");
		button_exitGame.setBounds(10, 10, 120, 20);
		// button_exitGame.setBounds(265, 330, 150, 30);
		button_exitGame.addActionListener(this);
		panel_bigframe[gameNumber].add(button_exitGame);
		turnToPlay = new JLabel();
		usernameForMainFrame.setSize(180, 20);
		usernameForMainFrame.setLocation(50, 40);
		usernameForMainFrame.setForeground(Color.white);
		usernameForMainFrame.setText(username);
		turnToPlay.setSize(160, 25);
		turnToPlay.setLocation(200, 5);
		turnToPlay.setText("TURN TO PLAY!!");
		turnToPlay.setForeground(Color.white);
		turnToPlay.setVisible(false);
		panel_bigframe[gameNumber].add(usernameForMainFrame);
		panel_bigframe[gameNumber].add(turnToPlay);		// plabel_layers = new JLabel[7];
		panel_bigframe[gameNumber].add(text_fieldTrumpSuite);
		// Add new game panel
		panel_bigframe[gameNumber].setName(gName);
		tabs.addTab(gName, panel_bigframe[gameNumber]);

	}

	public void newGameUpadatePlayers(String GameName, String[] pNames) {
		label_waitingToJoin.setVisible(true);
		pleaseWork.setVisible(false);

		// remove all rows from table
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tableModel.removeRow(i);
		}
		tableModel.getRowCount();

		int gameNumber = 0;
		// Find gameNumber
		for (int i = 0; i < 15; i++) {
			if (string_games[i].equals(GameName)) {
				gameNumber = i;
				break;
			}
		}
		// add player names to jlist
		jlist_contactsMain.setListData(pNames);
		// add headings
		// scoreScores,label_usernameScores
		scoreScores = new JLabel("Current score");
		label_usernameScores = new JLabel("Username");
		scoreScores.setBounds(873, 0, 100, 20);
		label_usernameScores.setBounds(798, 0, 100, 20);
		label_bids.setBounds(970, 0, 50, 20);
		// panel_bigframe[gameNumber].add(label_usernameScores);
		panel_bigframe[gameNumber].add(scoreScores);
		panel_bigframe[gameNumber].add(label_bids);

		// Add Player Names
		// totalrounds
		int k = 0;
		for (k = 0; k < pNames.length; k++) {
			tempLastTrickScore[k] = 0;
			bids.put(pNames[k], 0);
			plabel_players[k] = new JLabel(pNames[k]);
			plabel_playersScores[k] = new JLabel("0");
			plabel_playersBids[k] = new JLabel("0");
			plabel_players[k].setBounds(798, 30 + (k * 20), 80, 15);
			plabel_playersBids[k].setBounds(978, 30 + (k * 20), 80, 15);
			plabel_playersScores[k].setBounds(873, 30 + (k * 20), 80, 15);
			plabel_players[k].setVisible(false);
			// panel_bigframe[gameNumber].add(plabel_playersBids[k]);
			panel_bigframe[gameNumber].add(plabel_players[k]);
			// panel_bigframe[gameNumber].add(plabel_playersScores[k]);
			scores.put(pNames[k], "0:0:0:0");
			// update table
			tableModel.addRow(new Object[] { pNames[k], "0", "0" });
		}
		if (k == 7) {
			totalrounds = 7;
		} else if (k == 6) {
			totalrounds = 8;
		} else {
			totalrounds = 10;
		}
		panel_bigframe[gameNumber].repaint();
		playerCountemp = pNames.length;

		int lengt = (table_scores.getRowCount()) * table_scores.getRowHeight()
				+ 21;
		spScores.setSize(spScores.getWidth(), lengt);
	}

	public void updateScoresInGame(String[] currentScores) {
		for (int i = 0; i < playerCountemp; i++) {
			int cScore = Integer.parseInt(currentScores[3 * i + 1]);
			tableModel.setValueAt(cScore + "", i, 1);
		}

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
			button_cards[gameNumber][k].setName(pCards[k] + ""+ panel_bigframe[gameNumber].getName());
			button_cards[gameNumber][k].setBounds((80 * k), 70, 75, 100);
			panel_bigframe[gameNumber].add(button_cards[gameNumber][k]);
		}
		for (int k = 9; k > pCards.length - 1; k--) {
			button_cards[gameNumber][k].setVisible(false);
		}
	}

	public void endGame(String gName) {
		int gameNumber = 0;
		for (int i = 0; i < 15; i++) {
			if (string_games[i].equals(gName)) {
				gameNumber = i;
				string_games[gameNumber] = "empty";
			}
		}
		int temp = tabs.getTabCount();
		for (int i = 0; i < temp; i++) {
			if (tabs.getComponentAt(i).getName().equals(gName)) {
				tabs.removeTabAt(i);
				break;
			}
		}
	}

	
	
	public void connectToServer() {
		try {
			String servername = text_loginTextfieldIp.getText();
			string_userName = text_loginTextfieldName.getText();
			int port = Integer.parseInt(text_loginTextfieldPort.getText());
			client = new Socket(servername, port);
			connected = true;
			objectOutput = new ObjectOutputStream(client.getOutputStream());
			objectInput = new ObjectInputStream(client.getInputStream());

		} catch (Exception e) {
//			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server Error..", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean getServerReady() {
		try {

			String serverMsg = (String) objectInput.readObject();
			System.out.println(serverMsg);
			if (serverMsg.equals("RD;")) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "Server is not ready");
				return false;
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Server Error..", "Error!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public boolean Login() {
		StringBuilder sb = new StringBuilder();
		sb.append("LI");
		sb.append(string_userName);
		username = string_userName.toString();
		sb.append(":password;");
		String msg = "";
		try {
			objectOutput.writeObject(sb.toString());
			objectOutput.flush();
			msg = (String) objectInput.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (msg.compareTo("LK;") == 0) {
			return true;
		} else if (msg.compareTo("ER100;") == 0) {
			frame_Welcome.dispose();
			welcomeScreen(1);
			return false;
		} else {
			return false;
		}
	}

	public void send_Message() {
		String text = text_message_in.getText();
		if (text.length() > 0) {
			try {
				textArea_display_in.append("<- " + text + " ->\n");
				text_message_in.setText("");
				objectOutput.writeObject(text);
				objectOutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			text_message_in.setText("");
		}
	}

	public boolean charCompare(char a, char b) {
		String c1 = Character.toString(a);
		String c2 = Character.toString(b);
		return (c1.equals(c2));
	}

	public void run() {
		while (connected) {
			try {
				String line = (String) objectInput.readObject();
				String msg = "!~:;~!";
				if (line.contains(msg)) {
					if (choice) {
						boolean value = true;
						for (int x = 0; x < msg.length(); x++) {
							if (charCompare(line.charAt(x), msg.charAt(x))) {
								continue;
							} else {
								value = false;
								break;
							}
						}
						if (value) {
							line = line.replaceFirst("!", "");
							line = line.replaceFirst("~", "");
							line = line.replaceFirst(":", "");
							line = line.replaceFirst(";", "");
							line = line.replaceFirst("~", "");
							line = line.replaceFirst("!", "");
							clientArr = line.split("~");
							jlist_contactsOutsideMain.setListData(clientArr);
							jlist_contactsOutsideMain.repaint();
						}
					}
				}
				else {
					String command = line.substring(0, 2);
					String[] arguments = line.substring(2).replace(";", "")
							.split(":");
					if (command.compareTo("GK") == 0) {
						frame_CreateGame.dispose();
						startingGame(tempGameName);
						try {
							objectOutput.writeObject("GN" + tempGameName + ";");
							objectOutput.flush();
							gameNotStarted = true;
							new Thread(new Runnable() {
								public void run() {
									while (gameNotStarted) {
										try {
											try {
												Thread.sleep(500);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											if (gameNotStarted) {
												objectOutput.writeObject("GN"
														+ tempGameName + ";");
												objectOutput.flush();
											}
										} catch (SocketException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							}).start();

						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (command.equals("GP")) {
						if (arguments.length == 1) {
							defaultList_players.addElement(arguments[0]);
						} else {
						}
					} else if (command.equals("GZ")) {

					} else if (command.equals("GM")) {
						frame_waitingToStartGame.dispose();
						gameNotStarted = false;
						System.out.println("Game has started");
						newGameGui(tempGameName);
						try {;
							objectOutput.writeObject("GA" + tempGameName + ";");
							objectOutput.flush();
							objectOutput.writeObject("HN" + tempGameName + ";");
							objectOutput.flush();
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(e);
						}
					} else if (command.equals("GQ")) {// Confirm that player has
						// been kicked
						defaultList_players.removeElement(tempKickPlayer);

					} else if (command.equals("GU")) {// List of games
						if (arguments.length > 0) {
							boolean pass = true;
							for (int i = 0; i < arguments.length; i++) {
								if (!arguments[i].equals(tempLastGU[i])) {
									pass = false;
								}
							}
							if (!pass) {// List Was Changed!
								for (int i = 0; i < arguments.length; i++) {
									tempLastGU[i] = (arguments[i]);
								}
								if (!bol_mainFrameActive) {
									// update jlist_contactsOutsideMain

									jlist_gmesListOutMain.removeAll();
									jlist_gmesListOutMain
											.setListData(arguments);

								} else {
									jlist_gameMain.removeAll();
									jlist_gameMain.setListData(arguments);
								}
							}
							// Thread to keep asking for active games every 2
							// seconds

							if (bol_prefix) {
//								System.out.println("IF");
//								System.out.println("Start Sleep");
								Thread.sleep(7000);
//								System.out.println("End Sleep");
								bol_prefix = false;
							} else {
								new Thread(new Runnable() {
									public void run() {
										// need to turn off gameNotStated before
										// thed
										// game starts.
										try {
											Thread.sleep(2000);
											objectOutput.writeObject("GL;");
											objectOutput.flush();
											objectOutput.writeObject("CC;");
											objectOutput.flush();
										} catch (SocketException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}).start();
							}
						}

					} else if (command.equals("GV")) {// truncated games list
						if (arguments.length > 0) {
							boolean pass = true;
							for (int i = 0; i < arguments.length; i++) {
								if (!arguments[i].equals(tempLastGU[i])) {
//									System.out.println("false");
//									System.out.println(arguments[i]);
//									System.out.println(tempLastGU[i]);
									pass = false;
								}
							}
							if (!pass) {// List Was Changed!
//								System.out.println("Changing");
//								System.out.println("Main frame Active? "
//										+ bol_mainFrameActive);
								for (int i = 0; i < arguments.length; i++) {
									tempLastGU[i] = (arguments[i]);
								}
								if (!bol_mainFrameActive) {
									// update jlist_contactsOutsideMain
									jlist_gmesListOutMain.removeAll();
									jlist_gmesListOutMain
											.setListData(arguments);
								} else {
									jlist_gameMain.removeAll();
									jlist_gameMain.setListData(arguments);
								}
							}
						}

					} else if (command.equals("GX")) {// Game joined

						Thread.sleep(300);
						pleaseWork.setVisible(true);

						gameNotStarted = true;
						new Thread(new Runnable() {

							public void run() {
								while (gameNotStarted) {
									try {
										Thread.sleep(500);
										if (gameNotStarted) {//							System.out.println("HN" + tempGameName + ";");
//											System.out.println("GW"
//													+ tempGameName + ";");
											objectOutput.writeObject("GW"
													+ tempGameName + ";");
											objectOutput.flush();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}).start();

					} else if (command.equals("GB")) {// Game begins
						System.out.println("Game Begins");
						gameNotStarted = false;
						newGameGui(tempGameName);
						try {
							// System.out.println("GA"+tempGameName+";");
							objectOutput.writeObject("GA" + tempGameName + ";");
							objectOutput.flush();

							// System.out.println("HN"+tempGameName+";");
							objectOutput.writeObject("HN" + tempGameName + ";");
							objectOutput.flush();
							// Ask player for bid
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(e);
						}
					} else if (command.equals("GZ")) {// Game not ready yet, can
						// be
						// received after a QW
						// command

					} else if (command.equals("GC")) {// players in a specific
														// game
						newGameUpadatePlayers(tempGameName, arguments);

					} else if (command.equals("QK")) {// Acknowledge quit
														// request
						// Client has successfully quit
						String[] emp = { "" };
						jlist_contactsMain.setListData(emp);
						frame_main.repaint();
						endGame(tempGameName);
						tableModel.setRowCount(0);
						gameInProgress = false;
						biddingActive = false;
						bidding = false;
						playedCardsPlacekeeper = 0;
						label_waitingToJoin.setVisible(false);
						pleaseWork.setVisible(false);
						// Check what frames are open, close them
						if (frame_roundWinner.isActive()) {
							frame_roundWinner.dispose();
						}
						if (frame_enterBid.isActive()) {
							frame_enterBid.dispose();
						}
						tempGameName = "";

					} else if (command.equals("CM")) {// Chat to all recieved
						// arguments[0]=sending player name
						// arguments[1]=message
						if (arguments.length < 2) {
							continue;
						}
						if (!arguments[0].equals(username)) {
							if (bol_mainFrameActive) {
								textArea_display_in.append("<" + arguments[0]
										+ ">" + arguments[1] + "\n");
							} else {
								textArea_display_out.append("<" + arguments[0]
										+ ">" + arguments[1] + "\n");
							}
						}
					} else if (command.equals("QP")) {// Someone has quit. Exit
														// game!
						String[] emp = { "" };
						jlist_contactsMain.setListData(emp);
						frame_main.repaint();
						endGame(tempGameName);
						tableModel.setRowCount(0);
						gameInProgress = false;
						bidding = false;
						biddingActive = false;
						playedCardsPlacekeeper = 0;
						label_waitingToJoin.setVisible(true);
						pleaseWork.setVisible(false);
						// Check what screens are open, close them
						if (frame_roundWinner.isActive()) {
							frame_roundWinner.dispose();
						}
						if (frame_enterBid.isActive()) {
							frame_enterBid.dispose();
						}
						tempGameName = "";

					} else if (command.equals("HI")) {
						// argument[0]=round num;

						if (Integer.parseInt(arguments[0]) == totalrounds) {
							lastRound = true;
						} else {
							lastRound = false;
						}
						threadHN = false;
						String[] cards = new String[arguments.length - 3];
						for (int i = 0; i < arguments.length - 3; i++) {
							cards[i] = arguments[i + 1];
						}
						updateGame(tempGameName, cards, null);
						nextPlayerToBid = arguments[arguments.length - 1];
						firstPlayerToPlay = arguments[arguments.length - 1];
//						System.out.println("First Playe to bid:"
//								+ firstPlayerToPlay);
						trumpSuite = arguments[arguments.length - 2];
						if (!trumpSuite.equalsIgnoreCase("x")) {
							// Trump Round
							String temp = "";
							if (trumpSuite.equalsIgnoreCase("C")) {
								temp = "Clubs";
							} else if (trumpSuite.equalsIgnoreCase("S")) {
								temp = "Spades";
							} else if (trumpSuite.equalsIgnoreCase("H")) {
								temp = "Hearts";
							} else if (trumpSuite.equalsIgnoreCase("D")) {
								temp = "Diamods";
							}
							text_fieldTrumpSuite.setText("Trump Suite:" + temp);

						} else {
							text_fieldTrumpSuite.setText("Trump Suite: None");

						}
						roundWinnerFrame = true;
						askForBid(tempGameName);

					} else if (command.equals("HC")) {
						if (arguments.length < 3) {
							continue;
						}
						bids.put(arguments[0], Integer.parseInt(arguments[1]));
						if (arguments[0].equals(username)) {
							// bid was accepted
							// is frame active?
							if (frame_enterBid.isActive()) {
								frame_enterBid.dispose();
								biddingActive = false;
							}
						}
						// check whose turn to bid
						if (firstPlayerToPlay.equals(arguments[2])) {
							// Bids finished and turn to play a card!
//							System.out.println();
//							System.out.println("All Bids Finished!!!");

							for (int i = 0; i < playerCountemp; i++) {
								int info = bids
										.get(plabel_players[i].getText());
								plabel_playersBids[i].setText(info + "");
								tableModel.setValueAt(info + "", i, 2);
							}
							bidding = false;
							turnToPlay.setVisible(false);
							turnToPlayCard = false;
							gameInProgress = true;
							new Thread(new Runnable() {

								public void run() {
									while (gameInProgress) {
										try {
											Thread.sleep(500);
											if (gameInProgress) {
												objectOutput.writeObject("HP"
														+ tempGameName + ";");
												objectOutput.flush();
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}).start();

						} else if (arguments[2].equals(username)) {
							if (!biddingActive) {
								enterBid(tempGameName);
							}
						}
						if (username.equals(firstPlayerToPlay)) {
							turnToPlay.setVisible(true);
							turnToPlayCard = true;
						}

					} else if (command.equals("HL")) {
						if (arguments.length < 2) {
							continue; // card,
						}	
						if (arguments[0].equals(username)) {
							button_cards[cardClickedI][cardClickedJ]
									.setIcon(new ImageIcon("src/cards/back.gif"));
						}
						if (line.equals(tempLastPlayer)) {
						} else {
							if (playedCardsPlacekeeper == playerCountemp) {

								if (arguments.length == 3) {
									onlyEndOfTrick = true;
									if (arguments[2].equals(username)) {
										turnToPlayCard = true;
									} else {
										turnToPlayCard = false;
									}
									objectOutput.writeObject("HS"
											+ tempGameName + ";");
									objectOutput.flush();
								}
								for (int y = 0; y < playerCountemp; y++) {
									System.out.print(y + " ");
									button_playedCards[y].setVisible(false);
									label_playedCards[y].setVisible(false);
								}
								// set placekeeper to zero
								playedCardsPlacekeeper = 0;
							}
							int gameNumber = 0;
							for (int i = 0; i < 15; i++) {
								if (string_games[i].equals(tempGameName)) {
									gameNumber = i;
									break;
								}
							}
							// System.out.println("Print Card,"+gameNumber);
							tempLastPlayer = line;
							Icon icon = new ImageIcon("src/cards/"
									+ arguments[1] + ".gif");

							button_playedCards[playedCardsPlacekeeper]
									.setIcon(icon);
							button_playedCards[playedCardsPlacekeeper]
									.setBounds((80 * playedCardsPlacekeeper),
											190, 75, 100);
							button_playedCards[playedCardsPlacekeeper]
									.setEnabled(false);
							button_playedCards[playedCardsPlacekeeper]
									.setVisible(true);

							label_playedCards[playedCardsPlacekeeper]
									.setText(arguments[0]);
							label_playedCards[playedCardsPlacekeeper]
									.setBounds(
											(80 * playedCardsPlacekeeper) + 8,
											167, 75, 25);
							label_playedCards[playedCardsPlacekeeper]
									.setVisible(true);
							panel_bigframe[gameNumber]
									.add((label_playedCards[playedCardsPlacekeeper]));

							panel_bigframe[gameNumber]
									.add(button_playedCards[playedCardsPlacekeeper]);
							panel_bigframe[gameNumber].repaint();
							playedCardsPlacekeeper++;
							if (playedCardsPlacekeeper == playerCountemp) {
								if (arguments.length == 3) {
									onlyEndOfTrick = true;
									objectOutput.writeObject("HS"
											+ tempGameName + ";");
									objectOutput.flush();
								}
							}
						}
						if (arguments.length == 2) {
							onlyEndOfTrick = false;
							if (lastRound) {
								gameInProgress = false;
							} else {
								gameInProgress = false;
								try {
									objectOutput.writeObject("HS"
											+ tempGameName + ";");
									objectOutput.flush();
									objectOutput.writeObject("HA"
											+ tempGameName + ";");
									objectOutput.flush();
									threadHN = true;
									new Thread(new Runnable() {
										public void run() {
											while (threadHN) {
												try {
													Thread.sleep(500);
													if (threadHN) {
														objectOutput
																.writeObject("HN"
																		+ tempGameName
																		+ ";");
														objectOutput.flush();
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										}
									}).start();
									objectOutput.writeObject("HN"
											+ tempGameName + ";");
									objectOutput.flush();
								} catch (Exception e) {
									System.out.println(e);
								}
							}
							if (username.equals(firstPlayerToPlay)) {
								turnToPlay.setVisible(true);
								turnToPlayCard = true;
							}
						} else if (username.equals(arguments[2])) {
							turnToPlay.setVisible(true);
							turnToPlayCard = true;
						} else {
							turnToPlayCard = false;
							turnToPlay.setVisible(false);
						}
					} else if (command.equals("HW")) {						
					} else if (command.equals("HO")) {
						for (int i = 0; i < arguments.length; i = i + 3) {

							String[] tempMoving = new String[4];

							tempMoving = scores.get(arguments[i]).split(":");
							System.out.println("Temp Moving:"+tempMoving[0]+","+tempMoving[1]+","+tempMoving[2]+","+tempMoving[3]);
							if (!onlyEndOfTrick) {
								tempMoving[1] = (Integer.parseInt(arguments[i + 2])+Integer.parseInt(tempMoving[1]))+"";
								tempMoving[2] = arguments[i + 1];
								tempMoving[0] = tempMoving[1];
								tempMoving[3] = (Integer.parseInt(tempMoving[3])+Integer.parseInt(tempMoving[2]))+"";								
							}
							StringBuilder s = new StringBuilder();
							s.append(tempMoving[0]);
							s.append(":");
							s.append(tempMoving[1]);
							s.append(":");
							s.append(tempMoving[2]);
							s.append(":");
							s.append(tempMoving[3]);
							scores.put(arguments[i], s.toString());
						}
						if (onlyEndOfTrick) {
							updateScoresInGame(arguments);
						} else {
							if (!lastRound) {
								if (roundWinnerFrame) {
									roundWinner();
								}

							} else {
								if (roundWinnerFrame) {
									roundWinner();
								}
							}
						}
					} else if (command.equals("LM")) {
						logoutConfirmed();
					} else if (command.equals("ML")) {
						for (int i = 0; i < arguments.length; i++) {
							if (arguments[i].equals("HN")) {
								arguments[i] = "Request new hand from server";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							} else if (arguments[i].equals("")) {
								arguments[i] = "";
							}
						}
					} else if (command.equals("ER")) {
						if (arguments[0].equals("100")) {
						} else if (arguments[0].equals("101")) {

						} else if (arguments[0].equals("102")) {

							System.out.println("Logoff unsuccessful, not logged in.");

						} else if (arguments[0].equals("110")) {
							System.out.println("Not part of a game.");
						} else if (arguments[0].equals("120")) {
							System.out.println("Game identifier already taken");
						} else if (arguments[0].equals("130")) {
							System.out.println("Game has too many players.");
						} else if (arguments[0].equals("131")) {
							System.out.println("Game has too few players.");
						} else if (arguments[0].equals("132")) {
							pleaseWork.setVisible(false);
							System.out.println("Kicked out player not part of game");
						} else if (arguments[0].equals("133")) {
							System.out.println("No such game identifier");
						} else if (arguments[0].equals("134")) {
							pleaseWork.setVisible(false);
							System.out.println("Player has been kicked out of game.");
						} else if (arguments[0].equals("139")) {
							System.out.println("Player in game has closed connection. Game abandoned.");
						} else if (arguments[0].equals("140")) {
							System.out.println("Illegal bid (bid is higher than the number of cards in this hand)");
							bidding = true;
							enterBid(tempGameName);
						} else if (arguments[0].equals("141")) {
							System.out.println("Illegal card (player does not have this card).");
						} else if (command.equals("150")) {
							System.out.println("One or more of the player names does not exist, chat not delivered to those players");
						} else if (command.equals("900")) {
							System.out.println("Bad message format");
						} else if (command.equals("901")) {
							System.out.println("Unexpected request");
						} else if (command.equals("910")) {
							System.out.println("Something very bad but unspecified has happened");
						}
					} else if (line.charAt(0) == 'L' && line.charAt(1) == 'C') {
						line = line.replaceFirst("L", "");
						line = line.replaceFirst("C", "");
						names = line.split(" ");
						String n = Boolean.toString(bol_mainFrameActive);
						System.out.println(n);
						if (bol_mainFrameActive) {
							jlist_contactsMain.setListData(names);
							frame_main.repaint();
						} else {
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logoutConfirmed();
				System.out.println("ERROR");
				System.exit(0);
			}
		}
	}


	public void askForBid(String gameName) {
		System.out.println("AskForBid");
		System.out.println(username);
		System.out.println(firstPlayerToPlay);
		bidding = true;
		new Thread(new Runnable() {
			public void run() {
				while (bidding) {
					try {
						Thread.sleep(500);
						if (bidding) {
							System.out.println("HB" + tempGameName + ";");
							objectOutput.writeObject("HB" + tempGameName + ";");
							objectOutput.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		if (username.equals(firstPlayerToPlay)) {
			// Bid
			System.out.println("BidNBow");
			enterBid(gameName);

		} else {
			try {
				System.out.println("HB" + gameName + ";");
				objectOutput.writeObject("HB" + gameName + ";");
				objectOutput.flush();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	void enterBid(String gameName) {
		biddingActive = true;
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

		frame_enterBid = new JFrame();
		frame_enterBid.setSize(400, 140);
		frame_enterBid.setLocation(frame_main.getX(), frame_main.getY());
		frame_enterBid.setEnabled(true);

		panel_enterBid = new JPanel();
		panel_enterBid.setSize(frame_enterBid.getWidth(),
				frame_enterBid.getHeight());
		panel_enterBid.setLayout(null);
		panel_enterBid.setBackground(Color.white);

		text_FieldEnterBid = new JTextField();
		text_FieldEnterBid.setSize(80, 30);
		text_FieldEnterBid.setLocation(180, 10);

		label_enterBid = new JLabel();
		label_enterBid.setSize(150, 30);
		label_enterBid.setLocation(10, 10);
		label_enterBid.setText("Enter your bid:");

		button_enterBid = new JButton();
		button_enterBid.setSize(140, 40);
		button_enterBid.setLocation(130, 65);
		button_enterBid.setText("Bid");
		button_enterBid.addActionListener(this);

		panel_enterBid.add(text_FieldEnterBid);
		panel_enterBid.add(label_enterBid);
		panel_enterBid.add(button_enterBid);
		frame_enterBid.add(panel_enterBid);
		System.out.println(("bidding thing open"));
		frame_enterBid.setVisible(true);
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
		button_createNewGame.addActionListener(this);

		panel_CreateGame.add(text_FieldEnterNewGameName);
		panel_CreateGame.add(label_enterNewGameName);
		panel_CreateGame.add(button_createNewGame);
		frame_CreateGame.add(panel_CreateGame);
		frame_CreateGame.setVisible(true);
	}

	// Winner of round
	@SuppressWarnings("rawtypes")
	void roundWinner() {
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
		roundWinnerFrame = false;

		winnerOfRound.setSize(200, 30);
		winnerOfRound.setLocation(130, 0);
		int tempRoundWinnerScore = 0;
		int winners = 0;

		frame_roundWinner.setSize(400, 250);
		frame_roundWinner.setLocation(frame_main.getX(), frame_main.getY());
		frame_roundWinner.setEnabled(true);

		panel_roundWinner.setSize(frame_roundWinner.getWidth(),
				frame_roundWinner.getHeight());
		panel_roundWinner.setLayout(null);
		panel_roundWinner.setBackground(Color.white);

		System.out.println("Player Count: " + playerCountemp);

		text_roundWinner[0][0].setText("Username");
		text_roundWinner[0][0].setSize(150, 30);
		text_roundWinner[0][0].setLocation(3, 10);
		panel_roundWinner.add(text_roundWinner[0][0]);

		text_roundWinner[0][1].setText("Bid");
		text_roundWinner[0][1].setSize(150, 30);
		text_roundWinner[0][1].setLocation(80, 10);
		panel_roundWinner.add(text_roundWinner[0][1]);

		text_roundWinner[0][2].setText("Tricks Won");
		text_roundWinner[0][2].setSize(150, 30);
		text_roundWinner[0][2].setLocation(140, 10);
		panel_roundWinner.add(text_roundWinner[0][2]);

		text_roundWinner[0][3].setText("Round Score");
		text_roundWinner[0][3].setSize(150, 30);
		text_roundWinner[0][3].setLocation(210, 10);
		panel_roundWinner.add(text_roundWinner[0][3]);

		text_roundWinner[0][4].setText("Total Score");
		text_roundWinner[0][4].setSize(150, 30);
		text_roundWinner[0][4].setLocation(300, 10);
		panel_roundWinner.add(text_roundWinner[0][4]);
		// get all usernames
		String[] names = new String[playerCountemp];
		int p = 0;
		for (Enumeration e = scores.keys(); e.hasMoreElements();) {
			String playerName = (String) e.nextElement();
			names[p] = playerName;
			System.out.println(names[p]);
			p++;
		}

		for (int i = 1; i < playerCountemp + 1; i++) {

			String[] info = new String[4];
			info = scores.get(names[i - 1]).split(":");
			System.out.println("---------------------------------------");
			System.out.println(scores.get(names[i - 1]));
			System.out.print(info[0] + " " + info[1] + " " + info[2] + " "
					+ info[3]);
			System.out.println("---------------------------------------");
			text_roundWinner[i][0].setText(names[i - 1]);
			text_roundWinner[i][0].setSize(100, 30);
			text_roundWinner[i][0].setLocation(3, 10 + i * 20);
			panel_roundWinner.add(text_roundWinner[i][0]);

			text_roundWinner[i][1].setText(bids.get(names[i - 1]) + "");
			text_roundWinner[i][1].setSize(100, 30);
			text_roundWinner[i][1].setLocation(80, 10 + i * 20);
			panel_roundWinner.add(text_roundWinner[i][1]);

			text_roundWinner[i][2].setText((Integer.parseInt(info[2]) ) + "");
	
			text_roundWinner[i][2].setSize(100, 30);
			text_roundWinner[i][2].setLocation(140, 10 + i * 20);
			panel_roundWinner.add(text_roundWinner[i][2]);


			text_roundWinner[i][3].setText((Integer.parseInt(info[0]) ) + "");
			text_roundWinner[i][3].setSize(100, 30);
			text_roundWinner[i][3].setLocation(210, 10 + i * 20);
			panel_roundWinner.add(text_roundWinner[i][3]);

			text_roundWinner[i][4].setText(Integer.parseInt(info[3]) + "");
			text_roundWinner[i][4].setSize(100, 30);
			text_roundWinner[i][4].setLocation(300, 10 + i * 20);
			panel_roundWinner.add(text_roundWinner[i][4]);
			tableModel.setValueAt("0", i - 1, 1);
			plabel_playersScores[i - 1].setText("0");
			tempLastTrickScore[i - 1] = Integer.parseInt(info[3]);
			System.out.println("tempScore at i=" + i + " is "
					+ tempLastTrickScore[i - 1]);

			if ((Integer.parseInt(info[1]) - Integer.parseInt(info[0])) > tempRoundWinnerScore) {
				// new highest score;
				tempRoundWinnerScore = Integer.parseInt(info[1])
						- Integer.parseInt(info[0]);
				winnerOfRound.setText("Winner of round is " + names[i - 1]);
				frame_roundWinner.repaint();
				winners = 1;

			} else if (((Integer.parseInt(info[1]) - Integer.parseInt(info[0])) == tempRoundWinnerScore)
					&& (tempRoundWinnerScore != 0)) {
				// Check if another player got the same score, then there is no
				// hand winner
				System.out.println("Two players have the same score");
				winners++;
			}
		}
		if (winners > 1) {
			System.out.println("No winner for this hand");
			winnerOfRound.setText("No winner for this hand");
			frame_roundWinner.repaint();

		}

		button_closeWinner.setSize(140, 40);
		button_closeWinner.setLocation(130, 200);
		button_closeWinner.setText("Close");
		button_closeWinner.addActionListener(this);

		panel_roundWinner.add(winnerOfRound);
		panel_roundWinner.add(button_closeWinner);
		frame_roundWinner.add(panel_roundWinner);
		frame_roundWinner.setVisible(true);
		frame_roundWinner.setAlwaysOnTop(true);

	}

	/**
	 * 
	 */
	public void logoutRequest() {
		StringBuilder sb = new StringBuilder();
		sb.append("LO;");
		try {
			objectOutput.writeObject(sb.toString());
			objectOutput.flush();
		} catch (IOException e) {
			System.out.println("Logoff fail");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void logoutConfirmed() {

		try {
			connected = false;
			bidding = false;

			String[] emp = { "" };
			jlist_contactsMain.setListData(emp);

			jlist_contactsMain.removeAll();

			frame_main.repaint();
			endGame(tempGameName);
			tableModel.setRowCount(0);
			gameInProgress = false;
			biddingActive = false;
			playedCardsPlacekeeper = 0;
			label_waitingToJoin.setVisible(true);
			pleaseWork.setVisible(false);
			// check what frames are open, close them
			// bidding androundwinner
			if (frame_roundWinner.isActive()) {
				frame_roundWinner.dispose();
			}
			if (frame_enterBid.isActive()) {
				frame_enterBid.dispose();
			}
			tempGameName = "";
			frame_main.dispose();
			closeConnections();

		} catch (Exception e) {
			closeConnections();
		}

	}

	public void closeConnections() {
		try {
			connected = false;
			objectInput.close();
			objectInput.close();

			if (client.isConnected()) {
				client.close();
			}
			interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == button_login) {
			String testEmpty = text_loginTextfieldName.getText().replace(" ",
					"");
			if (text_loginTextfieldName.getText().equals("")) {
				frame_Welcome.dispose();
				welcomeScreen(2);
			} else if (testEmpty.equals("")) {
				frame_Welcome.dispose();
				welcomeScreen(3);
			} else {
				connectToServer();
				if (getServerReady()) {
					if (Login()) {
						System.out.println("LK sent");
						afterLoginScreen();
						frame_Welcome.dispose();
						start();
					}
				}
			}

		}
		// Protocol
		// client -> server: CAgame_name:message;
		// server -> client: CG;

		else if (evt.getSource() == button_sendMessage_in) {

			String text = text_message_in.getText();
			if ((text.length() > 0) && (tabs.getComponentCount() != 0)) {
				// textArea_display_in.append("<" + text + ">\n");
				if (text.charAt(0) == '@') {
					textArea_display_in.append("<");
					// Private Message
					System.out.println(text);
					String[] arguments = text.replace(":", "@").split("@");
					StringBuilder sb = new StringBuilder();
					sb.append("CP");
					for (int i = 1; i < arguments.length - 1; i++) {
						sb.append(arguments[i]);
						sb.append(":");
						textArea_display_in.append("@" + arguments[i]);
					}
					textArea_display_in.append(">");
					textArea_display_in.append(arguments[arguments.length - 1]
							+ "\n");
					sb.append(arguments[arguments.length - 1] + ";");
					try {
						System.out.println(sb.toString());
						objectOutput.writeObject(sb.toString());
						objectOutput.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					text_message_in.setText("");
				} else {
					// Send to all
					StringBuilder sb = new StringBuilder();
					sb.append("CA");
					sb.append(tabs.getSelectedComponent().getName());
					sb.append(":");
					sb.append(text);
					sb.append(";");
					try {
						System.out.println(sb.toString());
						objectOutput.writeObject(sb.toString());
						objectOutput.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					text_message_in.setText("");
				}

			} else {
				text_message_in.setText("");
			}
		}

		else if (evt.getSource() == button_joinGame_out) {

			// Join game selected in list

			try {

				if (!jlist_gmesListOutMain.isSelectionEmpty()) {
					tempGameName = jlist_gmesListOutMain.getSelectedValue()
							.toString();
					System.out.println(tempGameName);
					objectOutput.writeObject("GJ"
							+ jlist_gmesListOutMain.getSelectedValue()
									.toString() + ";");
					objectOutput.flush();
					clientGui();
					System.out.println("GJ"
							+ jlist_gmesListOutMain.getSelectedValue()
									.toString() + ";");
					frame_choice.dispose();
					choice = false;

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			// getClients();
			// names = getClients();
			// jlist_contactsMain.setListData(names);
			// frame_main.repaint();
		}
		// Protocol
		// client -> server: GSgame_name;
		// server -> client: GK;
		else if (evt.getSource() == button_createNewGame) {
			// Send to server
			StringBuilder sb = new StringBuilder();
			sb.append("GS");
			tempGameName = text_FieldEnterNewGameName.getText();
			sb.append(tempGameName);
			sb.append(";");
			System.out.println(sb.toString());

			try {
				objectOutput.writeObject(sb.toString());
				objectOutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (evt.getSource() == button_newGame_out) {
			frame_choice.dispose();
			choice = false;

			clientGui();
			createGame();

		} // button_closeWinner,panel_roundWinner
		else if (evt.getSource() == button_closeWinner) {
			frame_roundWinner.dispose();
			roundWinnerFrame = true;

		} else if (evt.getSource() == button_prefixOut) {
			// text_FieldPrefixOut
			if (text_FieldPrefixOut.getText().length() != 0) {
				// Send prefix command
				try {
					bol_prefix = true;
					System.out.println("GG" + text_FieldPrefixOut.getText()
							+ ";");
					objectOutput.writeObject("GG"
							+ text_FieldPrefixOut.getText() + ";");
					objectOutput.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (evt.getSource() == button_prefixIn) {
			// text_FieldPrefixOut
			if (text_FieldPrefixIn.getText().length() != 0) {
				// Send prefix command
				try {
					bol_prefix = true;
					System.out.println("GG" + text_FieldPrefixIn.getText()
							+ ";");
					objectOutput.writeObject("GG"
							+ text_FieldPrefixIn.getText() + ";");
					objectOutput.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		else if (evt.getSource() == button_listPlayers) {
			textArea_display_in.append("List Of Players Button Pressed\n");
		} else if (evt.getSource() == button_exitGame) {
			textArea_display_in.append("QTgame_name\n");

			try {
				System.out.println("QT" + tempGameName + ";");
				objectOutput.writeObject("QT" + tempGameName + ";");
				objectOutput.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (evt.getSource() == button_kickPlayer) {
			System.out.println("Kick player");

			try {
				if (!jlist_playersJoiningGame.isSelectionEmpty()) {
					StringBuilder sb = new StringBuilder();
					sb.append("GO");
					sb.append(tempGameName + ":");
					sb.append(defaultList_players.getElementAt(
							jlist_playersJoiningGame.getSelectedIndex())
							.toString());
					sb.append(";");
					tempKickPlayer = defaultList_players.getElementAt(
							jlist_playersJoiningGame.getSelectedIndex())
							.toString();
					System.out.println(sb.toString());
					objectOutput.writeObject(sb.toString());
					objectOutput.flush();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		else if (evt.getSource() == button_sendMessage_out) {
			String text = text_message_out.getText();
			if ((text.length() > 0)) {
				if (text.charAt(0) == '@') {
					textArea_display_out.append("<");
					System.out.println(text);
					String[] arguments = text.replace(":", "@").split("@");
					StringBuilder sb = new StringBuilder();
					sb.append("CP");
					for (int i = 1; i < arguments.length - 1; i++) {
						sb.append(arguments[i]);
						sb.append(":");
						textArea_display_out.append("@" + arguments[i]);
					}
					sb.append(arguments[arguments.length - 1] + ";");
					textArea_display_out.append(">");
					textArea_display_out.append(arguments[arguments.length - 1]
							+ "\n");
					try {
						System.out.println(sb.toString());
						objectOutput.writeObject(sb.toString());
						objectOutput.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					text_message_out.setText("");
				} else {

					text_message_out.setText("");
				}

			} else {
				text_message_out.setText("");
			}
		} else if (evt.getSource() == button_listGames) {
			try {
				if (!jlist_gameMain.isSelectionEmpty()) {
					if ((!jlist_gameMain.getSelectedValue().toString()
							.equals(tempGameName))
							&& (!gameInProgress)) {
						tempGameName = jlist_gameMain.getSelectedValue()
								.toString();

						System.out.println(tempGameName);
						objectOutput.writeObject("GJ"
								+ jlist_gameMain.getSelectedValue().toString()
								+ ";");
						objectOutput.flush();
						System.out.println("GJ"
								+ jlist_gameMain.getSelectedValue().toString()
								+ ";");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (evt.getSource() == button_history) {
			textArea_display_in.append("History Pressed\n");
		} else if (evt.getSource() == button_enterBid) {
			try {
				int temp_bid = Integer.parseInt(text_FieldEnterBid.getText());
				StringBuilder sb = new StringBuilder();
				sb.append("HD" + tempGameName);
				sb.append(":");
				sb.append(temp_bid);
				sb.append(";");
				try {
					System.out.println(sb.toString());
					objectOutput.writeObject(sb.toString());
					objectOutput.flush();
				} catch (Exception e) {
					System.out.println(e);
				}
			} catch (Exception e) {
				System.out.println("Not a number!");
				text_FieldEnterBid.setText("");
			}
		}

		else if (evt.getSource() == button_createGame) {
			textArea_display_in.append("Create new Game Screen\n");
			createGame();

		} else if (evt.getSource() == button_startCreatedGame) {
			try {
				objectOutput.writeObject("GF" + tempGameName + ";");
				objectOutput.flush();
				System.out.println("GF" + tempGameName);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (evt.getSource() == button_logoff) {
			logoutRequest();
		} else {
			if (turnToPlayCard && gameInProgress) {
				for (int i = 0; i < 15; i++) {
					for (int j = 0; j < 10; j++) {
						if (evt.getSource().equals(button_cards[i][j])) {
							cardClickedJ = j;
							cardClickedI = i;
						}
					}
				}
				String temp = evt.toString().substring(
						evt.toString().indexOf(" on") + 4);
				String card = temp.substring(0, 2);
				String game = temp.substring(2);
				StringBuilder sb = new StringBuilder();
				sb.append("HR");
				sb.append(game + ":");
				sb.append(card + ";");
				try {
					objectOutput.writeObject(sb.toString());
					objectOutput.flush();
					System.out.println(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				turnToPlay.setVisible(false);
				turnToPlayCard = false;
			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		if (bol_mainFrameActive) {
			if (jlist_contactsMain.getComponentCount() > 0) {
				System.out.println("Clicked Name: "
						+ jlist_contactsMain.getSelectedValue().toString());
				String curText = text_message_in.getText();
				if (!jlist_contactsMain.getSelectedValue().toString()
						.equals(username)) {
					if (curText.length() != 0) {
						if ((curText.charAt(0) == '@')
								&& (curText.charAt(curText.length() - 1) == ':')) {
							System.out
									.println("Already a private chat, add another user");
							String[] arguments = curText.replace(":", "@")
									.split("@");
							boolean add = true;
							for (int i = 0; i < arguments.length; i++) {
								System.out.println(arguments[0]);
								if (jlist_contactsMain.getSelectedValue()
										.toString().equals(arguments[i])) {
									add = false;
								}
							}
							if (add) {
								curText = curText.substring(0,
										curText.length() - 1);
								text_message_in.setText(curText
										+ "@"
										+ jlist_contactsMain.getSelectedValue()
												.toString() + ":");
							}

						}
					} else {
						text_message_in.setText("@"
								+ jlist_contactsMain.getSelectedValue()
										.toString() + ":");
					}
				}
			}
		} else {
			if (jlist_contactsOutsideMain.getComponentCount() > 0) {
				System.out.println("Clicked Name: "
						+ jlist_contactsOutsideMain.getSelectedValue()
								.toString());
				String curText = text_message_out.getText();
				if (!jlist_contactsOutsideMain.getSelectedValue().toString()
						.equals(username)) {
					if (curText.length() != 0) {
						if ((curText.charAt(0) == '@')
								&& (curText.charAt(curText.length() - 1) == ':')) {
							System.out.println("Already a private chat, add another user");
							String[] arguments = curText.replace(":", "@")
									.split("@");
							boolean add = true;
							for (int i = 0; i < arguments.length; i++) {
								System.out.println(arguments[0]);
								if (jlist_contactsOutsideMain
										.getSelectedValue().toString()
										.equals(arguments[i])) {
									add = false;
								}
							}
							if (add) {
								curText = curText.substring(0,
										curText.length() - 1);
								text_message_out.setText(curText
										+ "@"
										+ jlist_contactsOutsideMain
												.getSelectedValue().toString()
										+ ":");
							}
						}
					} else {
						text_message_out.setText("@"
								+ jlist_contactsOutsideMain.getSelectedValue()
										.toString() + ":");
					}
				}
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}

	public void mouseReleased(MouseEvent arg0) {}

	public void valueChanged(ListSelectionEvent arg0) {
	}
}
