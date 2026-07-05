package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.delivery.OrderDeliveryDto;
import myshop.shop.repository.delivery.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;


    public List<OrderDeliveryDto> findAllDelivery(Long sellerNo) {
        // delivery 값 가져오기
        Set<Long> orderNoSet = deliveryRepository.getOrderNoSet(sellerNo);
        return deliveryRepository.getOrderDeliveryList(orderNoSet);
    }
}
