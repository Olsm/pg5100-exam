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

    private List<Post> postList;

    @EJB
    private PostBean postEJB;
    @EJB
    private UserBean userEJB;
    @Inject
    private UserController userController;


    @PostConstruct
    public void init(){
        postList = Collections.synchronizedList(new ArrayList<>());
        updatePostList();
    }


    public List<Post> getPostList(){
        return postList;
    }

    public void updatePostList() {
        postList = postEJB.getAll();
    }

}
