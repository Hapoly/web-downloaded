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
import javax.swing.JSpinner.NumberEditor;

public class SendRequestPanel extends JFrame {
	private Main programm;

	private String username;
	private String selectedUsername;

	private JLabel dptLabel;
	private JTextField dpt;
	private JButton search, send;
	private JList<String> list;
	private DefaultListModel<String> listModel;

	public SendRequestPanel(String username, Main pr) {
		this.programm = pr;
		this.username = username;

		setBounds(200, 50, 200, 420);
		setLayout(null);

		dptLabel = new JLabel("suggest dept");
		dptLabel.setBounds(20, 5, 160, 30);
		add(dptLabel);

		dpt = new JTextField("2");
		dpt.setBounds(20, 40, 140, 30);
		add(dpt);

		search = new JButton("search");
		search.setBounds(20, 80, 140, 30);
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int searchDpt = Integer.valueOf(dpt.getText());
				try {
					listModel = new DefaultListModel<>();
					SadafArray<String> userList = programm.driver.getUserInDept(username, searchDpt);
					System.out.println("result: " + userList);
					for (int i = 0; i < userList.size; i++) {
						listModel.addElement(userList.get(i));
					}
					list.setModel(listModel);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		add(search);

		list = new JList<>();
		list.setBounds(20, 120, 140, 200);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 1) {
					int index = list.locationToIndex(evt.getPoint());
					selectedUsername = listModel.get(index);

				}
			}
		});
		add(list);

		send = new JButton("send request");
		send.setBounds(20, 330, 140, 30);
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (selectedUsername == null) {
					JOptionPane.showMessageDialog(null, "please select a user");
				} else {
					sendRequest(username, selectedUsername);
				}
			}
		});
		add(send);

		loadUserList();
	}

	private void sendRequest(String username1, String username2) {
		try {
			this.programm.driver.sendRequest(username1, username2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadUserList() {
		try {
			listModel = new DefaultListModel<>();
			SadafArray<String> useList = this.programm.driver.getNotFriends(username);
			for (int i = 0; i < useList.size; i++) {
				listModel.addElement(useList.get(i));
			}
			list.setModel(listModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
