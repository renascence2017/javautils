package com.api.reflection.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Service("apiService")
@Slf4j
public class APIService {

    @Autowired
    private InterfaceMapper interfaceMapper;

    @Autowired
    private JsonMappingService jsonMappingService;

    @Autowired
    ResultDetailMapper resultDetailMapper;

    @Autowired
    ApiRouteHelperService apiRouteHelperService;

    private static ThreadLocal<XmanDataInterfaceDO> interfaceDOThreadLocal = new ThreadLocal<>();

    /**
     * 异常统一捕获, 检查是否设置RequestNo 保存日志
     *
     * @param apiRequestService
     * @param apiRequestDTO
     * @return
     */
    public RouteResponseDTO executeApi(String apiRequestService, APIRequestDTO apiRequestDTO) {
        RouteResponseDTO routeResponseDTO;
        long start = System.currentTimeMillis();
        try {
            routeResponseDTO = execute(apiRequestService, apiRequestDTO);
        } catch (Exception e) {
            log.warn("executeApi:", e);
            routeResponseDTO = new RouteResponseDTO(SYSTEM_ERR.getCode());
        }
        if (StringUtils.isEmpty(routeResponseDTO.getRequestNo())) {
            routeResponseDTO.setRequestNo(apiRequestDTO.getRequestNo());
        }
        long end = System.currentTimeMillis();
        apiRouteHelperService.saveResultToDB(apiRequestDTO.getUserId(), apiRequestDTO.getRequestInfo(),
                interfaceDOThreadLocal.get(), routeResponseDTO, String.valueOf(end - start), "");
        interfaceDOThreadLocal.remove();
        return routeResponseDTO;
    }

    public RouteResponseDTO execute(String apiRequestService, APIRequestDTO apiRequestDTO) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        RouteResponseDTO result;
        if (StringUtils.isEmpty(apiRequestService)) {
            return new RouteResponseDTO(REQUEST_PARAM_EMPTY.getCode());
        }
        String[] requestLocation = apiRequestService.split("-");
        XmanDataInterfaceDO xmanDataInterfaceDO = interfaceMapper.selectById(Long.valueOf(requestLocation[1]));
        if (null == xmanDataInterfaceDO) {
            return new RouteResponseDTO(SERVICE_NOT_EXIST.getCode());
        }
        interfaceDOThreadLocal.set(xmanDataInterfaceDO);
        int mockSwitch = xmanDataInterfaceDO.getMockSwitch();
        String mockResult = xmanDataInterfaceDO.getMock();

        String className = xmanDataInterfaceDO.getClassName();
        String methodName = xmanDataInterfaceDO.getMethodName();
        String callMode = xmanDataInterfaceDO.getCallMode();
        //核对一下参数和请求ID是否一致 jsonMapping模式不核对
        if (!StringUtils.equals(callMode, "jsonMapping") && !StringUtils.equals(requestLocation[0], methodName)) {
            return new RouteResponseDTO(REQUEST_PATH_ERROR.getCode());
        }
        Object service = SpringUtil.getBean(className);

        Map<String, Object> bodyParam = BeanUtil.json2map(apiRequestDTO.getRequestInfo());

        log.info("API-" + callMode + ", 服务: " + service.getClass().getTypeName() + ", 方法: " + methodName + ", 参数: " + apiRequestDTO.getRequestInfo());

        if ("jsonMapping".equals(callMode)) {
            result = jsonMappingService.callByJsonMappingService((ProviderSendInterface) service, xmanDataInterfaceDO, JSONObject.parseObject(apiRequestDTO.getRequestInfo()));
        } else {
            if (mockSwitch == 1 && StringUtils.isNotEmpty(mockResult)) {
                return JSONObject.parseObject(mockResult, RouteResponseDTO.class);
            }
            result = apiRouteHelperService.callByReflect(service, methodName, bodyParam);
        }
        if (result == null) {
            return new RouteResponseDTO(SOURCE_FORBIDDEN.getCode());
        }

        return result;

    }


}
