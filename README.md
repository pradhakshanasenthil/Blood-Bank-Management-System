# Blood Bank Management System

A digital platform designed to streamline the process of managing blood donation, storage, and distribution. This project was developed as a part of the **Database Management Systems (23ITI402)** course for the Academic Year 2025-2026 at **Dr. Mahalingam College of Engineering and Technology**.

## 📌 Project Objective
The system aims to improve the efficiency of maintaining donor records and tracking blood inventory. By integrating a digital database, the system ensures timely availability of blood for patients, reduces manual record-keeping errors, and simplifies administrative tasks such as monitoring stock levels and managing donor information.

## 🚀 Key Features
*   **Administrator Authentication:** Secure login for authorized staff.
*   **Real-Time Inventory Tracking:** Monitor available blood units by group (A+, B+, O-, etc.).
*   **Donor Management:** Store and retrieve donor details including name, blood group, and contact info.
*   **Full CRUD Operations:** Add, update, and delete donor and blood unit records easily.
*   **Automated Validation:** Ensures phone numbers are 10 digits and unit counts are numeric.
*   **Modern UI:** Built with Java Swing using a clean, professional aesthetic.

## 🛠️ Technology Stack
*   **Frontend:** Java Swing (GUI)
*   **Backend:** MySQL Database
*   **Connectivity:** JDBC (Java Database Connectivity)

## 📊 Database Schema
The system uses a MySQL database named `bloodbank` with the following tables:

| Table | Columns |
| :--- | :--- |
| **admin** | `username`, `password` |
| **blood_units** | `id`, `blood_group`, `units_available` |
| **donors** | `id`, `name`, `blood_group`, `phone` |

## ⚙️ Installation & Setup
1.  **MySQL Setup:**
    *   Create a database named `bloodbank`.
    *   Create the required tables (`admin`, `blood_units`, `donors`).
    *   Ensure your MySQL user is `root` and password is `abcd` (or update the credentials in the Java source code).
2.  **Java Environment:**
    *   Ensure JDK is installed.
    *   Add the **MySQL Connector/J** library to your project classpath.
3.  **Execution:**
    *   Compile and run `BloodBankManagementSystemRefactored.java`.

## 📂 Project Structure
*   `BloodBankManagementSystemRefactored.java`: Main application file containing GUI logic and Database connectivity.
*   `DBMS Mini Project.docx`: Project documentation and certification details.

## 🎓 Academic Details
*   **Institution:** Dr. Mahalingam College of Engineering and Technology, Pollachi.
*   **Department:** Information Technology
*   **Semester:** IV
*   **Course Code:** 23ITI402 - Database Management Systems
