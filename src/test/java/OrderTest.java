import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.Order;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;

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
        return new Object[]{
                new Order("anna", "hanna", "7998 street", "metro", "+7734892742375", 5, "2020-06-06", "comment", new String[]{}),
                new Order("anna", "hanna", "7998 street", "metro", "+7734892742375", 5, "2020-06-06", "comment", new String[]{"BLACK"}),
                new Order("anna", "hanna", "7998 street", "metro", "+7734892742375", 5, "2020-06-06", "comment", new String[]{"GREY"}),
                new Order("anna", "hanna", "7998 street", "metro", "+7734892742375", 5, "2020-06-06", "comment", new String[]{"BLACK", "GREY"})
        };
    }

    @Test
    @Parameters(method = "testDataForOrder")
    public void createWithSetOfColorSuccessful(Order order) {
        System.out.println(order.toString());
        if (order.getColor().length > 1 && colors.containsAll(Arrays.asList(order.getColor()))) {
            response = steps.orderCreate(order);
            assertThat("Можно указать оба цвета: BLACK и GRAY", response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
        } else if (order.getColor().length == 1 && colors.containsAll(Arrays.asList(order.getColor()))) {
            response = steps.orderCreate(order);
            assertThat("Можно указать один из цветов: BLACK или GRAY", response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
        } else if (order.getColor().length == 0) {
            response = steps.orderCreate(order);
            assertThat("Можно совсем не указывать цвет", response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
        }
        assertThat("Тело ответа содержит \"track\"", response.extract().body().jsonPath().getInt("track"), notNullValue());
    }

}
