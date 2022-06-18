package com.java.DB.Novel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.java.DB.User.MemberDAO;
import com.java.DB.common.*;

public class ContextDAO extends JDBConnect {
	public ContextDAO() {
	}

	// 검색 조건에 맞는 게시물의 개수를 반환합니다.
	public int selectCount(String novel) {
		int totalCount = 0; // 결과(게시물 수)를 담을 변수

		// 게시물 수를 얻어오는 쿼리문 작성
		String query = "SELECT COUNT(*) FROM context" + " WHERE code = (select num from novel where title = ?)";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, novel); // 쿼리문 생성
			rs = psmt.executeQuery(query); // 쿼리 실행
			rs.next(); // 커서를 첫 번째 행으로 이동
			totalCount = rs.getInt(1); // 첫 번째 칼럼 값을 가져옴
		} catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}

		return totalCount;
	}

	// 검색 조건에 맞는 게시물 목록을 반환합니다.
	public List<ContextDTO> selectList(String input) {
		List<ContextDTO> bbs = new Vector<ContextDTO>(); // 결과(게시물 목록)를 담을 변수
		// 쿼리문 템플릿
		String query = "SELECT * FROM context ";
		query += "WHERE code = (select num from novel where title = ?)";

		query += "ORDER BY num";

		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, input);
			// 쿼리문 실행
			rs = psmt.executeQuery();
			int rownum = 0;
			
			while (rs.next()) {
				// 한 행(게시물 하나)의 데이터를 DTO에 저장
				ContextDTO dto = new ContextDTO();
				dto.setNum(rs.getInt("num")); // 일련번호
				dto.setTitle(rs.getString("title")); // 제목
				dto.setContext(rs.getString("context")); // 내용
				dto.setVisitcount(rs.getInt("visitcount")); // 조회수
				dto.setRownum(rownum);
				rownum++;

				// 반환할 결과 목록에 게시물 추가
				bbs.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		// 목록 반환
		return bbs;
	}

	public ContextDTO selectNovel(String input) {
		ContextDTO dto = new ContextDTO(); // 결과(게시물 목록)를 담을 변수
		// 쿼리문 템플릿
		String query = "SELECT * FROM context ";
		query += "WHERE title = ?";

		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, input);
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				// 한 행(게시물 하나)의 데이터를 DTO에 저장
				dto.setNum(rs.getInt("num")); // 일련번호
				dto.setTitle(rs.getString("title")); // 제목
				dto.setContext(rs.getString("context")); // 내용
				dto.setVisitcount(rs.getInt("visitcount")); // 조회수
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	// 게시글 데이터를 받아 DB에 추가합니다.
	public int insertWrite(ContextDTO dto) {
		int result = 0;

		try {
			// INSERT 쿼리문 작성
			String query = "INSERT INTO context ( " + " code,title,context,visitcount) " + " VALUES ( "
					+ "  ?, ?, ?, 0)";

			psmt = con.prepareStatement(query); // 동적 쿼리
			psmt.setInt(1, dto.getCode());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getContext());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}

		return result;
	}

	// 지정한 게시물을 수정합니다.
	public int updateEdit(ContextDTO dto) {
		int result = 0;

		try {
			// 쿼리문 템플릿
			String query = "UPDATE context SET " + " title=?, context=? " + " WHERE num=?";

			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContext());
			psmt.setInt(3, dto.getNum());

			// 쿼리문 실행
			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생");
			e.printStackTrace();
		}

		return result; // 결과 반환
	}

	// 지정한 게시물을 찾아 내용을 반환합니다.
	public ContextDTO contextData(String novel, String title, String context) {
		ContextDTO dto = new ContextDTO();
		NovelDAO cdao = new NovelDAO();
		int code = cdao.searchCode(novel);

		// 쿼리문 준비
		String query = "SELECT num " + " FROM context " + " WHERE title=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, title); // 인파라미터를 일련번호로 설정
			rs = psmt.executeQuery(); // 쿼리 실행

			// 결과 처리
			if (rs.next()) {
				dto.setNum(rs.getInt("num"));
			}
			dto.setCode(code);
			dto.setTitle(title);
			dto.setContext(context);
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	// 지정한 게시물의 조회수를 1 증가시킵니다.
	public void updateVisitCount(String title) {
		// 쿼리문 준비
		String query = "UPDATE context SET " + " visitcount=visitcount+1 " + " WHERE title=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, title);

			// 쿼리문 실행
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
	}

	public int searchCode(String context) {
		int code = 0;
		String query = "select num from context where title = ?";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, context);
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				code = rs.getInt("num");
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return code;
	}

	// 지정한 게시물을 삭제합니다.
	public int deleteContext(String context) {
		int result = 0;
		String query = "delete from context where title = ? ";
		try {
			CommentDAO dao = new CommentDAO();
			dao.deleteComment(searchCode(context));
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, context);
			// 쿼리문 실행
			result = psmt.executeUpdate();
			numbering();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result; // 결과 반환
	}

	public List<Integer> deleteList(int code) {
		List<Integer> result = new Vector<Integer>();
		String query = "select num from context where code = ?";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				result.add(rs.getInt("num"));
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result; // 결과 반환
	}

	public int deleteContext(int code) {
		int result = 0;
		String query = "delete from context where code = ?";
		try {
			CommentDAO dao = new CommentDAO();
			List<Integer> cList = deleteList(code);
			for (int i : cList) {
				dao.deleteComment(i);
			}
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
			// 쿼리문 실행
			result = psmt.executeUpdate();
			numbering();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result; // 결과 반환
	}

	public void numbering() {		 //auto_increment 숫자 초기화
		String query = "ALTER TABLE context AUTO_INCREMENT=1";
		String query2 = "SET @COUNT = 0";
		String query3 = "UPDATE context SET context.num = @COUNT:=@COUNT+1";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.executeUpdate();
			psmt = con.prepareStatement(query2);
			psmt.executeUpdate();
			psmt = con.prepareStatement(query3);
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
	}
}