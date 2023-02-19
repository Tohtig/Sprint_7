package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate; // date ?
    private String comment;
    private String[] color;

    @Override
    public String toString() {
        return String.format("Order. firstName: %s; lastName: %s; address: %s; metroStation: %s; phone: %s; " +
                        "rentTime: %d; deliveryDate: %s; comment: %s; color: %s.", firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate, comment, Arrays.toString(color));
//        return String.format("Order. firstName: %s; colors: %s;", firstName, Arrays.toString(colors));
    }
}
