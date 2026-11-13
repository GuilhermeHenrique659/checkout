package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.GetOrderDTO;
import com.example.checkout.order.infrastructure.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GetOrder {
    private final OrderDao orderDao;
    private final OrderHistoryDao orderHistoryDao;

    public GetOrder(OrderDao orderDao, OrderHistoryDao orderHistoryDao) {
        this.orderDao = orderDao;
        this.orderHistoryDao = orderHistoryDao;
    }

    public List<GetOrderDTO.Output> execute(UUID customerId) {
        var rawData = orderDao.queryByCustomerId(customerId);

        var orderIds = rawData.stream().map(OrderDaoDTO.Detail::orderId).collect(Collectors.toSet());
        var historiesByOrder = orderIds.isEmpty()
                ? Map.<UUID, List<OrderHistoryModel>>of()
                : orderHistoryDao.findAllByOrderIdIn(orderIds).stream()
                .collect(Collectors.groupingBy(OrderHistoryModel::getOrderId));

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
                                    .toList(),
                            OrderHistoryChain.follow(order.orderId(), historiesByOrder.getOrDefault(order.orderId(), List.of()))
                                    .stream()
                                    .map(history -> new GetOrderDTO.Output.History(
                                            history.getStatus(),
                                            history.getOccurredAt()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }
}
