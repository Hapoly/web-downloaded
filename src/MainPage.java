import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;

public class MainPage extends JFrame {
	private JButton initChat, sendRequest, friends;
	private JList<String> chatList, messages;

	private Main programm;

	private String username;

	public MainPage(Main pr, String username) {
		this.programm = pr;

		setBounds(100, 50, 400, 500);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initChat = new JButton("new chat");
		initChat.setBounds(20, 400, 100, 50);
		add(initChat);

		sendRequest = new JButton("send request");
		sendRequest.setBounds(130, 400, 110, 50);
		add(sendRequest);

		friends = new JButton("friends");
		friends.setBounds(250, 400, 110, 50);
		add(friends);

		chatList = new JList<>();
		chatList.setBounds(20, 20, 120, 350);
		add(chatList);

		messages = new JList<>();
		messages.setBounds(160, 20, 200, 350);
		add(messages);

		this.username = username;
		try {
			
			SadafArray<String> chatTitleArray = pr.driver.getChats(username);
			System.out.println(chatTitleArray);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
