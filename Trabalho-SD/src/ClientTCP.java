import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientTCP {

	public static void main(String[] args){
		Socket s;
		Scanner reader = new Scanner(System.in);
		try {
			int option = 1;
			s = new Socket("localhost", 1972);
			while(option != 5){
				System.out.println(
				"1. Select | 2. Insert | 3.Update | 4.Delete | 5.Quit");
				//Lê input from user
				option = reader.nextInt();
				OutputStream op = s.getOutputStream();	
				ObjectOutputStream write = new ObjectOutputStream(op);
				write.writeObject(option);
				try {
					
					//encerra a comunicação 
					if(option == 5) {
						System.out.println("Bye!");
						break;
					}
					InputStream ip = s.getInputStream();
					ObjectInputStream read = new ObjectInputStream(ip);
					String message;
					message = (String)read.readObject();
					System.out.println(message);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			reader.close();
			s.close();
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
