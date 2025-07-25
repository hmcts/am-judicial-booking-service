package uk.gov.hmcts.reform.judicialbooking.oidc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.OAuth2Configuration;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdamRepositoryTest {

    @Mock
    private IdamApi idamApi = mock(IdamApi.class);

    @Mock
    private OIdcAdminConfiguration oidcAdminConfiguration = mock(OIdcAdminConfiguration.class);

    @Mock
    private OAuth2Configuration oauth2Configuration = mock(OAuth2Configuration.class);

    @Mock
    private CacheManager cacheManager = mock(CacheManager.class);

    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    IdamRepository idamRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idamRepository = new IdamRepository(idamApi, oidcAdminConfiguration,
                oauth2Configuration, restTemplate, cacheManager);
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

    @Test
    void getUserRolesBlankResponse() {
        String userId = "003352d0-e699-48bc-b6f5-5810411e60af";
        UserDetails userDetails = UserDetails.builder().email("black@betty.com").forename("ram").surname("jam").id(
                        "1234567890123456")
                .roles(null).build();

        when(idamApi.getUserByUserId(any(), any())).thenReturn(userDetails);

        assertNotNull(idamRepository.getUserByUserId("Test", userId));
    }

    @Test
    @SuppressWarnings("unchecked")
    void getManageUserToken() {
        CaffeineCache caffeineCacheMock = mock(CaffeineCache.class);
        com.github.benmanes.caffeine.cache.Cache cache = mock(com.github.benmanes.caffeine.cache.Cache.class);

        when(cacheManager.getCache(anyString())).thenReturn(caffeineCacheMock);
        when(caffeineCacheMock.getNativeCache()).thenReturn(cache);
        when(cache.estimatedSize()).thenReturn(1L);

        when(oauth2Configuration.getClientId()).thenReturn("clientId");
        when(oauth2Configuration.getClientSecret()).thenReturn("secret");
        when(oidcAdminConfiguration.getSecret()).thenReturn("password");
        when(oidcAdminConfiguration.getScope()).thenReturn("scope");
        TokenResponse tokenResponse = new
                TokenResponse("a", "1", "1", "a", "v", "v");
        when(idamApi.generateOpenIdToken(any())).thenReturn(tokenResponse);

        String result = idamRepository.getManageUserToken("123");

        assertNotNull(result);
        assertFalse(result.isBlank());
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldThrowNullPointerException() {

        String token = "eyJhbGciOiJIUzUxMiJ9.Eim7hdYejtBbWXnqCf1gntbYpWHRX8BRzm4zIC_oszmC3D5QlNmkIetVPcMINg";
        String userId = "4dc7dd3c-3fb5-4611-bbde-5101a97681e0";


        doThrow(NullPointerException.class)
                .when(restTemplate)
                .exchange(anyString(), any(), any(), (Class<?>) any(Class.class));

        assertThrows(NullPointerException.class, () -> idamRepository.searchUserByUserId(token, userId));
    }

    @Test
    void shouldReturnUserRoles() {
        Map<String, Object> mapRoles = new HashMap<>();

        mapRoles.put("userRoles", List.of("caseworker", "am_import"));

        List<Object> list = new ArrayList<>();
        list.add(mapRoles);

        ResponseEntity<List<Object>> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).exchange(isA(String.class), eq(HttpMethod.GET),
                isA(HttpEntity.class), (ParameterizedTypeReference<?>) any(ParameterizedTypeReference.class));

        String token = "eyJhbGciOiJIUzUxMiJ9.Eim7hdYejtBbWXnqCf1gntbYpWHRX8BRzm4zIC_oszmC3D5QlNmkIetVPcMINg";
        String userId = "4dc7dd3c-3fb5-4611-bbde-5101a97681e0";

        ResponseEntity<List<Object>> actualResponse = idamRepository.searchUserByUserId(token, userId);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void testGetManageUserToken() {
        CaffeineCache caffeineCacheMock = mock(CaffeineCache.class);
        when(cacheManager.getCache(anyString())).thenReturn(caffeineCacheMock);
        when(caffeineCacheMock.getNativeCache()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> idamRepository.getManageUserToken("123"));
    }

    @Test
    void testGetHttpHeaders() {
        assertThrows(NullPointerException.class, () -> idamRepository.searchUserByUserId(null, null));
    }
}