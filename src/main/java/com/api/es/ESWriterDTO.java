package com.api.es;

import lombok.Data;

import java.util.List;

@Data
public class ESWriterDTO {

    private List<String> indexes;
    private List<String> types;
    private List<ESMessageDTO> documents;
}
