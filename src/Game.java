import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private LinkedList<ObjectOutputStream> nextPlayersToJoin = new LinkedList<ObjectOutputStream>() ;
    private boolean addAnotherPlayer;
    
    //Hardcoded to 7 (max players) we can change this but hardcoding it to 7 wont really
    //lead to any inefficiency
    private int [] playerBets = new int [7];
    private int [] playerScores = new int [7];
    private int max_players;
    //Also hardcoded to 10 (max cards)
    private String [][] playerCards = new String [7][10];
   
    
    private Hashtable<ObjectOutputStream, String> playerList = new Hashtable<ObjectOutputStream, String>();

    //Don't think we need more than one constructor?? Because there's only one way a game can be started
    public Game(String gameName, Socket creator, Server server, int max_players) {
        this.name = gameName;
        this.max_players = max_players;
        this.server = server;
        ObjectOutputStream o = null;
		try {
			o = (ObjectOutputStream) creator.getOutputStream();
		} catch (IOException e) {
			System.out.println("error in creating a game " + e);
		}
        String creatorName = server.getName(o);
        this.playerList.put(o, creatorName);
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
	
	public void addPlayerToGame(){
		ObjectOutputStream addUser = nextPlayersToJoin.getLast();
		playerList.put(addUser , server.getName(addUser));
	}
	
	public ObjectOutputStream getAddedPlayer(){
		addPlayerToGame();
		return nextPlayersToJoin.getLast();
	}
	
	public boolean gameIsFull(){
		return (playerList.size()+ nextPlayersToJoin.size()) >= max_players;
	}

}
