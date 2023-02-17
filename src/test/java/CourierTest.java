import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class CourierTest {
    private final Faker faker = new Faker(new Locale("en"));
    private final Steps steps = new Steps();
    private CourierAccount account;
    private int courierId;

    @Before
    public void setUp() {
        account = new CourierAccount(faker.funnyName().name(), faker.internet().password(), faker.name().firstName());
    }

    @Test
//    нельзя создать двух одинаковых курьеров
    public void createIdenticalAccountsForbidden() {
        ValidatableResponse createFirst = steps.create(account);
        int statusCode;
        statusCode = createFirst.extract().statusCode();
        assertThat("Курьер создан. Код 201", statusCode, equalTo(HttpStatus.SC_CREATED));
        courierId = steps.getAccountId(account);
        ValidatableResponse createSecond = steps.create(account);
        statusCode = createSecond.extract().statusCode();
        assertNotEquals("Статус код не должен быть 201", statusCode, equalTo(HttpStatus.SC_CREATED));
    }

    @Test
//    запрос возвращает правильный код ответа
    public void createNewCourierReturnSC_CREATED() {
        ValidatableResponse response = steps.create(account);
        courierId = steps.getAccountId(account);
        assertThat("Статус код должен быть 201", response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
    }

    @Test
//    успешный запрос возвращает ok: true
    public void createNewCourierReturnBodyWithOk() {
        ValidatableResponse response = steps.create(account);
        courierId = steps.getAccountId(account);
        boolean expected = true;
        boolean actual = response.extract().body().jsonPath().getBoolean("ok");
        assertEquals("Успешный запрос возвращает ok: true", expected, actual);
    }

    @Test
//    если создать пользователя с логином, который уже есть, возвращается ошибка
    public void createIdenticalLoginForbidden() {
        ValidatableResponse createFirst = steps.create(account);
        int statusCode = createFirst.extract().statusCode();
        assertThat("Создали первого курьера. Код 201", statusCode, equalTo(HttpStatus.SC_CREATED));
        courierId = steps.getAccountId(account);
        CourierAccount courierSecondAccount = new CourierAccount(account.getLogin(), faker.internet().password(), faker.name().firstName());
        ValidatableResponse createSecond = steps.create(courierSecondAccount);
        statusCode = createSecond.extract().statusCode();
        assertNotEquals("Статус код не должен быть 201", statusCode, equalTo(HttpStatus.SC_CREATED));
    }

    @Test
//    если одного из полей нет, запрос возвращает ошибку
    public void createFieldlessReturnsError() {
        account = new CourierAccount();
        account.setLogin(faker.funnyName().name());
        account.setFirstName(faker.name().firstName());
        assertThat("Пароль обязательное поле, ждем 400 код", steps.create(account).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        account = new CourierAccount();
        account.setPassword(faker.internet().password());
        account.setFirstName(faker.name().firstName());
        assertThat("Логин обязательное поле, ждем 400 код", steps.create(account).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        account = new CourierAccount();
        account.setLogin(faker.funnyName().name());
        account.setPassword(faker.internet().password());
        assertThat("Имя обязательное поле, ждем 400 код", steps.create(account).extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @After
    public void cleanUp() {
        steps.delete(courierId);
    }
}