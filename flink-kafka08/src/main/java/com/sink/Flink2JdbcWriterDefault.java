package com.sink;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.flink.api.java.tuple.Tuple10;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
/**
 * 描述：
 *
 * @author iechenyb
 * @create --
 */
public class Flink2JdbcWriterDefault extends RichSinkFunction<Tuple10<String, String, String, String, String, String, String, String, String, String>>{
    private static final long serialVersionUID = -8930276689109741501L;

    private Connection connect = null;
    private PreparedStatement ps = null;

    /*
     * (non-Javadoc)
     *
     * @see org.apache.flink.api.common.functions.AbstractRichFunction#open(org.
     * apache.flink.configuration.Configuration) get database connect
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        openH2DBConnection();
    }

    void openMysqlConnection()throws Exception{
        Class.forName("");
        connect = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.21.11:3306", "root", "flink");
        ps = (PreparedStatement) connect.prepareStatement("insert into flink.test_tb1 values (?,?,?,?,?,?,?,?,?,?)");
    }
    void openH2DBConnection()throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        connect = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.21.11:3306", "root", "flink");
        ps = (PreparedStatement) connect.prepareStatement("insert into flink.test_tb1 values (?,?,?,?,?,?,?,?,?,?)");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.flink.streaming.api.functions.sink.SinkFunction#invoke(java.
     * lang.Object,
     * org.apache.flink.streaming.api.functions.sink.SinkFunction.Context) read
     * data from flink DataSet to database
     */
    @Override
    public void invoke(Tuple10<String, String, String, String, String, String, String, String, String, String> value,
                       Context context) throws Exception {
        ps.setString(1, value.f0);
        ps.setString(2, value.f1);
        ps.setString(3, value.f2);
        ps.setString(4, value.f3);
        ps.setString(5, value.f4);
        ps.setString(6, value.f5);
        ps.setString(7, value.f6);
        ps.setString(8, value.f7);
        ps.setString(9, value.f8);
        ps.setString(10, value.f9);
        ps.executeUpdate();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.flink.api.common.functions.AbstractRichFunction#close()
     * close database connect
     */
    @Override
    public void close() throws Exception {
        try {
            super.close();
            if (connect != null) {
                connect.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
