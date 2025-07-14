📇 Smart Contact Manager
Smart Contact Manager is a full-featured Spring Boot web application designed to help users securely manage their contacts with ease. It includes advanced features like OTP-based password reset, email notifications, and data export in Excel format.

🚀 Features
🔐 User Registration and Login
🔒 OTP-based Password Reset via Email
📧 Email API integration for notifications
👥 Role-based Access Control (User/Admin)
📒 Contact CRUD (Create, Read, Update, Delete)
📤 Export Contacts to Excel (.xlsx) Format
📁 Upload Profile Images for Contacts
🔎 Search and Filter Contacts
🧾 Clean UI with Thymeleaf Templates
🔐 Secured with Spring Security
🚫 Unauthorized Access Restriction
📊 Admin Dashboard

🛠 Tech Stack
Layer	Technology
Frontend	HTML, CSS, Bootstrap, Thymeleaf
Backend	Java 17, Spring Boot, Spring MVC, Spring Security
Database	MySQL
Email Service	JavaMailSender (Spring Boot Starter Mail)
Excel Export	Apache POI
Build Tool	Maven
Template Engine	Thymeleaf


📤 Excel Export Feature
Users can export all their contact data into an Excel file with one click.
✔ Uses Apache POI
✔ Saves file as .xlsx
✔ Custom styling and formatting supported

🔐 OTP-based Password Reset
If a user forgets their password:
They enter their registered email.
An OTP is sent to their email.
User verifies OTP and sets a new password.

✔ Built using JavaMailSender
✔ OTP has an expiration time
✔ Secure and user-friendly flow

🏗 Project Structure

SCM/
├── src/
│   ├── main/
│   │   ├── java/com/scm/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── SmartContactManagerApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
├── pom.xml
└── README.md

📦 Setup Instructions
1. Clone the Repository

git clone https://github.com/yourusername/SmartContactManager.git
cd SmartContactManager

2. Set MySQL Credentials in application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/scm
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

3. Email Configuration

Properties props = new Properties();
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.starttls.enable", "true");
props.put("mail.smtp.host", "smtp.gmail.com");
props.put("mail.smtp.port", "587");

Session session = Session.getInstance(props,
    new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("your_email@gmail.com", "your_app_password");
        }
    });


4. CREATE DATABASE scm;

5. Run the App
./mvnw spring-boot:run

6. Visit in Browser

http://localhost:8080

<img width="1366" height="768" alt="Screenshot (33)" src="https://github.com/user-attachments/assets/d42218b0-646b-4401-a796-e284af9a1988" />
<img width="1366" height="768" alt="Screenshot (32)" src="https://github.com/user-attachments/assets/d86221b0-4a75-4042-b461-f36f37ee3675" />
<img width="1366" height="768" alt="Screenshot (30)" src="https://github.com/user-attachments/assets/ff4265e3-5e8c-4f79-ae98-71e7f0c95597" />
<img width="1366" height="768" alt="Screenshot (29)" src="https://github.com/user-attachments/assets/f4a53a0b-dc6c-4c1a-a91f-4501d74983be" />
<img width="1366" height="768" alt="Screenshot (28)" src="https://github.com/user-attachments/assets/43731383-2779-476e-a6d6-04b50fb4f084" />
<img width="1366" height="768" alt="Screenshot (27)" src="https://github.com/user-attachments/assets/442da1f3-ad60-448d-afa3-385dde0d5ec7" />
<img width="1366" height="768" alt="Screenshot (26)" src="https://github.com/user-attachments/assets/a65365fa-95e7-4175-b506-cb8d13562d52" />
<img width="1366" height="768" alt="Screenshot (25)" src="https://github.com/user-attachments/assets/4515a0cb-4cec-4985-91cd-b18b514f4d50" />
<img width="1366" height="768" alt="Screenshot (24)" src="https://github.com/user-attachments/assets/b54b3e24-f938-4b8a-9738-9962f7e0cad7" />

👨‍💻 Author
Shiv Kumar Umar
Java Fullstack Developer
