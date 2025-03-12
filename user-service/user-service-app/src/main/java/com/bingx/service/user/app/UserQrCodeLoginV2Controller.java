package com.bingx.service.user.app.controller;

import com.bingx.service.user.api.vo.login.QrCodeGenerateVo;
import com.bingx.service.user.api.vo.login.req.QrCodeGenerateReq;
import com.bingx.service.user.app.restclient.proxy.II18nServiceProxy;
import com.bingx.service.user.app.service.IQrcodeLoginService;
import com.bon.framework.boot.core.common.ResponseCode;
import com.bon.framework.boot.core.common.ServerResponse;
import com.bon.framework.boot.core.log.LogNames;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Author: right
 * Date: 2023/02/14 19:10
 * Description: 用户二维码登陆相关
 */
@RestController
@RequestMapping("/api/user-service/v2/login")
@Api(value = "扫码登陆相关", tags = "扫码登陆相关")
public class UserQrCodeLoginV2Controller {

    private static final Logger log = LoggerFactory.getLogger(LogNames.DATA);

    @Resource
    private IQrcodeLoginService qrcodeLoginService;

    @Resource
    private II18nServiceProxy i18nServiceProxy;

    @PostMapping("/qrcode-generate")
    @ApiOperation(value = "生成扫描二维码")
    public ServerResponse<QrCodeGenerateVo> qrCodeGenerate(@Validated @RequestBody QrCodeGenerateReq req) {
        if (StringUtils.isEmpty(req.getValidationId())) {
            log.warn("not have validationId!!!");
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode()
                    , i18nServiceProxy.getText(ResponseCode.ILLEGAL_ARGUMENT.getI18nMsgKey())
                            .orElse("illegal request"));
        }
        return qrcodeLoginService.generateQrCode(req);
    }

}
