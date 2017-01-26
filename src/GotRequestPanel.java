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

public class GotRequestPanel extends JFrame {
	private Main programm;

	private String username;
	private String selectedUsername;

	private JButton accept;
	private JList<String> list;
	private DefaultListModel<String> listModel;

	public GotRequestPanel(String username, Main pr) {
		this.programm = pr;
		this.username = username;

		setBounds(200, 50, 200, 420);
		setLayout(null);


		list = new JList<>();
		list.setBounds(20, 20, 140, 300);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 1) {
					int index = list.locationToIndex(evt.getPoint());
					selectedUsername = listModel.get(index);

				}
			}
		});
		add(list);

		accept = new JButton("accept");
		accept.setBounds(20, 330, 140, 30);
		accept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (selectedUsername == null) {
					JOptionPane.showMessageDialog(null, "please select a user");
				} else {
					AcceptRequest(username, selectedUsername);
				}
			}
		});
		add(accept);

		loadRequestList();
	}

	private void AcceptRequest(String username1, String username2) {
		try {
			this.programm.driver.acceptRequest(username1, username2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadRequestList() {
		try {
			listModel = new DefaultListModel<>();
			SadafArray<String> useList = this.programm.driver.getRecievedRequests(username);
			for (int i = 0; i < useList.size; i++) {
				listModel.addElement(useList.get(i));
			}
			list.setModel(listModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
