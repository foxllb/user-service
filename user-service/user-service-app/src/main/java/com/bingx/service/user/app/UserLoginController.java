package com.bingx.service.user.app.controller;

import bon.framework.boot.autoconfigure.web.common.ReqContextHolder;
import bon.framework.boot.autoconfigure.web.common.RequestContext;
import com.bingx.service.user.api.vo.PlatformType;
import com.bingx.service.user.api.vo.login.CheckTokenVo;
import com.bingx.service.user.api.vo.login.OAuthTokenVo;
import com.bingx.service.user.api.vo.login.UserLoginVo;
import com.bingx.service.user.api.vo.login.UserThirdLoginRetV2Vo;
import com.bingx.service.user.api.vo.login.req.CheckTokenReq;
import com.bingx.service.user.api.vo.login.req.ThirdUserLoginReq;
import com.bingx.service.user.api.vo.login.req.User2faLoginReq;
import com.bingx.service.user.api.vo.login.req.UserLoginReq;
import com.bingx.service.user.app.service.IUserLoginService;
import com.bingx.service.user.app.service.IUserThirdLoginV2Service;
import com.bon.framework.boot.core.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author: neo.xu
 * Date: 2023/08/01 17:15
 * Description: 用户登录接口
 */
@RestController
@RequestMapping("/api/user-service/v1")
@Api(value = "用户第三方登录", tags = "用户第三方登录")
public class UserLoginController {
    @Resource
    private IUserLoginService userLoginService;

    @Resource
    private IUserThirdLoginV2Service userThirdLoginV2Service;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    ServerResponse<UserLoginVo> login(@Validated @RequestBody UserLoginReq req) {
        return userLoginService.login(req);
    }

    @PostMapping("/2fa/login")
    @ApiOperation(value = "二次登录校验")
    ServerResponse<UserLoginVo> twoFaLogin(@Validated @RequestBody User2faLoginReq req) {
        return userLoginService.login2fa(req);
    }

    @PostMapping("/third-login")
    @ApiOperation(value = "第三方登录")
    ServerResponse<UserThirdLoginRetV2Vo> thirdLogin(@Validated @RequestBody ThirdUserLoginReq req) {
        return userThirdLoginV2Service.login(req);
    }

    @PostMapping("/2fa/third-login")
    @ApiOperation(value = "三方登录二次校验")
    ServerResponse<UserThirdLoginRetV2Vo> twoFaThirdLogin(@Validated @RequestBody User2faLoginReq req) {
        return userThirdLoginV2Service.twoFaLogin(req);
    }

    @PostMapping("/check-token")
    @ApiOperation(value = "判断当前token是否有效")
    ServerResponse<CheckTokenVo> checkToken(@Validated @RequestBody CheckTokenReq req) {
        return userLoginService.checkToken(req);
    }

    @GetMapping("/generate-oauth-token")
    @ApiOperation(value = "生成oauth临时token是否有效")
    ServerResponse<OAuthTokenVo> generateOauthToken() {
        return userLoginService.generateOauthToken(ReqContextHolder.getUid());
    }

}
