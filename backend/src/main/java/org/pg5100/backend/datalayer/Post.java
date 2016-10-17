package org.pg5100.backend.datalayer;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Post.SUM_POSTS, query = "select count(p) from Post p"),
        @NamedQuery(name = Post.SUM_POSTS_IN_NORWAY, query = "select count(p) from Post p where p.author.address.country = 'Norway'")
})

@Entity
public class Post {

    public static final String SUM_POSTS = "SUM_POSTS";
    public static final String SUM_POSTS_IN_NORWAY = "SUM_POSTS_IN_NORWAY";

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne
    private User author;
    @Size(max = 50)
    private String title;
    @NotEmpty
    @Column(length = 50000)
    private String text;
    @Past
    private Date date;
    private int upVotes;
    private int downVotes;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Post() {
        this(null, null, null);
    }

    public Post(User author, String title, String text) {
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = new Date();
        this.upVotes = 0;
        this.downVotes = 0;
        comments = Collections.synchronizedList(new ArrayList<Comment>());
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public synchronized List<Comment> getComments() {
        return comments;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public int getVotes() {
        return upVotes - downVotes;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public synchronized void addComment(Comment comment) {comments.add(comment);}

    public void upVote() {
        this.upVotes++;
    }

    public void downVote() {
        this.downVotes++;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Post.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Post other = (Post) obj;
        return (this.getId() == null) ? other.getId() == null : this.getId().equals(other.getId());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
