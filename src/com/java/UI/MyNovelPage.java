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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import com.java.DB.Novel.CommentDTO;
import com.java.DB.Novel.ContextDAO;
import com.java.DB.Novel.ContextDTO;
import com.java.DB.User.MemberDAO;

public class MyNovelPage extends JFrame {
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);
	
	String name;
	Object item;
	JList<String> lst;
	ContextDAO cdao;
	List<ContextDTO> dtos;
	
	public MyNovelPage(String novelname) {
		setTitle("소설");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));
		
		name = novelname;

		JPanel north = new JPanel();
		JPanel center = new JPanel(new BorderLayout());
		JPanel menu = new JPanel(new BorderLayout());
		JPanel main = new JPanel();
		
		JLabel uName = new JLabel(name);
		uName.setFont(font);
		north.add(uName);
		north.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 0));
		
		DefaultListModel<String> list = new DefaultListModel<>();
        
        JList<String> lst = new JList<String>(list);
        main.add(new JScrollPane(lst));
        lst.setVisibleRowCount(14);
        lst.setFixedCellHeight(30);
        lst.setFixedCellWidth(300);
        main.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
        
        cdao = new ContextDAO();
		dtos = cdao.selectList(name);

		if (dtos.isEmpty())
			list.addElement("작성된 소설이 없습니다.");
		else {

			for (ContextDTO cdto : dtos) {
				list.addElement(cdto.getTitle());
			}
		}
		lst.setModel(list);
        
		JPanel dNovel = new JPanel();
		JLabel dLabel = new JLabel("삭제하기");
		dLabel.setForeground(Color.red);
        dNovel.add(dLabel);
        dNovel.setBorder(border);
        dNovel.setPreferredSize(new Dimension(150,10));
        
        
        JPanel cNovel = new JPanel();
        JLabel cLabel = new JLabel("다음화");
        cNovel.add(cLabel);
        cNovel.setBorder(border);
        
        menu.add(dNovel, BorderLayout.WEST);
        menu.add(cNovel, BorderLayout.CENTER);
        menu.setBorder(BorderFactory.createEmptyBorder(0 , 15 , 15 , 15));
        
        center.add(menu, BorderLayout.SOUTH);
        center.add(main, BorderLayout.CENTER);
        
        contentPane.add(north, BorderLayout.NORTH);
        contentPane.add(center, BorderLayout.CENTER);
        
		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);

		cLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new WriteNovel(0, name);
				setVisible(false);
			}
		});
		dLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int selected = lst.getSelectedIndex();
				item = lst.getModel().getElementAt(selected);
				if(item.toString().equals("작성된 소설이 없습니다.")) {}
                else {
    				int ans = JOptionPane.showConfirmDialog(null, item.toString() + "를 삭제하시겠습니까?", "경고",
    						JOptionPane.WARNING_MESSAGE);
    				if (ans == 0) {
    					ContextDAO dao = new ContextDAO();
    					String title = item.toString();
    					int result = dao.deleteContext(title);
    					if (1 == result) {
    						JOptionPane.showMessageDialog(null, "작품을 삭제했습니다.", "성공", JOptionPane.DEFAULT_OPTION);
    						dtos = cdao.selectList(name);
    						list.clear();
    						for (ContextDTO dto : dtos) {
    							list.addElement(dto.getTitle());
    						}
    						lst.setModel(list);
    					}
    					 else JOptionPane.showMessageDialog(null, "실패했습니다.", "실패", JOptionPane.ERROR_MESSAGE);
    					dao.close();
    				} else {}   
                }
			}
		});
		uName.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "내 소설 리스트로 돌아갑니다", "확인",
						JOptionPane.WARNING_MESSAGE);
				if (ans == 0) {
					MemberDAO dao = new MemberDAO(); 
					String uname = dao.searchUser(name);
					new MyNovelList(uname);
					setVisible(false);
					dao.close();
				} else {}
			}
		});
		
		lst.addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent me) {
	             if (me.getClickCount() == 2) {
	                JList<String> target = (JList<String>)me.getSource();
	                int index = target.locationToIndex(me.getPoint());
	                if (index >= 0) {
	                   item = target.getModel().getElementAt(index);
	                   if(item.toString().equals("작성된 소설이 없습니다.")) {}
	                   else {
		                   new WriteNovel(1, item.toString());
		                	setVisible(false);   
	                   }
	                }
	             }
	          }
		});
		cdao.close();
	}
}
