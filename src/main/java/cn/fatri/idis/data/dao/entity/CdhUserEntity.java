/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.dao.entity;

import cn.fatri.idis.data.constant.EntityConstants;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

/**
 * @author: Korol Chen
 * @date: 2020/8/24
 **/
@Data
@Entity
@Table(name = EntityConstants.TABLE_USERS)
@EntityListeners(AuditingEntityListener.class)
public class CdhUserEntity {

    @Id
    @Column(name = EntityConstants.COLUMN_USER_ID)
    private Long userId;

    @Column(name = EntityConstants.COLUMN_USER_NAME)
    private String userName;

    @Column(name = EntityConstants.COLUMN_USER_PASSWORD_HASH)
    private String passwordHash;

    @Column(name = EntityConstants.COLUMN_USER_PASSWORD_SALT)
    private Long passwordSalt;

    @Column(name = EntityConstants.COLUMN_USER_PASSWORD_LOGIN)
    private Integer passwordLogin;

    @Column(name = EntityConstants.COLUMN_USER_OPTIMISTIC_LOCK_VERSION)
    private Long optimisticLockVersion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = EntityConstants.COLUMN_USER_AUTH_ROLES,
            joinColumns = {@JoinColumn(name = EntityConstants.COLUMN_USER_ID)},
            inverseJoinColumns = {@JoinColumn(name = EntityConstants.COLUMN_AUTH_ROLE_ID)})
    private Set<CdhAuthRoleEntity> authRoleSet;

}
