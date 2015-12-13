package ci_events;

import static ci_events.UserDatabase.DB_URL;
import ci_events.User_Classes.User;
import java.util.ArrayList;
import java.sql.Array.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EventsDatabase {

        // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/venusclassic";
    
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    /**
     * I'm thinking this class would likely be best implemented as a static
     * class. If so, we would need to modify the methods as needed.
     */
    public EventsDatabase() {

    }

    /**
     * Attempts to find an event by searching for its unique name. If the specified
     * event cannot be found, this method should return null
     *
     * @param title The name of the event to be returned
     * @return The specified event returned in an Event object, or null if not found
     */
    public Event findEventByName(String title) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement =conn.prepareStatement("SELECT * FROM Event WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("idEvent");
                String eventName = rs.getString("title");
                String description = rs.getString("description");
                String creator = rs.getString("creator");
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + eventName);
                System.out.println(", Description: " + description);
                System.out.println(", Creator: " + creator);
                return null;
            }
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Event not found");
        return null;
    }
    
    /**
     * Attempts to find a list of all events that the input user is a member of
     * and returns them in an ArrayList.
     * 
     * @param userName The email of the user to be found in all of the returned events
     * @return An ArrayList that contains all of the Events that the user is a member of
     */
    public ArrayList<Event> findEventsByUser(String userName) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement =conn.prepareStatement("SELECT * FROM Event inner join event_user on Event.idEvent=event_user.EventIdEvent inner join User"
                    + " on event_user.UserIdUser=user.IdUser WHERE user.name = ?");
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("idEvent");
                String eventName = rs.getString("title");
                String description = rs.getString("description");
                String creator = rs.getString("creator");
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + eventName);
                System.out.println(", Description: " + description);
                System.out.println(", Creator: " + creator);             
            }
            
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return null;
    }
    
    /**
     * This should return a list of all events currently in the database. This will
     * be used to periodically update the weather for each event.
     * 
     * @return An ArrayList of all events in the database
     */
    public ArrayList<Event> allEvents() {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement =conn.prepareStatement("SELECT * FROM Event");
            ResultSet rs = statement.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("idEvent");
                String eventName = rs.getString("title");
                String description = rs.getString("description");
                String creator = rs.getString("creator");
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + eventName);
                System.out.println(", Description: " + description);
                System.out.println(", Creator: " + creator);
            }
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return null;
    }
    
    /**
     * Updates the specified Event in the database with the new information passed
     * in through the event object. Returns a false if the event cannot be found
     * in the database
     * 
     * @param event The updated event with the new information
     * @return false if the event could not be found or updated, true otherwise
     */
    public boolean updateEvent(Event event, Event_User eu) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement1;
        PreparedStatement statement2;
        PreparedStatement statement3;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement1 =conn.prepareStatement("SELECT * FROM Event WHERE title = ?");
            statement1.setString(1, event.getTitle());
            ResultSet rs = statement1.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){              
            System.out.println("Event existent");
            String updateEvent = "UPDATE Event"
				+ "(IdEvent, Title, Description, Creator, Date) VALUES"
				+ "(?,?,?,?,?)";
            statement2 =conn.prepareStatement(updateEvent);
            statement2.setInt(1, event.getIdEvent());
	    statement2.setString(2, event.getTitle());
	    statement2.setString(3, event.getDescription());
            statement2.setString(4, event.getCreator());
            statement2.setString(5, event.getDate());
            statement2.executeUpdate();
            
            String updateGuest = "UPDATE Guest"
				+ "(IdGuest, guestEmail) VALUES"
				+ "(?,?)";
            statement3 =conn.prepareStatement(updateGuest);
            statement3.setInt(1, eu.getIdGuest());
	    statement3.setString(2, eu.getGuestEmail());


            System.out.println("Record updated into Event table!");
            statement3.close();
            statement2.close();
            return true;
            }
            //STEP 6: Clean-up environment
            rs.close();
            statement1.close();
            
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Event not registered");
        return false;
    }
    
    
    
    

    /**
     * Attempts to store the passed in event in the Events table of the database.
     * The Event should likely have a unique name, so if another event in the table
     * already has the same name, return false and do not add the new event.
     * 
     * @param event The new event to be added to the database
     * @return false if the event could not be added. True otherwise
     */
    public boolean storeEvent(Event event, Event_User eu) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement1;
        PreparedStatement statement2;
        PreparedStatement statement3;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement1 =conn.prepareStatement("SELECT * FROM Event WHERE title = ?");
            statement1.setString(1, event.getTitle());
            ResultSet rs = statement1.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){              
                System.out.println("Event existent");
                return true;
            }
            String insertTableSQL = "INSERT INTO Event"
				+ "(IdEvent, Title, Description, Creator, Date) VALUES"
				+ "(?,?,?,?,?)";
            statement2 =conn.prepareStatement(insertTableSQL);
            statement2.setInt(1, event.getIdEvent());
	    statement2.setString(2, event.getTitle());
	    statement2.setString(3, event.getDescription());
            statement2.setString(4, event.getCreator());
            statement2.setString(5, event.getDate());
            statement2.executeUpdate();
            
            String updateGuest = "INSERT into Guest"
				+ "(IdGuest, guestEmail) VALUES"
				+ "(?,?)";
            statement3 =conn.prepareStatement(updateGuest);
            statement3.setInt(1, eu.getIdGuest());
	    statement3.setString(2, eu.getGuestEmail());
            
            System.out.println("Record is inserted into Event table!");
            statement3.close();
            statement2.close();
            
            //STEP 6: Clean-up environment
            rs.close();
            statement1.close();
            statement2.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return false;
    }
    /**
     * Used to remove an event from the table after its date has passed. 
     *
     * @param event The event to be removed from the table
     */
    public void deleteEvent(Event event) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement1;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement1 =conn.prepareStatement("DELETE Event WHERE IdEvent = ?");
            statement1.setInt(1, event.getIdEvent());
            ResultSet rs = statement1.executeQuery();
            
            // execute insert SQL stetement
            statement1.executeUpdate();

            System.out.println("Event deleted! ");
            
            //STEP 6: Clean-up environment
            rs.close();
            statement1.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }
    
    public void deleteEvent(int eventID) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement1;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement1 =conn.prepareStatement("DELETE FROM Event WHERE IdEvent = ?");
            statement1.setInt(1, eventID);
            //ResultSet rs = statement1.executeQuery();
            
            // execute insert SQL stetement
            statement1.executeUpdate();

            System.out.println("Event deleted! ");
            
            //STEP 6: Clean-up environment
            //rs.close();
            statement1.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }

}
