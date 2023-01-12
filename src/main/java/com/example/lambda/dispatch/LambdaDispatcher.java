package com.example.lambda.dispatch;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Singleton;

@Singleton
public class LambdaDispatcher {

    private final Instance<ILambdaProcess> functions;

    public LambdaDispatcher(@Any Instance<ILambdaProcess> functions) {
        this.functions = functions;
    }

    public String dispatch(String resourceId, String httpMethod, String message) throws Exception {
        for (ILambdaProcess f : functions) {
            if (resourceId.equals(f.getResourceId()) && httpMethod.equals(f.getHttpMethod().name())) {
                return f.process(message);
            }
        }
        return "";
    }
}
