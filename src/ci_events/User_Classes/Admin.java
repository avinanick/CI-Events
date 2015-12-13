package ci_events.User_Classes;
import ci_events.User_Classes.User;

public class Admin extends User {

    private static int USER_SECURITY = 3;

	public Admin(String name, String email, String password) {
            super(name, email, password, USER_SECURITY);
	}

}