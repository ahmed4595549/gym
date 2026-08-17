package application.member;

import domain.member.Member;
import domain.member.MemberRepository;

import java.time.LocalDate;

public class RegisterMemberService {

    private final MemberRepository memberRepository;

    public RegisterMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member register(
            String name,
            String phone
    ) {

        Member member = new Member(
                name,
                phone,
                LocalDate.now()
        );

        return memberRepository.save(member);
    }
}