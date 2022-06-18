package com.java.DB.User;

import com.java.DB.common.*;

public class MemberDAO extends JDBConnect {
	public MemberDAO() {
		super();
	}
	
    public MemberDAO(String drv, String url, String id, String pw) {
        super(drv, url, id, pw);
    }

    public MemberDTO checkUser(String uid, String upw) {	// 아이디/패스워드와 일치하는 회원 정보를 반환
        MemberDTO dto = new MemberDTO();
        String query = "SELECT * FROM user WHERE uid=? AND upw=?";

        try {
            // 쿼리 실행
            psmt = con.prepareStatement(query); 
            psmt.setString(1, uid);    
            psmt.setString(2, upw);  
            rs = psmt.executeQuery(); 

            
            if (rs.next()) {
                
                dto.setNum(rs.getInt("num"));
                dto.setUid(rs.getString("uid"));
                dto.setUpw(rs.getString("upw"));
                dto.setUname(rs.getString("uname"));
            }
        }
        catch (Exception e) {

        }

        return dto; 
    }
    
    public int signUp(String name, String id, String pw) {	// 이름/아이디/패스워드 값을 입력하여 회원가입
    	String namecheck = "SELECT * FROM user WHERE uname=?";	//이름중복체크
    	String idcheck = "SELECT * FROM user WHERE uid=?";		//아이디중복체크
        String query = "insert into user(uid, upw, uname) values (?,?,?)";
        
    	try {
			psmt = con.prepareStatement(namecheck);
			psmt.setString(1, name);
            rs = psmt.executeQuery();    
			
            if(rs.next()) {
            	return 1;
            }
            else {
            	psmt = con.prepareStatement(idcheck);
            	psmt.setString(1, id);
            	rs = psmt.executeQuery();
            	
            	if(rs.next()) {
            		return 2;
            	}
            	else {
                    psmt = con.prepareStatement(query);
            		psmt.setString(1, id);
            		psmt.setString(2, pw);
            		psmt.setString(3, name);
            		int resultQuery = psmt.executeUpdate();
        			if(1 != resultQuery) return 3;
        			else return 0;
            	}
            }
		} catch (Exception e) {
			e.printStackTrace();
			return 4;
		}   	
    }
    
    public String pwSearch(String name, String id) {	//이름/아이디로 비밀번호 출력
    	String query = "SELECT * FROM user WHERE uid=?";
    	try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("uname").equals(name)) {
					
					return rs.getString("upw");
				}
				else return "2";
			}
			else return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "3";
		}	
    }
    
    public String searchUser(String name) {		//소설 제목으로 유저이름출력
    	String query = "SELECT uname FROM user WHERE num= (select unum from novel where title = ?)";
    	try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, name);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("uname");
			}
			else return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "3";
		}	
    }
    
    public int searchUserCode(String user) {	//유저 이름으로 유저코드 받아오기
    	int talker = 0;
    	String query = "select num from user where uname = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, user);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	talker = rs.getInt("num");
            }
    	} 
        catch (Exception e) {
            System.out.println("유저 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return talker;
    }
}
