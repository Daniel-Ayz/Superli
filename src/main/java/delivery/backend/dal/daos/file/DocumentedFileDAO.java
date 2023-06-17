package delivery.backend.dal.daos.file;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.file.DocumentedFileDTO;

import java.sql.*;
import java.util.List;

public class DocumentedFileDAO extends DAO {
    private static final String TABLE_NAME = "DocumentedFiles";

    public DocumentedFileDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new DocumentedFileDTO(
                rs.getInt("documentId"),
                rs.getInt("shipmentId"),
                rs.getInt("storeId"));
    }

    public void addDocumentedFile(DocumentedFileDTO documentedFileDTO) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO " + TABLE_NAME + " (documentId, shipmentId, StoreId) VALUES (?, ?, ?)");
            ps.setInt(1, documentedFileDTO.getDocumentId());
            ps.setInt(2, documentedFileDTO.getShipmentId());
            ps.setInt(3, documentedFileDTO.getStoreId());
            ps.executeUpdate();
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

    public List<DocumentedFileDTO> getByShipmentId(int shipmentId) {
        try {
            return selectAll().stream()
                    .map(dto -> (DocumentedFileDTO) dto)
                    .filter(dto -> dto.getShipmentId() == shipmentId)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error in getByShipmentId(int id): " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"DocumentedFiles\" (\n" +
                "\t\"documentId\"\tINTEGER,\n" +
                "\t\"shipmentId\"\tINTEGER,\n" +
                "\t\"storeId\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"storeId\") REFERENCES \"Stores\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"shipmentId\") REFERENCES \"Shipments\"(\"id\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"documentId\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
