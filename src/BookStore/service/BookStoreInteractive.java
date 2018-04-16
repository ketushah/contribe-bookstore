package BookStore.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import BookStore.dao.BookStoreDao;
import BookStore.model.Book;
import BookStore.model.Cart;
import BookStore.model.Status;
import BookStore.model.User;
import BookStore.inventory.BookStore;

/**
 * Created by ketu.shah on 4/3/2018.
 */
public class BookStoreInteractive {
    public User user;
    private StringBuffer requestOptions;
    static BookStore inventory;

    public BookStoreInteractive() {
        this.user = new User("Guest");
        this.requestOptions = new StringBuffer();
        requestOptions.append("1 - Display Books \n");
        requestOptions.append("2 - Search Title/Author \n");
        requestOptions.append("3 - Display Cart \n");
        requestOptions.append("4 - Checkout \n");
        requestOptions.append("5 - Add Book to Inventory(Requires Admin Access) \n");
        requestOptions.append("6 - Exit");
        inventory = new BookStore();
        loadData();
    }

    public BookStoreInteractive(String userName) {
        this.user = new User(userName);
        this.requestOptions = new StringBuffer();
        requestOptions.append("1 - Display Books \n");
        requestOptions.append("2 - Search Title/Author \n");
        requestOptions.append("3 - Display Cart \n");
        requestOptions.append("4 - Checkout \n");
        requestOptions.append("5 - Add Book to Inventory(Requires Admin Access) \n");
        requestOptions.append("6 - Exit");
        inventory = new BookStore();
        loadData();
    }

    public String getUserName() {
        return this.user.getName();
    }

    public void setUserName(String userName) {
        this.user.setName(userName);
    }

    //Initial User Interaction Function
    public BookStoreInteractive init(BookStoreInteractive interactive) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
            System.out.println("Hello Guest, to tell us your name, please enter 1, or please enter any key to continue:");
            String input = reader.readLine();
            if(input.equals("1")) {
                System.out.println("Please enter your name: ");
                input = reader.readLine();
                interactive.setUserName(input);
                System.out.println("Welcome, "+interactive.getUserName());
            } else {
                System.out.println("Welcome, "+interactive.getUserName());
            }
        } catch (Exception e) {
            System.out.println("Unexpected Entry Made! Please Enter valid input next time.");
        }
        return interactive;
    }

    //Populating Inventory for Current Session
    private void loadData() {
        BookStoreDao dao =new BookStoreDao();
        inventory = new BookStore(dao.loadData());
        if(inventory.getBookStore() == null || inventory.getBookStore().size() == 0) {
            System.out.println("Sorry, we do not have inventory ready. We aplogize for inconvenience. Please visit again later!");
            System.out.println("System Exiting...");
            System.exit(0);
        }
    }

    private void updateData() {
        BookStoreDao dao =new BookStoreDao();
        this.inventory = new BookStore(dao.getLatestData());
    }

    //Function calls for selection made by User
    public void serviceUserRequests() {

        Scanner inputReader = new Scanner(new InputStreamReader(System.in));
        String userInput = "-1";
        while (!userInput.equals("6")) {
            System.out.println("Please enter number associated with your request:");
            System.out.println(requestOptions);
            userInput = inputReader.next();
            switch (userInput) {
                case "1":
                    //Display All Items
                    processBookListing();
                    break;
                case "2":
                    //Search for Book by Name/ Author
                    processSearch();
                    break;
                case "3":
                    //Display Cart
                    processCartListing();
                    break;
                case "4":
                    //Checkout
                    processOrder();
                    break;
                case "5":
                    //Add Item
                    addInventoryItem(3);
                    break;
                case "6":
                    //Exit
                    System.out.println("We appreciate your business! See you soon, "+user.getName());
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Input. Please try again.");
                    System.out.println(requestOptions);
                    userInput = "-1";
                    break;
            }
        }
    }

    //Display Cart Function
    private void processCartListing() {
        if(this.user.getItems() == null || this.user.getItems().getBooks() == null || this.user.getItems().getBooks().isEmpty() || this.user.getItems().getBooks().size() == 0) {
            System.out.println("Hello, "+user.getName());
            System.out.println(this.user.getItems().displayCart());
            emptyCartCheckoutHandler();
        } else {
            System.out.println("Hello, "+user.getName());
            System.out.println(this.user.getItems().displayCart());
            processPrePurchaseInteraction();
        }
    }

    //Checkout Process
    private void processOrder() {
        if(this.user.getItems() == null || this.user.getItems().getBooks() == null || this.user.getItems().getBooks().isEmpty() || this.user.getItems().getBooks().size() == 0) {
            processCartListing();
            //emptyCartCheckoutHandler();
        } else {
            processCartListing();
            //processPurchase(this.user.getItems());
        }
    }

    //Displaying Books
    private void processBookListing() {
        TreeMap<Integer, Book> resultElements = new TreeMap<>();
        int index = 1;
        Book[] books = inventory.list("");
        for(Book book : books) {
            resultElements.put(index, book);
            index++;
        }
        processSearchResults(resultElements);
    }

    //Process to add the book to Cart after selecting Index of Book from Search Result
    private int processSelection(Book book) {
        System.out.println("You have selected following book:");
        System.out.println(book.display());
        System.out.println("Please Select the following options: ");
        System.out.println("1 - Add to cart");
        System.out.println("2 - Back to Search Result");
        Scanner inputReader = new Scanner(new InputStreamReader(System.in));
        String selection = inputReader.next();
        switch (selection) {
            case "1":
                processCartUpdate(book);
                break;
            case "2":
                return 2;
            default:
                System.out.println("Invalid Input. Please Try Again.");
                processSelection(book);
                break;
        }
        return 0;
    }

    //Editing Cart Item Options
    private void processCartUpdate(Book book) {
        int reuqestedQuantity = 0;
        System.out.println("Please Enter Quantity for the Book: ");
        Scanner userInput = new Scanner(new InputStreamReader(System.in));
        try {
            reuqestedQuantity = userInput.nextInt();
        } catch(Exception e) {
            System.out.println("Invalid Input! Only whole numbers allowed. Please try again.");
            processCartUpdate(book);
        }
        if(reuqestedQuantity > 0) {
            //Cart cart = user.getItems();
            if(this.user.getItems().addItemToCart(book, reuqestedQuantity)) {
                System.out.println("Item Added Successfully to the cart.");
                System.out.println(book.display());
                System.out.println("Quantity: " + reuqestedQuantity);
                user.setItems(this.user.getItems());
                processCartUpdate();
            }
        } else {
            System.out.println("Please Enter Valid Quantity Number.");
            processCartUpdate(book);
        }
    }

    //Display Cart Update Options
    private void processCartUpdate() {
        System.out.println(this.user.getItems().displayCart());
        System.out.println("Please Select One of the following options:");
        System.out.println("1 - Checkout");
        System.out.println("2 - Continue Shopping & Jump to previous menu");
        System.out.println("3 - Edit Cart");
        System.out.println("4 - Exit");
        Scanner userInput = new Scanner(new InputStreamReader(System.in));
        String userSelection = userInput.next();
        switch (userSelection) {
            case "1":
                processPurchase(this.user.getItems());
                break;
            case "2":
                return;
            case "3":
                processEditCart();
                processPrePurchaseInteraction();
            case "4":
                System.exit(0);
            default:
                System.out.println("Please enter valid selection.");
                processCartUpdate();
                break;
        }
    }

    //Processing Cart Items for purchase
    private void processPurchase(Cart cart) {
        ArrayList<Book> books = new ArrayList<>();
        //int cartSize = 0;
        for (Map.Entry<Book, Integer> book : new ArrayList<>(cart.getBooks().entrySet())) {
            int quantity = book.getValue();
            for(int i = 0; i < quantity; i++) {
                books.add(book.getKey());
            }
        }
        int[] result = this.inventory.buy(books.toArray(new Book[books.size()]));
        this.user.setItems(new Cart());
        displayPurchaseStatus(books, result);
        System.out.println();
        System.out.println("We appreciate your business. To continue shopping, please enter 1. To exit, enter any key");
        Scanner inputReader = new Scanner(new InputStreamReader(System.in));
        String userInput = inputReader.next();
        if(userInput.equals("1")) {
            serviceUserRequests();
        } else {
            System.exit(0);
        }
    }

    //Validating and displaying status of cart item after initiating transaction
    private void displayPurchaseStatus(ArrayList<Book> books, int[] status) {
        StringBuffer displayData = new StringBuffer();
        displayData.append("Thank You for your business with Contribe Book Store, "+user.getName());
        displayData.append("Your Purchase Summary: \n");
        for(int i=0; i< status.length; i++) {
            displayData.append("\n");
            displayData.append(books.get(i).display());
            Status result = Status.getStatusName(status[i]);
            displayData.append("STATUS: "+ result.name() +"\n");
            if(status[i] != 0) {
                displayData.append("We Apologize for Inconvenience!\n");
            }
        }
        displayData.append("\n");
        displayData.append("Please Visit Us Again, "+user.getName());
        System.out.println(displayData);
    }

    //Creating Result Set with Index
    private void processSearchResults(TreeMap<Integer, Book> resultSet) {
        displaySearchResult(resultSet);
        System.out.println("To Select One of the Result, Enter Index Number of the Book. To go back, press 0");
        Scanner inputReader = new Scanner(new InputStreamReader(System.in));
        try {
            int selection = inputReader.nextInt();
            if (selection == 0)
                return;
            if(selection > resultSet.size()) {
                throw new Exception("Invalid Input!");
            }
            if (processSelection(resultSet.get(selection)) == 2) {
                processSearchResults(resultSet);
            }
        } catch (Exception e) {
            System.out.println("Invalid Entry! Please Try Again...");
            //e.printStackTrace();
            processSearchResults(resultSet);
        }
    }

    //Get Search String from User
    private void processSearch() {
        System.out.print("Please Enter Search String: ");
        Scanner inputReader = new Scanner(new InputStreamReader(System.in));
        String userInput = inputReader.nextLine();
        TreeMap<Integer, Book> resultElements = new TreeMap<>();
        int index = 1;
        Book[] books = inventory.list(userInput);
        if(books == null || books.length == 0) {
            System.out.println("Sorry, no results found. To Go back to Main menu press 1 , or enter any other key to exit.");
            userInput = inputReader.next();
            if(userInput.equals("1")) {
                return;
            } else {
                System.exit(0);
            }
        }
        for(Book book : books) {
            resultElements.put(index, book);
            index++;
        }
        processSearchResults(resultElements);
    }

    //Display Search Results
    private void displaySearchResult(TreeMap<Integer, Book> resultElements) {
        for(Map.Entry<Integer, Book> result : new ArrayList<>(resultElements.entrySet())) {
            System.out.println("Index : "+result.getKey());
            System.out.println(result.getValue().display());
        }
    }

    //Process Transaction when Cart is Empty
    private void emptyCartCheckoutHandler() {
        System.out.println("Select one of the following options:");
        System.out.println("1 - Go Back");
        System.out.println("2 - Add Item to Cart Manually");
        System.out.println("Enter Any other key to exit.");
        Scanner userInput = new Scanner(new InputStreamReader(System.in));
        String userSelection = userInput.next();
        switch (userSelection) {
            case "1":
                return;
            case "2":
                processManualCartItemEntry();
                break;
            default:
                System.out.println("Thank You for visiting us, "+user.getName());
                System.exit(0);
        }
    }

    //Confirmation before purchase
    private void processPrePurchaseInteraction() {
        System.out.println("Please select one of the following options:");
        System.out.println("1 - Checkout");
        System.out.println("2 - Edit Cart");
        System.out.println("3 - Go Back");
        System.out.println("4 - Exit");
        Scanner userInput = new Scanner(new InputStreamReader(System.in));
        String userSelection = userInput.next();
        switch (userSelection) {
            case "1":
                processPurchase(this.user.getItems());
                break;
            case "2":
                processEditCart();
                this.user.setItems(this.user.getItems());
                processCartUpdate();
                break;
            case "3":
                serviceUserRequests();
                break;
            case "4":
                System.out.println("We Appreciate your business with us. Please visit again, "+user.getName());
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Input. Please try again.");
                break;
        }
    }

    //Updating inventory
    private void addInventoryItem(int tryLeft) {
        if(tryLeft == 0) {
            System.out.println("You have exhausted your chances. The Application will terminate now for security reasons.");
            System.exit(1);
        }
        System.out.println("You require Admin Privileges. Please enter Secret Admin Access Key (Case Senstitive): ");
        Scanner inputReader = new Scanner(new InputStreamReader(System.in));
        String userInput = inputReader.nextLine();
        if(this.user.hasAdminPrivileges(userInput)) {
            insertNewItemDetails();
        } else {
            tryLeft--;
            System.out.println("WRONG KEY! TELL US THE CORRECT SECRET KEY IN "+tryLeft+" TRY...");
            addInventoryItem(tryLeft);
        }
    }

    //Process New Book Information
    private void insertNewItemDetails() {
        BookStore.displayInventory();
        String continueFlag;
        int quantity;
        boolean more = true;
        while(more) {
            Scanner inputReader = new Scanner(new InputStreamReader(System.in, Charset.forName("UTF-8")));
            try {
                Book book = processManualItemEntry();
                if (book == null ) {
                    throw new Exception("Invalid Book Data Entered.");
                }
                System.out.print("Please Enter Book Quantity(Only WHOLE POSITIVE numbers allowed): ");
                quantity = inputReader.nextInt();
                if (!checkQuantityInputValidity(quantity)) {
                    throw new Exception("Invalid Quantity Entered. Only Positive, Integer and Non-zero numbers allowed.");
                }
                inventory.add(book, quantity);
                System.out.println("Do you want to add more books? (Enter y for yes | n for no):");
                continueFlag = inputReader.next();
                switch (continueFlag) {
                    case "y":
                        more = true;
                        break;
                    case "n":
                        more = false;
                        break;
                    default:
                        System.out.println("Invalid Input. Exiting Inventory Edit Section.");
                        return;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input! Try Again...");
                //e.printStackTrace();
                //insertNewItemDetails();
                continue;
            }
        }
        BookStoreDao dao = new BookStoreDao();
        dao.updateLocalFileData(inventory.getBookStore());
    }

    private int processManualQuantityEntry() {
        return 0;
    }

    //Process Book Input
    private Book processManualItemEntry() {
        Book book;

        Scanner inputReader = new Scanner(new InputStreamReader(System.in, Charset.forName("UTF-8")));
        String name, author;
        BigDecimal price;
        System.out.print("Please Enter Book Name: ");
        name = inputReader.nextLine();
        System.out.print("Please Enter Book Author Name: ");
        author = inputReader.nextLine();
        System.out.print("Please Enter Book Price(Only Numbers Allowed): ");
        price = inputReader.nextBigDecimal();
        if(!checkPriceInput(price)) {
            book = processManualItemEntry();
        } else {
            book = new Book(name, author, price);
        }
        return book;
    }

    //Option for Cart Update
    private void processEditCart() {

        System.out.println("Select one of the following options:");
        System.out.println("1 - Remove Item from Cart");
        System.out.println("2 - Edit Quantity of an Item in cart");
        System.out.println("3 - Manual Addition of an item in cart");
        System.out.println("4 - Return to previous menu");
        Scanner inputReader = new Scanner(new InputStreamReader(System.in, Charset.forName("UTF-8")));
        String userInput = inputReader.next();

        switch (userInput) {
            case "1":
                processItemRemoval();
                this.user.setItems(this.user.getItems());
                processPrePurchaseInteraction();
                break;
            case "2":
                processEditQuantity();
                this.user.setItems(this.user.getItems());
                processPrePurchaseInteraction();
                break;
            case "3":
                processManualCartItemEntry();
                this.user.setItems(this.user.getItems());
                processPrePurchaseInteraction();
                break;
            case "4":
                break;
            default:
                System.out.println("Invalid Selection. Only choose from Index. Let's Try Again");
                processEditCart();
                break;
        }
    }

    private TreeMap<Integer, Book> getCartMap(Cart cart) {
        int index = 1;
        TreeMap<Integer, Book> cartMap = new TreeMap<>();
        for(Book book : new ArrayList<Book>(cart.getBooks().keySet())) {
            cartMap.put(index, book);
            index++;
        }
        return cartMap;
    }

    //Remove item from Cart
    private void processItemRemoval() {
        TreeMap<Integer, Book> cartItems = getCartMap(this.user.getItems());
        if(cartItems == null || cartItems.size() == 0) {
            emptyCartCheckoutHandler();
        } else {
            System.out.println("You have following Items in your cart.");
            System.out.println(this.user.getItems().displayCart(cartItems));
            System.out.println("Please select the Index of Item you want to remove");
            Scanner inputReader = new Scanner(new InputStreamReader(System.in, Charset.forName("UTF-8")));
            try {
                int userInput = inputReader.nextInt();
                if (userInput > cartItems.size()) {
                    System.out.println("Index Does not Exist. Enter valid input. Try Again.");
                    processItemRemoval();
                }
                if (this.user.getItems().removeItem(cartItems.get(userInput))) {
                    this.user.setItems(this.user.getItems());
                    System.out.println("Item Removed Successfully");
                    //this.user.setItems();
                } else {
                    System.out.println("There was some error. We apologize for inconvenience. Please try again");
                    processItemRemoval();
                }
            } catch (Exception e) {
                System.out.println("Only Whole Numbers Allowed. Enter Valid Input!");
                //e.printStackTrace();
                processItemRemoval();
            }
        }
    }

    private void processEditQuantity() {
        TreeMap<Integer, Book> cartItems = getCartMap(this.user.getItems());
        if(cartItems == null || cartItems.size() == 0) {
            emptyCartCheckoutHandler();
        } else {
            System.out.println("You have following Items in your cart.");
            System.out.println(this.user.getItems().displayCart(cartItems));
            System.out.println("Please select the Index of Book you want to update quantity of: ");
            Scanner inputReader = new Scanner(new InputStreamReader(System.in, Charset.forName("UTF-8")));
            try {
                int indexInput = inputReader.nextInt();
                if (indexInput != 0 && indexInput > cartItems.size()) {
                    System.out.println("Index Does not Exist. Try Again.");
                    processEditQuantity();
                }
                System.out.println("You have selected following item:");
                System.out.println(cartItems.get(indexInput).display());
                System.out.println("Please Enter New Quantity Number(Only Whole Positive Numbers Allowed):");
                int qntInput = inputReader.nextInt();
                if(!checkQuantityInputValidity(qntInput)) {
                    throw new Exception("Invalid Entry!");
                }
                this.user.getItems().updateItem(cartItems.get(indexInput), qntInput);
                this.user.setItems(this.user.getItems());
            } catch (Exception e) {
                System.out.println("Only Whole Positive Numbers Allowed. Enter Valid Input!");
                //e.printStackTrace();
                processEditQuantity();
            }
        }
    }

    //Handle Manual Book Entry into Cart
    private void processManualCartItemEntry() {
        Book book = processManualItemEntry();
        processCartUpdate(book);
        this.user.setItems(this.user.getItems());
    }

    //Check Validity of Quantity Input by User
    public boolean checkQuantityInputValidity(int userInput) {
        if (userInput <= 0)
            return false;
        else
            return true;
    }

    //Check validity of price input by User
    public boolean checkPriceInput(BigDecimal userInput) {
        if (userInput.compareTo(new BigDecimal(0)) <= 0)
            return false;
        else
            return true;
    }
}
