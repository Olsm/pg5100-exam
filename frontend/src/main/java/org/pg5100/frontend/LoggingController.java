package org.pg5100.frontend;

import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.Address;
//import org.pg5100.backend.Countries;
import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoggingController implements Serializable{

    @EJB
    private UserBean userEJB;

    private String formUserName;
    private String formPassword;
    private String formConfirmPassword;
    private String formFirstName;
    private String formEmail;
    private String formCity;
    private String formCountry;



    /**
     * The current user registered in this session
     */
    private String registeredUser;


    public LoggingController(){
    }

    public String getUserCountry(){
        if(registeredUser == null){
            return null;
        }

        return userEJB.getUser(registeredUser).getAddress().getCountry();
    }

    /*public List<String> getCountries(){
        return Countries.getCountries();
    }*/



    public boolean isLoggedIn(){
        return registeredUser != null;
    }


    public String getRegisteredUser(){
        return registeredUser;
    }

    public User getUser(){
        return userEJB.getUser(registeredUser);
    }

    public String logOut(){
        registeredUser = null;
        return "home.jsf";
    }


    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            registeredUser = formUserName;
            return "home.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){

        if(! formPassword.equals(formConfirmPassword)){
            return "newUser.jsf";
        }

        boolean registered = false;

        try {
            registered = userEJB.registerNewUser(formUserName, formPassword, formEmail, formFirstName, new Address(formCity, formCountry)) != null;
        } catch (Exception e){
        }

        if(registered){
            registeredUser = formUserName;
            return "home.jsf";
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

    public String getFormFirstName() {
        return formFirstName;
    }

    public void setFormFirstName(String formFirstName) {
        this.formFirstName = formFirstName;
    }

    public String getFormEmail() {
        return formEmail;
    }

    public void setFormEmail(String formEmail) {
        this.formEmail = formEmail;
    }

    public String getFormCity() {
        return formCity;
    }

    public void setFormCity(String formCity) {
        this.formCity = formCity;
    }

    public String getFormCountry() {
        return formCountry;
    }

    public void setFormCountry(String formCountry) {
        this.formCountry = formCountry;
    }
}
