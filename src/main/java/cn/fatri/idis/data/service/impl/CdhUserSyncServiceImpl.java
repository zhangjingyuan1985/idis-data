/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.service.impl;

import cn.fatri.idis.data.bean.JarvisUser;
import cn.fatri.idis.data.dao.CdhUserDao;
import cn.fatri.idis.data.dao.entity.CdhAuthRoleEntity;
import cn.fatri.idis.data.dao.entity.CdhUserEntity;
import cn.fatri.idis.data.service.CdhUserSyncService;
import com.cloudera.api.ClouderaManagerClientBuilder;
import com.cloudera.api.model.ApiAuthRoleRef;
import com.cloudera.api.model.ApiUser2;
import com.cloudera.api.model.ApiUser2List;
import com.cloudera.api.v32.RootResourceV32;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author: Korol Chen
 * @date: 2020/8/20
 **/
@Slf4j
@Service
public class CdhUserSyncServiceImpl implements CdhUserSyncService {

    @Value("${idis.cdh.host}")
    private String clouderaHost;

    @Value("${idis.cdh.admin-username}")
    private String clouderaAdminUsername;

    @Value("${idis.cdh.admin-password}")
    private String clouderaAdminPassword;

    @Value("${idis.cdh.admin-role-uuid}")
    private String clouderaAdminRoleUuid;

    @Value("${idis.cdh.read-only-role-uuid}")
    private String clouderaReadOnlyRoleUuid;

    @Value("${idis.cdh.limit-admin-role-id}")
    private Long clouderaLimitAdminRoleId;

    @Value("${idis.cdh.internal-username-prefix}")
    private String clouderaInternalUsernamePrefix;

    @Value("${idis.jarvis.system-admin-role-id}")
    private Long jarvisSystemAdminRoleId;

    @Autowired
    private CdhUserDao cdhUserDao;

    @Override
    public void createUser(JarvisUser jarvisUser) {
        boolean denyLogin = true;
        ApiUser2 user = new ApiUser2();
        ApiAuthRoleRef apiRoleRef = new ApiAuthRoleRef();
        if (jarvisSystemAdminRoleId.equals(jarvisUser.getRoleId())) {
            // 完全管理员
            user.setName(jarvisUser.getEmail());
            apiRoleRef.setUuid(clouderaAdminRoleUuid);
            denyLogin = false;
        } else {
            // 加上前缀，在CDH界面上不显示
            user.setName(clouderaInternalUsernamePrefix + jarvisUser.getEmail());
            // 暂使用只读用户，因为该接口只允许填写【完全管理员】和【只读用户】的角色，而且还是必填
            apiRoleRef.setUuid(clouderaReadOnlyRoleUuid);
        }
        user.setPassword(jarvisUser.getCredential());
        user.addAuthRole(apiRoleRef);
        ApiUser2List apiUserList = new ApiUser2List();
        apiUserList.add(user);
        try {
            RootResourceV32 apiRoot = new ClouderaManagerClientBuilder().withHost(clouderaHost)
                    .withUsernamePassword(clouderaAdminUsername, clouderaAdminPassword).build().getRootV32();
            apiRoot.getUsersResource().createUsers2(apiUserList);
            log.info("Created CDH user successfully");
            if (denyLogin) {
                denyLogin(user.getName());
            }
        } catch (Exception e) {
            log.error("Created CDH user error");
        }
    }

    @Override
    public void updateUser(JarvisUser jarvisUser) {
        boolean denyLogin = true;
        String oldUsername = jarvisUser.getEmail();
        ApiUser2 user = new ApiUser2();
        ApiAuthRoleRef apiRoleRef = new ApiAuthRoleRef();
        if (jarvisSystemAdminRoleId.equals(jarvisUser.getRoleId())) {
            // 感知平台系统管理员，对应CDH完全管理员
            user.setName(jarvisUser.getEmail());
            apiRoleRef.setUuid(clouderaAdminRoleUuid);
            if (jarvisUser.isEnable()) {
                denyLogin = false;
            }
        } else {
            // 加上前缀，在CDH界面上不显示
            user.setName(clouderaInternalUsernamePrefix + jarvisUser.getEmail());
            // 非感知平台系统管理员，暂使用只读用户，因为该接口只允许填写【完全管理员】和【只读用户】的角色
            apiRoleRef.setUuid(clouderaReadOnlyRoleUuid);
        }
        user.setPassword(jarvisUser.getCredential());
        user.addAuthRole(apiRoleRef);
        try {
            RootResourceV32 apiRoot = new ClouderaManagerClientBuilder().withHost(clouderaHost)
                    .withUsernamePassword(clouderaAdminUsername, clouderaAdminPassword).build().getRootV32();
            apiRoot.getUsersResource().readUser2(oldUsername);
            log.info("Exist CDH user with raw user name");
        } catch (Exception e) {
            oldUsername = clouderaInternalUsernamePrefix + jarvisUser.getEmail();
            log.info("Not exist CDH user with raw user name");
        }
        ApiUser2List apiUserList = new ApiUser2List();
        apiUserList.add(user);
        try {
            RootResourceV32 apiRoot = new ClouderaManagerClientBuilder().withHost(clouderaHost)
                    .withUsernamePassword(clouderaAdminUsername, clouderaAdminPassword).build().getRootV32();
            apiRoot.getUsersResource().updateUser2(oldUsername, user);
            log.info("Updated CDH user successfully");
            if (denyLogin) {
                denyLogin(user.getName());
            }
            if (!oldUsername.equals(user.getName())) {
                updateUsername(oldUsername, user.getName());
            }
        } catch (Exception e) {
            log.error("Updated CDH user error");
        }
    }

    private void denyLogin(String username) {
        // 操作数据库更改该用户角色为不可登录的角色
        if (cdhUserDao.getUserByUsername(username).isPresent()) {
            CdhUserEntity cdhUserEntity = cdhUserDao.getUserByUsername(username).get();
            // 改为不可登录的角色
            Set<CdhAuthRoleEntity> authRoleSet = cdhUserEntity.getAuthRoleSet();
            authRoleSet.clear();
            authRoleSet.add(new CdhAuthRoleEntity(clouderaLimitAdminRoleId));
            cdhUserDao.saveUser(cdhUserEntity);
        }
    }

    private void updateUsername(String oldUsername, String newUsername) {
        if (cdhUserDao.getUserByUsername(oldUsername).isPresent()) {
            CdhUserEntity cdhUserEntity = cdhUserDao.getUserByUsername(oldUsername).get();
            cdhUserEntity.setUserName(newUsername);
            cdhUserDao.saveUser(cdhUserEntity);
        }
    }

    @Override
    public void deleteUser(JarvisUser jarvisUser) {
        String chdUsername = jarvisUser.getEmail();
        if (!jarvisSystemAdminRoleId.equals(jarvisUser.getRoleId())) {
            chdUsername = clouderaInternalUsernamePrefix + chdUsername;
        }
        try {
            RootResourceV32 apiRoot = new ClouderaManagerClientBuilder().withHost(clouderaHost)
                    .withUsernamePassword(clouderaAdminUsername, clouderaAdminPassword).build().getRootV32();
            apiRoot.getUsersResource().deleteUser2(chdUsername);
            log.info("Deleted CDH user successfully");
        } catch (Exception e) {
            log.error("Deleted CDH user error");
        }
    }
}
