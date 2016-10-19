package org.pg5100.backend.datalayer;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = User.GET_POSTS, query = "select p from Post p left join fetch p.upVotes left join fetch p.downVotes left join fetch p.comments where p.author = :user"),
        @NamedQuery(name = User.GET_COMMENTS, query = "select c from Comment c left join fetch c.upVotes left join fetch c.downVotes left join fetch c.comments where c.author = :user")
})

@Entity
public class User {

    public static final String GET_POSTS = "GET_POSTS";
    public static final String GET_COMMENTS = "GET_COMMENTS";

    @Id @Pattern(regexp = "^(?i)[A-Z0-9_-]{3,15}$")
    private String username;
    @NotEmpty @Size(max = 26)
    private String salt;
    @NotEmpty
    private String hash;
    @Embedded
    private Address address;
    private String name;
    @Email @Pattern(regexp = ".*?\\.(?i)[A-Z0-9].*") // valid email must end with .something
    private String email;
    @Past
    private Date date;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="POST_UPVOTES",
            joinColumns={@JoinColumn(name="USERNAME")},
            inverseJoinColumns={@JoinColumn(name="POST_ID")})
    private Set<Post> upVotes;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="POST_DOWNVOTES",
            joinColumns={@JoinColumn(name="USERNAME")},
            inverseJoinColumns={@JoinColumn(name="POST_ID")})
    private Set<Post> downVotes;

    public User(String username, Address address, String name, String email) {
        if (username != null) username = username.toLowerCase();
        if (email != null) email = email.toLowerCase();
        this.username = username;
        this.address = address;
        this.name = name;
        this.email = email;
        this.date = new Date();
        this.upVotes = new HashSet<>();
        this.downVotes = new HashSet<>();
    }

    public User(String username) {
        this(username, null, null, null);
    }

    public User() {
        this(null, null, null, null);
    }

    public String getUsername() {
        return username;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void upVote(Post post) {
        upVotes.add(post);
        downVotes.remove(post);
    }

    public void downVote(Post post) {
        downVotes.add(post);
        upVotes.remove(post);
    }

    public void unVote(Post post) {
        upVotes.remove(post);
        downVotes.remove(post);
    }

    public int getUpVotes() {
        return upVotes.size();
    }

    public int getDownVotes() {
        return downVotes.size();
    }

    public String toString() {
        return getUsername();
    }
}
