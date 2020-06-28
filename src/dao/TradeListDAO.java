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
import model.CompanyModel;
import model.TradeListModel;

public class TradeListDAO {
	// tradeList 초기화
	public ArrayList<TradeListModel> tradeListUp(String purchaseOrSell) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayListTrade = null;

		try {
			con = DBUtil.getConnection();
			String query = null;
			// trade List 초기화

			query = "select c.no, c.order_quantity, c.total_price, c.date, c.ps, a.company_name, b.product_name, b.product_number from companyTBL a inner join  inventoryTBL b on a.company_number = b.c_cnumber_fk inner join tradelistTBL c on b.product_number = c.i_pnumber_fk where ps = ? order by date ASC";
			ps = con.prepareStatement(query);
			ps.setString(1, purchaseOrSell);
			rs = ps.executeQuery();

			// model을 저장할 ObservableList
			arrayListTrade = new ArrayList<TradeListModel>();
			
			// resultSet 값을 모델, arrayList에 저장
			while (rs.next()) {
				TradeListModel tradeMd = new TradeListModel(rs.getInt(1), rs.getInt(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				arrayListTrade.add(tradeMd);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 resultSet을 return받는 과정에서 오류 발생");
			alert.setContentText("tradeListUp() \n" + e.getStackTrace());
			alert.showAndWait();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return arrayListTrade;
	}

	// 검색
	public ArrayList<TradeListModel> TradeListFind(String companyNumber, String purchaseOrSell) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			String query = "select * from tradelistTBL where company_number like ? and production_consumption =  ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + companyNumber + "%");
			pstmt.setString(2, purchaseOrSell);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<TradeListModel>();
			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				arrayList.add(tm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("TradeListFind() \n" + e.getStackTrace());
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

	// 데이트 피커 검색
	public ArrayList<TradeListModel> FindTradeListDate(String dateStart, String nowPurchaseOrSell) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from tradelistTBL where date >= ? and ps = ? order by date ASC";
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, dateStart);
			pstmt.setString(2, nowPurchaseOrSell);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<TradeListModel>();

			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				arrayList.add(tm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("DateDescendingOrderTradeListFind() \n" + e.getStackTrace());
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

	// 주문,판매 등록 - 재고관리 화면에서 주문, 판매 시 tradeList에 등록 함수
	public int registrationPurchaseOrSell(String nowDate, int quantity, String total_price, String purchaseOrSell) {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			con = DBUtil.getConnection();
			String query = "insert into tradelistTBL values(null,?,?,?,?)";
			ps = con.prepareStatement(query);
			ps.setInt(1, quantity);
			ps.setString(2, total_price);
			ps.setString(3, nowDate);
			ps.setString(4, purchaseOrSell);

			result = ps.executeUpdate();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("registrationPurchaseOrSell() \n" + e.getMessage());
			alert.showAndWait();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}

}
