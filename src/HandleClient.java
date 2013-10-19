import java.io.*;
import java.net.*;

public class HandleClient extends Thread {

	private Server server;
	private Socket socket;
	private boolean disconnected = false;
        private boolean loggedIn = false;

	public HandleClient(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		start();
	}

	public void run() {
		try {
			ObjectInputStream input = new ObjectInputStream(
					socket.getInputStream());

			while (true) {
				String message = (String) input.readObject();
                                String command = "";
                                String[] arguments = {};
                                if(message.length() >= 2){
                                    command= message.substring(0, 2);
                                    arguments = message.substring(2).replace(";", "")
                                                    .split(":");
                                }
				if (command.equals("LI")) {
					loggedIn = server.login(arguments[0], arguments[1], socket);

				} else if (command.equals("LO")) {
					server.logoff(socket);
				}
                                if(loggedIn){
                                    if (command.equals("GS")) {
					server.createGame(arguments[0], socket);

                                    } else if (command.equals("GN")) {
                                            server.addPlayerToGame(arguments[0], socket);

                                    } else if (command.equals("GF")) {
                                            server.gameIsFull(arguments[0], socket);

                                    } else if (command.equals("GO")) {
                                            server.kickPlayerFromGame(arguments[0], arguments[1],
                                                            socket);

                                    } else if (command.equals("GL")) {
                                            server.getGamesList(socket, "");

                                    } else if (command.equals("GG")) {
                                            server.getGamesList(socket, arguments[0]);

                                    } else if (command.equals("GJ")) {
                                            server.joinGame(socket, arguments[0]);

                                    } else if (command.equals("GW")) {
                                            server.waitForGame(socket, arguments[0]);

                                    } else if (command.equals("GA")) {
                                            server.playersInGame(socket, arguments[0]);

                                    } else if (command.equals("QT")) {
                                            server.quitGame(socket, arguments[0]);

                                    } else if (command.equals("LC")) {
                                            server.sendclientList();

                                    } else if (command.equals("HN")) {
                                            server.handRequest(socket, arguments[0]);

                                    } else if (command.equals("HB")) {
                                            server.bidRequest(socket, arguments[0]);

                                    } else if (command.equals("HD")) {
                                            server.bidPlay(socket, arguments[0], arguments[1]);

                                    } else if (command.equals("HP")) {
                                            server.cardRequest(socket, arguments[0]);

                                    } else if (command.equals("HR")) {
                                            server.cardPlay(socket, arguments[0], arguments[1]);

                                    } else if (command.equals("HA")) {
                                            server.anotherHand(socket, arguments[0]);	

                                    } else if (command.equals("HS")) {
                                            server.scoreRequest(socket, arguments[0]);	

                                    } else if (command.equals("CP")) {
                                            server.chatToPlayers(socket, arguments);

                                    } else if (command.equals("CA")) {
                                            server.chatToGame(socket, arguments[0], arguments[1]);

                                    } else if (command.equals("CC")) {
                                            server.collectMessages(socket);	

                                    } else {
                                            server.badMessageFormat(socket);
                                    }
                                }else{
                                    server.unExpectedCommand(socket);
                                }
			}

		} catch (EOFException ie) {
                        
		} catch (IOException ie) {
                        
		} catch (Exception e) {
                        
		}

		finally {
			if (!disconnected) {
				server.removeUsername(socket);
				server.removeConnection(socket);
			}
		}
	}
}
