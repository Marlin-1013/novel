package com.java.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import com.java.DB.Novel.ContextDAO;
import com.java.DB.Novel.ContextDTO;
import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;
import com.java.DB.Novel.RecentDAO;
import com.java.DB.Novel.RecentDTO;
import com.java.DB.User.MemberDAO;
import com.java.common.Users;

public class NovelMainPage extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);
	
	String[] list = {};
	String name;
	
	public NovelMainPage(String novelname) {
		setTitle("작품");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));
		
		name = novelname;

		JPanel north = new JPanel();
		JPanel center = new JPanel(new BorderLayout(0,30));
		
		JLabel uName = new JLabel(name);
		uName.setFont(font);
		north.add(uName);
		north.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 0));
		
		NovelDAO dao = new NovelDAO();
		NovelDTO dto = dao.selectNovel(name);

        JTextArea cText = new JTextArea();
        cText.setText(dto.getIntroduce());
		cText.setLineWrap(true);
		cText.setPreferredSize(new Dimension(300,100));
		cText.setEditable(false);
		
        JList<String> lst = new JList<String>(list);
        lst.setVisibleRowCount(10);
        lst.setFixedCellHeight(30);
        lst.setFixedCellWidth(300);
        
		ContextDAO cdao = new ContextDAO();
		List<ContextDTO> dtos = cdao.selectList(name);

		DefaultListModel<String> list = new DefaultListModel<>();

		if (dtos.isEmpty())
			list.addElement("작성된 소설이 없습니다.");
		else {

			for (ContextDTO cdto : dtos) {
				list.addElement(cdto.getTitle());
			}
		}
		lst.setModel(list);
        
        center.add(cText, BorderLayout.NORTH);
        center.add(new JScrollPane(lst), BorderLayout.CENTER);
        center.setBorder(BorderFactory.createEmptyBorder(0 , 20 , 20 , 20));

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
					Users users = Users.getUserClass();
					new MainPage(users.getUsername());
					setVisible(false);
				} else {}
			}
		});
		
		lst.addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent me) {
	             if (me.getClickCount() == 2) {
	                JList<String> target = (JList<String>)me.getSource();
	                int index = target.locationToIndex(me.getPoint());
	                if (index >= 0) {
	                   Object item = target.getModel().getElementAt(index);
	                   if(item.toString().equals("작성된 소설이 없습니다.")) {}
	                   else {
		                   Users uclass = Users.getUserClass();
		                   String uname = uclass.getUsername();
		                   RecentDAO rdao = new RecentDAO();
		                   RecentDTO rdto = rdao.novelData(uname, name);
		                   rdao.overlap(rdto.getNovel());
		                   rdao.insertWrite(rdto);
		                   new ReadNovel(item.toString());
		                	setVisible(false);
		                	rdao.close();
	                   }
	                }
	             }
	          }
		});
		dao.close();
		cdao.close();
	}
}
