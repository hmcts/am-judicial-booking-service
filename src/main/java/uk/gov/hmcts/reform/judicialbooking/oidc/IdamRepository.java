package uk.gov.hmcts.reform.judicialbooking.oidc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.judicialbooking.util.Constants.BEARER;

@Component
@Slf4j
public class IdamRepository {

    private final IdamApi idamApi;

    @Value("${spring.cache.type}")
    protected String cacheType;

    private final CacheManager cacheManager;

    @Autowired
    public IdamRepository(IdamApi idamApi,
                          CacheManager cacheManager) {
        this.idamApi = idamApi;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "token")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 3))
    public UserInfo getUserInfo(String jwtToken) {
        if (cacheType != null && !cacheType.equals("none")) {
            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache("token");
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = requireNonNull(caffeineCache)
                    .getNativeCache();
            log.info("generating Bearer Token, current size of cache: {}", nativeCache.estimatedSize());
        }
        return idamApi.retrieveUserInfo(BEARER + jwtToken);
    }

}
