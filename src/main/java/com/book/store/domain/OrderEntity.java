package com.book.store.domain;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "order_table")
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "order_date")
  private Instant orderDate;

  @OneToMany
  @JoinColumn(name = "id")
  private List<BookEntity> books;

}
