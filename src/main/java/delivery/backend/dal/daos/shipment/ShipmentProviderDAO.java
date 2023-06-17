package delivery.backend.dal.daos.shipment;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipment.ShipmentProviderDTO;

import java.sql.*;
import java.util.List;

public class ShipmentProviderDAO extends DAO {
    private static final String TABLE_NAME = "ShipmentProviders";

    public ShipmentProviderDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentProviderDTO(
                rs.getInt("shipmentId"),
                rs.getInt("providerId"),
                rs.getBoolean("visited"));
    }

    public void addShipmentProvider(ShipmentProviderDTO shipmentProviderDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (shipmentId, providerId, visited) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentProviderDTO.getShipmentId());
            pstmt.setInt(2, shipmentProviderDTO.getProviderId());
            pstmt.setBoolean(3, shipmentProviderDTO.getVisited());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setVisited(int shipmentId, int supplierId, boolean value) {
        String sql = "UPDATE " + TABLE_NAME + " SET visited = ? WHERE shipmentId = ? AND providerId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, value);
            pstmt.setInt(2, shipmentId);
            pstmt.setInt(3, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    public List<Integer> getProviderIdsOfShipment(int id) {
        String sql = "SELECT providerId FROM " + TABLE_NAME + " WHERE shipmentId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return getIntegerList(rs, "providerId");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean getProviderPassedThrough(int shipmentId, int providerId) {
        String sql = "SELECT visited FROM " + TABLE_NAME + " WHERE shipmentId = ? AND providerId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, shipmentId);
            pstmt.setInt(2, providerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getBoolean("visited");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"ShipmentProviders\" (\n" +
                "\t\"shipmentId\"\tINTEGER,\n" +
                "\t\"providerId\"\tINTEGER,\n" +
                "\t\"visited\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"providerId\") REFERENCES \"Providers\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"shipmentId\") REFERENCES \"Shipments\"(\"id\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
