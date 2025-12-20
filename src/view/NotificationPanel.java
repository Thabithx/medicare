package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controller.NotificationController;
import service.SessionManager;
import java.util.List;
import view.style.Theme;

public class NotificationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel model;

	public NotificationPanel() {
		setLayout(new BorderLayout());
		setBackground(Theme.BG_COLOR);
		setBorder(Theme.createPadding(20));

		// Header Panel with Title
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Theme.BG_COLOR);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		JLabel lblTitle = Theme.createTitleLabel("My Notifications");
		headerPanel.add(lblTitle, BorderLayout.WEST);

		add(headerPanel, BorderLayout.NORTH);

		// Table
		String[] columns = { "Time", "Message" };
		model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		table.setRowHeight(30);
		table.setFont(Theme.FONT_REGULAR);
		table.getTableHeader().setFont(Theme.FONT_BOLD);
		table.getTableHeader().setBackground(Theme.LIGHT);
		table.setSelectionBackground(Theme.PRIMARY);
		table.setSelectionForeground(Color.WHITE);

		// Column width adjustment
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMaxWidth(200);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		add(scrollPane, BorderLayout.CENTER);

		// Refresh Button
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.setBackground(Theme.BG_COLOR);
		JButton btnRefresh = Theme.createButton("Refresh", Theme.PRIMARY);
		btnRefresh.addActionListener(e -> loadNotifications());
		btnPanel.add(btnRefresh);
		add(btnPanel, BorderLayout.SOUTH);

		loadNotifications();
	}

	public void loadNotifications() {
		model.setRowCount(0);

		String role = "PATIENT";
		int userId = 0;
		if (SessionManager.isDoctor()) {
			role = "DOCTOR";
			userId = SessionManager.getUser().getUserId();
		} else if (SessionManager.isPatient()) {
			role = "PATIENT";
			userId = SessionManager.getUser().getUserId();
		}

		// Registrar/Admin
		if (SessionManager.isAdmin())
			return;

		List<String> notifs = NotificationController.getNotifications(role, userId);

		for (String n : notifs) {
			// Expected format: "2023-10-10 10:00:00: Message content"
			String[] parts = n.split(": ", 2);
			if (parts.length < 2)
				continue;
			model.addRow(new Object[] { parts[0], parts[1] });
		}
	}
}
