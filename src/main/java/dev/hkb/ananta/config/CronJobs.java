package dev.hkb.ananta.config;

import dev.hkb.ananta.order.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
public class CronJobs {

    private final OrderRepository orderRepository;

    // Constructor injection
    public CronJobs(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Runs every 5 hours.
    // timeUnit = TimeUnit.HOURS is a cleaner way to write this in modern Spring.
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 5)
    @Transactional
    public void cancelPendingAnantaOrders() {
        try {
            int updatedCount = orderRepository.cancelAllPendingOrders();
        } catch (Exception e) {
            throw new RuntimeException("Order are not being processed.",e);
        }
    }
}
