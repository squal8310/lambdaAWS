package com.example.repository;

import com.example.model.SalesPoint;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SalesPointRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "sales_points";

    public SalesPointRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public SalesPoint save(SalesPoint salesPoint) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(salesPoint.toAttributeValueMap())
                .build();
        
        dynamoDbClient.putItem(request);
        return salesPoint;
    }

    public Optional<SalesPoint> findById(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(id).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        return response.hasItem() 
            ? Optional.of(SalesPoint.fromAttributeValueMap(response.item()))
            : Optional.empty();
    }

    public List<SalesPoint> findAll() {
        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .build();

        return dynamoDbClient.scan(request).items().stream()
                .map(SalesPoint::fromAttributeValueMap)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(id).build()))
                .build();

        dynamoDbClient.deleteItem(request);
    }
}