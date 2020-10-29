/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.dao.entity;

import cn.fatri.idis.data.constant.EntityConstants;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author: Korol Chen
 * @date: 2020/8/25
 **/
@Data
@Entity
@Table(name = EntityConstants.TABLE_AUTH_ROLES)
@EntityListeners(AuditingEntityListener.class)
public class CdhAuthRoleEntity {

    @Id
    @Column(name = EntityConstants.COLUMN_AUTH_ROLE_ID)
    private Long authRoleId;

    @Column(name = EntityConstants.COLUMN_AUTH_ROLE_NAME)
    private String name;

    @Column(name = EntityConstants.COLUMN_AUTH_ROLE_UUID)
    private String uuid;

    public CdhAuthRoleEntity(Long authRoleId) {
        this.setAuthRoleId(authRoleId);
    }
}
