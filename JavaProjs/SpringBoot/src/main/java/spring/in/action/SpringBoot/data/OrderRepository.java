package spring.in.action.SpringBoot.data;

import spring.in.action.SpringBoot.Order;

public interface OrderRepository {
    Order save(Order order);
}
