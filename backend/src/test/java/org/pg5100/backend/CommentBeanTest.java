package org.pg5100.backend;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pg5100.backend.businesslayer.CommentBean;
import org.pg5100.backend.businesslayer.PostBean;
import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.Comment;
import org.pg5100.backend.datalayer.Post;
import org.pg5100.backend.datalayer.User;

import javax.ejb.EJB;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CommentBeanTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.pg5100.backend.datalayer", "org.pg5100.backend.businesslayer",
                        "org.apache.commons.codec", "com.google.common.collect")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CommentBean commentBean;
    @EJB
    private UserBean userBean;
    @EJB
    private PostBean postBean;

    private User user;
    private Post post;
    private String content;
    private static int counter;

    @Before
    public void setupBefore() {
        user = userBean.registerNewUser("username" + counter++, "password", "such@mail.com", "Shiba Inu", null);
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus volutpat turpis vitae bibendum auctor. Aliquam posuere tempus hendrerit. Sed at leo massa. Aenean eget libero est. Cras semper neque vitae nulla interdum rutrum. Duis est augue, vestibulum et justo eget, commodo consequat nulla. Sed elit libero, tincidunt eget finibus quis, cursus non ex. Nam in luctus ante. Quisque odio orci, scelerisque vel fringilla eget, suscipit ut sapien. Vivamus elementum eros vitae risus imperdiet aliquet. In ac dui sem. Morbi quis eros eleifend, feugiat tellus sed, malesuada massa.";
        post = postBean.registerPost(user, "title", content);
        post = postBean.registerComment(post, user, "Very comment");
    }

    @Test
    public void testGetComment() {
        Comment comment = post.getComments().get(0);
        assertEquals(comment, commentBean.getComment(comment.getId()));
    }

    @Test
    public void testModerateOwn() {
        // TODO
    }

    @Test
    public void testFailModerateOther() {
        // TODO
    }

}