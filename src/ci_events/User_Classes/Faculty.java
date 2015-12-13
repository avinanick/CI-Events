package ci_events.User_Classes;
import ci_events.User_Classes.User;

public class Faculty extends User {

	private static int USER_SECURITY = 2;

	public Faculty(String name, String email, String password) {
            super(name, email, password, USER_SECURITY);
	}

}