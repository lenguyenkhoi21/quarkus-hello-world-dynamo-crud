package com.example.lambda.dispatch;

import com.example.lambda.service.HelloQuarkusDaoService;

import javax.json.bind.Jsonb;

public class AbstractDynamoLambda {

    protected final HelloQuarkusDaoService helloQuarkusDaoService;
    protected final Jsonb jsonb;

    public AbstractDynamoLambda(Jsonb jsonb, HelloQuarkusDaoService helloQuarkusDaoService) {
        this.jsonb = jsonb;
        this.helloQuarkusDaoService = helloQuarkusDaoService;
    }



}
