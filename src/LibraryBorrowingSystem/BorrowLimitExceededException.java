package LibraryBorrowingSystem;

/** Thrown when a member tries to borrow beyond their allowed borrow limit. */
public class BorrowLimitExceededException extends RuntimeException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
