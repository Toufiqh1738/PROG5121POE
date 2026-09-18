package prog5121.poe;

import java.util.Scanner;


public class PROG5121POE {

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login userLogin = new Login();

        System.out.println("===== USER REGISTRATION =====");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter SurName: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Your South Africa Cell Phone Number: ");
        String cellPhone = scanner.nextLine();

        //Registration Attempt
        String regStatus = userLogin.registerUser(username, password, cellPhone, firstName, lastName);
        System.out.println(regStatus);

        // If registration fails, exit program
        if (!regStatus.contains("registered successfully")) {
            System.out.println("Registration failed.Please try again.");
            return;
        }

        //Login Attempt 
        System.out.println("===== USER LOGIN =====");
        System.out.print("Enter Username: ");
        String loginUser = scanner.nextLine();

        System.out.print("Enter Password: ");
        String loginPass = scanner.nextLine();

        boolean loginSuccess = userLogin.loginUser(loginUser, loginPass);
        System.out.println(userLogin.returnLoginStatus(loginSuccess));
        
        scanner.close();
    }
    
}
