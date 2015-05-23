package assignment3;
import java.sql.*;
import java.io.*;


public class RideSharing {

	private DBConnection con;
    private Statement st;
    private ResultSet rs;
	
    public RideSharing() {
    	 con = new DBConnection();
    	 
    }

    public static void main(String[] args) {
        RideSharing app = new RideSharing();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line = null;

            while ((line = in.readLine()) != null) {
                String[] argv = line.split(" ");
                
                if (argv[0].equalsIgnoreCase("request")) {
                	app.requestExecute(argv);
                }else if (argv[0].equalsIgnoreCase("rate")) {
                	app.rateExecute(argv);
                }else if (argv[0].equalsIgnoreCase("search")) {
                	app.searchExecute(argv);
                }else if (argv[0].equalsIgnoreCase("print")) {
                	 app.printExecute(argv);
                }else if (argv[0].equalsIgnoreCase("drivers")) {
                	app.driversExecute(argv);
                }else if (argv[0].equalsIgnoreCase("accept")) {
                	app.acceptExecute(argv);
                }else if (argv[0].equalsIgnoreCase("pickup")) {
                	app.pickupExecute(argv);
                }else if (argv[0].equalsIgnoreCase("dropoff")) {
                	app.dropoffExecute(argv);
                }else if (argv[0].equalsIgnoreCase("cancel")) {
                	app.cancelExecute(argv);
                } else if (argv[0].equalsIgnoreCase("EXIT")) {
                    app.con.disconnect();
                    break;
                } else {
                    System.out.println("Unknown command");
                }
            }
            
            in.close();

        } catch (Exception e) {
            System.err.println("Exception: " + e.getStackTrace());
        }
    }
    public void requestExecute(String [] arg){
    	 try {
    		 con.connect(); 
    		 con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    		 String query = "select status from rides where pid = "+arg[1]+";";
    		 boolean hasRide = false;
    		 st = con.getConnection().createStatement();
    		 rs = st.executeQuery(query);
    		 while (rs.next()) {
    			 String status = rs.getString("status");
    			 
    			 if(status.equalsIgnoreCase("requeted") ||
    				status.equalsIgnoreCase("accepted") ||
    				status.equalsIgnoreCase("en route")){
    				 hasRide = true;
    			 }
    		 }
    		 if(!hasRide){
    			 query = "insert into rides (pid, num_riders, pickup_lat, pickup_lon, dropoff_lat, dropoff_lon,status) values (" + arg[1]+ ", " +arg[2] + ", " + arg[3]+ ", " +arg[4] + ", "+ arg[5]+ ", " +arg[6] + ", 'requested');";
    			 st = con.getConnection().createStatement();
    			 rs = st.executeQuery(query);
    		 }else{
    			 System.out.println("Passenger already has another ride");
    		 }
             con.disconnect(); 
         } catch (Exception e) {
         	System.out.println(e);
         }

    }
    public void searchExecute(String[] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    		String query = "select name, plate, phone from ("
    				+ "SELECT d.name,d.plate,d.phone,(3958.75 * "
    				+ "acos(cast((sin(r.pickup_lat/57.2958) * sin(l.lat/57.2958) +"
    				+ " cos(r.pickup_lat/57.2958) * cos(l.lat/57.2958) * "
    				+ "cos(l.lon/57.2958 - r.pickup_lon/57.2958))as integer))) AS dist "
    				+ "FROM drivers d natural join locations l, rides r "
    				+ "WHERE r.rid = "+arg[1]+" ORDER BY dist LIMIT 5 ) as distance;";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            while(rs.next()) {
                 String name = rs.getString("name");
                 String plate = rs.getString("plate");
                 String phone = rs.getString("phone");
                 
                 System.out.println(name + "\t" + plate + "\t" + phone);
             }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }    	
    }
    
    public void rateExecute(String[] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    		String query = "select status from rides where rid = "+arg[1]+";";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
            	String status = rs.getString("status");
            	if(status.equalsIgnoreCase("completed")){
            		query = "update ratings set rating = '" + arg[2]+ "' where rid = " +arg[1] + ";";
            		st = con.getConnection().createStatement();
            		rs = st.executeQuery(query);
            	}
            }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }    	
    }
    
    public void printExecute(String[] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    		String query = "select r.status, r.fare, p.name as pname, d.name, d.plate "
    				+ "from passengers p natural join rides r join drivers d "
    				+ "on r.did = d.did where rid = "+arg[1]+";";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            if(rs.next()) {
             	 String status = rs.getString("status");
                 String fare = rs.getString("fare");
                 String pname = rs.getString("pname");
                 String dname = rs.getString("name");
                 String plate = rs.getString("plate");
                 
                 System.out.println(status + "\t" + fare + "\t" + pname +
                		 "\t" + dname + "\t" + plate);
             }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }    	
    }
    
    public void driversExecute(String [] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    		String query = "select d.name, d.plate, d.phone, AVG(rt.rating) as average "
    				+ "from ratings rt natural join rides r natural join drivers d "
    				+ "group by d.name, d.plate, d.phone "
    				+ "order by average DESC;";

    		st = con.getConnection().createStatement();
    		rs = st.executeQuery(query);
    		while (rs.next()) {
    			String name = rs.getString("name");
    			String plate = rs.getString("plate");
    			String phone = rs.getString("phone");
    			String avg = rs.getString("average");

    			System.out.println(name + "\t" + plate + "\t" + phone + "\t" + avg);
    		}
            con.disconnect(); 
    	} catch (Exception e) {
    		System.out.println(e);
    	}

    }
    
    public void acceptExecute(String [] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    		String query = "select status from rides where rid = "+arg[1]+";";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
            	String status = rs.getString("status");
            	if(status.equalsIgnoreCase("requested")){
            		query = "update rides set status = 'accepted', did = "+ arg[2]+ " where rid = "+arg[1]+";";
            		st = con.getConnection().createStatement();
            		rs = st.executeQuery(query);
            	}else{
            		System.out.println("Only a 'requested' ride can be accepted");
            	}
            }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }

   }
   public void pickupExecute(String [] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    		String query = "select status from rides where rid = "+arg[1]+";";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
            	String status = rs.getString("status");
            	if(status.equalsIgnoreCase("accepted")){
        
            		query = "update rides set pickup_time = '"+arg[2]+"', "
            				+ "status = 'en route' where rid = "+arg[1]+";";
            		st = con.getConnection().createStatement();
            		rs = st.executeQuery(query);
            	}else{
            		System.out.println("Only an 'accepted' ride can be picked up");
            	}
            }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }

   }

   public void dropoffExecute(String [] arg){
   	try {
   		con.connect(); 
   		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
   		String query = "select status from rides where rid = "+arg[1]+";";
           st = con.getConnection().createStatement();
           rs = st.executeQuery(query);
           if (rs.next()) {
           	String status = rs.getString("status");
           	if(status.equalsIgnoreCase("en route")){
           		
           		query = "select (pickup_time - '"+arg[2]+"')as duration, pickup_lat, pickup_lon, dropoff_lat, dropoff_lon"
           				+ " from rides where rid = "+arg[1]+";";
                st = con.getConnection().createStatement();
                rs = st.executeQuery(query);
                
                if (rs.next()) {
                   	
                	String duration = rs.getString("duration");
                   	float pk_lat = rs.getFloat("pickup_lat");
                   	float pk_lon = rs.getFloat("pickup_lon");
                   	float dp_lat = rs.getFloat("dropoff_lat");
                   	float dp_lon = rs.getFloat("dropoff_lon");
                   	
                   	String[] times = duration.split(":");
                   	int hours = Integer.parseInt(times[0]);
                    int minutes = Integer.parseInt(times[1]);
                    int seconds = Integer.parseInt(times[2]);
                    int dur = hours * 60 + minutes;
                    if(seconds > 30){
                    	dur =+ 1;
                    }
                   	double fare = calculateFare(pk_lat, pk_lon, dp_lat, dp_lon, dur);
                   	
                   	query = "update rides set dropoff_time = '"+arg[2]+"', "
               				+ "status = 'completed', fare = "+fare+" where rid = "+arg[1]+";";
               		st = con.getConnection().createStatement();
               		rs = st.executeQuery(query);
                }
           		
           	}else{
           		System.out.println("Only a 'en route' ride can be completed");
           	}
           }
           con.disconnect(); 
       } catch (Exception e) {
       	System.out.println(e);
       }

  }


    public void cancelExecute(String [] arg){
    	try {
    		con.connect(); 
    		con.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    		String query = "select status from rides where rid = "+arg[1]+";";
            st = con.getConnection().createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
            	String status = rs.getString("status");
            	if(status.equalsIgnoreCase("requested")){
            		query = "update rides set status = 'canceled' where rid = "+arg[1]+";";
            		st = con.getConnection().createStatement();
            		rs = st.executeQuery(query);
            	}else if(status.equalsIgnoreCase("accepted")){
            		query = "update rides set status = 'canceled', fare = 5.00 where rid = "+arg[1]+";";
            		st = con.getConnection().createStatement();
            		rs = st.executeQuery(query);
            	}else{
            		System.out.println("Only a 'requested' or an 'accepted' ride can be canceled");
            	}
            }
            con.disconnect(); 
        } catch (Exception e) {
        	System.out.println(e);
        }

   }
    
    public double calculateFare(float latA, float longA, float latB, float longB, int dur) {

        double theDistance = (Math.sin(Math.toRadians(latA)) *
                Math.sin(Math.toRadians(latB)) +
                Math.cos(Math.toRadians(latA)) *
                Math.cos(Math.toRadians(latB)) *
                Math.cos(Math.toRadians(longA - longB)));

        double mile = new Double((Math.toDegrees(Math.acos(theDistance))) * 69.09).intValue();
        double kilometer = mile * 1.609344;
        double fare = 2.75 + (kilometer * 0.90) + (dur * 0.30);
        
        return fare;
    }
   
}
