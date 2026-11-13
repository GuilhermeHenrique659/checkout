package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.GetOrderDTO;
import com.example.checkout.order.infrastructure.persistence.OrderDao;
import com.example.checkout.order.infrastructure.persistence.OrderDaoDTO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetOrder {
    private final OrderDao orderDao;

    public GetOrder(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<GetOrderDTO.Output> execute(UUID customerId) {
        var rawData = orderDao.queryByCustomerId(customerId);

        return rawData.stream()
                .collect(Collectors.groupingBy(OrderDaoDTO.Detail::orderId, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(raw -> {
                    var order = raw.getFirst();

                    return new GetOrderDTO.Output(
                            order.orderId(),
                            order.total(),
                            raw.stream()
                                    .map(item -> new GetOrderDTO.Output.Product(
                                            item.productId(),
                                            item.price(),
                                            item.productName(),
                                            item.quantity()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }
}
