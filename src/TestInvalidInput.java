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
 * @author //Ask
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
	private Socket client3;
	private Socket client2;
	private ObjectOutputStream objectOutput2;
	private ObjectOutputStream objectOutput3;
	private ObjectInputStream objectInput2;
	private ObjectInputStream objectInput3;
	private String input5;
    
    @SuppressWarnings("rawtypes")
	@Parameters
   public static Collection messageInputs() throws FileNotFoundException, IOException {
    	BufferedReader read = new BufferedReader(new FileReader("./src/InvalidInput1"));
        String[] a = read.readLine().split(",");
        String[] b = read.readLine().split(",");
        String[] c = read.readLine().split(",");
        
        String[] d = read.readLine().split(",");
        String[] e = read.readLine().split(",");
        String [][] end;
        int maxlength = Math.max(a.length, Math.max(b.length, Math.max(c.length,Math.max(d.length,e.length))));
        end = new String [maxlength][5];
        
        for(int i = 0; i < a.length; i++){
        	if(i<a.length){
        		end[i][0] = a[i];
        	}else{
        		end[i][0] = "LIusername:password;";
        	}
        	if(i<b.length){
	        	end[i][1] = b[i];
        	}else{
        		end[i][1] = "LIusername:password;";
        	}
        	if(c.length>i){
	        	end[i][2] = c[i];
        	}else{
        		end[i][2] = "LIusername:password;";
        	}
        	if(d.length>i){
	        	end[i][3] = d[i];
        	}else{
        		end[i][3] = "&*^%$;";
        	}
        	if(e.length>i){
        		end[i][4] = e[i];
        	}else{
        		end[i][4] = "LIusername:password";
        	}
        }
        
        return Arrays.asList(end);
   }

    public TestInvalidInput(String input,String input2,String input3, String input4,String input5) {
        this.input = input;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.input5 = input5;
    }
    

    @SuppressWarnings("unused")
	@Before
         public void setUp() throws Exception {
    	server = new Server();
    	serverThread = new Thread(server);
    	serverThread.start();
    	Thread.sleep(100);
        client = new Socket("localhost", 3000);
        client2 = new Socket("localhost", 3000);
        client3 = new Socket("localhost", 3000);
        objectInput = new ObjectInputStream(client.getInputStream());
        objectOutput = new ObjectOutputStream(client.getOutputStream());
        objectInput2 = new ObjectInputStream(client2.getInputStream());
        objectOutput2 = new ObjectOutputStream(client2.getOutputStream());
        objectInput3 = new ObjectInputStream(client3.getInputStream());
        objectOutput3 = new ObjectOutputStream(client3.getOutputStream());
        String error = (String) objectInput.readObject();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
        client2.close();
        client3.close();
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
    
    @SuppressWarnings("unused")
	@Test
    public void testInvalidInputsBeforeGame() throws Exception{
    	objectOutput.writeObject("LIusername:password;");
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
    	System.out.println("error " + error);
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
    
    @Test
    public void testInvalidInputsAfterGameCreationNotAMessage() throws Exception{
    	objectOutput.writeObject("LIusername:Password;");
    	objectOutput.flush();
    	String rd = (String)objectInput.readObject();
    	
    	objectOutput.writeObject("GSgamename;");
    	objectOutput.flush();
    	rd = (String)objectInput.readObject();
    	
        objectOutput.writeObject(input4);
        objectOutput.flush();
        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER900;\' @ "+ input4, error.equals("ER900;"));
    }
    
    @Test
    public void testInvalidInputsGameStartPlayer1() throws Exception{
    	setupGameStart();
    	
        objectOutput.writeObject(input5);
        objectOutput.flush();
        
        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER901;\' @ "+ input5, error.equals("ER901;"));
    }

	private void setupGameStart() throws Exception {
		objectOutput.writeObject("LIusername:Password;");
    	objectOutput.flush();
    	String rd = (String)objectInput.readObject();
    	
    	objectOutput.writeObject("GSgamename;");
    	objectOutput.flush();
    	rd = (String)objectInput.readObject();
    	
    	objectOutput2.writeObject("LIusername2:Password;");
    	objectOutput2.flush();
    	rd = (String)objectInput2.readObject();
    	objectOutput3.writeObject("LIusername3:Password;");
    	objectOutput3.flush();
    	rd = (String)objectInput3.readObject();
    	
    	objectOutput2.writeObject("GJgamename;");
    	objectOutput2.flush();
    	rd = (String)objectInput2.readObject();
    	objectOutput3.writeObject("GJgamename;");
    	objectOutput3.flush();
    	rd = (String)objectInput3.readObject();
    	Thread.sleep(100);
    	objectOutput.writeObject("GPgamename;");
    	objectOutput.flush();
    	rd = (String)objectInput.readObject();
	}
    
}
