package com.example.testfinal.model.command.create;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CreatePersonCommand {
    private String type;
    private Map<String, Object> parameters;
}
