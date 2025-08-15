package com.quiz_study.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationScheduler {

    @Scheduled(cron = "0 */10 * * * *")
    public void sendPendingNotifications() {
        log.info("NotificationScheduler: 알림 발송 실행");
    }
}
