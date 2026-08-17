package application.member;

import domain.member.Member;
import domain.member.MemberRepository;

    public class UpdateMemberService {

        private final MemberRepository memberRepository;

        public UpdateMemberService(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }

        public void updateName(Integer memberId, String newName) {

            Member member = memberRepository
                    .findById(memberId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Member not found."
                            )
                    );

            member.changeName(newName);

            memberRepository.update(member);
        }

        public void updatePhone(Integer memberId, String newPhone) {

            Member member = memberRepository
                    .findById(memberId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Member not found."
                            )
                    );

            member.changePhone(newPhone);

            memberRepository.update(member);
        }
    }

