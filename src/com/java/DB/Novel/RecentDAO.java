package com.java.DB.Novel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.java.DB.User.MemberDAO;
import com.java.DB.common.*;

public class RecentDAO extends JDBConnect {
	public RecentDAO() {
	}

	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;

		String query = "SELECT COUNT(*) FROM novel";

		try {
			stmt = con.createStatement(); 
			rs = stmt.executeQuery(query); 
			rs.next(); 
			totalCount = rs.getInt(1); 
		} catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}

		return totalCount;
	}

	public void insertWrite(RecentDTO dto) {	//최근에 읽은 소설입력

		try {

			String query = "INSERT INTO recent ( " + " user, novel) " + " VALUES ( " + "  ?, ?)";

			psmt = con.prepareStatement(query); 
			psmt.setInt(1, dto.getUser());
			psmt.setInt(2, dto.getNovel());

			psmt.executeUpdate();
			numbering();
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	public RecentDTO novelData(String uname, String novel) {	//소설 데이터(유저이름, 소설코드)
		RecentDTO dto = new RecentDTO();
		MemberDAO cdao = new MemberDAO();
		int user = cdao.searchUserCode(uname);
		dto.setUser(user);
		NovelDAO ndao = new NovelDAO();
		int title = ndao.searchCode(novel);
		dto.setNovel(title);
		
		return dto;
	}
	
	public void overlap(int novel) {	//읽은 소설 최신화
		String query = "delete from recent where novel = ? ";
        try {

            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setInt(1, novel);
            // 쿼리문 실행 
            psmt.executeUpdate();
            numbering();
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
	}
	
	public void numbering() {       //auto_increment 숫자 초기화
        String query = "ALTER TABLE recent AUTO_INCREMENT=1";
        String query2 =  "SET @COUNT = 0";
        String query3 = "UPDATE recent SET recent.num = @COUNT:=@COUNT+1";
        try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
            psmt.executeUpdate();
            psmt = con.prepareStatement(query2);
            psmt.executeUpdate();
            psmt = con.prepareStatement(query3);
            psmt.executeUpdate();
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
	}
}
