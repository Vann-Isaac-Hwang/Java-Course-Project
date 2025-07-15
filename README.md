# Java-Course-Project
A simple frontend and backend separated library management system.
<img src="https://github.com/Vann-Isaac-Hwang/Java-Course-Project/blob/main/Snapshots/User%20GUI.png?raw=true">
## System Design
The system follows a client - server architecture. The client side is responsible for presenting the graphical user interface (GUI) and interacting with users, while the server side manages the database operations and processes client requests. This separation allows for better scalability and maintainability.
### Client - Side Design
The client is developed using JavaFX. It includes several key components:
* **Login and Registration Windows:** These are the entry points for users. They allow users to either log in with existing credentials or register a new account. The system differentiates between normal users and admins during the login process.
* **User - Specific Interfaces:** Based on the user's role (normal user or admin), different interfaces are presented. Normal users can search for books, view recommended books, borrow and return books, and check their borrowing records. Admins have additional privileges such as managing book records, viewing all borrowing records, and managing user accounts.
* **Table Views and Buttons:** Table views are used to display book lists and borrowing records in an organized manner. Buttons are provided for actions such as borrowing, returning, and searching, making the user experience more intuitive.
* **Popup Alerts:** Popup alerts are used to notify users of important events, such as successful borrowing, overdue notifications, and errors during operations.
### Server - Side Design
The server side is implemented in Java and has the following main features:
* **Core Classes:** The system defines several core classes, including User, NormalUser, Admin, Book, and BorrowRecord. These classes use object - oriented programming concepts such as inheritance and polymorphism to represent different entities in the library management system.
* **Multithreading:** A thread pool is used to handle multiple client requests concurrently. This ensures that the server can handle a large number of clients without blocking, improving the overall performance and responsiveness of the system.
* **Networking:** The client and server communicate using Socket programming. The client sends operation requests to the server, and the server processes these requests, performs database operations, and sends back the corresponding responses.
* **Database Operations:** The server uses JDBC to interact with the SQLite database. It manages the creation, query, update, and deletion of records in tables such as Users, Books, and BorrowingRecords.
## GUI Design
The GUI design follows the principles of simplicity, usability, and aesthetics. The goal is to provide users with an intuitive and visually appealing interface to interact with the library management system.
### Login and Registration Windows
* **Login Window:** This window contains input fields for the username and password, as well as a checkbox to indicate whether the user is an admin. A "Login" button is provided to submit the credentials.
* **Registration Window:** The registration window has input fields for the username and password. After the user fills in the information and clicks the "Register" button, the system checks the availability of the username and creates a new account if the registration is successful.
### User - Specific Interfaces
#### Normal User Interface:
* **Book Search Area:** A text field and a combo box are provided for users to search for books by title, author, category, ID, or status. A "Search" button triggers the search operation.
* **Book List Table:** A table view displays the list of books, including columns for book name, author, genre, ID, and status. Users can select a book from the table to view its details.
* **Recommended Books Area:** Another table view shows the recommended books for the user. These recommendations are generated using collaborative filtering or content - based filtering algorithms.
* **Borrow and Return Buttons:** Buttons are provided for users to borrow or return books. When a user clicks these buttons, the system updates the book status and borrowing records accordingly.
#### Admin Interface:
* **Book Management Area:** Admins can add, update, or delete book records using input fields and buttons.
* **Borrowing Records Table:** A table view displays all borrowing records, including information such as the borrower, action (borrow or return), book ID, and timestamp.
* **User Account Management Area:** Admins can manage user accounts, such as freezing or unfreezing accounts, using buttons and input fields.
### Popup Alerts
Popup alerts are designed to be concise and clear. They use appropriate icons and colors to indicate the type of message (e.g., green for success, red for error). The alerts provide important information to users, such as the result of an operation or a reminder.
## Database Design
SQLite is chosen as the database for this system because it is lightweight, easy to integrate with Java, and does not require a separate server process. It is suitable for small - to - medium - sized applications like this library management system.
### Table Design
#### Users Table:
* ID: An auto - incremented integer primary key.
* Username: A unique text field representing the user's username.
* Password: A text field storing the user's password.
* PhoneNumber: A text field for the user's phone number.
* IsBanned: An integer field indicating whether the user is banned (0 for not banned, 1 for banned).
#### Admins Table:
* ID: An auto - incremented integer primary key.
* Username: A unique text field representing the admin's username.
* Password: A text field storing the admin's password.
* PhoneNumber: A text field for the admin's phone number.
#### Books Table:
* ID: An auto - incremented integer primary key.
* BookName: A text field representing the book's name.
* Author: A text field for the book's author.
* Genre: A text field indicating the book's genre.
* BookID: A unique text field representing the book's ID.
* Status: A text field indicating the book's status (e.g., "Available", "Borrowed").
* Borrower: A text field storing the username of the borrower when the book is borrowed.
#### BorrowingRecords Table:
* ID: An auto - incremented integer primary key.
* Borrower: A text field representing the borrower's username.
* Action: A text field indicating the action (either "Borrow" or "Return").
* BookID: A text field representing the ID of the book involved in the action.
* Timestamp: A datetime field recording the time of the action.
## Snapshots
### Login Window
<img src="https://github.com/Vann-Isaac-Hwang/Java-Course-Project/blob/main/Snapshots/Login%20Window.png?raw=true">
### User Interface
<img src="https://github.com/Vann-Isaac-Hwang/Java-Course-Project/blob/main/Snapshots/User%20GUI.png?raw=true">
### Admin Interface
<img src="https://github.com/Vann-Isaac-Hwang/Java-Course-Project/blob/main/Snapshots/Admin%20GUI.png?raw=true">
### Server End
<img src="https://github.com/Vann-Isaac-Hwang/Java-Course-Project/blob/main/Snapshots/Server%20End.png?raw=true">
