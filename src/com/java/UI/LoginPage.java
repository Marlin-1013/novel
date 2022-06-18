package com.java.UI;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import org.w3c.dom.events.MouseEvent;

import java.awt.event.*;
import com.java.DB.User.*;
import com.java.common.Users;

public class LoginPage extends JFrame {

	final static Font font = new Font("맑은 고딕", Font.BOLD, 30);

	public LoginPage() {
		setTitle("로그인");

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 30));

		JPanel emptynorth = new JPanel();
		JPanel emptysouth = new JPanel();
		JPanel north = new JPanel();
		JPanel center = new JPanel(new GridLayout(3, 1));
		JPanel south = new JPanel();
		JPanel main = new JPanel(new BorderLayout(50, 30));
		JLabel title = new JLabel("로그인");
		title.setFont(font);
		north.add(title);

		JPanel id = new JPanel();
		JLabel lid = new JLabel("   아이디 : ");
		id.add(lid);
		JTextField tid = new JTextField(15);
		id.add(tid);

		JPanel pw = new JPanel();
		JLabel lpw = new JLabel("비밀번호 : ");
		pw.add(lpw);
		JPasswordField tpw = new JPasswordField(15);
		pw.add(tpw);

		JPanel btn = new JPanel();
		JButton login = new JButton("로그인");
		btn.add(login);

		center.add(id);
		center.add(pw);
		center.add(btn);

		JLabel sign = new JLabel("   회원가입   ");
		JLabel pwS = new JLabel("   비밀번호 찾기   ");
		south.add(sign);
		south.add(pwS);

		main.add(north, BorderLayout.NORTH);
		main.add(center, BorderLayout.CENTER);
		main.add(south, BorderLayout.SOUTH);

		contentPane.add(emptynorth, BorderLayout.NORTH);
		contentPane.add(main, BorderLayout.CENTER);
		contentPane.add(emptysouth, BorderLayout.SOUTH);

		setSize(300, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);

		sign.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new SignUp();
			}
		});
		pwS.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new PwSearch();
			}
		});
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO dao = new MemberDAO();
				MemberDTO dto = dao.checkUser(tid.getText(), tpw.getText());
				if (dto.getUname() != null) {
					JOptionPane.showMessageDialog(null, dto.getUname() + "님 환영합니다.", "로그인성공", JOptionPane.DEFAULT_OPTION);
					Users uclass = Users.getUserClass();
					uclass.setUsername(dto.getUname());
					new MainPage(uclass.getUsername());
					setVisible(false);
					dao.close();
				} else {
					JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 일치하지 않습니다.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new LoginPage();
	}
}
