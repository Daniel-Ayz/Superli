package HumanResources.DataAcessLayer.EmployeeDAL;

import HR_Delivery.LicenseType;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DriverDAO extends DAO {
    private static final String TABLE_NAME = "Driver";
    private static final String COLUMN_EMPLOYEE_ID = "employeeId";
    private static final String COLUMN_DRIVER_LICENSE = "driverLicense";

    public DriverDAO() {
        super(TABLE_NAME);
    }

    public LicenseType getById(int employeeId) {
        LicenseType result = null;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, employeeId);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                String driverLicense = rs.getString(COLUMN_DRIVER_LICENSE);
                result = LicenseType.valueOf(driverLicense);
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

    public boolean insert(int employeeId, LicenseType driverLicense) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, employeeId, driverLicense.toString());) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int employeeId, String driverLicense) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s,%s) VALUES(?,?)", tableName, COLUMN_EMPLOYEE_ID, COLUMN_DRIVER_LICENSE);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, driverLicense);
        return ps;
    }

    public Map<Integer, LicenseType> selectAll(){
        Map<Integer, LicenseType> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int employeeId = rs.getInt(COLUMN_EMPLOYEE_ID);
                String driverLicense = rs.getString(COLUMN_DRIVER_LICENSE);
                result.put(employeeId, LicenseType.valueOf(driverLicense));
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

    public boolean update(int employeeId, LicenseType driverLicense) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, employeeId, driverLicense.toString());) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int employeeId, String driverLicense) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, COLUMN_DRIVER_LICENSE, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, driverLicense);
        ps.setInt(2, employeeId);
        return ps;
    }

    public boolean delete(int employeeId) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, employeeId);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int employeeId) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        return ps;
    }

}
