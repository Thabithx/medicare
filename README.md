# Medicare Hospital Management System

<div align="center">

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)](https://www.eclipse.org/)

</div>


<br />

<div align="center">
  <p align="center">
    A high-performance, desktop-based medical ecosystem built to transform hospital operations.
    <br />
    <a href="#key-features"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="#issues">Report Bug</a>
    ·
    <a href="#issues">Request Feature</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#key-features">Key Features</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
  </ol>
</details>

## About The Project

Medicare is not just another hospital management software, it's a high-performance medical ecosystem built to transform the way hospitals operate. It is designed to handle the high-stakes environment of a modern hospital with the elegance of a modern desktop OS.

### Built With

*   **Language:** Java 17+ (MVC Architecture)
*   **GUI:** Deep AWT 2D Graphics & Custom Swing Components
*   **Database:** MySQL (Robust Persistence)
*   **Security:** Role-Based Access Control (RBAC) & Secure Session Management

<p align="right">(<a href="#top">back to top</a>)</p>

## Key Features

### 🖥️ Smart UI Engine
Custom-built rendering engine that delivers a modern, fluid user experience, breaking away from the traditional, rigid look of standard Java applications.

### 🔒 Role-Based Access Control
Secure session management ensuring that each actor (Admin, Doctor, Receptionist) accesses only the data and features relevant to their role.

### ⏱️ Time Analytics & Reporting
Built-in reporting tools that calculate doctor efficiency and hospital revenue in real-time. The optimized SQL layer handles massive datasets effortlessly.

### 🏥 Medical Command Center
A unified dashboard for hospital administrators to track clinical performance, financial health, and operational metrics at a glance.

### 🩺 Doctor’s Clinical Suite
A radical new workflow for doctors to manage schedules, approve appointments, and view deep patient histories with a single click.

### 🔔 Centralized Notifications
An intelligent alert system that ensures critical patient updates and appointment requests are never overlooked.

### 📂 The Patient Profile
A beautiful, deep medical ledger that tracks allergies, chronic conditions, and medical history in an easy-to-read format.

<p align="right">(<a href="#top">back to top</a>)</p>

## Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

*   **Java Development Kit (JDK) 17** or higher.
*   **MySQL Server** installed and running.
*   **Eclipse IDE** (or any Java IDE).

### Installation

1.  **Clone the Repo**
    ```
    git clone https://github.com/Thabithx/medicare.git
    ```
2.  **Database Setup**
    *   Create a new database in MySQL.
    *   Import the `medicare.sql` file provided in the root directory.
    *   Update the database connection settings in `src/medicare/Connectdb.java`

3.  **Launch the Application**
    *   Open the project in your IDE.
    *   Run `LoginFrame.java` to start the application.

<p align="right">(<a href="#top">back to top</a>)</p>

## Usage

1.  **Login:** Use the credentials provided in the database (or create a new admin account manually in the DB if first run).
2.  **Dashboard:** Navigate through the dashboard to manage doctors, patients, and appointments.
3.  **Appointments:** Receptionists can book slots; Doctors can approve/reject them.
4.  **Reports:** View detailed analytics on the admin panel.

<p align="right">(<a href="#top">back to top</a>)</p>

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>
