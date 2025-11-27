package com.example.member.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "user_details")
@Data
public class MemberDetail {
    @Id
    private Long id;  // Member와 동일한 ID 사용
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Member member;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "phone_num")
    private String phoneNum;
}
