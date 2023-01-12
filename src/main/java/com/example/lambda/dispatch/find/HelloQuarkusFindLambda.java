package com.example.lambda.dispatch.find;

import com.example.lambda.dispatch.AbstractDynamoLambda;
import com.example.lambda.dispatch.ILambdaProcess;
import com.example.lambda.model.HelloQuarkus;
import com.example.lambda.service.HelloQuarkusDaoService;
import com.example.lambda.util.Constant;
import com.example.lambda.util.HttpMethod;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import java.util.HashMap;

@Singleton
public class HelloQuarkusFindLambda extends AbstractDynamoLambda implements ILambdaProcess {

    @Inject
    public HelloQuarkusFindLambda(Jsonb jsonb, HelloQuarkusDaoService helloQuarkusDaoService) {
        super(jsonb, helloQuarkusDaoService);
    }

    @Override
    public String process(String input) {
        var params = jsonb.fromJson(input, HashMap.class);
        if (!params.isEmpty()) {
            var helloId = (String) params.get("hello_id");
            if (helloId != null) {
                var entity = HelloQuarkus.builder().helloId(helloId).build();
                var result = helloQuarkusDaoService.findById(entity);
                if (result != null) return jsonb.toJson(result);
            }
        }
        return "";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getResourceId() {
        return Constant.HELLO_QUARKUS_PATH;
    }
}
