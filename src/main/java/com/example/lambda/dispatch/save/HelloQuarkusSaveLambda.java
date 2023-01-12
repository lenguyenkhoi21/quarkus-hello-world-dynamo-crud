package com.example.lambda.dispatch.save;

import com.example.lambda.dispatch.AbstractDynamoLambda;
import com.example.lambda.dispatch.ILambdaProcess;
import com.example.lambda.model.HelloQuarkus;
import com.example.lambda.service.HelloQuarkusDaoService;
import com.example.lambda.util.Constant;
import com.example.lambda.util.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.eventstream.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;

@Singleton
public class HelloQuarkusSaveLambda extends AbstractDynamoLambda implements ILambdaProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloQuarkusSaveLambda.class);

    @Inject
    public HelloQuarkusSaveLambda(Jsonb jsonb, HelloQuarkusDaoService helloQuarkusDaoService) {
        super(jsonb, helloQuarkusDaoService);
    }

    @Override
    public String process(String input) {
        try {
            var entity = jsonb.fromJson(input, HelloQuarkus.class);
            var status = helloQuarkusDaoService.save(entity);
            return String.format("Process with status %s", status);
        } catch (Exception e) {
            LOGGER.info("Invalid content");
            return "Can't save entity";
        }
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getResourceId() {
        return Constant.HELLO_QUARKUS_PATH;
    }
}
