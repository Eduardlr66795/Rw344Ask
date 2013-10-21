import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 16579070
 */
@RunWith(Parameterized.class)
public class TestInvalidInput{
    private String input = "";
    Socket client;
    ObjectInputStream objectInput = null;
    ObjectOutputStream objectOutput = null;
    int passed = 0;
	private Server server;
	private Thread serverThread;
	private String input2;
	private String input3;
	private String input4;
    
    @Parameters
   public static Collection messageInputs() throws FileNotFoundException, IOException {
    	BufferedReader read = new BufferedReader(new FileReader("./src/InvalidInput1"));
        String[] a = read.readLine().split(",");
        String[] b = read.readLine().split(",");
        String[] c = read.readLine().split(",");
        
        String[] d = read.readLine().split(",");
        String [][] e;
        int maxlength = Math.max(a.length, Math.max(b.length, Math.max(c.length,d.length)));
        e = new String [maxlength][4];
        
        for(int i = 0; i < a.length; i++){
        	if(i<a.length){
        		e[i][0] = a[i];
        	}else{
        		e[i][0] = "GNGamename;";
        	}
        	if(i<b.length){
	        	e[i][1] = b[i];
        	}else{
        		e[i][1] = "GNGamename;";
        	}
        	if(c.length>i){
	        	e[i][2] = c[i];
        	}else{
        		e[i][2] = "LIusername:password;";
        	}
        	if(d.length>i){
	        	e[i][3] = d[i];
        	}else{
        		e[i][3] = "&*^%$;";
        	}
        }
        
        return Arrays.asList(e);
   }

    public TestInvalidInput(String input,String input2,String input3, String input4) {
        this.input = input;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
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
        String error = (String) objectInput.readObject();
        	
        
    }

    @After
    public void tearDown() throws Exception {
        client.close();
        server.closeServer();
        server = null;
    }
    
    @Test
    public void testInvalidInputsBeforeLogin() throws Exception{
        System.out.print("Anything except LO, MC or LI before login Start \n");
        objectOutput.writeObject(input);
        objectOutput.flush();

        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER901;\' @ "+ input, error.equals("ER901;"));
        
    }
    
    @Test
    public void testInvalidInputsBeforeGame() throws Exception{
    	objectOutput.writeObject("LIusername:Password;");
    	objectOutput.flush();
    	String rd = (String)objectInput.readObject();
    	
        objectOutput.writeObject(input2);
        objectOutput.flush();

        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER901;\' @ "+ input2, error.equals("ER901;"));
    }
    
    @Test
    public void testInvalidInputsAfterGameCreation() throws Exception{
    	objectOutput.writeObject("LIusername:Password;");
    	objectOutput.flush();
    	String rd = (String)objectInput.readObject();
    	
    	objectOutput.writeObject("GSgamename;");
    	objectOutput.flush();
    	rd = (String)objectInput.readObject();
    	
        objectOutput.writeObject(input3);
        objectOutput.flush();
        
        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER901;\' @ "+ input3, error.equals("ER901;"));
    }
    
    @Test
    public void testInvalidInputsBeforeLoginNotAMessage() throws Exception{
        System.out.print("Anything except LO, MC or LI before login Start \n");
        objectOutput.writeObject(input4);
        objectOutput.flush();
        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER900;\' @ "+ input4, error.equals("ER900;"));
        
    }
    
    @Test
    public void testInvalidInputsBeforeGameNotAMessage() throws Exception{
    	objectOutput.writeObject("LIusername:Password;");
    	objectOutput.flush();
    	String rd = (String)objectInput.readObject();
    	
        objectOutput.writeObject(input4);
        objectOutput.flush();
        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER900;\' @ "+ input4, error.equals("ER900;"));
    }
    
    
    
}