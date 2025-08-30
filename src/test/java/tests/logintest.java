package tests;

import Utils.RoleManager;
import endPoints.Authapi;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static endPoints.Authapi.logout;

public class logintest {

    static String salesToken;
    static String adminToken;
    static String accountManToken;

    @Test
    public void testSalesLoginAndOtpFlow() {
        String salesToken = RoleManager.getAccessToken("sales");
        System.out.println("✅ Sales Access Token: " + salesToken);
        Response res = Authapi.logout(salesToken);
        // Print result
        System.out.println("Logout Response: " + res.asString());

    }

    @Test
    public void testAdminLoginAndOtpFlow() {
        String adminToken = RoleManager.getAccessToken("admin");
        System.out.println("✅ Admin Access Token: " + adminToken);
    }

    @Test
    public void testAccountManLoginAndOtpFlow() {
        String accountManToken = RoleManager.getAccessToken("accountman");
        System.out.println("✅ AccountMan Access Token: " + accountManToken);
    }

}
