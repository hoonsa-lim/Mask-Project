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
	// tradeList �ʱ�ȭ
	public ArrayList<TradeListModel> tradeListUp(String purchaseOrSell) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayListTrade = null;

		try {
			con = DBUtil.getConnection();
			String query = null;
			// trade List �ʱ�ȭ

			query = "select t.no, t.order_quantity, t.total_price, t.date, t.ps, c.company_name, i.product_name, i.product_number "
					+ "from companyTBL c " + "inner join tradelistTBL t " + "on c.company_number = t.c_cnumber_fk "
					+ "inner join inventoryTBL i " + "on t.i_pnumber_fk=i.Product_number " + "where ps = ? "
					+ "order by date ASC";
			ps = con.prepareStatement(query);
			ps.setString(1, purchaseOrSell);
			rs = ps.executeQuery();

			// model�� ������ ObservableList
			arrayListTrade = new ArrayList<TradeListModel>();

			// resultSet ���� ��, arrayList�� ����
			while (rs.next()) {
				TradeListModel tradeMd = new TradeListModel(rs.getInt(1), rs.getInt(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				arrayListTrade.add(tradeMd);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� resultSet�� return�޴� �������� ���� �߻�");
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

	// �˻�
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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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
				System.out.println("�ڿ��ݳ� ���� : " + e.getMessage());
			}
		}
		return arrayList;
	}

	// ����Ʈ ��Ŀ �˻�
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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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
				System.out.println("�ڿ��ݳ� ���� : " + e.getMessage());
			}
		}
		return arrayList;
	}

	// �ֹ�,�Ǹ� ��� - ������ ȭ�鿡�� �ֹ�, �Ǹ� �� tradeList�� ��� �Լ�
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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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

	// report â���� �޺��ڽ� �ʱ�ȭ �Լ�
	public ArrayList<TradeListModel> reportCombocox() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// ������ �߻��� ����, ���� ������
			String query = "select date \r\n" + "from (select date from tradelistTBL where ps = '�Ǹ�')SELL \r\n"
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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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
				System.out.println("�ڿ��ݳ� ���� : " + e.getMessage());
			}
		}
		return arrayList;
	}

	// report â���� ���� ���ÿ� ���� �׷���
	public ArrayList<TradeListModel> reportCombocoxYearSales(String year) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// �Ǹ� ����Ʈ �߿��� Ư���⵵(?)�� ���� ������ ������
			String query = "select date, sum(total_price)  from (select * from tradelistTBL where ps = '�Ǹ�')SELL group by year(date), month(date) having year(date) = ?";

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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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
				System.out.println("�ڿ��ݳ� ���� : " + e.getMessage());
			}
		}
		return arrayList;
	}

	// report â���� �� �޺��ڽ� �̺�Ʈ
	public ArrayList<TradeListModel> reportCombocoxMonthSales(String year, String month) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();

			// �Ǹ� ���� ��, ������ �ߺ����� ������ �� ��, Ư�� �⵵, Ư�� ���� ��¥�� �� ����
			String query = "select date, sum(total_price)" + "from (select * from tradelistTBL where ps = '�Ǹ�')SELL "
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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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
				System.out.println("�ڿ��ݳ� ���� : " + e.getMessage());
			}
		}
		return arrayList;
	}

	// report â���� ���� ���ÿ� ���� �׷���
	public ArrayList<TradeListModel> reportInit(String year) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TradeListModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			// �Ǹ� ����Ʈ �߿��� Ư���⵵(?)�� ���� ������ ������
			String query = "select date, sum(total_price)  from (select * from tradelistTBL where ps = '�Ǹ�')SELL group by year(date), month(date) having year(date) = ?";

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
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
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
				System.out.println("�ڿ��ݳ� ���� : " + e.getMessage());
			}
		}
		return arrayList;
	}
}
