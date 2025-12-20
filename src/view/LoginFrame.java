package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import service.AuthService;
import service.SessionManager;
import model.User;
import db.DBMigration;
import view.style.Theme;

public class LoginFrame extends JFrame {

   private static final long serialVersionUID = 1L;
   private JTextField txtEmail;
   private JPasswordField txtPassword;
   private JButton btnLogin;

   public static void main(String[] args) {
      // Ensure DB Schema is up to date
      DBMigration.updateDatabaseSchema();

      EventQueue.invokeLater(() -> {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
         } catch (Exception e) {
            e.printStackTrace();
         }
      });
   }

   public LoginFrame() {
      setTitle("Medicare Plus - Login");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 600);
      setLocationRelativeTo(null);
      setResizable(false);

      // Main container with split layout
      JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
      mainPanel.setBackground(Color.WHITE);
      setContentPane(mainPanel);

      // Left Panel - Hero Image
      JPanel leftPanel = createHeroPanel();
      mainPanel.add(leftPanel);

      // Right Panel - Login Form
      JPanel rightPanel = createFormPanel();
      mainPanel.add(rightPanel);
   }

   private JPanel createHeroPanel() {
      JPanel panel = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            try {
               ImageIcon icon = new ImageIcon(getClass().getResource("/resources/login_hero.png"));
               if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                  Image img = icon.getImage();
                  g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
               } else {
                  GradientPaint gp = new GradientPaint(
                        0, 0, new Color(102, 126, 234),
                        0, getHeight(), new Color(118, 75, 162));
                  g2.setPaint(gp);
                  g2.fillRect(0, 0, getWidth(), getHeight());
               }
            } catch (Exception e) {
               GradientPaint gp = new GradientPaint(
                     0, 0, new Color(102, 126, 234),
                     0, getHeight(), new Color(118, 75, 162));
               g2.setPaint(gp);
               g2.fillRect(0, 0, getWidth(), getHeight());
               System.err.println("Hero image not found: " + e.getMessage());
            }

            g2.dispose();
         }
      };
      panel.setLayout(new BorderLayout());
      return panel;
   }

   private JPanel createFormPanel() {
      JPanel panel = new JPanel();
      panel.setBackground(Color.WHITE);
      panel.setLayout(new GridBagLayout());
      panel.setBorder(new EmptyBorder(60, 50, 60, 50));

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridx = 0;
      gbc.insets = new Insets(0, 0, 15, 0);

      // Logo/Title
      JLabel lblLogo = new JLabel("Medicare Plus");
      lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 32));
      lblLogo.setForeground(new Color(10, 80, 255));
      gbc.gridy = 0;
      gbc.insets = new Insets(0, 0, 5, 0);
      panel.add(lblLogo, gbc);

      // Subtitle
      JLabel lblSubtitle = new JLabel("Professional Healthcare Management System");
      lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      lblSubtitle.setForeground(new Color(107, 114, 128));
      gbc.gridy = 1;
      gbc.insets = new Insets(0, 0, 40, 0);
      panel.add(lblSubtitle, gbc);

      // Email Label
      JLabel lblEmail = new JLabel("Email Address");
      lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
      lblEmail.setForeground(new Color(17, 24, 39));
      gbc.gridy = 2;
      gbc.insets = new Insets(0, 0, 8, 0);
      panel.add(lblEmail, gbc);

      // Email Input
      txtEmail = Theme.createStyledTextField(20);
      txtEmail.setPreferredSize(new Dimension(350, 48)); // Keep manual sizing preference
      gbc.gridy = 3;
      gbc.insets = new Insets(0, 0, 20, 0);
      panel.add(txtEmail, gbc);

      // Password Label
      JLabel lblPassword = new JLabel("Password");
      lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
      lblPassword.setForeground(new Color(17, 24, 39));
      gbc.gridy = 4;
      gbc.insets = new Insets(0, 0, 8, 0);
      panel.add(lblPassword, gbc);

      // Password Input
      txtPassword = Theme.createStyledPasswordField(20);
      txtPassword.setPreferredSize(new Dimension(350, 48));
      gbc.gridy = 5;
      gbc.insets = new Insets(0, 0, 30, 0);
      panel.add(txtPassword, gbc);

      // Login Button
      btnLogin = Theme.createGradientButton("Sign In");
      btnLogin.setPreferredSize(new Dimension(350, 52));
      btnLogin.addActionListener(e -> performLogin());
      gbc.gridy = 6;
      gbc.insets = new Insets(0, 0, 20, 0);
      panel.add(btnLogin, gbc);

      // Footer Text
      JLabel lblFooter = new JLabel("Don't have an account? Contact Administrator");
      lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      lblFooter.setForeground(new Color(156, 163, 175));
      lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
      gbc.gridy = 7;
      gbc.insets = new Insets(0, 0, 0, 0);
      panel.add(lblFooter, gbc);

      return panel;
   }

   private void performLogin() {
      String email = txtEmail.getText().trim();
      String password = new String(txtPassword.getPassword());

      if (email.isEmpty() || password.isEmpty()) {
         JOptionPane.showMessageDialog(this,
               "Please enter both email and password",
               "Login Error",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      // Authenticate
      User user = AuthService.login(email, password);

      if (user != null) {
         SessionManager.setUser(user);

         // Open Dashboard
         SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            LoginFrame.this.dispose();
         });
      } else {
         JOptionPane.showMessageDialog(this,
               "Invalid email or password",
               "Login Failed",
               JOptionPane.ERROR_MESSAGE);
         txtPassword.setText("");
      }
   }
}
