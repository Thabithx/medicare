package view; 

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import service.SessionManager;
import view.style.Theme;

public class SidebarPanel extends JPanel {

   private static final long serialVersionUID = 1L;
   private Dashboard dashboard;
   private Map<String, JButton> buttons = new HashMap<>();

   public SidebarPanel(Dashboard dashboard) {
      this.dashboard = dashboard;

      setOpaque(false);

      setLayout(new GridBagLayout());
      setPreferredSize(new Dimension(260, 0));

      initComponents();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      if (getWidth() <= 0 || getHeight() <= 0)
         return;

      GradientPaint gp = new GradientPaint(0, 0, Theme.GRADIENT_START, 0, getHeight(), Theme.GRADIENT_END);
      g2.setPaint(gp);
      g2.fillRect(0, 0, getWidth(), getHeight());
   }

   private void initComponents() {
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 1.0;
      gbc.insets = new Insets(0, 0, 0, 0);

      // -- Logo --
      ImageIcon logoIcon = Theme.loadIcon("icon.png", 220, 80);
      JLabel lblLogo = new JLabel();
      if (logoIcon != null) {
         lblLogo.setIcon(logoIcon);
      } else {
         lblLogo.setText("Medicare +");
         lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 32));
         lblLogo.setForeground(Color.WHITE);
      }
      lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
      lblLogo.setBorder(BorderFactory.createEmptyBorder(40, 20, 60, 20));
      add(lblLogo, gbc);
      gbc.gridy++;

      // -- Buttons (Text Only, Clean) --
      if (SessionManager.isAdmin()) {
         addButton("Dashboard", "dashboard", gbc);
         addButton("Appointments", "appointments", gbc);
         addButton("Patients", "patients", gbc);
         addButton("Doctors", "doctors", gbc);
         addButton("Reports", "reports", gbc);
      } else if (SessionManager.isDoctor()) {
         addButton("My Dashboard", "dashboard", "Dashboard", gbc);
         addButton("My Schedule", "appointments", "Appointments", gbc);
         addButton("My Profile", "doctors", "DoctorProfile", gbc);
         addButton("Notifications", "notifications", gbc);
      } else if (SessionManager.isPatient()) {
         addButton("Home", "dashboard", "Dashboard", gbc);
         addButton("My Appointments", "appointments", "Appointments", gbc);
         addButton("My Profile", "patients", "PatientProfile", gbc);
         addButton("Notifications", "notifications", gbc);
      } else {
         // Fallback
         addButton("Dashboard", "dashboard", gbc);
         addButton("Appointments", "appointments", gbc);
      }

      // Spacer
      gbc.weighty = 1.0;
      JPanel spacer = new JPanel();
      spacer.setOpaque(false);
      add(spacer, gbc);

      // Logout
      gbc.weighty = 0;
      gbc.gridy++;
      JButton btnLogout = createCleanSidebarButton("Logout");
      add(btnLogout, gbc);
      btnLogout.addActionListener(e -> {
         SessionManager.logout();
         dashboard.dispose();
         new LoginFrame().setVisible(true);
      });
   }

   private void addButton(String text, String iconName, GridBagConstraints gbc) {
      addButton(text, iconName, text, gbc);
   }

   private void addButton(String text, String iconName, String command, GridBagConstraints gbc) {
      JButton btn = createCleanSidebarButton(text);
      btn.addActionListener(e -> {
         dashboard.switchPanel(command);
         setSelected(command);
      });
      buttons.put(command, btn);
      add(btn, gbc);
      gbc.gridy++;
   }

   private JButton createCleanSidebarButton(String text) {
      JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isSelected()) {
               g2.setColor(new Color(255, 255, 255, 50));
               g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 15, 15);
            } else if (getModel().isRollover()) {
               g2.setColor(new Color(255, 255, 255, 25));
               g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 15, 15);
            } else {
               g2.setColor(new Color(0, 0, 0, 0));
               g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 15, 15);
            }

            g2.dispose();
            super.paintComponent(g);
         }
      };

      btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
      btn.setForeground(Color.WHITE);
      btn.setHorizontalAlignment(SwingConstants.LEFT);
      btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 20));
      btn.setContentAreaFilled(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
      return btn;
   }

   public void setSelected(String command) {
      for (JButton b : buttons.values()) {
         b.setSelected(false);
      }
      JButton target = buttons.get(command);
      if (target != null) {
         target.setSelected(true);
      }
   }
}
