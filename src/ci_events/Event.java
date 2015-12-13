package ci_events;
import ci_events.User_Classes.User;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class Event {
    
    private String date;
    private int idEvent;
    private String title;
    private String description;
    private String creator;
    private ArrayList<String> members;
    private String location;
    private ArrayList<String> tags;
    private double [] weeklyWeather;

    private static final int WEEK = 7;

    private int numOfEvents;
    
    // Geocoding will likely be handled in the events class when given the address, so I will include the key here
    // For information about using the geocoding key, see https://developers.google.com/maps/documentation/geocoding/intro
    private static final String GEO_KEY = "AIzaSyBkfyhnqFzGUIdkfpuw_3zVEAFo1eenIoU";

    /**
     * Right now the constructor only takes the title and creator as parameters, as 
     * they are the only permanent parts of an Event. The other fields can be populated
     * by their respective set methods.
     * @param idEvent
     * @param title
     * @param creator 
     * @param date
     */
    public Event(String title, String date, String creator, String location) {
        this.date=date;
        this.idEvent= idEvent;
        this.creator = creator;
        this.location = location;
        this.members = new ArrayList<>();
        this.title = title;
        this.description = new String();
        this.tags = new ArrayList<>();
    }
    
    public Event(){
        //Empty constructor for overload.
        this.date="NotSet";
        this.idEvent= -1;
        this.creator = "NotSet";
        this.location = "NotSet";
        this.members = new ArrayList<>();
        this.title = "NotSet";
        this.description = "NotSet";
        this.tags = new ArrayList<>();
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String eventTitle) {
        title = eventTitle;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public String getDate() {
        return date;
    }
    
    public void setDate(String eventDate) {
        date = eventDate;
    }
   
    /**
     * This will set the current list of tags to whatever is listed as parameters
     * To do so, it first clears out the current list of tags, then adds everything in 
     * the list passed in
     * 
     * @param tags 
     */
    public void setTags(String... tags) {
        this.tags.clear();
        for(int i = 0; i < tags.length; i++) {
            this.tags.add(tags[i]);
        }
    }
    
    public ArrayList<String> getMembers() {
        return this.members;
    }
    
    public void addMember(String member){
        this.members.add(member);
    }
    
    public String getCreator() {
        return this.creator;
    }
    
     public void setCreator(String creatorName) {
        this.creator = creatorName;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public ArrayList<String> getTags() {
        return this.tags;
    }

    public Date getEventDate(String date)
    {   
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date eventDate = null;
        
        try {
            eventDate = dateFormat.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        
        return eventDate; 
    }

    /**
     * This takes a list of email addresses and adds them to the current list of members
     * make sure that each email address in the members list is unique, we don't 
     * need the same member entered twice
     * 
     * @param newMembers The list of members to be added
     */
    public void addMembers(String... newMembers) {
        for(int i = 0; i < newMembers.length; i++) {
            if(!this.members.contains(newMembers[i])) {
                this.members.add(newMembers[i]);
            }
        }
    }
   
    public void addMembers(ArrayList<String> newMembers) {
        for(int i = 0; i < newMembers.size(); i++) {
            if(!this.members.equals(newMembers.get(i))) {
                this.members.add(newMembers.get(i));
            }
        }
    }
    
    /**
     * This method takes a list of members by their email addresses and removes
     * any that are currently in the members list
     * 
     * @param members The list of members to be removed
     */
    public void removeMembers(String... members) {
        for(int i = 0; i < members.length; i++) {
            if(this.members.contains(members[i])) {
                this.members.remove(members[i]);
            }
        }
    }
    
    /**
     * This method should check the weather forecast for the next 7 days
     * and if the weather includes any of the alert tags, send an email
     * to the creator and members about the weather conflict
     */
    public void updateWeather() {
        Email updateUsers = new Email();
        Weather currentWeather = new Weather(getLocation());
        updateUsers.setTitle("Update for event " + this.title);
        
        int eventNumber = currentWeather.withinSevenDays(getEventDate(this.date));
        
//        // first check if the event date is within 7 days (Weather API limitations)
        if(eventNumber <= 7)
        { 
            for(String individuals: members)
            {
                updateUsers.addRecipients(individuals);             
            }
            String message = "DATE: " + this.date + "\n" + "Location: " + this.location + "\n" + "Weather: " + currentWeather.getSummary(WEEK-eventNumber) + "\n";
            
            // test
            System.out.println(message);
            
            updateUsers.setMessage(message);
            updateUsers.sendEmail(); 
        }
        else
        {
              if(eventNumber <= 7)
              { 
                for(String individuals: members)
                {
                    updateUsers.addRecipients(individuals);             
                }
                String message = "DATE: " + this.date + "\n" + "Location: " + this.location + "\n" + "Weather: Update not available (event date is beyond 7 days from today) \n";
                updateUsers.setMessage(message);
                updateUsers.sendEmail(); 
             }
                       
        }
    }

}
