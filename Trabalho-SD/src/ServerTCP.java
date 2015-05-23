import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ServerTCP {
	
	private DBConnection con;
    private Statement st;
    private ResultSet rs;
    
    public ServerTCP() {
      	 con = new DBConnection();
    }
    
	public static void main(String[] args){
		
		ServerTCP server = new ServerTCP();
		
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
						message = server.selectExecute();
					break;
					case 2:
						message = server.insertExecute();
					break;
					case 3:
						message = server.updateExecute();
					break;
					case 4:
						message = server.deleteExecute();
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
	
	private String selectExecute(){
		String result = "";
		try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    		String query = "select * from students;";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            while(rs.next()) {
            	String id = rs.getString("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String age = rs.getString("age");
                                
                result += id + "\t" + firstName + "\t" + lastName + "\t" + age + "\n";
            }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }    
		return result;
	}
	
	private String insertExecute(){
		return "Call of Insert function";
	}
	
	private String updateExecute(){
		return "Call of Update function";
	}
	
	private String deleteExecute(){
		return "Call of Delete function";
	}

}
