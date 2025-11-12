package rental;

import vehicle.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class VehicleRepository extends Connect{

    public List<PrintStructure> select(String type, Boolean status){
        List<PrintStructure> printStructures = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            if (type==(null)){
                String sql = "SELECT ROW_NUMBER() OVER(ORDER BY vehicle_id) AS RN, vehicle.* FROM "+TABLE_NAME+" WHERE vehicle_status = "+status;
                pstmt = conn.prepareStatement(sql);
            } else {
                String sql = "SELECT ROW_NUMBER() OVER(ORDER BY vehicle_id) AS RN, vehicle.* FROM "+TABLE_NAME+" WHERE vehicle_type = '"+type+"'";
                pstmt = conn.prepareStatement(sql);

            }

            rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No vehicles found");
                return null;
            } else {
                while (rs.next()) {
                    PrintStructure printStructure = new PrintStructure(
                            rs.getInt("RN"),
                            rs.getString("vehicle_id"),
                            rs.getString("vehicle_type"),
                            rs.getBoolean("vehicle_status")
                    );
                    printStructures.add(printStructure);
                }
                return printStructures;
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception Occurred: " + e.getMessage());
            System.err.println("Please ensure the MySQL server is running, the database 'dku' exists, " +
                    "the 'vehicle' table exists, and the MySQL Connector/J library is correctly configured in your project.");
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            return printStructures;
        }
    }
    public String[] update(String id, Boolean status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int updatedRows = 0;
        String[] rentalInfo=new String[]{null};

        try {
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);


            String updateSql = "UPDATE " + TABLE_NAME + " SET vehicle_status = ? WHERE vehicle_id = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setBoolean(1, status);
            pstmt.setString(2, id);

            System.out.println("Executing Return Query: " + updateSql.replace("?", "'" + id + "'"));
            updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("SUCCESS: " + id + " 차량의 상태를 '"+status+"'으로 변경했습니다.");
                rentalInfo = new String[]{id, String.valueOf(status)};
            } else {
                System.out.println("FAIL: ID " + id + "를 가진 차량을 찾을 수 없거나 상태가 변경되지 않았습니다.");
                rentalInfo = new String[]{id, String.valueOf(!status)};
                return null;
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception Occurred during return: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return rentalInfo;
    }
    public String[] Report(String id) {
        return update(id, false);
    }
    public String[] Repairing(String id) {
        return update(id, true);
    }
}