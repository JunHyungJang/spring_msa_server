package com.example.orderservice.vo;

import lombok.Data;

@Data
public class RequesOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;

}
