package com.orangehrm.utilities;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility for tracking and waiting on network requests using Chrome DevTools Protocol (CDP).
 */
public class NetworkWaiter {

    private final DevTools devTools;
    private final Set<String> activeRequests = ConcurrentHashMap.newKeySet();
    private final Set<String> completedRequests = ConcurrentHashMap.newKeySet();
    private final Set<String> earlyFinishedRequests = ConcurrentHashMap.newKeySet();

    public NetworkWaiter(DevTools devTools) {
        this.devTools = devTools;
    }

    public void startTracking() {
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), event -> {
            String id = event.getRequestId().toString();

            // Если уже завершено, не добавляем
            if (completedRequests.contains(id)) return;


            if (earlyFinishedRequests.contains(id)) {
                // Уже завершён — не добавляем
                earlyFinishedRequests.remove(id);
                completedRequests.add(id);
            } else {
                activeRequests.add(id);
            }
        });

        devTools.addListener(Network.loadingFinished(), event -> {
            String id = event.getRequestId().toString();

            if (!activeRequests.remove(id)) {
                earlyFinishedRequests.add(id);
            }
        });

        devTools.addListener(Network.loadingFailed(), event -> {
            String id = event.getRequestId().toString();

            if (!activeRequests.remove(id)) {
                earlyFinishedRequests.add(id);
            }
        });
    }

    public void reset() {
        activeRequests.clear();
        completedRequests.clear();
        earlyFinishedRequests.clear();
    }

    public void waitForAllNetworkRequestsToFinish(Duration timeout) {
        Instant startTime = Instant.now();

        while (true) {
            if (activeRequests.isEmpty()) return;

            if (Duration.between(startTime, Instant.now()).compareTo(timeout) > 0) {
                System.out.println("WARNING: Not all network requests finished in time.");
                System.out.println("❗ ACTIVE REQUESTS AT TIMEOUT:");
                activeRequests.forEach(id ->
                        System.out.println("  Request still active: " + id)
                );
                return; // просто выйти без падения теста
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("WARNING: Network wait interrupted.");
                return;
            }
        }
    }
}