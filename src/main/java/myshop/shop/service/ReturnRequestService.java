package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.cancelRequest.ManageCancelReturnDto;
import myshop.shop.dto.returnRequest.SaveReturnRequestDto;
import myshop.shop.entity.orderItem.OrderItem;
import myshop.shop.entity.member.Member;
import myshop.shop.entity.orderItem.OrderItemStatus;
import myshop.shop.entity.returnRequest.ReturnRequest;
import myshop.shop.entity.returnRequest.ReturnRequestStatus;
import myshop.shop.repository.cancelRequest.CancelRequestRepository;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.repository.orderItem.OrderItemRepository;
import myshop.shop.repository.returnRequest.ReturnRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReturnRequestService {

    private final ReturnRequestRepository returnRequestRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final CancelRequestRepository cancelRequestRepository;


    /**
     * 반품 신청
     * 주문 목록/배송 조회 -> 반품 신청
     */
    public void saveReturnRequest(SaveReturnRequestDto saveReturnRequestDto) {
        OrderItem orderItem = orderItemRepository.findById(saveReturnRequestDto.getOrderItemNo()).orElse(null);
        Member memberProxy = memberRepository.getReferenceById(saveReturnRequestDto.getMemberNo());

        orderItem.updateOrderItemStatus(OrderItemStatus.반품접수);

        ReturnRequest saveReturnRequest = new ReturnRequest(orderItem, memberProxy,
                saveReturnRequestDto.getCount(),
                saveReturnRequestDto.getReturnReasonCode(),
                saveReturnRequestDto.getReasonDetail(),
                saveReturnRequestDto.getPrice(),
                ReturnRequestStatus.요청);

        returnRequestRepository.save(saveReturnRequest);

    }


    /**
     * 취소/반품 폼
     * 취소/반품 내역
     * 주문 취소 신청 -> 취소/반품 폼
     * 반품 신청 -> 취소/반품 폼
     */
    public List<ManageCancelReturnDto> getCancelReturnList(Long memberNo) {
        return cancelRequestRepository.findCancelReturnList(memberNo);
    }

}
