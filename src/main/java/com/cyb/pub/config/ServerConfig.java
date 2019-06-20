package com.cyb.pub.config;

/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
public class ServerConfig {
    public static final String KAFKA224="192.168.108.224:9092";
    public static final String ZK224="192.168.108.224:2181";

    public static final String KAFKALOCAL="localhost:9092";
    public static final String ZKLOCAL="localhost:2181";

    public static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
    public static final String MYSQL_URL="jdbc:mysql://192.168.21.11:3306";
    public static final String MYSQL_USERNAME="root";
    public static final String MYSQL_PASSWORD="111111";


    public static final String ORACLE_DRIVER="com.oracle.jdbc.Driver";
    public static final String ORACLE_URL="jdbc:oracle:thin:@127.0.0.1:1521:zvfdb";
    public static final String ORACLE_USERNAME="root";
    public static final String ORACLE_PASSWORD="111111";

    public static final String IBM_DRIVER="com.ibm.db2.jcc.DB2Driver";
    public static final String IBM_URL="jdbc:db2://127.0.0.1:50000/zvfdb";
    public static final String IBM_USERNAME="root";
    public static final String IBM_PASSWORD="111111";

    public static final String POSTGRESQL_DRIVER="org.postgresql.Driver";
    public static final String POSTGRESQL_URL="jdbcostgresql://127.0.0.1/zvfdb";
    public static final String POSTGRESQL_USERNAME="root";
    public static final String POSTGRESQL_PASSWORD="111111";


    public static final String SQLSERVER_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String SQLSERVER_URL="jdbc:sqlserver://127.0.0.1:1433;DatabaseName=zvfdb";
    public static final String SQLSERVER_USERNAME="root";
    public static final String SQLSERVER_PASSWORD="111111";

    public static final String INFORMIX_DRIVER="com.informix.jdbc.IfxDriver";
    public static final String INFORMIX_URL="jdbc:informix-sqli://127.0.0.1:1533/zvfdb";
    public static final String INFORMIX_USERNAME="root";
    public static final String INFORMIX_PASSWORD="111111";

    public static final String SYBASE_DRIVER="com.sybase.jdbc.SybDriver";
    public static final String SYBASE_URL="jdbcybase:Tds:127.0.0.1:5007/zvfdb";
    public static final String SYBASE_USERNAME="root";
    public static final String SYBASE_PASSWORD="111111";
    // Websphere/WebLogic数据源 -> datasource=zvfds
    public static final String JBOOS_TOMCAT_URL="java:comp/env/jdbc/zvfds";
    public static final String JBOOS_TOMCAT_USERNAME="root";
    public static final String JBOOS_TOMCAT_PASSWORD="111111";



    public static final String H2_DRIVER="";
    public static final String H2_URL="";
    public static final String H2_USERNAME="";
    public static final String H2_PASSWORD="";




}
