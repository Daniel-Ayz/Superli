package delivery.backend.dal.daos.shipment;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipment.ShipmentEstimationsDTO;

import java.sql.*;
import java.time.LocalTime;

public class ShipmentEstimationsDAO extends DAO {
    private static final String TABLE_NAME = "ShipmentEstimations";

    public ShipmentEstimationsDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentEstimationsDTO(
                rs.getInt("shipmentId"),
                rs.getInt("storeId"),
                LocalTime.parse(rs.getString("estimation")));
    }

    public void addShipmentEstimations(ShipmentEstimationsDTO shipmentEstimationsDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (shipmentId, storeId, estimation) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentEstimationsDTO.getShipmentId());
            pstmt.setInt(2, shipmentEstimationsDTO.getStoreId());
            pstmt.setString(3, shipmentEstimationsDTO.getEstimation().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void truncate(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE shipmentId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setEstimation(int shipmentId, int storeId, LocalTime value) {
        String sql = "UPDATE " + TABLE_NAME + " SET estimation = ? WHERE shipmentId = ? AND storeId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, value.toString());
            pstmt.setInt(2, shipmentId);
            pstmt.setInt(3, storeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("error in setEstimation: " + e.getMessage());
        }
    }

    public LocalTime getEstimatedArrivalTime(int shipmentId, int storeId) {
        String sql = "SELECT estimation FROM " + TABLE_NAME + " WHERE shipmentId = ? AND storeId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentId);
            pstmt.setInt(2, storeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return LocalTime.parse(rs.getString("estimation"));
            }
            else {
                throw new RuntimeException("error in getEstimatedArrivalTime: no such shipmentId and storeId");
            }
        } catch (Exception e) {
            throw new RuntimeException("error in getEstimatedArrivalTime: " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"ShipmentEstimations\" (\n" +
                "\t\"shipmentId\"\tINTEGER,\n" +
                "\t\"storeId\"\tINTEGER,\n" +
                "\t\"estimation\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"shipmentId\") REFERENCES \"Shipments\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"storeId\") REFERENCES \"Stores\"(\"id\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
