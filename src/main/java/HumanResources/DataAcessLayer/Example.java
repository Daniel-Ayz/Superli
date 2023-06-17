package HumanResources.DataAcessLayer;

import java.sql.*;

public class Example extends DAO{
    private static final String TABLE_NAME = "example";

    public Example() {
        super(TABLE_NAME);
    }

    public String getById(int userId) {
        String result = "";
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, userId);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                result += rs.getString("Id");
                result += rs.getString("Name");
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementGetById(Connection con, int userId) throws SQLException {
        String sql = String.format("SELECT Id, Name FROM %s WHERE Id = ?", tableName);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps;
    }

    public boolean insert(int userId, String name) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, userId, name);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int userId, String name) throws SQLException {
        String sql = String.format("INSERT INTO %s(Id,Name) VALUES(?,?)", tableName);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, name);
        return ps;
    }

    public String selectAll(){
        String result = "";
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                result += rs.getString("Id");
                result += rs.getString("Name");
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementSelectAll(Connection con) throws SQLException {
        String sql = String.format("SELECT * FROM %s", tableName);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }

    public boolean update(int userId, String name) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, userId, name);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int userId, String name) throws SQLException {
        String sql = String.format("UPDATE %s SET Name = ? WHERE Id = ?", tableName);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, userId);
        return ps;
    }

    public boolean delete(int userId) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, userId);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int userId) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps;
    }






}
