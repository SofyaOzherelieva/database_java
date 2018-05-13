package ru.mipt.java2017.hw3.models;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Books_Authors")
public class BookAuthor {
  @Id
  @GeneratedValue(generator = "book_aut_id")
  @GenericGenerator(name = "book_aut_id", strategy = "increment")
  @Column(name="ID")
  private Long ID;

  // not null
  @Column(name="books_id")
  private Long bookId;

  // not null
  @Column(name="authors_id")
  private Long authorId;

  // not null
  @Column(name="num")
  private int num;

  public BookAuthor() {

  }

  public BookAuthor(Long bookId, Long authorId, int num) {
    this.bookId = bookId;
    this.authorId = authorId;
    this.num = num;
  }

  public Long getID() {
    return ID;
  }

  public Long getBookId() {
    return bookId;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public int getNum() {
    return num;
  }
}

