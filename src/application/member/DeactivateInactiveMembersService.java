package application.member;

import domain.member.Member;
import domain.member.MemberRepository;

public class DeactivateInactiveMembersService {

    private final MemberRepository memberRepository;

    public DeactivateInactiveMembersService(
            MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
    }

    public void deactivate(Integer memberId) {

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Member not found."
                                )
                        );

        member.deactivate();

        memberRepository.update(member);
    }
}