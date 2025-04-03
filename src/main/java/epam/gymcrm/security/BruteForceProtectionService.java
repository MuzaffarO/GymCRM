package epam.gymcrm.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BruteForceProtectionService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_DURATION_SECONDS = 300; // 5 mins

    private String attemptsKey(String username) {
        return "LOGIN_ATTEMPTS:" + username;
    }

    private String lockKey(String username) {
        return "LOCKED_USER:" + username;
    }

    public boolean isBlocked(String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey(username)));
    }

    public void recordFailedAttempt(String username) {
        String key = attemptsKey(username);
        Long attempts = redisTemplate.opsForValue().increment(key);

        if (attempts == 1) {
            // set initial TTL
            redisTemplate.expire(key, Duration.ofMinutes(5));
        }

        if (attempts >= MAX_ATTEMPTS) {
            redisTemplate.delete(key); // reset counter
            redisTemplate.opsForValue().set(lockKey(username), "LOCKED", LOCK_DURATION_SECONDS, TimeUnit.SECONDS);
        }
    }

    public void resetAttempts(String username) {
        redisTemplate.delete(attemptsKey(username));
        redisTemplate.delete(lockKey(username));
    }
}
