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

import LibraryBorrowingSystem.modal.ItemNotAvailableException;
import LibraryBorrowingSystem.modal.Person;

/**
 * Represents a library member who can borrow, return, and search for items.
 * Inherits from Person — gains id and name.
 *
 * A Member is a specific type of Person who interacts with the library
 * by borrowing and returning LibraryItems (Books or Multimedia).
 * The member can hold up to MAX_BORROW items at the same time.
 *
 * Inheritance:
 *   Member extends Person
 *   - Inherits : id, name (and their getters/setters)
 *   - Adds     : borrowedItems array, borrowCount
 *   - Overrides: getInfo(), toString()
 *
 * Overloading:
 *   searchItem(catalog, size, keyword) — search by keyword
 *   searchItem(catalog, size)          — no keyword, lists all available items
 *
 * Attributes:
 *   - borrowedItems : array of items the member is currently borrowing
 *   - borrowCount   : number of items currently borrowed
 */
public class Member extends Person {

    // borrowedItems holds all LibraryItems (Books or Multimedia) this member currently has
    // protected so PremiumMember can directly add/remove items without re-implementing the array
    protected LibraryItem[] borrowedItems;

    // borrowCount tracks how many items this member is currently borrowing
    // protected so PremiumMember can read and increment it in its overridden borrowItem()
    protected int borrowCount;

    // MAX_BORROW is the physical array capacity — enough for any member tier (premium max = 5)
    private static final int MAX_BORROW = 5;

    /**
     * Returns the maximum number of items this member tier can borrow at once.
     * Regular members are limited to 3. PremiumMember overrides this to return 5.
     * Called inside borrowItem() so the limit is always tier-appropriate.
     */
    protected int getBorrowLimit() { return 3; }

    /**
     * Creates a new Member with the given ID and name.
     * Calls the Person constructor (super) to set id and name.
     * The borrowed items array is initialized empty.
     *
     * @param memberId unique member identifier (e.g. "M001")
     * @param name     full name of the member
     */
    public Member(String memberId, String name) {
        // Call the parent Person constructor to set this.id and this.name
        super(memberId, name);

        // Create an empty array with MAX_BORROW slots to hold borrowed items
        this.borrowedItems = new LibraryItem[MAX_BORROW];

        // No items are borrowed yet, so the count starts at 0
        this.borrowCount = 0;
    }

    /**
     * Returns the pre-defined initial member data for the library.
     * Keeping this data here ensures the Member class owns its own defaults.
     *
     * @return array of Member objects pre-filled with default members
     */
    public static Member[] getInitialMembers() {
        // Create an array that can hold 3 Member objects
        Member[] initial = new Member[3];

        // Fill each slot with a pre-defined member using their ID and name
        initial[0] = new Member("M001", "Alice");
        initial[1] = new Member("M002", "Bob");
        initial[2] = new Member("M003", "Charlie");

        // Return the completed array
        return initial;
    }

    /**
     * Borrows a library item (Book or Multimedia) for this member.
     * Regular members are limited to 3 items (getBorrowLimit() returns 3).
     * PremiumMember overrides this method for a limit of 5.
     *
     * Throws ItemNotAvailableException if the item is currently borrowed by someone else.
     * Throws BorrowLimitExceededException if this member has reached their tier limit.
     *
     * @param item the LibraryItem to borrow (can be a Books or Multimedia object)
     * @return true if the borrow was successful
     */
    public boolean borrowItem(LibraryItem item) {
        // If the item is not on the shelf, throw — caller handles the error
        if (!item.isAvailable()) {
            throw new ItemNotAvailableException(
                    "Item '" + item.getTitle() + "' is currently not available for borrowing.");
        }

        // If this member is at their tier limit, throw — caller handles the error
        if (borrowCount >= getBorrowLimit()) {
            throw new BorrowLimitExceededException(
                    name + " has reached the maximum borrow limit of " + getBorrowLimit() + " items.");
        }

        // Add the item to the member's borrowedItems array at the next open slot
        borrowedItems[borrowCount++] = item;

        // Mark the item as no longer available in the system
        item.setAvailable(false);

        // Confirm the successful borrow
        System.out.println("[Regular] " + name + " borrowed: " + item.getTitle());
        return true;
    }

    /**
     * Returns a borrowed library item back to the library.
     * Fails if this member does not currently have the given item.
     *
     * @param item the LibraryItem to return (can be a Books or Multimedia object)
     * @return true if the return was successful, false otherwise
     */
    public boolean returnItem(LibraryItem item) {
        // Loop through all items this member is currently borrowing
        for (int i = 0; i < borrowCount; i++) {

            // Check if this slot has an item and its ID matches the one to return
            if (borrowedItems[i] != null
                    && borrowedItems[i].getItemId().equals(item.getItemId())) {

                // Mark the item as available again in the system
                item.setAvailable(true);

                // Remove the item from the array by replacing it with the last entry
                // --borrowCount decreases the count first, then uses that value as the index
                borrowedItems[i] = borrowedItems[--borrowCount];

                // Clear the last slot to avoid keeping a duplicate reference
                borrowedItems[borrowCount] = null;

                // Confirm the successful return to the console
                System.out.println("  [SUCCESS] " + name + " returned \"" + item.getTitle() + "\".");

                // Return true to indicate the return was successful
                return true;
            }
        }

        // If the loop finishes without finding the item, report failure
        System.out.println("  [FAILED] " + name + " does not have \"" + item.getTitle() + "\".");
        return false;
    }

    /**
     * OVERLOADED METHOD (with keyword) —
     * Searches the library catalog for items whose title contains the given keyword.
     * The search is case-insensitive and works for both Books and Multimedia.
     * This is the first version of searchItem — it takes a keyword to filter by.
     *
     * @param catalog     the full array of LibraryItems in the library
     * @param catalogSize the number of valid items in the catalog array
     * @param keyword     the search keyword to match against item titles
     */
    public void searchItem(LibraryItem[] catalog, int catalogSize, String keyword) {
        // Print a header showing what keyword is being searched
        System.out.println("  Search results for \"" + keyword + "\":");

        // found tracks whether at least one matching item was found
        boolean found = false;

        // Loop through every valid item in the catalog
        for (int i = 0; i < catalogSize; i++) {

            // Convert both strings to lowercase so the search is case-insensitive
            // contains() checks if the title includes the keyword anywhere inside it
            if (catalog[i] != null
                    && catalog[i].getTitle().toLowerCase().contains(keyword.toLowerCase())) {

                // Print the matching item's details using its toString() method
                System.out.println("    -> " + catalog[i]);

                // Mark that we found at least one result
                found = true;
            }
        }

        // If no match was found after checking all items, print a message
        if (!found) {
            System.out.println("    No items found matching \"" + keyword + "\".");
        }
    }

    /**
     * OVERLOADED METHOD (without keyword) —
     * Lists all items currently available to borrow from the library.
     * This is the second version of searchItem — it takes NO keyword.
     * Instead of filtering by title, it shows every item that is available.
     *
     * This method has the SAME name as the one above but a DIFFERENT number of parameters.
     * Java chooses which version to call based on whether a keyword is provided.
     * This is called METHOD OVERLOADING.
     *
     * @param catalog     the full array of LibraryItems in the library
     * @param catalogSize the number of valid items in the catalog array
     */
    public void searchItem(LibraryItem[] catalog, int catalogSize) {
        // Print a header for the browse view
        System.out.println("  All available items in the library:");

        // found tracks whether at least one available item was found
        boolean found = false;

        // Loop through every valid item in the catalog
        for (int i = 0; i < catalogSize; i++) {

            // Only show items that are currently available to borrow
            if (catalog[i] != null && catalog[i].isAvailable()) {

                // Print the item's details using its toString() method
                System.out.println("    -> " + catalog[i]);

                // Mark that we found at least one available item
                found = true;
            }
        }

        // If no available items were found, print a message
        if (!found) {
            System.out.println("    No items are currently available.");
        }
    }

    /**
     * Returns a detailed description of this member.
     * OVERRIDES the base getInfo() in Person with member-specific details.
     * The parent version only shows id and name.
     * This version also adds how many items the member is currently borrowing.
     *
     * @return a formatted string with member ID, name, and borrow count
     */
    public String getInfo() {
        // Build a string that includes the member-specific borrow count
        // id and name are accessed directly because they are protected in Person
        return "Member[" + id + "] " + name + " (borrowing: " + borrowCount + " item(s))";
    }

    /**
     * Returns a readable summary of this member.
     * Calls the overridden getInfo() method from this class.
     */
    public String toString() {
        // Delegates to getInfo() — which is the overridden version in Member
        return getInfo();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /**
     * Returns the member ID.
     * Delegates to getId() inherited from Person.
     */
    public String getMemberId() { return id; }

    /** Returns the array of items currently borrowed by this member. */
    public LibraryItem[] getBorrowedItems() { return borrowedItems; }

    /** Returns the number of items currently borrowed by this member. */
    public int getBorrowCount() { return borrowCount; }

    // ── Setters ───────────────────────────────────────────────────────────────

    /** Updates the member ID. Delegates to Person's id field. */
    public void setMemberId(String memberId) { this.id = memberId; }
}
