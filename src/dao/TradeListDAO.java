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

			query = "select t.no, t.order_quantity, t.total_price, t.date, t.ps, c.company_name, i.product_name, i.product_number "
					+ "from companyTBL c " + "inner join tradelistTBL t " + "on c.company_number = t.c_cnumber_fk "
					+ "inner join inventoryTBL i " + "on t.i_pnumber_fk=i.Product_number " + "where ps = ? "
					+ "order by date ASC";
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
	public ArrayList<TradeListModel> tradeListFind(String companyName, String purchaseOrSell) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			String query = "select t.no, t.order_quantity, t.total_price, t.date, t.ps, c.company_name, i.product_name, i.product_number "
					+ "from companyTBL c " + "inner join tradelistTBL t " + "on c.company_number = t.c_cnumber_fk "
					+ "inner join inventoryTBL i " + "on t.i_pnumber_fk=i.Product_number "
					+ "where company_name like ? and ps =  ? " + "order by date ASC";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + companyName + "%");
			pstmt.setString(2, purchaseOrSell);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<TradeListModel>();
			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
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
	public ArrayList<TradeListModel> findTradeListDate(String dateStart, String nowPurchaseOrSell) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;
		try {
			con = DBUtil.getConnection();
			String query = "select t.no, t.order_quantity, t.total_price, t.date, t.ps, c.company_name, i.product_name, i.product_number "
					+ "from companyTBL c " + "inner join tradelistTBL t " + "on c.company_number = t.c_cnumber_fk "
					+ "inner join inventoryTBL i " + "on t.i_pnumber_fk=i.Product_number "
					+ "where date >= ? and ps = ? "
					+ "order by date ASC";

			pstmt = con.prepareStatement(query);

			pstmt.setString(1, dateStart);
			pstmt.setString(2, nowPurchaseOrSell);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<TradeListModel>();

			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
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
	public int registrationPurchaseOrSell(int quantity, String total_price, String nowDate, String purchaseOrSell,
			String cNumber, String pNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			con = DBUtil.getConnection();
			String query = "insert into tradelistTBL values(null,?,?,?,?,?,?)";
			ps = con.prepareStatement(query);
			ps.setInt(1, quantity);
			ps.setString(2, total_price);
			ps.setString(3, nowDate);
			ps.setString(4, purchaseOrSell);
			ps.setString(5, cNumber);
			ps.setString(6, pNumber);

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

	// report 창에서 콤보박스 초기화 함수
	public ArrayList<TradeListModel> reportCombocox() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// 매출이 발생한 연도, 월을 가져옴
			String query = "select date \r\n" + "from (select date from tradelistTBL where ps = '판매')SELL \r\n"
					+ "group by year(date), month(date);";
			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();

			arrayList = new ArrayList<TradeListModel>();
			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getString(1));
				arrayList.add(tm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("reportCombocox() \n" + e.getStackTrace());
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

	// report 창에서 연도 선택에 따른 그래프
	public ArrayList<TradeListModel> reportCombocoxYearSales(String year) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// 판매 리스트 중에서 특정년도(?)의 월별 매출을 가져옴
			String query = "select date, sum(total_price)  from (select * from tradelistTBL where ps = '판매')SELL group by year(date), month(date) having year(date) = ?";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, year);
			rs = pstmt.executeQuery();

			arrayList = new ArrayList<TradeListModel>();
			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getString(1), rs.getString(2));
				arrayList.add(tm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("reportCombocox() \n" + e.getStackTrace());
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

	// report 창에서 월 콤보박스 이벤트
	public ArrayList<TradeListModel> reportCombocoxMonthSales(String year, String month) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();

			// 판매 내역 중, 연월의 중복값을 제외한 값 중, 특정 년도, 특정 월의 날짜와 총 매출
			String query = "select date, sum(total_price)" + "from (select * from tradelistTBL where ps = '판매')SELL "
					+ "group by year(date), month(date) " + "having year(date) = ? and month(date) = ?";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			rs = pstmt.executeQuery();

			arrayList = new ArrayList<TradeListModel>();
			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getString(1), rs.getString(2));
				arrayList.add(tm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("reportCombocox() \n" + e.getStackTrace());
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

	// report 창에서 연도 선택에 따른 그래프
	public ArrayList<TradeListModel> reportInit(String year) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// 판매 리스트 중에서 특정년도(?)의 월별 매출을 가져옴
			String query = "select date, sum(total_price)  from (select * from tradelistTBL where ps = '판매')SELL group by year(date), month(date) having year(date) = ?";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, year);
			rs = pstmt.executeQuery();

			arrayList = new ArrayList<TradeListModel>();
			while (rs.next()) {
				TradeListModel tm = new TradeListModel(rs.getString(1), rs.getString(2));
				arrayList.add(tm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 result 값을 return받는 과정에서 오류 발생");
			alert.setContentText("reportCombocox() \n" + e.getStackTrace());
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
