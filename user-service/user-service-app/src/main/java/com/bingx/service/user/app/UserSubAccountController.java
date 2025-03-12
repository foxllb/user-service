package com.bingx.service.user.app.controller;

import bon.framework.boot.autoconfigure.web.common.RequestContext;
import com.bingx.service.user.api.vo.PlatformType;
import com.bingx.service.user.api.vo.subaccount.*;
import com.bingx.service.user.api.vo.subaccount.req.*;
import com.bingx.service.user.api.vo.subaccount.rsp.ApplySubAccountStatusVo;
import com.bingx.service.user.app.service.ISubAccountService;
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
@RequestMapping("/api/user-service/v1/sub-account")
@Api(value = "子账号相关接口", tags = "子账号相关接口")
public class UserSubAccountController {
    @Resource
    private ISubAccountService subAccountService;

    @PostMapping("/create")
    @ApiOperation(value = "创建子账号")
    ServerResponse<CreateSubAccountVo> create(@Validated @RequestBody CreateSubAccountReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.create(req);
    }

    @PostMapping("/switch-account")
    @ApiOperation(value = "切换子账号")
    ServerResponse<SwitchSubAccountVo> switchAccount(@Validated @RequestBody SwitchSubAccountReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.switchAccount(req);
    }

    @GetMapping("/switch-list")
    @ApiOperation(value = "切换子账号列表")
    ServerResponse<SubAccountSwitchListVo> switchAccount() {
        SubAccountSwitchListReq req = new SubAccountSwitchListReq();
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        req.setPagination(false);
        return subAccountService.switchList(req);
    }

    @PostMapping("/switch-list")
    @ApiOperation(value = "查找子账号列表")
    ServerResponse<SubAccountSwitchListVo> querySwitchAccount(@Validated @RequestBody SubAccountSwitchListReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        req.setPagination(true);
        return subAccountService.switchList(req);
    }

    @PostMapping("/list")
    @ApiOperation(value = "子账号管理列表")
    ServerResponse<SubAccountListVo> list(@Validated @RequestBody ListSubAccountReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.list(req);
    }

    @PostMapping("/status")
    @ApiOperation(value = "更新子账号状态")
    ServerResponse<UpdateSubAccountStatusVo> updateStatus(@Validated @RequestBody UpdateSubAccountStatusReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.updateStatus(req);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新子账号")
    ServerResponse<?> updateSubAccount(@Validated @RequestBody UpdateSubAccountReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.updateSubAccount(req);
    }

    @PostMapping("/permission")
    @ApiOperation(value = "更新子账号权限")
    ServerResponse<?> resetLoginPwd(@Validated @RequestBody UpdateSubAccountPermissionReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.updatePermission(req);
    }

    @PostMapping("/reset-login-pwd")
    @ApiOperation(value = "重置子账号密码")
    ServerResponse<?> resetLoginPwd(@Validated @RequestBody ResetSubAccountLoginPwdReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.resetLoginPwd(req);
    }

    @PostMapping("/update-name")
    @ApiOperation(value = "更新子账号名")
    ServerResponse<?> updateSubAccountName(@Validated @RequestBody UpdateSubAccountNameReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.updateSubAccountName(req);
    }

    @PostMapping("/remove-google")
    @ApiOperation(value = "移除子账号Google")
    ServerResponse<?> removeSubAccountGoogle(@Validated @RequestBody RmSubAccountGoogleReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.removeSubAccountGoogle(req);
    }

    @PostMapping("/check-create")
    @ApiOperation(value = "创建子账号前置检查")
    ServerResponse<CheckCreateSubAccountVo> checkCreate() {
        return subAccountService.checkCreate(RequestContext.get().getUid());
    }

    @PostMapping("/apply-permission")
    @ApiOperation(value = "申请子账户创建权限")
    ServerResponse<?> applyPermission(@Validated @RequestBody ApplySubAccountPermissionReq req) {
        req.setUid(RequestContext.get().getUid());
        req.setPlatformType(PlatformType.platformType(Integer.parseInt(RequestContext.get().getPlatformId())));
        return subAccountService.applyPermission(req);
    }

    @GetMapping("/search-apply-status")
    @ApiOperation(value = "查询申请状态")
    ServerResponse<ApplySubAccountStatusVo> searchApplyStatus() {
        return subAccountService.searchApproveFlowStatus(RequestContext.get().getUid());
    }
}
