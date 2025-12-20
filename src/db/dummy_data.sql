-- Disable FK checks to allow truncation
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data (DELETE is safer for FKs than TRUNCATE)
DELETE FROM Appointments;
DELETE FROM Doctors;
DELETE FROM Patients;
DELETE FROM Users;

-- Reset Auto Increment
ALTER TABLE Appointments AUTO_INCREMENT = 1;
ALTER TABLE Doctors AUTO_INCREMENT = 1;
ALTER TABLE Patients AUTO_INCREMENT = 1;
ALTER TABLE Users AUTO_INCREMENT = 1;


SET FOREIGN_KEY_CHECKS = 1;

-- ========================================================
-- 1. Insert Doctors
-- ========================================================
INSERT INTO Doctors (doctor_id, first_name, last_name, gender, specialty, qualification, schedule, timeslot, email, phone, address, dob, profile_picture_path) VALUES
(1, 'Sarah', 'Connor', 'Female', 'Cardiology', 'MBBS, MD', 'Mon-Fri', '09:00 AM - 05:00 PM', 'dr.sarah@medicare.com', '555-0101', '101 Heartbeat Ave, NY', '1980-05-15', ''),
(2, 'James', 'Wilson', 'Male', 'Oncology', 'MBBS, PhD', 'Mon-Wed, Fri', '10:00 AM - 04:00 PM', 'dr.james@medicare.com', '555-0102', '202 Cancer Care Blvd, NY', '1975-08-22', ''),
(3, 'Emily', 'White', 'Female', 'Pediatrics', 'MBBS, MD', 'Tue-Sat', '08:00 AM - 02:00 PM', 'dr.emily@medicare.com', '555-0103', '303 Kids Lane, CA', '1985-03-30', ''),
(4, 'Michael', 'House', 'Male', 'Neurology', 'MD, PhD', 'Mon-Thu', '11:00 AM - 06:00 PM', 'dr.house@medicare.com', '555-0104', '404 Brain St, Chicago', '1969-06-11', ''),
(5, 'Jessica', 'Pearson', 'Female', 'Dermatology', 'MBBS', 'Wed-Sun', '09:00 AM - 03:00 PM', 'dr.jessica@medicare.com', '555-0105', '505 Skin Care Rd, Miami', '1978-11-05', '');

-- ========================================================
-- 2. Insert Patients
-- ========================================================
INSERT INTO Patients (patient_id, first_name, last_name, gender, dob, blood_group, phone, email, address, medical_history, medications, allergies, profile_picture_path) VALUES
(1, 'John', 'Doe', 'Male', '1990-01-01', 'O+', '555-1001', 'john.doe@gmail.com', '123 Main St, NY', 'Diabetes Type 2', 'Metformin', 'Peanuts', ''),
(2, 'Alice', 'Smith', 'Female', '1992-07-14', 'A+', '555-1002', 'alice.smith@yahoo.com', '456 Elm St, CA', 'None', 'None', 'None', ''),
(3, 'Bob', 'Johnson', 'Male', '1985-04-23', 'B-', '555-1003', 'bob.j@hotmail.com', '789 Oak Ave, TX', 'Hypertension', 'Lisinopril', 'Penicillin', ''),
(4, 'Clara', 'Oswald', 'Female', '1995-11-23', 'AB+', '555-1004', 'clara.oswald@bbc.co.uk', '101 Box St, London', 'Asthma', 'Albuterol Inhaler', 'None', ''),
(5, 'Bruce', 'Wayne', 'Male', '1980-02-19', 'O-', '555-1005', 'bruce.wayne@wayneent.com', '1007 Mountain Dr, Gotham', 'Multiple Fractures (Healed)', 'Painkillers (Occasional)', 'Validation', '');

-- ========================================================
-- 3. Insert Users (Auth)
-- ========================================================
-- Note: Passwords are 'password123' for simplicity in demo
INSERT INTO Users (email, password, role, reference_id) VALUES
('admin@medicare.com', 'admin123', 'ADMIN', NULL),
-- Doctor Users
('dr.sarah@medicare.com', 'password123', 'DOCTOR', 1),
('dr.james@medicare.com', 'password123', 'DOCTOR', 2),
('dr.emily@medicare.com', 'password123', 'DOCTOR', 3),
('dr.house@medicare.com', 'password123', 'DOCTOR', 4),
('dr.jessica@medicare.com', 'password123', 'DOCTOR', 5),
-- Patient Users
('john.doe@gmail.com', 'password123', 'PATIENT', 1),
('alice.smith@yahoo.com', 'password123', 'PATIENT', 2),
('bob.j@hotmail.com', 'password123', 'PATIENT', 3),
('clara.oswald@bbc.co.uk', 'password123', 'PATIENT', 4),
('bruce.wayne@wayneent.com', 'password123', 'PATIENT', 5);

-- ========================================================
-- 4. Insert Appointments
-- ========================================================
INSERT INTO Appointments (patient_id, doctor_id, appointment_date, appointment_time, status, remarks) VALUES
-- Past Appointments
(1, 1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), '10:00 AM', 'COMPLETED', 'Routine checkup, BP stable.'),
(2, 3, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '02:00 PM', 'COMPLETED', 'Flu symptoms, prescribed rest.'),
(5, 4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '11:30 AM', 'CANCELLED', 'Patient emergency elsewhere.'),

-- Today's Appointments
(3, 2, CURDATE(), '09:00 AM', 'SCHEDULED', 'Follow-up on X-Ray results.'),
(4, 5, CURDATE(), '01:00 PM', 'PENDING', 'Skin rash consultation.'),
(1, 3, CURDATE(), '03:15 PM', 'ACCEPTED', 'Child checkup.'),

-- Future Appointments
(2, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00 AM', 'SCHEDULED', 'Cardiac stress test.'),
(5, 4, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '04:00 PM', 'PENDING', 'Neurological assessment.'),
(3, 2, DATE_ADD(CURDATE(), INTERVAL 7 DAY), '11:00 AM', 'SCHEDULED', 'Chemo session 3.'),
(1, 5, DATE_ADD(CURDATE(), INTERVAL 10 DAY), '02:30 PM', 'PENDING', 'Regular skin screening.');
