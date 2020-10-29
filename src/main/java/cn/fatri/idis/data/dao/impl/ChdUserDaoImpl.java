/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.dao.impl;

import cn.fatri.idis.data.dao.CdhUserDao;
import cn.fatri.idis.data.dao.CdhUserRepository;
import cn.fatri.idis.data.dao.entity.CdhUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author: Korol Chen
 * @date: 2020/8/24
 **/
@Component
public class ChdUserDaoImpl implements CdhUserDao {

    @Autowired
    private CdhUserRepository cdhUserRepository;

    @Override
    public CdhUserEntity saveUser(CdhUserEntity cdhUserEntity) {
        return cdhUserRepository.save(cdhUserEntity);
    }

    @Override
    public Optional<CdhUserEntity> getUserByUsername(String username) {
        return cdhUserRepository.findByUserName(username);
    }
}
