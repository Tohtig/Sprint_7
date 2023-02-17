import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class LoginTests {

    private static final String ACCOUNT_ERROR = "Учетная запись не найдена";

    private final Faker faker = new Faker(new Locale("en"));
    private final Steps steps = new Steps();
    private CourierAccount account;
    private int courierId;

    @Before
    public void setUp() {
        account = new CourierAccount(faker.funnyName().name(), faker.internet().password(), faker.name().firstName());
    }

    @Test
//    запрос возвращает правильный код ответа
    public void loginSuccessReturnId() {
        steps.create(account);
        courierId = steps.getAccountId(account);
        ValidatableResponse response = steps.login(account);
        assertThat("Успешный запрос возвращает \"id\": int", response.extract().body().jsonPath().getInt("id"), notNullValue());
    }

    @Test
//    если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    public void loginIsNotCreatedUserShowError() {
        ValidatableResponse response = steps.login(account);
        assertEquals("Авторизация под несуществующим пользователем должна вернуть ошибку", response.extract().body().jsonPath().getString("message"), ACCOUNT_ERROR);
    }


    @Test
//    система вернет ошибку, если неправильно указан логин или пароль
    public void loginIncorrectAccountShowError() {
        steps.create(account);
        courierId = steps.getAccountId(account);
        CourierAccount wrongAccount = new CourierAccount(faker.funnyName().name(), account.getPassword(), account.getFirstName());
        ValidatableResponse response = steps.login(wrongAccount);
        assertEquals("Авторизация с неверным логином или паролем должна вернуть ошибку", response.extract().body().jsonPath().getString("message"), ACCOUNT_ERROR);
    }

    @Test
//    Если одного из полей нет, запрос возвращает ошибку.
//    У портала баг. Java\Postman запрос падает с таймаутом если нет логина
    public void createFieldlessReturnsError() {
        steps.create(account);
        courierId = steps.getAccountId(account);
        CourierAccount wrongAccount = new CourierAccount();
        wrongAccount.setPassword(account.getPassword());
        assertThat("Пароль обязательное поле, ждем 400 код", steps.login(wrongAccount).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        wrongAccount = new CourierAccount();
        wrongAccount.setLogin(account.getLogin());
        assertThat("Логин обязательное поле, ждем 400 код", steps.login(wrongAccount).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @After
    public void cleanUp() {
        steps.delete(courierId);
    }
}
