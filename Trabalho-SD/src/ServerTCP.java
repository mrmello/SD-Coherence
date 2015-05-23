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
			serverSocket = new ServerSocket(1972);
			System.out.println("Waiting...");
			Socket s = serverSocket.accept();
			System.out.println("Connected!");
			int option = 1;
			String message = "";
			while(true){
				InputStream ip = s.getInputStream();
				ObjectInputStream read = new ObjectInputStream(ip);
				try {
					option = (Integer)read.readObject();
					switch(option){
					case 1:
						message = "Select";
					break;
					case 2:
						message = "Insert";
					break;
					case 3:
						message = "Update";
					break;
					case 4:
						message = "Delete";
					break;
					default:
						message = "Please verify the option!";
				}
					//encerra a comunicação
					if(option == 5) 
						break;
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println(message);
				OutputStream oip = s.getOutputStream();	
				ObjectOutputStream write = new ObjectOutputStream(oip);
				write.writeObject(message);
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
