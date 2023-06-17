package HumanResources.DataAcessLayer.ShiftDAL;

import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShiftRequiredDAO extends DAO {
    private static final String TABLE_NAME = "ShiftRequired";

    public ShiftRequiredDAO() {
        super(TABLE_NAME);
    }

    public ArrayList<Role> getByShift(int shiftID) {
        ArrayList<Role> roles = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetByShift(con, shiftID);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                String role = rs.getString("Role");
                roles.add(Role.valueOf(role));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return roles;
    }

    private PreparedStatement createPreparedStatementGetByShift(Connection con, int shiftID) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE shiftID = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        return ps;
    }
    public ArrayList<Integer> getByRole(String role) {
        ArrayList<Integer> shiftId = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetByRole(con, role);
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

    private PreparedStatement createPreparedStatementGetByRole(Connection con, String Role) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE Role = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, Role);
        return ps;
    }

    public boolean insert(int shiftID, String Role, int amount) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, shiftID,Role,amount);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int shiftID, String Role, int amount) throws SQLException {
        String sql = String.format("INSERT INTO %s(shiftID,Role,amount) VALUES(?,?,?)", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        ps.setString(2, Role);
        ps.setInt(3, amount);
        return ps;
    }

    public boolean update(int shiftID, String Role, int amount) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, shiftID,Role,amount);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int shiftID, String Role, int amount) throws SQLException {
        String sql = String.format("UPDATE %s SET amount = ? WHERE shiftID=? AND Role=?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, amount);
        ps.setInt(2, shiftID);
        ps.setString(3, Role);
        return ps;
    }

    public Map<Integer, Map<Role,Integer>> selectAll(){
        HashMap<Integer, Map<Role,Integer>> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int shiftId = rs.getInt("shiftID");
                String role = rs.getString("Role");
                int amount = rs.getInt("amount");
                Role role1 = Role.valueOf(role);
                if (!result.containsKey(shiftId)){
                    HashMap<Role,Integer> map = new HashMap<>();
                    result.put(shiftId, map);
                }
                result.get(shiftId).put(role1,amount);
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


    public boolean delete(int shiftID, String Role) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, shiftID, Role);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int shiftID, String Role) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE shiftID = ? AND Role = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        ps.setString(2, Role);
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
