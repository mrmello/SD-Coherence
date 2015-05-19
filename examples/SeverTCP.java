import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerTCP {

	public static void main(String[] args){
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(2006);
			Socket s = serverSocket.accept();
			OutputStream oip = s.getOutputStream();	
			ObjectOutputStream write = new ObjectOutputStream(oip);
			write.writeObject("Message from server");
			InputStream ip = s.getInputStream();
			ObjectInputStream read = new ObjectInputStream(ip);
			String message;
			try {
				message = (String)read.readObject();
				System.out.println(message);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
