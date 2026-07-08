package myshop.shop.repository.cancelRequest;

import myshop.shop.entity.cancelRequest.CancelRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelRequestRepository extends JpaRepository<CancelRequest, Long> {
}
