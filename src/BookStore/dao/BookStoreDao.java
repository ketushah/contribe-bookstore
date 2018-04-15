package BookStore.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import BookStore.model.Book;

/**
 * Created by ketu.shah on 4/4/2018.
 */
public class BookStoreDao {
    private final static String DELIMETER = ";";

    //Loading Data initially at the start of the application from URL.
    public HashMap<Book, Integer> loadData() {
        HashMap<Book, Integer> bookStoreInvenotry = new HashMap<>();
        try {
            URL dataSource = new URL("https://raw.githubusercontent.com/contribe/contribe/dev/bookstoredata/bookstoredata.txt");
            URLConnection connection = dataSource.openConnection();

            BufferedReader dataReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String fileData;
            while ((fileData = dataReader.readLine()) != null) {
                Object[] objects = processFileInput(fileData);
                Book book = (Book) objects[0];
                int quantity = Integer.parseInt(objects[1].toString());
                if(bookStoreInvenotry.containsKey(book)) {
                    int qnty = bookStoreInvenotry.get(book);
                    bookStoreInvenotry.put(book, qnty + quantity);
                } else {
                    bookStoreInvenotry.put(book, quantity);
                }
            }
            updateLocalFileData(bookStoreInvenotry);
        } catch(Exception e) {
            System.out.println("Inventory Unavailable :( ");
            e.printStackTrace();
        }
        return bookStoreInvenotry;
    }

    //Processes the input of each line of file
    private Object[] processFileInput(String fileData) {
        String[] tokens = fileData.split(DELIMETER);
        Object[] returnData = new Object[2];
        Book book = new Book(tokens[0], tokens[1], new BigDecimal(tokens[2].replaceAll(",","")));
        returnData[0] = book;
        returnData[1] = tokens[3].trim();
        return returnData;
    }

    //Updating local file data after each transaction by user/ inventory update by Admin
    public boolean updateLocalFileData(HashMap<Book, Integer> bookInventory) {
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File("bookdata.txt");

                if (!file.exists()) {
                    file.createNewFile();
                }
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            for(Map.Entry<Book, Integer> book : bookInventory.entrySet()) {
                bufferedWriter.write(book.getKey().toString()+";"+book.getValue());
                bufferedWriter.newLine();
            }


        } catch (Exception e) {
            System.out.println("Error in Local Data Storage: ");
            e.printStackTrace();
            return false;
        }
        finally
        {
            try{
                if(bufferedWriter!=null)
                    bufferedWriter.close();
            } catch(Exception e){
                System.out.println("Error in closing the BufferedWriter");
                e.printStackTrace();
            }
        }
        return true;
    }

    //Getting latest data from Local File after each update on Local
    public HashMap<Book, Integer> getLatestData() {
        BufferedReader dataReader = null;
        HashMap<Book, Integer> bookStoreInvenotry = new HashMap<>();
        try {
            File file = new File("bookdata.txt");

            if (!file.exists()) {
                throw new Exception("Inventory Unavailable");
            }

            dataReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),  Charset.forName("UTF-8")));

            String fileData;
            while ((fileData = dataReader.readLine()) != null) {
                Object[] objects = processFileInput(fileData);
                Book book = (Book) objects[0];
                int quantity = Integer.parseInt(objects[1].toString());
                if(bookStoreInvenotry.containsKey(book)) {
                    int qnty = bookStoreInvenotry.get(book);
                    bookStoreInvenotry.put(book, qnty + quantity);
                } else {
                    bookStoreInvenotry.put(book, quantity);
                }
            }


        } catch (Exception e) {
            System.out.println("Error in reading Local Data Storage: ");
            e.printStackTrace();
        }

        return bookStoreInvenotry;
    }
}
