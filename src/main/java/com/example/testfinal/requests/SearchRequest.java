package com.example.testfinal.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SearchRequest {
    private Map<String, Object> parameters = new HashMap<>();
}
