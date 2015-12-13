package ci_events.User_Classes;
import ci_events.User_Classes.User;

public class Student extends User {

	private static int USER_SECURITY = 1;

	public Student(String name, String email, String password) {
            super(name, email, password, USER_SECURITY);
	}

}