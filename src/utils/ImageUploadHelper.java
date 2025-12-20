package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;


public class ImageUploadHelper {

   private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
   private static final String PROFILE_IMAGES_DIR = "profile_images";
   private static final String DEFAULT_PATIENT_IMAGE = "src/resources/default_patient.png";
   private static final String DEFAULT_DOCTOR_IMAGE = "src/resources/default_doctor.png";


   public static String selectProfilePicture() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select Profile Picture");

      // Filter for image files only
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files (JPG, JPEG, PNG)", "jpg", "jpeg", "png");
      fileChooser.setFileFilter(filter);
      fileChooser.setAcceptAllFileFilterUsed(false);

      int result = fileChooser.showOpenDialog(null);

      if (result == JFileChooser.APPROVE_OPTION) {
         File selectedFile = fileChooser.getSelectedFile();

         // Validate file
         String validationError = validateImageFile(selectedFile);
         if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Invalid File",
                  JOptionPane.ERROR_MESSAGE);
            return null;
         }

         // Copy file to profile_images directory
         return copyToProfileImagesDir(selectedFile);
      }

      return null;
   }


   private static String validateImageFile(File file) {
      // Check file size
      if (file.length() > MAX_FILE_SIZE) {
         return "File size exceeds 5MB limit.";
      }

      // Check file extension
      String fileName = file.getName().toLowerCase();
      if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
         return "Only JPG and PNG files are allowed.";
      }

      // Verify it's actually an image
      try {
         BufferedImage img = ImageIO.read(file);
         if (img == null) {
            return "File is not a valid image.";
         }
      } catch (IOException e) {
         return "Unable to read image file.";
      }

      return null;
   }

   /**
    * Copies selected file to profile_images directory
    * 
    * @return Path to copied file
    */
   private static String copyToProfileImagesDir(File sourceFile) {
      try {
         // Create directory if it doesn't exist
         File dir = new File(PROFILE_IMAGES_DIR);
         if (!dir.exists()) {
            dir.mkdirs();
         }

         // Generate unique filename
         String timestamp = String.valueOf(System.currentTimeMillis());
         String extension = getFileExtension(sourceFile.getName());
         String newFileName = "profile_" + timestamp + extension;

         File destFile = new File(PROFILE_IMAGES_DIR, newFileName);
         Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

         return destFile.getPath();
      } catch (IOException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, "Failed to save image: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
         return null;
      }
   }

   //Gets file extension including the dot
   private static String getFileExtension(String fileName) {
      int lastDot = fileName.lastIndexOf('.');
      return (lastDot > 0) ? fileName.substring(lastDot) : "";
   }

   /**
    * Loads profile image from path
    * 
    * @param imagePath Path to image, can be null
    * @param isDoctor  Whether this is for a doctor (determines default image)
    * @param width     Desired width
    * @param height    Desired height
    * @return ImageIcon with the profile picture
    */
   public static ImageIcon loadProfileImage(String imagePath, boolean isDoctor, int width, int height) {
      BufferedImage img = null;

      // Try to load from path
      if (imagePath != null && !imagePath.isEmpty()) {
         File imageFile = new File(imagePath);
         if (imageFile.exists()) {
            try {
               img = ImageIO.read(imageFile);
            } catch (IOException e) {
               System.err.println("Failed to load image from: " + imagePath);
            }
         }
      }

      // Fallback to default if not loaded
      if (img == null) {
         String defaultPath = isDoctor ? DEFAULT_DOCTOR_IMAGE : DEFAULT_PATIENT_IMAGE;
         File defaultFile = new File(defaultPath);
         if (defaultFile.exists()) {
            try {
               img = ImageIO.read(defaultFile);
            } catch (IOException e) {
               System.err.println("Failed to load default image: " + defaultPath);
            }
         }
      }

      // If still null, create a placeholder
      if (img == null) {
         img = createPlaceholderImage(width, height, isDoctor);
      }

      // Resize and return
      Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
      return new ImageIcon(scaledImg);
   }

   /**
    * Creates a circular profile image
    */
   public static ImageIcon createCircularImage(ImageIcon source, int diameter) {
      BufferedImage circularImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = circularImage.createGraphics();

      // Enable antialiasing for smooth edges
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Create circular clip
      g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));

      // Draw the image
      g2.drawImage(source.getImage(), 0, 0, diameter, diameter, null);
      g2.dispose();

      return new ImageIcon(circularImage);
   }

   /**
    * Creates a placeholder image when no default is available
    */
   private static BufferedImage createPlaceholderImage(int width, int height, boolean isDoctor) {
      BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2 = img.createGraphics();

      // Background color
      g2.setColor(isDoctor ? new Color(10, 80, 255) : new Color(140, 50, 255));
      g2.fillRect(0, 0, width, height);

      // Draw initials
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Segoe UI", Font.BOLD, width / 3));
      String text = isDoctor ? "Dr" : "P";
      FontMetrics fm = g2.getFontMetrics();
      int x = (width - fm.stringWidth(text)) / 2;
      int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
      g2.drawString(text, x, y);

      g2.dispose();
      return img;
   }
}
