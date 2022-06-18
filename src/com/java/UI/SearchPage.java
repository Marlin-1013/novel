package com.java.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;
import com.java.DB.User.MemberDAO;
import com.java.DB.User.MemberDTO;

public class SearchPage extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static LineBorder border = new LineBorder(Color.black, 1, true);

	String[] list = {};
	String user;

	public SearchPage(String username) {
		setTitle("소설검색");

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 30));

		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel(new BorderLayout());

		user = username;

		JLabel uName = new JLabel(user);
		uName.setFont(font);
		north.add(uName, BorderLayout.EAST);
		north.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));

		JPanel search = new JPanel(new BorderLayout(5, 0));
		JPanel menu = new JPanel(new BorderLayout(0, 25));

		String[] value = { "유저명", "제목" };
		JComboBox<String> sItem = new JComboBox<String>(value);

		JTextField sValue = new JTextField();
		sValue.setPreferredSize(new Dimension(220, 10));

		search.add(sItem, BorderLayout.WEST);
		search.add(sValue, BorderLayout.EAST);

		JPanel btn = new JPanel();
		JButton sBtn = new JButton("검색");
		sBtn.setPreferredSize(new Dimension(100, 30));

		btn.add(sBtn);

		menu.add(search, BorderLayout.NORTH);
		menu.add(btn, BorderLayout.CENTER);

		JList<String> lst = new JList<String>(list);
		lst.setVisibleRowCount(12);
		lst.setFixedCellHeight(30);
		lst.setFixedCellWidth(300);

		center.add(menu, BorderLayout.NORTH);
		center.add(new JScrollPane(lst), BorderLayout.SOUTH);
		center.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		contentPane.add(north, BorderLayout.NORTH);
		contentPane.add(center, BorderLayout.CENTER);

		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);

		lst.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 1) {
					JList<String> target = (JList<String>) me.getSource();
					int index = target.locationToIndex(me.getPoint());
					if (index >= 0) {
						Object item = target.getModel().getElementAt(index);
						if(item.toString().equals("검색된 소설이 없습니다.")) {}
						else {
							new NovelMainPage(item.toString());
							setVisible(false);
						}
					}
				}
			}
		});

		sBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = sItem.getSelectedIndex();
				NovelDAO dao = new NovelDAO();
				List<NovelDTO> dtos = dao.selectList(index, sValue.getText());

				DefaultListModel<String> list = new DefaultListModel<>();

				if (dtos.isEmpty())
					list.addElement("검색된 소설이 없습니다.");
				else {

					for (NovelDTO dto : dtos) {
						list.addElement(dto.getTitle());
					}
				}
				lst.setModel(list);
				dao.close();
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
	}
}
