package ci_events.User_Classes;

public class User {

    private String name;
    private String email;
    private String password;
    private int securityLevel;

    public User(String name, String email, String password, int security) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.securityLevel = security;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public int getSecurity() {
        return this.securityLevel;
    }
    
    public String getPassword() {
        return this.password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean equals(User other) {
        return this.email.equals(other.email) && this.password.equals(other.password);
    }

}
