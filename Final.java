import AccountManage.accountmanage.Account;
import AccountManage.accountmanage.LoginService;
import rental.Rental;
import vehicle.Bike;
import vehicle.Scooter;
import vehicle.Vehicle;
import payment.PaymentHistoryViewer;

import java.io.Console;
import java.sql.SQLException;
import java.util.Scanner;

public class Final {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        LoginService loginService = new LoginService();
        Account account;
        Scanner sc = new Scanner(System.in);
        Console console = System.console();

        while (true) {
            System.out.println("\n==== 메인 메뉴 ====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");
            System.out.print("선택: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }

            switch (choice) {
                case 1 -> {
                    loginService.register();
                    continue;
                }
                case 2 -> {
                    account = loginService.login();
                    if (account == null) continue;
                }
                case 3 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> {
                    continue;
                }
            }

            // 로그인 성공 시 사용자 메뉴
            while (true) {
                System.out.println("\n==== 사용자 메뉴 ====");
                System.out.println("1. 탈것 대여");
                System.out.println("2. 결제 내역 조회");
                System.out.println("3. 로그아웃");
                System.out.print("선택: ");

                int userChoice;
                try {
                    userChoice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    continue;
                }

                switch (userChoice) {
                    case 1 -> {
                        // ✅ 1단계: 탈것 종류 선택
                        System.out.println("\n[대여 선택]");
                        System.out.println("1. 자전거");
                        System.out.println("2. 스쿠터");
                        System.out.print("선택: ");
                        int vehicleChoice = Integer.parseInt(sc.nextLine());

                        // ✅ 2단계: 개별 차량 선택
                        Vehicle vehicle = null;
                        if (vehicleChoice == 1) {
                            System.out.println("\n[자전거 선택]");
                            System.out.println("1. Bike-01");
                            System.out.println("2. Bike-02");
                            System.out.println("3. Bike-03");
                            System.out.print("선택: ");
                            int bikeNum = Integer.parseInt(sc.nextLine());
                            vehicle = new Bike("Bike-0" + bikeNum);

                        } else if (vehicleChoice == 2) {
                            System.out.println("\n[스쿠터 선택]");
                            System.out.println("1. Scooter-01");
                            System.out.println("2. Scooter-02");
                            System.out.println("3. Scooter-03");
                            System.out.print("선택: ");
                            int scooterNum = Integer.parseInt(sc.nextLine());
                            vehicle = new Scooter("Scooter-0" + scooterNum);

                        } else {
                            System.out.println("잘못된 선택입니다.");
                            continue;
                        }

                        // 주행 시간 입력
                        System.out.print("주행 시간(분): ");
                        int minutes = Integer.parseInt(sc.nextLine());

                        System.out.print("쿠폰을 사용하시겠습니까? (y/n): ");
                        boolean useCoupon = sc.nextLine().equalsIgnoreCase("y");

                        // ✅ null 방지: account 객체에서 정보 확인
                        if (account.getId() == null || account.getName() == null) {
                            System.out.println("⚠ 사용자 정보가 올바르지 않습니다. 다시 로그인해주세요.");
                            continue;
                        }

                        // ✅ 요금 계산 (Rental + payment)
                        Rental rental = new Rental(vehicle, minutes);
                        double total = rental.processPayment(useCoupon, account.getId(), account.getName());

                        System.out.println("\n✅ 결제가 완료되었습니다!");
                        System.out.printf("이용자: %s (%s)\n", account.getName(), account.getId());
                        System.out.printf("이용 수단: %s\n", vehicle.getType());
                        System.out.printf("이용 시간: %d분\n", minutes);
                        System.out.printf("총 결제 금액: %.2f원\n", total);
                    }

                    case 2 -> {
                        System.out.println("\n==== 결제 내역 조회 ====");
                        // ✅ PaymentHistoryViewer 내부에서 header 중복 방지 및 null 필터링 필요
                        PaymentHistoryViewer.viewPaymentHistory();
                    }

                    case 3 -> {
                        System.out.println("로그아웃합니다.\n");
                        account.logout();
                        break;
                    }

                    default -> System.out.println("잘못된 입력입니다.");
                }

                if (userChoice == 3) break;
            }
        }
    }
}
