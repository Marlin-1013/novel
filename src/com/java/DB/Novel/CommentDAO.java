package com.java.DB.Novel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.java.DB.User.MemberDAO;
import com.java.DB.User.MemberDTO;
import com.java.DB.common.*;

public class CommentDAO extends JDBConnect {
    public CommentDAO() {
    }

    public int selectCount(Map<String, Object> map) {	    // 검색 조건에 맞는 게시물의 개수를 반환합니다.
        int totalCount = 0;

        
        String query = "SELECT COUNT(*) FROM novel";
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                   + " LIKE '%" + map.get("searchWord") + "%'";
        }

        try {
            stmt = con.createStatement();   
            rs = stmt.executeQuery(query);  
            rs.next();  
            totalCount = rs.getInt(1);
        }
        catch (Exception e) {
            System.out.println("게시물 수를 구하는 중 예외 발생");
            e.printStackTrace();
        }

        return totalCount; 
    }
    
    public List<CommentDTO> selectList(String input) {     // 소설 내용으로 댓글데이터 받아옴
    	List<CommentDTO> bbs = new Vector<CommentDTO>();
 
        String query = "SELECT * FROM comment ";
            query += "WHERE code = (select num from context where title = ?)";
        
        query += "ORDER BY num";
        
        try {
            psmt = con.prepareStatement(query);
			psmt.setString(1, input);
            rs = psmt.executeQuery();
            
            while (rs.next()) {
                CommentDTO dto = new CommentDTO();
                dto.setNum(rs.getInt("num"));         		 
                dto.setCode(rs.getInt("code"));     		 
                dto.setTalker(rs.getInt("talker")); 		
                dto.setComment(rs.getString("comment"));

                bbs.add(dto);
            }
        } 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        
        return bbs;
    }
 
    public int insertWrite(CommentDTO dto) {	//댓글쓰기
        int result = 0;
        
        try {
            String query = "INSERT INTO comment ( "
                         + " code,talker,comment) "
                         + " VALUES ( "
                         + "  ?, ?, ?)";  

            psmt = con.prepareStatement(query);
            psmt.setInt(1, dto.getCode());  
            psmt.setInt(2, dto.getTalker());
            psmt.setString(3, dto.getComment());  
            
            result = psmt.executeUpdate(); 
        }
        catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        
        return result;
    }
    
    public CommentDTO commentData(String context, String user, String comment) {	//댓글 데이터(댓글을 작성할 소설내용, 작성한 유저, 작성된 내용)
    	CommentDTO dto = new CommentDTO();
    	ContextDAO cdao = new ContextDAO();
    	MemberDAO mdao = new MemberDAO();
    	int code = cdao.searchCode(context);
    	int talker = mdao.searchUserCode(user);
    	dto.setCode(code);
    	dto.setTalker(talker);
    	dto.setComment(comment);
    	
    	return dto;
    }
    
    public int deleteComment(int code) { 	//댓글 삭제
    	int result = 0;        
        String query = "delete from comment where code = ?";
        try {
            psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
            result = psmt.executeUpdate();
            numbering();
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }
    
    public void numbering() {      //auto_increment 숫자 초기화
        String query = "ALTER TABLE comment AUTO_INCREMENT=1";
        String query2 =  "SET @COUNT = 0";
        String query3 = "UPDATE comment SET comment.num = @COUNT:=@COUNT+1";
        try {
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
