package com.api.es;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ESSearchParamDto {

    private List<String> indexes = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private String query;
    private int size;
    private int from;
    private String scrollId;
    private Map<String, String> sort;
}