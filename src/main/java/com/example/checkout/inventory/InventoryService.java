package com.example.checkout.inventory;

import com.example.checkout.inventory.application.ReserveProduct;
import com.example.checkout.inventory.application.dto.ReserveProductDTO;
import com.example.checkout.order.application.OrderInventoryService;
import com.example.checkout.order.application.dto.ReserveItemInventoryServiceDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryService implements OrderInventoryService {
    private final ReserveProduct reserveProduct;

    public InventoryService(ReserveProduct reserveProduct) {
        this.reserveProduct = reserveProduct;
    }

    @Override
    public List<ReserveItemInventoryServiceDTO.Output> reserveItems(ReserveItemInventoryServiceDTO.Input items) {
        var output = reserveProduct.execute(new ReserveProductDTO.Input(
                items.items().stream().map(item -> new ReserveProductDTO.ProductItem(item.itemId(), item.quantity())).toList()
        ));

        return output.productItems().stream()
                .map(ReserveProductDTO.ProductItem::productId)
                .map(ReserveItemInventoryServiceDTO.Output::new)
                .toList();
    }
}
