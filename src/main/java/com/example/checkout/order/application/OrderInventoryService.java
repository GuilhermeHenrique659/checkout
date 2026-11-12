package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.ReserveItemInventoryServiceDTO;

import java.util.List;

public interface OrderInventoryService {
    public List<ReserveItemInventoryServiceDTO.Output> reserveItems(ReserveItemInventoryServiceDTO.Input items);
}
