package br.com.paulo.ultra.orderserviceapi.repositories;

import br.com.paulo.ultra.orderserviceapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
