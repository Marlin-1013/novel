package com.java.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.java.DB.User.MemberDAO;

public class PwSearch extends JFrame {
	
	final static Font font = new Font("맑은 고딕", Font.BOLD, 30); 
	
	public PwSearch() {
		setTitle("비밀번호 찾기");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,15));
		
		JPanel emptynorth = new JPanel();
		JPanel emptysouth = new JPanel();
		JPanel north = new JPanel();
		JPanel center = new JPanel(new GridLayout(4, 1));
		JPanel south = new JPanel();
		JPanel main = new JPanel(new BorderLayout(50,30));
		
		JLabel title = new JLabel("비밀번호 찾기");
		title.setFont(font);
		north.add(title);
		
		JPanel name = new JPanel();
        name.add(new JLabel("   유저명 : "));
        JTextField uname = new JTextField(15);
        name.add(uname);
		
		JPanel id = new JPanel();
        id.add(new JLabel("   아이디 : "));
        JTextField uid =new JTextField(15);
        id.add(uid);

        
        JPanel btn = new JPanel();
        JButton sea = new JButton("찾기");
        btn.add(sea);
        
        center.add(name);
        center.add(id);
        center.add(btn);
        
        main.add(north, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        
        contentPane.add(emptynorth, BorderLayout.NORTH);
        contentPane.add(main, BorderLayout.CENTER);
        contentPane.add(emptysouth, BorderLayout.SOUTH);
        
		setSize(300, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);
		
		sea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
            	MemberDAO dao = new MemberDAO();
            	if(uname.getText().equals("") || uid.getText().equals(""))
            	{
            		JOptionPane.showMessageDialog(null, "입력창에 빈칸이 있습니다..","가입 오류", JOptionPane.ERROR_MESSAGE);
            	}
            	else {
                	String i = dao.pwSearch(uname.getText(), uid.getText());
                	switch (i) {
    				case "1":
    					JOptionPane.showMessageDialog(null, "존재하지 않는 아이디입니다.","찾기 오류", JOptionPane.ERROR_MESSAGE);   	
    					break;
    				case "2":
    					JOptionPane.showMessageDialog(null, "유저명이 맞지 않습니다.","찾기 오류", JOptionPane.ERROR_MESSAGE);   	
    					break;
    				default:
    					JOptionPane.showMessageDialog(null, "비밀번호는 " + i + "입니다.","가입 완료", JOptionPane.DEFAULT_OPTION);
    					setVisible(false);
    					break;
    				}
            	}
            	dao.close();
			}
		});
	}
}
