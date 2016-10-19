package org.pg5100.frontend;

import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.Address;
import org.pg5100.backend.datalayer.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserController implements Serializable{

    @EJB
    private UserBean userEJB;

    private String formUserName;
    private String formEmail;
    private String formPassword;
    private String formConfirmPassword;
    private String formName;
    private String formCity;
    private String formCountry;

    // The current user registered in this session
    private User registeredUser;

    public UserController(){
    }

    public String getUserCountry(){
        if(registeredUser == null){
            return null;
        }

        return registeredUser.getAddress().getCountry();
    }

    public boolean isLoggedIn(){
        return registeredUser != null;
    }


    public User getRegisteredUser(){
        return registeredUser;
    }

    public String logOut(){
        registeredUser = null;
        return "index.jsf";
    }


    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            registeredUser = userEJB.getUser(formUserName);
            return "index.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){

        if(! formPassword.equals(formConfirmPassword)){
            return "newUser.jsf";
        }

        User user = userEJB.registerNewUser(formUserName, formPassword, formEmail, formName, new Address(formCity, formCountry));

        if(user != null){
            registeredUser = user;
            return "index.jsf";
        } else {
            return "newUser.jsf";
        }
    }

    public String getFormUserName() {
        return formUserName;
    }

    public void setFormUserName(String formUserName) {
        this.formUserName = formUserName;
    }

    public String getFormPassword() {
        return formPassword;
    }

    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }

    public String getFormConfirmPassword() {
        return formConfirmPassword;
    }

    public void setFormConfirmPassword(String formConfirmPassword) {
        this.formConfirmPassword = formConfirmPassword;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormCountry() {
        return formCountry;
    }

    public void setFormCountry(String formCountry) {
        this.formCountry = formCountry;
    }

    public String getFormCity() {
        return formCity;
    }

    public void setFormCity(String formCity) {
        this.formCity = formCity;
    }

    public String getFormEmail() {
        return formEmail;
    }

    public void setFormEmail(String formEmail) {
        this.formEmail = formEmail;
    }
}
