package com.bingx.service.user.app.controller;

import bon.framework.boot.autoconfigure.web.common.RequestContext;
import com.bingx.service.user.api.dnd.vo.UserNoDisturbConfigVo;
import com.bingx.service.user.api.vo.UserPreferenceVo;
import com.bingx.service.user.app.dto.user.UpdateFiatCurrencyDto;
import com.bingx.service.user.app.service.IUserConfigService;
import com.bingx.service.user.app.service.IUserInfoService;
import com.bingx.service.user.app.service.impl.TurkeyUserConfigServiceImpl;
import com.bingx.service.user.app.utils.CommonUtils;
import com.bon.framework.boot.core.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Author: Eric
 * Date: 2022/11/22 19:10
 * Description: 用户配置相关
 */
@Slf4j
@RestController
@RequestMapping("/api/user-service/v1/config")
@Api(value = "用户配置相关", tags = "用户配置相关")
public class UserConfigController {

    @Resource
    private IUserConfigService userConfigService;

    @Resource
    private TurkeyUserConfigServiceImpl turkeyUserConfigService;

    @Resource
    private IUserInfoService userInfoService;

    @GetMapping("/preference")
    @ApiOperation(value = "获取偏好设置配置信息")
    public ServerResponse<UserPreferenceVo> getUserPreference() {
        long uid = RequestContext.get().getUid();
        UserPreferenceVo vo;
        if (!userInfoService.allowRequest(uid, CommonUtils.getAppSiteId())) {
            vo = turkeyUserConfigService.getPreference(uid, RequestContext.get().getLang());
        } else {
            vo = userConfigService.getPreference(uid, RequestContext.get().getLang(), true);
        }
        return ServerResponse.createBySuccess(vo);
    }

    @PostMapping("/update-preference")
    @ApiOperation(value = "更新偏好设置配置信息")
    public ServerResponse updatePreference(@RequestBody UserPreferenceVo userPreferenceVo) {
        Long uid = RequestContext.get().getUid();
        UserPreferenceVo userPreferenceVo1 = userConfigService.updatePreference(uid, userPreferenceVo);
        return ServerResponse.createBySuccess(userPreferenceVo1);
    }

    @ApiOperation(value = "更换用户的默认的展示法币")
    @PostMapping("/update-fiat-currency")
    public ServerResponse<UserPreferenceVo> updateFiatCurrency(@Valid @RequestBody UpdateFiatCurrencyDto dto) {
        long uid = RequestContext.get().getUid();
        UserPreferenceVo userPreferenceVo = new UserPreferenceVo();
        userPreferenceVo.setFiatCurrency(dto.getName());
        UserPreferenceVo userPreferenceVo1 = userConfigService.updatePreference(uid, userPreferenceVo);
        return ServerResponse.createBySuccess(userPreferenceVo1);
    }

    @ApiOperation(value = "更新用户的推送免打扰配置信息")
    @PostMapping("/update-no-disturb-config")
    ServerResponse<UserPreferenceVo> updatePushNoDisturbConfig(@RequestBody UserNoDisturbConfigVo noDisturbConfig) {
        long uid = RequestContext.get().getUid();
        UserPreferenceVo userPreferenceVo = new UserPreferenceVo();
        userPreferenceVo.setNoDisturb(noDisturbConfig);
        UserPreferenceVo userPreferenceVo1 = userConfigService.updatePreference(uid, userPreferenceVo);
        return ServerResponse.createBySuccess(userPreferenceVo1);
    }
}
