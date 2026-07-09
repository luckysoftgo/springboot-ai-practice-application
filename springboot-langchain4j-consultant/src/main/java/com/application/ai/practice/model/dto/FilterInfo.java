package com.application.ai.practice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterInfo{
    private String key;
    private Object value;
    private String type;
}
