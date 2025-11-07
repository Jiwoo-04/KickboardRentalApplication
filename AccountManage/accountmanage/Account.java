package AccountManage.accountmanage;

import java.io.Console;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 로그인한 사용자의 계정 정보를 관리<br>
 * 비밀번호 변경, 탈퇴, 로그아웃 수행<br>
 * {@link LoginService} 인스턴스의 login() 메서드로 생성
 * @see LoginService
 */
public class Account implements AutoCloseable{
    private PreparedStatement pstmt;
    private ResultSet rs;
    //id와 name을 반환받기 위한 코드 추가 (심지승)
    private String id;
    private String name;
    Console console;

    Account(PreparedStatement pstmt, ResultSet rs) throws SQLException {
        this.pstmt = pstmt;
        this.rs = rs;
        this.id = rs.getString("id");
        this.name = rs.getString("name");
        if((console = System.console()) == null) System.out.println("콘솔을 지원하지 않는 환경입니다.");
    }


    /**
     * 자동 자원 해제<br>
     * 호출 시 기능 수행 불가
     * @throws SQLException 데이터베이스 오류 시
     */
    @Override
    public void close() throws SQLException { pstmt.close(); rs.close(); }

    /**
     * 로그아웃 수행, 자원 해제
     * @throws SQLException 데이터베이스 오류 시
     */
    public void logout() throws SQLException { close(); }

    /**
     * 탈퇴 수행, 자원 해제
     * @return 탈퇴 성공 시 true, 취소 시 false
     * @throws SQLException 데이터베이스 오류 시
     */
    public boolean withdraw() throws SQLException {
        char[] password;
        while(true){
            password = console.readPassword("비밀번호를 입력하세요(취소: quit): ");
            if(String.valueOf(password).equals("quit")) return false;
            if(!rs.getString("password").equals(String.valueOf(password)))
                System.out.println("비밀번호가 틀렸습니다.");
            else break;
        }
        pstmt.executeUpdate();
        close();
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
        rs.updateString("password", String.valueOf(password));
        rs.updateRow();
        System.out.println("비밀번호가 변경되었습니다.");
        return true;
    }

    // ✅ ID 반환 메서드
    public String getId() {
        return id;
    }

    // ✅ 이름 반환 메서드
    public String getName() {
        return name;
    }

    /**
     * 계정의 ID 반환
     * @return 계정의 ID
     * @throws SQLException 데이터베이스 오류 시
     */
    public String id() throws SQLException { return rs.getString("id"); }
    /**
     * 계정의 이름 반환
     * @return 계정의 이름
     * @throws SQLException 데이터베이스 오류 시
     */
    public String name() throws SQLException { return rs.getString("name"); }
    /**
     * 계정의 전화번호 반환
     * @return 계정의 전화번호
     * @throws SQLException 데이터베이스 오류 시
     */
    public String phoneNumber() throws SQLException { return rs.getString("phoneNumber"); }
    /**
     * 계정의 주민등록번호 반환
     * @return 계정의 주민등록번호
     * @throws SQLException 데이터베이스 오류 시
     */
    public String registrationNumber() throws SQLException { return rs.getString("registrationNumber"); }
    /**
     * 계정의 권한 반환
     * @return 계정의 권한
     * @throws SQLException 데이터베이스 오류 시
     * @see Role
     */
    public Role role() throws SQLException { return Role.valueOf(rs.getString("role")); }
    /**
     * 계정 이름 변경
     * @param newName 새 이름
     * @throws SQLException 데이터베이스 오류 시
     */
    public void setName(String newName) throws SQLException {
        rs.updateString("name", newName);
        rs.updateRow();
    }
    /**
     * 계정 전화번호 변경
     * @param newPhoneNumber 새 전화번호
     * @throws SQLException 데이터베이스 오류 시
     */
    public void setPhoneNumber(String newPhoneNumber) throws SQLException {
        rs.updateString("phoneNumber", newPhoneNumber);
        rs.updateRow();
    }
    /**
     * 계정 권한 변경
     * @param newRole 새 권한
     * @throws SQLException 데이터베이스 오류 시
     * @see Role
     */
    public void setRole(Role newRole) throws SQLException {
        rs.updateString("role", newRole.toString());
        rs.updateRow();
    }
}