package org.pg5100.backend.businesslayer;
import org.pg5100.backend.datalayer.Comment;
import org.pg5100.backend.datalayer.Post;
import org.pg5100.backend.datalayer.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PostBean {

    @PersistenceContext
    protected EntityManager em;

    public PostBean(){}

    public Post registerPost(User author, String title, String content) {
        Post post = new Post(author, title, content);
        em.persist(post);
        return post;
    }

    public Post getPost(Long id) {
        Query query = em.createQuery("SELECT p FROM Post p left join fetch p.upVotes left join fetch p.downVotes where p.id = :id");
        query.setParameter("id", id);
        return (Post) query.getSingleResult();
    }

    public int getNumberOfPosts(){
        Query query = em.createNamedQuery(Post.SUM_POSTS);
        return ((Number)query.getSingleResult()).intValue();
    }

    public List<Post> getAll() {
        Query query = em.createQuery("SELECT p FROM Post p");
        return (List<Post>) query.getResultList();
    }

    public Comment registerComment(Post post, User author, String content) {
        Comment comment = new Comment(author, content);
        em.persist(comment);
        post.addComment(comment);
        return comment;
    }

    public void upvote(Post post, User user) {
        post.upVote(user);
        user.upVote(post);
    }

    public void downVote(Post post, User user) {
        post.downVote(user);
        user.downVote(post);
    }

    public void unVote(Post post, User user) {
        post.unVote(user);
        user.unVote(post);
    }
}
