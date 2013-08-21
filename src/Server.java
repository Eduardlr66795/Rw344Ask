
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private ServerSocket server;
    private Socket Soket;
    private int port = 9119;
    private boolean islistening;
    HashSet<HandleClient> clientsList = new HashSet<HandleClient>();
    HashSet<Game> gamesList = new HashSet<Game>();

    //does this mean anything specific?
    //take away comments if so please.
    /*public static void main(String[] args) {
        try {
            new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    

    public Server() {
        //open server port for communication
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            //could not open to the specifeid port.
        }
    }

    public void run() {
        while (islistening) {
            try {
                Soket = server.accept();
                // Handle clients in game
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            HandleClient client = new HandleClient(Soket, null, this);
            //add client
            if( clientsList.add(client) == false){
                //client connection is unique
                
            }else{
                //client connection is not unique
                client.closeConnection();
            }
        }
    }
    
    public boolean createGame(String gameName, HandleClient creator){
        //handle the creation of a game object and add to the HashSet.
        Game newGame = new Game(gameName,creator,this);
        if(gamesList.add(newGame) == false){
            //game is unique and was created
            return true;
        }else{
            //game is not unique and was not created
            return false;
        }
    }
}
