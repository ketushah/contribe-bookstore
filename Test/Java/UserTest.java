package Java;

import org.junit.Assert;
import org.junit.Test;

import BookStore.dto.User;

/**
 * Created by ketu.shah on 4/12/2018.
 */
public class UserTest {
    @Test
    public void adminPrivilegeTest() {
        User user = new User();
        Assert.assertEquals(false, user.hasAdminPrivileges("HackingTheBookStore"));
        Assert.assertEquals(true, user.hasAdminPrivileges("IAmTheBoss"));
    }
}
