package ci_events;

import ci_events.User_Classes.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDatabase {
    
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
    public  UserDatabase() {

    }
    
    /**
     * Returns a specific user from the database by searching for their email
     * This method should find the specified user by email, then use the information
     * to create a User object to return to the caller. Returns null if the user
     * cannot be found
     *
     * @param email The email of the user to be found
     * @return A User object containing the user data, or null if no user is found
     */
    public User findUser(String email) {
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
            statement =conn.prepareStatement("SELECT idUser, name, email, securityLevel FROM User WHERE  email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("idUser");
                int securityLevel = rs.getInt("securityLevel");
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                
                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Security Level: " + securityLevel);
                System.out.print(", Name: " + name);
                System.out.println(", Email: " + userEmail);
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
        System.out.println("User not found");
        return null;
    }
    
    /**
     * Stores a user in the database if possible. The email for each user should
     * be unique, so do not insert if there is already a user with the same email
     *
     * @param email The user to be stored in the User table in the database
     * @return boolean false if the user could not be added, true otherwise
     */
    public boolean storeUser(String email) {
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
            statement =conn.prepareStatement("SELECT idUser, name, email, securityLevel FROM User WHERE  email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                System.out.println("User existent");
                return true;
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
        System.out.println("Email not registered");
        return false;
    }
    
}
