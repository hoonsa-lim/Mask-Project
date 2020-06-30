package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.AdminModel;
import model.CompanyModel;
import model.TradeListModel;

public class AdminDAO {
	// tradeList 초기화
	public ArrayList<AdminModel> adminConnect() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<AdminModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// 판매 리스트 중에서 특정년도(?)의 월별 매출을 가져옴
			String query = "select * from employeeTBL;";
			
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();

			arrayList = new ArrayList<AdminModel>();
			while (rs.next()) {
				AdminModel am = new AdminModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				arrayList.add(am);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("adminConnect() \n" + e.getStackTrace());
			alert.showAndWait();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("자원반납 에러 : " + e.getMessage());
			}
		}
		return arrayList;
	}
}
