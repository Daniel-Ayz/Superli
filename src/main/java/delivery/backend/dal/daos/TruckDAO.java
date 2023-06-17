package delivery.backend.dal.daos;

import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.TruckDTO;

import java.sql.*;

public class TruckDAO extends DAO {
    private static final String TABLE_NAME = "Trucks";

    public TruckDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    public void addTruck(TruckDTO truckDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (licenseNumber, model, weight, maxWeight, available, licenseType) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, truckDTO.getLicenseNumber());
            pstmt.setString(2, truckDTO.getModel());
            pstmt.setInt(3, truckDTO.getWeight());
            pstmt.setInt(4, truckDTO.getMaxWeight());
            pstmt.setBoolean(5, truckDTO.isAvailable());
            pstmt.setString(6, truckDTO.getLicenseType());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setAvailable(String licenseNumber, boolean value) {
        String sql = "UPDATE " + TABLE_NAME + " SET available = ? WHERE licenseNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, value);
            pstmt.setString(2, licenseNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new TruckDTO(
                rs.getString("licenseNumber"),
                rs.getString("model"),
                rs.getInt("weight"),
                rs.getInt("maxWeight"),
                rs.getBoolean("available"),
                rs.getString("licenseType"));
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"Trucks\" (\n" +
                "\t\"licenseNumber\"\tTEXT,\n" +
                "\t\"model\"\tTEXT,\n" +
                "\t\"weight\"\tINTEGER,\n" +
                "\t\"maxWeight\"\tINTEGER,\n" +
                "\t\"available\"\tINTEGER,\n" +
                "\t\"licenseType\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"licenseNumber\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
