package com.api.reflection.service;

public class GetLockToken {

    /**
     * 分布式锁, 获取token
     */
    private String getAccessToken() {
        String accessToken = powerCacheUtil.getString(POWER_CACHE_KEY);
        if (StringUtils.isEmpty(accessToken)) {
            //获取分布式锁, 锁超时时间为5秒
            BaseResponse<TryLockResult> result = iPowerCacheAgent.tryLock(POWER_REDIS_LOCK, 2 * 60);
            //获锁成功,生成token并把token放入redis
            if (null != result && result.getRc().getCode() == 200) {
                //双重检验
                accessToken = powerCacheUtil.getString(POWER_CACHE_KEY);
                if (StringUtils.isEmpty(accessToken)) {
                    SecurityService securityService = new SecurityService();
                    accessToken = securityService.getToken();
                    log.info("从powerCache获取token失败，请求获取token:" + accessToken);
                    powerCacheUtil.setString(POWER_CACHE_KEY, accessToken, 3600);
                    iPowerCacheAgent.releaseLock(POWER_REDIS_LOCK);
                }
                //获锁失败 尝试五秒10次从cache中获取accesToken
            } else {
                for (int i = 0; i < 11; i++) {
                    try {
                        //休眠0.5秒
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    accessToken = powerCacheUtil.getString(POWER_CACHE_KEY);
                    if (StringUtils.isNotEmpty(accessToken)) {
                        break;
                    }
                }
            }
        }
        return accessToken;
    }
}
