package AccountManage.accountmanage;

import AccountManage.accountmanage.tier.*;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * 로그인, 회원가입에 필요한 절차를 수행<br>
 * {@code LoginService.INSTANCE} 형태로 객체 참조 요망
 */
public enum LoginService {
    INSTANCE;

    private Connection conn;
    private Console console;
    static final int MAX_ID_LENGTH = 20;
    static final int MAX_PASSWORD_LENGTH = 20;
    static final int MAX_NAME_LENGTH = 10;
    static final int MAX_PHONENUMBER_LENGTH = 11;
    static final int MAX_REGISTRATION_NUMBER_LENGTH = 14;


    /**
     * MySQL 드라이버를 로드, 데이터베이스와 연결
     * @throws ClassNotFoundException JDBC 드라이버 로드 실패 시
     * @throws SQLException 데이터베이스 연결 실패 시
     */
    private LoginService() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://14.34.82.127:3306/dkuschema",
                    "dku",
                    "dkudku");
            if ((console = System.console()) == null)
                System.out.println("콘솔을 지원하지 않는 환경입니다.");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("LoginService 초기화 실패", e);
        }
    }
    
    /**
     * 회원가입 수행
     * @return 회원가입 성공 시 true, 취소 시 false
     * @throws SQLException 데이터베이스 오류 시
     */
    public boolean register() throws SQLException {
        String id, name, phoneNumber, registrationNumber, role;
        char[] password;
        while(true){
            id = console.readLine("ID를 입력하세요(" + MAX_ID_LENGTH + "자 이내)(취소: quit): ");
            if(id.equals("quit")) return false;
            if(id.length() > MAX_ID_LENGTH) System.out.println("ID는 " + MAX_ID_LENGTH + "자 이내로 입력해야 합니다.");
            else if(isIdExists(id)) System.out.println("이미 존재하는 ID입니다.");
            else break;
        }
        while(true) {
            password = console.readPassword("비밀번호를 입력하세요(" + MAX_PASSWORD_LENGTH + "자 이내): ");
            if(String.valueOf(password).equals("quit")) return false;
            if(password.length > MAX_PASSWORD_LENGTH) System.out.println("비밀번호는 " + MAX_PASSWORD_LENGTH + "자 이내로 입력해야 합니다.");
            else break;
        }
        while(true) {
            char[] confirmPassword = console.readPassword("비밀번호를 한 번 더 입력하세요: ");
            if(String.valueOf(password).equals("quit")) return false;
            if(!Arrays.equals(password, confirmPassword))
                System.out.println("일치하지 않습니다.");
            else break;
        }
        while(true) {
            name = console.readLine("이름을 입력하세요(" + MAX_NAME_LENGTH + "자 이내): ");
            if(name.equals("quit")) return false;
            if(name.length() > MAX_NAME_LENGTH) System.out.println("이름은 " + MAX_NAME_LENGTH + "자 이내로 입력해야 합니다.");
            else break;
        }
        while(true) {
            phoneNumber = console.readLine("전화번호를 입력하세요(\"-\" 제외, " + MAX_PHONENUMBER_LENGTH + "자 이내): ");
            if(phoneNumber.equals("quit")) return false;
            if(phoneNumber.length() > MAX_PHONENUMBER_LENGTH) System.out.println("전화번호는 " + MAX_PHONENUMBER_LENGTH + "자 이내로 입력해야 합니다.");
            else break;
        }
        while(true) {
            registrationNumber = console.readLine("주민등록번호를 입력하세요(\"-\" 포함, " + MAX_REGISTRATION_NUMBER_LENGTH + "자 이내): ");
            if(registrationNumber.equals("quit")) return false;
            if(registrationNumber.length() > MAX_REGISTRATION_NUMBER_LENGTH) System.out.println("주민등록번호는 " + MAX_REGISTRATION_NUMBER_LENGTH + "자 이내로 입력해야 합니다.");
            else break;
        }
        while(true) {
            String isAdmin = console.readLine("관리자입니까?[y/n]: ").toLowerCase();
            switch (isAdmin) {
                case "y" -> role = "ADMIN";
                case "n" -> role = "USER";
                case "quit" -> { return false; }
                default -> { continue; }
            }
            break;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO dkuschema.accounts (id, password, name, phoneNumber, registrationNumber, role) VALUES (?, ?, ?, ?, ?, ?)")
        ) {
            pstmt.setString(1, id);
            pstmt.setString(2, String.valueOf(password));
            pstmt.setString(3, name);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, registrationNumber);
            pstmt.setString(6, role);
            pstmt.executeUpdate();
        }
        System.out.println("회원가입이 완료되었습니다.");
        return true;
    }

    /**
     * 로그인 수행<br>
     * 로그인 성공 시 {@link Account} 객체 반환
     * @return 로그인 성공 시 {@link Account} 객체, 취소 시 null
     * @throws SQLException 데이터베이스 오류 시
     * @see Account
     */
    public Account login() throws SQLException {
        String id;
        char[] password;
        ResultSet rs;
        try (Statement stmt = conn.createStatement()) {
            while (true) {
                id = console.readLine("ID를 입력하세요(취소: quit): ");
                if (id.equals("quit")) return null;
                rs = stmt.executeQuery("SELECT * FROM dkuschema.accounts WHERE id = '" + id + "'");
                if (!rs.next()) System.out.println("존재하지 않는 ID입니다.");
                else break;
            }
            while (true) {
                password = console.readPassword("비밀번호를 입력하세요: ");
                if (String.valueOf(password).equals("quit")) return null;
                if (!rs.getString("password").equals(String.valueOf(password))) System.out.println("비밀번호가 틀렸습니다.");
                else break;
            }

            // --- #1 총 사용 금액 가져오기 ---
            double totalSpent = rs.getDouble("total_spent");

            // --- #2 현재 티어 계산 (팩토리 메서드 패턴) ---
            Tier newTier = TierFactory.create(totalSpent);

            // --- #3 DB에 저장된 이전 티어 ---
            Tier oldTier = TierFactory.fromString(rs.getString("tier"));

            System.out.println("[" + newTier.getName() + "] " + rs.getString("name") + "님 환영합니다");
            // --- 티어가 달라졌으면 DB 업데이트 ---
            if (!newTier.equals(oldTier)) {
                PreparedStatement updateTier = conn.prepareStatement(
                        "UPDATE dkuschema.accounts SET tier = ? WHERE id = ?"
                );
                updateTier.setString(1, newTier.getName());
                updateTier.setString(2, id);
                updateTier.executeUpdate();
            }

            rs.close();
        }
        return new Account(id, conn, console);
    }

    private boolean isIdExists(String id) throws SQLException {
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
            "SELECT * FROM dkuschema.accounts WHERE id = '" + id + "'")
        ) { return rs.next(); }
    }

}