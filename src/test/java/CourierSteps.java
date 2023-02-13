import client.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import model.Login;

public class CourierSteps extends BaseHttpClient {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru/api/v1";

    @Step("Получение id курьера")
    public int getAccountId(CourierAccount account){
        Login body = new Login(account);
        return doPostRequest(baseUrl + "/courier/login", body).extract().body().jsonPath().getInt("id");
    }

    @Step("Авторизация курьера в системе")
    public ValidatableResponse login(CourierAccount account){
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

}
