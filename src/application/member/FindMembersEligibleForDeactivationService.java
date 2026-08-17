package application.member;

import domain.member.Member;
import domain.member.MemberRepository;

import java.util.List;

public class FindMembersEligibleForDeactivationService {

    private final MemberRepository memberRepository;

    public FindMembersEligibleForDeactivationService(
            MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
    }

    public List<Member> find() {
        return memberRepository
                .findMembersEligibleForDeactivation();
    }
}