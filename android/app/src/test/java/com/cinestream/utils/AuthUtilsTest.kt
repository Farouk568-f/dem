package com.cinestream.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for authentication utilities
 */
class AuthUtilsTest {
    
    @Test
    fun testEncodeQRCode() {
        val testData = "cinestream://auth?code=test123"
        val encoded = AuthUtils.encodeToQR(testData)
        assertNotNull("QR code should be generated", encoded)
    }
    
    @Test
    fun testValidateToken() {
        // Test valid token format
        val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U"
        assertTrue("Token should be valid", AuthUtils.isTokenValid(validToken))
        
        // Test invalid token format
        val invalidToken = "not.a.token"
        assertFalse("Token should be invalid", AuthUtils.isTokenValid(invalidToken))
    }
    
    @Test
    fun testTokenExpiration() {
        val expirationTime = System.currentTimeMillis() + 3600000 // 1 hour
        assertTrue("Token should not be expired", !AuthUtils.isTokenExpired(expirationTime))
        
        val pastTime = System.currentTimeMillis() - 3600000 // 1 hour ago
        assertTrue("Token should be expired", AuthUtils.isTokenExpired(pastTime))
    }
}
