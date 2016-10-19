package org.pg5100.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public class HomePageObject extends PageObject {


    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public HomePageObject toStartingPage() {
        String context = "/pg5100_exam/mynews"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }

    public boolean isOnPage() {
        return driver.getTitle().equals("MyNews Home Page");
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }


    public CreatePostPageObject toCreatePost() {
        if (!isLoggedIn()) {
            return null;
        }

        driver.findElement(By.id("createPost")).click();
        waitForPageToLoad();
        return new CreatePostPageObject(driver);
    }

    public int getNumberOfDisplayedPosts() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr[string-length(text()) > 0]"));

        return elements.size();
    }


    public int getNumberOfAttendees(String title) {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr[contains(td[2], '" + title + "')]/td[4]")
        );
        if (elements.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(elements.get(0).getText());
    }


    public boolean isAttending(String title) {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr[contains(td[2], '" + title + "')]/td[5]/form/input[@type='checkbox' and @checked='checked']")
        );

        return !elements.isEmpty();
    }

    public void setAttendance(String title, boolean value) {
        boolean alreadyAttending = isAttending(title);
        if ((value && alreadyAttending) || (!value && !alreadyAttending)) {
            return;
        }

        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr[contains(td[2], '" + title + "')]/td[5]/form/input[@type='checkbox']")
        );
        if (elements.isEmpty()) {
            return;
        }

        elements.get(0).click();
        waitForPageToLoad();
    }
}
