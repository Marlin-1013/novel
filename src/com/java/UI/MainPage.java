package com.java.UI;

import java.util.List;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;

public class MainPage extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static LineBorder border = new LineBorder(Color.black, 1, true);

	String user;

	public MainPage(String username) {
		setTitle("메인");

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 40));

		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel(new BorderLayout());
		JPanel menu = new JPanel(new GridLayout(1, 3));
		JPanel main = new JPanel();

		user = username;

		JLabel uName = new JLabel(user);
		uName.setFont(font);
		north.add(uName, BorderLayout.EAST);
		north.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));

		JPanel my = new JPanel();
		JLabel mn = new JLabel("내소설");
		my.add(mn);
		my.setBorder(border);

		JPanel se = new JPanel();
		JLabel sea = new JLabel("검색");
		se.add(sea);
		se.setBorder(border);

		JPanel res = new JPanel();
		JLabel rec = new JLabel("최근기록");
		res.add(rec);
		res.setBorder(border);

		menu.add(my);
		menu.add(se);
		menu.add(res);

		NovelDAO dao = new NovelDAO();
		List<NovelDTO> dtos = dao.selectList();
		DefaultListModel<String> list = new DefaultListModel<>();

		for (NovelDTO dto : dtos) {
			list.addElement(dto.getTitle());
		}

		JList<String> lst = new JList<String>(list);
		main.add(new JScrollPane(lst));
		lst.setVisibleRowCount(14);
		lst.setFixedCellHeight(30);
		lst.setFixedCellWidth(300);
		main.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

		center.add(menu, BorderLayout.NORTH);
		center.add(main, BorderLayout.CENTER);

		contentPane.add(north, BorderLayout.NORTH);
		contentPane.add(center, BorderLayout.CENTER);

		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);

		mn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new MyNovelList(user);
				setVisible(false);
			}
		});
		sea.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new SearchPage(user);
				setVisible(false);
			}
		});
		rec.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new RecentPage(user);
				setVisible(false);
			}
		});
		uName.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "경고",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					new LoginPage();
					setVisible(false);
				} else {}
			}
		});

		lst.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					JList<String> target = (JList<String>) me.getSource();
					int index = target.locationToIndex(me.getPoint());
					if (index >= 0) {
						Object item = target.getModel().getElementAt(index);
						new NovelMainPage(item.toString());
						setVisible(false);
					}
				}
			}
		});
		dao.close();
	}
}
