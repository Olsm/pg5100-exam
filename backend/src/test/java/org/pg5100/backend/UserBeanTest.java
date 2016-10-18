package org.pg5100.backend;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.spi.ArquillianProxyException;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.Address;
import org.pg5100.backend.datalayer.Post;
import org.pg5100.backend.datalayer.User;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

@RunWith(Arquillian.class)
public class UserBeanTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.apache.commons.codec", "com.google.common.collect",
                        "org.pg5100.backend.datalayer", "org.pg5100.backend.businesslayer")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserBean userEJB;

    private User createUser(String userName, String password){
        return createUser(userName, password, "shiba@inu.wow");
    }

    private User createUser(String userName, String password, String email){
        return userEJB.registerNewUser(userName, password, email, "Satoshi Nakamoto", new Address("Suchcity", "Verycountry"));
    }

    @Test
    public void testRegisterNewUser() {
        String username = "SuchUserName";
        User user = createUser(username, "password");
        assertEquals(username.toLowerCase(), user.getUsername());
        assertEquals("Suchcity", user.getAddress().getCity());
        assertEquals("Verycountry", user.getAddress().getCountry());
        assertEquals("Satoshi Nakamoto", user.getName());
        assertEquals("shiba@inu.wow", user.getEmail());
    }

    @Test
    public void testGetNumberOfUsers() {
        int users = userEJB.getNumberOfUsers();
        createUser("suchUser", "password");
        assertEquals(users+1, userEJB.getNumberOfUsers());
    }

    @Test(expected = ArquillianProxyException.class)
    public void testUserMustHaveUsername() {
        createUser(null, "password");
    }

    @Test(expected = EJBException.class)
    public void testUserCannotBeEmpty() {
        createUser("   ", "password");
    }

    @Test(expected = EJBException.class)
    public void testUserMustBeMin3Chars() {
        createUser("12", "password");
    }

    public void testUserWithMinChars() {
        createUser("123", "password");
    }

    @Test(expected = EJBException.class)
    public void testUserMustBeMax15Chars() {
        createUser("1234567890123456", "password");
    }

    public void testUserWithMaxChars() {
        createUser("123456789012345", "password");
    }

    @Test(expected = EJBException.class)
    public void testUserWithInvalidSpecialChar() {
        createUser("123*abc", "password");
    }

    public void testUserWithValidSpecialChars() {
        createUser("1_2-3", "password");
    }

    @Test(expected = EJBException.class)
    public void testEmailWithoutDomain() {
        createUser("invalidEmail1", "password", "invalid@email");
    }

    @Test(expected = EJBException.class)
    public void testEmailWithoutAt() {
        createUser("invalidEmail2", "password", "invalidemail.wow");
    }

    @Test(expected = EJBException.class)
    public void testEmailWithoutAtAndDomain() {
        createUser("invalidEmail3", "password", "invalidemail");
    }

    @Test
    public void testNoTwoUsersWithSameId(){

        String userName = "sameusername";

        boolean created = createUser(userName,"a") != null;
        assertTrue(created);

        created = createUser(userName,"b") != null;
        assertFalse(created);
    }

    @Test
    public void testSamePasswordLeadToDifferentHashAndSalt(){

        String password = "password";
        String first = "first";
        String second = "second";

        createUser(first,password);
        createUser(second,password); //same password

        User f = userEJB.getUser(first);
        User s = userEJB.getUser(second);

        //those are EXTREMELY unlikely to be equal, although not impossible...
        //however, likely more chances to get hit in the head by a meteorite...
        assertNotEquals(f.getHash(), s.getHash());
        assertNotEquals(f.getSalt(), s.getSalt());
    }

    @Test
    public void testVerifyPassword(){

        String user = "verifypass";
        String correct = "correct";
        String wrong = "wrong";

        createUser(user, correct);

        boolean  canLogin = userEJB.login(user, correct);
        assertTrue(canLogin);

        canLogin = userEJB.login(user, wrong);
        assertFalse(canLogin);
    }

    @Test
    public void testBeSurePasswordIsNotStoredInPlain(){
        String password = "password";
        User user = createUser("plain", password);

        assertNotEquals(password, user.getUsername());
        assertNotEquals(password, user.getHash());
        assertNotEquals(password, user.getSalt());
    }

    /*
    @Test(expected = EJBException.class)
    public void testCreateAUserWithWrongCountry(){

        String user = "wrongcountry";
        String password = "password";

        userEJB.createUser(user,password,"a","b","c","FOO");
    }
    */

}