package delivery.backend.dal.daos.shipment;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipment.ShipmentStoreItemDTO;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ShipmentStoreItemDAO extends DAO {
    private static final String TABLE_NAME = "ShipmentStoreItems";

    public ShipmentStoreItemDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentStoreItemDTO(
                rs.getInt("shipmentId"),
                rs.getInt("storeId"),
                rs.getInt("itemId"),
                rs.getInt("amount"));
    }

    public void addShipmentStoreItem(ShipmentStoreItemDTO shipmentStoreItemDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (shipmentId, storeId, itemId, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentStoreItemDTO.getShipmentId());
            pstmt.setInt(2, shipmentStoreItemDTO.getStoreId());
            pstmt.setInt(3, shipmentStoreItemDTO.getItemId());
            pstmt.setInt(4, shipmentStoreItemDTO.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void truncate(int shipmentId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE shipmentId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ShipmentStoreItemDTO> getByShipmentId(int id) {
        try {
            return selectAll().stream()
                    .map(dto -> (ShipmentStoreItemDTO) dto)
                    .filter(dto -> dto.getShipmentId() == id)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error in getByShipmentId(int id): " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"ShipmentStoreItems\" (\n" +
                "\t\"shipmentId\"\tINTEGER,\n" +
                "\t\"storeId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"amount\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"shipmentId\") REFERENCES \"Shipments\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"storeId\") REFERENCES \"Stores\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"itemId\") REFERENCES \"Items\"(\"id\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
