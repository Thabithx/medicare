package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import controller.AppointmentController;
import model.Doctor;
import model.AppointmentDetails;

public class SchedulerService {
   public static List<String> getAvailableSlots(Doctor doctor, Date date) {
      List<String> slots = new ArrayList<>();

      String timeSlotRange = doctor.getTimeslot(); // e.g., "09:00 AM - 05:00 PM"
      if (timeSlotRange == null || !timeSlotRange.contains("-")) {
         return slots;
      }

      int startHour = 9;
      int endHour = 17;

      try {
         String[] parts = timeSlotRange.split("-");
         String startStr = parts[0].trim();
         String endStr = parts[1].trim();

         // Helper to parse "09:00 AM" or "09:00" to 24h int hour
         startHour = parseHour(startStr);
         endHour = parseHour(endStr);

         // Fix for PM range crossover or logic
         if (endHour <= startHour && endHour < 12) {
            endHour += 12;
         }
      } catch (Exception e) {
         e.printStackTrace();
         // Fallback default
         startHour = 9;
         endHour = 17;
      }

      // 2. Fetch existing appointments for this doctor on this date
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String dateStr = sdf.format(date);

      List<AppointmentDetails> allApps = new AppointmentController().getAllAppointmentDetails();
      List<String> bookedTimes = new ArrayList<>();

      for (AppointmentDetails app : allApps) {
         if (app.getDoctorId() == doctor.getId() && app.getAppointmentDate().equals(dateStr)) {
            // Normalized stored time to removing spaces or ensuring format match
            // stored: "10:00 AM" or "14:00"
            bookedTimes.add(app.getAppointmentTime());
         }
      }

      // 3. Generate slots
      for (int h = startHour; h < endHour; h++) {
         // :00 slot
         String time1 = formatTime(h, 0);
         if (!isBooked(time1, bookedTimes))
            slots.add(time1);

         // :30 slot
         String time2 = formatTime(h, 30);
         if (!isBooked(time2, bookedTimes))
            slots.add(time2);
      }

      return slots;
   }

   private static int parseHour(String timeStr) {
      // timeStr: "09:00 AM" or "14:00"
      try {
         // Remove all non alphanumeric except : and space
         String clean = timeStr.toUpperCase().replace(".", "");
         boolean isPM = clean.contains("PM");
         boolean isAM = clean.contains("AM");

         String[] t = clean.replace("AM", "").replace("PM", "").trim().split(":");
         int h = Integer.parseInt(t[0]);

         if (isPM && h != 12)
            h += 12;
         if (isAM && h == 12)
            h = 0;

         return h;
      } catch (Exception e) {
         return 9; // default
      }
   }

   private static String formatTime(int h, int m) {
      // Return in DB format "hh:mm a" ideally, or "HH:mm"
      // The DB dummy data seems to have mixes, but let's stick to HH:mm for
      // simplicity or hh:mm a if that's what UI wants
      // The user complaint "timeslots say not null" suggests they want standard
      // readable slots.
      // Let's output "hh:mm a" format for consistency with stored data "10:00 AM"

      /*
       * However, the DB insertion in dummy data uses '10:00 AM'.
       * So we should generate '10:00 AM'.
       */
      String ampm = "AM";
      int dispH = h;
      if (h >= 12) {
         ampm = "PM";
         if (h > 12)
            dispH -= 12;
      }
      if (dispH == 0)
         dispH = 12;

      return String.format("%02d:%02d %s", dispH, m, ampm);
   }

   private static boolean isBooked(String slot, List<String> booked) {
      // slot: "10:00 AM"
      // booked: ["10:00 AM", "02:00 PM"]
      for (String b : booked) {
         if (b.equalsIgnoreCase(slot))
            return true;
         // Also check if b is HH:mm vs hh:mm a
         // Simple loose matching for robustness
         if (b.startsWith(slot.substring(0, 5)))
            return true;
      }
      return false;
   }
}
