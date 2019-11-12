package com.api.reflection.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "清除缓存")
@RequestMapping("clearCache")
public class ClearCacheController {

    @GetMapping("mrcache")
    public String clearMybatisRedisCache(String mapperName) {
        BaseMapper baseMapper = SpringUtil.getBean(mapperName, BaseMapper.class);
        baseMapper.deleteById(Long.MIN_VALUE);
        return "success";
    }
}
