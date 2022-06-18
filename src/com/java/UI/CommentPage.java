package com.java.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.xml.stream.events.Comment;

import com.java.DB.Novel.CommentDAO;
import com.java.DB.Novel.CommentDTO;
import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;
import com.java.DB.User.MemberDAO;
import com.java.DB.User.MemberDTO;
import com.java.common.Users;

public class CommentPage extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);
	
	String nname;
	CommentDAO dao;
	List<CommentDTO> dtos;
	
	public CommentPage(String name) {
		setTitle("댓글");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));
		
		nname = name;

		JPanel north = new JPanel();
		JPanel center = new JPanel(new BorderLayout());
		JPanel menu = new JPanel(new BorderLayout());
		JPanel main = new JPanel(new BorderLayout(0,40));
		
		JLabel uName = new JLabel(nname);
		uName.setFont(font);
		north.add(uName);
		north.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 0));
		
		dao = new CommentDAO();
		dtos = dao.selectList(nname);

		DefaultListModel<String> list = new DefaultListModel<>();

		if (dtos.isEmpty())
			list.addElement("작성된 댓글이 없습니다.");
		else {

			for (CommentDTO dto : dtos) {
				list.addElement(dto.getComment());
			}
		}

        JList<String> lst = new JList<String>(list);
        lst.setVisibleRowCount(10);
        lst.setFixedCellHeight(20);
        lst.setFixedCellWidth(300);
        JScrollPane commentList = new JScrollPane(lst, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        commentList.setPreferredSize(new Dimension(300,350));

        JTextArea cText = new JTextArea();
		cText.setLineWrap(true);
		JScrollPane text = new JScrollPane(cText);
		text.setPreferredSize(new Dimension(300,80));
        
        main.add(commentList, BorderLayout.CENTER);
        main.add(text, BorderLayout.SOUTH);
        main.setBorder(BorderFactory.createEmptyBorder(0 , 20 , 0 , 20));
        		
		JButton commit = new JButton("댓글쓰기");
        
        menu.add(commit, BorderLayout.EAST);
        menu.setBorder(BorderFactory.createEmptyBorder(10 , 0 , 20 , 20));
        
        
        center.add(main, BorderLayout.CENTER);
        center.add(menu, BorderLayout.SOUTH);
        
        contentPane.add(north, BorderLayout.NORTH);
        contentPane.add(center, BorderLayout.CENTER);
        
		setSize(350, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);
		
		commit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "댓글을 작성하시겠습니까?", "알림",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					CommentDAO cdao = new CommentDAO();
					CommentDTO cdto = new CommentDTO();
					Users uclass = Users.getUserClass();
					String code = nname;
					String talker = uclass.getUsername();
					String comment = cText.getText();
					cdto = cdao.commentData(code, talker, comment);
					int result = dao.insertWrite(cdto);
					if (1 == result) {
						JOptionPane.showMessageDialog(null, "댓글을 작성했습니다.", "성공", JOptionPane.DEFAULT_OPTION);
						setVisible(false);
						new CommentPage(nname);
					}
					 else JOptionPane.showMessageDialog(null, "실패했습니다.", "실패", JOptionPane.ERROR_MESSAGE);		
					cdao.close();
					dao.close();
				} else {}
			}
		});
	}
}
