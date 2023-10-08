package com.example.testfinal.model.command.edit;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditPersonCommand {
    private Map<String, Object> parameters;
}
