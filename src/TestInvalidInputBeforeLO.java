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
public class TestInvalidInputBeforeLO {
    private String input = "";
    Socket client;
    ObjectInputStream objectInput = null;
    ObjectOutputStream objectOutput = null;
    int passed = 0;
    
    @Parameters
   public static Collection messageInputs() throws FileNotFoundException, IOException {
        String[] a = (new BufferedReader(new FileReader("./src/InvalidInput1"))).readLine().split(",");
        String [][] b;
        b = new String [a.length][1];
        for(int i = 0; i < a.length; i++){
        	b[i][0] = a[i];
        }
        return Arrays.asList(b);
   }

    public TestInvalidInputBeforeLO(String input) {
        this.input = input;
    }
    
    @Before
         public void setUp() throws Exception {
            client = new Socket("localhost", 3000);
            objectInput = new ObjectInputStream(client.getInputStream());
            objectOutput = new ObjectOutputStream(client.getOutputStream());
            String error = (String) objectInput.readObject();
    }

    @After
        public void tearDown() throws Exception {
        client.close();
    }
    
    @Test
    public void testInvalidInputsBeforeLogin() throws Exception{
        System.out.print("Anything except LO, MC or LI before login Start \n");
        objectOutput.writeObject(input);
        objectOutput.flush();

        String error = (String) objectInput.readObject();
        assertTrue("recieved \'" + error + "\' instead of \'ER901;\' @ "+ input, error.equals("ER901;"));
        
    }
}