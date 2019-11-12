package com.api.es;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetryException extends RuntimeException {

    private String code;

    private String msg;
}
