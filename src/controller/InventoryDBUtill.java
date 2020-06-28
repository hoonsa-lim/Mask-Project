package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class InventoryDBUtill {
	public static final String DRIVER = "org.mariadb.jdbc.Driver";
	public static final String URL = "jdbc:mariadb://jbstv.synology.me:3307/mbmdb";
	
	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(URL,"mbmdb","Kssbyklhs!1");
		return con;
	}
}


//public class InventoryDBUtill {
//public static final String DRIVER = "com.mysql.jdbc.Driver";
//public static final String URL = "jdbc:mysql://localhost/mbmdb";
//
//public static Connection getConnection() throws Exception {
//	Class.forName(DRIVER);
//	Connection con = DriverManager.getConnection(URL, "root", "123456");
//	System.out.println("DB 연결 성공");
//	return con;
//}
//
//}