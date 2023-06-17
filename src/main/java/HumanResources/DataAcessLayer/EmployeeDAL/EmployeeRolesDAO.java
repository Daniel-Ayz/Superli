package HumanResources.DataAcessLayer.EmployeeDAL;

import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeRolesDAO extends DAO {
    private static final String TABLE_NAME = "EmployeeRoles";
    private static final String COLUMN_EMPLOYEE_ID = "employeeId";
    private static final String COLUMN_ROLE = "role";

    public EmployeeRolesDAO() {
        super(TABLE_NAME);
    }

    public List<Role> getById(int employeeId) {
        List<Role> result = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, employeeId);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                String roleString = rs.getString(COLUMN_ROLE);
                Role role = Role.valueOf(roleString);
                result.add(role);
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementGetById(Connection con, int employeeId) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        return ps;
    }

    public boolean insert(int employeeId, Role role) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, employeeId, role.toString());) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int employeeId, String roleString) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s,%s) VALUES(?,?)", tableName, COLUMN_EMPLOYEE_ID, COLUMN_ROLE);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, roleString);
        return ps;
    }

    public Map<Integer, List<Role>> selectAll(){
        Map<Integer, List<Role>> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int employeeId = rs.getInt(COLUMN_EMPLOYEE_ID);
                String roleString = rs.getString(COLUMN_ROLE);
                Role role = Role.valueOf(roleString);
                if (!result.containsKey(employeeId)){
                    List<Role> list = new ArrayList<>();
                    result.put(employeeId, list);
                }
                result.get(employeeId).add(role);
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

    public boolean update(int employeeId, Role role) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, employeeId, role.toString());) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int employeeId, String roleString) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, COLUMN_ROLE, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, roleString);
        ps.setInt(2, employeeId);
        return ps;
    }

    public boolean delete(int employeeId, Role role) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, employeeId, role.toString());) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int employeeId, String roleString) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ? AND %s = ?", tableName, COLUMN_EMPLOYEE_ID, COLUMN_ROLE);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, roleString);
        return ps;
    }
}
