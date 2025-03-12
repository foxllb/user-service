package com.bingx.service.user.app.controller;

import bon.framework.boot.autoconfigure.web.common.RequestContext;
import com.bingx.service.user.api.vo.openauth.*;
import com.bingx.service.user.app.dto.UserApiKeyInfo;
import com.bingx.service.user.app.service.IOpenApiService;
import com.bon.framework.boot.core.common.ResponseCode;
import com.bon.framework.boot.core.common.ServerResponse;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Eric
 * Date: 2022/8/15 23:56
 * Description: 对外开放api相关 apiKey管理接口.
 */
@RestController
@RequestMapping("/api/user-service/v1/open")
@Api(value = "对外OpenApi管理", tags = "对外OpenApi管理")
public class UserOpenApiController {

    @Autowired
    private IOpenApiService openApiService;

    @PostMapping("/create-api-key")
    @ApiOperation(value = "创建新的apiKey")
    ServerResponse<CreateOpenApiKeyRsp> createApiKey(@RequestBody CreateOpenApiKeyReq req) {
        Long uid = RequestContext.get().getUid();
        String lang = RequestContext.get().getLang();
        //默认给读取权限
        if (CollectionUtils.isEmpty(req.getBusinessIds())) {
            req.setBusinessIds(Lists.newArrayList(2));
        }
        UserApiKeyInfo info = openApiService.createNewApiKey(uid, lang, req);
        CreateOpenApiKeyRsp rsp = new CreateOpenApiKeyRsp();
        rsp.setApiKey(info.getApiKey());
        rsp.setApiSecret(info.getApiSecret());
        rsp.setUserLabel(req.getUserLabel());
        rsp.setIpList(req.getIpList());
        rsp.setBusinessIds(req.getBusinessIds());
        return ServerResponse.createBySuccess(rsp);
    }


    @PostMapping("/edit-api-key")
    @ApiOperation(value = "修改apiKey")
    ServerResponse editApiKey(@RequestBody EditOpenApiKeyReq req) {
        Long uid = RequestContext.get().getUid();
        String lang = RequestContext.get().getLang();
        boolean success = openApiService.editApiKey(uid, lang, req);
        if (success) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode());
        }
    }

    @PostMapping("/delete-api-key")
    @ApiOperation(value = "删除apiKey")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiKey", value = "用户apiKey"),
            @ApiImplicitParam(name = "isOldKey", value = "是否旧apiKey")
    })
    ServerResponse deleteApiKey(@RequestParam(value = "apiKey",required = false) String apiKey
            , @RequestParam(value = "isOldKey",required = false) Integer isOldKey
            , @RequestBody(required = false) DeleteOpenApiKeyReq req) {
        Long uid = RequestContext.get().getUid();
        if (req == null || StringUtils.isEmpty(req.getApiKey())) {
            req = new DeleteOpenApiKeyReq();
            req.setApiKey(apiKey);
            req.setIsOldKey(isOldKey);
        }
        boolean success = openApiService.disableApiKey(uid, req);
        if (success) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode());
        }
    }

    @GetMapping("/api-keys")
    @ApiOperation(value = "获取apiKey配置列表")
    ServerResponse<QueryOpenApiKeyRsp> queryApiKeys() {
        Long uid = RequestContext.get().getUid();
        List<OpenApiKeyConfigVo> list = openApiService.queryAllApiKey(uid, new QueryOpenApiReq());
        QueryOpenApiKeyRsp rsp = new QueryOpenApiKeyRsp();
        rsp.setApiKeyConfigList(list);
        return ServerResponse.createBySuccess(rsp);
    }

    @PostMapping("/api-keys")
    @ApiOperation(value = "获取apiKey配置列表")
    ServerResponse<QueryOpenApiKeyRsp> queryApiKeys(@RequestBody QueryOpenApiReq req) {
        Long uid = RequestContext.get().getUid();
        List<OpenApiKeyConfigVo> list = openApiService.queryAllApiKey(uid, req);
        QueryOpenApiKeyRsp rsp = new QueryOpenApiKeyRsp();
        rsp.setApiKeyConfigList(list);
        return ServerResponse.createBySuccess(rsp);
    }

    @GetMapping("/all-business-list")
    @ApiOperation(value = "查询apiKey支持的所有业务类型列表")
    ServerResponse<OpenBusIdListRsp> getAllBusList() {
//        Long uid = RequestContext.get().getUid();
        String lang = RequestContext.get().getLang();
        List<OpenBusIdVo> list = openApiService.getAllBusList(lang);
        OpenBusIdListRsp rsp = new OpenBusIdListRsp();
        rsp.setBusList(list);
        return ServerResponse.createBySuccess(rsp);
    }
}
