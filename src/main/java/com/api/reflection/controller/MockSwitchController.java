package com.api.reflection.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "mock开关")
@RequestMapping("mockSwitch")
public class MockSwitchController {

    @Value("${DEPLOY_ENV}")
    private String env;


    @Autowired
    InterfaceMapper interfaceMapper;

    @GetMapping("/{id}/{mockSwitch}")
    public String mockSwitch(@PathVariable("id") Long id, @PathVariable("mockSwitch") int mockSwitch) {
        if (StringUtils.equals(env, "prd")) {
            return "prd";
        }
        if (mockSwitch != 0 && mockSwitch != 1) {
            return "mockSwitch is incorrect";
        }
        if (null == id) {
            return "id can not be null ! ";
        }

        //需要设置的值
        XmanDataInterfaceDO xmanDataInterfaceDO = new XmanDataInterfaceDO();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        //全开或全关
        int count;
        if (-1 == id) {
            xmanDataInterfaceDO.setMockSwitch(mockSwitch);
            count = interfaceMapper.update(xmanDataInterfaceDO, updateWrapper);
            //按照id设置开关
        } else {
            xmanDataInterfaceDO.setId(id);
            xmanDataInterfaceDO.setMockSwitch(mockSwitch);
            count = interfaceMapper.updateById(xmanDataInterfaceDO);
        }
        if (count >= 1) {
            return "success: " + count;
        }
        return "failed";
    }
}
