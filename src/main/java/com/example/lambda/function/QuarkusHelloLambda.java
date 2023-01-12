package com.example.lambda.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.lambda.dispatch.LambdaDispatcher;
import com.example.lambda.util.Constant;
import com.example.lambda.util.HttpMethod;
import software.amazon.awssdk.http.HttpStatusCode;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import java.util.HashMap;

public class QuarkusHelloLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final Jsonb jsonb;

    private final LambdaDispatcher dispatcher;

    @Inject
    public QuarkusHelloLambda(Jsonb jsonb, LambdaDispatcher dispatch) {
        this.jsonb = jsonb;
        this.dispatcher = dispatch;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiRequest, Context context) {
        var resourceId = apiRequest.getResource();
        var httpMethod = apiRequest.getHttpMethod();
        var body = "";

        if (HttpMethod.GET.name().equals(httpMethod)) {
            var queryStringParameter = apiRequest.getQueryStringParameters();

            if (queryStringParameter == null) {
                queryStringParameter = new HashMap<>();
            }
            body = jsonb.toJson(queryStringParameter);
        } else {
            body = apiRequest.getBody();
        }


        var apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        var headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");

        apiGatewayProxyResponseEvent.setHeaders(headers);

        try {
            var response = dispatcher.dispatch(resourceId, httpMethod, body);
            apiGatewayProxyResponseEvent.setBody(response);
            apiGatewayProxyResponseEvent.setStatusCode(HttpStatusCode.OK);
            return apiGatewayProxyResponseEvent;
        } catch (Exception ex) {
            var errorText = Constant.WRONG_REQUEST + ": " + ex.getMessage();
            apiGatewayProxyResponseEvent.setBody("{\"error\":\""+errorText+"\"");
            apiGatewayProxyResponseEvent.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
            return apiGatewayProxyResponseEvent;
        }
    }
}
