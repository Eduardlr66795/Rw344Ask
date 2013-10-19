import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MeTaBee
 */
public class ServerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("------~BEFORE LOGON~------");
        testLogonPhase();
        System.out.println("------~AFTER LOGON~------");
        System.out.println("------~BEFORE GAME~------");
        testGamePhase();
        System.out.println("------~AFTER GAME~------");
    }
    
    private static void testLogonPhase(){
        System.out.print("Test Server connection: ");
        Socket client;
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try{
            client = new Socket("localhost", 3000);

            objectInput = new ObjectInputStream(client.getInputStream());
            objectOutput = new ObjectOutputStream(client.getOutputStream());

            String serverMsg = (String) objectInput.readObject();
            if(serverMsg.equals("RD;")){
                System.out.println("Pass.");
            }
            else{
                System.out.println("Fail.");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error.");
        }
        
        System.out.print("Test Many connections simultaneously: ");
        LinkedList<Socket> clients = new LinkedList<Socket>();
        LinkedList<ObjectInputStream> clientsIn = new LinkedList<ObjectInputStream>();
        LinkedList<ObjectOutputStream> clientsOut = new LinkedList<ObjectOutputStream>();
        int passed = 0;
        for(int i = 0; i < 20; i++){
            try {
                Socket client2 = new Socket("localhost", 9119);
                clients.add(client2);
                
            ObjectInputStream objectInput2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(client2.getOutputStream());
            clientsIn.add(objectInput2);
            clientsOut.add(objectOutput2);

            String serverMsg = (String) objectInput2.readObject();
            if(serverMsg.equals("RD;")){
                passed ++;
            }
                
            } catch (UnknownHostException ex) {
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e){
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if(passed == 20){
            System.out.println("Pass.");
        }else{
            System.out.println("Fail ("+passed+"/20 passed).");
        }
        
        System.out.println("Test if server will send error when wrong command is sent.");
        
        String[] notExpected = {"GSgame_name;","GN;","GFgame_name;","GOgame_name:player_name;",
            "GL;","GGprefix;","GJgame_name;","QWgame_name;","GAgame_name;","QTgame_name;","QPname;",
        "HNgame_name;","HBgame_name;","HDgame_name:bid;","HPgame_name;","HRgame_name:card;",
        "HAgame_name;","HSgame_name;","CPplayer_name1:player_name2:player_namen:message;",
        "CAgame_name:message;","CC;",""};
        
        System.out.print("Anything except LO, MC or LI before login Start \n");
        
            passed = 0;
            for(int i = 0; i < notExpected.length; i++){
                System.out.print("\t" + notExpected[i] + " before login: ");
                try{
                objectOutput.writeObject(notExpected[i]);
                objectOutput.flush();
                } catch (Exception e){
                    //Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
                    System.out.print("Could not send message.   ");
                }
                String error = "";
                try{
                    error = (String) objectInput.readObject();
                    if(error.equals("ER901;")){
                    System.out.print("Passed");
                        passed ++;
                    } else{
                        System.out.print("Fail, recieved \'" + error + "\' instead of \'ER901;\' @ "+ notExpected[i]);
                    }
                }catch(Exception e){
                    //Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
                    System.out.print("Could not read message.   ");
                }
                System.out.println("");
            }
            
        
        if(passed == notExpected.length){
            System.out.println("Anything except LO, MC or LI before login End: All Pass.");
        }else{
            System.out.println("Anything except LO, MC or LI before login End: Fail ("+passed+"/"+notExpected.length+" passed).");
        }
        
        System.out.print("LO before login: ");
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
            if(response.equals("ER100;")){
                System.out.println("Pass.");
            }
            else{
                System.out.println("Fail, Recieved \'" + response + "\' instead of \'ER100;\'");
            }
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
                if(response.equals("LK;")){
                    passed ++;
                }
                else{
                }
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
    }

    private static void testGamePhase() {
        //---~Setup for testing game phase.~---
        Socket client;
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try{
            client = new Socket("localhost", 9119);

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
            objectOutput.writeObject("GSgame1unique;");
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
    }
}
