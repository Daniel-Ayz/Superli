package delivery.backend.dal.daos.shipmentFactory;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipmentFactory.SupplyLeftoverDTO;

import java.sql.*;

public class SupplyLeftoverDAO extends DAO {
    private static final String TABLE_NAME = "SupplyLeftovers";

    public SupplyLeftoverDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    public void addShortage(SupplyLeftoverDTO leftovers) {
        String sql = "INSERT INTO " + TABLE_NAME + " (providerId, itemId, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, leftovers.getProviderId());
            pstmt.setInt(2, leftovers.getItemId());
            pstmt.setInt(3, leftovers.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new SupplyLeftoverDTO(
                rs.getInt("providerId"),
                rs.getInt("itemId"),
                rs.getInt("amount"));
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"SupplyLeftovers\" (\n" +
                "\t\"providerId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"amount\"\tINTEGER,\n" +
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
