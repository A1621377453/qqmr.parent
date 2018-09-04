package com.qingqingmr.qqmr.domain.bean;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author crn
 * @datetime 2018-07-21 16:30:19
 */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.DECIMAL)
public class DoubleTypeHandler implements TypeHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DoubleTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        LOG.info("setParameter called");
    }

    @Override
    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        LOG.info("getResult：："+s);
       /* Double d = resultSet.getDouble(s);
        LOG.info("getResultDouble：："+d);*/
        String v = resultSet.getString(s);
        LOG.info("getResultString：："+v);
        return null
                ;
    }

    @Override
    public Object getResult(ResultSet resultSet, int i) throws SQLException {
        LOG.info("getResult 1 called");
        return null;
    }

    @Override
    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        LOG.info("getResult 3 called");
        return null;
    }
}
