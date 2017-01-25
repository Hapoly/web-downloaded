import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainPage extends JFrame {
	private JButton initChat, sendRequest, friends;
	private JList<String> chatList, messagesList, friendList;
	private DefaultListModel<String> chatListModel;

	private Main programm;

	private String username;
	private SadafArray<String> numbers;

	public MainPage(Main pr, String username) {
		this.programm = pr;

		setBounds(100, 50, 600, 500);
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

		messagesList = new JList<>();
		messagesList.setBounds(160, 20, 200, 350);
		add(messagesList);

		friendList = new JList<>();
		friendList.setBounds(380, 20, 200, 350);
		add(friendList);

		this.username = username;

		loadNumbers();
		loadChatList();
		loadFriendList();
	}

	private void loadNumbers() {
		try {
			numbers = this.programm.driver.getNumbers(username);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadChatList() {
		try {

			chatListModel = new DefaultListModel<>();
			for (int j = 0; j < numbers.size; j++) {
				String number = numbers.get(j);
				SadafArray<String> chatTitleArray = this.programm.driver.getChats(number);
				for (int i = 0; i < chatTitleArray.size; i++) {
					chatListModel.addElement(chatTitleArray.get(i) + "(" + number + ")");
				}
			}
			chatList.setModel(chatListModel);
			chatList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() >= 1) {
						int index = chatList.locationToIndex(evt.getPoint());
						String chatTitle = chatListModel.get(index).split("[(]")[0];
						System.out.println("clicked on " + chatTitle);
						loadChatMessages(chatTitle);
					}
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void loadChatMessages(String chatTitle) {
		try {

			DefaultListModel<String> messageListModel = new DefaultListModel<>();
			SadafArray<String> messageList = this.programm.driver.getMessages(chatTitle);
			for (int i = 0; i < messageList.size; i++)
				messageListModel.addElement(messageList.get(i));
			System.out.println(messageList);
			messagesList.setModel(messageListModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadFriendList() {
		try {
			DefaultListModel<String> friendListModel = new DefaultListModel<>();
			SadafArray<String> firendList = this.programm.driver.getFriends(username, 1);
			for (int i = 0; i < firendList.size; i++) {
				friendListModel.addElement(firendList.get(i));
			}
			System.out.println(firendList);
			friendList.setModel(friendListModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
