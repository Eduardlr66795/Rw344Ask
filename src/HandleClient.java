import java.io.*;
import java.net.*;

public class HandleClient extends Thread {

	private Server server;
	private Socket socket;
	private boolean disconnected = false;

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
				
				if (message.length() < 3) {
					server.badMessageFormat(socket);
					continue;
				}
				
				String command = message.substring(0, 2);
				String[] arguments = message.substring(2).replace(";", "")
						.split(":");

				if (command.equals("LI")) {
					if (arguments.length != 2) {
						server.badMessageFormat(socket);
					} else {
						server.login(arguments[0], arguments[1], socket);
					}

				} else if (command.equals("LO")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.logoff(socket);
					}

				} else if (command.equals("GS")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.createGame(arguments[0], socket);
					}

				} else if (command.equals("GN")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.addPlayerToGame(arguments[0], socket);
					}

				} else if (command.equals("GF")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.gameIsFull(arguments[0], socket);
					}

				} else if (command.equals("GO")) {
					if (arguments.length != 2) {
						server.badMessageFormat(socket);
					} else {
						server.kickPlayerFromGame(arguments[0], arguments[1],socket);
					}

				} else if (command.equals("GL")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.getGamesList(socket, "");
					}

				} else if (command.equals("GG")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.getGamesList(socket, arguments[0]);
					}
					

				} else if (command.equals("GJ")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.joinGame(socket, arguments[0]);
					}
					
				} else if (command.equals("GW")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.waitForGame(socket, arguments[0]);
					}
					
				} else if (command.equals("GA")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.playersInGame(socket, arguments[0]);
					}
					
				} else if (command.equals("QT")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.quitGame(socket, arguments[0]);
					}
				
				} else if (command.equals("LC")) {
                    server.sendclientList();
                    
				} else if (command.equals("HN")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.handRequest(socket, arguments[0]);
					}
				
				} else if (command.equals("HB")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.bidRequest(socket, arguments[0]);
					}
					
				} else if (command.equals("HD")) {
					if (arguments.length != 2) {
						server.badMessageFormat(socket);
					} else {
						server.bidPlay(socket, arguments[0], arguments[1]);
					}
					
				} else if (command.equals("HP")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.cardRequest(socket, arguments[0]);
					}
					
				} else if (command.equals("HR")) {
					if (arguments.length != 2) {
						server.badMessageFormat(socket);
					} else {
						server.cardPlay(socket, arguments[0], arguments[1]);
					}
				
				} else if (command.equals("HA")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.anotherHand(socket, arguments[0]);
					}
					
				} else if (command.equals("HS")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.scoreRequest(socket, arguments[0]);
					}
					
				} else if (command.equals("CP")) {
					// CP arguments are checked in server method
					server.chatToPlayers(socket, arguments);
					
				} else if (command.equals("CA")) {
					if (arguments.length != 2) {
						server.badMessageFormat(socket);
					} else {
						server.chatToGame(socket, arguments[0], arguments[1]);
					}
					
				} else if (command.equals("CC")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.collectMessages(socket);
					}
					
				} else if (command.equals("MC")) {
					if (arguments.length != 1) {
						server.badMessageFormat(socket);
					} else {
						server.modeConfusion(socket);
					}

				} else {
					server.badMessageFormat(socket);
				}
			}

		} catch (EOFException ie) {

		} catch (IOException ie) {

		} catch (Exception e) {

		}

		finally {
			if (!disconnected) {
				server.logoff(socket);
			}
		}
	}
}
