package BookStore.model;

/**
 * Created by ketu.shah on 4/7/2018.
 */
public class User {
    String name;
    Cart items;

    public User(String name) {
        this.name = name;
        this.items = new Cart();
    }

    public User() {
        this.name = "Guest";
        this.items = new Cart();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cart getItems() {
        return items;
    }

    public void setItems(Cart items) {
        this.items = items;
    }

    public boolean hasAdminPrivileges(String key) {
        if(key.equals("IAmTheBoss"))
            return true;
        else
            return false;
    }
}
