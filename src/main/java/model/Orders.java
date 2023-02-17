package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Orders {
    // список групп, потому что в исходном JSON — массив элементов
    private List<OrdersItem> orders;
    private PageInfo pageInfo;
    private List<Station> availableStations;
}
