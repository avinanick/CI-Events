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
    // static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/venusclassic";
    private static final String DB_URL = "postgres://prfshwatmndsfw:F-gBA0Cm5ruSU15m_qtunZRIEo@ec2-54-83-59-203.compute-1.amazonaws.com:5432/d22tq5p9r1orpq";
    
    //  Database credentials
    private static final String USER = "prfshwatmndsfw";                //old "root"
    private static final String PASS = "F-gBA0Cm5ruSU15m_qtunZRIEo";    //old "root"
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
    public static Event findEventByName(String title) {
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
                String date = rs.getString("date");
                String description = rs.getString("description");
                String creator = rs.getString("creator");
                String location = rs.getString("location");
                
                //Get the members for the event
                PreparedStatement getMembers = conn.prepareStatement("SELECT * FROM event_user WHERE idEvent = ?");
                getMembers.setString(1, id + "");
                ResultSet membersResult = getMembers.executeQuery();
                ArrayList<String> members = new ArrayList<>();
                while(membersResult.next()) {
                    members.add(rs.getString("email"));
                }
                
                //Put the data into an event class
                Event returnEvent = new Event(eventName, date, creator, location, id);
                returnEvent.setDescription(description);
                for(String currentUser : members) {
                    returnEvent.addMember(currentUser);
                }
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + eventName);
                System.out.println(", Description: " + description);
                System.out.println(", Creator: " + creator);
                return returnEvent;
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
    public static ArrayList<Event> findEventsByUser(String userName) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement;
        ArrayList<Event> returnEvents = new ArrayList<>();
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement =conn.prepareStatement("SELECT * FROM Event inner join event_user on Event.idEvent=event_user.idEvent "
                    + "WHERE event_user.email = ?");
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("idEvent");
                String eventName = rs.getString("title");
                String date = rs.getString("date");
                String description = rs.getString("description");
                String creator = rs.getString("creator");
                String location = rs.getString("location");
                
                //Get the members for the event
                PreparedStatement getMembers = conn.prepareStatement("SELECT * FROM event_user WHERE idEvent = ?");
                getMembers.setString(1, id + "");
                ResultSet membersResult = getMembers.executeQuery();
                ArrayList<String> members = new ArrayList<>();
                while(membersResult.next()) {
                    members.add(rs.getString("email"));
                }
                
                //Put the data into an event class
                Event returnEvent = new Event(eventName, date, creator, location, id);
                returnEvent.setDescription(description);
                for(String currentUser : members) {
                    returnEvent.addMember(currentUser);
                }
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + eventName);
                System.out.println(", Description: " + description);
                System.out.println(", Creator: " + creator);
                returnEvents.add(returnEvent);
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
        return returnEvents;
    }
    
    /**
     * This should return a list of all events currently in the database. This will
     * be used to periodically update the weather for each event.
     * 
     * @return An ArrayList of all events in the database
     */
    public static ArrayList<Event> allEvents() {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement;
        ArrayList<Event> allEvents = new ArrayList<>();
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
                String date = rs.getString("date");
                String description = rs.getString("description");
                String creator = rs.getString("creator");
                String location = rs.getString("location");
                
                //Get the members for the event
                PreparedStatement getMembers = conn.prepareStatement("SELECT * FROM event_user WHERE idEvent = ?");
                getMembers.setString(1, id + "");
                ResultSet membersResult = getMembers.executeQuery();
                ArrayList<String> members = new ArrayList<>();
                while(membersResult.next()) {
                    members.add(rs.getString("email"));
                }
                
                //Put the data into an event class
                Event returnEvent = new Event(eventName, date, creator, location, id);
                returnEvent.setDescription(description);
                for(String currentUser : members) {
                    returnEvent.addMember(currentUser);
                }
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + eventName);
                System.out.println(", Description: " + description);
                System.out.println(", Creator: " + creator);
                allEvents.add(returnEvent);
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
        return allEvents;
    }
    
    /**
     * Updates the specified Event in the database with the new information passed
     * in through the event object. Returns a false if the event cannot be found
     * in the database
     * 
     * @param event The updated event with the new information
     * @return false if the event could not be found or updated, true otherwise
     */
    public static boolean updateEvent(Event event, Event_User eu) {
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
				+ "(IdEvent, Title, Description, Creator, Date, Location) VALUES"
				+ "(?,?,?,?,?,?)";
                statement2 =conn.prepareStatement(updateEvent);
                statement2.setInt(1, event.getIdEvent());
                statement2.setString(2, event.getTitle());
                statement2.setString(3, event.getDescription());
                statement2.setString(4, event.getCreator());
                statement2.setString(5, event.getDate());
                statement2.setString(6, event.getLocation());
                statement2.executeUpdate();
            
                //Update the list of guests, currently nonfunctional
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
    public static boolean storeEvent(Event event, Event_User eu) {
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
				+ "(IdEvent, Title, Description, Creator, Date, Location) VALUES"
				+ "(?,?,?,?,?,?)";
            statement2 =conn.prepareStatement(insertTableSQL);
            statement2.setInt(1, event.getIdEvent());
	    statement2.setString(2, event.getTitle());
	    statement2.setString(3, event.getDescription());
            statement2.setString(4, event.getCreator());
            statement2.setString(5, event.getDate());
            statement2.setString(6, event.getLocation());
            statement2.executeUpdate();
            
            for(String currentEmail : event.getMembers()) {
                String updateGuest = "INSERT into event_user"
				+ "(IdEvent, Email) VALUES"
				+ "(?,?)";
                statement3 =conn.prepareStatement(updateGuest);
                statement3.setInt(1, event.getIdEvent());
                statement3.setString(2, currentEmail);
                statement3.executeUpdate();
            }
            
            System.out.println("Record is inserted into Event table!");
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
    public static void deleteEvent(Event event) {
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
            
            PreparedStatement statement2 = conn.prepareStatement("DELETE event_user WHERE idEvent = ?");
            statement2.setInt(1, event.getIdEvent());
            statement2.executeUpdate();

            System.out.println("Event deleted! ");
            
            //STEP 6: Clean-up environment
            rs.close();
            statement2.close();
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
    
    public static void deleteEvent(int eventID) {
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
            
            PreparedStatement statement2 = conn.prepareStatement("DELETE event_user WHERE idEvent = ?");
            statement2.setInt(1, eventID);
            statement2.executeUpdate();

            System.out.println("Event deleted! ");
            
            //STEP 6: Clean-up environment
            //rs.close();
            statement2.close();
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
