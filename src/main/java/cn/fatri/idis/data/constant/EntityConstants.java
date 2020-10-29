/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.constant;

/**
 * @author: Korol Chen
 * @date: 2020/9/4
 **/
public class EntityConstants {
    public static final String TABLE_USERS = "USERS";
    public static final String TABLE_AUTH_ROLES = "AUTH_ROLES";

    public static final String COLUMN_AUTH_ROLE_ID = "AUTH_ROLE_ID";
    public static final String COLUMN_AUTH_ROLE_NAME = "NAME";
    public static final String COLUMN_AUTH_ROLE_UUID = "UUID";

    public static final String COLUMN_USER_ID = "USER_ID";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_USER_PASSWORD_HASH = "PASSWORD_HASH";
    public static final String COLUMN_USER_PASSWORD_SALT = "PASSWORD_SALT";
    public static final String COLUMN_USER_PASSWORD_LOGIN = "PASSWORD_LOGIN";
    public static final String COLUMN_USER_OPTIMISTIC_LOCK_VERSION = "OPTIMISTIC_LOCK_VERSION";
    public static final String COLUMN_USER_AUTH_ROLES = "USER_AUTH_ROLES";
}
