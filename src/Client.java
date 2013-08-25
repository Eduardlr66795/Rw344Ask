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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	}

	/*
	 * TODO - KRISTO MAKE A COOL WELCOME SCREEN
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
	 * TODO - KRISTO GUI
	 */
	void clientGui() {
		frameMain = new JFrame();
		frameMain.setSize(400, 400);

		ff = new JTextArea();
		ff.setSize(400, 400);
		ff.setEditable(false);
		
		frameMain.add(ff);
		frameMain.setVisible(true);
		frameMain.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		frameMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});
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
					ff.append("Client and server ready...");
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
		}
	}
}
