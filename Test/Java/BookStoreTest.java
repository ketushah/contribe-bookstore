package Java;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import BookStore.model.Book;
import BookStore.inventory.BookStore;

/**
 * Created by ketu.shah on 4/12/2018.
 */
public class BookStoreTest {

    BookStore bookStore = new BookStore();

    private void setUpData() {
        HashMap<Book, Integer> inventory = new HashMap<>();
        inventory.put(new Book("Title 1", "Author 1", new BigDecimal(100.00)), 5);
        inventory.put(new Book("Title 2", "Author 2", new BigDecimal(49.99)), 3);
        inventory.put(new Book("Title 3", "Author 3", new BigDecimal(149.49)), 0);
        inventory.put(new Book("Title 4", "Author 4", new BigDecimal(299.99)), 10);
        inventory.put(new Book("Title 5", "Author 5", new BigDecimal(1000.00)), 25);
        bookStore.setBookStore(inventory);
    }

    @Test
    public void buyTest() {
        setUpData();
        Book[] books = new Book[5];
        books[0] = new Book("Title 1", "Author 1", new BigDecimal(100.00));
        books[1] = new Book("Title 1", "Author 1", new BigDecimal(100.00));
        books[2] = new Book("Title 3", "Author 3", new BigDecimal(149.49));
        books[3] = new Book("Title 8", "Author 1", new BigDecimal(100.00));
        books[4] = new Book("Title 2", "Author 2", new BigDecimal(49.99));
        int[] status = bookStore.buy(books);
        Assert.assertEquals(5,status.length);
        Assert.assertArrayEquals(new int[]{0,0,1,2,0}, status);
    }

    @Test
    public void addTest() {
        Assert.assertEquals(true, bookStore.add(new Book("Title 1", "Author 1", new BigDecimal(100.00)), 5));
        Assert.assertEquals(false, bookStore.add(new Book("Title 1", "Author 1", new BigDecimal(100.00)), -3));
        Assert.assertEquals(5, bookStore.getBookStore().get(new Book("Title 1", "Author 1", new BigDecimal(100.00))).intValue());
    }
}
