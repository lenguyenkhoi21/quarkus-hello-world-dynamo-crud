package com.example.lambda.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class HelloQuarkus {
    private String helloId;
    private String helloName;
    private String helloTitle;
    private Integer helloNumber;
}

