package com.project.personal;

import io.qameta.allure.junit4.DisplayName;
import net.datafaker.Faker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.project.personal.pageObject.LoginPage;
import com.project.personal.pageObject.ProfilePage;
import com.project.personal.pageObject.RegistrationPage;
import com.project.personal.utils.Url;

public class RegistrationTests extends BaseTest {

    LoginPage loginPage;
    RegistrationPage registrationPage;
    ProfilePage profilePage;

    Faker faker;
    String name;
    String email;
    String correctPassword;
    String wrongPassword;

    @Before
    public void setUpTestData() {
        faker = new Faker();

        name = faker.name().fullName();
        email = faker.internet().emailAddress();
        correctPassword = faker.internet().password(6, 10);
        wrongPassword = faker.lorem().characters(1, 5);
    }

    @Test
    @DisplayName("Регистрация с корректным паролем")
    public void testRegistrationWithCorrectPasswordSuccess() {

        homePage.waitForPersonalProfileButton();
        homePage.enterPersonalProfile();

        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        loginPage.clickRegistrationLink();

        registrationPage = new RegistrationPage(webDriver);
        registrationPage.waitForPageLoad();
        registrationPage.fillInRegistrationForm(name, email, correctPassword);

        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        Assert.assertEquals(Url.LOGIN_URL, webDriver.getCurrentUrl());

        loginPage.fillInUserData(email, correctPassword);
        loginPage.clickEnterButton();

        homePage.waitForPersonalProfileButton();
        homePage.enterPersonalProfile();

        profilePage = new ProfilePage(webDriver);
        profilePage.waitForPageLoad();
        Assert.assertEquals(name, profilePage.getNameText());
        Assert.assertEquals(email, profilePage.getEmailText());
    }

    @Test
    @DisplayName("Регистрация с коротким паролем")
    public void testRegistrationWithShortPasswordError() {
        String testName = faker.name().fullName();
        String testEmail = faker.internet().emailAddress();

        homePage.waitForPersonalProfileButton();
        homePage.enterPersonalProfile();

        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        loginPage.clickRegistrationLink();

        registrationPage = new RegistrationPage(webDriver);
        registrationPage.waitForPageLoad();
        registrationPage.fillInRegistrationForm(testName, testEmail, wrongPassword);

        Assert.assertEquals("Некорректный пароль", registrationPage.getPasswordFieldErrorText());
        Assert.assertEquals(Url.REGISTER_URL, webDriver.getCurrentUrl());
    }
}
