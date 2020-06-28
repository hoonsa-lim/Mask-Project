package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	static final String DRIVER = "org.mariadb.jdbc.Driver";
	static final String URL = "jdbc:mariadb://jbstv.synology.me:3307/mbmdb";

	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(URL, "mbmdb", "Kssbyklhs!1");
		if(con != null) {
			System.out.println("Driver load 성공 : " + con);
		}else {
			System.out.println("Driver load 실패 : " + con);
		}
		return con;
	}
}
