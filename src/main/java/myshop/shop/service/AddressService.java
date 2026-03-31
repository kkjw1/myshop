package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Address;
import myshop.shop.repository.address.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;

}
