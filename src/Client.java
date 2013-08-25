import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Client extends JFrame implements ActionListener {

	/*
	 * Kristo and Kyle. Remember that when a we run a client the first go to a
	 * welcome screen
	 */

	// Global Variables
	private String usernName;
	private Socket client;
	private int port = 9119;
	
	//WelcomeScreen
	private JFrame frameWelcome;
	private JPanel panel;
	private JTextField loginTextfieldName ;
	private JTextField loginTextfielIp;
	private JButton loginButton;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Client frame = new Client();
		
	}

	public Client() {
		welcomeScreen();
	}

	void welcomeScreen() {
		//This try and catch is just a GUI theme. Looks cool
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
		
		frameWelcome= new JFrame();
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
					// What to do when someone presses the close button
					// Seems stupid i know but ask me and i will explain it.
					System.exit(0);	
			}
		});
		
		
	}

	void clientGui() {

	}

	public void connectToServer(String userName, String servername)
			throws Exception {
		try {
			/*
			 * TODOChange the servername to IP Address to test over network
			 */

			servername = "localhost";
			client = new Socket(servername, port);
		} catch (Exception ex) {

		}
	}

	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == loginButton)
		{
			String name = loginTextfieldName.getText();
			//Check if the name is unique, there will be a method in server called usernameUnique if the name is unique it will return true, else false
			//The if(1 == 1) is the check btw
			
			if( 1 == 1) {
				this.usernName = name;
				// Now that we know that the user name is unique we need options of the different games.... We must still decide where to go from here on.
				frameWelcome.dispose();
				
			}
		}
	}
}
























