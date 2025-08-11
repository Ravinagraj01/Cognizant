import java.util.ArrayList;
import java.util.Scanner;

/**
 * Mini Project: Student Database Management System
 * Author: Ravi N
 * Description:
 * A console-based Java application to store, update, search, and display student records.
 * Demonstrates use of ArrayList, OOP, and basic CRUD operations.
 */
class Student {
    private String name;
    private String hobbies;
    private double marks; // stored as percentage

    // Constructor
    public Student(String name, String hobbies, double marks) {
        this.name = name;
        this.hobbies = hobbies;
        this.marks = marks;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getHobbies() {
        return hobbies;
    }

    public double getMarks() {
        return marks;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    // Method to calculate GPA (just a mock formula for demo)
    public double calculateGPA() {
        return (marks / 10);
    }

    // Display student details
    public void displayDetails() {
        System.out.println("Name : " + name);
        System.out.println("Hobbies: " + hobbies);
        System.out.println("Marks : " + marks + "%");
        System.out.println("GPA  : " + String.format("%.2f", calculateGPA()));
    }
}

class StudentDatabase {
    private ArrayList<Student> students;

    public StudentDatabase() {
        students = new ArrayList<>();
    }

    public void addStudent(String name, String hobbies, double marks) {
        students.add(new Student(name, hobbies, marks));
        System.out.println("Student added successfully!");
    }

    public void updateStudent(int index, String name, String hobbies, double marks) {
        if (index >= 0 && index < students.size()) {
            Student s = students.get(index);
            s.setName(name);
            s.setHobbies(hobbies);
            s.setMarks(marks);
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Invalid student index!");
        }
    }

    public void removeStudent(int index) {
        if (index >= 0 && index < students.size()) {
            students.remove(index);
            System.out.println("Student removed successfully!");
        } else {
            System.out.println("Invalid student index!");
        }
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("⚠ No students in the database.");
            return;
        }
        System.out.println("\n--- All Students ---");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("Index: " + i);
            students.get(i).displayDetails();
            System.out.println("------------------------");
        }
    }

    public void searchByName(String searchName) {
        boolean found = false;
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(searchName)) {
                s.displayDetails();
                found = true;
            }
        }
        if (!found) {
            System.out.println("⚠ No student found with the name: " + searchName);
        }
    }
}

public class MiniProjectStudentDB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDatabase db = new StudentDatabase();

        System.out.println("===== Student Database Management System =====");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Remove Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Search Student by Name");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter hobbies: ");
                    String hobbies = sc.nextLine();
                    System.out.print("Enter marks (%): ");
                    double marks = Double.parseDouble(sc.nextLine());
                    db.addStudent(name, hobbies, marks);
                    break;

                case 2:
                    System.out.print("Enter student index to update: ");
                    int uIndex = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter new name: ");
                    String uName = sc.nextLine();
                    System.out.print("Enter new hobbies: ");
                    String uHobbies = sc.nextLine();
                    System.out.print("Enter new marks (%): ");
                    double uMarks = Double.parseDouble(sc.nextLine());
                    db.updateStudent(uIndex, uName, uHobbies, uMarks);
                    break;

                case 3:
                    System.out.print("Enter student index to remove: ");
                    int rIndex = Integer.parseInt(sc.nextLine());
                    db.removeStudent(rIndex);
                    break;

                case 4:
                    db.displayAllStudents();
                    break;

                case 5:
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine();
                    db.searchByName(searchName);
                    break;

                case 6:
                    System.out.println("Exiting system... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
