import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.LinkedList;

public class Game extends Thread {

    public String gameName;
    public String creatorName;
    public Server server;
    
    public int playerCount;
    public int round;
    public String trumpCard;
    
    //Not sure if we need but we can always remove later
    public String nextPlayerToPlay;
    
    //Player joins have to be handled this way
    public LinkedList<String> nextPlayersToJoin = new LinkedList<String>() ;
    
    //Hardcoded to 7 (max players) we can change this but hardcoding it to 7 wont really
    //lead to any inefficiency
    public int [] playerBets = new int [7];
    
    public int [] playerScores = new int [7];
    
    //Also hardcoded to 10 (max cards)
    public String [][] playerCards = new String [7][10];
    
    public Hashtable <String, Integer> playerList = new Hashtable <String, Integer>();
    
    public boolean readyToStart;

    //Don't think we need more than one constructor?? Because there's only one way a game can be started
    public Game(String gameName, String creatorName, Server server) {
        this.gameName = gameName;
        this.server = server;
        this.creatorName = creatorName;
        this.playerList.put(creatorName,0);
        this.playerCount = 1;
        this.round = 1;
        this.readyToStart = false;
    }
   

    // deals all cards to users
    public void deal(){
        //iterate through all users and add card into hands
    }
    
	

}
