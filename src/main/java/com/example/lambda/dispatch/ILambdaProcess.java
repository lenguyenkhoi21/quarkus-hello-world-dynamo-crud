package com.example.lambda.dispatch;

import com.example.lambda.util.HttpMethod;

public interface ILambdaProcess {
    String process(String input);
    HttpMethod getHttpMethod();
    String getResourceId();
}
