package main.java.member;

public final class Account {

    public static volatile String usernameSaved;
    public static volatile String passwordSaved;
    public static volatile boolean isRemembered;
    public static volatile String roleSaved;

    public Account() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String username;
    private String password;
    private String role;
    private String email;

    public Object[] toArray() {
        return new Object[]{
            this.username,
            this.password,
            this.role,
            this.email
        };
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
