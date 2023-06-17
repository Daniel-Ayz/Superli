package delivery.backend.dal.daos;

import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.ItemDTO;

import java.sql.*;

public class ItemDAO extends DAO {
    private static final String TABLE_NAME = "Items";

    public ItemDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new ItemDTO(
                rs.getInt("id"),
                rs.getString("name"));
    }

    public void addItem(ItemDTO itemDTO) {
        String sql = "INSERT INTO " + TABLE_NAME + " (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemDTO.getId());
            pstmt.setString(2, itemDTO.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"Items\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
