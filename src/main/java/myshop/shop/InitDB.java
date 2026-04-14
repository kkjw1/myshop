package myshop.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.entity.Address;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;
import myshop.shop.entity.MemberLevel;
import myshop.shop.repository.address.AddressRepository;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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

        public void dbInit() {
/*            for(int i=1; i<10; i++) {
                String passwordEncode = passwordEncoder.encode("password" + i);
                Member member = new Member("id" + i, "email" + i, passwordEncode, "name" + i,
                        "telecom" + i, "010-0000-000" + i, Gender.MAN, MemberLevel.normal);
                memberRepository.save(member);
            }*/
            String passwordEncode = passwordEncoder.encode("test");
            Member admin = new Member("test", "kkjjoo1212@naver.com", passwordEncode,
                    "테스트아이디", "KT", "010-4710-6305", Gender.MAN, MemberLevel.vip);
            memberRepository.save(admin);
            em.flush();
            em.clear();

            Member findMember = memberRepository.findById("test").orElse(null);
            Address mainAddress = new Address(findMember, "테스트 메인주소", "메인수령인",
                    "010-1234-1234", "12345", "인천광역시 서구",
                    "A아파트", "메인 배송요청사항", false);
            addressRepository.save(mainAddress);

            for (int i=1; i<5; i++) {
                addressRepository.save(new Address(findMember, "테스트 서브주소" + i, "수령인" + i,
                        "010-0000-000" + i, "0000" + i, "도로명" + i,
                        "아파트" + i, "배송요청사항" + i, false));
            }


        }
    }
}
