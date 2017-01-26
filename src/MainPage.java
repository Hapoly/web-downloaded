import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainPage extends JFrame {
	private JButton initChat, sendRequest, friends;
	private JLabel chats,messages,friend;
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
		
		chats=new JLabel("chats");
		chats.setBounds(20,10,50,20);
		add(chats);
		
		chatList = new JList<>();
		chatList.setBounds(20, 30, 120, 350);
		add(chatList);
		
		messages=new JLabel("messages");
		messages.setBounds(160,10,70,20);
		add(messages);

		messagesList = new JList<>();
		messagesList.setBounds(160, 30, 200, 350);
		add(messagesList);
		
		friend=new JLabel("friends");
		friend.setBounds(380,10,50,20);
		add(friend);

		friendList = new JList<>();
		friendList.setBounds(380, 30, 200, 350);
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
