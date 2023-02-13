import client.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CourierAccount;
import org.jetbrains.annotations.NotNull;

public class CourierSteps extends BaseHttpClient {
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru/api/v1";

    @Step("Проверка авторизации курьера в системе")
    public int login(@NotNull CourierAccount account){
        String body = String.format("{\r\n    \"login\": \"%s\",\r\n    \"password\": \"%s\"\r\n}", account.getLogin(), account.getPassword());
        return doPostRequest(baseUrl + "/courier/login", body).extract().body().jsonPath().getInt("id");
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
