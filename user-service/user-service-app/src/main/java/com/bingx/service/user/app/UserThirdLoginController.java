package com.bingx.service.user.app.controller;

import com.bingx.service.user.api.vo.login.req.ThirdUserLoginReq;
import com.bingx.service.user.api.vo.login.req.UserThirdTwoFaLoginReq;
import com.bingx.service.user.app.service.IUserThirdLoginService;
import com.bingx.service.user.api.vo.login.UserThirdLoginRetVo;
import com.bon.framework.boot.core.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author: Eric
 * Date: 2022/10/17 17:15
 * Description: 邀请好友相关接口
 */
@RestController
@RequestMapping("/api/user-service/v1/third")
@Api(value = "用户第三方登录", tags = "用户第三方登录")
public class UserThirdLoginController {
    @Resource
    private IUserThirdLoginService userThirdLoginService;

    @PostMapping("/login")
    @ApiOperation(value = "第三方登录")
    ServerResponse<UserThirdLoginRetVo> login(@Validated @RequestBody ThirdUserLoginReq req) {
        return userThirdLoginService.login(req);
    }

    @PostMapping("/2fa/login")
    @ApiOperation(value = "二次登录校验")
    ServerResponse<UserThirdLoginRetVo> twoFaLogin(@Validated @RequestBody UserThirdTwoFaLoginReq req) {
        return userThirdLoginService.twoFaLogin(req);
    }

}
