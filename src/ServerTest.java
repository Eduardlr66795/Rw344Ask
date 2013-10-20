import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MeTaBee
 */

public class ServerTest {
    static Socket client;
    static ObjectInputStream objectInput = null;
    static ObjectOutputStream objectOutput = null;
    static LinkedList<Socket> clients = new LinkedList<Socket>();
    static LinkedList<ObjectInputStream> clientsIn = new LinkedList<ObjectInputStream>();
    static LinkedList<ObjectOutputStream> clientsOut = new LinkedList<ObjectOutputStream>();
    static int passed = 0;
    static String input = "";
        
    
    public ServerTest() {
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
    public void testOneConnection() throws Exception{
       
            String serverMsg = (String) objectInput.readObject();
            assertTrue("Fail, ", serverMsg.equals("RD;"));
        
        /////....................................................................................................
        
     /*   System.out.print("LO before login: ");
        try{
            objectOutput.writeObject("LO;");
            objectOutput.flush();
            String error = (String) objectInput.readObject();
            if(error.equals("ER102;")){
                System.out.println("Pass.");
            } else{
                System.out.println("Fail, recieved \'" + error + "\' instead of \'ER102;\'");
            }
        }catch(Exception e){
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.print("MC before login: ");
        try{
            objectOutput.writeObject("MC;");
            objectOutput.flush();
            String modeList = (String) objectInput.readObject();
            if(modeList.equals("MLLI;")){
                System.out.println("Pass.");
            } else{
                System.out.println("Fail, recieved \'" + modeList + "\' instead of \'MLLI;\'");
            }
        }catch(Exception e){
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.print("Test login of unique client (NOTE: This client has been sent a LO command earlier, I suspect it causes failiure): ");
        try{
            objectOutput.writeObject("LIusername:password;");
            objectOutput.flush();
            String response = (String) objectInput.readObject();
            if(response.equals("LK;")){
                System.out.println("Pass.");
            }
            else{
                System.out.println("Fail, Recieved \'" + response + "\' instead of \'LK;\'");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        
        System.out.print("Test login of non-Unique client: ");
        try{
            
            clientsOut.get(0).writeObject("LIusername:password;");
            clientsOut.get(0).flush();
            String response = (String) objectInput.readObject();
            assertTrue("Fail, Recieved \'" + response + "\' instead of \'ER100;\'", response.equals("ER100;"));
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        System.out.print("Test login of many unique clients: ");
        passed = 0;
        try{
                for(int i = 1; i < clients.size();i++){
                clientsOut.get(i).writeObject("LIusername"+i+":password;");
                clientsOut.get(i).flush();
                String response = (String) clientsIn.get(i).readObject();
                assertTrue("Fail, Recieved \'" + response + "\' instead of \'ER100;\'", response.equals("LK;"));
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        if(passed == 19){
            System.out.println("Pass.");
        }else{
            System.out.println("Fail ("+passed+"/19 passed).");
        }
        
        //----~Ending steps~----*/
        
    }
    
    @Test
    public void testManyConnections() throws Exception{
        
        System.out.print("Test Many connections simultaneously: ");
        
        for(int i = 0; i < 20; i++){

            Socket client2 = new Socket("localhost", 3000);
            clients.add(client2);
            ObjectInputStream objectInput2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(client2.getOutputStream());
            clientsIn.add(objectInput2);
            clientsOut.add(objectOutput2);

            String serverMsg = (String) objectInput2.readObject();
            if(serverMsg.equals("RD;")){
                passed ++;
            }

        }
            assertTrue(passed+"/20 passed.", passed == 20);
    }
    
    
    @Test
    public void testGamePhase() {
        //---~Setup for testing game phase.~---
        Socket client;
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try{
            client = new Socket("localhost", 3000);

            objectInput = new ObjectInputStream(client.getInputStream());
            objectOutput = new ObjectOutputStream(client.getOutputStream());

            String serverMsg = (String) objectInput.readObject();
            if(serverMsg.equals("RD;")){
            }
            else{
                System.out.println("Error recieved "+serverMsg+" instead of RD;");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        
        LinkedList<Socket> clients = new LinkedList<Socket>();
        LinkedList<ObjectInputStream> clientsIn = new LinkedList<ObjectInputStream>();
        LinkedList<ObjectOutputStream> clientsOut = new LinkedList<ObjectOutputStream>();
        
        for(int i = 0; i < 20; i++){
            try {
                Socket client2 = new Socket("localhost", 3000);
                clients.add(client2);
                
            ObjectInputStream objectInput2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(client2.getOutputStream());
            clientsIn.add(objectInput2);
            clientsOut.add(objectOutput2);

            String serverMsg = (String) objectInput2.readObject();
            if(serverMsg.equals("RD;")){
            }else{
                System.out.println("Error recieved "+serverMsg+" instead of RD;");
            }
                
            } catch (UnknownHostException ex) {
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e){
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        try{
            objectOutput.writeObject("LIGamer:password;");
            objectOutput.flush();
            String response = (String) objectInput.readObject();
            if(response.equals("LK;")){// TODO: Still need to put in the ':' once corrected in server. 
            }
            else{
                
                System.out.println("Error, recieved "+response);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        try{
                for(int i = 0; i < clients.size();i++){
                clientsOut.get(i).writeObject("LIGamer"+i+":password;");
                clientsOut.get(i).flush();
                String response = (String) clientsIn.get(i).readObject();
                if(response.equals("LK;")){
                    
                }
                else{
                    
                    System.out.println("Error recieved "+response+" instead of LK;");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            
            System.out.println("Error.");
        }
        System.out.println("Setup complete");
        //------------~Setup End~-------------
        System.out.print("Create a valid game:");
        try{
            objectOutput.writeObject("GSgame1;");
            objectOutput.flush();
            String serverMsg = (String) objectInput.readObject();
            if(serverMsg.equals("GK;")){
                System.out.println("Pass");
            }
            else{
                System.out.println("Fail, Recieved \'" + serverMsg + "\' instead of \'GK;\'");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        System.out.print("Create a invalid game:");
        try{
            clientsOut.get(0).writeObject("GSgame1;");
            clientsOut.get(0).flush();
            String serverMsg = (String) clientsIn.get(0).readObject();
            if(serverMsg.equals("ER120;")){
                System.out.println("Pass");
            }
            else{
                System.out.println("Fail, Recieved \'" + serverMsg + "\' instead of \'ER120;\'");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        
        System.out.println("Create many valid games");
         try{
            for(int i = 1; i < clientsOut.size(); i++){
                clientsOut.get(i).writeObject("GSgame1"+i);
                clientsOut.get(i).flush();
                String serverMsg = (String) clientsIn.get(i).readObject();
                if(serverMsg.equals("GK;")){
                    System.out.println("Pass");
                }
                else{
                    System.out.println("Fail, Recieved \'" + serverMsg + "\' instead of \'ER120;\'");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        
    }
}
