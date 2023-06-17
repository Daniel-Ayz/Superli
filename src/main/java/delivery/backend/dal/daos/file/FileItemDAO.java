package delivery.backend.dal.daos.file;

import delivery.backend.dal.daos.DAO;
import delivery.backend.dal.dtos.DTO;
import delivery.backend.dal.dtos.file.FileItemDTO;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class FileItemDAO extends DAO {
    private static final String TABLE_NAME = "FileItems";

    public FileItemDAO(Connection conn) {
        super(TABLE_NAME, conn);
    }

    @Override
    protected DTO createDTO(ResultSet rs) throws SQLException {
        return new FileItemDTO(
                rs.getInt("documentId"),
                rs.getInt("itemId"),
                rs.getInt("amount"));
    }

    public void addFileItem(FileItemDTO fileItemDTO) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO " + TABLE_NAME + " (documentId, itemId, amount) VALUES (?, ?, ?)");
            ps.setInt(1, fileItemDTO.getDocumentId());
            ps.setInt(2, fileItemDTO.getItemId());
            ps.setInt(3, fileItemDTO.getAmount());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw  new RuntimeException(e.getMessage());
        }
    }

    public List<FileItemDTO> getByDocumentedFileIds(List<Integer> documentIds) {
        try {
            return selectAll().stream()
                    .map(dto -> (FileItemDTO) dto)
                    .filter(dto -> documentIds.contains(dto.getDocumentId()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error in getByDocumentedFileIds(List<Integer> toList): " + e.getMessage());
        }
    }

    public List<FileItemDTO> getByDocumentId(int documentId) {
        try {
            return selectAll().stream()
                    .map(dto -> (FileItemDTO) dto)
                    .filter(dto -> dto.getDocumentId() == documentId)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error in getByDocumentId(int id): " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"FileItems\" (\n" +
                "\t\"documentId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"amount\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"itemId\") REFERENCES \"Items\"(\"id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"documentId\") REFERENCES \"DocumentedFiles\"(\"documentId\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
