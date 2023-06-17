package HumanResources.DataAcessLayer.ShiftDAL;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.EmployeeModule.Salary;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class ShiftApprovedDAO extends DAO{
    private static final String TABLE_NAME = "ShiftApproved";

    public ShiftApprovedDAO() {
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

    public boolean insert(int shiftID, int employeeID, String role) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, shiftID,employeeID,role);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int shiftID, int employeeID,String role) throws SQLException {
        String sql = String.format("INSERT INTO %s(shiftID,employeeID,role) VALUES(?,?,?)", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        ps.setInt(2, employeeID);
        ps.setString(3, role);
        return ps;
    }

    public Map<Integer, Map<Integer,Role>> selectAll(){
        Map<Integer, Map<Integer,Role>> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int shiftId = rs.getInt("shiftID");
                if (!result.containsKey(shiftId)){
                    Map<Integer,Role> map = new HashMap<>();
                    result.put(shiftId, map);
                }
                int employeeId = rs.getInt("employeeID");
                String role = rs.getString("role");
                result.get(shiftId).put(employeeId,getRole(role));
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
    private Role getRole(String role){
        Role role1 = null;
        switch(role){
            case "MANAGER":
                role1 = Role.MANAGER;
                break;
            case "CASHIER":
                role1 = Role.CASHIER;
                break;
            case "STOREKEEPER":
                role1 = Role.STOREKEEPER;
                break;
            case "GENERAL_EMPLOYEE":
                role1 = Role.GENERAL_EMPLOYEE;
                break;
            case "SECURITY":
                role1 = Role.SECURITY;
                break;
            case "CLEANER":
                role1 = Role.CLEANER;
                break;
            case "STEWARD":
                role1 = Role.STEWARD;
                break;
        }
        return role1;
    }
}
