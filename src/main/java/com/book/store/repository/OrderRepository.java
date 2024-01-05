package com.book.store.repository;

import com.book.store.domain.OrderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

  @Query("""
          select o from OrderEntity as o
          where o.user.id = :user_id""")
  List<OrderEntity> findAllByUserId(@Param("user_id") String userId);
}
