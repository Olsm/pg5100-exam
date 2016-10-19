package org.pg5100.frontend;
import org.pg5100.backend.businesslayer.PostBean;
import org.pg5100.backend.businesslayer.UserBean;
import org.pg5100.backend.datalayer.Post;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
@SessionScoped
public class PostController implements Serializable{

    private String formTitle;
    private String formText;

    @EJB
    private PostBean postEJB;
    @EJB
    private UserBean userEJB;
    @Inject
    private UserController userController;

    public String registerNew(){

        try {
            postEJB.registerPost(userController.getRegisteredUser(), formTitle, formText);
        } catch (Exception e){
            return "newPost.jsf";
        }

        return "home.jsf";
    }


    public List<Post> getPostList(){
        return postEJB.getAll();
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
