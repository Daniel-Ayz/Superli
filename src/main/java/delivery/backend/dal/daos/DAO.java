package delivery.backend.dal.daos;

import delivery.backend.dal.dtos.DTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DAO {
    protected final String tableName;
    protected Connection conn;

    protected DAO(String tableName, Connection conn) {
        this.tableName = tableName;
        this.conn = conn;
    }

    /**
     * select query
     * @param query - query to execute
     * @return - result set
     * @throws SQLException
     */
    protected ResultSet select(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * insert or update query
     * @param query - query to execute
     * @return - number of rows affected
     * @throws SQLException
     */
    protected int update(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeUpdate(query);
    }

    /**
     * select all rows from table
     * @return - list of DTOs
     * @throws SQLException
     */
    public List<DTO> selectAll() throws SQLException {
        String query = "SELECT * FROM " + tableName;
        ResultSet rs = select(query);
        List<DTO> result = new ArrayList<>();
        while (rs.next()) {
            result.add(createDTO(rs));
        }
        return result;
    }

    /**
     * covert result set to DTO
     * @param rs - result set
     * @return - DTO
     * @throws SQLException
     */
    protected abstract DTO createDTO(ResultSet rs) throws SQLException;

    protected List<Integer> getIntegerList(ResultSet rs, String columnName) {
        List<Integer> result = new ArrayList<>();
        try {
            while (rs.next()) {
                result.add(rs.getInt(columnName));
            }
        } catch (SQLException e) {
            throw new RuntimeException("error in getIntegerList");
        }
        return result;
    }

    public void truncate() {
        String sql = "DELETE FROM " + tableName;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
