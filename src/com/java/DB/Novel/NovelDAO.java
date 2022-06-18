package com.java.DB.Novel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.java.DB.User.MemberDAO;
import com.java.DB.common.*;

public class NovelDAO extends JDBConnect {
	public NovelDAO() {
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

	public List<NovelDTO> selectList() {		//소설의 열람횟수순으로 소설 리스트를 정렬하여 출력
		List<NovelDTO> bbs = new Vector<NovelDTO>();

		String query = "SELECT CASE WHEN SUM(context.visitcount) IS NULL THEN 0	ELSE SUM(context.visitcount) end as total, novel.title"
					 + " FROM novel LEFT JOIN context ON context.code = novel.num group by novel.num ORDER BY total DESC";

		try {
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();

			while (rs.next()) {
				NovelDTO dto = new NovelDTO();

				dto.setTitle(rs.getString("novel.title"));

				bbs.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		return bbs;
	}

	public List<NovelDTO> selectList(int index, String input) {		//유저 이름으로 또는 소설 제목으로 소설데이터 리스트 출력
		List<NovelDTO> bbs = new Vector<NovelDTO>();
		String query = "SELECT * FROM novel ";

		if (index == 0) query += "WHERE unum in (select num from user where uname like '%" + input + "%')";
		else query += "WHERE title like '%" + input + "%'";
		query += "ORDER BY num DESC";

		try {
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();

			while (rs.next()) {
				NovelDTO dto = new NovelDTO();
				dto.setNum(rs.getInt("num"));
				dto.setTitle(rs.getString("title"));
				dto.setUnum(rs.getInt("unum"));

				bbs.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return bbs;
	}

	public NovelDTO selectNovel(String input) {	//소설 제목으로 소설데이터 출력
		NovelDTO dto = new NovelDTO();
		
		String query = "SELECT * FROM novel ";
		query += "WHERE title = ?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, input);
			rs = psmt.executeQuery();

			while (rs.next()) {
				dto.setNum(rs.getInt("num")); 
				dto.setTitle(rs.getString("title")); 
				dto.setUnum(rs.getInt("unum"));
				dto.setIntroduce(rs.getString("introduce"));
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	public List<NovelDTO> selectMyList(String uname) {		//유저 이름으로 유저의 소설데이터 리스트 출력
		List<NovelDTO> bbs = new Vector<NovelDTO>();
		String query = "SELECT * FROM novel ";
		query += "WHERE unum = (select num from user where uname = ?)";

		query += "ORDER BY num DESC";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, uname);
			rs = psmt.executeQuery();

			while (rs.next()) {
				NovelDTO dto = new NovelDTO();
				dto.setNum(rs.getInt("num"));
				dto.setTitle(rs.getString("title")); 
				dto.setUnum(rs.getInt("unum"));

				bbs.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		return bbs;
	}

	public String searchNovel(String context) {		//소설 내용제목으로 소설제목 출력
		String bbs = "";
		String query = "SELECT title FROM novel ";
		query += "WHERE num = (SELECT code FROM context WHERE title = ?)";

		query += "ORDER BY num DESC";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, context);
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				bbs = rs.getString("title");
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		// 목록 반환
		return bbs;
	}
	
	public List<String> searchNovel(int user) {		//유저의 최근기록을 바탕으로 소설 리스트 출력
		List<String> bbs = new Vector<String>();
		String query = "SELECT novel.title FROM recent LEFT JOIN novel ON recent.novel = novel.num  WHERE recent.user = ? ORDER BY recent.num desc";

		try {
			psmt = con.prepareStatement(query);
			psmt.setInt(1, user);
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				bbs.add(rs.getString("title"));
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		return bbs;
	}


	public int insertWrite(NovelDTO dto) {	// 소설데이터로 소설 생성
		int result = 0;

		try {
			String query = "INSERT INTO novel ( " + " unum,title,introduce) " + " VALUES ( " + "  ?, ?, ?)";

			psmt = con.prepareStatement(query);
			psmt.setInt(1, dto.getUnum());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getIntroduce());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
 
		return result;
	}


	public NovelDTO novelData(String unum, String title, String introduce) {	// 소설 데이터(유저이름, 소설제목, 소설 소개)
		NovelDTO dto = new NovelDTO();
		MemberDAO cdao = new MemberDAO();
		int user = cdao.searchUserCode(unum);
		dto.setUnum(user);
		dto.setTitle(title);
		dto.setIntroduce(introduce);
		
		return dto;
	}
	
    public int searchCode(String novel) {	//소설이름으로 소설 코드받아오기
    	int code = 0;
    	String query = "select num from novel where title = ?";
    	try {
            psmt = con.prepareStatement(query);
			psmt.setString(1, novel);
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	code = rs.getInt("num");
            }
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return code;
    }
	
	public int deleteNovel(String novel) {		//소설 삭제
		int result = 0;        
        String query = "delete from novel where title = ? ";
        try {
            ContextDAO dao = new ContextDAO();
            dao.deleteContext(searchCode(novel)); 
            psmt = con.prepareStatement(query);
			psmt.setString(1, novel);
            result = psmt.executeUpdate();
            numbering();
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        return result;
	}
	
	public void numbering() {      	 //auto_increment 숫자 초기화
        String query = "ALTER TABLE novel AUTO_INCREMENT=1";
        String query2 =  "SET @COUNT = 0";
        String query3 = "UPDATE novel SET novel.num = @COUNT:=@COUNT+1";
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
