/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.service;

import cn.fatri.idis.data.bean.JarvisUser;

/**
 * @author: Korol Chen
 * @date: 2020/8/20
 **/
public interface CdhUserSyncService {

    /**
     * 创建CDH用户
     *
     * @param jarvisUser 感知平台用户
     */
    void createUser(JarvisUser jarvisUser);

    /**
     * 更新CDH用户
     *
     * @param jarvisUser 感知平台用户
     */
    void updateUser(JarvisUser jarvisUser);

    /**
     * 删除CDH用户
     *
     * @param jarvisUser 感知平台用户
     */
    void deleteUser(JarvisUser jarvisUser);
}
