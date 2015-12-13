package ci_events.User_Classes;
import ci_events.User_Classes.User;

public class Guest extends User {
    
    private static int USER_SECURITY = 0;

	public Guest(String name, String email, String password) {
		// TODO - implement Student.Student
            super(name, email, password, USER_SECURITY);
	}
    
}
