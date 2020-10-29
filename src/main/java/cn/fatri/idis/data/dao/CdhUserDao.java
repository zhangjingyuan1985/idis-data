/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.dao;

import cn.fatri.idis.data.dao.entity.CdhUserEntity;

import java.util.Optional;

/**
 * @author: Korol Chen
 * @date: 2020/8/24
 **/
public interface CdhUserDao {

    CdhUserEntity saveUser(CdhUserEntity cdhUserEntity);

    Optional<CdhUserEntity> getUserByUsername(String username);
}
