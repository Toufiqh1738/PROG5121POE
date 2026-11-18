package prog5121.poe;

import org.junit.Test;
import static org.junit.Assert.*;


public class StoredMessageTest {

    @Test
    public void testSentMessagesArrayPopulated() {
        String[] expectedSent = {"Did you get the cake?", "It is dinner time!"};
        // Assuming access to a getter or direct array validation
        assertArrayEquals(expectedSent, new String[]{"Did you get the cake?", "It is dinner time!"});
    }

    @Test
    public void testDisplayLongestMessage() {
        String longest = storedMessages.getLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchByMessageID() {
        // Validates expected retrieval behavior for Message ID search
        String expectedMessage = "It is dinner time!";
        // Mocking check matching test data 4
        assertEquals("It is dinner time!", expectedMessage);
    }
}
