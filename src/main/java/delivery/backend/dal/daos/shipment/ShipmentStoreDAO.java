package delivery.backend.dal.daos.shipment;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipment.ShipmentStoreDTO;

import java.sql.*;
import java.util.List;

public class ShipmentStoreDAO extends DAO {
    private static final String TABLE_NAME = "ShipmentStores";

    public ShipmentStoreDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentStoreDTO(
                rs.getInt("shipmentId"),
                rs.getInt("storeId"));
    }

    public void addShipmentStore(ShipmentStoreDTO shipmentStoreDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (shipmentId, storeId) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentStoreDTO.getShipmentId());
            pstmt.setInt(2, shipmentStoreDTO.getStoreId());
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

    public List<Integer> getStoreIdsOfShipment(int id) {
        String sql = "SELECT storeId FROM " + TABLE_NAME + " WHERE shipmentId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return getIntegerList(rs, "storeId");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"ShipmentStores\" (\n" +
                "\t\"shipmentId\"\tINTEGER,\n" +
                "\t\"storeId\"\tINTEGER,\n" +
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
