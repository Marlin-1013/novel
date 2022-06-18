package com.java.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import org.w3c.dom.events.MouseEvent;

import com.java.DB.Novel.ContextDAO;
import com.java.DB.Novel.ContextDTO;
import com.java.DB.Novel.NovelDAO;
import com.java.DB.Novel.NovelDTO;

public class ReadNovel extends JFrame{
	final static Font font = new Font("맑은 고딕", Font.BOLD, 20);
	final static Font alert = new Font("맑은 고딕", Font.PLAIN, 10);
	final static LineBorder border = new LineBorder(Color.black, 1, true);
	
	String[] list = {};
	String nname;
	
	public ReadNovel(String name) {
		setTitle("감상");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));

		JPanel north = new JPanel();
		JPanel center = new JPanel(new BorderLayout());
		JPanel menu = new JPanel(new GridLayout(1,3));
		JPanel main = new JPanel();
		
		nname = name;

		JLabel uName = new JLabel(nname);
		uName.setFont(font);
		north.add(uName);
		north.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 0));
		
		ContextDAO dao = new ContextDAO();
		ContextDTO dto = dao.selectNovel(nname);
		dao.updateVisitCount(nname);
        
        JTextArea cText = new JTextArea();
        cText.setText(dto.getContext());
		cText.setLineWrap(true);
		cText.setEditable(false);
		JScrollPane text = new JScrollPane(cText);
		text.setPreferredSize(new Dimension(300,360));
		
		JPanel up = new JPanel();
		JLabel mup = new JLabel("↑맨위로");
        up.add(mup);
        up.setPreferredSize(new Dimension(110,50));
        up.setBorder(BorderFactory.createEmptyBorder(10 , 0 , 5 , 0));
        
        JPanel com = new JPanel();
        JLabel comm = new JLabel("댓글보기");
        com.add(comm);
		com.setPreferredSize(new Dimension(110,50));
		com.setBorder(BorderFactory.createEmptyBorder(10 , 0 , 5 , 0));
		
		main.add(text);
		main.add(up);
		main.add(com);
		main.setBorder(BorderFactory.createEmptyBorder(0 , 15 , 15 , 15));
		
		JPanel pre = new JPanel();
		JLabel prm = new JLabel("이전화");
        pre.add(prm);
        pre.setBorder(border);
        
        JPanel home = new JPanel();
        JLabel li = new JLabel("목록");
        home.add(li);
        home.setBorder(border);
        
        JPanel next = new JPanel();
        JLabel ne = new JLabel("다음화");
        next.add(ne);
        next.setBorder(border);
        
        menu.add(pre);
        menu.add(home);
        menu.add(next);
        menu.setBorder(BorderFactory.createEmptyBorder(0 , 15 , 15 , 15));
        
        center.add(main, BorderLayout.CENTER);
        center.add(menu, BorderLayout.SOUTH);
        
        contentPane.add(north, BorderLayout.NORTH);
        contentPane.add(center, BorderLayout.CENTER);
        
		setSize(350, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);
		
		prm.addMouseListener(new MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int rownum = 0;
				NovelDAO ndao = new NovelDAO();
				ContextDAO cdao = new ContextDAO();
				ContextDTO dto = new ContextDTO();
				String uname = ndao.searchNovel(nname);
				List<ContextDTO> dtos = cdao.selectList(uname);
				for (ContextDTO cdto : dtos) {
					if(cdto.getTitle().equals(nname)) {
						rownum = cdto.getRownum();
					}
				}
				if(rownum > 0) {
					dto = dtos.get(rownum-1);
					new ReadNovel(dto.getTitle());
					setVisible(false);
				}
				else JOptionPane.showMessageDialog(null, "첫화입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				dao.close();
				ndao.close();
				cdao.close();
			}
		});
		
		li.addMouseListener(new MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e) {
				NovelDAO dao = new NovelDAO();
				String uname = dao.searchNovel(nname);
				new NovelMainPage(uname);
				setVisible(false);
				dao.close();
			}
		});
		ne.addMouseListener(new MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int rownum = 0;
				NovelDAO ndao = new NovelDAO();
				ContextDAO cdao = new ContextDAO();
				ContextDTO dto = new ContextDTO();
				String uname = ndao.searchNovel(nname);
				List<ContextDTO> dtos = cdao.selectList(uname);
				for (ContextDTO cdto : dtos) {
					if(cdto.getTitle().equals(nname)) {
						rownum = cdto.getRownum();
					}
				}
				if(rownum < dtos.size()-1) {
					dto = dtos.get(rownum + 1);
					new ReadNovel(dto.getTitle());
					setVisible(false);
				}
				else JOptionPane.showMessageDialog(null, "마지막화입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				ndao.close();
				cdao.close();
			}
		});
		mup.addMouseListener(new MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e) {
				text.getVerticalScrollBar().setValue(0);
			}
		});
		comm.addMouseListener(new MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new CommentPage(nname);
			}
		});
		dao.close();
	}
}
