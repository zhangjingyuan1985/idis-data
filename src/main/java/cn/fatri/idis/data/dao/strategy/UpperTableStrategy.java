/*
 * Copyright © 2020 Fatri
 */
package cn.fatri.idis.data.dao.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * @author: Korol Chen
 * @date: 2020/8/24
 **/
public class UpperTableStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // 将表名全部转换成大写
        String tableName = name.getText().toUpperCase();
        return Identifier.toIdentifier(tableName);
    }
}