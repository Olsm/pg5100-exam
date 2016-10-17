package org.pg5100.backend.datalayer;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class User {

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

    public User(String username, Address address, String name, String email) {
        if (username != null) username = username.toLowerCase();
        if (email != null) email = email.toLowerCase();
        this.username = username;
        this.address = address;
        this.name = name;
        this.email = email;
        this.date = new Date();
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
}
