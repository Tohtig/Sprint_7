package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdersItem {
    private int id;
    private String courierId;
    //    private Order order;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private int track;
    private String[] color;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private int status;
}
