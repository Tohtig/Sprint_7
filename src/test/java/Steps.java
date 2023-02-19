import client.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import model.Login;
import model.Order;
import org.apache.http.HttpStatus;

import java.util.List;

public class Steps extends BaseHttpClient {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru/api/v1";

    @Step("Авторизация курьера в системе")
    public ValidatableResponse login(CourierAccount account) {
        Login body = new Login(account);
        return doPostRequest(baseUrl + "/courier/login", body);
    }

    @Step("Создание курьера")
    public ValidatableResponse create(CourierAccount account) {
        return doPostRequest(baseUrl + "/courier", account);
    }

    @Step("Удаление курьеров")
    public void delete(List<CourierAccount> accounts) {
        ValidatableResponse loginResp;
        if(!accounts.isEmpty()) {
            for (CourierAccount account: accounts) {
                loginResp = login(account);
                if (loginResp.extract().statusCode() == HttpStatus.SC_OK) {
                    doDeleteRequest(baseUrl + String.format("/courier/%d", loginResp.extract().body().jsonPath().getInt("id")));
                }
            }
        }
    }

    @Step("Создание заказа")
    public ValidatableResponse orderCreate(Order order) {
        return doPostRequest(baseUrl + "/orders", order);
    }

    @Step("Список заказов")
    public Response orders() {
        return doGetRequest(baseUrl + "/orders");
    }
}
