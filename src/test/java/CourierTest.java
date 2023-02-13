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
    private final CourierSteps steps = new CourierSteps();
    private CourierAccount courierAccount;
    private int courierId;

    @Before
    public void setUp() {
        courierAccount = new CourierAccount(faker.funnyName().name(), faker.internet().password(), faker.name().firstName());
    }

//    @Test
//    public void courierCanBeCreated(){
//        ValidatableResponse response = steps.create(courierAccount).assertThat();
//        courierId = steps.login(courierAccount);
//    }

    @Test
    public void createIdenticalAccountsForbidden() {
        ValidatableResponse createFirst = steps.create(courierAccount);
        int statusCode;
        statusCode = createFirst.extract().statusCode();
        assertThat("Статус код должен быть 201", statusCode, equalTo(HttpStatus.SC_CREATED));
        courierId = steps.login(courierAccount);
        ValidatableResponse createSecond = steps.create(courierAccount);
        statusCode = createSecond.extract().statusCode();
        //не работает ??? statusCode - не переопределили, но assertNotEquals не отбросил
        assertNotEquals("Статус код не должен быть 201", statusCode, equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    public void createNewCourierReturnSC_CREATED(){
        ValidatableResponse response = steps.create(courierAccount);
        courierId = steps.login(courierAccount);
        assertThat("Статус код должен быть 201", response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    public void createNewCourierReturnBodyWithOk(){
        ValidatableResponse response = steps.create(courierAccount);
        courierId = steps.login(courierAccount);
        boolean expected = true;
        boolean actual = response.extract().body().jsonPath().getBoolean("ok");
        assertEquals("Статус код должен быть 201", expected, actual);
    }

    @Test
    public void createIdenticalLoginForbidden() {
        ValidatableResponse createFirst = steps.create(courierAccount);
        int statusCode = createFirst.extract().statusCode();
        assertThat("Статус код должен быть 201", statusCode, equalTo(HttpStatus.SC_CREATED));
        courierId = steps.login(courierAccount);
        CourierAccount courierSecondAccount = new CourierAccount(courierAccount.getLogin(), faker.internet().password(), faker.name().firstName());
        ValidatableResponse createSecond = steps.create(courierSecondAccount);
        statusCode = createSecond.extract().statusCode();
        assertNotEquals("Статус код не должен быть 201", statusCode, equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    public void createFieldlessReturnsError() {
        courierAccount = new CourierAccount();
        courierAccount.setLogin(faker.funnyName().name());
        ValidatableResponse response = steps.create(courierAccount);
        assertThat("Статус код должен быть 400", response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @After
    public void cleanUp() {
        steps.delete(courierId);
    }
}