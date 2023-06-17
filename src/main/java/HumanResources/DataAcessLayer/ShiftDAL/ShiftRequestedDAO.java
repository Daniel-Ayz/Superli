package HumanResources.DataAcessLayer.ShiftDAL;

import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftRequestedDAO extends DAO {
    private static final String TABLE_NAME = "ShiftRequested";

    public ShiftRequestedDAO() {
        super(TABLE_NAME);
    }

    public ArrayList<Integer> getByShift(int shiftID) {
        ArrayList<Integer> employeeId = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetByShift(con, shiftID);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                employeeId.add(rs.getInt("employeeID"));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return employeeId;
    }

    private PreparedStatement createPreparedStatementGetByShift(Connection con, int shiftID) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE shiftID = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        return ps;
    }
    public ArrayList<Integer> getByEmployeeID(int employeeID) {
        ArrayList<Integer> shiftId = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetByEmployeeID(con, employeeID);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                shiftId.add(rs.getInt("shiftID"));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return shiftId;
    }

    private PreparedStatement createPreparedStatementGetByEmployeeID(Connection con, int employeeID) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE employeeID = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeID);
        return ps;
    }

    public boolean insert(int shiftID, int employeeID) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, shiftID,employeeID);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int shiftID, int employeeID) throws SQLException {
        String sql = String.format("INSERT INTO %s(shiftID,employeeID) VALUES(?,?)", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        ps.setInt(2, employeeID);
        return ps;
    }

    public Map<Integer, List<Integer>> selectAll(){
        Map<Integer, List<Integer>> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int shiftId = rs.getInt("shiftID");
                if (!result.containsKey(shiftId)){
                    List<Integer> list = new ArrayList<>();
                    result.put(shiftId, list);
                }
                result.get(shiftId).add(rs.getInt("employeeID"));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementSelectAll(Connection con) throws SQLException {
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }


    public boolean delete(int shiftID, int employeeID) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, shiftID, employeeID);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int shiftID, int employeeID) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE shiftID = ? AND employeeID = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        ps.setInt(2, employeeID);
        return ps;
    }
}
