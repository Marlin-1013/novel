package com.java.UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;
import com.java.DB.User.MemberDAO;

public class RecentPage extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static LineBorder border = new LineBorder(Color.black, 1, true);
	
	String[] list = {};
	String user;
	
	public RecentPage(String username){
		setTitle("최근기록");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,30));

		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel();

		user = username;
		
		JLabel uName = new JLabel(user);
		uName.setFont(font);
		north.add(uName, BorderLayout.EAST);
		north.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 20));
		
		NovelDAO dao = new NovelDAO();
		MemberDAO mdao = new MemberDAO();
		int unum = mdao.searchUserCode(user);
		List<String> dtos = dao.searchNovel(unum);
		DefaultListModel<String> list = new DefaultListModel<>();

		for (String dto : dtos) {
			list.addElement(dto);
		}
		
        JList<String> lst = new JList<String>(list);
        center.add(new JScrollPane(lst));
        lst.setVisibleRowCount(15);
        lst.setFixedCellHeight(30);
        lst.setFixedCellWidth(300);
        
        contentPane.add(north, BorderLayout.NORTH);
        contentPane.add(center, BorderLayout.CENTER);
        
		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);
		
		uName.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "메인 페이지로 돌아갑니다", "확인",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					new MainPage(user);
					setVisible(false);
				} else {}
			}
		});
		dao.close();
		mdao.close();
	}
}
