import AccountManage.accountmanage.Account;
import AccountManage.accountmanage.LoginService;
import rental.*;
import vehicle.*;
import payment.PaymentHistoryViewer;

import java.io.Console;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Final {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
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
                    LoginService.INSTANCE.register();
                    continue;
                }
                case 2 -> {
                    account = LoginService.INSTANCE.login();
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
                System.out.println("4. 수리");
                System.out.println("5. 신고");
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
                        List<PrintStructure> p_vehicles;
                        Vehicle vehicle=null;
                        VehicleRepository  vehicleRepository = new VehicleRepository();
                        String[] id_type;

                        if (vehicleChoice == 1) {
                            System.out.println("\n[자전거 선택]");
                            p_vehicles = vehicleRepository.select("bike", null);
                        } else if (vehicleChoice == 2) {
                            System.out.println("\n[스쿠터 선택]");
                            p_vehicles = vehicleRepository.select("scooter", null);

                        } else {
                            System.out.println("잘못된 선택입니다.");
                            continue;
                        }

                        for (PrintStructure value : p_vehicles) {
                            System.out.println(value.getRN() + " ID: " + value.getId() +
                                    ", Type: " + value.getType() + ", Status: " + value.getStatus());
                        }

                        System.out.print("선택: ");
                        int vehicle_num = Integer.parseInt(sc.nextLine());
                        if (vehicle_num>p_vehicles.size()||vehicle_num<=0) {
                            continue;
                        }
                        String cid=p_vehicles.get(vehicle_num-1).getId();
                        String ctype=p_vehicles.get(vehicle_num-1).getType();

                        id_type = vehicleRepository.update(cid, false);
                        if (id_type==null){
                            System.out.println("사용불가 기기입니다.");
                            continue;
                        }
                        if (ctype.equals("scooter")){vehicle = new Scooter(cid);}
                        else if (ctype.equals("bike")){ vehicle = new Bike(cid);}
                        // 주행 시간 입력
                        System.out.print("주행 시간(분): ");
                        int minutes = Integer.parseInt(sc.nextLine());

                        System.out.print("쿠폰을 사용하시겠습니까? (y/n): ");
                        boolean useCoupon = sc.nextLine().equalsIgnoreCase("y");

                        // ✅ null 방지: account 객체에서 정보 확인
                        if (account.id() == null || account.name() == null) {
                            System.out.println("⚠ 사용자 정보가 올바르지 않습니다. 다시 로그인해주세요.");
                            continue;
                        }

                        // ✅ 요금 계산 (Rental + payment)
                        Rental rental = new Rental(vehicle, minutes);
                        double total = rental.processPayment(useCoupon, account.id(), account.name());
                        vehicleRepository.update(id_type[0], true);
                        // MySQL에 결제 내역 기록
                        account.savePaymentRecord(cid, minutes, total, useCoupon);

                        System.out.println("\n✅ 결제가 완료되었습니다!");
                        System.out.printf("이용자: %s (%s)\n", account.name(), account.id());
                        System.out.printf("이용 수단: %s\n", vehicle.getType());
                        System.out.printf("이용 시간: %d분\n", minutes);
                        System.out.printf("총 결제 금액: %.2f원\n", total);
                    }

                    case 2 -> {
                        System.out.println("\n==== 결제 내역 조회 ====");

                        if (account != null) {
                            PaymentHistoryViewer.viewPaymentHistory(account.id());
                        } else {
                            System.out.println("[오류] 로그인 정보가 없습니다. 먼저 로그인해주세요.");
                        }
                    }

                    case 3 -> {
                        System.out.println("로그아웃합니다.\n");
                        //account.logout();     deprecated
                        break;
                    }

                    case 4 -> {
                        List<PrintStructure> vehicles;
                        System.out.println("장비를 수리합니다.");
                        VehicleRepository  vehicleRepository = new VehicleRepository();
                        vehicles=vehicleRepository.select(null, false);
                        String[] resultInfo=new String[]{null};

                        if (vehicles.isEmpty()){
                            System.out.println("수리할 장비가 없습니다.");
                            continue;
                        }
                        for (PrintStructure value : vehicles) {
                            System.out.println(value.getRN() + " ID: " + value.getId() +
                                    ", Type: " + value.getType() + ", Status: " + value.getStatus());
                        }
                        System.out.println("수리하고자 하는 장비를 선택하여주십시오");
                        System.out.printf("입력 : ");
                        int repair_num = Integer.parseInt(sc.nextLine());
                        if  (repair_num>vehicles.size()||repair_num<=0) {
                            System.out.println("out of bounds.");
                            continue;
                        }
                        String cid=vehicles.get(repair_num-1).getId();
                        resultInfo=vehicleRepository.Repairing(cid);
                    }
                    case 5 -> {
                        List<PrintStructure> vehicles;
                        System.out.println("장비 고장 신고");
                        VehicleRepository  vehicleRepository = new VehicleRepository();
                        vehicles=vehicleRepository.select(null, true);
                        String[] resultInfo=new String[]{null};
                        if (vehicles==null){
                            System.out.println("신고할 수 있는 장비가 없습니다.");
                            continue;
                        }
                        for (PrintStructure value : vehicles) {
                            System.out.println(value.getRN() + " ID: " + value.getId() +
                                    ", Type: " + value.getType() + ", Status: " + value.getStatus());
                        }
                        System.out.println("신고하고자 하는 장비를 선택하여주십시오");
                        System.out.printf("입력 : ");
                        int report_num = Integer.parseInt(sc.nextLine());
                        if (report_num>vehicles.size()||report_num<=0) {
                            System.out.println("out of bounds.");
                            continue;
                        }
                        String cid=vehicles.get(report_num-1).getId();
                        resultInfo=vehicleRepository.Report(cid);
                    }
                    default -> System.out.println("잘못된 입력입니다.");
                }

                if (userChoice == 3) break;
            }
        }
    }
}
