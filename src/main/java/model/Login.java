package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String login;
    private String password;

    public Login(CourierAccount account) {
        this.login = account.getLogin();
        this.password = account.getPassword();
    }

    @Override
    public String toString() {
        return String.format("Логин курьера. Логин: %s; Пароль: %s.", login, password);
    }
}
