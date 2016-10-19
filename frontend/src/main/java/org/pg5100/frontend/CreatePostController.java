package org.pg5100.frontend;

import org.pg5100.backend.businesslayer.PostBean;
import org.pg5100.backend.businesslayer.UserBean;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class CreatePostController {

    private String formTitle;
    private String formText;

    @EJB
    private PostBean postEJB;
    @EJB
    private UserBean userEJB;
    @Inject
    private UserController userController;


    public String createPost(){

        try {
            postEJB.registerPost(userController.getRegisteredUser(), formTitle, formText);
        } catch (Exception e){
            return "newEvent.jsf";
        }

        return "home.jsf";
    }


    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formext) {
        this.formText = formext;
    }
}
