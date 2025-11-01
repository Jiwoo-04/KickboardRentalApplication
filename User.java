import java.util.HashMap;
import java.util.Map;

class User {
    private String username;
    private String password;
    private String name;
}

class UserManager {
    private Map<String, User> userDB = new HashMap<>();

    public void register(String id, User user) {
        userDB.put(id, user);
    }

    public boolean login(String username, String password) {
        return true;
    }

    public void logout() {

    }
}

class UserRepository {
    // 저장, 조회, 중복 확인 등
}