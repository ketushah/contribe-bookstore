package Java;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import BookStore.dto.Book;
import BookStore.dto.Cart;

/**
 * Created by ketu.shah on 4/12/2018.
 */
public class CartTest {
    Cart cart = new Cart();

    @Test
    public void addItemToCartTestv1() {
        this.cart = new Cart();
        Assert.assertEquals(true, this.cart.addItemToCart(new Book("Title 1", "Author 1", new BigDecimal(100.00)), 5));
        Assert.assertEquals(true, this.cart.addItemToCart(new Book("Title 4", "Author 4", new BigDecimal(299.99)), 3));
        Assert.assertEquals(false, this.cart.addItemToCart(new Book("Title 5", "Author 5", new BigDecimal(1000.00)), 0));
        Assert.assertEquals(false, this.cart.addItemToCart(new Book("Title 5", "Author 5", new BigDecimal(1000.00)), -2));
        Assert.assertEquals(true, this.cart.addItemToCart(new Book("Title113", "Author 7", new BigDecimal(9.99)), 2));
    }

    @Test
    public void addItemToCartTestv2() {
        this.cart = new Cart();
        this.cart.addItemToCart(new Book("Title 2", "Author 2", new BigDecimal(49.99)), 2);
        this.cart.addItemToCart(new Book("Title 3", "Author 3", new BigDecimal(149.49)), 3);
        Assert.assertEquals(new BigDecimal(548.45).setScale(2, BigDecimal.ROUND_HALF_DOWN), cart.getTotal().setScale(2, BigDecimal.ROUND_HALF_DOWN));
    }

    @Test
    public void removeItemTest() {
        this.cart = new Cart();
        this.cart.addItemToCart(new Book("Title 4", "Author 4", new BigDecimal(299.99)), 5);
        this.cart.addItemToCart(new Book("Title 1", "Author 1", new BigDecimal(100.00)), 2);
        this.cart.addItemToCart(new Book("Title 5", "Author 5", new BigDecimal(1000.00)), 3);
        Assert.assertEquals(true, this.cart.removeItem(new Book("Title 4", "Author 4", new BigDecimal(299.99))));
        Assert.assertEquals(false, this.cart.removeItem(new Book("Title 3", "Author 1", new BigDecimal(123.45))));
        Assert.assertEquals(new BigDecimal(3200.00).setScale(2, BigDecimal.ROUND_HALF_DOWN), this.cart.getTotal().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        Assert.assertEquals(2, this.cart.getBooks().size());
    }

    @Test
    public void updateItemTest() {
        this.cart = new Cart();
        this.cart.addItemToCart(new Book("Title 2", "Author 2", new BigDecimal(49.99)), 1);
        this.cart.addItemToCart(new Book("Title 5", "Author 5", new BigDecimal(1000.00)), 1);
        this.cart.addItemToCart(new Book("Title 3", "Author 3", new BigDecimal(149.49)), 1);
        this.cart.addItemToCart(new Book("Title 1", "Author 1", new BigDecimal(100.00)), 1);
        Assert.assertEquals(false, this.cart.updateItem(new Book("Title 4", "Author 4", new BigDecimal(299.99)),1));
        Assert.assertEquals(true, this.cart.updateItem(new Book("Title 1", "Author 1", new BigDecimal(100.00)),5));
        Assert.assertEquals(5, this.cart.getBooks().get(new Book("Title 1", "Author 1", new BigDecimal(100.00))).intValue());
        Assert.assertEquals(new BigDecimal(1699.48).setScale(2, BigDecimal.ROUND_HALF_DOWN), this.cart.getTotal().setScale(2, BigDecimal.ROUND_HALF_DOWN));
    }
}
