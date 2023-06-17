package delivery.backend.dal.daos.shipment;

import delivery.backend.businessLayer.shipment.Treatment;
import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipment.ShipmentDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShipmentDAO extends DAO {
    private static final String TABLE_NAME = "Shipments";

    public ShipmentDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentDTO(
                rs.getInt("id"),
                LocalDate.parse(rs.getString("date")),
                LocalTime.parse(rs.getString("timeOfExit")),
                rs.getString("truckLicenseNumber"),
                rs.getString("driverName"),
                rs.getInt("sourceId"),
                rs.getInt("weight"),
                rs.getBoolean("isActive"),
                rs.getBoolean("overWeight"),
                rs.getString("treatment"));
    }

    public void setWeight(int id, int weight) {
        String sql = "UPDATE " + TABLE_NAME + " SET weight = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, weight);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setOverWeight(int id, boolean overWeight) {
        String sql = "UPDATE " + TABLE_NAME + " SET overWeight = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, overWeight);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTreatment(int id, Treatment treatment) {
        String sql = "UPDATE " + TABLE_NAME + " SET treatment = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, treatment.toString());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setDriverName(int id, String driverName) {
        String sql = "UPDATE " + TABLE_NAME + " SET driverName = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, driverName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setDriverId(int id, String driverId) {
        String sql = "UPDATE " + TABLE_NAME + " SET driverId = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, driverId);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTruckLicenseNumber(int id, String licenseNumber) {
        String sql = "UPDATE " + TABLE_NAME + " SET truckLicenseNumber = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, licenseNumber);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addShipment(ShipmentDTO shipmentDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (id, date, timeOfExit, truckLicenseNumber, driverName, sourceId, weight, isActive, overWeight, treatment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentDTO.getId());
            pstmt.setString(2, shipmentDTO.getDate().toString());
            pstmt.setString(3, shipmentDTO.getTimeOfExit().toString());
            pstmt.setString(4, shipmentDTO.getTruckLicenseNumber());
            pstmt.setString(5, shipmentDTO.getDriverName());
            pstmt.setInt(6, shipmentDTO.getSourceId());
            pstmt.setInt(7, shipmentDTO.getWeight());
            pstmt.setBoolean(8, shipmentDTO.isActive());
            pstmt.setBoolean(9, shipmentDTO.isOverWeight());
            pstmt.setString(10, shipmentDTO.getTreatment().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    public void setIsActive(int id, boolean isActive) {
        String sql = "UPDATE " + TABLE_NAME + " SET isActive = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable() {
String sql = "CREATE TABLE IF NOT EXISTS \"Shipments\" (\n" +
        "\t\"id\"\tINTEGER,\n" +
        "\t\"date\"\tTEXT,\n" +
        "\t\"timeOfExit\"\tTEXT,\n" +
        "\t\"truckLicenseNumber\"\tTEXT,\n" +
        "\t\"driverName\"\tTEXT,\n" +
        "\t\"sourceId\"\tINTEGER,\n" +
        "\t\"weight\"\tINTEGER,\n" +
        "\t\"isActive\"\tINTEGER,\n" +
        "\t\"overWeight\"\tINTEGER,\n" +
        "\t\"treatment\"\tINTEGER,\n" +
        "\tPRIMARY KEY(\"id\"),\n" +
        "\tFOREIGN KEY(\"sourceId\") REFERENCES \"Providers\"(\"id\") ON DELETE CASCADE,\n" +
        "\tFOREIGN KEY(\"truckLicenseNumber\") REFERENCES \"Trucks\"(\"licenseNumber\") ON DELETE CASCADE\n" +
        ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
