package com.aroom.domain.roomProduct.repository;

import com.aroom.domain.roomProduct.model.RoomProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomProductRepository extends JpaRepository<RoomProduct, Long> {

    Optional<RoomProduct> findByRoomId(Long roomId);
}
