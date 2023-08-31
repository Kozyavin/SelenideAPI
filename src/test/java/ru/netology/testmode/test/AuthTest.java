package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.test.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.test.DataGenerator.Registration.getUser;
import static ru.netology.testmode.test.DataGenerator.getRandomLogin;
import static ru.netology.testmode.test.DataGenerator.getRandomPassword;


class AuthTest {

    @BeforeEach
    void setup() {
            open("http://localhost:9999");
    }

    @Test
    //вход в ЛК зарегестрированного пользователя
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $(".heading")
                .shouldHave(text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    //попытка входа в ЛК НЕ ЗАРЕГЕСТРИРОВАННОГО ПОЛЬЗОВАТЕЛЯ. Просто создан user.
    void shouldGetErrorIfNotRegisteredUser() {

        var notRegisteredUser = getUser("active");
          $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
          $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
          $(".button").click();
          $("[data-test-id = 'error-notification'] .notification__content")
                          .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                          .shouldBe(Condition.visible);                               
    }
    @Test
    //Вход в ЛК зарегестрированного ,но Заблокированного пользователя
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {

        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $(".button").click();
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }
    @Test
    //пользователь зарегестрирован, но пытается войти с неверным логином
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

   @Test
    //пользователь зарегестрирован, но пытается войти с неверным паролем
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $(".button").click();
         $("[data-test-id = 'error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);  
    }
}
