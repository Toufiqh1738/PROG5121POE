
package prog5121.poe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class LoginTest {
    private Login login;

    @Before
    public void setUp() {
        login = new Login();
    }

   

    @Test
    public void testCheckUserName_Valid() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserName_InvalidLength() {
        assertFalse(login.checkUserName("kyle_123"));
    }

    @Test
    public void testCheckUserName_MissingUnderscore() {
        assertFalse(login.checkUserName("kyle1"));
    }

    

    @Test
    public void testCheckPassword_Valid() {
        assertTrue(login.checkPassword("Ch3ss!!!"));
    }

    @Test
    public void testCheckPassword_Invalid() {
        assertFalse(login.checkPassword("password"));
    }

    

    @Test
    public void testCheckCellPhoneNumber_ValidWithPlus() {
        assertTrue(login.checkCellPhoneNumber("+27831234567"));
    }

    @Test
    public void testCheckCellPhoneNumber_ValidDigitsOnly() {
        assertTrue(login.checkCellPhoneNumber("0831234567"));
    }

    @Test
    public void testCheckCellPhoneNumber_InvalidFormat() {
        assertFalse(login.checkCellPhoneNumber("083abc1234"));
    }

  

    @Test
    public void testRegisterUser_Success() {
        String expected = "The two above conditions have been met, and the user has been registered successfully.";
        String actual = login.registerUser("kyl_1", "Ch3ss!!!", "+27831234567", "Kyle", "Smith");
        assertEquals(expected, actual);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        String expected = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        String actual = login.registerUser("kyle_123", "Ch3ss!!!", "+27831234567", "Kyle", "Smith");
        assertEquals(expected, actual);
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        String expected = "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        String actual = login.registerUser("kyl_1", "password", "+27831234567", "Kyle", "Smith");
        assertEquals(expected, actual);
    }

    @Test
    public void testRegisterUser_InvalidCellPhoneNumber() {
        String expected = "Cell number is incorrectly formatted, please correct the number and try again.";
        String actual = login.registerUser("kyl_1", "Ch3ss!!!", "123", "Kyle", "Smith");
        assertEquals(expected, actual);
    }

   

    @Test
    public void testLoginUser_SuccessAndFailure() {
        login.registerUser("kyl_1", "Ch3ss!!!", "+27831234567", "Kyle", "Smith");

        assertTrue(login.loginUser("kyl_1", "Ch3ss!!!"));
        assertFalse(login.loginUser("kyl_1", "WrongPass"));
        assertFalse(login.loginUser("wrong_user", "Ch3ss!!!"));
    }

    @Test
    public void testReturnLoginStatus_Success() {
        login.registerUser("kyl_1", "Ch3ss!!!", "+27831234567", "Kyle", "Smith");
        boolean loginResult = login.loginUser("kyl_1", "Ch3ss!!!");

        String expected = "Welcome Kyle, Smith it is great to see you again.";
        String actual = login.returnLoginStatus(loginResult);
        assertEquals(expected, actual);
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        login.registerUser("kyl_1", "Ch3ss!!!", "+27831234567", "Kyle", "Smith");
        boolean loginResult = login.loginUser("kyl_1", "WrongPass");

        String expected = "Username or password incorrect, please try again.";
        String actual = login.returnLoginStatus(loginResult);
        assertEquals(expected, actual);
    }
}