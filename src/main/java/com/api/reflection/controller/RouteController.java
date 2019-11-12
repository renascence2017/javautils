package com.api.reflection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("router")
@Api(tags = "数据路由网关")
@Slf4j
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * 对外唯一入口
     */
    @ApiOperation("routeName")
    @PostMapping("/{routeName}")
    public RouteResponseDTO execute(@RequestBody RouterRequestDTO routerRequestDTO, HttpServletRequest request, @PathVariable("routeName") String routeName) {
        return routeService.executeRoute(routerRequestDTO, routeName);
    }

    //	 TODO 重设缓存 加入前端功能后去掉
    @ApiOperation("resetInterfacesRedis")
    @PostMapping("resetInterfacesRedis/{routeName}")
    public String resetInterfacesRedis(@RequestBody RouterRequestDTO routerRequestDTO, @PathVariable("routeName") String routeName) {
        routeService.resetInterfacesRedis(routerRequestDTO, routeName);
        return "SUCC";
    }

    @ApiOperation("deleteInterfacesRedis")
    @PostMapping("deleteInterfacesRedis/{routeName}")
    public String deleteInterfacesRedis(@RequestBody RouterRequestDTO routerRequestDTO, @PathVariable("routeName") String routeName) {
        routeService.deleteInterfacesRedis(routerRequestDTO, routeName);
        return "SUCC";
    }

    @ApiOperation("resetValidRedis")
    @PostMapping("resetValidRedis/{routeName}")
    public String resetValidRedis(@RequestBody RouterRequestDTO routerRequestDTO, @PathVariable("routeName") String routeName) {
        routeService.resetValidRedis(routerRequestDTO, routeName);
        return "SUCC";
    }

    @ApiOperation("deleteValidRedis")
    @PostMapping("deleteValidRedis/{routeName}")
    public String deleteValidRedis(@RequestBody RouterRequestDTO routerRequestDTO, @PathVariable("routeName") String routeName) {
        routeService.deleteValidRedis(routerRequestDTO, routeName);
        return "SUCC";
    }

    /**
     * 获取访问者ip
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getUserId() {
        return String.valueOf(1);
    }
}
