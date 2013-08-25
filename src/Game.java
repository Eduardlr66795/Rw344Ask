
import java.util.HashMap;
import java.util.HashSet;

public class Game extends Thread {

    private String name;
    private Server server;
    //private int players;
    
    private HashSet<HandleClient> clientsList = new HashSet<HandleClient>();
    
    public Game(String name) {
        this.name = name;
    }

    Game(String gameName, HandleClient creator, Server server) {
        name = gameName;
        this.server = server;
        //assuming clientsList will always be empty at first:
        clientsList.add(creator);
    }
    
    
    
    Game(String gameName, Server server) {
        name = gameName;
        this.server = server;
        //assuming clientsList will always be empty at first:
    }

    // deals all cards to users
    void deal(){
        //iterate through all users and add card into hands
    }
}


