package com.capstone.backend.member.domain.entity;

import static com.capstone.backend.member.domain.value.Role.ROLE_TEMPORARY_MEMBER;

import com.capstone.backend.global.entity.BaseEntity;
import com.capstone.backend.member.domain.value.AcademicStatus;
import com.capstone.backend.member.domain.value.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "NAME", length = 30)
    private String name;

    @Column(name = "EMAIL", length = 30)
    private String email;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACADEMIC_STATUS")
    private AcademicStatus academicStatus;

    @Column(name = "GRADE")
    private Long grade;

    @Column(name = "COLLEGE", length = 30)
    private String college;

    @Column(name = "DEPARTMENT", length = 30)
    private String department;

    public static Member createTemporaryMember(String email) {
        return Member.builder()
                .email(email)
                .role(ROLE_TEMPORARY_MEMBER)
                .build();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateAcademicInfo(
            AcademicStatus academicStatus,
            Long grade,
            String college,
            String department,
            String name
    ) {
        this.academicStatus = academicStatus;
        this.grade = grade;
        this.college = college;
        this.department = department;
        this.name = name;
    }

    public String getStringGrade(){
        return this.grade.toString() + "학년";
    }
}
