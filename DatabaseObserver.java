package ru.mipt.java2017.hw3;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import ru.mipt.java2017.hw3.models.*;

public class DatabaseObserver {

  private final EntityManager entityManager;
  private final CriteriaBuilder builder;

  DatabaseObserver(DatabaseAccess access) {
    entityManager = access.entityManager;
    builder = entityManager.getCriteriaBuilder();
  }

  List<Book> getBooks() {
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    query.select(query.from(Book.class));

    List<Book> bookList = entityManager.createQuery(query).getResultList();
    return bookList;
  }

  List<Author> getAuthors() {
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    query.select(query.from(Author.class));

    List<Author> authorList = entityManager.createQuery(query).getResultList();
    return authorList;
  }

  List<BookAuthor> getBooksAuthors() {
    CriteriaQuery<BookAuthor> query = builder.createQuery(BookAuthor.class);
    query.select(query.from(BookAuthor.class));

    List<BookAuthor> bookAuthorList = entityManager.createQuery(query).getResultList();
    return bookAuthorList;
  }
}
