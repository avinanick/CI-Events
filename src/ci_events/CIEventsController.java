package ci_events;

import view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JTable;


/**
 *
 * @author Clifton
 */
public class CIEventsController {
    
    private CreateEventView     eventView;
    
    private Login               loginView;
    
    private MainWindow          mainView;
    
    private String              currentUsername;
    
    //Since many of our views are panels and need to be attached to Frames,
    //This variable is needed to control above.
    private JFrame              controllingJFrame;
    
    public boolean              editMode = false;
    
    
   public CIEventsController(CreateEventView eventView){
       this.eventView = eventView;
       
       this.eventView.addCreateEventListener(new CreateEventListener());
   }
   
   public void addMainView(MainWindow mainWin){
       mainView = mainWin;
   }
   
   public void addJFrame(JFrame frame){
       controllingJFrame = frame;
   }
   
   public void setCurrentUser(String username){
       currentUsername = username;
   }
   
   public String getCurrentUser(){
       return currentUsername;
   }
   
   // Modify this method to get an event using the EventsDatabase matching the selected row
   public void EditEvent()
   {
       JTable eventTable = mainView.getEventTable();
       int row = eventTable.getSelectedRow();

       String eventName = eventTable.getValueAt(row, 1).toString();
       String eventDescription = eventTable.getValueAt(row, 2).toString();
       String eventCreator = eventTable.getValueAt(row, 3).toString();
       String eventDate = eventTable.getValueAt(row, 4).toString();
       
       eventView.setName(eventName);
       eventView.setEventDescription(eventDescription);
       eventView.setEventDate(eventDate);
       //Need method for getting event Location
       
       //Save Event
       Event e = new Event();
       e.setTitle(eventName);
       e.setDescription(eventDescription);
       e.setCreator(eventCreator);
       e.setDate(eventDate);
       
       
        JFrame f = new JFrame("VENUS Classic - Event Listing");
        f.add(eventView);
        f.setSize(500,500);
        f.pack();        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
       
       //int eventID = Integer.parseInt( eventTable.getValueAt(row,col).toString() );
       eventView.setVisible(true);
       editMode = false;
   }
   
   
   /* This Listener is to be attached to the Create event view.
        When listening, if the  Create Event / Save button is clicked, this function
        will handle and save off an event to the event database.
   */
   class CreateEventListener implements ActionListener{
       
       //Finish button pressed.
       @Override
       public void actionPerformed(ActionEvent e){
            String name, description, location, date, attendees;
            name = description = location = date = attendees = "";

            
            try{
                name = eventView.getEventName();
                description = eventView.getEventDescription();
                location = eventView.getEventLocation();
                date = eventView.getEventDate();
                attendees = eventView.getEventAttendees();
                
                
                if(     name.length() == 0 || 
                        description.length() == 0 ||
                        location.length() == 0 ||
                        date.length() == 0){
                    IOException exception = new IOException(); 
                    throw( exception );
                }
                
                Event_User guest = new Event_User();
                guest.setGuestEmail(attendees);
                
                Event ev= new Event(name, date, currentUsername, location);
                ev.setDescription(description);
                
                //Loop through, break at each white space for emails.
                ArrayList memberList = new ArrayList();
                for (String word : attendees.split("\\s+")) {
                    memberList.add(word);
                }
                ev.addMembers(memberList);
                ev.updateWeather();
                System.out.println("Attempting event storage.");
                EventsDatabase.storeEvent(ev, guest);
                System.out.println("Post-event storage.");
                
                mainView.updateTable(EventsDatabase.findEventsByUser(currentUsername)); 
                eventView.setVisible(false);
                controllingJFrame.setVisible(false);
                
                //eventModel.storeEvent(ev.getTitle());
            }
            catch (IOException exc){
                System.out.println(exc);
                System.out.println("The following fields cannot be blank:");
                if ( name.length() == 0 ){ System.out.println("Event Name"); }
                if ( description.length() == 0 ){ System.out.println("Description"); }
                if ( location.length() == 0 ){ System.out.println("Location"); }
                if ( date.length() == 0 ){ System.out.println("Date"); }
            }
            
            
            
          
       }
   }
    
}
