import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.Order;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class OrderTest {
    private final Steps steps = new Steps();
    private final HashSet<String> colors;
    private ValidatableResponse response;

    public OrderTest() {
        colors = new HashSet<>();
        colors.add("BLACK");
        colors.add("GREY");
    }

    public Object[] testDataForOrder() {
        Faker faker = new Faker(new Locale("en"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().fullAddress();
        String metroStation = faker.address().streetName();
        String phone = faker.phoneNumber().phoneNumber();
        int rentTime = new Random().nextInt(10) + 1;
        String deliveryDate = sdf.format(faker.date().future(3, TimeUnit.DAYS));
        String comment = faker.yoda().quote();

        return new Object[]{
                new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{}),
                new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{"BLACK"}),
                new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{"GREY"}),
                new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{"BLACK", "GREY"})
        };
    }

    @Test
    @Parameters(method = "testDataForOrder")
    public void createWithSetOfColorSuccessful(Order order) {
        System.out.println(order.toString());
        response = steps.orderCreate(order);
        assertThat("Данные создаются с любым параметром color и даже при его отсутствии", response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
        assertThat("Тело ответа содержит \"track\"", response.extract().body().jsonPath().getInt("track"), notNullValue());
    }

}
