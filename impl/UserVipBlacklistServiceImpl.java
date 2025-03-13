package com.bingx.service.user.app.service.impl;

import com.bingx.service.user.api.vo.vip.VipBlackListVo;
import com.bingx.service.user.app.constant.UserVipConstants;
import com.bingx.service.user.app.dao.entity.vip.TbUserVipWhiteListConfig;
import com.bingx.service.user.app.dao.service.IUserVipWhiteListConfigDao;
import com.bingx.service.user.app.dto.InviteUserInfoDto;
import com.bingx.service.user.app.service.IUserInviteService;
import com.bingx.service.user.app.service.IUserVipBlacklistService;
import com.bon.framework.boot.core.log.LogNames;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserVipBlacklistServiceImpl implements IUserVipBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(LogNames.DATA);

    @Resource
    private IUserVipWhiteListConfigDao userVipWhiteListConfigDao;
    @Resource
    private IUserInviteService userInviteService;


    @Override
    public VipBlackListVo findInBlackListByUid(Long uid, List<Integer> blacklistTypes) {

        if (CollectionUtils.isEmpty(blacklistTypes)) {
            TbUserVipWhiteListConfig tbUserVipWhiteListConfig = userVipWhiteListConfigDao.selectByUidAndType(uid, TbUserVipWhiteListConfig.WHITE_LIST_TYPE);
            if (tbUserVipWhiteListConfig != null
                    && tbUserVipWhiteListConfig.getExpireDate().after(new Date())
                    && tbUserVipWhiteListConfig.getMaxLevel() <= UserVipConstants.VIP_LEVEL_ZERO) {
                return new VipBlackListVo(true, uid);
            }
        }
        Map<Byte, Set<Long>> byteSetMap = covertBlackList(blacklistTypes);
        logger.info("findInBlackListByUid blacklistTypes:{}", byteSetMap);
        if (byteSetMap.isEmpty()) {
            logger.info("findInBlackListByUid byteSetMap isEmpty uid :{}", uid);
            return new VipBlackListVo(false, null);
        }
        List<InviteUserInfoDto> allAncestor = userInviteService.getAllAncestor(uid);
        if (CollectionUtils.isEmpty(allAncestor)) {
            logger.info("findInBlackListByUid allAncestor isEmpty uid :{}", uid);
            return new VipBlackListVo(false, null);
        }

        for (Map.Entry<Byte, Set<Long>> byteSetEntry : byteSetMap.entrySet()) {
            Integer type = byteSetEntry.getKey().intValue();
            List<Long> ancestorUids;
            if (type.equals(TbUserVipWhiteListConfig.BLACK_LIST_TYPE2)) {
                ancestorUids = allAncestor.stream()
                        .filter(v -> v.getDistance() == 1).map(InviteUserInfoDto::getUid).collect(Collectors.toList());
            } else {
                ancestorUids = allAncestor.stream().map(InviteUserInfoDto::getUid).collect(Collectors.toList());
            }
            logger.info("findInBlackListByUid uid {}, ancestorUids is :{}", uid, ancestorUids);
            Set<Long> blackUids = byteSetEntry.getValue();
            for (Long aLong : blackUids) {
                if (ancestorUids.contains(aLong)) {
                    logger.info("findInBlackListByUid uid {}, hit blackList is :{}", uid, aLong);
                    return new VipBlackListVo(true, aLong);
                }
            }
        }
        return new VipBlackListVo(false, null);
    }

    private Map<Byte, Set<Long>> covertBlackList(List<Integer> blacklistTypes) {
        long now = System.currentTimeMillis();
        return userVipWhiteListConfigDao.listByTypeListCache(Lists.newArrayList(TbUserVipWhiteListConfig.BLACK_LIST_TYPE2, TbUserVipWhiteListConfig.BLACK_LIST_TYPE3))
                .stream()
                //过期时间
                .filter(v -> now < v.getExpireDate().getTime())
                .filter(v -> {
                    if (CollectionUtils.isEmpty(blacklistTypes)) {
                        return true;
                    }
                    return blacklistTypes.contains(v.getType().intValue());
                })
                .collect(Collectors.groupingBy(TbUserVipWhiteListConfig::getType, Collectors.mapping(TbUserVipWhiteListConfig::getUid, Collectors.toSet())));
    }
}
