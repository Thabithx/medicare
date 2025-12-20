package view.style;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Theme {
   private static Color hex(String h) {
      return Color.decode(h);
   }

   public static final Color PRIMARY = hex("#007AFF");
   public static final Color ACCENT_TEAL = hex("#30B0C7");
   public static final Color ACCENT_PURPLE = hex("#AF52DE");
   public static final Color ACCENT_PINK = hex("#FF2D55");

   public static final Color GRADIENT_START = hex("#0A50FF");
   public static final Color GRADIENT_END = hex("#8C32FF");

   public static final Color SUCCESS = hex("#34C759");
   public static final Color DANGER = hex("#FF3B30");
   public static final Color WARNING = hex("#FF9500");
   public static final Color BACKGROUND = hex("#F5F5F7");
   public static final Color SIDEBAR_BG = hex("#FFFFFF");
   public static final Color TEXT_DARK = hex("#1C1C1E");
   public static final Color TEXT_GREY = hex("#8E8E93");
   public static final Color WHITE = Color.WHITE;

   public static final Color BG_COLOR = BACKGROUND;
   public static final Color INFO = ACCENT_TEAL;
   public static final Color LIGHT = hex("#F2F2F7");

   public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
   public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
   public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
   public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

   public static final Font FONT_REGULAR = REGULAR_FONT;
   public static final Font FONT_BOLD = BOLD_FONT;
   public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
   public static final Font FONT_TITLE = TITLE_FONT;

   public static JPanel createSidebar() {
      return new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            if (getWidth() <= 0 || getHeight() <= 0)
               return;

            GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, 0, getHeight(), GRADIENT_END);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
         }
      };
   }

   public static JPanel createCard(String title, String value, Color accentInfo) {
      JPanel card = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);

            g2.setColor(accentInfo);
            g2.fillRoundRect(0, 0, 8, getHeight() - 5, 20, 20);
            g2.fillRect(5, 0, 10, getHeight() - 5);

            g2.dispose();
         }
      };
      card.setOpaque(false);
      card.setLayout(new BorderLayout(10, 10));
      card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));

      JLabel lblTitle = new JLabel(title);
      lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
      lblTitle.setForeground(TEXT_GREY);

      JLabel lblValue = new JLabel(value);
      lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
      lblValue.setForeground(TEXT_DARK);

      card.add(lblTitle, BorderLayout.NORTH);
      card.add(lblValue, BorderLayout.CENTER);

      return card;
   }

   public static JPanel createCardPanel() {
      JPanel card = new JPanel();
      card.setBackground(Color.WHITE);
      card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)));
      return card;
   }

   public static ImageIcon loadIcon(String path, int maxWidth, int maxHeight) {
      try {
         java.net.URL url = Theme.class.getResource("/resources/" + path);
         if (url == null) {
            return null;
         }
         ImageIcon icon = new ImageIcon(url);
         Image img = icon.getImage();

         int originalWidth = icon.getIconWidth();
         int originalHeight = icon.getIconHeight();

         if (originalWidth <= 0 || originalHeight <= 0) {
            return icon;
         }

         int newWidth = originalWidth;
         int newHeight = originalHeight;

         if (originalWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (newWidth * originalHeight) / originalWidth;
         }
         if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (newHeight * originalWidth) / originalHeight;
         }

         Image newImg = img.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
         return new ImageIcon(newImg);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static JButton createSidebarButton(String text, String iconName) {
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
            }

            g2.dispose();
            super.paintComponent(g);
         }
      };

      // Load Icon
      String iconPath = "icon_" + iconName + ".png";
      ImageIcon icon = loadIcon(iconPath, 24, 24);
      if (icon != null) {
         btn.setIcon(icon);
      }

      btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
      btn.setForeground(Color.WHITE);
      btn.setHorizontalAlignment(SwingConstants.LEFT);
      btn.setIconTextGap(15);
      btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 20));
      btn.setContentAreaFilled(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
      return btn;
   }

   public static JButton createButton(String text, Color bg) {
      JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
               g2.setColor(bg.darker());
            } else if (getModel().isRollover()) {
               g2.setColor(bg.brighter());
            } else {
               g2.setColor(bg);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
         }
      };
      btn.setFont(BOLD_FONT);
      btn.setForeground(Color.WHITE);
      btn.setContentAreaFilled(false);
      btn.setFocusPainted(false);
      btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
      btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
      return btn;
   }

   public static JButton createButton(String text) {
      return createButton(text, PRIMARY);
   }

   public static JLabel createTitleLabel(String text) {
      JLabel lbl = new JLabel(text);
      lbl.setFont(TITLE_FONT);
      lbl.setForeground(TEXT_DARK);
      return lbl;
   }

   // Date Spinner Helper
   public static JSpinner createDateSpinner() {
      JSpinner spDate = new JSpinner(new SpinnerDateModel());
      JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spDate, "yyyy-MM-dd");
      spDate.setEditor(dateEditor);
      spDate.setPreferredSize(new Dimension(200, 35));
      return spDate;
   }

   public static Border createPadding(int size) {
      return BorderFactory.createEmptyBorder(size, size, size, size);
   }

   public static JButton createGradientButton(String text) {
      JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getWidth() <= 0 || getHeight() <= 0) {
               g2.dispose();
               return;
            }

            GradientPaint gp = new GradientPaint(0, 0, new Color(102, 126, 234),
                  getWidth(), getHeight(), new Color(118, 75, 162));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
         }
      };
      btn.setForeground(Color.WHITE);
      btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
      btn.setFocusPainted(false);
      btn.setBorderPainted(false);
      btn.setContentAreaFilled(false);
      btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
      btn.setPreferredSize(new Dimension(120, 38));
      return btn;
   }

   public static JLabel createIconBadge(String symbol, Color bgColor) {
      JLabel badge = new JLabel(symbol) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
         }
      };
      badge.setFont(new Font("Segoe UI", Font.BOLD, 18));
      badge.setForeground(Color.WHITE);
      badge.setHorizontalAlignment(SwingConstants.CENTER);
      badge.setVerticalAlignment(SwingConstants.CENTER);
      badge.setPreferredSize(new Dimension(36, 36));
      return badge;
   }

   public static JTextField createStyledTextField(int columns) {
      return createPlaceholderTextField(columns, "");
   }

   public static JTextField createPlaceholderTextField(int columns, String placeholder) {
      JTextField field = new JTextField(columns);
      field.setFont(new Font("Segoe UI", Font.PLAIN, 15));

      // Initial Border
      field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));

      // Placeholder Logic
      if (placeholder != null && !placeholder.isEmpty()) {
         field.setText(placeholder);
         field.setForeground(Color.GRAY);
      } else {
         field.setForeground(Color.BLACK);
      }

      field.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusGained(java.awt.event.FocusEvent evt) {
            // Border Highlight
            field.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(10, 80, 255), 2),
                  BorderFactory.createEmptyBorder(9, 11, 9, 11)));

            // Placeholder Clear
            if (field.getText().equals(placeholder)) {
               field.setText("");
               field.setForeground(Color.BLACK);
            }
         }

         public void focusLost(java.awt.event.FocusEvent evt) {
            // Border Reset
            field.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                  BorderFactory.createEmptyBorder(10, 12, 10, 12)));

            // Placeholder Restore
            if (field.getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
               field.setText(placeholder);
               field.setForeground(Color.GRAY);
            }
         }
      });
      return field;
   }

   public static JPasswordField createStyledPasswordField(int columns) {
      JPasswordField field = new JPasswordField(columns);
      field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
      field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));
      field.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusGained(java.awt.event.FocusEvent evt) {
            field.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(10, 80, 255), 2),
                  BorderFactory.createEmptyBorder(9, 11, 9, 11)));
         }

         public void focusLost(java.awt.event.FocusEvent evt) {
            field.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                  BorderFactory.createEmptyBorder(10, 12, 10, 12)));
         }
      });
      return field;
   }

   public static JTextArea createStyledTextArea(int rows, int columns) {
      JTextArea area = new JTextArea(rows, columns);
      area.setFont(new Font("Segoe UI", Font.PLAIN, 15));
      area.setLineWrap(true);
      area.setWrapStyleWord(true);
      area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));
      area.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusGained(java.awt.event.FocusEvent evt) {
            area.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(10, 80, 255), 2),
                  BorderFactory.createEmptyBorder(9, 11, 9, 11)));
         }

         public void focusLost(java.awt.event.FocusEvent evt) {
            area.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                  BorderFactory.createEmptyBorder(10, 12, 10, 12)));
         }
      });
      return area;
   }

   public static void applyTableStyling(JTable table) {
      table.setRowHeight(35);
      table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      table.setShowVerticalLines(false);
      table.setGridColor(new Color(229, 231, 235));
      table.setIntercellSpacing(new Dimension(0, 0));
      table.setSelectionBackground(new Color(235, 240, 255));
      table.setSelectionForeground(TEXT_DARK);

      // Header
      javax.swing.table.JTableHeader header = table.getTableHeader();
      header.setFont(new Font("Segoe UI", Font.BOLD, 14));
      header.setBackground(Color.WHITE);
      header.setForeground(TEXT_GREY);
      header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(229, 231, 235)));
      header.setPreferredSize(new Dimension(0, 40));

      // Alternating Row Colors
      table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
               c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
            }
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Cell padding
            return c;
         }
      });
   }

   public static JLabel createLabel(String text) {
      JLabel lbl = new JLabel(text);
      lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
      lbl.setForeground(new Color(55, 65, 81)); // Dark grey
      return lbl;
   }
}
