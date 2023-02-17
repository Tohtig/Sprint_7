import io.restassured.response.Response;
import model.Orders;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrdersTest {
    private final Steps steps = new Steps();

    @Test
//    В тело ответа возвращается список заказов
    public void ordersReturnsOrderList() {
        Response response = steps.orders();
        assertThat("В тело ответа возвращается список заказов", response.as(Orders.class).getOrders(), notNullValue());
    }
}
