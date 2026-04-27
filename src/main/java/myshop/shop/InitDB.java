package myshop.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.item.AddItemDto;
import myshop.shop.dto.item.AddItemOptionDto;
import myshop.shop.entity.*;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemStatus;
import myshop.shop.entity.member.Gender;
import myshop.shop.entity.member.Member;
import myshop.shop.entity.member.MemberLevel;
import myshop.shop.repository.address.AddressRepository;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.repository.seller.SellerRepository;
import myshop.shop.service.item.ItemService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
@Profile("local")
@RequiredArgsConstructor
public class InitDB {

    private final InitData initData;

    @PostConstruct
    public void init() {
        initData.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitData {
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final AddressRepository addressRepository;
        private final EntityManager em;
        private final SellerRepository sellerRepository;
        private final ItemService itemService;

        public void dbInit() {
            /**
             * 회원 저장
             */
            for(int i=1; i<5; i++) {
                String testPasswordEncode = passwordEncoder.encode("password" + i);
                Member member = new Member("id" + i, "email" + i, testPasswordEncode, "name" + i,
                        "telecom" + i, "010-0000-000" + i, Gender.MAN, MemberLevel.normal);
                memberRepository.save(member);
            }
            String memberPassword = passwordEncoder.encode("test");
            Member admin = new Member("test", "kkjjoo1212@naver.com", memberPassword,
                    "테스트아이디", "KT", "010-4710-6305", Gender.MAN, MemberLevel.vip);
            memberRepository.save(admin);
            em.flush();
            em.clear();


            /**
             * 주소 저장
             */
            Member findMember = memberRepository.findById("test").orElse(null);
            Address mainAddress = new Address(findMember, "테스트 메인주소", "메인수령인",
                    "010-1234-1234", "12345", "인천광역시 서구",
                    "A아파트", "메인 배송요청사항", true);
            addressRepository.save(mainAddress);

            for (int i=1; i<5; i++) {
                addressRepository.save(new Address(findMember, "테스트 서브주소" + i, "수령인" + i,
                        "010-0000-000" + i, "0000" + i, "도로명" + i,
                        "아파트" + i, "배송요청사항" + i, false));
            }


            /**
             * 판매자 저장
             */
            String sellerPassword = passwordEncoder.encode("test");
            Seller seller = new Seller("test", sellerPassword, "kkjjoo1212@naver.com", "판매자테스트", "010-4710-6305", "테스트회사명",
                    "테스트회사전화번호", "테스트회사우편번호", "테스트회사 도로명", "테스트회사 상세주소");
            sellerRepository.save(seller);


            /**
             * 상품 저장
             */
            Long sellerNo = sellerRepository.findById("test").orElse(null).getNo();
            itemService.saveItem(new AddItemDto(sellerNo, "상품테스트1", ItemCategory.상의, 30000, 40, 10,
                    List.of(
                            new AddItemOptionDto("검정색", 0, 10),
                            new AddItemOptionDto("나이키", 3000, 10),
                            new AddItemOptionDto("흰색", 1000, 10),
                            new AddItemOptionDto("로고", 3000, 10)
                    ), null, "C:/project/shop_image/8ea0eafb-b1a7-492c-b574-654946184243.png", null,
                    List.of(
                            "C:/project/shop_image/0d57c72e-ad5c-463d-b404-93b58fc020e7.png",
                            "C:/project/shop_image/00e766c7-b5b7-46bb-8b9b-50bb5079668b.png",
                            "C:/project/shop_image/1c7e2d70-4ef2-4fd9-862e-40226846215c.png"
                    ), "상품옵션있음, 추가이미지있음", true));

            itemService.saveItem(new AddItemDto(sellerNo, "상품테스트2(품절)", ItemCategory.바지, 50000, 0, 0,
                    List.of(
                            new AddItemOptionDto("면바지", 0, 0),
                            new AddItemOptionDto("청바지", 10000, 0)
                    ), null, "C:/project/shop_image/1736f6e5-e78c-4f29-96d4-621fbfd034d0.png", null,
                    List.of(
                            "C:/project/shop_image/97297b82-81cd-4de7-ba70-bd4e467716df.png"
                    ), "판매완료", ItemStatus.품절,true));


            List<AddItemOptionDto> emptyAddItemOptionDtoList = new ArrayList<>();
            itemService.saveItem(new AddItemDto(sellerNo, "상품테스트3(옵션X)", ItemCategory.아우터, 250000, 20, 0,
                    emptyAddItemOptionDtoList, null, "C:/project/shop_image/30537136-0d60-450e-8544-2f9eda4e4800.png", null,
                    List.of(
                            "C:/project/shop_image/b3ba7699-d546-4075-84a9-9b6888049a0c.png"
                    ), "상품옵션없음, 추가이미지있음", true));


            List<String> emptySubImageList = new ArrayList<>();
            itemService.saveItem(new AddItemDto(sellerNo, "상품테스트4(추가이미지X)", ItemCategory.신발, 150000, 20, 0,
                    List.of(
                            new AddItemOptionDto("250", 0, 10),
                            new AddItemOptionDto("260", 10000, 10)
                    ), null, "C:/project/shop_image/b3ecf6ae-140e-4950-81bf-74adce043239.png", null,
                    emptySubImageList, "상품옵션있음, 추가이미지없음", true));


            itemService.saveItem(new AddItemDto(sellerNo, "상품테스트5(옵션X,추가이미지X)", ItemCategory.신발, 100000, 20, 0,
                    emptyAddItemOptionDtoList, null, "C:/project/shop_image/c9cf742d-c0b0-4b8d-9253-cc32823d36db.png", null,
                    emptySubImageList, "상품옵션없음, 추가이미지없음", true));


        }
    }
}
