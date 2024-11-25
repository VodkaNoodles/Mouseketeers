package Utilities; // Adjust the package name as needed.

public class UserSession {
    private static UserSession instance;
    private int userId;

    // Private constructor to prevent instantiation
    private UserSession() {}

    // Get the singleton instance
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Set user ID
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Get user ID
    public int getUserId() {
        return userId;
    }
}
