package com.example.lambda.service;


import com.example.lambda.model.HelloQuarkus;
import com.example.lambda.util.Constant;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Singleton
public class HelloQuarkusDaoService {
    private final DynamoDbClient dynamoDbClient;

    @Inject
    public HelloQuarkusDaoService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public String save(HelloQuarkus helloQuarkus) {
        var itemValues = new HashMap<String, AttributeValue>();
        itemValues.put("hello_id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
        itemValues.put("hello_name", AttributeValue.builder().s(helloQuarkus.getHelloName()).build());
        itemValues.put("hello_title", AttributeValue.builder().s(helloQuarkus.getHelloTitle()).build());
        itemValues.put("hello_number", AttributeValue.builder().n(helloQuarkus.getHelloNumber().toString()).build());
        PutItemRequest dbRequest = PutItemRequest.builder()
                                                 .tableName(Constant.HELLO_QUARKUS_TABLE)
                                                 .item(itemValues)
                                                 .build();
        PutItemResponse response = dynamoDbClient.putItem(dbRequest);
        int statusCode = response.sdkHttpResponse().statusCode();
        return String.format("%s", statusCode);
    }

    public HelloQuarkus findById(HelloQuarkus helloQuarkus) {
        if (Objects.nonNull(helloQuarkus.getHelloId())) {
            var itemValues = new HashMap<String, AttributeValue>();
            itemValues.put("hello_id", AttributeValue.builder().s(helloQuarkus.getHelloId()).build());

            GetItemRequest getItemRequest = GetItemRequest.builder()
                                                          .tableName(Constant.HELLO_QUARKUS_TABLE)
                                                          .key(itemValues)
                                                          .build();

            GetItemResponse response = dynamoDbClient.getItem(getItemRequest);

            var item = response.item();
            return HelloQuarkus.builder()
                               .helloId(item.get("hello_id").s())
                               .helloName(item.get("hello_name").s())
                               .helloTitle(item.get("hello_title").s())
                               .helloNumber(Integer.parseInt(item.get("hello_number").n()))
                               .build();
        }
        return null;
    }
}
