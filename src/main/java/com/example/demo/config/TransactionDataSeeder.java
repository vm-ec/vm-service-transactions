package com.example.demo.config;

import com.example.demo.entity.TransactionLog;
import com.example.demo.repository.TransactionLogRepository;
import com.example.demo.repository.TransactionStateLookupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.stream.IntStream;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Seeds lookup and transaction log data on startup for development.
 * This creates:
 * - 6 distinct lookup-like states are implicit via state values
 * - 100 transaction log entries total
 * - 25 entries for a specific transactionId "txn-25" as history
 * - 10 failed entries in total
 * - 3 failed entries for service "service-3"
 */
@Configuration
public class TransactionDataSeeder {

    @Bean
    CommandLineRunner seed(TransactionLogRepository repository, TransactionStateLookupRepository lookupRepository) {
        return args -> {
            repository.deleteAll();
            lookupRepository.deleteAll();

            // Insert 6 lookup rows
            lookupRepository.save(new com.example.demo.entity.TransactionStateLookup("PENDING", "Waiting to be processed"));
            lookupRepository.save(new com.example.demo.entity.TransactionStateLookup("PROCESSING", "Currently being processed"));
            lookupRepository.save(new com.example.demo.entity.TransactionStateLookup("SUCCESS", "Completed successfully"));
            lookupRepository.save(new com.example.demo.entity.TransactionStateLookup("FAILED", "Processing failed"));
            lookupRepository.save(new com.example.demo.entity.TransactionStateLookup("RETRY", "Scheduled to retry"));
            lookupRepository.save(new com.example.demo.entity.TransactionStateLookup("CANCELLED", "Transaction was cancelled"));

            // Define six states (lookup table semantics are simply represented by these strings)
            String[] states = new String[] {"PENDING", "PROCESSING", "SUCCESS", "FAILED", "RETRY", "CANCELLED"};

            // human-friendly random names for users
            String[] names = new String[] {"Alice", "Bob", "Carol", "Dave", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};

            // list of services to choose from
            String[] services = new String[] {"policy_service", "claim_service", "payment_service", "transaction_service", "notification_service"};

            // Create 100 sample logs across multiple transactionIds with random users and services
            IntStream.rangeClosed(1, 100).forEach(i -> {
                String txnId = "txn-" + ((i % 25) + 1); // generates txn-1 .. txn-25 repeat (so txn-25 has 25 entries)
                String state = states[ThreadLocalRandom.current().nextInt(states.length)];
                String service = services[ThreadLocalRandom.current().nextInt(services.length)];
                String user = names[ThreadLocalRandom.current().nextInt(names.length)];
                Instant timestamp = Instant.now().minusSeconds(1000 - i); // spread timestamps
                repository.save(new TransactionLog(txnId, state, service, user, timestamp));
            });

            // Ensure there are exactly 10 FAILED entries total: if more, convert some to other states and create explicit failed ones
            long failedCount = repository.countByState("FAILED");
            if (failedCount < 10) {
                // create additional failed entries
                for (int j = 0; j < (10 - failedCount); j++) {
                    repository.save(new TransactionLog("txn-failed-" + j, "FAILED", "service-3", "user-fail", Instant.now().plusSeconds(j)));
                }
            }

            // Guarantee at least 3 failed entries exist for service-3
            long failedService3 = repository.findAllByStateAndServiceNameOrderByTimestampDesc("FAILED", "service-3", org.springframework.data.domain.PageRequest.of(0, 100)).getTotalElements();
            if (failedService3 < 3) {
                for (int k = 0; k < (3 - failedService3); k++) {
                    repository.save(new TransactionLog("txn-svc3-fail-" + k, "FAILED", "service-3", "user-fail", Instant.now().plusSeconds(k + 100)));
                }
            }
        };
    }
}
