package com.example.checkout.order.api.dto;

import com.example.checkout.order.application.dto.GetOrderDTO;

import java.util.List;

public record GetOrderResponse(List<GetOrderDTO.Output> orders) {
}
