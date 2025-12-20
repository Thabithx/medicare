package view;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import db.Connectdb;
import view.style.Theme;

public class ReportPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> cmbMonth;
	private JComboBox<Integer> cmbYear;
	private JLabel lblTotalAppts, lblTotalRevenue, lblActivePatients;

	// Tables
	private DefaultTableModel doctorModel;
	private DefaultTableModel patientModel;

	public ReportPanel() {
		setLayout(new BorderLayout());
		setBackground(Theme.BG_COLOR);
		setBorder(Theme.createPadding(20));

		// -- Top Panel: Header + Controls --
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Theme.BG_COLOR);

		JLabel lblTitle = Theme.createTitleLabel("Monthly Registrar Report");
		topPanel.add(lblTitle, BorderLayout.NORTH);

		JPanel controlPanel = createControlPanel();
		topPanel.add(controlPanel, BorderLayout.CENTER);

		add(topPanel, BorderLayout.NORTH);

		// -- Center Panel: Summary + Tabs --
		JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
		centerPanel.setBackground(Theme.BG_COLOR);

		// 1. Summary Cards
		JPanel summaryPanel = createSummaryCards();
		centerPanel.add(summaryPanel, BorderLayout.NORTH);

		// 2. Data Tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(Theme.FONT_REGULAR);

		// Tab 1: Doctor Performance
		JPanel pnlDoctors = createDoctorReportPanel();
		tabbedPane.addTab("Doctor Performance", pnlDoctors);

		// Tab 2: Patient Activity
		JPanel pnlPatients = createPatientReportPanel();
		tabbedPane.addTab("Patient Analysis", pnlPatients);

		centerPanel.add(tabbedPane, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);

		// Initial Load (Current Month)
		if (!java.beans.Beans.isDesignTime()) {
			refreshData();
		}
	}

	private JPanel createControlPanel() {
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		pnl.setBackground(new Color(230, 240, 255));
		pnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel lblPeriod = new JLabel("Select Period:");
		lblPeriod.setFont(Theme.FONT_BOLD);
		pnl.add(lblPeriod);

		String[] months = { "January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December" };
		cmbMonth = new JComboBox<>(months);
		cmbMonth.setSelectedIndex(LocalDate.now().getMonthValue() - 1); // Current month
		pnl.add(cmbMonth);

		cmbYear = new JComboBox<>();
		int currentYear = LocalDate.now().getYear();
		for (int i = currentYear; i >= currentYear - 5; i--) {
			cmbYear.addItem(i);
		}
		pnl.add(cmbYear);

		JButton btnGenerate = Theme.createButton("Generate Report", Theme.PRIMARY);
		btnGenerate.addActionListener(e -> refreshData());
		pnl.add(btnGenerate);

		return pnl;
	}

	private JPanel createSummaryCards() {
		JPanel pnl = new JPanel(new GridLayout(1, 3, 20, 0));
		pnl.setBackground(Theme.BG_COLOR);
		pnl.setPreferredSize(new Dimension(0, 100));

		lblTotalAppts = createCardLabel("Total Appointments", "0");
		lblTotalRevenue = createCardLabel("Est. Revenue", "$0");
		lblActivePatients = createCardLabel("Active Patients", "0");

		pnl.add(createCard(lblTotalAppts, new Color(220, 245, 255)));
		pnl.add(createCard(lblTotalRevenue, new Color(220, 255, 230)));
		pnl.add(createCard(lblActivePatients, new Color(255, 240, 240)));

		return pnl;
	}

	private JPanel createCard(JLabel label, Color bg) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(bg);
		card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

		// Centered Content
		label.setHorizontalAlignment(SwingConstants.CENTER);
		card.add(label, BorderLayout.CENTER);

		return card;
	}

	private JLabel createCardLabel(String title, String value) {
		JLabel lbl = new JLabel("<html><center><div style='font-size:12px;color:gray;'>" + title
				+ "</div><div style='font-size:24px;font-weight:bold;'>" + value + "</div></center></html>");
		return lbl;
	}

	private JPanel createDoctorReportPanel() {
		JPanel pnl = new JPanel(new BorderLayout());
		pnl.setBackground(Theme.BG_COLOR);

		String[] columns = { "Doctor Name", "Specialty", "Total Apps", "Completed", "Cancelled" };
		doctorModel = new DefaultTableModel(columns, 0);
		JTable table = new JTable(doctorModel);
		styleTable(table);
		pnl.add(new JScrollPane(table), BorderLayout.CENTER);

		return pnl;
	}

	private JPanel createPatientReportPanel() {
		JPanel pnl = new JPanel(new BorderLayout());
		pnl.setBackground(Theme.BG_COLOR);

		String[] columns = { "Patient Name", "Visits This Month", "Last Appointment" };
		patientModel = new DefaultTableModel(columns, 0);
		JTable table = new JTable(patientModel);
		styleTable(table);
		pnl.add(new JScrollPane(table), BorderLayout.CENTER);

		return pnl;
	}

	private void styleTable(JTable table) {
		Theme.applyTableStyling(table);
	}

	private void refreshData() {
		int month = cmbMonth.getSelectedIndex() + 1;
		int year = (int) cmbYear.getSelectedItem();

		loadSummaryData(month, year);
		loadDoctorData(month, year);
		loadPatientData(month, year);
	}

	private void loadSummaryData(int month, int year) {
		String sql = "SELECT COUNT(*) as total_apps, " +
				"SUM(CASE WHEN status='COMPLETED' THEN 1 ELSE 0 END) as completed_apps, " +
				"COUNT(DISTINCT patient_id) as active_patients " +
				"FROM appointments WHERE MONTH(appointment_date) = ? AND YEAR(appointment_date) = ?";

		try (Connection con = Connectdb.getConnection();
				PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setInt(1, month);
			pst.setInt(2, year);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					int total = rs.getInt("total_apps");
					int completed = rs.getInt("completed_apps");
					int active = rs.getInt("active_patients");

					// Update labels
					lblTotalAppts.setText(
							"<html><center><div style='font-size:12px;color:gray;'>Total Appointments</div><div style='font-size:24px;font-weight:bold;'>"
									+ total + "</div></center></html>");
					lblActivePatients.setText(
							"<html><center><div style='font-size:12px;color:gray;'>Active Patients</div><div style='font-size:24px;font-weight:bold;'>"
									+ active + "</div></center></html>");

					// Estimate Revenue: Assume 2500 LKR per completed appointment
					int revenue = completed * 2500;
					lblTotalRevenue.setText(
							"<html><center><div style='font-size:12px;color:gray;'>Est. Revenue</div><div style='font-size:24px;font-weight:bold;'>"
									+ "LKR " + String.format("%,d", revenue) + "</div></center></html>");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadDoctorData(int month, int year) {
		doctorModel.setRowCount(0);

		String sql = "SELECT d.first_name, d.last_name, d.specialty, " +
				"COUNT(a.id) as total, " +
				"SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed, " +
				"SUM(CASE WHEN a.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled " +
				"FROM Doctors d " +
				"LEFT JOIN appointments a ON d.doctor_id = a.doctor_id " +
				"AND MONTH(a.appointment_date) = ? AND YEAR(a.appointment_date) = ? " +
				"GROUP BY d.doctor_id";

		try (Connection con = Connectdb.getConnection();
				PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setInt(1, month);
			pst.setInt(2, year);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					doctorModel.addRow(new Object[] {
							"Dr. " + rs.getString("first_name") + " " + rs.getString("last_name"),
							rs.getString("specialty"),
							rs.getInt("total"),
							rs.getInt("completed"),
							rs.getInt("cancelled")
					});
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadPatientData(int month, int year) {
		patientModel.setRowCount(0);
		String sql = "SELECT p.first_name, p.last_name, " +
				"COUNT(a.id) as visits, MAX(a.appointment_date) as last_visit " +
				"FROM Patients p " +
				"JOIN appointments a ON p.patient_id = a.patient_id " + // INNER JOIN to show only active patients
				"WHERE MONTH(a.appointment_date) = ? AND YEAR(a.appointment_date) = ? " +
				"GROUP BY p.patient_id ORDER BY visits DESC";

		try (Connection con = Connectdb.getConnection();
				PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setInt(1, month);
			pst.setInt(2, year);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					patientModel.addRow(new Object[] {
							rs.getString("first_name") + " " + rs.getString("last_name"),
							rs.getInt("visits"),
							rs.getString("last_visit")
					});
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
