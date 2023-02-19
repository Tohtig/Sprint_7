import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class LoginTest {

    private static final String ACCOUNT_ERROR = "Учетная запись не найдена";

    private final Faker faker = new Faker(new Locale("en"));
    private final Steps steps = new Steps();
    private CourierAccount account;
    private List<CourierAccount> testData;

    @Before
    public void setUp() {
        testData = new ArrayList<>();
        account = new CourierAccount(faker.funnyName().name(), faker.internet().password(), faker.name().firstName());
        testData.add(account);
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void loginSuccessReturnId() {
        steps.create(account);
        ValidatableResponse response = steps.login(account);
        assertThat("Успешный запрос возвращает \"id\": int", response.extract().body().jsonPath().getInt("id"), notNullValue());
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void loginIsNotCreatedUserShowError() {
        ValidatableResponse response = steps.login(account);
        assertEquals("Авторизация под несуществующим пользователем должна вернуть ошибку", response.extract().body().jsonPath().getString("message"), ACCOUNT_ERROR);
    }


    @Test
    @DisplayName("система вернет ошибку, если неправильно указан логин или пароль")
    public void loginIncorrectAccountShowError() {
        steps.create(account);
        CourierAccount wrongAccount = new CourierAccount(faker.funnyName().name(), account.getPassword(), account.getFirstName());
        testData.add(wrongAccount);
        ValidatableResponse response = steps.login(wrongAccount);
        assertEquals("Авторизация с неверным логином или паролем должна вернуть ошибку", response.extract().body().jsonPath().getString("message"), ACCOUNT_ERROR);
    }

    @Test
    @DisplayName("Если одного из полей нет, запрос возвращает ошибку.")
    @Description("У портала баг. Java-Postman запрос падает с таймаутом если нет логина")
    @Issue("BUG-001")
    public void createFieldlessReturnsError() {
        steps.create(account);
        CourierAccount wrongAccount = new CourierAccount();
        testData.add(wrongAccount);
        wrongAccount.setPassword(account.getPassword());
        assertThat("Пароль обязательное поле, ждем 400 код", steps.login(wrongAccount).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        wrongAccount = new CourierAccount();
        wrongAccount.setLogin(account.getLogin());
        assertThat("Логин обязательное поле, ждем 400 код", steps.login(wrongAccount).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @After
    public void cleanUp() {
        steps.delete(testData);
    }
}
