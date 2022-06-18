package com.java.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import com.java.DB.Novel.ContextDAO;
import com.java.DB.Novel.ContextDTO;
import com.java.DB.Novel.NovelDAO;

public class WriteNovel extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);
	
	String nname;

	public WriteNovel(int mode, String name) {
		setTitle("소설쓰기");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));

		JPanel south = new JPanel(new GridLayout(1,2));
		JPanel center = new JPanel();
		
		JLabel tLabel = new JLabel("제목");
		tLabel.setFont(font);
		tLabel.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 5 , 0));
		tLabel.setPreferredSize(new Dimension(300,40));
		
		JTextField tText = new JTextField();
		tText.setPreferredSize(new Dimension(300,40));
		
		JLabel cLabel = new JLabel("본문");
		cLabel.setFont(font);
		cLabel.setPreferredSize(new Dimension(300,40));
		cLabel.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 5 , 0));
		

		JTextArea cText = new JTextArea();
		cText.setLineWrap(true);
		JScrollPane text = new JScrollPane(cText);
		text.setPreferredSize(new Dimension(300,340));
		
		center.add(tLabel);
		center.add(tText);
		center.add(cLabel);
		center.add(text);
		
		center.setBorder(BorderFactory.createEmptyBorder(20 , 10 , 0 , 10));		
        
		JPanel cContent = new JPanel();
		JLabel cancel = new JLabel("취소");
        cContent.add(cancel);
        cContent.setBorder(border);        
        
        JPanel sContent = new JPanel();
        JLabel save = new JLabel("저장");
        sContent.add(save);
        sContent.setBorder(border);
        
        south.add(cContent);
        south.add(sContent);
        south.setBorder(BorderFactory.createEmptyBorder(0 , 15 , 15 , 15));
        
        contentPane.add(center, BorderLayout.CENTER);
        contentPane.add(south, BorderLayout.SOUTH);
        
		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		if(mode == 0) {
			nname = name;
		}
		else {
			tText.setText(name);
			ContextDAO dao = new ContextDAO();
			ContextDTO dto = dao.selectNovel(name);
			cText.setText(dto.getContext());
			NovelDAO ndao = new NovelDAO();
			nname = ndao.searchNovel(name);
			dao.close();
			ndao.close();
		}

		setLocationRelativeTo(null);
		setVisible(true);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "취소하시겠습니까?", "경고",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					new MyNovelPage(nname);
					setVisible(false);
				} else {}
			}
		});
		save.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?", "알림",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					ContextDTO dto = new ContextDTO();
					ContextDAO dao = new ContextDAO();
					String title = tText.getText();
					String context = cText.getText();
					dto = dao.contextData(nname, title, context);
					if(mode == 0) {
						int result = dao.insertWrite(dto);
						if (1 == result) {
							JOptionPane.showMessageDialog(null, "새 작품을 등록했습니다.", "성공", JOptionPane.DEFAULT_OPTION);
							new MyNovelPage(nname);
							setVisible(false);
						}
						 else JOptionPane.showMessageDialog(null, "실패했습니다.", "실패", JOptionPane.ERROR_MESSAGE);					
					}
					else {
						int result = dao.updateEdit(dto);
						if (1 == result) {
							JOptionPane.showMessageDialog(null, "작품을 수정했습니다.", "성공", JOptionPane.DEFAULT_OPTION);
							new MyNovelPage(nname);
							setVisible(false);
						}
						 else JOptionPane.showMessageDialog(null, "실패했습니다.", "실패", JOptionPane.ERROR_MESSAGE);
					}
					dao.close();
				} else {}
			}
		});
	}
}
