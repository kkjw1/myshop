package myshop.shop.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.Address.ManageAddressDto;
import myshop.shop.entity.Address;
import myshop.shop.entity.Member;
import myshop.shop.repository.address.AddressRepository;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;


    /**
     * 주소 저장
     */
    public void saveAddress(Long memberNo, ManageAddressDto manageAddressDto) {
        Member memberProxy = memberRepository.getReferenceById(memberNo);
        Address address = new Address(memberProxy,
                manageAddressDto.getAddressName(),
                manageAddressDto.getRecipientName(),
                manageAddressDto.getPhoneNumber(),
                manageAddressDto.getPostcode(),
                manageAddressDto.getRoadAddress(),
                manageAddressDto.getDetailAddress(),
                manageAddressDto.getDeliveryRequest(),
                manageAddressDto.getMainAddress());
        addressRepository.save(address);
    }



    /**
     * 주소리스트 반환(ManageAddressDto)
     */
    public List<ManageAddressDto> getAddressesByMemberNo(Long memberNo) {
        List<Address> findAddresses = addressRepository.findByMemberNoOrderByMainAddressDesc(memberNo);
        return findAddresses.stream()
                .map(ManageAddressDto::new)
                .collect(toList());
    }



    /**
     * 기본 배송지로 변경
     */
    public int mainUpdate(Long addressNo, Long memberNo) {
        int result1 = addressRepository.updateMainAddressToFalse(memberNo);
        int result2 = addressRepository.updateMainAddressToTrue(addressNo);
        return result1 + result2;
    }


    /**
     * 주소 삭제
     */
    public int deleteAddress(Long addressNo) {
        return addressRepository.deleteAddressByNo(addressNo);
    }
}
