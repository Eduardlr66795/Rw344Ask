import static org.junit.Assert.*;

import java.io.IOException;
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
 * @author MeTaBee
 */

public class ServerTest {
    Socket client;
    ObjectInputStream objectInput = null;
    ObjectOutputStream objectOutput = null;
    LinkedList<Socket> clients = new LinkedList<Socket>();
    LinkedList<ObjectInputStream> clientsIn = new LinkedList<ObjectInputStream>();
    LinkedList<ObjectOutputStream> clientsOut = new LinkedList<ObjectOutputStream>();
    int passed = 0;
    String input = "";
    Server server;
    Thread serverThread;
    
    
    public ServerTest() {
    }
    
    @Before
    public void setUp() throws Exception {
    	server = new Server();
    	serverThread = new Thread(server);
    	serverThread.start();
    	Thread.sleep(100);
        client = new Socket("localhost", 3000);
        objectInput = new ObjectInputStream(client.getInputStream());
        objectOutput = new ObjectOutputStream(client.getOutputStream());   
        
        for(int i = 0; i < 5; i++){
            Socket client2 = new Socket("localhost", 3000);
            clients.add(client2);
            ObjectInputStream objectInput2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(client2.getOutputStream());
            clientsIn.add(objectInput2);
            clientsOut.add(objectOutput2);
        }
    }

    @After
    public void tearDown() throws Exception {
        client.close();
        int tir = clients.size();
        for(int i = tir-1; i > 0; i--){
                    clients.get(i).close();
        }
        clients.removeAll(clients);
        clientsIn.removeAll(clientsIn);
        clientsOut.removeAll(clientsOut);
        server.closeServer();
        server = null;
    }
    
    @Test
    public void testOneConnection() throws Exception{
            String serverMsg = (String) objectInput.readObject();
            assertTrue("Fail, ", serverMsg.equals("RD;"));
        
    }
    
    @Test
    public void testManyConnections() throws Exception{
        
        
        for(int i = 0; i < 5; i++){

            
            String serverMsg = (String) clientsIn.get(i).readObject();
            if(serverMsg.equals("RD;")){
                passed ++;
            }

        }
            assertTrue(passed+"/"+ clients.size() + " passed.", passed == clients.size());
    }
    
    @Test
    public void testLOBeforeLogin() throws IOException, ClassNotFoundException, InterruptedException{
		beforeLISetupSteps();
            objectOutput.writeObject("LO;");
            objectOutput.flush();
            String error = (String) objectInput.readObject();
            assertTrue("recieved \'" + error + "\' instead of \'ER102;\'",error.equals("ER102;"));
    }
    
    private void beforeLISetupSteps() throws IOException, ClassNotFoundException, InterruptedException {

    	String serverMsg = (String) objectInput.readObject();
    	for(int i = 0; i < clients.size(); i++){
    		serverMsg = (String) clientsIn.get(i).readObject();
    	}
	}

	@Test
    public void testMCBeforeLogin() throws IOException, ClassNotFoundException, InterruptedException{
			beforeLISetupSteps();
            objectOutput.writeObject("MC;");
            objectOutput.flush();
            String modeList = (String) objectInput.readObject();
            assertTrue("recieved \'" + modeList + "\' instead of \'MLLI;\'",modeList.equals("MLLI;"));
    }
    
    @Test
    public void testLogonOfUniqueClient() throws IOException, ClassNotFoundException, InterruptedException{
    	beforeLISetupSteps();
            objectOutput.writeObject("LIusername:password;");
            objectOutput.flush();
            String response = (String) objectInput.readObject();
            assertTrue("Recieved \'" + response + "\' instead of \'LK;\'",(response.equals("LK;")));
    }
    
    @Test
    public void testLogonOfNonUniqueClientName() throws IOException, ClassNotFoundException, InterruptedException{
    	beforeLISetupSteps();
    	System.out.println("Setup done");
    		clientsOut.get(2).writeObject("LIusername:password;");
    		clientsOut.get(2).flush();
    		Thread.sleep(100);
            clientsOut.get(1).writeObject("LIusername:password;");
            clientsOut.get(1).flush();
            String response = (String) clientsIn.get(1).readObject();
            assertTrue("Recieved \'" + response + "\' instead of \'ER100;\'", response.equals("ER100;"));
    }
    
    @Test
    public void testManyUniqueLogins() throws IOException, ClassNotFoundException, InterruptedException{
    	beforeLISetupSteps();
        passed = 0;
                for(int i = 0; i < clients.size();i++){
                clientsOut.get(i).writeObject("LIusername"+i+":password;");
                clientsOut.get(i).flush();
                String response = (String) clientsIn.get(i).readObject();
                if(response.equals("LK;")){
                	passed ++;
                }
            }
        assertTrue(passed + "/" + clients.size() + "Passed the test.",passed == clients.size());
    }
    
    public void testAfterLoginPhaseSetup() throws IOException, ClassNotFoundException, InterruptedException{
    	beforeLISetupSteps();
        objectOutput.writeObject("LIGamerminus1:password;");
        objectOutput.flush();
        String response = (String) objectInput.readObject();
        if(response.equals("LK;")){
        }
        else{
            
            System.out.println("Error, recieved "+response);
        }
    
        for(int i = 0; i < clients.size();i++){
            clientsOut.get(i).writeObject("LIGamer"+i+":password;");
            clientsOut.get(i).flush();
            response = (String) clientsIn.get(i).readObject();
            if(response.equals("LK;")){
                
            }
            else{
                
                System.out.println("Error recieved " + response + " instead of LK; @ " + i);
            }
        }
    }
    
    public void testAfterLoginBreakdown() throws IOException, ClassNotFoundException{
    	objectOutput.writeObject("LO;");
    	String serverMsg = (String) objectInput.readObject();
    	for(int i = 0; i < clients.size(); i++){
	    	clientsOut.get(i).writeObject("LO;");
	    	serverMsg = (String) clientsIn.get(i).readObject();
    	}
    }
    
    @Test
    public void testSingleLogoff() throws IOException, ClassNotFoundException, InterruptedException{
    	testAfterLoginPhaseSetup();
    	objectOutput.writeObject("LO;");
    	String serverMsg = (String) objectInput.readObject();
    	assertTrue("Recieved \'" + serverMsg + "\' instead of \'LO;\'",serverMsg.equals("LM;"));
    }
    
    @Test
    public void testManyLogoff() throws IOException, ClassNotFoundException, InterruptedException{
    	testAfterLoginPhaseSetup();
    	
    	for(int i = 0; i < clients.size(); i++){
	    	clientsOut.get(i).writeObject("LO;");
	    	String serverMsg = (String) clientsIn.get(i).readObject();
	    	if(serverMsg.equals("LM;")){
	    		passed ++;
	    	}else{
	    		System.out.println(serverMsg + " Recieved instead of \'LM;\'");
	    	}
    	}
    	assertTrue(passed + "/"+ clients.size()+" passed the test",passed == clients.size());
    }
    
    @Test
    public void testValidGame() throws IOException, ClassNotFoundException, InterruptedException{
    	testAfterLoginPhaseSetup();
    	clientsOut.get(1).writeObject("GSgame1;");
    	clientsOut.get(1).flush();
            String serverMsg = (String) clientsIn.get(1).readObject();
            assertTrue("Recieved \'" + serverMsg + "\' instead of \'GK;\'",serverMsg.equals("GK;"));
        testAfterLoginBreakdown();
    }
    
    @Test
    public void testInvalidGame() throws IOException, ClassNotFoundException, InterruptedException{
    	testAfterLoginPhaseSetup();
         clientsOut.get(1).writeObject("GSgame1;");
         clientsOut.get(1).flush();
         String serverMsg = (String) clientsIn.get(1).readObject();
         clientsOut.get(0).writeObject("GSgame1;");
         clientsOut.get(0).flush();
         serverMsg = (String) clientsIn.get(0).readObject();
         assertTrue("Recieved \'" + serverMsg + "\' instead of \'ER120;\'",serverMsg.equals("ER120;"));
         testAfterLoginBreakdown();
    }
    
    
    
    @Test
    public void testManyValidGames() throws IOException, ClassNotFoundException, InterruptedException{
    	testAfterLoginPhaseSetup();
    	passed = 0;
        for(int i = 0; i < clientsOut.size(); i++){
            clientsOut.get(i).writeObject("GSgame1"+i);
            clientsOut.get(i).flush();
            String serverMsg = (String) clientsIn.get(i).readObject();
            if(serverMsg.equals("GK;")){
               passed ++;
            }
            else{
                System.out.println("Fail, Recieved \'" + serverMsg + "\' instead of \'ER120;\'");
            }
        }
        assertTrue(passed + "/" + clientsOut.size() + " passed the test" ,passed == clientsOut.size());
        testAfterLoginBreakdown();
    }
    
    @Test
    public void testLogoffInGame() throws Exception{
    	testAfterGamePhaseSetup();
    	clientsOut.get(0).writeObject("LO;");
    	clientsOut.get(0).flush();
    	String msg = (String) clientsIn.get(0).readObject();
    	assertTrue("Logoff fail, recieved " + msg + "Instead of LM;",msg.equals("LM;"));
    	objectOutput.writeObject("HW;");
    	objectOutput.flush();
    	msg = (String) objectInput.readObject();
    	assertTrue("Logoff fail, recieved " + msg + "Instead of QP;",msg.equals("QPGamer0;"));
    	for(int i = 1; i < clients.size();i++){
    		clientsOut.get(i).writeObject("HW;");
    		clientsOut.get(i).flush();
        	msg = (String) clientsIn.get(i).readObject();
        	assertTrue("Logoff fail, recieved " + msg + "Instead of QP;",msg.equals("QPGamer0;"));
    	}
    }
    
    @Test
    public void testQuitGame() throws Exception{
    	testAfterGamePhaseSetup();
    	clientsOut.get(0).writeObject("QTGame;");
    	clientsOut.get(0).flush();
    	String msg = (String) clientsIn.get(0).readObject();
    	assertTrue("Logoff fail, recieved " + msg + "Instead of QK;",msg.equals("QK;"));
    	objectOutput.writeObject("HAGame;");
    	objectOutput.flush();
    	msg = (String) objectInput.readObject();
    	assertTrue("Logoff fail, recieved " + msg + "Instead of QP;",msg.equals("QPGamer0;"));
    	for(int i = 1; i < clients.size();i++){
    		clientsOut.get(i).writeObject("HAGame;");
    		clientsOut.get(i).flush();
        	msg = (String) clientsIn.get(i).readObject();
        	assertTrue("Logoff fail, recieved " + msg + "Instead of QP;",msg.equals("QPGamer0;"));
    	}
    }
    
    private void testAfterGamePhaseSetup() throws Exception{
    	testAfterLoginPhaseSetup();
    	objectOutput.writeObject("GSGame;");
    	objectOutput.flush();
    	
    	String msg = (String) objectInput.readObject();
    	assertTrue("Could not create game Game: " + msg,msg.equals("GK;"));
    	for(int i = 0; i < clients.size(); i++){
    		clientsOut.get(i).writeObject("GJGame;");
    		clientsOut.get(i).flush();
    		msg = (String)clientsIn.get(i).readObject();
    		assertTrue("Could not join game Game: " + msg,msg.equals("GX;"));
    		
    		objectOutput.writeObject("GNGame;");
    		objectOutput.flush();
    		msg = (String) objectInput.readObject();
    	}
    }
}
