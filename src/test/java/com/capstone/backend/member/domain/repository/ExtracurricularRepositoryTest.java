package com.capstone.backend.member.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.backend.member.domain.entity.Extracurricular;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class ExtracurricularRepositoryTest {
    @Autowired
    private ExtracurricularRepository extracurricularRepository;

    @DisplayName("findById테스트")
    @Test
    void findById_success() {
        //given
        Extracurricular extracurricular = Extracurricular.builder().title("A비교과").build();
        extracurricularRepository.save(extracurricular);
        //when
        Extracurricular findExtracurricular = extracurricularRepository.findById(extracurricular.getId()).get();
        //then
        assertThat(findExtracurricular.getId()).isEqualTo(extracurricular.getId());
        assertThat(findExtracurricular.getTitle()).isEqualTo(extracurricular.getTitle());
    }

    @DisplayName("deleteById테스트")
    @Test
    void deleteById_success() {
        //given
        Extracurricular extracurricular = Extracurricular.builder().title("A비교과").build();
        extracurricularRepository.save(extracurricular);
        //when
        extracurricularRepository.deleteById(extracurricular.getId());
        //then
        Optional<Extracurricular> findExtracurricular = extracurricularRepository.findById(extracurricular.getId());
        assertThat(findExtracurricular).isEqualTo(Optional.empty());
    }
}
