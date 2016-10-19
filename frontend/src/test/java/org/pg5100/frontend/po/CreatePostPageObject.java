package org.pg5100.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

public class CreatePostPageObject extends PageObject {

    public CreatePostPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Create Post", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Create Post");
    }

    public HomePageObject createPost(String title, String text){
        setText("createPostForm:title",title);
        setText("createPostForm:text",text);

        driver.findElement(By.id("createPostForm:createButton")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }

}
