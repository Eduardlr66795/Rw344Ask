
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class HandleClient extends Thread {

    private Server server;
    private String gameName;
    private Game game;
    private Socket socket;
    private String userName;
    private boolean listening;
    public BufferedReader input;
    public PrintWriter output;
    boolean isLoggedIn = false;
    private int EddieTest = 0;

    public HandleClient(Socket socket, Server server) {
        this.server = server;
        //Removed the game from the constructor, thought that at the moment a client
        //connects to server it wont yet know what game it will join/create
        this.socket = socket;

        try {
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            run();
        //Removed login, should also be in thread since it wont happen immediately nesicarily.
        //Can make client to only connect when logging in, but it may not
        //support other clients.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(int value, String uname, String msg) {
        output.println(uname + " : " + msg);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket Socket) {
        this.socket = Socket;
    }

    public String getUserName() {
        return userName;
    }

    public String getGame() {
        return gameName;
    }

    public void setGame(String game) {
        this.gameName = game;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void closeConnection() {
        // close streams and set listening to false;
        listening = false;
    }

    public void run() {
        while (listening) {
            String line;
                if(!isLoggedIn){
                try{
                    userName = input.readLine();

                    if (!server.clientsList.contains(userName)) {
                        if (EddieTest == 1) {
                            JOptionPane.showMessageDialog(null,
                                    "DOES NOT CONTAIN - HANDLE CLIENT");
                        }
                        listening = true;
                        output.println("LK");
                    } else {
                        if (EddieTest == 1) {
                            JOptionPane.showMessageDialog(null,
                                    "CONTAINS  - HANDLE CLIENT");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                /*
                * Should only happen once user is logged in:
                */
                try {
                    line = input.readLine();

                    if (EddieTest == 1) {
                        JOptionPane.showMessageDialog(null, line);
                    }
                    /*
                    * ...or we could do this as one function with server returning
                    * a string for data to be sent to client...
                    */
                    if (line.equals("GL;")) {

                        //No games are av. so user has to create a game
                        if (server.getGamesCount() == 0) {
                            //Send "no games available, do u want to start a new game?"
                            //User should have options (join or create)
                        } else {
                            //Client can join a game. Send a list of games 
                            for (int x = 0; x < server.getGamesCount(); x++) {

                                String[] gamesList = server.getGamesList();
                                output.print("GU");
                                for(int xg = 0; xg < gamesList.length-1; xg++){
                                    output.print(gamesList[xg]+":");
                                }
                                output.println(gamesList[(gamesList.length-1)]+";");
                            }
                        }

                    } 
                    
                    //Create a game for the player
                    else if (line.substring(0, 2).equals("GS")) {
                        String tempGameName = line.substring(2, line.length() - 1);
                        if (server.gameNameExists(tempGameName)) {
                            //send error 120 to client "Game Already Exists"
                            output.println("ER120"); 
                            System.out.println("Error, game name taken");
                        } else {
                            
                            //create game
                            server.createGame(tempGameName, userName);
                            //set chosenGameName as current game
                            gameName = tempGameName;
                            //set current game pointer as game in server, to 
                            //bypass uneccesary calls to server class
                            game = server.getGame(gameName);
                          //send acknowledgement to client
                            output.println("GK;");
                        }
                    } 
                    
                    //Allow another player to join the game
                    else if (line.equals("GN;")) {
                        server.allowAddPlayerToGame(gameName);
                        //TODO response, we going to need a hashtable of sockets
                        
                    } 
                    
                    //if game has enough players, and not too much, game will start
                    else if (line.equals("GF;")) {
                    	int totalplayers = server.getTotalPlayers(gameName);
                    	
                        if (totalplayers < 3) {
                        	output.println("ER131");
                            continue;
                        }
                        else if (totalplayers > 7) {
                        	output.println("ER130");
                        	continue;
                        }
                        else {
                        	server.startGame(gameName);
                        	//Game full OK response
                        	output.println("GM");
                        }
                        
                    } 
                    
                    //Kick a player out
                    else if (line.substring(0, 2).equals("GO")) {
                    	if(server.kickPlayerFromGame(gameName, line.substring(2))) {
                    		//player removed
                    		output.println("GQ");
                    	} else {
                    		//No such player error
                    		output.println("ER132");
                    	}
                    }
                    
                    //Prefixed game search
                    else if (line.substring(0, 2).equals("GG")) {
                    	String prefix = line.substring(2);
                    	
                    	if (server.getGamesCount() == 0) {
                            //Send "no games available, do u want to start a new game?"
                            //User should have options (join or create)
                    		output.println("GU");
                    		//Empty list should be handled on client
                        } else {
                            //Client can join a game. Send a list of games 
                            for (int x = 0; x < server.getGamesCount(); x++) {

                                String[] gamesList = server.getGamesList(prefix);
                                if (gamesList[0].equals("")) {
                                	output.println("GU");
                                	continue;
                                }
                                output.print("GU");
                                for (int xg = 0; xg < gamesList.length-1; xg++){
                                    output.print(gamesList[xg]+":");
                                }
                                output.println(gamesList[(gamesList.length-1)]+";");
                            }
                        }
                    	
                    }
                    
                    //Request to join game
                    else if (line.substring(0, 2).equals("GJ")) {
                    	server.joinGame(gameName, line.substring(2));
                    	output.println("GX");
                    	
                    }
                    
                    
                    
                    //Command not understood
                    else {
                    	output.println("ER901");
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//if(isLoggedIn)
        }//while(listening)
    }//run()
}
