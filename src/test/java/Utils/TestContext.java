package Utils;

import io.restassured.response.Response;

public class TestContext {

    private String accessToken;
    private Response lastResponse;

    private int selectedCustomerId;
    private String selectedCustomerSwellId;
    private String selectedCustomerEmail;

    private String impersonationToken;
    private String impersonationMessage;
    private String impersonationOtp;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }
    public int getSelectedCustomerId() {
        return selectedCustomerId;
    }


    public void setSelectedCustomerId(int selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }
    public String getSelectedCustomerSwellId() {
        return selectedCustomerSwellId;
    }


    public void setSelectedCustomerSwellId(String selectedCustomerSwellId) {
        this.selectedCustomerSwellId = selectedCustomerSwellId;
    }
    public String getselectedCustomerEmail() {
        return selectedCustomerEmail;
    }


    public void setselectedCustomerEmail(String selectedCustomerEmail) {
        this.selectedCustomerEmail = selectedCustomerEmail;
    }

    /** Store the impersonation token returned by the API */
    public void setImpersonationToken(String impersonationToken) {
        this.impersonationToken = impersonationToken;
    }

    /** Retrieve the stored impersonation token */
    public String getImpersonationToken() {
        return impersonationToken;
    }

    /** Store the impersonation message returned by the API */
    public void setImpersonationMessage(String impersonationMessage) {
        this.impersonationMessage = impersonationMessage;
    }

    /** Retrieve the stored impersonation message */
    public String getImpersonationMessage() {
        return impersonationMessage;
    }

    /** Retrieve the stored impersonation otp */
    public void SetImpersonationOtp(String impersonationOtp) {
        this.impersonationOtp = impersonationOtp;
    }

    public String GetImpersonationOtp() {
        return impersonationOtp;
    }


}
