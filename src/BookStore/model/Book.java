package BookStore.model;

import java.math.BigDecimal;

/**
 * Created by ketu.shah on 4/3/2018.
 */
public class Book {
    private String title;
    private String author;
    private BigDecimal price;

    public Book(String title, String author, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        if (getTitle() != null ? !getTitle().equals(book.getTitle()) : book.getTitle() != null) return false;
        if (getAuthor() != null ? !getAuthor().equals(book.getAuthor()) : book.getAuthor() != null) return false;
        return getPrice() != null ? getPrice().equals(book.getPrice()) : book.getPrice() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ""+title+";"+author+";"+price;
    }

    public String display() {
        StringBuffer bookInfo = new StringBuffer();
        bookInfo.append("Title: "+title+"\n");
        bookInfo.append("Author: "+author+"\n");
        bookInfo.append("Price: "+price+"\n");
        return new String(bookInfo);
    }
}
