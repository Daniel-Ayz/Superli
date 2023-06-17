package delivery.backend.dal.daos.shipmentFactory;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipmentFactory.ShipmentFactoryDTO;

import java.sql.*;

public class ShipmentFactoryDAO extends DAO {
    private static final String TABLE_NAME = "ShipmentFactories";

    public ShipmentFactoryDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ShipmentFactoryDTO(
                rs.getInt("documentIdGenerator"));
    }

    public void updateDocumentIdGenerator(int i) {
        String sql = "UPDATE " + TABLE_NAME + " SET documentIdGenerator = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, i);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getDocumentIdGenerator() {
        String sql = "SELECT documentIdGenerator FROM " + TABLE_NAME;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return rs.getInt("documentIdGenerator");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setDocumentIdGenerator() {
        String sql = "INSERT INTO " + TABLE_NAME + " (documentIdGenerator) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"ShipmentFactories\" (\n" +
                "\t\"documentIdGenerator\"\tINTEGER\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
