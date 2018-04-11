import BookStore.service.BookStoreInteractive;

/**
 * Created by ketu.shah on 4/3/2018.
 */
public class App {
    //public static Book bookInventory;
    public static void main(String[] args) {
        System.out.println("Contribe Book Store");
        BookStoreInteractive interactive = new BookStoreInteractive();
        interactive = interactive.init(interactive);
        interactive.serviceUserRequests();
    }


}
