package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.cancelRequest.SaveCancelRequestDto;
import myshop.shop.entity.orderItem.OrderItem;
import myshop.shop.entity.cancelRequest.CancelRequest;
import myshop.shop.entity.cancelRequest.CancelRequestStatus;
import myshop.shop.entity.member.Member;
import myshop.shop.entity.orderItem.OrderItemStatus;
import myshop.shop.repository.cancelRequest.CancelRequestRepository;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.repository.orderItem.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelRequestService {

    private final CancelRequestRepository cancelRequestRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;

    /**
     * 주문 취소 신청
     * 주문 목록/배송 조회 -> 주문 취소 신청
     */
    public void saveCancelRequest(SaveCancelRequestDto saveCancelRequestDto) {
        OrderItem orderItem = orderItemRepository.findById(saveCancelRequestDto.getOrderItemNo()).orElse(null);
        Member memberProxy = memberRepository.getReferenceById(saveCancelRequestDto.getMemberNo());

        orderItem.updateOrderItemStatus(OrderItemStatus.취소접수);

        CancelRequest saveCancelRequest = new CancelRequest(orderItem, memberProxy,
                saveCancelRequestDto.getCount(),
                saveCancelRequestDto.getCancelReasonCode(),
                saveCancelRequestDto.getReasonDetail(),
                saveCancelRequestDto.getPrice(),
                CancelRequestStatus.요청);

        cancelRequestRepository.save(saveCancelRequest);
    }



    /**
     * 취소/반품 폼
     * 취소/반품 내역
     * 주문 취소 신청 -> 취소/반품 폼
     * 반품 신청 -> 취소/반품 폼
     */
    public void getCancelReturnList() {

    }
}
