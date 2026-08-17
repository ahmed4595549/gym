package application.member;

import domain.member.Member;
import domain.member.MemberRepository;

public class ActivateMemberService {

    private final MemberRepository memberRepository;

    public ActivateMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void activate(Integer memberId) {

        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found.")
                );

        member.activate();

        memberRepository.update(member);
    }
}