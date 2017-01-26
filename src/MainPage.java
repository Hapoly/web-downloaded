import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class MainPage extends JFrame {
	private JButton initChat, sendRequest, gotRequests;
	private JLabel chats, messages;
	private JList<String> chatList, messagesList, friendList;
	private DefaultListModel<String> chatListModel;

	private Main programm;

	private String username;
	private SadafArray<String> numbers;

	private JTextField messageText;
	private JButton messageSend;

	private String selectedChatTitle;
	private String selectedChatNumber;

	public MainPage(Main pr, String username) {
		this.programm = pr;

		setBounds(100, 50, 600, 500);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initChat = new JButton("new chat");
		initChat.setBounds(20, 390, 120, 30);
		initChat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				programm.newChatEvent(username);
			}
		});
		add(initChat);

		gotRequests = new JButton("requests list");
		gotRequests.setBounds(390, 425, 180, 30);
		gotRequests.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				programm.gotRequestsEvent(username);
			}
		});
		add(gotRequests);

		sendRequest = new JButton("send request");
		sendRequest.setBounds(390, 390, 180, 30);
		sendRequest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				programm.requestSendEvent(username);
			}
		});
		add(sendRequest);

		chats = new JLabel("chats");
		chats.setBounds(20, 10, 50, 20);
		add(chats);

		chatList = new JList<>();
		chatList.setBounds(20, 30, 120, 350);
		add(chatList);

		messages = new JLabel("messages");
		messages.setBounds(160, 10, 70, 20);
		add(messages);

		messagesList = new JList<>();
		messagesList.setBounds(160, 30, 200, 350);
		add(messagesList);

		friendList = new JList<>();
		friendList.setBounds(380, 30, 200, 350);
		add(friendList);

		messageText = new JTextField("message text...");
		messageText.setBounds(160, 390, 200, 30);
		add(messageText);

		messageSend = new JButton("send!");
		messageSend.setBounds(160, 425, 200, 30);
		messageSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (selectedChatTitle == null) {
					JOptionPane.showMessageDialog(null, "no chat selected");
				} else {
					try {
						programm.driver.sendMessage(selectedChatTitle, messageText.getText(), selectedChatNumber);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		add(messageSend);

		this.username = username;
		loadNumbers();
		loadChatList();
		loadFriendList();
		
		timer.start();
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
						selectedChatNumber = chatListModel.get(index).split("[(|)]")[1];
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
		this.selectedChatTitle = chatTitle;
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
			SadafArray<String> firendList = this.programm.driver.getUserInDept(username, 1);
			for (int i = 0; i < firendList.size; i++) {
				friendListModel.addElement(firendList.get(i));
			}
			System.out.println(firendList);
			friendList.setModel(friendListModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Timer timer = new Timer(2000, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			loadNumbers();
			loadChatList();
			loadFriendList();

		}
	});
}
