package Utils;

import Pojo.LoginRes;
import Pojo.VerifyOtpRes;
import endPoints.Authapi;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RoleManager {

    // Thread-safe cache
    private static final Map<String, String> tokenCache = new ConcurrentHashMap<>();

    public static String getAccessToken(String role) {
        if (tokenCache.containsKey(role)) {
            return tokenCache.get(role);
        }

        //Step 1: login → get POJO
                LoginRes loginRes = Authapi.login(role);
                System.out.println("Login Response for role [" + role + "]: " + loginRes.getMessage()+" "+ loginRes.getStatus()+" "+loginRes.getData().getEmail());
                String accessToken;
                String refreshToken;


        if ("sales".equalsIgnoreCase(role)) {
            String otpCode = loginRes.getData().getOtpCode();
            if (otpCode == null || otpCode.isEmpty()) {
                throw new RuntimeException("❌ OTP code not received for role: " + role);
            }
            System.out.println("OTP Code received: " + otpCode);

            // Step 2: Verify OTP
            VerifyOtpRes otpResponse = Authapi.verifyOtp(role, otpCode);
            System.out.println("OTP Response for role [" + role + "]: " + otpResponse);
            System.out.println(" Email : "+otpResponse.getData().getEmail()+" Role: "+otpResponse.getData().getRole());
            accessToken = otpResponse.getMeta().getAccessToken();
            refreshToken = otpResponse.getMeta().getRefreshToken();

        } else {
            accessToken = loginRes.getMeta().getAccessToken();
            refreshToken = loginRes.getMeta().getRefreshToken();
        }
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("❌ Access token not received for role: " + role);
        }
        tokenCache.put(role, accessToken);
        return accessToken;

    }
}