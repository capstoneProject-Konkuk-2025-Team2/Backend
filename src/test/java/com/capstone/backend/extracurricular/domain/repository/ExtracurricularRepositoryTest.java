package com.capstone.backend.extracurricular.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class ExtracurricularRepositoryTest {
    @Autowired
    private ExtracurricularRepository extracurricularRepository;

    @DisplayName("findById 테스트")
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

    @DisplayName("deleteById 테스트")
    @Test
    void deleteById_success() {
        //given
        Extracurricular extracurricular = Extracurricular.builder().title("A비교과").build();
        extracurricularRepository.save(extracurricular);
        //when
        extracurricularRepository.deleteById(extracurricular.getId());
        //then
        Optional<Extracurricular> findExtracurricular = extracurricularRepository.findById(
                extracurricular.getId());
        assertThat(findExtracurricular).isEqualTo(Optional.empty());
    }

    @DisplayName("LIKE 검색 + 페이징 기본 동작")
    @Test
    void findAllByTitle_like_with_paging() {
        //given
        extracurricularRepository.saveAll(
                List.of(
                        Extracurricular.builder().title("A비교과").build(),
                        Extracurricular.builder().title("B비교과").build(),
                        Extracurricular.builder().title("C비교과").build(),
                        Extracurricular.builder().title("D비교과").build()
                )
        );
        String key = "비교과";
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        // when
        Page<Extracurricular> page = extracurricularRepository.findAllByTitle(key, pageable);
        // then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();

        assertThat(page.getContent().get(0).getTitle()).contains(key);
        assertThat(page.getContent().get(1).getTitle()).contains(key);
    }

    @Test
    @DisplayName("검색 결과가 없을 때도 Page 메타데이터가 정상이어야 한다")
    void findAllByTitle_noResult() {
        // given
        extracurricularRepository.save(Extracurricular.builder().title("A비교과").build());
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Extracurricular> page = extracurricularRepository.findAllByTitle("B", pageable);

        // then
        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isTrue();
    }
}
