package delivery.backend.dal.daos.shipmentFactory;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.shipmentFactory.StockLeftoverDTO;

import java.sql.*;

public class StockLeftoverDAO extends DAO {
    private static final String TABLE_NAME = "StockLeftovers";

    public StockLeftoverDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    public void addShortage(StockLeftoverDTO leftovers) {
        String sql = "INSERT INTO " + TABLE_NAME + " (storeId, itemId, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, leftovers.getStoreId());
            pstmt.setInt(2, leftovers.getItemId());
            pstmt.setInt(3, leftovers.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new StockLeftoverDTO(
                rs.getInt("storeId"),
                rs.getInt("itemId"),
                rs.getInt("amount"));
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"StockLeftovers\" (\n" +
                "\t\"storeId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"amount\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"itemId\") REFERENCES \"Items\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"storeId\") REFERENCES \"Stores\"(\"id\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
