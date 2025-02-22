package com.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.model.SalesPoint;
import com.example.service.SalesPointService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SalesPointLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
    private final SalesPointService salesPointService;
    private final ObjectMapper objectMapper;

    public SalesPointLambdaHandler(SalesPointService salesPointService, ObjectMapper objectMapper) {
        this.salesPointService = salesPointService;
        this.objectMapper = objectMapper;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String httpMethod = input.getHttpMethod();
            String path = input.getPath();
            String body = input.getBody();

            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setHeaders(Map.of("Content-Type", "application/json"));

            switch (httpMethod) {
                case "POST":
                    SalesPoint newSalesPoint = objectMapper.readValue(body, SalesPoint.class);
                    SalesPoint created = salesPointService.createSalesPoint(newSalesPoint);
                    return response
                            .withStatusCode(201)
                            .withBody(objectMapper.writeValueAsString(created));

                case "GET":
                    if (path.matches("/sales-points/\\w+")) {
                        String id = path.substring(path.lastIndexOf('/') + 1);
                        SalesPoint salesPoint = salesPointService.getSalesPoint(id);
                        return response
                                .withStatusCode(200)
                                .withBody(objectMapper.writeValueAsString(salesPoint));
                    } else {
                        List<SalesPoint> allSalesPoints = salesPointService.getAllSalesPoints();
                        return response
                                .withStatusCode(200)
                                .withBody(objectMapper.writeValueAsString(allSalesPoints));
                    }

                case "PUT":
                    String updateId = path.substring(path.lastIndexOf('/') + 1);
                    SalesPoint updateSalesPoint = objectMapper.readValue(body, SalesPoint.class);
                    SalesPoint updated = salesPointService.updateSalesPoint(updateId, updateSalesPoint);
                    return response
                            .withStatusCode(200)
                            .withBody(objectMapper.writeValueAsString(updated));

                case "DELETE":
                    String deleteId = path.substring(path.lastIndexOf('/') + 1);
                    salesPointService.deleteSalesPoint(deleteId);
                    return response.withStatusCode(204);

                default:
                    return response
                            .withStatusCode(405)
                            .withBody("{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}