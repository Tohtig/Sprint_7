package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourierAccount {
    private String login;
    private String password;
    private String firstName;

    @Override
    public String toString() {
        return String.format("Аккаунт курьера. Логин: %s; Пароль: %s; Имя: %s.", login, password, firstName);
    }
}
