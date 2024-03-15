package com.example.task5.instance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddPropDto {
    String key;
    String value;
    String name;
}
