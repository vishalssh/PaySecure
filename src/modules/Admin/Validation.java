package Admin;

public class Validation {

    // Validate username: minimum 3 characters, not null or empty
    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.length() >= 3;
    }

    // Validate password: minimum 6 characters, not null or empty
    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty() && password.length() >= 6;
    }

    // Validate amount (positive number)
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    // Validate role (ADMIN or USER)
    public static boolean isValidRole(String role) {
        return "ADMIN".equalsIgnoreCase(role) || "USER".equalsIgnoreCase(role);
    }
}

