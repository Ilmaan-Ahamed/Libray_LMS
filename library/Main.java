package Libray_LMS.library;

import Libray_LMS.model.User;               // Represnts a user of the libaray
import Libray_LMS.service.LibraryService;   // Service class to manage libarary operations

import java.util.Scanner;       // Scanner for user input

public class Main {
    private static Scanner scanner = new Scanner(System.in);            // Scanner for reading user input
    private static LibraryService libraray = new LibraryService();      // Library service instance to manage books & User
    private static User currentUSer = null;                             // Current user of the library

    public static void main(String[] args) {
        showMainMenu();
    }

    private static void showMainMenu(){
        while (true) {
            System.out.println("\n==== Library Managment System ===");
            System.out.println("1. Admin Funtions");
            System.out.println("2. User Functions");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();      // consume newline

            // Handel user choise for the main menu
            switch (choice) {
                case 1:
                    showAdminMenu();
                    break;
                case 2:
                    showUserMenu();
                    break;
                case 3:
                    System.out.println("Exiting System...");
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }











}

