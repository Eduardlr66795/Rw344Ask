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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class Client extends Thread implements ActionListener, ListSelectionListener,MouseListener {

		//new--------------------------------
		public JButton button_startCreatedGame;
		public JList jlist_playersJoiningGame;
		public JLabel label_NameOfGameToBeCreated;
		public JButton button_kickPlayer;
		public JPanel panel_waitingToStartGame;
		public JFrame frame_waitingToStartGame;
		public DefaultListModel defaultList_players;
		public DefaultListModel defaultList_games;
		public JList jlist_availableGames;
		public boolean gameNotStarted=true;
		public String username;
		public String round_number;
		public JPanel panel_enterBid;
		public JTextField text_FieldEnterBid;
		public JLabel label_enterBid;
		public JButton button_enterBid;
		public JFrame frame_enterBid;
		public JLabel usernameForMainFrame;
		public JLabel turnToPlay;
		public JLabel text_fieldTrumpSuite;
		public int playedCardsPlacekeeper=0;
		public int playerCountemp=0;
		public int cardClickedJ;
		public int cardClickedI;
		public int totalrounds=0;
		
		public String nextPlayerToBid="";
		public String firstPlayerToPlay;
		public String trumpSuite;
		public String tempLastPlayer="";
        // Global Variables--------------------------------
        // Frames
        public JFrame frame_main;
        public JFrame frame_choice;
        public JFrame frame_CreateGame;
        public JFrame frame_Welcome;
        
        public Hashtable <String, Integer> scores = new Hashtable <String, Integer>();
        public Hashtable <String, Integer> bids = new Hashtable <String, Integer>();
        
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
        public JButton[] button_playedCards;
        

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
        public JLabel[] plabel_players;

        // TextFields
        public JTextField text_loginTextfieldName;
        public JTextField text_loginTextfielIp;
        public JTextField text_message_in;
        public JTextField text_message_out;
        public JTextField text_FieldEnterNewGameName;
        
        // Strings
        public String string_userName;
        public String[] string_games;
        public String names[] = null;
        public String tempGameName;
        public String tempKickPlayer;
        public String updateTempGameName;
        public String recentHB;
        
        public JFrame frame_roundWinner;
        public JPanel panel_roundWinner;
        public JButton button_closeWinner;
        
        // Other
        private Socket client;
        private int port = 9119;
        private ObjectInputStream objectInput;
        private ObjectOutputStream objectOutput;
        public boolean bol_mainFrameActive;
        public boolean bidding;
        public boolean biddingActive;
        public boolean turnToPlayCard;
        public boolean gameInProgress=false;
        public boolean threadHN=false;
        public boolean lastRound=false;
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
//                clientGui();
        }
        public void startingGame(String GameName){
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
        		defaultList_players=new DefaultListModel ();
	        	button_startCreatedGame=new JButton();
	    		jlist_playersJoiningGame=new JList(defaultList_players);
	    		label_NameOfGameToBeCreated=new JLabel();
	    		button_kickPlayer=new JButton();
	    		panel_waitingToStartGame=new JPanel();
	    		frame_waitingToStartGame=new JFrame();
	    		panel_waitingToStartGame.setLayout(null);

	    		frame_waitingToStartGame.setSize(300, 350);
	    		frame_waitingToStartGame.setLocation(frame_main.getX(), frame_main.getY());
	    		frame_waitingToStartGame.setEnabled(true);
	    		
	    		jlist_playersJoiningGame.setLocation(10, 50);
	    		jlist_playersJoiningGame.setSize(150, 200);

	    		
	    		jlist_playersJoiningGame.repaint();
	    		jlist_playersJoiningGame.setVisible(true);

	    		panel_waitingToStartGame.setSize(frame_CreateGame.getWidth(),frame_CreateGame.getHeight());
	    		panel_waitingToStartGame.setLayout(null);
	    		panel_waitingToStartGame.setBackground(Color.white);
		
	    		label_NameOfGameToBeCreated.setSize(150, 30);
	    		label_NameOfGameToBeCreated.setLocation((frame_waitingToStartGame.getWidth()/2)-30, 10);
	    		label_NameOfGameToBeCreated.setText(tempGameName);
	    		
	    		button_kickPlayer.setSize(120, 40);
	    		button_kickPlayer.setText("Kick Player");
	    		button_kickPlayer.setLocation(180, 80);
	    		button_kickPlayer.addActionListener(this);
	    		
	    		button_startCreatedGame.setSize(140, 40);
	    		button_startCreatedGame.setLocation((frame_waitingToStartGame.getWidth()/2)-(button_startCreatedGame.getWidth()/2), 270);
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
                button_sendMessage_out = new JButton("Refresh Games");
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
                JScrollPane sp1 = new JScrollPane(jlist_contactsOutsideMain = new JList());
                jlist_contactsOutsideMain.setVisibleRowCount(4);
                jlist_contactsOutsideMain.addListSelectionListener(this);
                jlist_contactsOutsideMain.setBackground(Color.LIGHT_GRAY);
                sp1.setBounds(420, 90, 150, 270);
                panel_main.add(sp1);
//                getClients();
                
                // TextField: Join Existing Game
                text_message_out = new JTextField();
                text_message_out.setBounds(15, 270, 400, 30);
                panel_main.add(text_message_out);

                frame_choice.add(panel_main);
                frame_choice.setVisible(true);
                frame_choice.setDefaultCloseOperation(frame_choice.EXIT_ON_CLOSE);
                
                try {													
					if(!bol_mainFrameActive){
						//System.out.println("GL;");
                        objectOutput.writeObject("GL;");
					}																											
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
        }

        //Protocol
        //Once a game has begun, a player (either a joining player or the initiating player) 
        //may request a list of the other players. The server responds with a list of players.
        //client -> server: GAgame_name;
        //server -> client: GCplayer_name1:player_name2:...;
        public void getClients()
        {
                //Write to server
                try {
                        //There is no LC; command
                        objectOutput.writeObject("LC;");

                } catch (IOException e) {
                        e.printStackTrace();
                }
                
                
        }
        
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
                button_playedCards=new JButton[7];
                button_cards = new JButton[15][10];
                panel_bigframe = new JPanel[40];
                tabs = new JTabbedPane();
                frame_main = new JFrame();
                plabel_players=new JLabel[15];
                frame_main.setSize(1000, 500);
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
                text_fieldTrumpSuite=new JLabel();
                
                frame_main.setTitle("Example GUI");
                text_message_in = new JTextField();
                text_message_in.setSize(700, 30);
                text_message_in.setLocation(3, 460);
                frame_main.add(text_message_in);
                
                

                JScrollPane spMain = new JScrollPane(textArea_display_in = new JTextArea());
                spMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                spMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                spMain.setBounds(3, 330, 800, 130);
                textArea_display_in.setEditable(false);
                frame_main.add(spMain);
        
                defaultList_games=new DefaultListModel();
                JScrollPane sp1 = new JScrollPane(jlist_contactsMain = new JList(defaultList_games));
                jlist_contactsMain.setVisibleRowCount(4);
                jlist_contactsMain.addListSelectionListener(this);
                jlist_contactsMain.setBackground(Color.LIGHT_GRAY);
                jlist_contactsMain.addMouseListener(this);
                
                sp1.setBounds(820, 40, 160, 300);
                frame_main.add(sp1);
                
                
        
                
                button_sendMessage_in = new JButton("Send Msg");
                button_sendMessage_in.setBounds(709, 460, 93, 30);
                //button_sendMessage_in.setBounds(709, 360, 93, 30);
                button_sendMessage_in.addActionListener(this);
                frame_main.add(button_sendMessage_in);
                
                button_listPlayers = new JButton("List of Players");
                button_listPlayers.setBounds(820, 380, 160, 25);
                //button_listPlayers.setBounds(820, 280, 160, 25);
                button_listPlayers.addActionListener(this);
                frame_main.add(button_listPlayers);

                button_listGames = new JButton("List Games");
                button_listGames.setBounds(820, 400, 160, 25);
                //button_listGames.setBounds(820, 300, 160, 25);
                button_listGames.addActionListener(this);
                frame_main.add(button_listGames);

                button_history = new JButton("History");
                button_history.setBounds(820, 420, 160, 25);
                //button_history.setBounds(820, 320, 160, 25);
                button_history.addActionListener(this);
                frame_main.add(button_history);

                button_logoff = new JButton("Log Off");
                button_logoff.setBounds(820, 440, 160, 25);
                //button_logoff.setBounds(820, 340, 160, 25);
                button_logoff.addActionListener(this);
                frame_main.add(button_logoff);

                button_createGame = new JButton("Create Game");
                button_createGame.setBounds(820, 460, 160, 25);
                //button_createGame.setBounds(820, 360, 160, 25);
                button_createGame.addActionListener(this);
                frame_main.add(button_createGame);

                tabs.setLocation(3, 8);
                tabs.setSize(990, 350);
                frame_main.add(tabs);
                frame_main.setLocationRelativeTo(null);
                frame_main.setVisible(true);

                
                bol_mainFrameActive = true;
                // Just for
                // testing--------------------------------------------------------------------
                /*
                String[] test1 = { "9s", "th", "2c", "4s", "6c", "7c", "9h", "qd",
                                "ks", "as" };
                String[] test2 = { "7s", "3h", "qc", "8s", "2c", "3c", "kh" };
                String[] test3 = { "qs", "th" };
                String[] names1 = { "Paul", "Luke", "Michael", "Kristo", "James",
                                "Gerrit", "Jukkie" };
                String[] names2 = { "Michael", "James", "Kristo", "Gerrit", "Jukkie",
                                "Luke" };
                String[] names3 = { "Michael", "James", "Kristo" };
                newGameGui("Friendly");
                newGameGui("Fun");
                newGameGui("Hello");
                newGameGui("G3");
                updateGame("Friendly", test3, null);
                updateGame("Fun", test1, null);
                updateGame("G3", test2, null);
                endGame("G3");
                newGameGui("Test2");
                newGameGui("Test3");
                updateGame("Test2", test1, null);
                updateGame("Test3", test2, null);
                // endGame("Test3");
                // endGame("Friendly");
                */
                // ------------------------------------------------------------------------------------
        }

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
                panel_bigframe[gameNumber] = new JPanel();
                panel_bigframe[gameNumber].setSize(990, 350);
                panel_bigframe[gameNumber].setLayout(null);
                panel_bigframe[gameNumber].setName(gName);
                button_cards[gameNumber] = new JButton[10];
//                plabel_layers = new JLabel[7];

                
                // Add game name label
                label_gameName = new JLabel(gName);
                label_gameName.setFont(new java.awt.Font("Tahoma", 0, 24));
                label_gameName.setBounds(340, 2, 180, 40);
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
                
        		turnToPlay=new JLabel();
        		usernameForMainFrame.setSize(180, 20);
        		usernameForMainFrame.setLocation(50, 40);
        		usernameForMainFrame.setText(username);
        		turnToPlay.setSize(160, 25);
        		turnToPlay.setLocation(100, 5);
        		turnToPlay.setText("TURN TO PLAY!!");
        		turnToPlay.setVisible(false);
        		panel_bigframe[gameNumber].add(usernameForMainFrame);
        		panel_bigframe[gameNumber].add(turnToPlay);
        		panel_bigframe[gameNumber].add(text_fieldTrumpSuite);
        		
                // Add new game panel
                panel_bigframe[gameNumber].setName(gName);
                tabs.addTab(gName, panel_bigframe[gameNumber]);
        }

        
        public void newGameUpadatePlayers(String GameName,String[] pNames){
        	// Find gameNumber
            int gameNumber = 0;
            
            for (int i = 0; i < 15; i++) {
                    if (string_games[i].equals(GameName)) {
                            gameNumber = i;
                            break;
                    }
            }
        	// Add Player Names
            //totalrounds
            int k=0;
            for (k=0 ; k < pNames.length; k++) {
            	scores.put(pNames[k],0);
            	plabel_players[k] = new JLabel(pNames[k]);
            	plabel_players[k].setBounds(750, (k * 20), 80, 15);
                panel_bigframe[gameNumber].add(plabel_players[k]);
            }
            if(k==7){
            	totalrounds=7;
            }else if(k==6){
            	totalrounds=8;
            }else{
            	totalrounds=10;
            }
            panel_bigframe[gameNumber].repaint();
            playerCountemp=pNames.length;
        	
        }
        public void updateGame(String gName, String[] pCards, int[] pScores) {
                //Find gameNumber
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
                //System.out.println(gName);
                //System.out.println(temp);
                for (int i = 0; i < temp; i++) {
                        //System.out.println(i);
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
                                username=string_userName.toString();
                                sb.append(":password");

                                objectOutput.writeObject(sb.toString());
                                String msg = (String) objectInput.readObject();
                                if (msg.compareTo("LK") == 0) {
//                                         clientGui();
                                        afterLoginScreen();
                                        frame_Welcome.dispose();
                                        
                                        
                                        //TODO
//                                        getClients();
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
                                
                                String command = line.substring(0, 2);
                				String[] arguments = line.substring(2).replace(";", "")
                						.split(":");

                				if (command.compareTo("GK") == 0) {
                                    System.out.println("Game succelfully started!");
                                    //new gameui
                                    frame_CreateGame.dispose();
                                    startingGame(tempGameName);
                                    //Now send GNgame_name; to ask for another player to join
                                    try {
                                    		//System.out.println("GN"+tempGameName+";");
	                                        objectOutput.writeObject("GN"+tempGameName+";");
	                                        objectOutput.flush();
	                                        //add thread with wait and keep asking GN
	                                        gameNotStarted=true;
	                                        new Thread(new Runnable() {
												
												@Override
												public void run() {
													//need to turn off gameNotStated before the game starts.
													while(gameNotStarted){
														try {
															Thread.sleep(500);
															if(gameNotStarted){
																//System.out.println("GN"+tempGameName+";");
						                                        objectOutput.writeObject("GN"+tempGameName+";");
															}																													
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}
													
													
												}
											}).start();
	                                        
	                                } catch (IOException e) {
	                                        e.printStackTrace();
	                                }
                                    
                				}else if (command.equals("GP")) {//Player has joined a game
                					defaultList_players.addElement(arguments[0]);
                					
                					System.out.println("Player Has joined " +arguments[0]);

                				}else if (command.equals("GZ")) {//Still waiting for player to join a game
                					

                				}else if (command.equals("GM")) {//Game full and had started
                					gameNotStarted=false;
                					System.out.println("Game has started");
                					newGameGui(tempGameName);
                					 try {
                                 		System.out.println("GA"+tempGameName+";");
	                                    objectOutput.writeObject("GA"+tempGameName+";");
	                                    objectOutput.flush();
	                                    System.out.println("HN"+tempGameName+";");
	                                    objectOutput.writeObject("HN"+tempGameName+";");
	                                    updateTempGameName=tempGameName;
	                                    objectOutput.flush();
	                                    recentHB=tempGameName;
                					 }catch(Exception e){
                						 System.out.println(e);
                					 }
                 				}else if (command.equals("GQ")) {//Confirm that player has been kicked
                					defaultList_players.removeElement(tempKickPlayer);

                				}else if (command.equals("GU")) {//List of games
                					if(!bol_mainFrameActive){
                						//update jlist_contactsOutsideMain
                						jlist_contactsOutsideMain.removeAll();
                						jlist_contactsOutsideMain.setListData(arguments);
                						
                					}else{
                						defaultList_games.removeAllElements();
                						for(int i=0;i<arguments.length;i++){
                    						defaultList_games.addElement(arguments[i]);
                    					}
                					}
                	                
                	                
                					
                				}else if (command.equals("GV")) {//truncated games list
                					
                					System.out.println(line);
                				}else if (command.equals("GX")) {//Game joined
                					
                					gameNotStarted=true;
                                    new Thread(new Runnable() {
										
										@Override
										public void run() {
											while(gameNotStarted){
												try {													
													Thread.sleep(500);
													if(gameNotStarted){
														//System.out.println("GW"+tempGameName+";");
				                                        objectOutput.writeObject("GW"+tempGameName+";");
													}																											
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}										
											
										}
									}).start();
                					

                				}else if (command.equals("GB")) {//Game begins
                					System.out.println("Game Begins");
                					gameNotStarted=false;
                					newGameGui(tempGameName);
                					 try {
                                 		//System.out.println("GA"+tempGameName+";");
	                                    objectOutput.writeObject("GA"+tempGameName+";");
	                                    objectOutput.flush();
	                                    
	                                    //System.out.println("HN"+tempGameName+";");
	                                    objectOutput.writeObject("HN"+tempGameName+";");
	                                    objectOutput.flush();
	                                    updateTempGameName=tempGameName;
	                                    //Ask player for bid
	                                    recentHB=tempGameName;
	                                    
                					 }catch(Exception e){
                						 System.out.println(e);
                					 }
                				}else if (command.equals("GZ")) {//Game not ready yet, can be received after a QW command
                					
                				}else if (command.equals("GC")) {//players in a specific game
                					newGameUpadatePlayers(tempGameName,arguments);

                				}else if (command.equals("QK")) {//Acknowledge quit request, send to client who has quit
                					

                				}else if (command.equals("QP")) {//To inform other clients that someone has quit game
                					

                				}else if (command.equals("HI")) {//receive next hand from server
                					//Receive Hand
                					//argument[0]=round num;
                					if(Integer.parseInt(arguments[0])==totalrounds){
                						lastRound=true;
                						
                					}else{
                						lastRound=false;
                					}
                					threadHN=false;
                					String[] cards=new String[arguments.length-3];
                					for(int i=0;i<arguments.length-3;i++){
                						cards[i]=arguments[i+1];
                					}
                					updateGame(updateTempGameName, cards, null);
                					nextPlayerToBid=arguments[arguments.length-1];
                					firstPlayerToPlay=arguments[arguments.length-1];
                					System.out.println("First Playe to bid:"+firstPlayerToPlay);
                					trumpSuite=arguments[arguments.length-2];
                					if(!trumpSuite.equalsIgnoreCase("x")){
                						//Trump Round
                						String temp="";
                						if(trumpSuite.equalsIgnoreCase("C")){
                							temp="Clubs";
                						}else if(trumpSuite.equalsIgnoreCase("S")){
                							temp="Spades";
                						}else if(trumpSuite.equalsIgnoreCase("H")){
                							temp="Hearts";
                						}else if(trumpSuite.equalsIgnoreCase("D")){
                							temp="Diamods";
                						}
                						text_fieldTrumpSuite.setText("Trump Suite:"+temp);
                						
                					}else{
                						text_fieldTrumpSuite.setText("Trump Suite: None");
                						
                					}
                					askForBid(tempGameName);

                				}else if (command.equals("HC")) {//Who is next to bid, and who has bid, along with their bid
                					//create thread to keep sending HBgame_name; request
                					//---------------------------------------------------------------------------
                					//TODO Keep track of other players bids, will be needed for calculating score
                					bids.put(arguments[0], Integer.parseInt(arguments[1]));
                					//---------------------------------------------------------------------------     
                					//check if server accepted out bid
                					if(arguments[0].equals(username)){
                						//bid was accepted
                						//is frame active?
                						if(frame_enterBid.isActive()){
                							frame_enterBid.dispose();
                							biddingActive=false;
                						}
                						
                                    	
                						//dispose screen and what not
                					}
                					//check whose turn to bid
                					if(firstPlayerToPlay.equals(arguments[2])){
                						//Bids finished and turn to play a card!
                						System.out.println("All Bids Finished!!!");
                						bidding=false;
                						turnToPlay.setVisible(false);
                						turnToPlayCard=false;
                						gameInProgress=true;//check somewhere when no more games are in progress
                						//send HPgame_name to server
                						new Thread(new Runnable() {              							
                							@Override
                							public void run() {
                								while(gameInProgress){
                									try {													
                										Thread.sleep(500);
                										if(gameInProgress){
                											//cycle through all games in progress
                											//dont actually need to cycle through all games.. client only playes one game at a time
                											//System.out.println("HP"+tempGameName+";");
                			                                objectOutput.writeObject("HP"+tempGameName+";");
                										}																											
                									} catch (Exception e) {
                										// TODO Auto-generated catch block
                										e.printStackTrace();
                									}
                								}										
                								
                							}
                						}).start();
                						
                						
                						
                					}
                					else if(arguments[2].equals(username)){
                						//Time to BID!!
                						if(!biddingActive){
                							System.out.println("Bid2");
                    						enterBid(recentHB);                   						
                						}
                						
                					}
                					if(username.equals(firstPlayerToPlay)){
                						//Ask/allow client to play a card
                						turnToPlay.setVisible(true);
            							turnToPlayCard=true;
                					}

                				}else if (command.equals("HL")) {//Who is next to play card, and who has played, along with their played card
                					//See whose turn it is to play a card and who has played what cards
                					//TODO
                					//arguments[0]=player who played card
                					//arguments[1]=card the player played
                					//use tempgamename
                					//Check if 'my' card was played
									if(arguments[0].equals(username)){
										//Set prev card disabled
										//card is arguments[1]
										button_cards[cardClickedI][cardClickedJ].setIcon(new ImageIcon("src/cards/back.gif"));       						
									}              					
                					if(arguments[0].equals(tempLastPlayer)){
                						//Do nothing?
                						//waiting for other player to play card
                					}else{
                						//Display cards other players have played
                						System.out.println("************playedCardsPlacekeeper= "+playedCardsPlacekeeper);
            							System.out.println("************playerCountemp= "+playerCountemp);
                						if(playedCardsPlacekeeper==playerCountemp){
                							//add check for arguments.length==2
                							if(arguments.length==3){
                								if(arguments[2].equals(username)){
                    								turnToPlayCard=true;
                    							}else{
                    								turnToPlayCard=false;
                    							}
                							}
                							
                    						//remove previous
                							System.out.println("-----------------------------------------");
                							System.out.println("Removing Cards! ");
                							System.out.print("playedCardsPlacekeeper= "+playedCardsPlacekeeper);
                							System.out.println("playerCountemp= "+playerCountemp);
                    						for(int y=0;y<playerCountemp;y++){
                    							System.out.print(y+" ");
                    							button_playedCards[y].setVisible(false);
                    						}
                    						System.out.println();
                    						System.out.println("-----------------------------------------");
                    						
                    						//set placekeeper to zero
                    						playedCardsPlacekeeper=0;
                    					}
                						int gameNumber = 0;
                		                for (int i = 0; i < 15; i++) {
                		                        if (string_games[i].equals(tempGameName)) {
                		                                gameNumber = i;
                		                                break;
                		                        }
                		                }
                		               // System.out.println("Print Card,"+gameNumber);
                						tempLastPlayer=arguments[0];
                						Icon icon = new ImageIcon("src/cards/"+arguments[1]+".gif");
                    					button_playedCards[playedCardsPlacekeeper]=new JButton(icon);
                    					button_playedCards[playedCardsPlacekeeper].setBounds((80 * playedCardsPlacekeeper), 175, 75, 100);
                    					button_playedCards[playedCardsPlacekeeper].setEnabled(false);
                    					button_playedCards[playedCardsPlacekeeper].setVisible(true);
                    					panel_bigframe[gameNumber].repaint();
                    					panel_bigframe[gameNumber].add(button_playedCards[playedCardsPlacekeeper]);
                    					playedCardsPlacekeeper++;
                    					//remember to set playedCardsPlacekeeper to zero
                    					
                    					
                					}
                					
                					//turnToPlayCard
                					if(arguments.length==2){
                						//End of trick
                						if(lastRound){
                							//Game is finished!
                							System.out.println("Game is Finished!!!");
                							gameInProgress=false;
                							//Show Winner Screen
                							
                						}else{

                    						System.out.println("End of all tricks in hand, game not finished");    
                    						gameInProgress=false;
                    						//show scores
                    						roundWinner();
                    						//TODO
                    						//Ask server for current scores
                    						//Then deal next hand!
                    						 try {
                    							System.out.println("HS"+tempGameName+";");
           	                                    objectOutput.writeObject("HS"+tempGameName+";");
           	                                    objectOutput.flush();
                    							System.out.println("HA"+tempGameName+";");
          	                                    objectOutput.writeObject("HA"+tempGameName+";");
          	                                    objectOutput.flush();
          	                                    //thread
          	                                     threadHN=true;
          	                                   new Thread(new Runnable() {              							
                       							@Override
                       							public void run() {
                       								while(threadHN){
                       									try {													
                       										Thread.sleep(500);
                       										if(threadHN){
                       											//System.out.println("HN"+tempGameName);
                       			                                objectOutput.writeObject("HN"+tempGameName);
                       										}																											
                       									} catch (Exception e) {
                       										// TODO Auto-generated catch block
                       										e.printStackTrace();
                       									}
                       								}										
                       								
                       							}
                       						}).start();
         	                                    System.out.println("HN"+tempGameName+";");
         	                                    objectOutput.writeObject("HN"+tempGameName+";");
         	                                    objectOutput.flush();
         	                                    
         	                                    updateTempGameName=tempGameName;
         	                                    //Ask player for bid
         	                                    recentHB=tempGameName;
         	                                    
                         					 }catch(Exception e){
                         						 System.out.println(e);
                         					 }
                						}
                						
                						
                						if(username.equals(firstPlayerToPlay)){
                    						//Ask/allow client to play a card
                							turnToPlay.setVisible(true);
                							turnToPlayCard=true;
                    					}
                					}else if(username.equals(arguments[2])){
                						turnToPlay.setVisible(true);
                						turnToPlayCard=true;
                					}else{
                						//System.out.println("turnToPlay=false:");
                						turnToPlayCard=false;
                						turnToPlay.setVisible(false);
                					}
                					

                				}else if (command.equals("HW")) {//Waiting for others message
                					
                					
                				}else if (command.equals("HO")) {//Player names and scores
                					System.out.println("Players and scores: ");
                					for(int i=0;i<arguments.length;i=i+3){
                						//arguments[i]-current player
                						//arguments[i+1]-tricks won in round
                						//arguments[i+2]-score for the round
                						//int tempScore=scores.get(arguments[i]);
                						System.out.println("------------------------");
                						scores.put(arguments[i], Integer.parseInt(arguments[i+2]));
                						System.out.println("Player: "+arguments[i]);
                						System.out.println("Total hands won: "+arguments[i+1]);
                						System.out.println("score for last round: "+scores.get(arguments[i]));
                						System.out.println("------------------------");
                					}

                				}else if (command.equals("LM")) {
                					
                					System.out.println("Logoff Successful!");

                				}else if (command.equals("100")) {//Login unsuccessful, username already logged in
                					System.out.println("Login unsuccessful, username already logged in");

                				}else if (command.equals("101")) {//Login unsuccessful, incorrect username/password pair.
                					System.out.println("Login unsuccessful, incorrect username/password pair.");

                				}else if (command.equals("102")) {//Logoff unsuccessful, not logged in.
                					System.out.println("Logoff unsuccessful, not logged in.");

                				}else if (command.equals("110")) {//Not part of a game.
                					System.out.println("Not part of a game.");

                				}else if (command.equals("120")) {//Game identifier already taken
                					System.out.println("Game identifier already taken");

                				}else if (command.equals("130")) {//Game has too many players.
                					System.out.println("Game has too many players.");

                				}else if (command.equals("131")) {//Game has too few players.
                					System.out.println("Game has too few players.");

                				}else if (command.equals("132")) {//Kicked out player not part of game
                					System.out.println("Kicked out player not part of game");

                				}else if (command.equals("133")) {//No such game identifier
                					System.out.println("No such game identifier");

                				}else if (command.equals("134")) {//Player has been kicked out of game.
                					System.out.println("Player has been kicked out of game.");

                				}else if (command.equals("139")) {//Player in game has closed connection. Game abandoned.
                					System.out.println("Player in game has closed connection. Game abandoned.");

                				}else if (command.equals("140")) {//Illegal bid (bid is higher than the number of cards in this hand)
                					System.out.println("Illegal bid (bid is higher than the number of cards in this hand)");
                					//Call enter bid
                					bidding=true;
                					enterBid(recentHB);

                				}else if (command.equals("141")) {//Illegal card (player does not have this card).
                					System.out.println("Illegal card (player does not have this card).");

                				}else if (command.equals("ER") && arguments[0].equals(142)) {//Illegal card (player must play the led suit because it has suitable cards)
                					System.out.println("Illegal card (player must play the led suit because it has suitable cards)");
                					//Played Wrong Card
                					

                				}else if (command.equals("150")) {//One or more of the player names does not exist, chat not delivered to those players
                					System.out.println("One or more of the player names does not exist, chat not delivered to those players");

                				}else if (command.equals("900")) {//Bad message format
                					System.out.println("Bad message format");

                				}else if (command.equals("901")) {//Unexpected request
                					System.out.println("Unexpected request");

                				}else if (command.equals("910")) {//Something very bad but unspecified has happened
                					System.out.println("Something very bad but unspecified has happened");

                				}else if(line.charAt(0)== 'L' && line.charAt(1) == 'C') {
                                        line = line.replaceFirst("L", "");
                                        line = line.replaceFirst("C", "");
                                        names = line.split(" ");
                                        String n = Boolean.toString(bol_mainFrameActive);
                                        System.out.println(n);
                                        if(bol_mainFrameActive){
                                                jlist_contactsMain.setListData(names);
                                                frame_main.repaint();
                                        } else {
                                                jlist_contactsOutsideMain.setListData(names);
                                        }
                                        
                                }
//                                String clients = (String)objectInput.readObject();
//                                System.out.println("CLIENTS: "+clients);
//                                names = clients.split(" ");
//                                return names;
//                                jlist_contactsOutsideMain.setListData(arr);
                                
                                
                                
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

        public void askForBid(String gameName){
        	System.out.println("AskForBid");
        	System.out.println(username);
        	System.out.println(firstPlayerToPlay);
        	//Start thread
    		bidding=true;
			 new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(bidding){
						try {													
							Thread.sleep(500);
							if(bidding){
								System.out.println("HB"+recentHB+";");
                                objectOutput.writeObject("HB"+recentHB+";");
							}																											
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}										
					
				}
			}).start();
        	if(username.equals(firstPlayerToPlay)){
        		//Bid
        		System.out.println("BidNBow");
        		enterBid(gameName);
        		
        		
        		
        	}else{
        		
        		//Otherwise, see whose turn it is to bid
        		try {
             		System.out.println("HB"+gameName+";");
                    objectOutput.writeObject("HB"+gameName+";");
                    objectOutput.flush();
    			 }catch(Exception e){
    				 System.out.println(e);
    			 }
        	}
        	   	
        }
     // Create New Game
        void enterBid(String gameName) {
                // Theme
        	biddingActive=true;
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
                text_FieldEnterBid.setLocation(90, 10);
                //add field for game name
                //TODO 

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
        //Winner of round
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
            System.out.println("Player Count: "+playerCountemp);
            JLabel[][] text_roundWinner=new JLabel[playerCountemp+1][5];
            text_roundWinner[0][0]=new JLabel("Username");
            text_roundWinner[0][0].setSize(150,30);
            text_roundWinner[0][0].setLocation(3, 10);
            panel_roundWinner.add(text_roundWinner[0][0]);
            
            text_roundWinner[0][1]=new JLabel("Bid");
            text_roundWinner[0][1].setSize(150,30);
            text_roundWinner[0][1].setLocation(70, 10);
            panel_roundWinner.add(text_roundWinner[0][1]);
            
            text_roundWinner[0][2]=new JLabel("Tricks Won");
            text_roundWinner[0][2].setSize(150,30);
            text_roundWinner[0][2].setLocation(140, 10);
            panel_roundWinner.add(text_roundWinner[0][2]);
            
            text_roundWinner[0][3]=new JLabel("Round Score");
            text_roundWinner[0][3].setSize(150,30);
            text_roundWinner[0][3].setLocation(210, 10);
            panel_roundWinner.add(text_roundWinner[0][3]);
            
            text_roundWinner[0][4]=new JLabel("Total Score");
            text_roundWinner[0][4].setSize(150,30);
            text_roundWinner[0][4].setLocation(280, 10);
            panel_roundWinner.add(text_roundWinner[0][4]);
            //get all usernames
            String[] names=new String[playerCountemp];
            int p=0;
            for (Enumeration e = scores.keys(); e.hasMoreElements();) {
    			String playerName = (String) e.nextElement();
    			names[p]=playerName;
    			System.out.println(names[p]);
    			p++;
    		}
            
            for(int i=1;i<playerCountemp+1;i++){
            	text_roundWinner[i][0]=new JLabel(names[p-1]);
                text_roundWinner[i][0].setSize(100,30);
                text_roundWinner[i][0].setLocation(3, 10+i*20);
                panel_roundWinner.add(text_roundWinner[i][0]);
                
                
                text_roundWinner[i][1]=new JLabel();//Get
                text_roundWinner[i][1].setText(bids.get(names[i-1])+"");
                text_roundWinner[i][1].setSize(100,30);
                text_roundWinner[i][1].setLocation(70, 10+i*20);
                panel_roundWinner.add(text_roundWinner[i][1]);
                
                text_roundWinner[i][2]=new JLabel("Tricks Won");//Get
                text_roundWinner[i][2].setSize(100,30);
                text_roundWinner[i][2].setLocation(140, 10+i*20);
                panel_roundWinner.add(text_roundWinner[i][2]);
                
                text_roundWinner[i][3]=new JLabel("Round Score");//Get
                text_roundWinner[i][3].setSize(100,30);
                text_roundWinner[i][3].setLocation(210, 10+i*20);
                panel_roundWinner.add(text_roundWinner[i][3]);
                
                text_roundWinner[i][4]=new JLabel();//Get
                text_roundWinner[i][4].setText(scores.get(names[i-1])+"");
                text_roundWinner[i][4].setSize(100,30);
                text_roundWinner[i][4].setLocation(280, 10+i*20);
                panel_roundWinner.add(text_roundWinner[i][4]);
            }
            
            
            
            
            frame_roundWinner = new JFrame();
            frame_roundWinner.setSize(600, 240);
            frame_roundWinner.setLocation(frame_main.getX(), frame_main.getY());
            frame_roundWinner.setEnabled(true);

            panel_roundWinner = new JPanel();
            panel_roundWinner.setSize(frame_roundWinner.getWidth(),
            		frame_roundWinner.getHeight());
            panel_roundWinner.setLayout(null);
            panel_roundWinner.setBackground(Color.white);

            

            button_closeWinner = new JButton();
            button_closeWinner.setSize(140, 40);
            button_closeWinner.setLocation(130, 65);
            button_closeWinner.setText("Next Hand");
            button_closeWinner.addActionListener(this);

            panel_roundWinner.add(button_closeWinner);
            panel_roundWinner.add(label_enterNewGameName);
            frame_CreateGame.add(panel_CreateGame);
            frame_CreateGame.setVisible(true);
            
    }

        // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public void actionPerformed(ActionEvent evt) {
                if (evt.getSource() == button_login) {
                	if(text_loginTextfieldName.getText().equals("")){
                		frame_Welcome.dispose();
                		welcomeScreen();
                	}else{
                		connectToServer();
                	}
                        
                } 
                //Protocol
                //client -> server: CAgame_name:message;
                //server -> client: CG;
                else if (evt.getSource() == button_sendMessage_in) {
                        String text = text_message_in.getText();
                        if ((text.length() > 0)&&(tabs.getComponentCount()!=0)) {
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

                else if(evt.getSource() == button_joinGame_out) {                      
                        
                        //Join game selected in list
                        
                        try {
                        	if(!jlist_contactsOutsideMain.isSelectionEmpty()){
                        		tempGameName=jlist_contactsOutsideMain.getSelectedValue().toString();
                				System.out.println(tempGameName);
                                objectOutput.writeObject("GJ"+jlist_contactsOutsideMain.getSelectedValue().toString()+";");
                                System.out.println("GJ"+jlist_contactsOutsideMain.getSelectedValue().toString()+";");
                                frame_choice.dispose();
                                clientGui();
                        	}
            				
            	        } catch (IOException e) {
            	                e.printStackTrace();
            	        }
                        
                        
                        
                        
//                        getClients();
//                        names = getClients();
//                        jlist_contactsMain.setListData(names);
//                        frame_main.repaint();
                }
                //Protocol
                //client -> server: GSgame_name;
                //server -> client: GK;
                else if (evt.getSource() == button_createNewGame) {
                        //Send to server
                        StringBuilder sb = new StringBuilder();
                        sb.append("GS");
                        tempGameName=text_FieldEnterNewGameName.getText();
                        sb.append(tempGameName);
                        sb.append(";");
                        System.out.println(sb.toString());
                        
                        try {
                                objectOutput.writeObject(sb.toString());
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        
                        
                }
                else if (evt.getSource() == button_newGame_out) {
                	frame_choice.dispose();
                	
                	clientGui();//For now
                	createGame();
                	
                } //button_closeWinner,panel_roundWinner
                else if (evt.getSource() == button_closeWinner) {
                	frame_roundWinner.dispose();

                	
                }
                
                //Protocol
                //Once a game has ben, a player (either a joining player or the initiating player) 
                //may request a list of the other players. The server responds with a list of players.
                //client -> server: GAgame_name;
                //server -> client: GCplayer_name1:player_name2:...;
                else if (evt.getSource() == button_listPlayers) {
                        textArea_display_in.append("List Of Players Button Pressed\n");
                } else if (evt.getSource() == button_kickPlayer) {
                	System.out.println("Kick player");
                	
                	try {
                		//Kick Player GOgame_name:player_name;
                		if(!jlist_playersJoiningGame.isSelectionEmpty()){
                			StringBuilder sb=new StringBuilder();
                    		sb.append("GO");
                    		sb.append(tempGameName+":");
                    		sb.append(defaultList_players.getElementAt(jlist_playersJoiningGame.getSelectedIndex()).toString());
                    		sb.append(";");
                    		tempKickPlayer=defaultList_players.getElementAt(jlist_playersJoiningGame.getSelectedIndex()).toString();
                    		System.out.println(sb.toString());
                            objectOutput.writeObject(sb.toString());
                		}
                		
                	} catch (IOException e) {
                        e.printStackTrace();
                	}
                	              
                } 
                //TODO: what command should we send to the server?
                //It is correct, read http://www.cs.sun.ac.za/rw344/project.html
                //There is no chat to all option. . .
                //client -> server: CAgame_name:message;
                //server -> client: CG;
                else if (evt.getSource() == button_sendMessage_out) {
                	//Refreshing Games for now
                	try {													
    					if(!bol_mainFrameActive){
    						System.out.println("GL;");
                            objectOutput.writeObject("GL;");
    					}																											
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                	
                	/*
                        if (text_message_out.getText().length() > 0) {
                                String text = text_message_out.getText();
                                textArea_display_out.append("<- " + text + " ->\n");
                                StringBuilder sb = new StringBuilder();
                                sb.append("CA");
                                //Read protocol, this must be here. . .
                                sb.append(tabs.getSelectedComponent().getName());
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
                        }*/
                }
                //Protocol
                //client -> server: GL;
                //server -> client: GUgame_name1:game_name2:...;
                else if (evt.getSource() == button_listGames) {
                        textArea_display_in.append("button_listGames Pressed\n");
                        StringBuilder sb = new StringBuilder();
                        sb.append("GL;");
                        try {
                                objectOutput.writeObject(sb.toString());
                                System.out.println(sb.toString());
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                else if (evt.getSource() == button_history) {
                        textArea_display_in.append("History Pressed\n");
                } else if (evt.getSource() == button_enterBid) {
                	//Enter bid
                	try{
                		int temp_bid=Integer.parseInt(text_FieldEnterBid.getText());
                		
                		StringBuilder sb=new StringBuilder();
                    	sb.append("HD"+recentHB);
                    	sb.append(":");
                    	sb.append(temp_bid);
                    	sb.append(";");
                    	try {
                     		System.out.println(sb.toString());
                            objectOutput.writeObject(sb.toString());
                            objectOutput.flush();
            			 }catch(Exception e){
            				 System.out.println(e);
            			 }
                    	
                    	
                    	//check if bid was valid
                    	
                	}catch(Exception e){
                		System.out.println("Not a number!");
                		text_FieldEnterBid.setText("");
                	}
                	
                    
         	  } 
                
                else if (evt.getSource() == button_createGame) {
                        textArea_display_in.append("Create new Game Screen\n");
                        createGame();                     

                }else if (evt.getSource() == button_startCreatedGame) {
                	//Start Game
                    try {
                            objectOutput.writeObject("GF"+tempGameName);
                            System.out.println("GF"+tempGameName);
                            
                    } catch (IOException e) {
                            e.printStackTrace();
                    } 
                    //Dispose
                    frame_waitingToStartGame.dispose();
                }
                //Protocol
                //client -> server: LO;
                //The server will respond with a logoff OK message and immediately close the connection:
                //server -> client: LM;
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
                	if(turnToPlayCard){
                		//A card has been played
                		for(int i=0;i<15;i++){
                			for(int j=0;j<10;j++){
                				if(evt.getSource().equals(button_cards[i][j])){
                					System.out.println("Card:"+i+":"+j);
                					cardClickedJ=j;
                					cardClickedI=i;
                					//button_cards[i][j].setIcon(new ImageIcon("src/cards/back.gif"));
                				}
                			}
                		}
                        String temp = evt.toString().substring(
                                        evt.toString().indexOf(" on") + 4);
                        // Check if temp equals any special cases, i.e. logoff, get list of
                        // games, if not then a card has been clicked
                        String card = temp.substring(0, 2);
                        String game = temp.substring(2);
                        System.out.print(card + " ");
                        System.out.println(game);
                        //Send played card to server;
                        StringBuilder sb = new StringBuilder();
                        sb.append("HR");
                        sb.append(game+":");
                        sb.append(card+";");
                        try {
                                objectOutput.writeObject(sb.toString());
                                System.out.println(sb.toString());
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        turnToPlay.setVisible(false);
                        turnToPlayCard=false;
                        //set played card as unclickable
                        
                        
                	}
                        

                }
        }

        public void valueChanged(ListSelectionEvent arg0) {
                // TODO Auto-generated method stub
                
        }

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//Attempt to join Game
			//Join game GJgame_name;
			System.out.println(defaultList_games.getSize());
			if(defaultList_games.getSize()>0){
				try {
					tempGameName=defaultList_games.getElementAt(jlist_contactsMain.getSelectedIndex()).toString();
	                objectOutput.writeObject("GJ"+defaultList_games.getElementAt(jlist_contactsMain.getSelectedIndex()).toString()+";");
	                //System.out.println("GJ"+defaultList_games.getElementAt(jlist_contactsMain.getSelectedIndex()).toString()+";");
		        } catch (IOException e) {
		                System.out.println("Logoff fail");
		                e.printStackTrace();
		        }
			}
				
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
}
