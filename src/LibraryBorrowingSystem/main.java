/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      None
 */
package LibraryBorrowingSystem;

////////////////////////////////////////////////////////////////////////////
// Import modules
////////////////////////////////////////////////////////////////////////////

// Import the Scanner class from java.util so we can read keyboard input
import java.util.Scanner;

import LibraryBorrowingSystem.controller.BorrowRecord;
import LibraryBorrowingSystem.modal.ItemNotAvailableException;
import LibraryBorrowingSystem.modal.Librarian;
import LibraryBorrowingSystem.model.Books;
import LibraryBorrowingSystem.model.Fine;

/**
 * Entry point for the Library Borrowing System.
 *
 * This class starts the application by creating a Librarian object (which
 * automatically loads initial book, multimedia, and member data) and then
 * presents a two-level menu-driven interface.
 *
 * Menu structure:
 *   Level 1 (Main Menu) : Book Management, Multimedia Management,
 *                         Member Management, Borrow and Return, Exit
 *   Level 2 (Sub-menus) : specific actions for each section + Back option
 *
 * OOP concepts demonstrated:
 *   Inheritance  : Books/Multimedia extend LibraryItem, Member/Librarian extend Person
 *   Overriding   : getInfo() and toString() overridden in all subclasses
 *   Overloading  : addBook (with/without genre), addMultimedia (with/without duration),
 *                  searchItem (with/without keyword)
 *   Encapsulation: all attributes private/protected, accessed via getters/setters
 *
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      None
 */
public class main {

    /**
     * Reads one integer from the Scanner.
     * If the input is not a number, discards the line and returns -1
     * so the caller can re-show the menu without crashing.
     *
     * @param sc the Scanner connected to keyboard input
     * @return the integer the user typed, or -1 for invalid input
     */
    private static int readInt(Scanner sc) {
        // Check whether the next token the user typed is an integer
        if (sc.hasNextInt()) {
            int value = sc.nextInt();   // read the integer value
            sc.nextLine();              // consume the leftover newline after the number
            return value;
        } else {
            sc.nextLine();              // discard the entire invalid line
            System.out.println("  Please enter a number from the menu.");
            return -1;                  // signal that the input was not a valid number
        }
    }

    /**
     * Main method — launches the library system and runs the two-level menu loop.
     * The outer loop shows the main menu and runs until the user selects 0 (Exit).
     * Each main menu option opens an inner loop for that section's sub-menu.
     * Exit (0) is only available on the main menu; sub-menus use 0 as Back.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Print program metadata when the application starts
        System.out.println("============================================");
        System.out.println("  Library Borrowing System");
        System.out.println("============================================");
        System.out.println("  Author      : masjohncook");
        System.out.println("  Version     : 0.0.1");
        System.out.println("  Copyright   : (C) Copyright 2026");
        System.out.println("============================================\n");

        // Create a Scanner object to read input typed by the user on the keyboard
        Scanner sc = new Scanner(System.in);

        // Create the Librarian — the constructor automatically loads initial data
        // Librarian extends Person, so id and name are set via super()
        Librarian librarian = new Librarian("L001", "Mrs. Smith");
        System.out.println("=== Welcome, " + librarian.getName() + "! ===\n");

        // ── MIDTERM FEATURE DEMO ──────────────────────────────────────────────
        System.out.println("============================================");
        System.out.println("          MIDTERM FEATURE DEMO             ");
        System.out.println("============================================");

        // ── Feature 1: Fine System ────────────────────────────────────────────
        System.out.println("\n--- Feature 1: Fine System ---");
        Member demoBudi1 = new Member("D001", "Budi");
        Books laskarPelangi = new Books("D-A1", "Laskar Pelangi", "Andrea Hirata", "Fiction");
        // Budi borrows the book on day 1
        try {
            demoBudi1.borrowItem(laskarPelangi);
        } catch (ItemNotAvailableException | BorrowLimitExceededException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        // Create a borrow record with day-number date (required by issueFine)
        BorrowRecord fineRecord = new BorrowRecord("REC-FINE", demoBudi1, laskarPelangi, "1");
        // Return on day 12 → daysLate = 12 - (1 + 7) = 4 → Rp8000
        try {
            Fine fine = librarian.issueFine(fineRecord, 12);
            System.out.println(fine.getInfo());
        } catch (InvalidFineException e) {
            System.out.println("  Error: " + e.getMessage());
        }

        // ── Feature 2: Premium Member ─────────────────────────────────────────
        System.out.println("\n--- Feature 2: Premium Member ---");
        Member budi = new Member("D002", "Budi");
        PremiumMember sari = new PremiumMember("D003", "Sari", "PREM001");

        // Books for Budi (regular — limit 3, trying 4)
        Books[] budiBooks = {
            new Books("D-B1", "Laskar Pelangi",  "Andrea Hirata",    "Fiction"),
            new Books("D-B2", "Dilan",            "Pidi Baiq",        "Fiction"),
            new Books("D-B3", "Bumi Manusia",     "Pramoedya A.T.",   "Historical"),
            new Books("D-B4", "Negeri 5 Menara",  "Ahmad Fuadi",      "Fiction")
        };
        for (Books book : budiBooks) {
            try {
                budi.borrowItem(book);
            } catch (BorrowLimitExceededException e) {
                System.out.println("[Regular] Borrow limit reached. Maximum 3 items for regular members.");
            }
        }

        // Books for Sari (premium — limit 5, trying 6)
        Books[] sariBooks = {
            new Books("D-C1", "Filosofi Kopi",   "Dee Lestari",  "Fiction"),
            new Books("D-C2", "Pulang",           "Tere Liye",    "Fiction"),
            new Books("D-C3", "Hujan",            "Tere Liye",    "Fiction"),
            new Books("D-C4", "Perahu Kertas",    "Dee Lestari",  "Romance"),
            new Books("D-C5", "Supernova",        "Dee Lestari",  "Sci-Fi"),
            new Books("D-C6", "Ranah 3 Warna",   "Ahmad Fuadi",  "Fiction")
        };
        for (Books book : sariBooks) {
            try {
                sari.borrowItem(book);
            } catch (BorrowLimitExceededException e) {
                System.out.println("[Premium] Borrow limit reached. Maximum 5 items for premium members.");
            }
        }

        // ── Feature 3: Reservation System ────────────────────────────────────
        System.out.println("\n--- Feature 3: Reservation System ---");
        Books dilanBook = new Books("D-D1", "Dilan", "Pidi Baiq", "Fiction");
        dilanBook.setAvailable(false); // simulate that this item is already borrowed
        Member demoSari = new Member("D004", "Sari");
        librarian.addReservation(demoSari, dilanBook);
        librarian.displayAllReservations();

        // ── Feature 4: Search by Genre / Type ────────────────────────────────
        System.out.println("\n--- Feature 4: Search by Genre / Type ---");
        // searchByGenre(Books[], int, String) — searches books by genre
        librarian.searchByGenre(librarian.getCatalog(), librarian.getCatalogCount(), "Fiction");
        System.out.println();
        // searchByGenre(Multimedia[], int, String) — same method name, different parameter type
        librarian.searchByGenre(librarian.getMultimedia(), librarian.getMultimediaCount(), "DVD");

        // ── Bonus: Exception Handling ─────────────────────────────────────────
        System.out.println("\n--- Bonus: Exception Handling ---");
        // Scenario 1: borrow an unavailable item
        Books unavailable = new Books("D-E1", "Dilan", "Pidi Baiq", "Fiction");
        unavailable.setAvailable(false);
        Member testMember = new Member("D005", "Budi");
        try {
            testMember.borrowItem(unavailable);
        } catch (ItemNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Scenario 2: exceed borrow limit
        Books ex1 = new Books("D-F1", "Book A", "Author", "Fiction");
        Books ex2 = new Books("D-F2", "Book B", "Author", "Fiction");
        Books ex3 = new Books("D-F3", "Book C", "Author", "Fiction");
        Books ex4 = new Books("D-F4", "Book D", "Author", "Fiction");
        try {
            testMember.borrowItem(ex1);
            testMember.borrowItem(ex2);
            testMember.borrowItem(ex3);
            testMember.borrowItem(ex4); // triggers BorrowLimitExceededException
        } catch (BorrowLimitExceededException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n============================================");
        System.out.println("          END OF DEMO                      ");
        System.out.println("============================================\n");

        // mainChoice stores which section the user picks from the main menu
        // Start at -1 so the while loop begins immediately (since -1 != 0)
        int mainChoice = -1;

        // ── LEVEL 1: Main Menu ────────────────────────────────────────────────────
        // Keep showing the main menu until the user types 0 (Exit)
        while (mainChoice != 0) {

            System.out.println("\n============================================");
            System.out.println("          LIBRARY BORROWING SYSTEM         ");
            System.out.println("============================================");
            System.out.println("  1. Book Management");
            System.out.println("  2. Multimedia Management");
            System.out.println("  3. Member Management");
            System.out.println("  4. Borrow & Return");
            System.out.println("--------------------------------------------");
            System.out.println("  0. Exit");
            System.out.println("============================================");
            System.out.print("Enter choice: ");

            // Read the main menu choice — returns -1 if input is not a number
            mainChoice = readInt(sc);

            switch (mainChoice) {

                // ── 1. Book Management sub-menu ───────────────────────────────────
                case 1:
                    // bookChoice stores the user's pick inside the Book Management sub-menu
                    // Start at -1 so the inner loop runs at least once
                    int bookChoice = -1;

                    // Keep showing the Book Management menu until the user types 0 (Back)
                    while (bookChoice != 0) {

                        System.out.println("\n--------------------------------------------");
                        System.out.println("          BOOK MANAGEMENT                  ");
                        System.out.println("--------------------------------------------");
                        System.out.println("  1. Add Book (with Genre)");
                        System.out.println("  2. Add Book (default Genre: General)");
                        System.out.println("  3. Remove Book");
                        System.out.println("  4. Update Book");
                        System.out.println("  5. View All Books");
                        System.out.println("--------------------------------------------");
                        System.out.println("  0. Back");
                        System.out.println("--------------------------------------------");
                        System.out.print("Enter choice: ");

                        // Read the sub-menu choice
                        bookChoice = readInt(sc);

                        switch (bookChoice) {

                            // ── 1. Add Book (with Genre) — calls addBook(id, title, author, genre)
                            case 1:
                                System.out.print("  Book ID  : ");
                                String b1Id = sc.nextLine().trim();
                                System.out.print("  Title    : ");
                                String b1Title = sc.nextLine().trim();
                                System.out.print("  Author   : ");
                                String b1Author = sc.nextLine().trim();
                                System.out.print("  Genre    : ");
                                String b1Genre = sc.nextLine().trim();

                                // Validate all fields are provided
                                if (b1Id.isEmpty() || b1Title.isEmpty() || b1Author.isEmpty() || b1Genre.isEmpty()) {
                                    System.out.println("  [FAILED] All fields are required.");
                                } else {
                                    // Calls the 4-argument overload of addBook (with genre)
                                    librarian.addBook(b1Id, b1Title, b1Author, b1Genre);
                                }
                                break;

                            // ── 2. Add Book (default Genre) — calls addBook(id, title, author)
                            case 2:
                                System.out.print("  Book ID  : ");
                                String b2Id = sc.nextLine().trim();
                                System.out.print("  Title    : ");
                                String b2Title = sc.nextLine().trim();
                                System.out.print("  Author   : ");
                                String b2Author = sc.nextLine().trim();

                                if (b2Id.isEmpty() || b2Title.isEmpty() || b2Author.isEmpty()) {
                                    System.out.println("  [FAILED] All fields are required.");
                                } else {
                                    // Calls the 3-argument overload of addBook (genre defaults to "General")
                                    librarian.addBook(b2Id, b2Title, b2Author);
                                }
                                break;

                            // ── 3. Remove Book ────────────────────────────────────────────────
                            case 3:
                                librarian.displayCatalog();
                                System.out.print("  Enter Book ID to remove: ");
                                String removeBookId = sc.nextLine().trim();
                                librarian.removeBook(removeBookId);
                                break;

                            // ── 4. Update Book ────────────────────────────────────────────────
                            case 4:
                                librarian.displayCatalog();
                                System.out.print("  Enter Book ID to update: ");
                                String updateBookId = sc.nextLine().trim();
                                System.out.print("  New Title  (Enter to keep current): ");
                                String updatedTitle = sc.nextLine();
                                System.out.print("  New Author (Enter to keep current): ");
                                String updatedAuthor = sc.nextLine();
                                System.out.print("  New Genre  (Enter to keep current): ");
                                String updatedGenre = sc.nextLine();
                                librarian.updateBook(updateBookId, updatedTitle, updatedAuthor, updatedGenre);
                                break;

                            // ── 5. View All Books ─────────────────────────────────────────────
                            case 5:
                                librarian.displayCatalog();
                                break;

                            // ── 0. Back — return to main menu ─────────────────────────────────
                            case 0:
                                System.out.println("  Returning to main menu...");
                                break;

                            // ── Invalid input inside Book Management ──────────────────────────
                            default:
                                if (bookChoice != -1) {
                                    // Only print this message if the input was a number (not -1 from readInt)
                                    System.out.println("  Invalid choice. Please enter a number from the menu.");
                                }
                        }
                    }
                    break;

                // ── 2. Multimedia Management sub-menu ─────────────────────────────
                case 2:
                    // multimediaChoice stores the user's pick inside the Multimedia sub-menu
                    int multimediaChoice = -1;

                    // Keep showing the Multimedia Management menu until the user types 0 (Back)
                    while (multimediaChoice != 0) {

                        System.out.println("\n--------------------------------------------");
                        System.out.println("          MULTIMEDIA MANAGEMENT            ");
                        System.out.println("--------------------------------------------");
                        System.out.println("  1. Add Multimedia (with Duration)");
                        System.out.println("  2. Add Multimedia (default Duration: Unknown)");
                        System.out.println("  3. Remove Multimedia");
                        System.out.println("  4. Update Multimedia");
                        System.out.println("  5. View All Multimedia");
                        System.out.println("--------------------------------------------");
                        System.out.println("  0. Back");
                        System.out.println("--------------------------------------------");
                        System.out.print("Enter choice: ");

                        // Read the sub-menu choice
                        multimediaChoice = readInt(sc);

                        switch (multimediaChoice) {

                            // ── 1. Add Multimedia (with Duration) — calls addMultimedia(id, title, type, duration)
                            case 1:
                                System.out.print("  Item ID  : ");
                                String m1Id = sc.nextLine().trim();
                                System.out.print("  Title    : ");
                                String m1Title = sc.nextLine().trim();
                                System.out.print("  Type (DVD/CD/Audiobook): ");
                                String m1Type = sc.nextLine().trim();
                                System.out.print("  Duration : ");
                                String m1Duration = sc.nextLine().trim();

                                if (m1Id.isEmpty() || m1Title.isEmpty() || m1Type.isEmpty() || m1Duration.isEmpty()) {
                                    System.out.println("  [FAILED] All fields are required.");
                                } else {
                                    // Calls the 4-argument overload of addMultimedia (with duration)
                                    librarian.addMultimedia(m1Id, m1Title, m1Type, m1Duration);
                                }
                                break;

                            // ── 2. Add Multimedia (default Duration) — calls addMultimedia(id, title, type)
                            case 2:
                                System.out.print("  Item ID  : ");
                                String m2Id = sc.nextLine().trim();
                                System.out.print("  Title    : ");
                                String m2Title = sc.nextLine().trim();
                                System.out.print("  Type (DVD/CD/Audiobook): ");
                                String m2Type = sc.nextLine().trim();

                                if (m2Id.isEmpty() || m2Title.isEmpty() || m2Type.isEmpty()) {
                                    System.out.println("  [FAILED] All fields are required.");
                                } else {
                                    // Calls the 3-argument overload of addMultimedia (duration defaults to "Unknown")
                                    librarian.addMultimedia(m2Id, m2Title, m2Type);
                                }
                                break;

                            // ── 3. Remove Multimedia ──────────────────────────────────────────
                            case 3:
                                librarian.displayMultimedia();
                                System.out.print("  Enter Item ID to remove: ");
                                String removeItemId = sc.nextLine().trim();
                                librarian.removeMultimedia(removeItemId);
                                break;

                            // ── 4. Update Multimedia ──────────────────────────────────────────
                            case 4:
                                librarian.displayMultimedia();
                                System.out.print("  Enter Item ID to update: ");
                                String updateItemId = sc.nextLine().trim();
                                System.out.print("  New Title    (Enter to keep current): ");
                                String updatedMTitle = sc.nextLine();
                                System.out.print("  New Type     (Enter to keep current): ");
                                String updatedMType = sc.nextLine();
                                System.out.print("  New Duration (Enter to keep current): ");
                                String updatedMDuration = sc.nextLine();
                                librarian.updateMultimedia(updateItemId, updatedMTitle, updatedMType, updatedMDuration);
                                break;

                            // ── 5. View All Multimedia ────────────────────────────────────────
                            case 5:
                                librarian.displayMultimedia();
                                break;

                            // ── 0. Back — return to main menu ─────────────────────────────────
                            case 0:
                                System.out.println("  Returning to main menu...");
                                break;

                            // ── Invalid input inside Multimedia Management ────────────────────
                            default:
                                if (multimediaChoice != -1) {
                                    System.out.println("  Invalid choice. Please enter a number from the menu.");
                                }
                        }
                    }
                    break;

                // ── 3. Member Management sub-menu ─────────────────────────────────
                case 3:
                    // memberChoice stores the user's pick inside the Member Management sub-menu
                    int memberChoice = -1;

                    // Keep showing the Member Management menu until the user types 0 (Back)
                    while (memberChoice != 0) {

                        System.out.println("\n--------------------------------------------");
                        System.out.println("          MEMBER MANAGEMENT                ");
                        System.out.println("--------------------------------------------");
                        System.out.println("  1. Register Member");
                        System.out.println("  2. Remove Member");
                        System.out.println("  3. Update Member Name");
                        System.out.println("  4. View All Members");
                        System.out.println("--------------------------------------------");
                        System.out.println("  0. Back");
                        System.out.println("--------------------------------------------");
                        System.out.print("Enter choice: ");

                        // Read the sub-menu choice
                        memberChoice = readInt(sc);

                        switch (memberChoice) {

                            // ── 1. Register Member ────────────────────────────────────────────
                            case 1:
                                System.out.print("  Member ID : ");
                                String newMemberId = sc.nextLine().trim();
                                System.out.print("  Name      : ");
                                String newMemberName = sc.nextLine().trim();

                                if (newMemberId.isEmpty() || newMemberName.isEmpty()) {
                                    System.out.println("  [FAILED] All fields are required.");
                                } else {
                                    librarian.registerMember(newMemberId, newMemberName);
                                }
                                break;

                            // ── 2. Remove Member ──────────────────────────────────────────────
                            case 2:
                                librarian.displayMembers();
                                System.out.print("  Enter Member ID to remove: ");
                                String removeMemberId = sc.nextLine().trim();
                                librarian.removeMember(removeMemberId);
                                break;

                            // ── 3. Update Member Name ─────────────────────────────────────────
                            case 3:
                                librarian.displayMembers();
                                System.out.print("  Enter Member ID to update: ");
                                String updateMemberId = sc.nextLine().trim();
                                System.out.print("  New Name  : ");
                                String updatedName = sc.nextLine().trim();
                                librarian.updateMember(updateMemberId, updatedName);
                                break;

                            // ── 4. View All Members ───────────────────────────────────────────
                            case 4:
                                librarian.displayMembers();
                                break;

                            // ── 0. Back — return to main menu ─────────────────────────────────
                            case 0:
                                System.out.println("  Returning to main menu...");
                                break;

                            // ── Invalid input inside Member Management ────────────────────────
                            default:
                                if (memberChoice != -1) {
                                    System.out.println("  Invalid choice. Please enter a number from the menu.");
                                }
                        }
                    }
                    break;

                // ── 4. Borrow & Return sub-menu ───────────────────────────────────
                case 4:
                    // borrowChoice stores the user's pick inside the Borrow & Return sub-menu
                    int borrowChoice = -1;

                    // Keep showing the Borrow & Return menu until the user types 0 (Back)
                    while (borrowChoice != 0) {

                        System.out.println("\n--------------------------------------------");
                        System.out.println("          BORROW & RETURN                  ");
                        System.out.println("--------------------------------------------");
                        System.out.println("  1. Borrow Item");
                        System.out.println("  2. Return Item");
                        System.out.println("  3. Search Item by Keyword");
                        System.out.println("  4. Browse All Available Items");
                        System.out.println("  5. View All Borrow Records");
                        System.out.println("--------------------------------------------");
                        System.out.println("  0. Back");
                        System.out.println("--------------------------------------------");
                        System.out.print("Enter choice: ");

                        // Read the sub-menu choice
                        borrowChoice = readInt(sc);

                        switch (borrowChoice) {

                            // ── 1. Borrow Item ────────────────────────────────────────────────
                            case 1:
                                librarian.displayMembers();
                                System.out.print("  Enter Member ID : ");
                                String borrowMemberId = sc.nextLine().trim();

                                // Search for the member by their ID
                                Member borrower = librarian.findMemberById(borrowMemberId);

                                if (borrower == null) {
                                    System.out.println("  [FAILED] Member not found.");
                                } else {
                                    // Show both books and multimedia so the member can pick any item
                                    librarian.displayAllItems();
                                    System.out.print("  Enter Item ID   : ");
                                    String borrowItemId = sc.nextLine().trim();

                                    // findItemById searches both books and multimedia catalogs
                                    LibraryItem itemToBorrow = librarian.findItemById(borrowItemId);

                                    if (itemToBorrow == null) {
                                        System.out.println("  [FAILED] Item not found.");
                                    } else {
                                        // borrowItem() throws if item unavailable or limit exceeded
                                        try {
                                            if (borrower.borrowItem(itemToBorrow)) {
                                                librarian.recordBorrow(borrower, itemToBorrow);
                                            }
                                        } catch (ItemNotAvailableException | BorrowLimitExceededException e) {
                                            System.out.println("  Error: " + e.getMessage());
                                        }
                                    }
                                }
                                break;

                            // ── 2. Return Item ────────────────────────────────────────────────
                            case 2:
                                librarian.displayMembers();
                                System.out.print("  Enter Member ID : ");
                                String returnMemberId = sc.nextLine().trim();
                                Member returner = librarian.findMemberById(returnMemberId);

                                if (returner == null) {
                                    System.out.println("  [FAILED] Member not found.");
                                } else if (returner.getBorrowCount() == 0) {
                                    // Member exists but has nothing to return
                                    System.out.println("  " + returner.getName() + " has no borrowed items.");
                                } else {
                                    // Show the items this member currently has
                                    System.out.println("  Items currently borrowed by " + returner.getName() + ":");
                                    LibraryItem[] borrowed = returner.getBorrowedItems();
                                    for (int i = 0; i < returner.getBorrowCount(); i++) {
                                        System.out.println("    " + borrowed[i]);
                                    }
                                    System.out.print("  Enter Item ID to return: ");
                                    String returnItemId = sc.nextLine().trim();

                                    // findItemById searches both catalogs
                                    LibraryItem itemToReturn = librarian.findItemById(returnItemId);

                                    if (itemToReturn == null) {
                                        System.out.println("  [FAILED] Item not found.");
                                    } else {
                                        // returnItem() is in Member — works for any LibraryItem
                                        if (returner.returnItem(itemToReturn)) {
                                            // Only update the record if the return was successful
                                            librarian.recordReturn(returner, itemToReturn);
                                        }
                                    }
                                }
                                break;

                            // ── 3. Search Item by Keyword — calls searchItem(catalog, size, keyword)
                            case 3:
                                librarian.displayMembers();
                                System.out.print("  Enter Member ID : ");
                                String searchMemberId = sc.nextLine().trim();
                                Member searcher = librarian.findMemberById(searchMemberId);

                                if (searcher == null) {
                                    System.out.println("  [FAILED] Member not found.");
                                } else {
                                    System.out.print("  Enter keyword   : ");
                                    String keyword = sc.nextLine().trim();

                                    // Calls the 3-argument overload of searchItem (WITH keyword)
                                    // getAllItems() returns a combined array of books + multimedia
                                    searcher.searchItem(librarian.getAllItems(), librarian.getAllItemsCount(), keyword);
                                }
                                break;

                            // ── 4. Browse All Available Items — calls searchItem(catalog, size)
                            case 4:
                                librarian.displayMembers();
                                System.out.print("  Enter Member ID : ");
                                String browseMemberId = sc.nextLine().trim();
                                Member browser = librarian.findMemberById(browseMemberId);

                                if (browser == null) {
                                    System.out.println("  [FAILED] Member not found.");
                                } else {
                                    // Calls the 2-argument overload of searchItem (WITHOUT keyword)
                                    // Lists all currently available items instead of filtering by title
                                    browser.searchItem(librarian.getAllItems(), librarian.getAllItemsCount());
                                }
                                break;

                            // ── 5. View All Borrow Records ────────────────────────────────────
                            case 5:
                                librarian.displayAllRecords();
                                break;

                            // ── 0. Back — return to main menu ─────────────────────────────────
                            case 0:
                                System.out.println("  Returning to main menu...");
                                break;

                            // ── Invalid input inside Borrow & Return ──────────────────────────
                            default:
                                if (borrowChoice != -1) {
                                    System.out.println("  Invalid choice. Please enter a number from the menu.");
                                }
                        }
                    }
                    break;

                // ── 0. Exit — only available on the main menu ─────────────────────
                case 0:
                    System.out.println("\n  Goodbye! Library system closed.");
                    break;

                // ── Invalid input on main menu ────────────────────────────────────
                default:
                    if (mainChoice != -1) {
                        // Only print this message if the input was a number (not -1 from readInt)
                        System.out.println("  Invalid choice. Please enter a number from the menu.");
                    }
            }
        }

        // Close the Scanner to release the keyboard input resource
        sc.close();
    }
}
