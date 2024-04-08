import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class Employee implements Serializable{
    private String name;
    private int id;
    private String department;
    private boolean codeOfConductCompleted;

    public Employee(String name, int id, String department) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.codeOfConductCompleted = false; // Initially set to false
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isCodeOfConductCompleted() {
        return codeOfConductCompleted;
    }

    public void setCodeOfConductCompleted(boolean codeOfConductCompleted) {
        this.codeOfConductCompleted = codeOfConductCompleted;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", department='" + department + '\'' +
                ", codeOfConductCompleted=" + codeOfConductCompleted +
                '}';
    }
}

class EmployeeManagementSystem {
    private ArrayList<Employee> employees = new ArrayList<>();
    private int nextId = 1;

    // Method to save employee data to a file
    public void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("employees.dat"))) {
            oos.writeObject(employees);
            System.out.println("Employee data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }

    // Method to load employee data from a file
    public void loadDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("employees.dat"))) {
            employees = (ArrayList<Employee>) ois.readObject();
            System.out.println("Employee data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading employee data: " + e.getMessage());
        }
    }

    public void addEmployee(String name, Date dob, String department) {
        // Calculate age based on DOB
        Date currentDate = new Date();
        long diff = currentDate.getTime() - dob.getTime();
        long ageInMillis = diff / (1000L * 60 * 60 * 24 * 365);
        int age = (int) ageInMillis;

        // Check if employee is above 21 years old
        if (age < 21) {
            System.out.println("Employee must be at least 21 years old.");
            return;
        }

        Employee employee = new Employee(name, nextId++, department);
        employees.add(employee);
        System.out.println("Employee added successfully.");
    }

    public void editEmployee(int id, String department) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                employee.setDepartment(department);
                System.out.println("Employee department updated successfully.");
                return;
            }
        }
        System.out.println("Employee not found with given ID.");
    }

    public void deleteEmployee(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                employees.remove(employee);
                System.out.println("Employee deleted successfully.");
                return;
            }
        }
        System.out.println("Employee not found with given ID.");
    }

    public void viewEmployees() {
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public void markCodeOfConductCompleted(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                employee.setCodeOfConductCompleted(true);
                System.out.println("Code of Conduct training marked as completed for employee.");
                return;
            }
        }
        System.out.println("Employee not found with given ID.");
    }
}

public class emsadvdb {
    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "Admin123";
    private static final String USER_PASSWORD = "User123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeManagementSystem empSystem = new EmployeeManagementSystem();

        // Load employee data from file
        empSystem.loadDataFromFile();

        while (true) {
            System.out.println("\nEmployee Management System");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    adminLogin(empSystem, scanner);
                    break;
                case 2:
                    userLogin(empSystem, scanner);
                    break;
                case 3:
                    // Save employee data to file before exiting
                    empSystem.saveDataToFile();
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminLogin(EmployeeManagementSystem empSystem, Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            adminMenu(empSystem, scanner);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void adminMenu(EmployeeManagementSystem empSystem, Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add Employee");
            System.out.println("2. Edit Employee Department");
            System.out.println("3. Delete Employee");
            System.out.println("4. Mark Code of Conduct Completed");
            System.out.println("5. View Employees");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter employee name: ");
                    String name = scanner.nextLine();
		    System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                    String dobStr = scanner.nextLine();
                    Date dob = parseDate(dobStr);
                    if (dob == null) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        return;
                    }
                    System.out.print("Enter department: ");
                    String department = scanner.nextLine();
                    empSystem.addEmployee(name, dob, department);
                    break;
                case 2:
                    System.out.print("Enter employee ID to edit department: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter new department: ");
                    department = scanner.nextLine();
                    empSystem.editEmployee(id, department);
                    break;
                case 3:
                    System.out.print("Enter employee ID to delete: ");
                    id = scanner.nextInt();
                    empSystem.deleteEmployee(id);
                    break;
                case 4:
                    System.out.print("Enter employee ID to mark Code of Conduct completed: ");
                    id = scanner.nextInt();
                    empSystem.markCodeOfConductCompleted(id);
                    break;
                case 5:
                    empSystem.viewEmployees();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminAddEmployee(EmployeeManagementSystem empSystem, Scanner scanner) {
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine();
        Date dob = parseDate(dobStr);
        if (dob == null) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }
        System.out.print("Enter department: ");
        String department = scanner.nextLine();
        empSystem.addEmployee(name, dob, department);
    }

    private static Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private static void userLogin(EmployeeManagementSystem empSystem, Scanner scanner) {
        System.out.print("Enter user ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        userMenu(empSystem, scanner, id);
    }

    private static void userMenu(EmployeeManagementSystem empSystem, Scanner scanner, int id) {
        while (true) {
            System.out.println("\nUser Menu");
            System.out.println("1. View Employees");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    empSystem.viewEmployees();
                    break;
                case 2:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
