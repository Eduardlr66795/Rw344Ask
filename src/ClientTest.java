import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ClientTest {
	serverForClientTest server;
	Socket clientSocket;
	Client client;
	Thread clientThread;
	Thread serverThread;
	
	@Before
	public void setUp() throws Exception {
		server = new serverForClientTest();
		serverThread = new Thread(server);
		serverThread.start();
		client = new Client();
		clientThread = new Thread(client);
		clientThread.start();
	}
	
	@After
	public void tearDown() throws Exception {
		server.running = false;
		server.end();
		client.quitTheClient();
	}
	
	@Test
	 public void testConnection() throws InterruptedException, IOException{
		client.text_loginTextfieldName.setText("client");
		client.connectToServer();
		server.clientoutputs.get(0).writeObject("RD;");
		server.clientoutputs.get(0).flush();
		assertTrue("Could not connect to client",client.getServerReady());
	}
	
	@Test
	 public void testNotConnection() throws InterruptedException, IOException{
		client.text_loginTextfieldName.setText("client");
		client.connectToServer();
		server.clientoutputs.get(0).writeObject("");
		server.clientoutputs.get(0).flush();
		assertTrue("Could not connect to client",!client.getServerReady());
		
	}
	
	
}
