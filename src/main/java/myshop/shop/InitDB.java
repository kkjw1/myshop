package myshop.shop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;
import myshop.shop.entity.MemberLevel;
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


        public void dbInit() {
/*            for(int i=1; i<10; i++) {
                String passwordEncode = passwordEncoder.encode("password" + i);
                Member member = new Member("id" + i, "email" + i, passwordEncode, "name" + i,
                        "telecom" + i, "010-0000-000" + i, Gender.MAN, MemberLevel.normal);
                memberRepository.save(member);
            }*/

            Member admin = new Member("test", "kkjjoo1212@naver.com", "test", "테스트아이디", "KT", "010-4710-6305", Gender.MAN, MemberLevel.vip);
            memberRepository.save(admin);
        }
    }
}
