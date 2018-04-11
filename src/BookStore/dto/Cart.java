package BookStore.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ketu.shah on 4/7/2018.
 */
public class Cart {
    HashMap<Book, Integer> books;
    BigDecimal total;

    public Cart() {
        this.books = new HashMap<>();
        this.total = new BigDecimal(0);
    }

    public boolean addItemToCart(Book book, int quantity) {
        if(this.books.containsKey(book)) {
            int quant = books.get(book);
            quant += quantity;
            this.total = this.total.add(book.getPrice().multiply(new BigDecimal(quant)));
            this.books.put(book, quant);
        } else {
            this.books.put(book, quantity);
            this.total = this.total.add(book.getPrice().multiply(new BigDecimal(quantity)));
        }
        return true;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String displayCart() {
        StringBuffer display =new StringBuffer();
        display.append("You have following Items in your cart:\n");
        if(this.books == null || this.books.isEmpty() || this.books.size() == 0) {
            return new String("Your Cart is Empty!");
        }
        for(Map.Entry<Book, Integer> book : new ArrayList<>(this.books.entrySet())) {
            display.append(book.getKey().display());
            display.append("Quantity: "+book.getValue()+"\n");
            display.append("\n \n");
        }
        display.append("Your Total is: "+this.total.toString()+"\n");
        return new String(display);
    }

    public boolean removeItem(Book book) {
        if(this.books.containsKey(book)) {
            this.total = this.total.subtract(book.getPrice().multiply(new BigDecimal(this.books.get(book))));
            this.books.remove(book);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateItem(Book book, int newQuantity) {
        if(this.books.containsKey(book)) {
            this.total = this.total.subtract(book.getPrice().multiply(new BigDecimal(this.books.get(book))));
            this.total = this.total.add(book.getPrice().multiply(new BigDecimal(newQuantity)));
            this.books.put(book, newQuantity);
            return true;
        } else {
            return false;
        }
    }

    public String displayCart(TreeMap<Integer, Book> cartMap) {
        StringBuffer display =new StringBuffer();
        //display.append("You have following Items in your cart:\n");
        if(cartMap == null || cartMap.size() == 0) {
            return new String("Your Cart is Empty!");
        }
        for(Map.Entry<Integer, Book> cartItem : new ArrayList<>(cartMap.entrySet())) {
            display.append("Index: "+cartItem.getKey()+"\n");
            display.append(cartItem.getValue().display());
            display.append("Quantity: "+this.books.get(cartItem.getValue())+"\n");
            display.append("\n");
        }
        display.append("Your Total is: "+this.total.toString()+"\n");
        return new String(display);
    }
}
