import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class NewChat extends JFrame {
	private Main programm;
	private String username;

	private JButton create;
	private JTextField chatTitle, numbers;
	private JLabel chatTitleLabel, numberLabel;

	public NewChat(String username, Main programm) {
		this.username = username;
		this.programm = programm;

		setBounds(100, 50, 200, 210);
		setLayout(null);

		chatTitleLabel = new JLabel("title: ");
		chatTitleLabel.setBounds(20, 5, 140, 30);
		add(chatTitleLabel);

		chatTitle = new JTextField("chat title 0");
		chatTitle.setBounds(20, 30, 140, 30);
		add(chatTitle);

		numberLabel = new JLabel("numbers: ");
		numberLabel.setBounds(20, 60, 140, 30);
		add(numberLabel);

		numbers = new JTextField("chat title 0");
		numbers.setBounds(20, 90, 140, 30);
		add(numbers);

		create = new JButton("create");
		create.setBounds(20, 130, 140, 30);
		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] numbeersArray = numbers.getText().split(",");
				String chatTitleString = chatTitle.getText();
				initChat(chatTitleString, numbeersArray);
				dispose();
			}
		});
		add(create);
	}

	private void initChat(String chatTitle, String[] numbers) {
		try {
			programm.driver.initChat(chatTitle);
			for (String nm : numbers)
				programm.driver.addMemberToChat(chatTitle, nm);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
