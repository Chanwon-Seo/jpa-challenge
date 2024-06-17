package com.scw.jpachallenge;

import com.scw.jpachallenge.entityproblem1.Member;
import com.scw.jpachallenge.entityproblem1.Team;
import com.scw.jpachallenge.entityproblem1.enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class Problem1 {

    @PersistenceContext
    private EntityManager em;

    @Test
    @Disabled
    @Transactional
    @DisplayName("Problem1_Fail_IndexOutOfBoundsException")
    void Problem1_Fail() {
        Team team = Team.builder()
                .name("wooteco")
                .build();
        /** team = {id = null, name = "wooteco", members = new ArrayList // size 0} */
        em.persist(team);
        /** team = {id = 1, name = "wooteco", members = new ArrayList // size 0} */

        // member 생성자로 team 정보 입력
        Member member = Member.builder()
                .name("kth990303")
                .team(team)
                .roleType(RoleType.ADMIN)
                .build();
        /**  member = { id = null, name = "kth990303", team = team@123, roleType = ROLE.ADMIN } */
        em.persist(member);

        /**  team = {id = 1, name = "wooteco", members = new ArrayList // size 0 } */
        /**  member = { id = 1, name = "kth990303", team = team@123, roleType = ROLE.ADMIN } */

        System.out.println("=================================");
        List<Member> findMembers = team.getMembers(); //findMembers Size = 0
        for (Member findMember : findMembers) {
            System.out.println(findMember.getName()); // 1번
        }
        System.out.println("=================================");

        Team findTeam = em.find(Team.class, team.getId());
        /**  team = {id = 1, name = "wooteco", members = new ArrayList // size 0 } */

        List<Member> teamMembers = findTeam.getMembers(); //teamMembers.size = 0
        teamMembers.get(0).setName("kth990202"); //IndexOutOfBoundsException

        System.out.println(member.getName());// 2번
    }

    @Test
    @Transactional
    @DisplayName("Problem1_OK")
    void Problem1_OK() {
        Team team = Team.builder()
                .name("wooteco")
                .build();
        /** team = {id = null, name = "wooteco", members = new ArrayList // size 0} */
        em.persist(team);
        /**  team = {id = 1, name = "wooteco", members = new ArrayList // size 0} */

        // member 생성자로 team 정보 입력
        Member member = Member.builder()
                .name("kth990303")
                .team(team)
                .roleType(RoleType.ADMIN)
                .build();
        team.addMember(member);
        /**  team = {id = 1, name = "wooteco", members = member@123 // size 1 } */
        /**  member = { id = null, name = "kth990303", team = team@123, roleType = ROLE.ADMIN } */
        em.persist(member);

        /**  team = {id = 1, name = "wooteco", members = member@123 // size 1 } */
        /**  member = { id = 1, name = "kth990303", team = team@123, roleType = ROLE.ADMIN } */

        System.out.println("=================================");
        List<Member> findMembers = team.getMembers(); //findMembers Size = 1
        for (Member findMember : findMembers) {
            System.out.println(findMember.getName()); // 1번 kth990303
        }
        System.out.println("=================================");

        Team findTeam = em.find(Team.class, team.getId());
        /**  team = {id = 1, name = "wooteco", members = member@123 // size 1 } */

        List<Member> teamMembers = findTeam.getMembers(); //teamMembers.size = 1
        teamMembers.get(0).setName("kth990202");
        /**  team = {id = 1, name = "wooteco", members = member@123 // size 1 } */
        /**  member = { id = 1, name = "kth990202", team = team@123, roleType = ROLE.ADMIN } */
        System.out.println(member.getName());// 2번
    }

}