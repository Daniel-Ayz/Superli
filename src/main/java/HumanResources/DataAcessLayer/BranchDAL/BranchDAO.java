package HumanResources.DataAcessLayer.BranchDAL;

import HumanResources.BusinessLayer.BranchModule.Branch;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BranchDAO extends DAO {
    private static final String TABLE_NAME = "Branch";

    public BranchDAO() {
        super(TABLE_NAME);
    }

    public Branch getById(int branchId) {
        Branch result = null;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, branchId);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            result = new Branch(rs.getInt("branchId"),rs.getString("morningStartTime"),rs.getString("morningEndTime"),rs.getString("nightStartTime"),rs.getString("nightEndTime"));
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementGetById(Connection con, int branchId) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE branchId = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, branchId);
        return ps;
    }

    public boolean insert(int branchId, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, branchId, morningStartTime,morningEndTime,nightStartTime,nightEndTime);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int branchId, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) throws SQLException {
        String sql = String.format("INSERT INTO %s(branchId,morningStartTime,morningEndTime,nightStartTime,nightEndTime) VALUES(?,?,?,?,?)", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, branchId);
        ps.setString(2, morningStartTime);
        ps.setString(3, morningEndTime);
        ps.setString(4, nightStartTime);
        ps.setString(5, nightEndTime);
        return ps;
    }

    public ArrayList<Branch> selectAll(){
        ArrayList<Branch> branches= new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process branches
            while(rs.next()){
                branches.add(new Branch(rs.getInt("branchId"),rs.getString("morningStartTime"),rs.getString("morningEndTime"),rs.getString("nightStartTime"),rs.getString("nightEndTime")));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return branches
        return branches;
    }

    private PreparedStatement createPreparedStatementSelectAll(Connection con) throws SQLException {
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }

    public boolean update(int branchId, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, branchId, morningStartTime,morningEndTime,nightStartTime,nightEndTime);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int branchId, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) throws SQLException {
        String sql = String.format("UPDATE %s SET morningStartTime = ?, morningEndTime = ?, nightStartTime = ?, nightEndTime = ? WHERE branchId = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, morningStartTime);
        ps.setString(2, morningEndTime);
        ps.setString(3, nightStartTime);
        ps.setString(4, nightEndTime);
        ps.setInt(5, branchId);


        return ps;
    }

    public boolean delete(int branchId) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, branchId);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int branchId) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE branchId = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, branchId);
        return ps;
    }
}
