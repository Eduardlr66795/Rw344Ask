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
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        // server.sendUsernames();

                        while (true) {
                                String message = (String) input.readObject();
                                String command = message.substring(0, 2);
                                String[] arguments = message.substring(2).split(":");

                                if (command.equals("LI")) {
                                        server.login(arguments[0], arguments[1], socket);
                                } else if (command.equals("LO")) {
                                        server.logoff(socket);
                                }
                                else if (command.equals("GS")){
                                        server.createGame(arguments[0], socket);
                                }
                                else if (command.equals("GN")){
                                        server.allowAddPlayerToGame(arguments[0], socket);
                                }
                                else if (command.equals("GF")){
                                        server.gameIsFull(arguments[0], socket);
                                }
                                
                                else {
                                        server.sendToAll(message, socket);
                                }
                        }

                } catch (EOFException ie) {

                } catch (IOException ie) {

                } catch (Exception e) {

                } 
                
//              finally {
//                      if (!disconnected) {
//                              server.removeUsername(socket);
//                              server.removeConnection(socket);
//                      }
//              }
        }
}