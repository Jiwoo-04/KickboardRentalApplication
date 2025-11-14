package AccountManage.accountmanage;

import java.io.Console;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import AccountManage.accountmanage.tier.Tier;
import AccountManage.accountmanage.tier.TierFactory;

/**
 * 로그인한 사용자의 계정 정보를 관리<br>
 * 비밀번호 변경, 탈퇴, 로그아웃 수행<br>
 * {@link LoginService} 인스턴스의 login() 메서드로 생성
 * @see LoginService
 */
public class Account {
    final private String id;
    final private String name;
    private Role role;
    private int balance;
    private Tier tier;
    private int couponCount;
    final private Connection conn;
    Console console;

    Account(String id, Connection conn, Console console) throws SQLException {
        this.id = id;
        this.conn = conn;
        this.console = console;
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
            "SELECT * FROM dkuschema.accounts WHERE id = '" + id + "'")
        ) {
            rs.next();
            this.name = rs.getString("name");
            this.role = Role.valueOf(rs.getString("role"));
            this.balance = rs.getInt("balance");
            this.tier = TierFactory.fromString(rs.getString("tier"));
            this.couponCount = rs.getInt("couponCount");
        }
        // payment = stmt.executeQuery(
        //     "SELECT p.userId, a.name, p.vehicleId, p.time, p.fee, p.isCouponUsed, p.paidAt "
        //     + "FROM dkuschema.accounts a INNER JOIN dkuschema.payments p ON a.id = p.userId");
    }

    /**
     * 탈퇴 수행, 자원 해제
     * @return 탈퇴 성공 시 true, 취소 시 false
     * @throws SQLException 데이터베이스 오류 시
     */
    public boolean withdraw() throws SQLException {
        char[] password;
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
            "SELECT * FROM dkuschema.accounts WHERE id = '" + id + "'")
        ) {
            rs.next();
            while(true){
                password = console.readPassword("비밀번호를 입력하세요(취소: quit): ");
                if(String.valueOf(password).equals("quit")) return false;
                if(!rs.getString("password").equals(String.valueOf(password)))
                    System.out.println("비밀번호가 틀렸습니다.");
                else break;
            }
            stmt.executeUpdate("DELETE FROM dkuschema.accounts WHERE id = '" + id + "'");
        }
        System.out.println("탈퇴가 완료되었습니다.");
        return true;
    }

    /**
     * 비밀번호 변경 수행
     * @return 비밀번호 변경 성공 시 true, 취소 시 false
     * @throws SQLException 데이터베이스 오류 시
     */
    public boolean changePassword() throws SQLException {
        char[] password;
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
            "SELECT * FROM dkuschema.accounts WHERE id = '" + id + "'")
        ) {
            rs.next();
            while(true) {
                password = console.readPassword("현재 비밀번호를 입력하세요(취소: quit): ");
                if(String.valueOf(password).equals("quit")) return false;
                if(!rs.getString("password").equals(String.valueOf(password)))
                    System.out.println("비밀번호가 틀렸습니다.");
                else break;
            }
            while(true) {
                password = console.readPassword("새 비밀번호를 입력하세요(" + LoginService.MAX_PASSWORD_LENGTH + "자 이내): ");
                if(String.valueOf(password).equals("quit")) return false;
                if(password.length > LoginService.MAX_PASSWORD_LENGTH)
                    System.out.println("비밀번호는 " + LoginService.MAX_PASSWORD_LENGTH + "자 이내로 입력해야 합니다.");
                else break;
            }
            while(true) {
                char[] confirmPassword = console.readPassword("새 비밀번호를 한 번 더 입력하세요: ");
                if(String.valueOf(confirmPassword).equals("quit")) return false;
                if(!Arrays.equals(password, confirmPassword))
                    System.out.println("일치하지 않습니다.");
                else break;
            }
            stmt.executeUpdate("UPDATE dkuschema.accounts SET password = '"
                + String.valueOf(password) + "' WHERE id = '" + id + "'");
        }
        System.out.println("비밀번호가 변경되었습니다.");
        return true;
    }
    /**
     * 결제 내역 저장
     * @param vehicleId 장치 id
     * @param time 대여 시간(분)
     * @param fee 요금
     * @param isCouponUsed 쿠폰 사용 여부
     * @throws SQLException 데이터베이스 오류 시
     */
    public void savePaymentRecord(String vehicleId, int time, double fee, boolean isCouponUsed) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "INSERT INTO dkuschema.payments (userId, vehicleId, time, fee, isCouponUsed, paidAt) "
            + "VALUES ('" + id + "', '" + vehicleId + "', " + time + ", " + fee + ", " + isCouponUsed + ", NOW());");
        }
    }
    /**
     * @return 계정의 총 결제 금액
     * @throws SQLException 데이터베이스 오류 시
     */
    public int getPaymentTotal() throws SQLException {
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
            "SELECT SUM(fee) AS paymentTotal FROM dkuschema.payments WHERE userId = '" + id + "'")
        ) { rs.next(); return rs.getInt("paymentTotal"); }
    }
    /**
     * @return 계정의 ID
     * @throws SQLException 데이터베이스 오류 시
     */
    public String id() throws SQLException { return id; }
    /**
     * @return 계정의 이름
     * @throws SQLException 데이터베이스 오류 시
     */
    public String name() throws SQLException { return name; }
    /**
     * @return 계정의 권한
     * @throws SQLException 데이터베이스 오류 시
     * @see Role
     */
    public Role role() throws SQLException { return role; }
    /**
     * @return 계정의 잔액
     * @throws SQLException 데이터베이스 오류 시
     */
    public int balance() throws SQLException { return balance; }
    /**
     * @return 계정의 등급
     * @throws SQLException 데이터베이스 오류 시
     */
    public Tier tier() throws SQLException { return tier; }
    /**
     * @return 계정이 보유한 쿠폰 개수
     * @throws SQLException 데이터베이스 오류 시
     */
    public int couponCount() throws SQLException { return couponCount; }

    /**
     * 계정 권한 지정
     * @param newRole 새 권한
     * @throws SQLException 데이터베이스 오류 시
     * @see Role
     */
    public void setRole(Role newRole) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE dkuschema.accounts SET role = '"
            + newRole + "' WHERE id = '" + id + "'");
            this.role = newRole;
        }
    }
    /**
     * 계정 잔고 지정
     * @param newBalance 새 잔고
     * @throws SQLException 데이터베이스 오류 시
     */
    public void setBalance(int newBalance) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE dkuschema.accounts SET balance = '"
            + newBalance + "' WHERE id = '" + id + "'");
            this.balance = newBalance;
        }
    }
    /**
     * 계정 등급 지정
     * @param newTier 새 등급
     * @throws SQLException 데이터베이스 오류 시
     */
    public void setTier(Tier newTier) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE dkuschema.accounts SET tier = '" + newTier.getName() + "' WHERE id = '" + id + "'");
            this.tier = newTier;
        }
    }
    /**
     * 계정 쿠폰 개수 지정
     * @param newCouponCount 새 쿠폰 개수
     * @throws SQLException 데이터베이스 오류 시
     */
    public void setCouponCount(int newCouponCount) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE dkuschema.accounts SET couponCount = '"
            + newCouponCount + "' WHERE id = '" + id + "'");
            this.couponCount = newCouponCount;
        }
    }

}