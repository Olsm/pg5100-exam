package org.pg5100.backend;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pg5100.backend.businesslayer.PostBean;
import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.Post;
import org.pg5100.backend.datalayer.User;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class PostBeanTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.pg5100.backend.businesslayer","org.pg5100.backend.datalayer", "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private PostBean postEJB;
    @EJB
    private UserBean userBean;

    private User user;
    Post post;
    private String content;
    private static int counter;

    @Before
    public void setupBefore() {
        user = userBean.registerNewUser("username" + counter++, "password", "such@mail.com", "Shiba Inu", null);
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus volutpat turpis vitae bibendum auctor. Aliquam posuere tempus hendrerit. Sed at leo massa. Aenean eget libero est. Cras semper neque vitae nulla interdum rutrum. Duis est augue, vestibulum et justo eget, commodo consequat nulla. Sed elit libero, tincidunt eget finibus quis, cursus non ex. Nam in luctus ante. Quisque odio orci, scelerisque vel fringilla eget, suscipit ut sapien. Vivamus elementum eros vitae risus imperdiet aliquet. In ac dui sem. Morbi quis eros eleifend, feugiat tellus sed, malesuada massa.";
        post = postEJB.registerPost(user, "test", content);
    }

    @Test
    public void registerPost() throws Exception {
        assertEquals(user, post.getAuthor());
        assertEquals(content, post.getText());
    }

    @Test
    public void getPost() {
        assertEquals(post, postEJB.getPost(post.getId()));
    }

    //ToDo: Fix concurrency issue
    /*
    @Test
    public void registerCommentConcurrency() {
        int numberOfComments = post.getComments().size();
        for (int i = 0; i < 100; i++) {
            Runnable task = () -> { post = postEJB.registerComment(post, user, content); };
            new Thread(task).start();
        }
        assertEquals(100, post.getComments().size());
        assertEquals(100, postEJB.getPost(post.getId()).getComments().size());
    } */

    @Test
    public void getNumberOfPosts() throws Exception {
        int posts = postEJB.getNumberOfPosts();
        postEJB.registerPost(user, "title", content);
        assertEquals(posts + 1, postEJB.getNumberOfPosts());
    }

    @Test(expected = EJBException.class)
    public void testContentLongerThanLimit() {
        postEJB.registerPost(user, "title", new String(new char[50001]).replace('\0', ' '));
    }

    @Test
    public void testContentLengthLimit() {
        postEJB.registerPost(user, "title", new String(new char[50000]).replace('\0', ' '));
    }

}