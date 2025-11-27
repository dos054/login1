package com.example.member.repository;

import com.example.member.model.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;

// 회원 상세정보 데이터 접근 Repository (JPA)
// - 현재 미사용, 추후 확장용
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {
}
