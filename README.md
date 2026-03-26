# 📚 Library Management System (LMS)

## 📌 Project Overview
The **Library Management System (LMS)** is a Java-based application designed to automate library operations.  
It simplifies tasks such as **book management, user handling, borrowing/returning books, and inventory tracking**.

The system is built using a **layered architecture with MVC principles** and integrates database connectivity for real-world usage.

---

## 🏗 Architecture

This project follows a **Layered + MVC-inspired Architecture**:

- **Model Layer** → Represents core entities (Book, User)  
- **DAO Layer** → Handles database operations  
- **Service Layer** → Contains business logic  
- **UI Layer** → Manages user interaction (Console-based)  
- **Database Layer** → Handles DB connection  

---

## 🚀 Features

- 👤 User registration and management  
- 📖 Book management (Add, Remove, Search, View)  
- 🔄 Borrowing and returning books  
- 🪪 Membership handling  
- 🔔 Due date tracking  
- 🗂 Inventory management  

---

## 🧠 Data Structures Used

- 🌳 **Binary Search Tree (BST)** → Book inventory management  
- 📥 **Queue** → Borrow requests  
- 📤 **Stack** → Return tracking  
- 🔗 **Linked List** → User history  

---

## 🛠 Technologies Used

- Java  
- JDBC (Database Connectivity)  
- SQL  
- OOP Principles  

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/Ilmaan-Ahamed/Libray_LMS.git
```

### 2️⃣ Navigate to Project Directory
```bash
cd Libray_LMS
```

### 3️⃣ Configure Database
- Import the `Database.sql` file into your database  
- Update database credentials in:
```java
DBConnection.java
```

### 4️⃣ Run the Application
- Open the project in your IDE (IntelliJ / Eclipse)  
- Run:
```java
Main.java
```

---

## 📁 Project Structure

```
Libray_LMS/
│
├── src/
│   ├── Dao/                      # Data Access Layer
│   │   ├── BookDAO.java
│   │   ├── IssuedBookDAO.java
│   │   └── UserDAO.java
│   │
│   ├── data/                     # Data Structures
│   │   ├── BookInventory.java
│   │   └── BSTNode.java
│   │
│   ├── db/                       # Database Connection
│   │   └── DBConnection.java
│   │
│   ├── library/                  # Main Entry Point
│   │   └── Main.java
│   │
│   ├── model/                    # Entity Classes
│   │   ├── Book.java
│   │   └── User.java
│   │
│   ├── service/                  # Business Logic
│   │   └── LibraryService.java
│   │
│   ├── ui/                       # Console UI
│   │   └── ConsoleUI.java
│   │
│   └── test/                     # Unit Testing
│       └── BookTest.java
│
├── Database.sql
├── LICENSE
└── README.md
```

---

## 🧪 Testing

Run tests using your Java testing framework (e.g., JUnit).

---

## 🔗 Project Repository

[View on GitHub](https://github.com/Ilmaan-Ahamed/Libray_LMS)

---

## 🚀 Future Improvements

- 🌐 Web-based UI (Spring Boot / React)  
- 📊 Admin dashboard  
- 🤖 AI-based book recommendation system  
- 📱 Mobile application integration  

---

## 🤝 Contributing

1. Fork the repository  
2. Create a new branch  
```bash
git checkout -b feature/YourFeature
```
3. Commit changes  
```bash
git commit -m "Add new feature"
```
4. Push to branch  
```bash
git push origin feature/YourFeature
```
5. Open a Pull Request  

---

## 👨‍💻 Author

**Ilmaan Ahamed**  
Software Engineering Undergraduate  
SLTC Research University  

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!