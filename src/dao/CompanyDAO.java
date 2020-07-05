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

public class CompanyDAO {
	// company List �ʱ�ȭ
	public ArrayList<CompanyModel> companyListUp(String production_consumption) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<CompanyModel> arrayListCompany = null;

		try {
			con = DBUtil.getConnection();
			String query = null;
			// company List �ʱ�ȭ
			query = "select * from companyTBL where production_consumption = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, production_consumption);
			rs = ps.executeQuery();

			// model�� ������ ObservableList
			arrayListCompany = new ArrayList<CompanyModel>();

			// resultSet ���� ��, arrayList�� ����
			while (rs.next()) {
				CompanyModel companyMd = new CompanyModel(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				arrayListCompany.add(companyMd);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� resultSet�� return�޴� �������� ���� �߻�");
			alert.setContentText("companyListUp() \n" + e.getStackTrace());
			alert.showAndWait();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			}
		}
		return arrayListCompany;
	}


	// �޺��ڽ� ����
	public ArrayList<CompanyModel> contractListUp(String production_consumption, String contract) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<CompanyModel> arrayListCompany = null;

		try {
			con = DBUtil.getConnection();
			String query = null;
			// �޺��ڽ� ����
			query = "select * from companyTBL where production_consumption = ? and contract = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, production_consumption);
			ps.setString(2, contract);
			rs = ps.executeQuery();

			// model�� ������ ObservableList
			arrayListCompany = new ArrayList<CompanyModel>();

			// resultSet ���� ��, arrayList�� ����
			while (rs.next()) {
				CompanyModel companyMd = new CompanyModel(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), production_consumption);
				arrayListCompany.add(companyMd);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� resultSet�� return�޴� �������� ���� �߻�");
			alert.setContentText("contractListUp() \n" + e.getStackTrace());
			alert.showAndWait();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			}
		}
		return arrayListCompany;
	}

	// ���
	public int CompanyRegistration(CompanyModel cm) {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			con = DBUtil.getConnection();
			String query = "insert into companyTBL values(?,?,?,?,?,?,?)";
			ps = con.prepareStatement(query); 
			ps.setString(1, cm.getCompany_number());
			ps.setString(2, cm.getCompany_name());
			ps.setString(3, cm.getManager());
			ps.setString(4, cm.getManager_phone());
			ps.setString(5, cm.getContract());
			ps.setString(6, cm.getAddress());
			ps.setString(7, cm.getProduction_consumption());
			result = ps.executeUpdate();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
			alert.setContentText("CompanyRegistration() \n" + e.getStackTrace());
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

	// ���� ���� ����
	public int CompanyEdit(CompanyModel cm) {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			con = DBUtil.getConnection();
			String query = "update companyTBL set company_number = ?, company_name = ?, manager = ?, "
					+ "manager_phone = ?, contract = ?, address = ?, production_consumption = ? where company_number = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, cm.getCompany_number());
			ps.setString(2, cm.getCompany_name());
			ps.setString(3, cm.getManager());
			ps.setString(4, cm.getManager_phone());
			ps.setString(5, cm.getContract());
			ps.setString(6, cm.getAddress());
			ps.setString(7, cm.getProduction_consumption());
			ps.setString(8, cm.getCompany_number());

			result = ps.executeUpdate();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
			alert.setContentText("CompanyRegistration() \n" + e.getStackTrace());
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

	// ����
	public int CompanyDelete(CompanyModel cm) {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			con = DBUtil.getConnection();
			String query = "delete from companyTBL where company_number = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, cm.getCompany_number());

			result = ps.executeUpdate();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
			alert.setContentText("CompanyRegistration() \n" + e.getStackTrace());
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

	// �˻�
	public ArrayList<CompanyModel> CompanyFind(String companyNumber, String production_consumption) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<CompanyModel> arrayList = null;

		try {
			con = DBUtil.getConnection();
			String query = "select * from companyTBL where company_number like ? and production_consumption =  ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + companyNumber + "%");
			pstmt.setString(2, production_consumption);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<CompanyModel>();
			while (rs.next()) {
				CompanyModel cm = new CompanyModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
				arrayList.add(cm);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����");
			alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� result ���� return�޴� �������� ���� �߻�");
			alert.setContentText("CompanyRegistration() \n" + e.getStackTrace());
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


	// inventory Stage �޺��ڽ� company List �ʱ�ȭ
		public ArrayList<CompanyModel> inventoryStageCompanyComboboxListUp(String production_consumption) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList<CompanyModel> arrayListCompany = null;

			try {
				con = DBUtil.getConnection();
				String query = null;
				// company List �ʱ�ȭ
				query = "select * from companyTBL where contract = '�����' and production_consumption = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, production_consumption);
				rs = ps.executeQuery();

				// model�� ������ ObservableList
				arrayListCompany = new ArrayList<CompanyModel>();

				// resultSet ���� ��, arrayList�� ����
				while (rs.next()) {
					CompanyModel companyMd = new CompanyModel(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
					arrayListCompany.add(companyMd);
				}
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("����");
				alert.setHeaderText("DB���� �Ǵ� ������ ���� �Ǵ� resultSet�� return�޴� �������� ���� �߻�");
				alert.setContentText("companyListUp() \n" + e.getStackTrace());
				alert.showAndWait();
			} finally {
				try {
					if (ps != null)
						ps.close();
					if (con != null)
						con.close();
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
				}
			}
			return arrayListCompany;
		}
}
