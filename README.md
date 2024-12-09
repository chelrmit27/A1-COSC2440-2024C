# Rental Management System 🏡

Welcome to the Rental Management System repository! This project serves as the first individual assignment for the COSC2440 Further Programming course.

---

## About the Project ℹ️

This repository contains the source code for a Rental Management System developed in Java. The system is designed to streamline the management of rental agreements, properties, tenants, owners, hosts, and payments. It provides essential CRUD operations and data persistence, enabling administrators to efficiently handle real estate rental operations.

---

## Key Features 🔑

- **Manage Rental Agreements:** Add, update, delete, and retrieve detailed information about rental agreements.
- **Property Management:** Handle details for both residential and commercial properties.
- **Tenant and Owner Management:** Maintain comprehensive records of tenants, hosts, and property owners.
- **Payment Tracking:** Link payment transactions to rental agreements and tenants for easy tracking.
- **Sorting and Reporting:** Sort agreements by lease period, contract date, renting fee, or status.
- **Data Persistence:** Save and load data using plain text files for seamless operation across sessions.
- **Text-Based User Interface:** Simple command-line interface for user interactions.

---

## Getting Started 🚀

To get started with the Rental Management System:

1. Clone this repository to your local machine using:
```bash
git clone https://github.com/RMIT-Vietnam-Teaching/further-programming-assignment-1-build-a-console-app-chelrmit27.git
```
2. Navigate to the project directory: cd rental-management-system
```bash
cd your_directory
```
3. Compile and run the Java source code:
```bash
javac MainApplication.java
java MainApplication
```
4. Follow the prompts in the command-line interface to interact with the system.

5. Explore features like adding agreements, updating details, and generating reports.

---

### Sample Data Files 📁
The system uses text files to store and retrieve data for core entities:

- **Rental Agreements**: Contains details such as agreement ID, property ID, main tenant, sub-tenants, lease period, contract date, renting fee, and status.
- **Properties**: Includes property ID, address, pricing, type (residential or commercial), and specific attributes.
- **Tenants**: Stores tenant details, including their ID, full name, date of birth, rental agreements, and payment records.
- **Payments**: Tracks payment records linked to tenants and rental agreements.
- **Owners and Hosts**: Stores details of property owners and hosts, including their relationships to properties.

---

### How It Works 🛠️

- **Load Data**: Upon startup, the system reads data from text files and stores it in memory.
- **Perform Operations**: Users can perform CRUD operations on rental agreements, tenants, properties, and more through the main menu.
- **Sort and Report**: View rental agreements sorted by attributes such as lease period or contract date.
- **Save Changes**: Any modifications are saved back to the text files upon exit.

---

### Assumptions upon Adding New Rental Agreement 🔎
- **Payment ID Already Exists**: Ensure the payment record (ID and amount) already exists for the specified tenant and amount.
- You can follow the prepared new details for new rental agreements:

| Agreement ID | Property ID | Main Tenant ID | Sub-Tenant IDs | Lease Period  | Contract Date | Renting Fee | Status |
|--------------|-------------|----------------|----------------|---------------|---------------|-------------|--------|
| RA017        | CP010       | T005           | T006           | WEEKLY        | 10/07/2024    | 3500.0      | NEW    |
| RA018        | RP014       | T014           | T015           | ANNUALLY      | 05/08/2024    | 4000.0      | NEW    |
| RA019        | CP011       | T006           | T007           | DAILY         | 15/09/2024    | 2100.0      | NEW    |
| RA020        | RP010       | T016           | T018           | FORTNIGHTLY   | 30/10/2024    | 4500.0      | NEW    |


--- 

### Acknowledgments 🙏
Special thanks to the COSC2440 Further Programming course instructors for their guidance and resources. Gratitude also to peers and contributors for feedback and suggestions to enhance this project.

---

Thank you for exploring the Rental Management System repository! 🎉
Happy coding! 🫶
