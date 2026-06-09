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

/**
 * Represents the librarian who manages the library system.
 * Inherits from Person — gains id and name.
 *
 * The Librarian is responsible for managing the book catalog,
 * the multimedia catalog, and the list of registered members.
 * It also keeps a full record of all borrow and return transactions.
 * On creation, initial data is automatically loaded from each class.
 *
 * Inheritance:
 *   Librarian extends Person
 *   - Inherits : id, name (and their getters/setters)
 *   - Adds     : catalog[], multimedia[], members[], borrowRecords[]
 *   - Overrides: getInfo(), toString()
 *
 * Overloading:
 *   addBook(id, title, author, genre) — full version
 *   addBook(id, title, author)        — genre defaults to "General"
 *   addMultimedia(id, title, type, duration) — full version
 *   addMultimedia(id, title, type)           — duration defaults to "Unknown"
 *
 * Associations:
 *   - catalog[]      : array of all Books in the library
 *   - multimedia[]   : array of all Multimedia items in the library
 *   - members[]      : array of all registered Members
 *   - borrowRecords[]: array of all BorrowRecord transactions
 */
public class Librarian extends Person {

    // catalog stores all Books objects in the library
    // Association: Librarian -> Books[]
    private Books[] catalog;

    // catalogCount tracks how many books are in the catalog array
    private int catalogCount;

    // multimedia stores all Multimedia objects in the library
    // Association: Librarian -> Multimedia[]
    private Multimedia[] multimedia;

    // multimediaCount tracks how many multimedia items are in the array
    private int multimediaCount;

    // members stores all registered Member objects
    // Association: Librarian -> Member[]
    private Member[] members;

    // memberCount tracks how many members are registered
    private int memberCount;

    // borrowRecords stores every borrow/return transaction
    private BorrowRecord[] borrowRecords;

    // recordCount tracks how many borrow records have been created
    private int recordCount;

    // reservations stores all active and cancelled reservations (Feature 3)
    private Reservation[] reservations;

    // reservationCount tracks how many reservations have been created
    private int reservationCount;

    // fineCount is used to generate unique fine IDs (Feature 1)
    private int fineCount;

    // Maximum sizes for each array
    private static final int MAX_BOOKS         = 100;
    private static final int MAX_MULTIMEDIA    = 50;
    private static final int MAX_MEMBERS       = 50;
    private static final int MAX_RECORDS       = 200;
    private static final int MAX_RESERVATIONS  = 100;

    /**
     * Creates a new Librarian and automatically loads initial data
     * from the Books, Multimedia, and Member classes.
     * Calls the Person constructor (super) to set id and name.
     *
     * @param librarianId unique identifier for the librarian (e.g. "L001")
     * @param name        full name of the librarian
     */
    public Librarian(String librarianId, String name) {
        // Call the parent Person constructor to set this.id and this.name
        super(librarianId, name);

        // Initialize the books catalog array with MAX_BOOKS empty slots
        this.catalog = new Books[MAX_BOOKS];
        this.catalogCount = 0;

        // Initialize the multimedia array with MAX_MULTIMEDIA empty slots
        this.multimedia = new Multimedia[MAX_MULTIMEDIA];
        this.multimediaCount = 0;

        // Initialize the members array with MAX_MEMBERS empty slots
        this.members = new Member[MAX_MEMBERS];
        this.memberCount = 0;

        // Initialize the borrow records array with MAX_RECORDS empty slots
        this.borrowRecords = new BorrowRecord[MAX_RECORDS];
        this.recordCount = 0;

        // Initialize the reservations array (Feature 3)
        this.reservations = new Reservation[MAX_RESERVATIONS];
        this.reservationCount = 0;

        // Fine ID counter starts at 0 (Feature 1)
        this.fineCount = 0;

        // Load all initial data from each class
        loadInitialData();
    }

    /**
     * Loads the starting data from each class.
     * Books, Multimedia, and Member each own their default data.
     */
    private void loadInitialData() {
        // Ask the Books class for its pre-defined list of starting books
        Books[] initialBooks = Books.getInitialBooks();
        for (int i = 0; i < initialBooks.length; i++) {
            catalog[catalogCount++] = initialBooks[i];
        }

        // Ask the Multimedia class for its pre-defined list of starting items
        Multimedia[] initialMultimedia = Multimedia.getInitialMultimedia();
        for (int i = 0; i < initialMultimedia.length; i++) {
            multimedia[multimediaCount++] = initialMultimedia[i];
        }

        // Ask the Member class for its pre-defined list of starting members
        Member[] initialMembers = Member.getInitialMembers();
        for (int i = 0; i < initialMembers.length; i++) {
            members[memberCount++] = initialMembers[i];
        }
    }

    // ── Book CRUD ─────────────────────────────────────────────────────────────

    /**
     * OVERLOADED METHOD (with genre) —
     * Adds a new book to the library catalog with all details provided.
     * Fails if the catalog is full or the book ID already exists.
     * This is the FULL version of addBook — all 4 fields are required.
     *
     * @param bookId unique ID for the new book
     * @param title  title of the new book
     * @param author author of the new book
     * @param genre  genre/category of the new book
     * @return the created Books object, or null if the operation failed
     */
    public Books addBook(String bookId, String title, String author, String genre) {
        // Check if the catalog has reached its maximum capacity
        if (catalogCount >= MAX_BOOKS) {
            System.out.println("  [FAILED] Catalog is full.");
            return null;
        }

        // Check if a book with the same ID already exists
        if (findBookById(bookId) != null) {
            System.out.println("  [FAILED] Book ID \"" + bookId + "\" already exists.");
            return null;
        }

        // Create the new Books object with all 4 details
        Books book = new Books(bookId, title, author, genre);

        // Add the book to the next open slot and increase the count
        catalog[catalogCount++] = book;
        System.out.println("  [ADDED] " + book);
        return book;
    }

    /**
     * OVERLOADED METHOD (without genre) —
     * Adds a new book using only 3 fields; genre defaults to "General".
     * This is the SHORT version of addBook — only ID, title, and author are needed.
     * Internally it calls the full 4-argument version with "General" as the genre.
     *
     * This method has the SAME name as the one above but FEWER parameters.
     * Java chooses which version to call based on how many arguments are passed.
     * This is called METHOD OVERLOADING.
     *
     * @param bookId unique ID for the new book
     * @param title  title of the new book
     * @param author author of the new book
     * @return the created Books object, or null if the operation failed
     */
    public Books addBook(String bookId, String title, String author) {
        // Call the full 4-argument version with a default genre of "General"
        return addBook(bookId, title, author, "General");
    }

    /**
     * Removes a book from the catalog by its ID.
     * Fails if the book is not found or is currently borrowed.
     *
     * @param bookId the ID of the book to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeBook(String bookId) {
        // Loop through the catalog to find the book with the matching ID
        for (int i = 0; i < catalogCount; i++) {
            if (catalog[i].getBookId().equals(bookId)) {

                // Prevent removing a book that is currently borrowed
                if (!catalog[i].isAvailable()) {
                    System.out.println("  [FAILED] Cannot remove a book that is currently borrowed.");
                    return false;
                }

                // Shift all entries after index i one position to the left to close the gap
                for (int j = i; j < catalogCount - 1; j++) {
                    catalog[j] = catalog[j + 1];
                }

                // Decrease the count and clear the last slot
                catalog[--catalogCount] = null;
                System.out.println("  [REMOVED] Book ID \"" + bookId + "\" removed from catalog.");
                return true;
            }
        }
        System.out.println("  [FAILED] Book ID \"" + bookId + "\" not found.");
        return false;
    }

    /**
     * Updates the title, author, and/or genre of an existing book.
     * Fields left blank (empty string) are not changed.
     *
     * @param bookId    the ID of the book to update
     * @param newTitle  new title (leave empty to keep current)
     * @param newAuthor new author (leave empty to keep current)
     * @param newGenre  new genre (leave empty to keep current)
     * @return true if updated successfully, false otherwise
     */
    public boolean updateBook(String bookId, String newTitle, String newAuthor, String newGenre) {
        // Search for the book by its ID
        Books book = findBookById(bookId);
        if (book == null) {
            System.out.println("  [FAILED] Book ID \"" + bookId + "\" not found.");
            return false;
        }

        // Only update each field if the user provided a non-blank value
        if (!newTitle.trim().isEmpty())  book.setTitle(newTitle.trim());
        if (!newAuthor.trim().isEmpty()) book.setAuthor(newAuthor.trim());
        if (!newGenre.trim().isEmpty())  book.setGenre(newGenre.trim());

        System.out.println("  [UPDATED] " + book);
        return true;
    }

    /**
     * Searches the books catalog for a book by its ID.
     *
     * @param bookId the book ID to look for
     * @return the matching Books object, or null if not found
     */
    public Books findBookById(String bookId) {
        for (int i = 0; i < catalogCount; i++) {
            if (catalog[i].getBookId().equals(bookId)) {
                return catalog[i]; // Found — return immediately
            }
        }
        return null; // Not found
    }

    // ── Multimedia CRUD ───────────────────────────────────────────────────────

    /**
     * OVERLOADED METHOD (with duration) —
     * Adds a new multimedia item with all details provided.
     * This is the FULL version of addMultimedia — all 4 fields are required.
     *
     * @param itemId   unique ID for the multimedia item (e.g. "MM004")
     * @param title    title of the multimedia item
     * @param type     format type (e.g. "DVD", "CD", "Audiobook")
     * @param duration running time or length (e.g. "120 min")
     * @return the created Multimedia object, or null if the operation failed
     */
    public Multimedia addMultimedia(String itemId, String title, String type, String duration) {
        // Check if the multimedia array has reached its maximum capacity
        if (multimediaCount >= MAX_MULTIMEDIA) {
            System.out.println("  [FAILED] Multimedia catalog is full.");
            return null;
        }

        // Check if a multimedia item with the same ID already exists
        if (findMultimediaById(itemId) != null) {
            System.out.println("  [FAILED] Item ID \"" + itemId + "\" already exists.");
            return null;
        }

        // Create the new Multimedia object with all 4 details
        Multimedia item = new Multimedia(itemId, title, type, duration);

        // Add it to the next open slot and increase the count
        multimedia[multimediaCount++] = item;
        System.out.println("  [ADDED] " + item);
        return item;
    }

    /**
     * OVERLOADED METHOD (without duration) —
     * Adds a new multimedia item using only 3 fields; duration defaults to "Unknown".
     * Internally calls the full 4-argument version with "Unknown" as the duration.
     *
     * This method has the SAME name as the one above but FEWER parameters.
     * This is METHOD OVERLOADING — Java picks the right version based on argument count.
     *
     * @param itemId unique ID for the multimedia item
     * @param title  title of the multimedia item
     * @param type   format type (e.g. "DVD", "CD", "Audiobook")
     * @return the created Multimedia object, or null if the operation failed
     */
    public Multimedia addMultimedia(String itemId, String title, String type) {
        // Call the full 4-argument version with a default duration of "Unknown"
        return addMultimedia(itemId, title, type, "Unknown");
    }

    /**
     * Removes a multimedia item by its ID.
     * Fails if the item is not found or is currently borrowed.
     *
     * @param itemId the ID of the multimedia item to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeMultimedia(String itemId) {
        // Loop through the multimedia array to find the item with the matching ID
        for (int i = 0; i < multimediaCount; i++) {
            if (multimedia[i].getItemId().equals(itemId)) {

                // Prevent removing an item that is currently borrowed
                if (!multimedia[i].isAvailable()) {
                    System.out.println("  [FAILED] Cannot remove an item that is currently borrowed.");
                    return false;
                }

                // Shift all entries after index i one position to the left to close the gap
                for (int j = i; j < multimediaCount - 1; j++) {
                    multimedia[j] = multimedia[j + 1];
                }

                // Decrease the count and clear the last slot
                multimedia[--multimediaCount] = null;
                System.out.println("  [REMOVED] Item ID \"" + itemId + "\" removed from multimedia catalog.");
                return true;
            }
        }
        System.out.println("  [FAILED] Item ID \"" + itemId + "\" not found.");
        return false;
    }

    /**
     * Updates the title, type, and/or duration of an existing multimedia item.
     * Fields left blank (empty string) are not changed.
     *
     * @param itemId      the ID of the item to update
     * @param newTitle    new title (leave empty to keep current)
     * @param newType     new type (leave empty to keep current)
     * @param newDuration new duration (leave empty to keep current)
     * @return true if updated successfully, false otherwise
     */
    public boolean updateMultimedia(String itemId, String newTitle, String newType, String newDuration) {
        // Search for the multimedia item by its ID
        Multimedia item = findMultimediaById(itemId);
        if (item == null) {
            System.out.println("  [FAILED] Item ID \"" + itemId + "\" not found.");
            return false;
        }

        // Only update each field if the user provided a non-blank value
        if (!newTitle.trim().isEmpty())    item.setTitle(newTitle.trim());
        if (!newType.trim().isEmpty())     item.setType(newType.trim());
        if (!newDuration.trim().isEmpty()) item.setDuration(newDuration.trim());

        System.out.println("  [UPDATED] " + item);
        return true;
    }

    /**
     * Searches the multimedia array for an item by its ID.
     *
     * @param itemId the item ID to look for
     * @return the matching Multimedia object, or null if not found
     */
    public Multimedia findMultimediaById(String itemId) {
        for (int i = 0; i < multimediaCount; i++) {
            if (multimedia[i].getItemId().equals(itemId)) {
                return multimedia[i]; // Found — return immediately
            }
        }
        return null; // Not found
    }

    /**
     * Searches both the books catalog and the multimedia array for an item by ID.
     * Returns a LibraryItem so it works for both Books and Multimedia.
     *
     * @param itemId the ID to search for in both catalogs
     * @return the matching LibraryItem (Books or Multimedia), or null if not found
     */
    public LibraryItem findItemById(String itemId) {
        // Check the books catalog first
        Books book = findBookById(itemId);
        if (book != null) {
            return book; // Found in books catalog — return it
        }

        // Check the multimedia catalog next
        Multimedia item = findMultimediaById(itemId);
        if (item != null) {
            return item; // Found in multimedia catalog — return it
        }

        // Not found in either catalog
        return null;
    }

    // ── Member CRUD ───────────────────────────────────────────────────────────

    /**
     * Registers a new member into the system.
     * Fails if the member limit is reached or the member ID already exists.
     *
     * @param memberId unique ID for the new member
     * @param name     full name of the new member
     * @return the created Member object, or null if the operation failed
     */
    public Member registerMember(String memberId, String name) {
        if (memberCount >= MAX_MEMBERS) {
            System.out.println("  [FAILED] Member limit reached.");
            return null;
        }
        if (findMemberById(memberId) != null) {
            System.out.println("  [FAILED] Member ID \"" + memberId + "\" already exists.");
            return null;
        }
        Member member = new Member(memberId, name);
        members[memberCount++] = member;
        System.out.println("  [REGISTERED] " + member);
        return member;
    }

    /**
     * Removes a member from the system by their ID.
     * Fails if the member still has borrowed items or is not found.
     *
     * @param memberId the ID of the member to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeMember(String memberId) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getMemberId().equals(memberId)) {

                // Do not remove a member who still has items checked out
                if (members[i].getBorrowCount() > 0) {
                    System.out.println("  [FAILED] Cannot remove a member who still has borrowed items.");
                    return false;
                }

                // Shift entries left to close the gap
                for (int j = i; j < memberCount - 1; j++) {
                    members[j] = members[j + 1];
                }
                members[--memberCount] = null;
                System.out.println("  [REMOVED] Member ID \"" + memberId + "\" removed.");
                return true;
            }
        }
        System.out.println("  [FAILED] Member ID \"" + memberId + "\" not found.");
        return false;
    }

    /**
     * Updates the name of an existing member.
     * Fails if the member is not found or the new name is blank.
     *
     * @param memberId the ID of the member to update
     * @param newName  the new name to assign
     * @return true if updated successfully, false otherwise
     */
    public boolean updateMember(String memberId, String newName) {
        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.println("  [FAILED] Member ID \"" + memberId + "\" not found.");
            return false;
        }
        if (!newName.trim().isEmpty()) {
            member.setName(newName.trim());
            System.out.println("  [UPDATED] " + member);
            return true;
        }
        System.out.println("  [FAILED] New name cannot be empty.");
        return false;
    }

    /**
     * Searches the members array for a member by their ID.
     *
     * @param memberId the member ID to look for
     * @return the matching Member object, or null if not found
     */
    public Member findMemberById(String memberId) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getMemberId().equals(memberId)) {
                return members[i]; // Found — return immediately
            }
        }
        return null; // Not found
    }

    // ── Borrow Record Management ──────────────────────────────────────────────

    /**
     * Creates and stores a new borrow record.
     * Accepts any LibraryItem (Books or Multimedia).
     *
     * @param member the member who is borrowing
     * @param item   the LibraryItem being borrowed
     */
    public void recordBorrow(Member member, LibraryItem item) {
        if (recordCount >= MAX_RECORDS) return;
        // Generate a record ID padded with leading zeros e.g. "REC001"
        String recordId = "REC" + String.format("%03d", recordCount + 1);
        borrowRecords[recordCount++] = new BorrowRecord(recordId, member, item, "2026-04-29");
    }

    /**
     * Marks the open borrow record as returned.
     * Searches for the first open record matching the member and item.
     *
     * @param member the member who is returning the item
     * @param item   the LibraryItem being returned
     */
    public void recordReturn(Member member, LibraryItem item) {
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord r = borrowRecords[i];

            // Find a record that: is not returned, matches the member ID, and matches the item ID
            if (!r.isReturned()
                    && r.getMember().getMemberId().equals(member.getMemberId())
                    && r.getItem().getItemId().equals(item.getItemId())) {
                r.setReturnDate("2026-04-29");
                r.setReturned(true);
                return; // Stop after updating the first matching open record
            }
        }
    }

    // ── Display Helpers ───────────────────────────────────────────────────────

    /** Prints all books in the catalog with their availability status. */
    public void displayCatalog() {
        System.out.println("  ---- Book Catalog (" + catalogCount + " book(s)) ----");
        if (catalogCount == 0) {
            System.out.println("  (empty)");
        } else {
            for (int i = 0; i < catalogCount; i++) {
                System.out.println("  " + catalog[i]);
            }
        }
    }

    /** Prints all multimedia items with their availability status. */
    public void displayMultimedia() {
        System.out.println("  ---- Multimedia Catalog (" + multimediaCount + " item(s)) ----");
        if (multimediaCount == 0) {
            System.out.println("  (empty)");
        } else {
            for (int i = 0; i < multimediaCount; i++) {
                System.out.println("  " + multimedia[i]);
            }
        }
    }

    /** Prints both books and multimedia catalogs together. */
    public void displayAllItems() {
        displayCatalog();
        displayMultimedia();
    }

    /** Prints all registered members and their borrow counts. */
    public void displayMembers() {
        System.out.println("  ---- Registered Members (" + memberCount + " member(s)) ----");
        if (memberCount == 0) {
            System.out.println("  (empty)");
        } else {
            for (int i = 0; i < memberCount; i++) {
                System.out.println("  " + members[i]);
            }
        }
    }

    /** Prints all borrow records including returned ones. */
    public void displayAllRecords() {
        System.out.println("  ---- Borrow Records (" + recordCount + " total) ----");
        if (recordCount == 0) {
            System.out.println("  (none yet)");
        } else {
            for (int i = 0; i < recordCount; i++) {
                System.out.println("  " + borrowRecords[i]);
            }
        }
    }

    /**
     * Returns a combined array of all LibraryItems (books + multimedia).
     * Used by Member.searchItem() which needs a single array to search through.
     *
     * @return a LibraryItem array containing all books followed by all multimedia items
     */
    public LibraryItem[] getAllItems() {
        // Create a new array big enough to hold all books and all multimedia
        LibraryItem[] all = new LibraryItem[catalogCount + multimediaCount];

        // Copy all books into the first part of the combined array
        for (int i = 0; i < catalogCount; i++) {
            all[i] = catalog[i];
        }

        // Copy all multimedia into the second part of the combined array
        for (int i = 0; i < multimediaCount; i++) {
            all[catalogCount + i] = multimedia[i];
        }

        return all;
    }

    /** Returns the total number of items (books + multimedia) in the library. */
    public int getAllItemsCount() {
        return catalogCount + multimediaCount;
    }

    // ── Feature 1: Fine System ────────────────────────────────────────────────

    /**
     * Issues a fine for a late return based on a BorrowRecord.
     * The borrow date stored in the record must be a numeric day number (e.g., "1", "8").
     * Due date is borrowDay + 7. daysLate = returnDay - dueDay.
     * Throws InvalidFineException if the item was not actually late (daysLate <= 0).
     *
     * @param record    the BorrowRecord for the item being returned
     * @param returnDay the day number on which the item was returned
     * @return a Fine object with the calculated amount
     */
    public Fine issueFine(BorrowRecord record, int returnDay) {
        int borrowDay = Integer.parseInt(record.getBorrowDate());
        int dueDay    = borrowDay + 7;
        int daysLate  = returnDay - dueDay;

        if (daysLate <= 0) {
            throw new InvalidFineException(
                    "No fine: item was returned on time (daysLate = " + daysLate + ").");
        }

        String fineId = "FINE" + String.format("%03d", ++fineCount);
        return new Fine(fineId, record.getMember(), record.getItem(), daysLate);
    }

    // ── Feature 3: Reservation System ────────────────────────────────────────

    /**
     * Creates a reservation for a member who wants an unavailable item.
     * Returns null if the reservation array is full.
     *
     * @param member the member placing the reservation
     * @param item   the LibraryItem to reserve
     * @return the created Reservation object, or null if the array is full
     */
    public Reservation addReservation(Member member, LibraryItem item) {
        if (reservationCount >= MAX_RESERVATIONS) {
            System.out.println("  [FAILED] Reservation limit reached.");
            return null;
        }
        String resId   = "RES" + String.format("%03d", reservationCount + 1);
        String resDate = "Day-" + (reservationCount + 1);
        Reservation res = new Reservation(resId, member, item, resDate);
        reservations[reservationCount++] = res;
        return res;
    }

    /** Prints all active reservations managed by this librarian. */
    public void displayAllReservations() {
        System.out.println("  ---- Active Reservations (" + reservationCount + " total) ----");
        boolean hasActive = false;
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i].isActive()) {
                System.out.println("  " + reservations[i].getInfo());
                hasActive = true;
            }
        }
        if (!hasActive) {
            System.out.println("  (no active reservations)");
        }
    }

    // ── Feature 4: Search by Genre / Type (overloaded) ───────────────────────

    /**
     * OVERLOADED METHOD — searches the books catalog by genre.
     * Prints all books whose genre matches (case-insensitive).
     * Prints "No items found for: [genre]" if none match.
     *
     * @param catalog the Books array to search
     * @param size    number of valid entries in catalog
     * @param genre   genre keyword to match (e.g. "Fiction")
     */
    public void searchByGenre(Books[] catalog, int size, String genre) {
        System.out.println("Search results for genre: " + genre);
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (catalog[i] != null
                    && catalog[i].getGenre().equalsIgnoreCase(genre)) {
                System.out.println("- " + catalog[i].getTitle()
                        + " | Author: " + catalog[i].getAuthor()
                        + " | Available: " + catalog[i].isAvailable());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found for: " + genre);
        }
    }

    /**
     * OVERLOADED METHOD — searches the multimedia catalog by type.
     * Same method name as above — demonstrates method overloading.
     * Prints all multimedia items whose type matches (case-insensitive).
     * Prints "No items found for: [type]" if none match.
     *
     * @param catalog the Multimedia array to search
     * @param size    number of valid entries in catalog
     * @param type    type keyword to match (e.g. "DVD")
     */
    public void searchByGenre(Multimedia[] catalog, int size, String type) {
        System.out.println("Search results for type: " + type);
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (catalog[i] != null
                    && catalog[i].getType().equalsIgnoreCase(type)) {
                System.out.println("- " + catalog[i].getTitle()
                        + " | Type: " + catalog[i].getType()
                        + " | Available: " + catalog[i].isAvailable());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found for: " + type);
        }
    }

    /**
     * Returns a detailed description of this librarian.
     * OVERRIDES the base getInfo() in Person with librarian-specific details.
     *
     * @return a formatted string with librarian ID and name
     */
    public String getInfo() {
        // id and name are accessed directly because they are protected in Person
        return "Librarian[" + id + "] " + name;
    }

    /**
     * Returns a readable summary of this librarian.
     * Calls the overridden getInfo() from this class.
     */
    public String toString() {
        return getInfo();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    /** Returns the librarian's ID. Delegates to Person's id field. */
    public String getLibrarianId() { return id; }

    /** Updates the librarian's ID. */
    public void setLibrarianId(String librarianId) { this.id = librarianId; }

    /** Returns the books catalog array. */
    public Books[] getCatalog() { return catalog; }

    /** Returns the number of books in the catalog. */
    public int getCatalogCount() { return catalogCount; }

    /** Returns the multimedia array. */
    public Multimedia[] getMultimedia() { return multimedia; }

    /** Returns the number of multimedia items. */
    public int getMultimediaCount() { return multimediaCount; }

    /** Returns the members array. */
    public Member[] getMembers() { return members; }

    /** Returns the number of registered members. */
    public int getMemberCount() { return memberCount; }

    /** Returns the borrow records array. */
    public BorrowRecord[] getBorrowRecords() { return borrowRecords; }

    /** Returns the total number of borrow records logged. */
    public int getRecordCount() { return recordCount; }

    /** Returns the reservations array. */
    public Reservation[] getReservations() { return reservations; }

    /** Returns the total number of reservations created. */
    public int getReservationCount() { return reservationCount; }
}
