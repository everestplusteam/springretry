package com.fuqiang.service;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
public class OcsService {

    int times = 0;
    @Retryable
    public void connectOcs() {
        System.out.println(String.format("Run %s times", times));
        times++;
        throw new RuntimeException("Failed operation.");
    }

    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(2000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }


    public static void main(String[] args) {
        final OcsService service = new OcsService();
        RetryTemplate template = service.retryTemplate();
        template.execute(args0 -> {
            service.connectOcs();
            return null;
        });
    }
}
