/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: Korol Chen
 * @date: 2020/8/20
 **/
@Data
@Accessors(chain = true)
public class JarvisUser {
    private Long id;
    private Long roleId;
    private String name;
    private String phoneNumber;
    private String email;
    private String credential;
    private boolean enable;
}
