package ru.netology.testmode.test;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()   //спецификация для того, чтобы переиспользовать настройки в разных запросах
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {}
    private static void sendRequest(RegistrationDto user) {   //запрос передачи объекта по спецификации

        given() // "дано"
                .spec(requestSpec)                         // указываем, какую спецификацию используем
                .body(user)                                // передаём в теле объект, который будет преобразован в JSON
        .when()                                            // "когда"
                .post("/api/system/users")            // путь относительный BaseUri, отправляем запрос
        .then()                                            // "ожидаем"
                .statusCode(200);          // код 200 OK
    }

    public static String getRandomLogin() {                //логин

        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {             //пароль

        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {    //принимает на вход "статус"
            // TODO: создать пользователя user используя методы getRandomLogin(), getRandomPassword() и параметр status
            var user = new RegistrationDto(getRandomLogin(),getRandomPassword(),status);
            return  user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            // TODO: объявить переменную registeredUser и присвоить ей значение возвращённое getUser(status).
            // Послать запрос на регистрацию пользователя с помощью вызова sendRequest(registeredUser)
            var registeredUser = getUser(status);      //создал пользователя, записал в переменную
            sendRequest(registeredUser);               //отправляем пользователя в метод регистрации//как метод отработает,
            return registeredUser;                     //возвращается уже зарегестрированный пользователь
        }
    }

    @Value                                    //дата класс user'а
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

}
