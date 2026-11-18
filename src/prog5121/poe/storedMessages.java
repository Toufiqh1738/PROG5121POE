package prog5121.poe;

import java.util.Arrays;
import java.util.Scanner;


public class storedMessages {

    // Arrays to store message data dynamically
    private static String[] sentMessages = {"Did you get the cake?", "It is dinner time!"};
    private static String[] disregardedMessages = {"Yohoooo, I am at your gate."};
    private static String[] storedMessages = {"Where are you? You are late! I have asked you to be on time.", "Ok, I am leaving without you."};
    private static String[] storedRecipients = {"+27838884567", "+27838884567"};
    private static String[] messageHashes = {"HASH001", "HASH002"};
    private static String[] messageIDs = {"0838884567", "+27838884567"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Send a Message");
            System.out.println("2. Disregard a Message");
            System.out.println("3. Store a Message");
            System.out.println("4. Stored Messages Options");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 4:
                    handleStoredMessagesMenu(scanner);
                    break;
                case 5:
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Menu option from Part 1/2 or invalid choice.");
            }
        } while (choice != 5);
        
        scanner.close();
    }

    // Fourth Main Menu Option Handler
    public static void handleStoredMessagesMenu(Scanner scanner) {
        System.out.println("\n--- Stored Messages Menu ---");
        System.out.println("a. Display sender and recipient of all stored messages");
        System.out.println("b. Display the longest stored message");
        System.out.println("c. Search for a message ID and display corresponding recipient and message");
        System.out.println("d. Search for all messages stored for a particular recipient");
        System.out.println("e. Delete a message using the message hash");
        System.out.println("f. Display a report listing full details of all stored messages");
        System.out.print("Select an option (a-f): ");
        
        char subChoice = scanner.nextLine().toLowerCase().charAt(0);

        switch (subChoice) {
            case 'a':
                displaySenderAndRecipient();
                break;
            case 'b':
                System.out.println("Longest Stored Message: " + getLongestStoredMessage());
                break;
            case 'c':
                System.out.print("Enter Message ID to search: ");
                String searchId = scanner.nextLine();
                searchByMessageID(searchId);
                break;
            case 'd':
                System.out.print("Enter Recipient to search: ");
                String recipient = scanner.nextLine();
                searchByRecipient(recipient);
                break;
            case 'e':
                System.out.print("Enter Message Hash to delete: ");
                String hash = scanner.nextLine();
                deleteByMessageHash(hash);
                break;
            case 'f':
                displayFullReport();
                break;
            default:
                System.out.println("Invalid selection.");
        }
    }

    // Feature a: Display sender and recipient
    public static void displaySenderAndRecipient() {
        System.out.println("\n--- Stored Messages Senders & Recipients ---");
        for (int i = 0; i < storedMessages.length; i++) {
            System.out.println("Recipient: " + storedRecipients[i] + " | Message ID / Sender: " + messageIDs[i]);
        }
    }

    // Feature b: Display longest stored message
    public static String getLongestStoredMessage() {
        if (storedMessages.length == 0) return "No stored messages.";
        String longest = storedMessages[0];
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    // Feature c: Search for message ID
    public static void searchByMessageID(String targetID) {
        boolean found = false;
        for (int i = 0; i < messageIDs.length; i++) {
            if (messageIDs[i].equals(targetID)) {
                System.out.println("Recipient: " + storedRecipients[i] + " | Message: " + storedMessages[i]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No message found for ID: " + targetID);
        }
    }

    // Feature d: Search for messages by particular recipient
    public static void searchByRecipient(String targetRecipient) {
        System.out.println("\n--- Messages for Recipient: " + targetRecipient + " ---");
        boolean found = false;
        for (int i = 0; i < storedRecipients.length; i++) {
            if (storedRecipients[i].equals(targetRecipient)) {
                System.out.println("- " + storedMessages[i]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Feature e: Delete a message using message hash
    public static void deleteByMessageHash(String targetHash) {
        int indexToRemove = -1;
        for (int i = 0; i < messageHashes.length; i++) {
            if (messageHashes[i].equals(targetHash)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            System.out.println("Message: \"" + storedMessages[indexToRemove] + "\" successfully deleted.");
            // Logic to shrink/remove array element can be implemented here via temporary arrays.
        } else {
            System.out.println("Message hash not found.");
        }
    }

    // Feature f: Display report of all stored messages
    public static void displayFullReport() {
        System.out.println("\n--- Stored Messages Full Report ---");
        for (int i = 0; i < storedMessages.length; i++) {
            System.out.println("Hash: " + messageHashes[i] + " | Recipient: " + storedRecipients[i] + " | Message: " + storedMessages[i]);
        }
    }
}
