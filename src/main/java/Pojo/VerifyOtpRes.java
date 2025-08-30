package Pojo;

import java.util.List;

public class VerifyOtpRes {

    private boolean success;
    private Data data;
    private List<String> error;
    private String message;
    private int status;
    private Meta meta;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public List<String> getError() { return error; }
    public void setError(List<String> error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Meta getMeta() { return meta; }
    public void setMeta(Meta meta) { this.meta = meta; }

    // Nested Data class
    public static class Data {
        private int id;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String createdAt;
        private String updatedAt;
        private String role;
        private int role_id;
        private String role_type;


        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public int getRole_id() { return role_id; }
        public void setRole_id(int role_id) { this.role_id = role_id; }

        public String getRole_type() { return role_type; }
        public void setRole_type(String role_type) { this.role_type = role_type; }
    }

    // Nested Meta class
    public static class Meta {
        private String accessToken;
        private String refreshToken;

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    @Override
    public String toString() {
        return "LoginRes{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                ", meta=" + meta +
                '}';
    }
}
