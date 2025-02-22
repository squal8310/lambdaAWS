package com.example.model;

import lombok.Data;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.Map;
import java.util.HashMap;

@Data
public class SalesPoint {
    private String id;
    private String name;
    private String address;
    private String status;

    public Map<String, AttributeValue> toAttributeValueMap() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(id).build());
        item.put("name", AttributeValue.builder().s(name).build());
        item.put("address", AttributeValue.builder().s(address).build());
        item.put("status", AttributeValue.builder().s(status).build());
        return item;
    }

    public static SalesPoint fromAttributeValueMap(Map<String, AttributeValue> item) {
        SalesPoint salesPoint = new SalesPoint();
        salesPoint.setId(item.get("id").s());
        salesPoint.setName(item.get("name").s());
        salesPoint.setAddress(item.get("address").s());
        salesPoint.setStatus(item.get("status").s());
        return salesPoint;
    }
}