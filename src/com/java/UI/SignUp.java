package com.java.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.java.DB.User.MemberDAO;
import com.java.DB.User.MemberDTO;

public class SignUp extends JFrame {
	
	final static Font font = new Font("맑은 고딕", Font.BOLD, 30); 
	
	public SignUp() {
		setTitle("회원가입");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,15));
		
		JPanel emptynorth = new JPanel();
		JPanel emptysouth = new JPanel();
		JPanel north = new JPanel();
		JPanel center = new JPanel(new GridLayout(4, 1));
		JPanel south = new JPanel();
		JPanel main = new JPanel(new BorderLayout(50,30));
		
		JLabel title = new JLabel("회원가입");
		title.setFont(font);
		north.add(title);
		
		JPanel name = new JPanel();
        name.add(new JLabel("   유저명 : "));
        JTextField sname = new JTextField(15);
        name.add(sname);
		
		JPanel id = new JPanel();
        id.add(new JLabel("   아이디 : "));
        JTextField sid = new JTextField(15);
        id.add(sid);
        
        JPanel pw = new JPanel();
        pw.add(new JLabel("비밀번호 : "));
        JTextField spw = new JTextField(15);
        pw.add(spw);
        
        JPanel btn = new JPanel();
        JButton sign = new JButton("회원가입");
        btn.add(sign);
        
        center.add(name);
        center.add(id);
        center.add(pw);
        center.add(btn);
        
        main.add(north, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        
        contentPane.add(emptynorth, BorderLayout.NORTH);
        contentPane.add(main, BorderLayout.CENTER);
        contentPane.add(emptysouth, BorderLayout.SOUTH);
        
		setSize(300, 350);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);
		
		sign.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	MemberDAO dao = new MemberDAO();
            	if(sname.getText().equals("") || sid.getText().equals("") || spw.getText().equals(""))
            	{
            		JOptionPane.showMessageDialog(null, "입력창에 빈칸이 있습니다..","가입 오류", JOptionPane.ERROR_MESSAGE);
            	}
            	else {
                	int i = dao.signUp(sname.getText(), sid.getText(), spw.getText());
                	switch (i) {
    				case 1:
    					JOptionPane.showMessageDialog(null, "존재하는 유저명입니다.","가입 오류", JOptionPane.ERROR_MESSAGE);   	
    					break;
    				case 2:
    					JOptionPane.showMessageDialog(null, "존재하는 아이디입니다.","가입 오류", JOptionPane.ERROR_MESSAGE);   	
    					break;
    				default:
    					JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.","가입 완료", JOptionPane.DEFAULT_OPTION);
    					setVisible(false);
    					break;
    				}
            	}
            	dao.close();
            }
        });
	}
}
