package delivery.backend.dal.daos.shipment;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipment.ShipmentProviderItemDTO;

import java.sql.*;
import java.util.List;

public class ShipmentProviderItemDAO extends DAO {
    private static final String TABLE_NAME = "ShipmentProviderItems";

    public ShipmentProviderItemDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentProviderItemDTO(
                rs.getInt("shipmentId"),
                rs.getInt("providerId"),
                rs.getInt("itemId"),
                rs.getInt("amount"));
    }

    public void addShipmentProviderItem(ShipmentProviderItemDTO shipmentProviderItemDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (shipmentId, providerId, itemId, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentProviderItemDTO.getShipmentId());
            pstmt.setInt(2, shipmentProviderItemDTO.getProviderId());
            pstmt.setInt(3, shipmentProviderItemDTO.getItemId());
            pstmt.setInt(4, shipmentProviderItemDTO.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ShipmentProviderItemDTO> getByShipmentId(int id) {
        try {
            return selectAll().stream()
                    .map(dto -> (ShipmentProviderItemDTO) dto)
                    .filter(dto -> dto.getShipmentId() == id)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error in getByShipmentId(int id): " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"ShipmentProviderItems\" (\n" +
                "\t\"shipmentId\"\tINTEGER,\n" +
                "\t\"providerId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"amount\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"shipmentId\") REFERENCES \"Shipments\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"providerId\") REFERENCES \"Providers\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"itemId\") REFERENCES \"Items\"(\"id\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
