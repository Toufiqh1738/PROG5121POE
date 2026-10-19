/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prog5121.poe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuickChat {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static int sentCount = 0;
    private static int processedCount = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to QuickChat.");

        // Classroom practice login gate (replace with Part 1 login when integrating)
        if (!loginGateForPractice()) {
            System.out.println("Login failed. You cannot access QuickChat.");
            return;
        }

        System.out.print("How many messages would you like to enter? ");
        int messageLimit = readPositiveInteger();

        boolean running = true;
        while (running) {
            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option (1-3): ");

            String option = INPUT.nextLine().trim();

            switch (option) {
                case "1":
                    sendMessages(messageLimit);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat.");
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        System.out.println("Total messages successfully sent: " + sentCount);
    }

    private static boolean loginGateForPractice() {
        System.out.print("For this Part 2 practice run, enter Y to simulate a successful login: ");
        return INPUT.nextLine().trim().equalsIgnoreCase("Y");
    }

    private static int readPositiveInteger() {
        while (true) {
            String value = INPUT.nextLine().trim();
            try {
                int number = Integer.parseInt(value);
                if (number > 0) {
                    return number;
                }
                System.out.print("Enter a number greater than zero: ");
            } catch (NumberFormatException ex) {
                System.out.print("That is not a whole number. Try again: ");
            }
        }
    }

    private static void sendMessages(int messageLimit) {
        if (processedCount >= messageLimit) {
            System.out.println("You have reached the message limit you selected.");
            return;
        }

        int remaining = messageLimit - processedCount;
        System.out.println("You may send or process up to " + remaining + " more message(s).");

        for (int i = processedCount; i < messageLimit; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + messageLimit + " ---");
            
            System.out.print("Enter recipient in international format (example +27718693002): ");
            String recipient = INPUT.nextLine().trim();

            Message draftForValidation = new Message(i, recipient, "placeholder");
            String recipientStatus = draftForValidation.checkRecipientCell();
            
            if (!recipientStatus.startsWith("Cell phone number successfully")) {
                System.out.println(recipientStatus);
                i--; // Let the user correct this message slot without consuming it
                continue;
            }

            System.out.print("Enter your message (maximum 250 characters): ");
            String text = INPUT.nextLine();

            Message message = new Message(i, recipient, text);
            String lengthStatus = message.checkMessageLength();
            System.out.println(lengthStatus);

            if (!lengthStatus.equals("Message ready to send.")) {
                i--; // Re-enter this message with shorter text
                continue;
            }

            System.out.println("\nChoose what to do with this message:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Your choice: ");

            String choice = INPUT.nextLine().trim();

            if (choice.equals("1")) {
                SENT_MESSAGES.add(message);
                sentCount++;
                processedCount = i + 1;
                System.out.println("Message successfully sent.");
                System.out.println(message.printMessages());
            } else if (choice.equals("2")) {
                System.out.println("Press 0 to delete the message.");
                System.out.print("Enter 0 to confirm disregard: ");
                String confirm = INPUT.nextLine().trim();
                if (confirm.equals("0")) {
                    processedCount = i + 1;
                    System.out.println("Message disregarded.");
                } else {
                    i--; // Retry slot
                    System.out.println("Disregard cancelled; message was not sent.");
                }
            } else if (choice.equals("3")) {
                try {
                    storeMessage(message);
                    processedCount = i + 1;
                    System.out.println("Message successfully stored.");
                    System.out.println(message.printMessages());
                } catch (IOException ex) {
                    System.out.println("The message could not be stored: " + ex.getMessage());
                    i--;
                }
            } else {
                System.out.println("Invalid message option. This message was not sent.");
                i--;
            }
        }
    }

    private static void storeMessage(Message message) throws IOException {
        Path file = Path.of("storedMessages.json");
        String existing = "[]";
        if (Files.exists(file)) {
            existing = Files.readString(file, StandardCharsets.UTF_8).trim();
        }

        String object = message.toJson();
        String updated;

        if (existing.isEmpty() || existing.equals("[]")) {
            updated = "[" + object + "]";
        } else if (existing.endsWith("]")) {
            updated = existing.substring(0, existing.length() - 1) + "," + object + "]";
        } else {
            throw new IOException("storedMessages.json is not a valid JSON array.");
        }

        Files.writeString(file, updated, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
