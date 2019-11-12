package com.api.es;

import lombok.Data;

import java.util.List;

@Data
public class ESSearchResult<T> {

    private boolean success;

    private List<T> hitsData;

    private Integer totalHits;

    private String errorMsg;

    private String scrollId;


}