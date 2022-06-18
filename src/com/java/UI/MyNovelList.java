package com.java.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import com.java.DB.Novel.ContextDAO;
import com.java.DB.Novel.ContextDTO;
import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;

public class MyNovelList extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);

	String user;
	Object item;
	JList<String> lst;

	public MyNovelList(String username) {
		setTitle("내 소설");

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 40));

		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel(new BorderLayout());
		JPanel menu = new JPanel(new BorderLayout());
		JPanel main = new JPanel();

		user = username;

		JLabel uName = new JLabel(user);
		uName.setFont(font);
		north.add(uName, BorderLayout.EAST);
		north.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));

		JPanel dNovel = new JPanel();
		JLabel dLabel = new JLabel("소설 삭제");
		dLabel.setForeground(Color.red);
		dNovel.add(dLabel);
		dNovel.setBorder(border);
		dNovel.setPreferredSize(new Dimension(120, 10));

		JPanel cNovel = new JPanel();
		JLabel cLabel = new JLabel("신규소설");
		cNovel.add(cLabel);
		cNovel.setBorder(border);

		menu.add(dNovel, BorderLayout.WEST);
		menu.add(cNovel, BorderLayout.CENTER);

		NovelDAO dao = new NovelDAO();
		List<NovelDTO> dtos = dao.selectMyList(user);

		DefaultListModel<String> list = new DefaultListModel<>();

		if (dtos.isEmpty()) {
			list.addElement("작성된 소설이 없습니다.");
		}
		else {

			for (NovelDTO dto : dtos) {
				list.addElement(dto.getTitle());
			}
		}

		lst = new JList<String>(list);
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

		cLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new NewNovel(user);
				setVisible(false);
			}
		});

		dLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int selected = lst.getSelectedIndex();
				item = lst.getModel().getElementAt(selected);
				int ans = JOptionPane.showConfirmDialog(null, item.toString() + "를 삭제하시겠습니까?", "경고",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					NovelDAO dao = new NovelDAO();
					String title = item.toString();
					int result = dao.deleteNovel(title);
					if (1 == result) {
						JOptionPane.showMessageDialog(null, "작품을 삭제했습니다.", "성공", JOptionPane.DEFAULT_OPTION);
						List<NovelDTO> dtos = dao.selectMyList(user);
						list.clear();
						for (NovelDTO dto : dtos) {
							list.addElement(dto.getTitle());
						}
						lst.setModel(list);
					}
					 else JOptionPane.showMessageDialog(null, "실패했습니다.", "실패", JOptionPane.ERROR_MESSAGE);
					dao.close();
				} else {}
			}
		});
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

		lst.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					JList<String> target = (JList<String>) me.getSource();
					int index = target.locationToIndex(me.getPoint());
					if (index >= 0) {
						item = target.getModel().getElementAt(index);
						new MyNovelPage(item.toString());
						setVisible(false);
					}
				}
			}
		});
		dao.close();
	}
}
