package com.example.checkout.order.infrastructure.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Orders the persisted history rows of one order by following the next pointers, from first to current.
 */
public final class OrderHistoryChain {
    private OrderHistoryChain() {
    }

    public static List<OrderHistoryModel> follow(UUID orderId, List<OrderHistoryModel> models) {
        Map<UUID, OrderHistoryModel> byId = models.stream()
                .collect(Collectors.toMap(OrderHistoryModel::getId, Function.identity()));

        var pointedTo = models.stream()
                .map(OrderHistoryModel::getNextId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var head = models.stream()
                .filter(m -> !pointedTo.contains(m.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Order " + orderId + " has no status history head"));

        var chain = new ArrayList<OrderHistoryModel>();
        var visited = new HashSet<UUID>();
        for (var current = head; current != null; current = current.getNextId() == null ? null : byId.get(current.getNextId())) {
            if (!visited.add(current.getId())) {
                throw new IllegalStateException("Order " + orderId + " has a cyclic status history");
            }
            chain.add(current);
        }
        return chain;
    }
}
