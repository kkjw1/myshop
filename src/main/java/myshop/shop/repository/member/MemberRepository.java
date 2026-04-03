package myshop.shop.repository.member;

import myshop.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findById(String id);

    Optional<Member> findByPhoneNumber(String phoneNumber);
}
