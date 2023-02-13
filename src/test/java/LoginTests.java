import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class LoginTests {

    private static final String ACCOUNT_IS_NOT_CREATED_ERROR = "Учетная запись не найдена";

    private final Faker faker = new Faker(new Locale("en"));
    private final CourierSteps steps = new CourierSteps();
    private CourierAccount courierAccount;
    private int courierId;

    @Before
    public void setUp() {
        courierAccount = new CourierAccount(faker.funnyName().name(), faker.internet().password(), faker.name().firstName());
    }

    @Test
//    запрос возвращает правильный код ответа
    public void loginSuccessReturnId() {
        steps.create(courierAccount);
        courierId = steps.getAccountId(courierAccount);
        ValidatableResponse response = steps.login(courierAccount);
        assertThat("Успешный запрос возвращает \"id\": int", response.extract().body().jsonPath().getInt("id"), notNullValue());
    }

    @Test
//    запрос возвращает правильный код ответа
    public void loginIsNotCreatedUserShowError() {
        ValidatableResponse response = steps.login(courierAccount);
        assertEquals("Авторизация под несуществующим пользователем возвращает ошибку", response.extract().body().jsonPath().getString("message"), ACCOUNT_IS_NOT_CREATED_ERROR);
    }

    @After
    public void cleanUp() {
        steps.delete(courierId);
    }
}
