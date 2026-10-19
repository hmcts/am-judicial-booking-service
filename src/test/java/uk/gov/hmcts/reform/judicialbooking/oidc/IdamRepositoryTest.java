package uk.gov.hmcts.reform.judicialbooking.oidc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdamRepositoryTest {

    @Mock
    private IdamApi idamApi = mock(IdamApi.class);

    @Mock
    private CacheManager cacheManager = mock(CacheManager.class);

    IdamRepository idamRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idamRepository = new IdamRepository(idamApi, cacheManager);
        ReflectionTestUtils.setField(
                idamRepository,
                "cacheType", "");
    }

    @Test
    @SuppressWarnings("unchecked")
    void getUserInfo() {
        UserInfo userInfo = mock(UserInfo.class);
        CaffeineCache caffeineCacheMock = mock(CaffeineCache.class);
        com.github.benmanes.caffeine.cache.Cache cache = mock(com.github.benmanes.caffeine.cache.Cache.class);

        when(idamApi.retrieveUserInfo(anyString())).thenReturn(userInfo);
        when(cacheManager.getCache(anyString())).thenReturn(caffeineCacheMock);
        when(caffeineCacheMock.getNativeCache()).thenReturn(cache);
        when(cache.estimatedSize()).thenReturn(anyLong());

        UserInfo returnedUserInfo = idamRepository.getUserInfo("Test");
        assertNotNull(returnedUserInfo);
        verify(idamApi, times(1)).retrieveUserInfo(any());
        verify(cacheManager, times(1)).getCache(any());
        verify(caffeineCacheMock, times(1)).getNativeCache();
        verify(cache, times(1)).estimatedSize();
    }

}