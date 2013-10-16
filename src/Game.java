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
    public String trumpSuit;
    
    //Not sure if we need but we can always remove later
    public String nextPlayerToPlay;
    
    //Player joins have to be handled this way
    public LinkedList<String> nextPlayersToJoin = new LinkedList<String>() ;
    
    // Next player to bid
    public String nextPlayerToBid;
    
    // Last bid played and the player that played it
    public String lastBid = "none:none";
    
    // Bid has been requested
    public boolean bidRequested;
    
    //Hardcoded to 7 (max players) we can change this but hardcoding it to 7 wont really
    //lead to any inefficiency
    public int [] playerBids = new int [7];
    
    public int [] playerScores = new int [7];
    
    //Also hardcoded to 10 (max cards)
    public String [][] playerCards = new String [7][10];
    
    public Hashtable <String, Integer> playerList = new Hashtable <String, Integer>();
    
    public boolean readyToStart;

    //Don't think we need more than one constructor?? Because there's only one way a game can be started
    public Game(String gameName, String creatorName) {
        this.gameName = gameName;
        this.creatorName = creatorName;
        this.playerList.put(creatorName,0);
        this.playerCount = 1;
        this.round = 1;
        this.readyToStart = false;
    }
   

    // deals all cards to users
    public void deal(){
        //iterate through all users and add card into hands
    	String [] AllCards = {"H2","H3","H4","H5","H6","H7","H8","H9","HT","HJ","HQ","HK","HA","S2","S3","S4","S5","S6","S7","S8","S9","ST","SJ","SQ","SK","SA",
    	                   "D2","D3","D4","D5","D6","D7","D8","D9","DT","DJ","DQ","DK","DA","C2","C3","C4","C5","C6","D7","C8","C9","CT","CJ","CQ","CK","CA"};

    	for (int i = 51; i >= 0; i--) {
    		int rnd = (int) (Math.random() * i);
    		String tmp = AllCards[i];
    		AllCards[i] = AllCards[rnd];
    		AllCards[rnd] = tmp;  
    		
    	}
    	
    	for (int i = 0; i < 52; i++) {
    		
    		if ((this.playerCount <= 5) && (this.playerCount >= 3)) {
    			if (i < this.playerCount * 10) {
    				this.playerCards[i % this.playerCount][i/this.playerCount] = AllCards[i];
    			} else {
    				this.trumpSuit = AllCards[i].substring(0,1);
    				return;
    			}
    		}
    		
    		else if (this.playerCount == 6) {
    			if (i < 48) {
    				this.playerCards[i % this.playerCount][i/this.playerCount] = AllCards[i];
    			} else {
    				this.trumpSuit = AllCards[i].substring(0,1);
    				return;
    			}
    		}
    		
    		else if (this.playerCount == 7) {
    			if (i < 49) {
    				this.playerCards[i % this.playerCount][i/this.playerCount] = AllCards[i];
    			} else {
    				this.trumpSuit = AllCards[i].substring(0,1);
    				return;
    			}
    		}
    		
    	}
    	
    }
    
	

}
