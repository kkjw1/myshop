package myshop.shop.repository.cancelRequest;

import myshop.shop.dto.cancelRequest.ManageCancelReturnDto;

import java.util.List;

public interface CancelRequestRepositoryCustom {
    List<ManageCancelReturnDto> findCancelReturnList(Long memberNo);
}
