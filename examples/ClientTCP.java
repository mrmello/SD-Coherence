import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientTCP {

	public static void main(String[] args){

		Socket s;
		try {
			s = new Socket("localhost", 1972);
			InputStream ip = s.getInputStream();
			ObjectInputStream read = new ObjectInputStream(ip);
			String message;
			try {
				message = (String)read.readObject();
				System.out.println(message);
				
				OutputStream op = s.getOutputStream();	
				ObjectOutputStream write = new ObjectOutputStream(op);
				write.writeObject("Message from client");
				System.out.println("connected to: "+ s.getLocalPort());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
