package prog5121.poe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StoredMessageTest {

    private storedMessages store;

    @Before
    public void setUp() throws IOException {
        store = new storedMessages();
        store.loadFromFile("testData.json");
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList("Did you get the cake?", "It is dinner time !"),
                     store.getSentMessages());
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                     store.getLongestMessage());
    }

    @Test
    public void testSearchForMessageID() {
        String result = store.searchByMessageID("0838884567");
        assertEquals("0838884567: It is dinner time !", result);
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
            "Where are you? You are late! I have asked you to be on time.",
            "Ok, I am leaving without you."));
        assertEquals(expected, store.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        String hash = store.getMessageHashes().get(1); // Test Data Message 2
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                     store.deleteByMessageHash(hash));
        assertFalse(store.getStoredMessages().contains(
            "Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testDisplayReport() {
        String report = store.displayReport();
        for (String hash : store.getMessageHashes()) {
            assertTrue(report.contains(hash));
        }
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
    }
}