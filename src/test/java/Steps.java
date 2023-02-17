import client.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import model.Login;
import model.Order;

public class Steps extends BaseHttpClient {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru/api/v1";

    @Step("Получение id курьера")
    public int getAccountId(CourierAccount account) {
        Login body = new Login(account);
        return doPostRequest(baseUrl + "/courier/login", body).extract().body().jsonPath().getInt("id");
    }

    @Step("Авторизация курьера в системе")
    public ValidatableResponse login(CourierAccount account) {
        Login body = new Login(account);
        return doPostRequest(baseUrl + "/courier/login", body);
    }

    @Step("Создание курьера")
    public ValidatableResponse create(CourierAccount account) {
        return doPostRequest(baseUrl + "/courier", account);
    }

    @Step("Удаление курьера")
    public void delete(int id) {
        doDeleteRequest(baseUrl + String.format("/courier/%d", id));
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
