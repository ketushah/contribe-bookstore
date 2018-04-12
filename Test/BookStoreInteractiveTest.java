import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import BookStore.service.BookStoreInteractive;

/**
 * Created by ketu.shah on 4/12/2018.
 */
public class BookStoreInteractiveTest {
    BookStoreInteractive interactive = new BookStoreInteractive();

    @Test
    public void checkQuantityValidityTest() {
        Assert.assertEquals(false, interactive.checkQuantityInputValidity(0));
        Assert.assertEquals(false, interactive.checkQuantityInputValidity(-1));
        Assert.assertEquals(true, interactive.checkQuantityInputValidity(1));
        Assert.assertEquals(true, interactive.checkQuantityInputValidity(5));
    }

    @Test
    public void checkPriceInputTest() {
        Assert.assertEquals(false, interactive.checkPriceInput(new BigDecimal(0)));
        Assert.assertEquals(false, interactive.checkPriceInput(new BigDecimal(-100)));
        Assert.assertEquals(true, interactive.checkPriceInput(new BigDecimal(1234.56)));
    }
}
