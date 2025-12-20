package view;

import controller.PatientController;
import controller.DoctorController;
import controller.AppointmentController;
import model.AppointmentDetails;
import service.SessionManager;
import view.style.Theme;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardHomePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dashboard dashboard;
    private javax.swing.Timer clockTimer;

    public DashboardHomePanel() {
        this(null);
    }

    public DashboardHomePanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(Theme.createPadding(30));

        buildDashboard();
        startClock();
    }

    private JPanel headerPanel;
    private JPanel statsPanel;
    private JPanel quickActionsPanel;
    private JPanel schedulePanel;
    private JPanel activityPanel;

    private void buildDashboard() {
        removeAll();

        // Header Section
        headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Theme.BG_COLOR);

        // Statistics Cards
        statsPanel = createStatisticsPanel();
        mainContent.add(statsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // Middle Row: Quick Actions + Schedule
        JPanel middleRow = new JPanel(new GridLayout(1, 2, 20, 0));
        middleRow.setBackground(Theme.BG_COLOR);
        middleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        quickActionsPanel = createQuickActionsPanel();
        schedulePanel = createSchedulePanel();

        middleRow.add(quickActionsPanel);
        middleRow.add(schedulePanel);

        mainContent.add(middleRow);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bottom Row: Recent Activity
        activityPanel = createActivityPanel();
        activityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        mainContent.add(activityPanel);

        JScrollPane scroll = new JScrollPane(mainContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Left: Welcome message
        String userName = "User";
        String role = "Dashboard";

        if (!java.beans.Beans.isDesignTime()) {
            userName = SessionManager.getUser() != null ? SessionManager.getUser().getEmail().split("@")[0] : "User";
            role = SessionManager.isAdmin() ? "Admin" : (SessionManager.isDoctor() ? "Doctor" : "Patient");
        }

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Theme.BG_COLOR);

        JLabel welcomeLabel = new JLabel("Welcome back, " + userName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(17, 24, 39));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel(role + " Dashboard");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleLabel.setForeground(new Color(107, 114, 128));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(welcomeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(roleLabel);

        // Right: Date and Time
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Theme.BG_COLOR);

        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setForeground(new Color(17, 24, 39));
        dateLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(new Color(107, 114, 128));
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Update clock
        clockTimer = new javax.swing.Timer(1000, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
            dateLabel.setText(dateFormat.format(new Date()));
            timeLabel.setText(timeFormat.format(new Date()));
        });
        clockTimer.start();

        rightPanel.add(dateLabel);
        rightPanel.add(timeLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(Theme.BG_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        if (java.beans.Beans.isDesignTime()) {
            panel.add(createStatCard("Total Patients", "0", new Color(59, 130, 246)));
            return panel;
        }

        if (SessionManager.isAdmin()) {
            // Admin Statistics
            int totalPatients = PatientController.getAllPatients().size();
            int totalDoctors = DoctorController.getAllDoctors().size();
            List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
            long pendingCount = allAppts.stream().filter(a -> "PENDING".equals(a.getStatus())).count();

            panel.add(createStatCard("Total Patients", String.valueOf(totalPatients), new Color(59, 130, 246)));
            panel.add(createStatCard("Total Doctors", String.valueOf(totalDoctors), new Color(16, 185, 129)));
            panel.add(createStatCard("Total Appointments", String.valueOf(allAppts.size()), new Color(139, 92, 246)));
            panel.add(createStatCard("Pending Requests", String.valueOf(pendingCount), new Color(245, 158, 11)));

        } else if (SessionManager.isDoctor()) {
            // Doctor Statistics
            int doctorId = SessionManager.getUser().getReferenceId();
            List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
            List<AppointmentDetails> myAppts = allAppts.stream()
                    .filter(a -> a.getDoctorId() == doctorId)
                    .collect(Collectors.toList());

            long patients = myAppts.stream().map(AppointmentDetails::getPatientId).distinct().count();
            long today = myAppts.stream()
                    .filter(a -> a.getAppointmentDate().equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    .count();
            long pending = myAppts.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
            long completed = myAppts.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();
            int rate = myAppts.size() > 0 ? (int) ((completed * 100.0) / myAppts.size()) : 0;

            panel.add(createStatCard("My Patients", String.valueOf(patients), new Color(59, 130, 246)));
            panel.add(createStatCard("Today's Appointments", String.valueOf(today), new Color(16, 185, 129)));
            panel.add(createStatCard("Pending Requests", String.valueOf(pending), new Color(245, 158, 11)));
            panel.add(createStatCard("Completion Rate", rate + "%", new Color(139, 92, 246)));

        } else {
            // Patient Statistics
            int patientId = SessionManager.getUser().getReferenceId();
            List<AppointmentDetails> allAppts = AppointmentController.getAllAppointmentDetails();
            List<AppointmentDetails> myAppts = allAppts.stream()
                    .filter(a -> a.getPatientId() == patientId)
                    .collect(Collectors.toList());

            long upcoming = myAppts.stream().filter(a -> "SCHEDULED".equals(a.getStatus())).count();
            long completed = myAppts.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();

            panel.add(createStatCard("Total Appointments", String.valueOf(myAppts.size()), new Color(59, 130, 246)));
            panel.add(createStatCard("Upcoming", String.valueOf(upcoming), new Color(16, 185, 129)));
            panel.add(createStatCard("Completed", String.valueOf(completed), new Color(139, 92, 246)));
            panel.add(createStatCard("Health Score", "95%", new Color(245, 158, 11)));
        }

        return panel;
    }

    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 0));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label
        JLabel labelLabel = new JLabel(label.toUpperCase());
        labelLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelLabel.setForeground(new Color(156, 163, 175)); // Muted grey
        labelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        card.add(labelLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8))); // Space
        card.add(valueLabel);

        return card;
    }

    private JPanel createQuickActionsPanel() {
        JPanel card = createCard("Quick Actions");

        JPanel actionsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        actionsGrid.setBackground(Color.WHITE);
        actionsGrid.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        if (SessionManager.isAdmin()) {
            actionsGrid.add(createActionButton("Register Patient", () -> dashboard.switchPanel("AddPatient")));
            actionsGrid.add(createActionButton("Add Doctor", () -> dashboard.switchPanel("AddDoctor")));
            actionsGrid.add(createActionButton("View Patients", () -> dashboard.switchPanel("Patients")));
            actionsGrid.add(createActionButton("View Doctors", () -> dashboard.switchPanel("Doctors")));
        } else if (SessionManager.isDoctor()) {
            actionsGrid.add(createActionButton("My Schedule", () -> dashboard.switchPanel("Appointments")));
            actionsGrid.add(createActionButton("Pending Approvals", () -> dashboard.switchPanel("Appointments")));
            actionsGrid.add(createActionButton("Patient Records", () -> dashboard.switchPanel("Patients")));
            actionsGrid.add(createActionButton("My Profile", () -> dashboard.switchPanel("DoctorProfile")));
        } else {
            actionsGrid.add(createActionButton("Book Appointment", () -> dashboard.switchPanel("Appointments")));
            actionsGrid.add(createActionButton("My Records", () -> dashboard.switchPanel("PatientProfile")));
            actionsGrid.add(createActionButton("Appointments", () -> dashboard.switchPanel("Appointments")));
            actionsGrid.add(createActionButton("My Profile", () -> dashboard.switchPanel("PatientProfile")));
        }

        card.add(actionsGrid, BorderLayout.CENTER);
        return card;
    }

    private JButton createActionButton(String text, Runnable action) {
        JButton btn = Theme.createGradientButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel createSchedulePanel() {
        JPanel card = createCard("Today's Schedule");

        if (java.beans.Beans.isDesignTime()) {
            return card;
        }

        JPanel scheduleList = new JPanel();
        scheduleList.setLayout(new BoxLayout(scheduleList, BoxLayout.Y_AXIS));
        scheduleList.setBackground(Color.WHITE);
        scheduleList.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        List<AppointmentDetails> todayAppts = getTodayAppointments();

        if (todayAppts.isEmpty()) {
            JLabel noAppts = new JLabel("No appointments scheduled for today");
            noAppts.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noAppts.setForeground(new Color(156, 163, 175));
            scheduleList.add(noAppts);
        } else {
            for (AppointmentDetails appt : todayAppts.stream().limit(5).collect(Collectors.toList())) {
                scheduleList.add(createScheduleItem(appt));
                scheduleList.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scroll = new JScrollPane(scheduleList);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createScheduleItem(AppointmentDetails appt) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(new Color(249, 250, 251));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel timeLabel = new JLabel(appt.getAppointmentTime());
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(new Color(59, 130, 246));

        String name = SessionManager.isDoctor() ? appt.getPatientName() : appt.getDoctorName();
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        item.add(timeLabel, BorderLayout.WEST);
        item.add(nameLabel, BorderLayout.CENTER);

        return item;
    }

    private JPanel createActivityPanel() {
        JPanel card = createCard("Recent Activity");

        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setBackground(Color.WHITE);
        activityList.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        List<AppointmentDetails> recentAppts = AppointmentController.getAllAppointmentDetails().stream()
                .sorted((a1, a2) -> a2.getAppointmentDate().compareTo(a1.getAppointmentDate()))
                .limit(5)
                .collect(Collectors.toList());

        for (AppointmentDetails appt : recentAppts) {
            String activity = "Appointment " + appt.getStatus().toLowerCase() + " - " + appt.getPatientName();
            activityList.add(createActivityItem(activity, appt.getAppointmentDate()));
            activityList.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scroll = new JScrollPane(activityList);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createActivityItem(String text, String time) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel bullet = new JLabel("•");
        bullet.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bullet.setForeground(new Color(229, 231, 235));
        bullet.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(156, 163, 175));

        item.add(bullet, BorderLayout.WEST);
        item.add(textLabel, BorderLayout.CENTER);
        item.add(timeLabel, BorderLayout.EAST);

        return item;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(17, 24, 39));

        card.add(titleLabel, BorderLayout.NORTH);
        return card;
    }

    private List<AppointmentDetails> getTodayAppointments() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<AppointmentDetails> all = AppointmentController.getAllAppointmentDetails();

        if (SessionManager.isDoctor()) {
            int doctorId = SessionManager.getUser().getReferenceId();
            return all.stream()
                    .filter(a -> a.getDoctorId() == doctorId && a.getAppointmentDate().equals(today))
                    .sorted(Comparator.comparing(AppointmentDetails::getAppointmentTime))
                    .collect(Collectors.toList());
        } else if (SessionManager.isPatient()) {
            int patientId = SessionManager.getUser().getReferenceId();
            return all.stream()
                    .filter(a -> a.getPatientId() == patientId && a.getAppointmentDate().equals(today))
                    .sorted(Comparator.comparing(AppointmentDetails::getAppointmentTime))
                    .collect(Collectors.toList());
        }

        return all.stream()
                .filter(a -> a.getAppointmentDate().equals(today))
                .sorted(Comparator.comparing(AppointmentDetails::getAppointmentTime))
                .collect(Collectors.toList());
    }

    private void startClock() {
        // Clock is started in createHeader
    }

    public void cleanup() {
        if (clockTimer != null) {
            clockTimer.stop();
        }
    }
}
