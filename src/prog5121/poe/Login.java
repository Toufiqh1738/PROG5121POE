package prog5121.poe;
public class Login {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String surName;   
    
// checks if Username conditions is met 
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }
// checks if Password conditions is met
    public boolean checkPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasNumber && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null || cellPhoneNumber.length() < 10) {
            return false;
        }

        for (int i = 0; i < cellPhoneNumber.length(); i++) {
            char ch = cellPhoneNumber.charAt(i);
            
            if (i == 0 && ch == '+') {
                continue;
            }
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public String registerUser(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPassword(password)) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number is incorrectly formatted, please correct the number and try again.";
        }

// Save user data upon successful validation
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.surName = lastName;

        return "The two above conditions have been met, and the user has been registered successfully.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername != null && enteredPassword != null
                && enteredUsername.equals(this.username)
                && enteredPassword.equals(this.password);
    }

    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + surName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}