package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.DBUtil;
import controller.InventoryDBUtill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.CompanyModel;
import model.Inventory;


public class InventoryDAO {
	
	//판매
	public int getProductSell(Inventory iv) {
		Connection con = null;
		PreparedStatement pst = null;
		int returnValue = 0;
		
		try {
			con=InventoryDBUtill.getConnection();
			
			//3.con 객체를 가지고 쿼리문을 실행가능 (select, insert, update, delete)
			String query = "update inventoryTBL set stock =? where product_number =?";
			//4.쿼리문 실행을 위한 준비
			pst=con.prepareStatement(query);
			
			pst.setInt(1, iv.getStock());
			pst.setString(2, iv.getProductNumber());
			
			returnValue=pst.executeUpdate();
			
			if(returnValue!= 0) {
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("판매 성공");
				alert.setHeaderText("판매 성공");
				alert.setContentText("판매");
				alert.showAndWait();
			}else {
				throw new Exception("판매 실패");
			}
			
		} catch (Exception e1) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("판매 실패");
			alert.setHeaderText("판매 실패");
			alert.setContentText("Error : "+e1.getMessage().toString());
			alert.showAndWait();
		}finally {
				try {
					if(pst!= null) pst.close();
					if(con!= null) con.close();
				} catch (SQLException e2) {
					System.out.println(e2.getMessage());
				}
		}
			
		return returnValue;
	}
	
	//구매
	public int getProductPurchase(Inventory iv) {
		Connection con = null;
		PreparedStatement pst = null;
		
		int returnValue = 0;
		
		try {
			con=InventoryDBUtill.getConnection();
			
			//3.con 객체를 가지고 쿼리문을 실행가능 (select, insert, update, delete)
			String query = "update inventoryTBL set stock =? where product_number =?";
			
			//4.쿼리문 실행을 위한 준비
			pst=con.prepareStatement(query);
			
			pst.setInt(1, iv.getStock());
			pst.setString(2, iv.getProductNumber());
			
			returnValue=pst.executeUpdate();
			
			if(returnValue!= 0) {
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("구매 성공");
				alert.setHeaderText("구매 성공");
				alert.setContentText("구매");
				alert.showAndWait();
			}else {
				throw new Exception("구매 실패");
			}
			
		} catch (Exception e1) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("구매 실패");
			alert.setHeaderText("구매실패");
			alert.setContentText("Error : "+e1.getMessage().toString());
			alert.showAndWait();
		}finally {
				try {
					if(pst!= null) pst.close();
					if(con!= null) con.close();
				} catch (SQLException e2) {
					System.out.println(e2.getMessage());
				}
		}
			
		return returnValue;
	}

	//삭제
	public int getInventoryDelete (String no) {
		Connection con = null;
		PreparedStatement pst = null;
		int returnValue =0;
		
		try {
			con = InventoryDBUtill.getConnection();
			
			String query = "delete from inventoryTBL where product_number =?";
			
			pst = con.prepareStatement(query);
			pst.setString(1, no);
			
			returnValue = pst.executeUpdate();
			
			if(returnValue!= 0) {
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("삭제 성공");
				alert.setHeaderText(no+"번 삭제 성공");
				alert.setContentText(no+" 삭제");
				alert.showAndWait();
			}else {
				throw new Exception("삭제 불가");
			}
		}catch (Exception e) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Delete Error");
			alert.setHeaderText("Delete Error \n InventoryManagementController.handleBtnDeleteAction");
			alert.setContentText("주의"+e.getMessage());
			alert.showAndWait();
		}finally {
				try {
					if(pst!= null) pst.close();
					if(con!= null) con.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		}
		
		return returnValue;
	}
	
	//전체리스트
	public ArrayList<Inventory> getTotalLoadList() {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		ArrayList<Inventory> arrList = null;
		
		try {
			con = InventoryDBUtill.getConnection();
			
			String query = "select b.product_number, b.product_name, \r\n" + 
					"b.stock, b.purchase_price, b.sell_price, b.type, b.size, b.color, a.company_name, a.company_number\r\n" + 
					"from companyTBL a, inventoryTBL b\r\n" + 
					"where a.company_number = b.c_cnumber_fk";
			
			pst = con.prepareStatement(query);
			rs=pst.executeQuery();
			
			arrList=new ArrayList<Inventory>();
			while(rs.next()) {
				Inventory inventory = new Inventory(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),
						rs.getInt(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10));
				
				arrList.add(inventory);
			}
			
		}//try 
		catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("TotalList Error");
			alert.setHeaderText("TotalList Error");
			alert.setContentText("주의" + e.getMessage());
			alert.showAndWait();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return arrList;
	}
	
	//등록
	public int getInventoryRegist(Inventory iv){
		Connection con=null;
		PreparedStatement pst=null;
		int returnValue = 0;
		
		try {
			con=InventoryDBUtill.getConnection();
			
			//3.con 객체를 가지고 쿼리문을 실행가능 (select, insert, update, delete)
			String query = "insert into inventoryTBL values (?,?,?,?,?,?,?,?,?)";
			
			//4.쿼리문 실행을 위한 준비
			pst=con.prepareStatement(query);
			
			pst.setString(1, iv.getProductNumber());
			pst.setString(2, iv.getProduct());
			pst.setInt(3, iv.getStock());
			pst.setInt(4, iv.getPurchase());
			pst.setInt(5, iv.getSell());
			pst.setString(6, iv.getType());
			pst.setString(7, iv.getSize());
			pst.setString(8, iv.getColor());
			pst.setString(9, iv.getCompanyNumber());
			
			returnValue=pst.executeUpdate();

			if(returnValue!= 0) {
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("재고 등록 성공");
				alert.setHeaderText(iv.getProduct()+" 재고 등록 성공");
				alert.setContentText(iv.getProduct()+" 등록");
				alert.showAndWait();
			}else {
				throw new Exception("등록 오류");
			}
			
		}catch (Exception e) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("등록 오류");
			alert.setHeaderText("빈 칸을 모두 입력하시오");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}finally {
				try {
					if(pst!= null) pst.close();
					if(con!= null) con.close();
				} catch (SQLException e) {
					Alert alert=new Alert(AlertType.ERROR);
					alert.setTitle("Insert Error");
					alert.setHeaderText("Insert Error");
					alert.setContentText("올바른 값을 입력하시오.1 "+e.getMessage());
					alert.showAndWait();
				}
		}
			
		return returnValue;
	}

	//정렬
	public ArrayList<CompanyModel> companyListUp2(String company_name) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<CompanyModel> arrayListCompany = null;

		try {
			con = DBUtil.getConnection();
			String query = null;
			// company List 초기화
			query = "select * from companyTBL where company_name = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, company_name);
			rs = ps.executeQuery();

			// model을 저장할 ObservableList
			arrayListCompany = new ArrayList<CompanyModel>();

			// resultSet 값을 모델, arrayList에 저장
			while (rs.next()) {
				CompanyModel companyMd = new CompanyModel(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				arrayListCompany.add(companyMd);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("DB접속 또는 쿼리문 실행 또는 resultSet을 return받는 과정에서 오류 발생");
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
	
	//제품번호로 검색
	public ArrayList<Inventory> getProductSearch(String pNumber){
		ArrayList<Inventory> arrList = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = InventoryDBUtill.getConnection();
			
			String query = "select b.product_number, b.product_name, \r\n" + 
					"b.stock, b.purchase_price, b.sell_price, b.type, b.size, b.color, a.company_name, a.company_number\r\n" + 
					"from companyTBL a, inventoryTBL b\r\n" + 
					"where a.company_number = b.c_cnumber_fk \r\n" +
					"having product_number like ?";
			pst = con.prepareStatement(query);
			pst.setString(1,"%"+ pNumber +"%");
			
			rs= pst.executeQuery();
			
			arrList = new ArrayList<Inventory>();
			while (rs.next()) {
				Inventory iv = new Inventory(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),
						rs.getInt(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10));
				arrList.add(iv);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Search Error");
			alert.setHeaderText("Search Error");
			alert.setContentText("주의" + e.getMessage());
			alert.showAndWait();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				System.out.println(e2.getMessage());
			}
		}
		
		
		return arrList;
	}
	
	//수정
	public int getInventoryUpdate(Inventory iv) {
		Connection con=null;
		PreparedStatement pst=null;
		int returnValue = 0;
		
		try {
			con=InventoryDBUtill.getConnection();
			
			String query = "update inventoryTBL set product_name =?, purchase_price=?, sell_price=?, type=?, size=?, "
					+ "color=? where product_number =?";
			pst=con.prepareStatement(query);
			
			pst.setString(1, iv.getProduct());
			pst.setInt(2, iv.getPurchase());
			pst.setInt(3, iv.getSell());
			pst.setString(4, iv.getType());
			pst.setString(5, iv.getSize());
			pst.setString(6, iv.getColor());
			pst.setString(7, iv.getProductNumber());
			
			returnValue=pst.executeUpdate();
			
			if(returnValue!= 0) {
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("수정 성공");
				alert.setHeaderText(iv.getProductNumber()+"번 수정 성공");
				alert.setContentText(iv.getProduct()+"수정");
				alert.showAndWait();
			}else {
				throw new Exception("수정 실패");
			}
			
		}catch(Exception e) {
			
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Edit Error");
			alert.setHeaderText("Edit Error");
			alert.setContentText("Error : "+e.getMessage());
			alert.showAndWait();
		}finally {
				try {
					if(pst!= null) pst.close();
					if(con!= null) con.close();
				} catch (SQLException e2) {
					System.out.println(e2.getMessage());
				}
		}
		
		return returnValue;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
