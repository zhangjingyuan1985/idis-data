/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.dao;

import cn.fatri.idis.data.dao.entity.CdhUserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: Korol Chen
 * @date: 2020/8/24
 **/
@Repository
public interface CdhUserRepository extends CrudRepository<CdhUserEntity, String>, JpaSpecificationExecutor<CdhUserEntity> {
    Optional<CdhUserEntity> findByUserName(String username);
}
