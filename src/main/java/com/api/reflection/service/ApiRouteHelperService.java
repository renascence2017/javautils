package com.api.reflection.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * APIService RouteService 辅助类, 提供两者通用的函数
 */
@Service("apiRouteCommonService")
@Slf4j
public class ApiRouteHelperService {


    @Autowired
    ResultDetailMapper resultDetailMapper;


    public RouteResponseDTO callByReflect(Object service, String methodName, Map<String, Object> bodyParam)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object result;
        Method method = service.getClass().getMethod(methodName, Map.class);
        result = method.invoke(service, bodyParam);
        return (RouteResponseDTO) result;
    }

    /**
     * 异步保存返回结果
     *
     * @param userId
     * @param requestInfo
     * @param interfaceDO
     * @param result
     * @param wholeTime
     * @param routeName
     */
    @Async("taskExecutor")
    public void saveResultToDB(String userId, String requestInfo, XmanDataInterfaceDO interfaceDO, RouteResponseDTO result, String wholeTime, String routeName) {
        ResultDetail resultDetail = new ResultDetail();
        BeanUtils.copyProperties(result, resultDetail);
        resultDetail.setErrCode(result.getErrCode());
        if (result.getResultJson() != null) {
            resultDetail.setResultJson(result.getResultJson().toJSONString());
        }
        if (result.getRspInfo() != null) {
            resultDetail.setRspInfo(result.getRspInfo().toJSONString());
        }

        resultDetail.setUserId(userId);
        resultDetail.setRouteName(routeName);
        resultDetail.setReqInfo(requestInfo);
        resultDetail.setWholeTime(wholeTime);

        String errCode = result.getErrCode();
        if (SUCCESS.getCode().equals(errCode)) {
            resultDetail.setQueryStatus("1");
        } else if (NO_RESULT.getCode().equals(errCode)) {
            resultDetail.setQueryStatus("0");
        }

        if (interfaceDO != null) {
            resultDetail.setService(interfaceDO.getClassName());
            resultDetail.setMethod(interfaceDO.getMethodName());
            resultDetail.setInterfaceId(interfaceDO.getId());
            resultDetail.setProviderId(interfaceDO.getProviderId());
        }
        resultDetailMapper.insert(resultDetail);
    }

}
