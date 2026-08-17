package domain.member;


import java.util.Optional;
import java.util.List;


public interface MemberRepository {
    Member save(Member member);



    Optional<Member> findById(Integer id);

    List<Member> findByName(String name);

    List<Member> findAll();
    void update(Member member);
    List<Member> findMembersEligibleForDeactivation();

}
