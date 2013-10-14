import java.util.Hashtable;
import java.util.LinkedList;

public class Game extends Thread {

    private String name;
    private Server server;
    //Game number seems redundant?? But i'll leave it there for now, we might need it
    //public int gameNumber;
    //public int playerCount;
    private int round;
    private String trumpCard;
    //Not sure if we need but we can always remove later
    private String nextPlayerToPlay;
    //Player joins have to be handled this way
    private LinkedList<String> nextPlayersToJoin = new LinkedList<String>() ;
    private boolean addAnotherPlayer;
    
    //Hardcoded to 7 (max players) we can change this but hardcoding it to 7 wont really
    //lead to any inefficiency
    private int [] playerBets = new int [7];
    private int [] playerScores = new int [7];
    //Also hardcoded to 10 (max cards)
    private String [][] playerCards = new String [7][10];
   
    
    private Hashtable<String, HandleClient> playerList = new Hashtable<String, HandleClient>();

    //Dont think we need more than one constructor?? Because theres only one way a game can be started
    public Game(String gameName, String creator, HandleClient client, Server server) {
        this.name = gameName;
        this.server = server;
        this.playerList.put(creator, client);
        //this.playerCount = 1;
        this.round = 1;
        this.addAnotherPlayer = false;
    }
   

    // deals all cards to users
    public void deal(){
        //iterate through all users and add card into hands
    }
    
	public boolean addPlayer(String playerName) {
		// TODO Auto-generated method stub
		//return true if not full
		if(true){
			
		}
		return false;
    }
	
	public boolean containsPlayer(String playerName){
		return playerList.containsKey(playerName);
	}
	
	public void removePlayer(String playerName){
		playerList.remove(playerName);
	}
	
	public int amountOfPlayers(){
		return playerList.size();
	}
	
	public boolean hasNewPlayer(){
		return !nextPlayersToJoin.isEmpty();
	}
	
	public void setAddAnotherPlayer(boolean addAnotherPlayer){
		this.addAnotherPlayer = addAnotherPlayer;
	}

}
