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
    LinkedList<Socket> clients = new LinkedList<Socket>();
    LinkedList<ObjectInputStream> clientsIn = new LinkedList<ObjectInputStream>();
    LinkedList<ObjectOutputStream> clientsOut = new LinkedList<ObjectOutputStream>();
    int passed = 0;
    
    @Parameterized.Parameters
   public static Collection messageInputs() throws FileNotFoundException, IOException {
        String[] a = (new BufferedReader(new FileReader("./src/InvalidInput1"))).readLine().split(",");
        String [][] b = new String[1][];
        b[0] = a;
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
            
            for(int i = 0; i < 20; i++){
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
                    clients.remove(i);
        }
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