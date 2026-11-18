package prog5121.poe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class storedMessages {

    public static final String SENDER = "Developer";

    // Required arrays
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessageList = new ArrayList<>();
    private final ArrayList<String> messageHash = new ArrayList<>();
    private final ArrayList<String> messageID = new ArrayList<>();

    // Parallel arrays (same index as messageID / messageHash)
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> allMessages = new ArrayList<>();
    private final ArrayList<String> flags = new ArrayList<>();

   

    // Reads a JSON file and populates every array
    public void loadFromFile(String path) throws IOException {
        loadFromJson(Files.readString(Path.of(path), StandardCharsets.UTF_8));
    }

    
    public void loadFromJson(String json) {
        Matcher obj = Pattern.compile("\\{[^{}]*\\}").matcher(json);
        while (obj.find()) {
            String o = obj.group();
            String id = field(o, "messageID");
            if (id.isEmpty()) id = field(o, "messageId");
            String flag = field(o, "flag");
            addMessage(id, field(o, "messageHash"), field(o, "recipient"),
                       field(o, "message"), flag.isEmpty() ? "Stored" : flag);
        }
    }

    private static String field(String obj, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(obj);
        if (!m.find()) return "";
        return m.group(1).replace("\\n", "\n").replace("\\r", "\r")
                         .replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // Adds one message to all arrays. A blank hash is generated. 
    public void addMessage(String id, String hash, String recipient, String text, String flag) {
        if (hash == null || hash.isBlank()) {
            hash = createHash(id, allMessages.size() + 1, text);
        }
        messageID.add(id);
        messageHash.add(hash);
        recipients.add(recipient);
        allMessages.add(text);
        flags.add(flag);

        switch (flag.trim().toLowerCase()) {
            case "sent":
                sentMessages.add(text);
                break;
            case "stored":
                storedMessageList.add(text);
                break;
            case "disregard":
            case "disregarded":
                disregardedMessages.add(text);
                break;
            default:
                break;
        }
    }

    // First two digits of ID : message number : FIRST+LAST word, upper case. 
    public static String createHash(String id, int number, String text) {
        String[] words = text.replaceAll("[^A-Za-z0-9\\s]", "").trim().split("\\s+");
        String prefix = id.length() >= 2 ? id.substring(0, 2) : id;
        return (prefix + ":" + number + ":" + words[0] + words[words.length - 1]).toUpperCase();
    }

    /** Writes every message back to a JSON file so changes survive a restart. */
    public void saveToFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < allMessages.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\n  {\"messageID\":\"").append(esc(messageID.get(i)))
              .append("\",\"messageHash\":\"").append(esc(messageHash.get(i)))
              .append("\",\"recipient\":\"").append(esc(recipients.get(i)))
              .append("\",\"message\":\"").append(esc(allMessages.get(i)))
              .append("\",\"flag\":\"").append(esc(flags.get(i))).append("\"}");
        }
        sb.append("\n]");
        Files.writeString(Path.of(path), sb.toString(), StandardCharsets.UTF_8);
    }

    private static String esc(String v) {
        return v.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    

    public ArrayList<String> getSentMessages() { return sentMessages; }
    public ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<String> getStoredMessages() { return storedMessageList; }
    public ArrayList<String> getMessageHashes() { return messageHash; }
    public ArrayList<String> getMessageIDs() { return messageID; }

    

    // Sender and recipient of all stored messages
    public String displaySenderAndRecipient() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allMessages.size(); i++) {
            if (flags.get(i).trim().equalsIgnoreCase("stored")) {
                sb.append("Sender: ").append(SENDER)
                  .append(" | Recipient: ").append(recipients.get(i)).append("\n");
            }
        }
        return sb.isEmpty() ? "No stored messages.\n" : sb.toString();
    }

    // Longest message held in the arrays. 
    public String getLongestMessage() {
        if (allMessages.isEmpty()) return "No messages.";
        String longest = allMessages.get(0);
        for (String m : allMessages) {
            if (m.length() > longest.length()) longest = m;
        }
        return longest;
    }

    // Search by message ID -> "recipient: message". 
    public String searchByMessageID(String id) {
        int i = messageID.indexOf(id);
        if (i < 0) return "No message found for ID: " + id;
        return recipients.get(i) + ": " + allMessages.get(i);
    }

    // d. All messages for one recipient. 
    public ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < allMessages.size(); i++) {
            if (recipients.get(i).equals(recipient)) result.add(allMessages.get(i));
        }
        return result;
    }

    // Delete a message using its hash (removed from every array). 
    public String deleteByMessageHash(String hash) {
        int i = messageHash.indexOf(hash);
        if (i < 0) return "Message hash not found.";
        String text = allMessages.get(i);
        String flag = flags.get(i).trim().toLowerCase();

        messageID.remove(i);
        messageHash.remove(i);
        recipients.remove(i);
        allMessages.remove(i);
        flags.remove(i);

        if (flag.equals("sent")) sentMessages.remove(text);
        else if (flag.equals("stored")) storedMessageList.remove(text);
        else disregardedMessages.remove(text);

        return "Message: \"" + text + "\" successfully deleted.";
    }

    // Report with the full details of every message. 
    public String displayReport() {
        StringBuilder sb = new StringBuilder("--- Message Report ---\n");
        for (int i = 0; i < allMessages.size(); i++) {
            sb.append("Message Hash: ").append(messageHash.get(i)).append("\n")
              .append("Message ID:   ").append(messageID.get(i)).append("\n")
              .append("Sender:       ").append(SENDER).append("\n")
              .append("Recipient:    ").append(recipients.get(i)).append("\n")
              .append("Message:      ").append(allMessages.get(i)).append("\n")
              .append("Flag:         ").append(flags.get(i)).append("\n")
              .append("----------------------\n");
        }
        return sb.toString();
    }

    //  menu handler

    // Fourth main menu option: "Stored Messages". Returns true if data changed. 
    public boolean handleStoredMessagesMenu(Scanner in) {
        boolean changed = false;
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search for all messages for a recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display a report of all messages");
            System.out.println("x) Back");
            System.out.print("Select an option: ");
            String c = in.nextLine().trim().toLowerCase();

            switch (c) {
                case "a":
                    System.out.print(displaySenderAndRecipient());
                    break;
                case "b":
                    System.out.println("Longest message: " + getLongestMessage());
                    break;
                case "c":
                    System.out.print("Enter message ID: ");
                    System.out.println(searchByMessageID(in.nextLine().trim()));
                    break;
                case "d":
                    System.out.print("Enter recipient: ");
                    ArrayList<String> found = searchByRecipient(in.nextLine().trim());
                    if (found.isEmpty()) System.out.println("No messages found for this recipient.");
                    for (String m : found) System.out.println("- " + m);
                    break;
                case "e":
                    System.out.print("Enter message hash: ");
                    String result = deleteByMessageHash(in.nextLine().trim());
                    System.out.println(result);
                    if (result.endsWith("successfully deleted.")) changed = true;
                    break;
                case "f":
                    System.out.print(displayReport());
                    break;
                case "x":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
        return changed;
    }
}
