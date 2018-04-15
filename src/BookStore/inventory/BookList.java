package BookStore.inventory;

import BookStore.model.Book;

/**
 * Created by ketu.shah on 4/3/2018.
 */
public interface BookList {
    public Book[] list(String searchString);
    public boolean add(Book book, int quantity);
    public int[] buy(Book... books);
}
