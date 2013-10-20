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
    
    
}