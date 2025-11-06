package com.capstone.backend.alarm.service;

import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.domain.service.MemberService;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;
    private final ScheduleRepository scheduleRepository;
    private final FCMService fcmService;


    public void enqueueSchedule(Schedule s, Duration offset) {
        Instant sendAt = s.getStartDateTime()
                .atZone(ZoneId.of("Asia/Seoul")).toInstant()
                .minus(offset);
        String memberSchedule = s.getMemberId() + ":" + s.getId();

        // ZADD notify:due score=sendAt
        redisTemplate.opsForZSet().add("notify:due", memberSchedule, sendAt.toEpochMilli());
    }

    public void disableNotification(Long memberId, Long scheduleId) {
        String key = "notify:due";
        String value = memberId + ":" + scheduleId;
        redisTemplate.opsForZSet().remove(key, value);
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void drainAndSend() throws IOException {
        long now = Instant.now().toEpochMilli();
        // 1) due 목록 조회
        Set<String> due = redisTemplate.opsForZSet()
                .rangeByScore("notify:due", 0, now, 0, 200); // 한 번에 200건

        if (due.isEmpty()) return;

        // 2) 처리 원자화: 파이프라인/트랜잭션 또는 ZPOPMIN 루프 사용
        for (String item : due) {
            Long removed = redisTemplate.opsForZSet().remove("notify:due", item);
            if (removed == 0) continue; // 경합 시 이미 누가 가져감

            String[] parts = item.split(":"); // memberId:scheduleId
            Long memberId = Long.valueOf(parts[0]);
            Long scheduleId = Long.valueOf(parts[1]);

            String sentKey = "notify:sent:" + scheduleId + ":d0"; // d0=당일 알림 등
            Boolean first = redisTemplate.opsForValue().setIfAbsent(sentKey, "1", Duration.ofDays(7));
            if (!first) continue; // 이미 보냈음(중복 방지)

            Member m = memberService.findById(memberId).orElse(null);
            Schedule s = scheduleRepository.findScheduleByMemberIdAndId(memberId, scheduleId).orElse(null);
            if (m == null || s == null || m.getFcmToken() == null) continue;
            log.info("알림 발생 : " + memberId + "오늘 일정 알림" + s.getTitle() + " 시작 임박");
            fcmService.sendMessageTo(m.getFcmToken(), "오늘 일정 알림", s.getTitle() + " 시작 임박", null);
        }
    }
}
