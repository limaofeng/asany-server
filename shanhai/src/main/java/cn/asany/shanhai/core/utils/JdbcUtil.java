package cn.asany.shanhai.core.utils;

import lombok.SneakyThrows;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.spring.SpringBeanUtils;

import javax.sql.DataSource;
import java.sql.*;

public class JdbcUtil {

  @SneakyThrows
  public static void dropColumn(String tableName, String columnName) {
    DataSource dataSource = SpringBeanUtils.getBeanByType(DataSource.class);
    Connection connection = null;
    Statement stmt = null;
    try {
      connection = dataSource.getConnection();
      stmt = connection.createStatement();
      int i =
          stmt.executeUpdate(
              String.format("alter TABLE %s drop column %s;", tableName, columnName));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      org.jfantasy.framework.dao.util.JdbcUtil.closeStatement(stmt);
      org.jfantasy.framework.dao.util.JdbcUtil.closeConnection(connection);
    }
  }

  @SneakyThrows
  public static void renameColumn(String tableName, String oldColumnName, String newColumnName) {
    DataSource dataSource = SpringBeanUtils.getBeanByType(DataSource.class);
    Connection connection = dataSource.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    String columnType;
    try {
      ps =
          connection.prepareStatement(
              "select column_type from information_schema.columns where table_name = ? and column_name = ?");
      ps.setString(1, tableName);
      ps.setString(2, oldColumnName);
      rs = ps.executeQuery();
      columnType = rs.next() ? rs.getString(1) : null;
      if (columnType == null) {
        throw new ValidationException("未发现字段");
      }
    } finally {
      org.jfantasy.framework.dao.util.JdbcUtil.closeStatement(ps);
      org.jfantasy.framework.dao.util.JdbcUtil.closeResultSet(rs);
    }
    try {
      ps =
          connection.prepareStatement(
              String.format(
                  "alter table %s change %s %s %s",
                  tableName, oldColumnName, newColumnName.toLowerCase(), columnType));
      ps.executeUpdate();
    } finally {
      org.jfantasy.framework.dao.util.JdbcUtil.closeStatement(ps);
      org.jfantasy.framework.dao.util.JdbcUtil.closeConnection(connection);
    }
  }

  public static void renameTable(String tableName, String newTableName) {
    DataSource dataSource = SpringBeanUtils.getBeanByType(DataSource.class);
    Connection connection = null;
    Statement stmt = null;
    try {
      connection = dataSource.getConnection();
      stmt = connection.createStatement();
      stmt.executeUpdate(String.format("alter table %s rename to %s", tableName, newTableName.toLowerCase()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      org.jfantasy.framework.dao.util.JdbcUtil.closeStatement(stmt);
      org.jfantasy.framework.dao.util.JdbcUtil.closeConnection(connection);
    }
  }

    public static void dropTable(String tableName) {

    }
}
