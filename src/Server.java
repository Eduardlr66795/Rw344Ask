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
        private Hashtable<ObjectOutputStream, String> clientList = new Hashtable<ObjectOutputStream, String>(); // outputstream, username
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
        public int EddieTest = 0;
                


        public static void main(String[] args) throws Exception {
                int port = 9119;
                Server server = new Server(port);
        }
        
        
        public Server(int port) throws IOException {
                buildGui();
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
                                System.exit(0);
                        }
                });
        }


        
        @SuppressWarnings("unchecked")
        private void listen(int port) throws IOException {
                ServerSocket server_Socket = new ServerSocket(port);
                System.out.println("Server started. Listening on " + server_Socket);
                textAreaServer.append("Server started. Listening on Port" + server_Socket.getLocalPort());
                textAreaServer.append("\n");
                while (true) {
                        Socket server = server_Socket.accept();
                        System.out.println("New connection from socket: " + server);
                        ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());

                        output.writeObject("RD");

                        outputStreams.put(server, output);
                        new HandleClient(this, server);


                        //textAreaServer.append("New client on "+server.getPort());
                        textAreaServer.append("New client on Port "+server.getPort());
                        textAreaServer.append("\n");
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
                                ObjectOutputStream output = (ObjectOutputStream) e.nextElement();
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
                    textAreaClient.append("New client: "+username);
                    textAreaClient.append(" at "+getTimeAndDate());
                    textAreaClient.append("\n");
                    return;
            } catch (IOException e) {
                    System.out.println("Exception in login " + e);
            }
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

        /**
         * 
         * @param gameName
         * @param creator
         * @return
         */
        public void createGame(String gameName, Socket socket) {
        	
                // handle the creation of a game object and add to the HashSet.
                Game newGame = new Game(gameName, getUsername(socket), this);
                ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
                
                try {
	                if (!gamesList.contains(gameName)) {
	                        // game is unique and was created
	                        gamesList.put(gameName, newGame);
	                        
	                        o.writeObject("GK");
	                        o.flush();
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
        
        public void addPlayerToGame(String gameName, Socket socket) {
        	try {
	        	ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
	        	
	        	//if game does not exist
	    		if (!gamesList.contains(gameName)) {
	    			o.writeObject("ER133;");
	    			o.flush();
	    			return;
	    		}
	    		
	            Game game = gamesList.get(gameName);
	            
	            // If game is full
	            if (game.playerCount > 7) {
	            	o.writeObject("ER130");
	            	o.flush();
	            	return;
	            }
	            
	            // Let next player to join join
	            if (!game.nextPlayersToJoin.isEmpty()) {
	            	String player = game.nextPlayersToJoin.pop();
	            	game.playerCount++;
	            	
	            	o.writeObject("GP" + player + ";");
	            	o.flush();
	            	
	            	// Search for a spot left open by a kicked player
	            	for (Enumeration e = game.playerList.keys(); e.hasMoreElements();) {
	            		String tmpplayer = (String) e.nextElement();
	            		
	            		if (tmpplayer.equals("EMPTYSPOTLEFTOPENBYKICKEDPLAYER")) {
	            			game.playerList.put(player, game.playerList.get("EMPTYSPOTLEFTOPENBYKICKEDPLAYER"));
	            			gamesList.put(gameName, game);
	            			return;
	            		}
	            	}
	            	
	            	game.playerList.put(player, game.playerCount);
	            	
	            // No player to join at moment
	            } else {
	            	
	            	o.writeObject("GZ;");
	            	o.flush();
	            	return;
	            	
	            }
	            gamesList.put(gameName, game);
	            
        	} catch (Exception e) {
        		System.out.println("Exception in addPlayerToGame: " + e);
        	}
        }
        
        public void gameIsFull(String gameName, Socket socket) {
        	try {
	            ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
	            
	            // if game does not exist
	    		if (!gamesList.contains(gameName)) {
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
	            	
	            	game.readyToStart = true;
	            	gamesList.put(gameName, game);
	            	
	            	o.writeObject("GM;");
	            	o.flush();
	            	return;
	            }
	           
        	} catch (Exception e) {
        		System.out.println("Exception in gameIsFull " + e);
        	}
    }

        // Remove a player from a game
        public void kickPlayerFromGame(String gameName, String playerName, Socket socket) {
        	try {
        		ObjectOutputStream o = (ObjectOutputStream) outputStreams.get(socket);
	            
  	          // if game does not exist
  	    		if (!gamesList.contains(gameName)) {
  	    			o.writeObject("ER133;");
  	    			o.flush();
  	    			return;
  	    		}
        		
  	    		Game game = gamesList.get(gameName);
  	    		
  	    		if (game.playerList.contains(playerName)) {
  	    			
  	    			game.playerList.put("EMPTYSPOTLEFTOPENBYKICKEDPLAYER", game.playerList.get(playerName));
  	    			game.playerList.remove(playerName);
  	    			game.playerCount--;
  	    			
  	    			gamesList.put(gameName, game);
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

        
        public int getTotalPlayers(String gameName) {
                Game game = gamesList.get(gameName);
                return game.amountOfPlayers();
        }

        
        

        
        public ObjectOutputStream getPlayerAdded(String gameName) {
                return gamesList.get(gameName).getAddedPlayer();
        }

        
        public boolean hasNewPlayer(String gameName) {
                return gamesList.get(gameName).hasNewPlayer();
        }
        
        
        public boolean joinGame(String gameName, String playerName) {
                return (gamesList.get(gameName).addPlayer(playerName));
        }
        // Get games list as string array for client to choose a game
        public String[] getGamesList() {
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

        public String getName(ObjectOutputStream o) {
                return clientList.get(o);
        }

        
        


        public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
        }
}
