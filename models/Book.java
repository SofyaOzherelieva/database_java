package ru.mipt.java2017.hw3.models;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Books")
public class Book {

  @Id
  @GeneratedValue(generator = "book_id")
  @GenericGenerator(name = "book_id", strategy = "increment")
  @Column(name = "ID")
  private Long ID;

  //not null
  @Column(name = "ISBN", scale = 13)
  private BigDecimal ISBN;

  //not null
  @Column(name = "title", length = 100)
  private String title;

  @Column(name = "cover", length = 400)
  private String cover;

  public Book() {

  }

  public Book(BigDecimal isbn, String title) {
    this.ISBN = isbn;
    this.title = title;
    this.cover = null;
  }

  public Long getID() {
    return ID;
  }

  public BigDecimal getISBN() {
    return ISBN;
  }

  public String getTitle() {
    return title;
  }

  public String getCover() {
    return cover;
  }

  /*public void setID(Long ID) {
    this.ID = ID;
  }

  public void setISBN(Long ISBN) {
    this.ISBN = ISBN;
  }*/

  public void setTitle(String title) {
    this.title = title;
  }

  /*public void setCover(String cover) {
    this.cover = cover;
  }*/
}
