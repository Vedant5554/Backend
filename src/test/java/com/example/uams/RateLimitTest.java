package com.example.uams;

import org.junit.jupiter.api.Test;

import com.example.uams.security.RateLimitFilter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;

class RateLimitTest {

    @Test
    void testRateLimitingEnforcement() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        
        for (int i = 0; i < 100; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            request.setRemoteAddr("127.0.0.1");

            filter.doFilter(request, response, filterChain);
            assert response.getStatus() != 429 : "Rate limit exceeded too early at request " + i;
        }
        
        MockHttpServletRequest finalReq = new MockHttpServletRequest();
        MockHttpServletResponse finalResp = new MockHttpServletResponse();
        MockFilterChain finalChain = new MockFilterChain();
        finalReq.setRemoteAddr("127.0.0.1");
        
        filter.doFilter(finalReq, finalResp, finalChain);
        assert finalResp.getStatus() == 429 : "Expected 429 TOO_MANY_REQUESTS but got " + finalResp.getStatus();
    }
}
