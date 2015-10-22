package eu.seaclouds.paas;

public interface Credentials {

    public class UserPasswordCredentials implements Credentials {
        private String user;
        private String password;
    
        public UserPasswordCredentials(String user, String password) {
            this.user = user;
            this.password = password;
        }
        
        public String getUser() {
            return user;
        }
        
        public String getPassword() {
            return password;
        }
    }

    public class ApiKeyCredentials implements Credentials {
        String apiKey;

        public ApiKeyCredentials(String apiKey) {
            super();
            this.apiKey = apiKey;
        }

        public String getApiKey() {
            return apiKey;
        }
    }
    
}