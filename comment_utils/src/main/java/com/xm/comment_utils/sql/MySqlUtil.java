package com.xm.comment_utils.sql;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class MySqlUtil {
	
	private static MySqlUtil instance = null;
	
	private Connection conn;
	RowSetFactory rowSetFactory;
	
	public MySqlUtil(MySqlUtil.Config config){
		try {
			Class.forName(config.getDriven());
			conn = DriverManager.getConnection(config.getUrl(),config.getUserName(),config.getPassword());
			rowSetFactory = RowSetProvider.newFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static MySqlUtil getInstance(MySqlUtil.Config config){
		if(instance == null) {
			instance = new MySqlUtil(config);
		}
		return instance;
	}
	
	public RowSet query(String sql,Object... param) throws SQLException{
		return (RowSet)excute("select",sql,false,param);
	}
	
	public Integer update(String sql,Object... param) throws SQLException{
		return (Integer)excute("update",sql,false,param);
	}
	public int updateWithKey(String sql,Object... param) throws SQLException{
		return (int)excute("update",sql,true,param);
	}
	
	private Object excute(String type,String sql,boolean autokey,Object... param){
		Object result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
	    try {
	    	CachedRowSet crs = rowSetFactory.createCachedRowSet(); 
			if(autokey){
				ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			}else{
				ps = conn.prepareStatement(sql);
			}
			for (int i = 0; i < param.length; i++) {
				if(param[i] instanceof String){
					ps.setString(i+1, (String)param[i]);
				}else if(param[i] instanceof Integer){
					ps.setInt(i+1, (Integer)param[i]);
				}else if(param[i] instanceof Date){
					ps.setDate(i+1, (Date)param[i]);
				}else if(param[i] instanceof Double){
					ps.setDouble(i+1, (Double)param[i]);
				}else if(param[i] instanceof Long){
					ps.setLong(i+1, (Long)param[i]);
				}else if(param[i] == null){
					ps.setNull(i+1, Types.INTEGER);
				}
			}
			if(type.equals("select")){
				rs = ps.executeQuery();
				crs.populate(rs);
				result = crs;
			}else{
				if(!autokey){
					result = ps.executeUpdate();
				}else{
					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					if(rs.next()){
						result = rs.getInt(1);
					}else{
						result = -1;
					}
				}
			}
	    } catch (SQLException e) {
	    	e.printStackTrace();
		} finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps != null){
					ps.close();
					ps = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public void close(ResultSet rs) throws SQLException{
		Statement st = rs.getStatement();
		try {
			rs.close();
			st.close();
		} catch (Exception e) {
			throw e;
		}
	}
	public static class Config{
		private String driven = "com.mysql.cj.jdbc.Driver";
		private String host = "127.0.0.1";
		private String port = "3306";
		private String database;
		private String userName = "root";
		private String password = "123456";
		private String url;
		
		public Config(){}
		
		public Config(String database){
			this.database = database;
		}
		public Config(String host,String database){
			this.host = host;
			this.database = database;
		}
		public Config(String host,String database,String username,String password){
			this.host = host;
			this.database = database;
			this.userName = username;
			this.password = password;
		}
		public String getDriven() {
			return driven;
		}
		public void setDriven(String driven) {
			this.driven = driven;
		}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public String getDatabase() {
			return database;
		}
		public void setDatabase(String database) {
			this.database = database;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUrl() {
			url = "jdbc:mysql://"+host+":"+port+"/"+database+"?useUnicode=true&characterEncoding=UTF8";
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}

