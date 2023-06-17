package delivery.backend.dal.daos.destination;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.destination.ProviderDTO;

import java.sql.*;

public class ProviderDAO extends DAO {
    private static final String TABLE_NAME = "Providers";

    public ProviderDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ProviderDTO(
                rs.getInt("id"),
                rs.getString("address"),
                rs.getString("shipmentArea"),
                rs.getString("phone"),
                rs.getString("contactName"),
                rs.getInt("northCoordinate"),
                rs.getInt("eastCoordinate"));
    }


    public void addProvider(ProviderDTO providerDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (id, address, shipmentArea, phone, contactName, northCoordinate, eastCoordinate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, providerDTO.getId());
            pstmt.setString(2, providerDTO.getAddress());
            pstmt.setString(3, providerDTO.getShipmentArea());
            pstmt.setString(4, providerDTO.getPhone());
            pstmt.setString(5, providerDTO.getContactName());
            pstmt.setInt(6, providerDTO.getNorthCoordinate());
            pstmt.setInt(7, providerDTO.getEastCoordinate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"Providers\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"address\"\tTEXT,\n" +
                "\t\"shipmentArea\"\tTEXT,\n" +
                "\t\"phone\"\tTEXT,\n" +
                "\t\"contactName\"\tTEXT,\n" +
                "\t\"northCoordinate\"\tINTEGER,\n" +
                "\t\"eastCoordinate\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
