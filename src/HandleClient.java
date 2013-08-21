import java.net.Socket;


public class HandleClient extends Thread {
    private Server server;
    private Game game;
    private Socket Soket;
    private String userName;
    private boolean listening;    

    public HandleClient(Socket Soket, Game game, Server server) {
        this.server = server;
        this.game = game;
        this.Soket = Soket;
        //create input stream and output stream
        listening = true;
    }
    
    

    public Socket getSoket() {
        return Soket;
    }

    public void setSoket(Socket Soket) {
        this.Soket = Soket;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void closeConnection() {
        //close streams and set listening to false;
        listening = false;
    }
    public void run(){
        while(listening){
            
        }
    }
    
}
