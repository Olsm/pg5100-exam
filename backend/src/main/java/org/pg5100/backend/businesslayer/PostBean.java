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
        return em.find(Post.class, id);
    }

    public int getNumberOfPosts(){
        Query query = em.createNamedQuery(Post.SUM_POSTS);
        return ((Number)query.getSingleResult()).intValue();
    }

    public List<Post> getAll() {
        Query query = em.createQuery("SELECT p FROM Post p");
        return (List<Post>) query.getResultList();
    }

    public Post registerComment(Post post, User author, String content) {
        Comment comment = new Comment(author, content);
        em.persist(comment);
        post.addComment(comment);
        em.merge(post);
        return post;
    }

    public Post upvote(Post post) {
        post.upVote();
        return post;
    }

    public Post downVote(Post post) {
        post.downVote();
        return post;
    }
}
