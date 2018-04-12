package BookStore.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import BookStore.dao.BookStoreDao;
import BookStore.dto.Book;
import BookStore.dto.Status;

/**
 * Created by ketu.shah on 4/3/2018.
 */
public class BookStore implements BookList {
    static HashMap<Book, Integer> bookStore;

    public BookStore() {
        BookStore.bookStore = new HashMap<>();
    }

    public BookStore(HashMap<Book, Integer> bookStore) {
        BookStore.bookStore = bookStore;
    }

    public HashMap<Book, Integer> getBookStore() {
        return BookStore.bookStore;
    }

    public void setBookStore(HashMap<Book, Integer> bookStore) {
        BookStore.bookStore = bookStore;
    }

    @Override
    public Book[] list(String searchString) {
        ArrayList<Book> resultList;
        if(searchString == null || searchString.equals("")) {
            resultList = new ArrayList<>();
            for(Map.Entry<Book, Integer> book : BookStore.bookStore.entrySet()) {
                    resultList.add(book.getKey());
            }
        } else {
            resultList = new ArrayList<>();
            for(Map.Entry<Book, Integer> book : new ArrayList<>(BookStore.bookStore.entrySet())) {
                if (book.getKey().getTitle().trim().toLowerCase().replaceAll(" ","").contains(searchString.trim().toLowerCase().replaceAll(" ","")) ||
                        book.getKey().getAuthor().trim().toLowerCase().replaceAll(" ","").contains(searchString.trim().toLowerCase().replaceAll(" ",""))) {
                    resultList.add(book.getKey());
                }
            }
        }

        return resultList.toArray(new Book[resultList.size()]);
    }

    @Override
    public boolean add(Book book, int quantity) {
        if(quantity >= 0) {
            try {
                if (BookStore.bookStore.containsKey(book)) {
                    int qnt = BookStore.bookStore.get(book);
                    qnt += quantity;
                    BookStore.bookStore.put(book, qnt);
                } else {
                    BookStore.bookStore.put(book, quantity);
                }
                return true;
            } catch (Exception e) {
                System.out.println("Error while updating inventory.");
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int[] buy(Book... books) {
        int[] status = new int[books.length];
        int i = 0;
        for(Book book : books) {
            if(BookStore.bookStore.containsKey(book)) {
                if (isAvailable(book)) {
                    BookStore.bookStore.put(book, (BookStore.bookStore.get(book) - 1));
                    status[i] = Status.OK.getStatus();
                } else {
                    status[i] = Status.NOT_IN_STOCK.getStatus();
                }
            } else {
                status[i] =Status.DOES_NOT_EXIST.getStatus();
            }
            i++;
        }
        BookStoreDao dao = new BookStoreDao();
        dao.updateLocalFileData(BookStore.bookStore);
        return status;
    }

    private boolean isAvailable(Book book) {
        if(BookStore.bookStore.get(book) < 1) {
            return false;
        }
        return true;
    }

    public static void displayInventory() {
        System.out.println("------Contribe Book Store Inventory Listing------");
        for(Map.Entry<Book, Integer> book : BookStore.bookStore.entrySet()) {
            System.out.println(book.getKey().display());
            System.out.println("Quantity: " + book.getValue());
            System.out.println();
        }
    }
}
