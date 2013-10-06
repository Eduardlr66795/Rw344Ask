import java.util.Hashtable;

public class Game extends Thread {

    public String name;
    public Server server;
    //Game number seems redundant?? But i'll leave it there for now, we might need it
    public int gameNumber;
    public int playerCount;
    public int round;
    public String trumpCard;
    //Not sure if we need but we can always remove later
    public String nextPlayerToPlay;
    
    //Hardcoded to 7 (max players) we can change this but hardcoding it to 7 wont really
    //lead to any inefficiency
    public int [] playerBets = new int [7];
    public int [] playerScores = new int [7];
    //Also hardcoded to 10 (max cards)
    public String [][] playerCards = new String [7][10];
   
    
    public Hashtable<String, Integer> playerList = new Hashtable<String, Integer>();

    //Dont think we need more than one constructor?? Because theres only one way a game can be started
    public Game(String gameName, String creator, Server server, int gameNumber) {
        this.name = gameName;
        this.server = server;
        this.playerList.put(creator, 1);
        this.playerCount = 1;
        this.round = 1;
        this.gameNumber = gameNumber;
    }
   

    // deals all cards to users
    void deal(){
        //iterate through all users and add card into hands
    }

    void allowNewPlayer() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void startGame(String gameName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
}

