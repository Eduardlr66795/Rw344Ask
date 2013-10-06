import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Client extends JDialog implements ActionListener {

	// Global Variables
	private Socket client;
	private int port = 9119;
	private PrintWriter pw;
	private BufferedReader brufferReader;
	private MessagesThread threadMessage;
	private String usernName;

	// WelcomeScreen
	private JFrame frameWelcome;
	private JPanel panel;
	private JTextField loginTextfieldName;
	private JTextField loginTextfielIp;
	private JButton loginButton;

	// MainGui
	private String[] games;
	private JPanel[] bigframe;
	private JTextField chat;
	private JTextField chatBox;
	private JButton[][] cards;
	private JButton[] helpers;
	private JLabel gameName;
	private JLabel[] players;
	private JTabbedPane tabs;
	
	
	//keep----
	JFrame frameMain;
	JTextArea ff;

	
	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Client() {
		welcomeScreen();
		//To test gui, comment out welcomeScreen() and run clientGui()
		//clientGui();
	}

	/*
	 * 
	 */
	void welcomeScreen() {
		// This try and catch is just a GUI theme. Looks cool
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		frameWelcome = new JFrame();
		frameWelcome.setSize(400, 300);
		frameWelcome.setLocation(400, 100);
		frameWelcome.setEnabled(true);

		panel = new JPanel();
		panel.setSize(frameWelcome.getWidth(), frameWelcome.getHeight());
		panel.setLayout(null);
		panel.setBackground(Color.black);

		JLabel heading = new JLabel();
		heading.setText("///Ask");
		heading.setForeground(Color.WHITE);
		heading.setFont(new Font("Serif", Font.BOLD, 45));
		heading.setBounds(100, 20, panel.getWidth(), 100);

		JLabel created = new JLabel();
		created.setText("Welcomes you!");
		created.setFont(new Font("Serif", Font.BOLD, 18));
		created.setForeground(Color.RED);
		created.setBounds(100, 55, 280, 100);

		JLabel loginName = new JLabel();
		loginName.setText("Name: ");
		loginName.setForeground(Color.gray);
		loginName.setFont(new Font("Serif", Font.BOLD, 18));
		loginName.setBounds(60, 160, 75, 25);

		JLabel IP = new JLabel();
		IP.setText("Address:");
		IP.setForeground(Color.gray);
		IP.setFont(new Font("Serif", Font.BOLD, 18));
		IP.setBounds(37, 190, 100, 25);

		loginTextfieldName = new JTextField();
		loginTextfieldName.setFont(new Font("Serif", Font.BOLD, 16));
		loginTextfieldName.setForeground(Color.RED);
		loginTextfieldName.setBounds(130, 160, 200, 25);

		loginTextfielIp = new JTextField();
		loginTextfielIp.setText("localhost");
		loginTextfielIp.setFont(new Font("Serif", Font.BOLD, 16));
		loginTextfielIp.setForeground(Color.RED);
		loginTextfielIp.setBounds(130, 190, 200, 25);

		loginButton = new JButton();
		loginButton.setText("Login");
		loginButton.setFont(new Font("Serif", Font.PLAIN, 20));
		loginButton.setBounds(130, 220, 200, 30);
		loginButton.setForeground(Color.BLACK);
		loginButton.addActionListener(this);

		panel.add(heading);
		panel.add(created);
		panel.add(loginName);
		panel.add(IP);
		panel.add(loginTextfieldName);
		panel.add(loginTextfielIp);
		panel.add(loginButton);

		frameWelcome.add(panel);
		frameWelcome.setResizable(false);
		frameWelcome.setVisible(true);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	/*
	 * 
	 */
	void clientGui() {
		games=new String[15];
        for(int k=0;k<15;k++){
            games[k]="empty";
        }
        cards=new JButton[15][10];
        bigframe=new JPanel[40];
        chat=new JTextField();
        chatBox=new JTextField();
        tabs=new JTabbedPane();
        helpers=new JButton[6];
        JFrame guiFrame = new JFrame();
        
        guiFrame.setSize(1000,390);
        guiFrame.setLayout(null);
        //make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Example GUI");
        //Add helper buttons
        helpers[0]=new JButton("Send");//To send chat message
        helpers[0].setSize(93, 20);
        helpers[0].setName("SendChat");
        helpers[0].setLocation(710, 330);
        helpers[0].addActionListener(this);
        helpers[1]=new JButton("Log Off");///Logoff
        helpers[1].setSize(160, 20);
        helpers[1].setLocation(820, 330);
        helpers[1].setName("Logoff");
        helpers[1].addActionListener(this);
        helpers[2]=new JButton("List Players");//List all players?
        helpers[2].setSize(160, 20);
        helpers[2].setLocation(820, 310);
        helpers[2].setName("ListPlayers");
        helpers[2].addActionListener(this);
        helpers[3]=new JButton("List Games");//Get a list of all available games
        helpers[3].setSize(160, 20);
        helpers[3].setLocation(820, 290);
        helpers[3].setName("ListGames");
        helpers[3].addActionListener(this);
        helpers[4]=new JButton("Other Button");//other button
        helpers[4].setSize(160, 20);
        helpers[4].setLocation(820, 270);
        helpers[4].setName("XX");
        helpers[4].addActionListener(this);
        helpers[5]=new JButton("Other Button");//other button
        helpers[5].setSize(160, 20);
        helpers[5].setLocation(820, 250);
        helpers[5].setName("YY");
        helpers[5].addActionListener(this);
        
        tabs.setLocation(3, 8);
        tabs.setSize(800, 310); 
        chatBox.setSize(160, 220);
        chatBox.setLocation(820, 30);
        chat.setSize(700, 20);
        chat.setLocation(3, 330);
        //Add panels to main frame
        guiFrame.add(chat);
        guiFrame.add(chatBox); 
        guiFrame.add(tabs);
        //Add buttons to main frame
        guiFrame.add(helpers[0]);
        guiFrame.add(helpers[1]);
        guiFrame.add(helpers[2]);
        guiFrame.add(helpers[3]);
        guiFrame.add(helpers[4]);
        guiFrame.add(helpers[5]);
        //center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
        
        
        //Just for testing--------------------------------------------------------------------
        String[] test1={"9s","th","2c","4s","6c","7c","9h","qd","ks","as"};
        String[] test2={"7s","3h","qc","8s","2c","3c","kh"};
        String[] test3={"qs","th"};
        String[] names1={"Paul","Luke","Michael","Kristo","James","Gerrit","Jukkie"};
        String[] names2={"Michael","James","Kristo","Gerrit","Jukkie","Luke"};
        String[] names3={"Michael","James","Kristo"};
        newGameGui("Friendly",names1);
        newGameGui("Fun",names2);
        newGameGui("Hello",names1);
        newGameGui("G3",names3);
        updateGame("Friendly",test3,null);
        updateGame("Fun",test1,null);
        updateGame("G3",test2,null);
        //------------------------------------------------------------------------------------
    
	}
	public void newGameGui(String gName,String[] pNames){
        //Set game number, game name
        int gameNumber=0;
        for(int i=0;i<15;i++){
            if(games[i].equals("empty")){
                games[i]=gName;
                gameNumber=i;
                break;
            }
            //add in for i=14, then playing more than 15 games
        }
        //can add check here to make sure gameNumber has been initialised properly
        
        //Initialise
        bigframe[gameNumber]=new JPanel();
        bigframe[gameNumber].setSize(800, 310);
        bigframe[gameNumber].setLayout(null);
        bigframe[gameNumber].setName(gName);
        cards[gameNumber]=new JButton[10];
        players=new JLabel[7];
        //Add game name label
        gameName=new JLabel(gName);
        gameName.setFont(new java.awt.Font("Tahoma", 0, 24));
        gameName.setBounds(340, 2, 180, 40);
        bigframe[gameNumber].add(gameName);
        //Add Player Names
        for(int k=0;k<pNames.length;k++){
            players[k]=new JLabel(pNames[k]);
            players[k].setBounds(10,(k*20), 50, 15);
            bigframe[gameNumber].add(players[k]);
        }
        //Add 10 facedown cards
        for(int k=0;k<10;k++){
        	Icon icon=new ImageIcon("src/cards/back.gif");
            cards[gameNumber][k]=new JButton(icon);
            cards[gameNumber][k].setBounds((80*k), 170, 75, 100);
            cards[gameNumber][k].addActionListener(this);
            cards[gameNumber][k].setEnabled(false);
            bigframe[gameNumber].add(cards[gameNumber][k]);
        }
        //Add new game panel
        tabs.addTab(gName,bigframe[gameNumber]);
    }
    public void updateGame(String gName,String[] pCards,int[] pScores){
        //Find gameNumber
        int gameNumber=0;
        for(int i=0;i<15;i++){
            if(games[i].equals(gName)){
                gameNumber=i;
                break;
            }
        }
        
        for(int k=0;k<pCards.length;k++){
            cards[gameNumber][k].setEnabled(true);
            Icon icon=new ImageIcon("src/cards/"+pCards[k]+".gif");
            cards[gameNumber][k].setIcon(icon);
            cards[gameNumber][k].setName(pCards[k]+""+bigframe[gameNumber].getName());
            cards[gameNumber][k].setBounds((80*k), 170, 75, 100);
            bigframe[gameNumber].add(cards[gameNumber][k]);
        }
        for(int k=9;k>pCards.length-1;k--){
            cards[gameNumber][k].setVisible(false);
        }
        //tabs.updateUI();
    }
	
	public void quit()
	{
		try {
			client.close();
			frameMain.dispose();
//			threadMessage.interrupt();
			System.exit(0);
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
//		threadMessage.destroy()
	}

	public void connectToServer(String userName, String servername) {
		try {
			/*
			 * TODO Change the servername to IP Address to test over network
			 */

			servername = "localhost";
			client = new Socket(servername, port);

			InputStreamReader reader = new InputStreamReader(
					client.getInputStream());

			brufferReader = new BufferedReader(reader);
			pw = new PrintWriter(client.getOutputStream(), true);

			// Checking to see if server is ready
			String serverMsg = brufferReader.readLine();

			if (serverMsg.compareTo("RD") == 0) {
				// Server is ready
				StringBuilder sb = new StringBuilder();
				sb.append("LI");
				sb.append(userName);
				sb.append(":password");

				// Send user name and password to server
				pw.println(sb.toString());

				// Check if user name is unique
				if (brufferReader.readLine().compareTo("LK") == 0) {
					clientGui();
					//ff.append("Client and server ready...");
					threadMessage = new MessagesThread();
					threadMessage.start();
				} else {
					new Client();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Server is not ready");
				quit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class MessagesThread extends Thread {
		public void run() {
			while (true) {
				try {
					String line = brufferReader.readLine();
					line = line.replace("\n", "");
					ff.append("Server--> " + line + "\n");
				} catch (Exception e) {
					e.printStackTrace();
					dispose();
					System.exit(0);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == loginButton) {
			usernName = loginTextfieldName.getText();
			try {
				frameWelcome.dispose();
				connectToServer(usernName, "localhost");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{//will try look for a better way to do this, but this works
			String temp=evt.toString().substring(evt.toString().indexOf(" on ")+4);
	        //Check if temp equals any special cases, i.e. logoff, get list of games, if not then a card has been clicked
	        if(temp.equals("SendChat")){
	            //Send typed chat message
	            System.out.println("SendChat");
	        }else if(temp.equals("Logoff")){
	            //Logoff
	            System.out.println("Logoff");
	        }else if(temp.equals("ListGames")){
	            //Send List of Games
	            System.out.println("ListGames");
	        }else if(temp.equals("ListPlayers")){
	            //Send List of Players
	            System.out.println("ListPlayers");
	        }else if(temp.equals("YY")){
	            //Todo
	            System.out.println("YY");
	        }else if(temp.equals("XX")){
	            //Todo
	            System.out.println("XX");
	        }else{
	            //Else a card has been clicked
	            //Card that has been clicked and the game name        
	            String card=temp.substring(0, 2);
	            String game=temp.substring(2);
	            System.out.print(card+" ");
	            System.out.println(game);  
	        }
		}
	}
}
