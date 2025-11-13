package AccountManage;

import AccountManage.accountmanage.Account;
import AccountManage.accountmanage.LoginService;

import java.io.Console;
import java.sql.SQLException;

public class Demo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Console console = System.console();
        Account account;
        while(true) {
            int choice;
            try{
                choice = Integer.parseInt(console.readLine("1. 회원가입   2. 로그인   3. 종료\n선택: "));
            } catch (NumberFormatException e) { continue; }
            switch(choice) {
                case 1 -> { LoginService.INSTANCE.register(); continue; }
                case 2 -> {}
                case 3 -> { System.out.println("프로그램 종료"); return; }
                default -> { continue; }
            }
            if((account = LoginService.INSTANCE.login()) == null) continue;
            while(true) {
                int accountChoice;
                try{
                    accountChoice = Integer.parseInt(console.readLine("1. 비밀번호 변경   2. 탈퇴   3. 로그아웃\n선택: "));
                } catch (NumberFormatException e) { continue; }
                switch(accountChoice) {
                    case 1 -> { account.changePassword(); continue; }
                    case 2 -> { if(!account.withdraw()) continue; }
                    // case 3 -> account.logout();
                    default -> { continue; }
                }
                break;
            }
        }
    }
}