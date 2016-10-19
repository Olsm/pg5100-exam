package org.pg5100.frontend;

import org.junit.Before;
import org.junit.Test;
import org.pg5100.frontend.po.CreatePostPageObject;
import org.pg5100.frontend.po.CreateUserPageObject;
import org.pg5100.frontend.po.HomePageObject;
import org.pg5100.frontend.po.LoginPageObject;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class WebPageIT extends WebTestBase{


    @Before
    public void startFromInitialPage() {

        assumeTrue(JBossUtil.isJBossUpAndRunning());

        home = new HomePageObject(getDriver());
        home.toStartingPage();
        home.logout();
        assertTrue(home.isOnPage());
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testHomePage(){
        home.toStartingPage();
        assert(home.isOnPage());
    }


    @Test
    public void testLoginLink(){
        LoginPageObject login = home.toLogin();
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        LoginPageObject login = home.toLogin();
        HomePageObject home = login.clickLogin(getUniqueId(),"foo");
        assertNull(home);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLogin(){
        String userId = getUniqueId();
        createAndLogNewUser(userId, "Joe", "shiba@inu.wow", "Oslo", "Norway");
        home.logout();

        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId, "foo");

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userId));
    }

    @Test
    public void testCreateValidUser(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();
        assertTrue(create.isOnPage());

        String userName = getUniqueId();

        HomePageObject home = create.createUser(userName,"foo","foo","Foo","foo@foo.foo","Bar","Norway");
        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userName));

        home.logout();
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testCreateUserFailDueToPasswordMismatch(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();

        HomePageObject home = create.createUser(getUniqueId(),"foo","differentPassword","Foo","","Bar","Norway");
        assertNull(home);
        assertTrue(create.isOnPage());
    }


    @Test
    public void testCreateOnePost(){

        String userId = getUniqueId();
        createAndLogNewUser(userId, "Foo", "shiba@inu.wow", "Oslo", "Norway");

        int n = home.getNumberOfDisplayedPosts();
        String title = getUniqueTitle();

        CreatePostPageObject create = home.toCreatePost();
        home = create.createPost(title, "a concert");

        assertNotNull(home);
        int x = home.getNumberOfDisplayedPosts();
        assertEquals(n+1, x);
        assertTrue(getPageSource().contains(title));
    }


    @Test
    public void testCreatePostsFromDifferenUsers(){

        int n = home.getNumberOfDisplayedPosts();

        String first = getUniqueId();
        createAndLogNewUser(first, "Foo", "shiba@inu.wow", "Oslo", "Norway");
        CreatePostPageObject create = home.toCreatePost();
        home = create.createPost(first, "a concert");
        home.logout();

        String second = getUniqueId();
        createAndLogNewUser(second, "Foo", "shiba@inu.wow", "Oslo", "Norway");
        create = home.toCreatePost();
        home = create.createPost(second, "a concert");

        assertEquals(n+2, home.getNumberOfDisplayedPosts());
        assertTrue(getPageSource().contains(first));
        assertTrue(getPageSource().contains(second));
    }
}
