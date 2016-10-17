package org.pg5100.backend.businesslayer;

import org.pg5100.backend.datalayer.Comment;
import org.pg5100.backend.datalayer.Post;
import org.pg5100.backend.datalayer.User;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CommentBean extends PostBean {

    public CommentBean(){}

    public Comment registerComment(Post post, User author, String content) {
        return (Comment) super.registerComment(post, author, content);
    }

    public Comment getComment(Long id) {
        return em.find(Comment.class, id);
    }


    public int getNumberOfComments() {
        Query query = em.createNamedQuery(Comment.SUM_COMMENTS);
        return ((Number)query.getSingleResult()).intValue();
    }

    // TODO: Make sure only the author of the post can moderate comments
    public boolean moderate(Long commentId) {
        Comment comment = getComment(commentId);
        comment.moderate();
        return true;
    }
}
