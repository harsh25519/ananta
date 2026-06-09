package dev.hkb.ananta.config;

import dev.hkb.ananta.payment.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class CronJobs {

    private final PaymentRepository paymentRepository;

    // Constructor injection
    public CronJobs(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Runs every 5 hours.
    // timeUnit = TimeUnit.HOURS is a cleaner way to write this in modern Spring.
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 5)
    @Transactional
    public void cancelPendingAnantaOrders() {
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusHours(5);
            int updatedCount = paymentRepository.cancelAllPendingPayments(cutoffTime);
        } catch (Exception e) {
            throw new RuntimeException("Order are not being processed.",e);
        }
    }
}
