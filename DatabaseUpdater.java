package ru.mipt.java2017.hw3;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.hw3.ReadDataXlsx.BookDescription;
import ru.mipt.java2017.hw3.models.*;

public class DatabaseUpdater {

  private final static Logger logger = LoggerFactory.getLogger("DatabaseUpdater");

  private final EntityManager entityManager;
  private static DatabaseObserver observer;
  private static String connectToJdbs;
  private static String pathToXlsx;
  private static String pathToOutput;
  private static DatabaseAccess access;

  private DatabaseUpdater(String connectToJdbs, String pathToXlsx, String pathToOutput) {
    access = new DatabaseAccess(connectToJdbs);

    this.entityManager = access.entityManager;
    observer = new DatabaseObserver(access);
    this.connectToJdbs = connectToJdbs;
    this.pathToXlsx = pathToXlsx;
    this.pathToOutput = pathToOutput;
  }

  private void updateBooks(Map<BigDecimal, BookDescription> allBooksDescription) {
    Set<BigDecimal> previousISBN = new HashSet<>();
    List<Book> bookList = observer.getBooks();

    for(BigDecimal bookDescription:allBooksDescription.keySet()){
      logger.debug("bookDescription: {}", bookDescription);
    }
    entityManager.getTransaction().begin();
    for (Book book : bookList) {
      String databaseName = book.getTitle();
      BigDecimal isbn = book.getISBN();
      logger.debug("ISBN: {}", isbn);
      String realName = allBooksDescription.get(isbn).title;
      previousISBN.add(isbn);

      if (!realName.equals(databaseName)) {
        entityManager.find(Book.class, book.getID()).setTitle(realName);
      }
    }

    for (Map.Entry<BigDecimal, BookDescription> entry : allBooksDescription.entrySet()) {
      BigDecimal isbn = entry.getKey();
      BookDescription descriptionBook = entry.getValue();
      if (!previousISBN.contains(isbn)) {
        Book newRow = new Book(isbn, descriptionBook.title);
        entityManager.persist(newRow);
      }
    }

    entityManager.getTransaction().commit();
  }

  private void fillAuthorsTable(Set<String> authors) {
    entityManager.getTransaction().begin();
    for (String author : authors) {
      Author newRow = new Author();
      newRow.setName(author);
      entityManager.persist(newRow);
    }
    entityManager.getTransaction().commit();
  }

  private void fillBooksAuthorsTable(Map<BigDecimal, BookDescription> booksDescription) {
    Collection<BookDescription> descriptions = booksDescription.values();

    List<Book> books = observer.getBooks();
    List<Author> authors = observer.getAuthors();

    Map<String, Long> PairBookId = new HashMap<>();
    Map<String, Long> PairAuthorId = new HashMap<>();

    for (Author author : authors) {
      PairAuthorId.put(author.getName(), author.getID());
    }
    for (Book book : books) {
      PairBookId.put(book.getTitle(), book.getID());
    }

    entityManager.getTransaction().begin();
    for (BookDescription description : descriptions) {

      Long bookId = PairBookId.get(description.title);

      int num = 1;
      for (String author : description.authors) {
        Long authorId = PairAuthorId.get(author);
        BookAuthor bookAuthor = new BookAuthor(bookId, authorId, num++);

        entityManager.persist(bookAuthor);
      }
    }
    entityManager.getTransaction().commit();
  }

  private void cleanTables() {
    entityManager.getTransaction().begin();
    List<Author> authors = observer.getAuthors();
    for (Author author : authors) {
      entityManager.remove(author);
    }

    List<BookAuthor> bookAuthors = observer.getBooksAuthors();
    for (BookAuthor book_author : bookAuthors) {
      entityManager.remove(book_author);
    }
    entityManager.getTransaction().commit();
  }

  private void fillDatabase(){
    ReadDataXlsx readXlsx = ReadDataXlsx.createReader(pathToXlsx);

    if (readXlsx == null) {
      logger.error("Cannot open file xlsx.");
      System.exit(1);
    }

    Map<BigDecimal, BookDescription> allBooks = readXlsx.getAllBooksDescription();
    updateBooks(allBooks);

    Set<String> authors = readXlsx.getAllUniqueAuthors(allBooks);
    fillAuthorsTable(authors);
    fillBooksAuthorsTable(allBooks);

  }

  private void fillExcel(){
    WriteDataXlsx writeXlsx = WriteDataXlsx.createProvider(pathToOutput);
    if (writeXlsx == null) {
      logger.error("can't create writeXlsx");
      access.shutdown();
      System.exit(1);
    }

    List<Book> books = observer.getBooks();
    List<Author> allAuthors = observer.getAuthors();
    List<BookAuthor> booksAuthors = observer.getBooksAuthors();
    writeXlsx.writeToDatabase(books, allAuthors, booksAuthors);
  }

  public static void main(String[] args) throws Exception {

    DatabaseUpdater updater = new DatabaseUpdater(args[0], args[1], args[2]);
    updater.cleanTables();

    updater.fillDatabase();
    updater.fillExcel();

    access.shutdown();
  }
}