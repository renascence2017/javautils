package com.api.reflection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
@Api(tags = "api调用")
@Slf4j
public class ApiController {

    @Autowired
    private APIService apiService;

    @ApiOperation("apiRequestService")
    @PostMapping("/{apiRequestService}")
    public RouteResponseDTO execute(@RequestBody APIRequestDTO apiRequestDTO, @PathVariable("apiRequestService") String apiRequestService) {
        return apiService.executeApi(apiRequestService, apiRequestDTO);
    }

}
