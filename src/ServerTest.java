
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
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
        System.out.print("Test Server connection: ");
        Socket client;
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try{
            client = new Socket("localhost", 9119);

            objectInput = new ObjectInputStream(client.getInputStream());
            objectOutput = new ObjectOutputStream(client.getOutputStream());

            String serverMsg = (String) objectInput.readObject();
            if(serverMsg.equals("RD;")){
                objectOutput.writeObject("Bleargh");
                objectOutput.flush();
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
        int passed = 0;
        for(int i = 0; i < 20; i++){
            try {
                Socket client2 = new Socket("localhost", 9119);
                clients.add(client2);
                
            ObjectInputStream objectInput2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(client2.getOutputStream());

            String serverMsg = (String) objectInput2.readObject();
            if(serverMsg.equals("RD;")){
                objectOutput2.writeObject("blergh");
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
        
        System.out.print("Anything except LO, MC or LI before login: \n");
        
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
                    System.out.println("Passed");
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
            System.out.println("Pass.");
        }else{
            System.out.println("Fail ("+passed+"/"+notExpected.length+" passed).");
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
                System.out.println("Fail, recieved \'" + modeList + "\' instead of \'ER102;\'");
            }
        }catch(Exception e){
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
