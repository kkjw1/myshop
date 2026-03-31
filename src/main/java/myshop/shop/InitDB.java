package myshop.shop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;
import myshop.shop.entity.MemberLevel;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.context.annotation.Profile;
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

        public void dbInit() {
            for(int i=1; i<10; i++) {
                Member member = new Member("id" + i, "email" + i, "password" + i, "name" + i,
                        "telecom" + i, "phoneNumber" + i, Gender.male, MemberLevel.normal);
                memberRepository.save(member);
            }

        }
    }
}
