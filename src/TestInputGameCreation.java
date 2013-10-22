import static org.junit.Assert.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 16579070
 */
public class TestInputGameCreation{
    LinkedList<Socket> clients = new LinkedList<Socket>();
    LinkedList<ObjectInputStream> clientsIn = new LinkedList<ObjectInputStream>();
    LinkedList<ObjectOutputStream> clientsOut = new LinkedList<ObjectOutputStream>();
    Server server;
    Thread serverThread;
    int passed = 0;
    

    public TestInputGameCreation () {
    }
    
    @Before
     public void setUp() throws Exception {
    	server = new Server();
    	serverThread = new Thread(server);
    	serverThread.start();
    	Thread.sleep(100);
    	for(int i = 0; i < 7; i++){
            Socket client2 = new Socket("localhost", 3000);
            clients.add(client2);
            ObjectInputStream objectInput2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(client2.getOutputStream());
            clientsIn.add(objectInput2);
            clientsOut.add(objectOutput2);
            String msg = (String) objectInput2.readObject();
            System.out.println(msg);
            objectOutput2.writeObject("LIPlayer"+i+":password;");
            msg = (String) objectInput2.readObject();
            System.out.println(msg);
        }
    }

    @After
    public void tearDown() throws Exception {
    	server.closeServer();
    }
    
    @Test
    public void testCreateGame1() throws Exception{
        String inputs = "Player0:GSGame; Player1:GL; Player2:GL;";
        String outputs = "Player0:GK; Player1:GUGame; Player2:GUGame;";
        
        clientsOut.get(0).writeObject("GSGame;");
        clientsOut.get(0).flush();
        String player0 = (String)clientsIn.get(0).readObject();
        clientsOut.get(1).writeObject("GL;");
        clientsOut.get(1).flush();
        String player1 = (String)clientsIn.get(1).readObject();
        clientsOut.get(2).writeObject("GL;");
        clientsOut.get(2).flush();
        String player2 = (String)clientsIn.get(2).readObject();
        
        assertTrue("Inputs: " + inputs + "; Found: Player0:" + player0 
        		+ " Player1:" + player1 + " Player2:" + player2
        		+ "; Expected: " + outputs,
        		player0.equals("GK;")&&player1.equals("GUGame;")&&player2.equals("GUGame;"));
    }
    
    @Test
    public void testCreateGame2() throws Exception{
    	
        String inputs = "Player0:GSGame; Player0:GN; Player1:GJGame; Player0:GN;";
        String outputs = "Player0:GK; Player0:GZ; Player1:GX; Player0:GPPlayer1;";
        
        clientsOut.get(0).writeObject("GSGame;");
        clientsOut.get(0).flush();
        String player01 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player02 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(1).writeObject("GJGame;");
        clientsOut.get(1).flush();
        String player1 = (String)clientsIn.get(1).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player03 = (String)clientsIn.get(0).readObject();
        
        assertTrue("Inputs: " + inputs + "; Found: Player0:" + player01 
        		+ " Player0:" + player02 + " Player2:" + player1
        		+ " Player0:" + player03 + "; Expected: " + outputs,
        		player01.equals("GK;")&&player02.equals("GZ;")&&player1.equals("GX;")&&player03.equals("GPPlayer1;"));
    }
    
    @Test
    public void testCreateGame3() throws Exception{
    	
        String inputs = "Player0:GSGame; Player1:GJGame; Player0:GNGame; Player2:GJGame; Player0:GNGame; Player3:GJ; Player0:GNGame; Player0:GFGame";
        String outputs = "Player0:GK; Player1:GX; Player0:GPPlayer1; Player2:GX; Player0:GPPlayer2; Player3:GX; Player0:GPPlayer3; Player0:ER131;";
        
        clientsOut.get(0).writeObject("GSGame;");
        clientsOut.get(0).flush();
        String player01 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(1).writeObject("GJGame;");
        clientsOut.get(1).flush();
        String player1 = (String)clientsIn.get(1).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player02 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(2).writeObject("GJGame;");
        clientsOut.get(2).flush();
        String player2 = (String)clientsIn.get(2).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player03 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(3).writeObject("GJGame;");
        clientsOut.get(3).flush();
        String player3 = (String)clientsIn.get(3).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player04 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(0).writeObject("GFGame;");
        clientsOut.get(0).flush();
        String player05 = (String)clientsIn.get(0).readObject();
        
        assertTrue("Inputs: " + inputs + "; \nFound: Player0:" + player01 
        		+ " Player1:" + player1 + " Player0:" + player02
        		+ " Player2:" + player2 + " Player0:" + player03
        		+ " Player3:" + player3 + " Player0:" + player04
        		+ " Player0:" + player05 + " \nExpected: " + outputs,
        		player01.equals("GK;")&&player02.equals("GPPlayer1;")&&
        		player1.equals("GX;")&&player2.equals("GX;")&&
        		player3.equals("GX;")&&player03.equals("GPPlayer2;")&&
        		player04.equals("GPPlayer3;")&&player05.equals("GM;"));
    }
    
    @Test
    public void testCreateGame4() throws Exception{
    	
        String inputs = "Player0:GSGame; Player1:GJGame; Player0:GNGame; Player2:GJGame; " +
        		"Player0:GNGame; Player3:GJ; Player0:GNGame; Player0:GFGame; Player4:GJ; " +
        		"Player5:GJ; Player6:GJ; Player0:GNGame; Player0:GNGame; Player0:GNGame; Player0:GFGame;";
        String outputs = "Player0:GK; Player1:GX; Player0:GPPlayer1; Player2:GX; Player0:GPPlayer2; " +
        		"Player3:GX; Player0:GPPlayer3; Player4:GX; Player5:GX; Player6:GX; Player0:GPPlayer4; " +
        		"Player0:GPPlayer5; Player0:GPPlayer6; Player0:GM;";
        
        clientsOut.get(0).writeObject("GSGame;");
        clientsOut.get(0).flush();
        String player01 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(1).writeObject("GJGame;");
        clientsOut.get(1).flush();
        String player1 = (String)clientsIn.get(1).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player02 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(2).writeObject("GJGame;");
        clientsOut.get(2).flush();
        String player2 = (String)clientsIn.get(2).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player03 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(3).writeObject("GJGame;");
        clientsOut.get(3).flush();
        String player3 = (String)clientsIn.get(3).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player04 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(4).writeObject("GJGame;");
        clientsOut.get(4).flush();
        String player4 = (String)clientsIn.get(4).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player05 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(5).writeObject("GJGame;");
        clientsOut.get(5).flush();
        String player5 = (String)clientsIn.get(5).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player06 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(6).writeObject("GJGame;");
        clientsOut.get(6).flush();
        String player6 = (String)clientsIn.get(6).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player07 = (String)clientsIn.get(0).readObject();
        clientsOut.get(0).writeObject("GFGame;");
        clientsOut.get(0).flush();
        String player08 = (String)clientsIn.get(0).readObject();
        
        assertTrue("Inputs: " + inputs + "; \nFound: Player0:" + player01 
        		+ " Player1:" + player1 + " Player0:" + player02
        		+ " Player2:" + player2 + " Player0:" + player03
        		+ " Player3:" + player3 + " Player0:" + player04
        		+ " Player4:" + player4 + " Player5:" + player5
        		+ " Player6:" + player6 + " Player0:" + player05
        		+ " Player0:" + player06 + " Player0:" + player07
        		+ " Player0:" + player08 + "; \nExpected: " + outputs,
        		player01.equals("GK;")&&player02.equals("GPPlayer1;")&&
        		player1.equals("GX;")&&player2.equals("GX;")&&
        		player3.equals("GX;")&&player03.equals("GPPlayer2;")&&
        		player04.equals("GPPlayer3;")&&player4.equals("GX;")&&
        		player5.equals("GX;")&&player6.equals("GX;")&&
        		player05.equals("GPPlayer4;")&&player06.equals("GPPlayer5;")&&
        		player07.equals("GPPlayer6;")&&player08.equals("GM;"));
    }
    
    @Test
    public void testCreateGame5() throws Exception{
    	
        String inputs = "Player0:GSGame; Player1:GJGame; Player0:GNGame; Player0:GNGame; Player0:GFGame";
        String outputs = "Player0:GK; Player1:GX; Player0:GPPlayer1; Player0:GZ; Player0:ER131;";
        
        clientsOut.get(0).writeObject("GSGame;");
        clientsOut.get(0).flush();
        String player01 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(1).writeObject("GJGame;");
        clientsOut.get(1).flush();
        String player1 = (String)clientsIn.get(1).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player02 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(0).writeObject("GNGame;");
        clientsOut.get(0).flush();
        String player03 = (String)clientsIn.get(0).readObject();
        
        clientsOut.get(0).writeObject("GFGame;");
        clientsOut.get(0).flush();
        String player04 = (String)clientsIn.get(0).readObject();
        
        assertTrue("Inputs: " + inputs + "; \nFound: Player0:" + player01 
        		+ " Player1:" + player1 + " Player0:" + player02
        		+ " Player0:" + player03 + " Player0:" + player04
        		+ " \nExpected: " + outputs,
        		player01.equals("GK;")&&player02.equals("GPPlayer1;")&&
        		player1.equals("GX;")&&player03.equals("GZ;")&&
        		player04.equals("ER131;"));
    }
    
}