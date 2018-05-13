package ru.mipt.java2017.hw3.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "Authors")
public class Author {

  @Id
  @GeneratedValue(generator = "aut_id")
  @GenericGenerator(name = "aut_id", strategy = "increment")
  @Column(name="ID")
  private Long ID;

  //  not null
  @Column(name="name", length = 50)
  private String Name;

  public Long getID() {
    return ID;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    this.Name = name;
  }

}
