import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginPage extends JFrame {
	private JTextField username, password, numbers;
	private JLabel usernameLabel, passwordLabel, numbersLabel;
	private JButton loginBTN, registerBTN;

	Main programm;

	public LoginPage(Main pr) {
		setLayout(null);
		setBounds(100, 50, 155, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		usernameLabel = new JLabel("username: ");
		usernameLabel.setBounds(20, 20, 100, 30);
		add(usernameLabel);
		passwordLabel = new JLabel("password: ");
		passwordLabel.setBounds(20, 100, 100, 30);
		add(passwordLabel);
		numbersLabel = new JLabel("numbers: ");
		numbersLabel.setBounds(20, 180, 100, 30);
		add(numbersLabel);
		username = new JTextField();
		username.setBounds(20, 50, 100, 30);
		add(username);
		password = new JTextField();
		password.setBounds(20, 140, 100, 30);
		add(password);
		numbers = new JTextField();
		numbers.setBounds(20, 220, 100, 30);
		add(numbers);

		loginBTN = new JButton("login");
		loginBTN.setBounds(20, 260, 100, 40);
		add(loginBTN);
		registerBTN = new JButton("register");
		registerBTN.setBounds(20, 310, 100, 40);
		add(registerBTN);

		this.programm = pr;
		loginBTN.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				programm.loginEvent(username.getText(), password.getText());
			}
		});
		registerBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				programm.registerEvent(username.getText(), password.getText(), numbers.getText().split(","));
			}
		});
	}

	public void setLoginEvent(ActionListener al) {
		this.loginBTN.addActionListener(al);
	}

	public void setRegisterEvent(ActionListener al) {
		this.registerBTN.addActionListener(al);
	}
}
