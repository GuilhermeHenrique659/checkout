package com.example.checkout.order.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateANewOder() throws Exception {
        String request = """
                {
                    "customerId": "550e8400-e29b-41d4-a716-446655440000",
                    "items": [
                        {
                            "id": "550e8400-e29b-41d4-a716-446655440001",
                            "quantity": 2
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldTryCreateNewOrder() throws Exception {
        String request = """
                {
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.detail").exists());

    }

    @Test
    void shouldGetCustomerOrdersWithStatusHistory() throws Exception {
        var customerId = UUID.randomUUID();
        var productId = "550e8400-e29b-41d4-a716-446655440001";
        String request = """
                {
                    "customerId": "%s",
                    "items": [
                        {
                            "id": "%s",
                            "quantity": 2
                        }
                    ]
                }
                """.formatted(customerId, productId);

        var created = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn();
        var orderId = com.jayway.jsonpath.JsonPath.<String>read(created.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/api/orders/{customerId}", customerId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.orders.length()").value(1))
                .andExpect(jsonPath("$.orders[0].id").value(orderId))
                .andExpect(jsonPath("$.orders[0].total").isNumber())
                .andExpect(jsonPath("$.orders[0].products.length()").value(1))
                .andExpect(jsonPath("$.orders[0].products[0].id").value(productId))
                .andExpect(jsonPath("$.orders[0].products[0].name").isString())
                .andExpect(jsonPath("$.orders[0].products[0].price").isNumber())
                .andExpect(jsonPath("$.orders[0].products[0].quantity").value(2))
                .andExpect(jsonPath("$.orders[0].history").isArray())
                .andExpect(jsonPath("$.orders[0].history[0].status").value("PENDING"))
                .andExpect(jsonPath("$.orders[0].history[0].occurredAt").exists());
    }
}
