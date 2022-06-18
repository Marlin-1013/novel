package com.java.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import com.java.DB.Novel.CommentDTO;
import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;

public class NewNovel extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);

	public NewNovel(String user) {
		setTitle("신규소설");

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 10));

		JPanel south = new JPanel(new GridLayout(1, 2));
		JPanel center = new JPanel(new BorderLayout());

		JPanel title = new JPanel(new BorderLayout());

		JLabel tLabel = new JLabel("작품명");
		tLabel.setFont(font);
		tLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		tLabel.setPreferredSize(new Dimension(300, 30));

		JTextField tText = new JTextField();
		tText.setPreferredSize(new Dimension(300, 40));

		title.add(tLabel, BorderLayout.NORTH);
		title.add(tText, BorderLayout.CENTER);

		JPanel context = new JPanel(new BorderLayout());

		JLabel cLabel = new JLabel("작품소개");
		cLabel.setFont(font);
		cLabel.setPreferredSize(new Dimension(300, 40));
		cLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		JTextArea cText = new JTextArea();
		cText.setLineWrap(true);
		JScrollPane text = new JScrollPane(cText);
		text.setPreferredSize(new Dimension(300, 250));

		context.add(cLabel, BorderLayout.NORTH);
		context.add(text, BorderLayout.CENTER);
		context.setPreferredSize(new Dimension(300, 250));

		JPanel tag = new JPanel(new BorderLayout());

		JLabel ctLabel = new JLabel("분류태그");
		ctLabel.setFont(font);
		ctLabel.setPreferredSize(new Dimension(300, 40));
		ctLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JPanel ctCheck = new JPanel();

		JCheckBox chF = new JCheckBox("판타지");
		chF.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		JCheckBox chM = new JCheckBox("무협");
		chM.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		JCheckBox chG = new JCheckBox("게임");
		chG.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		JCheckBox chR = new JCheckBox("로맨스");
		chR.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		ctCheck.add(chF);
		ctCheck.add(chM);
		ctCheck.add(chG);
		ctCheck.add(chR);
		ctCheck.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		tag.add(ctLabel, BorderLayout.NORTH);
		tag.add(ctCheck, BorderLayout.CENTER);

		center.add(title, BorderLayout.NORTH);
		center.add(context, BorderLayout.CENTER);
		center.add(tag, BorderLayout.SOUTH);

		center.setSize(300, 100);
		center.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));

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
		south.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

		contentPane.add(center, BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);

		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "취소하시겠습니까?", "경고", JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					new MyNovelList(user);
					setVisible(false);
				} else {
				}
			}
		});
		save.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?", "알림", JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					NovelDAO dao = new NovelDAO();
					NovelDTO dto = new NovelDTO();
					String title = tText.getText();
					String introduce = cText.getText();
					dto = dao.novelData(user, title, introduce);
					int result = dao.insertWrite(dto);
					if (1 == result) {
						JOptionPane.showMessageDialog(null, "새 작품을 등록했습니다.", "성공", JOptionPane.DEFAULT_OPTION);
						new MyNovelList(user);
						setVisible(false);
					} else
						JOptionPane.showMessageDialog(null, "실패했습니다.", "실패", JOptionPane.ERROR_MESSAGE);
					dao.close();
				} else {}
			}
		});
	}
}
