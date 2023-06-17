package HumanResources.DataAcessLayer.EmployeeDAL;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TODO: id in db is int but in Employee class is String
public class EmployeeDAO extends DAO {
    private static final String TABLE_NAME = "Employee";
    private static final String COLUMN_EMPLOYEE_ID = "employeeId";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PERSONAL_INFO = "personalInfo";
    private static final String COLUMN_BASE_SALARY = "baseSalary";
    private static final String COLUMN_PASSWORD = "password";

    public EmployeeDAO() {
        super(TABLE_NAME);
    }

    public Employee getById(int employeeId) {
        Employee result = null;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, employeeId);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                String name = rs.getString(COLUMN_NAME);
                String personalInfo = rs.getString(COLUMN_PERSONAL_INFO);
                double baseSalary = rs.getDouble(COLUMN_BASE_SALARY);
                String password = rs.getString(COLUMN_PASSWORD);
                result = new Employee(name, Integer.toString(employeeId), personalInfo, baseSalary, password);
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

    public boolean insert(int employeeId, String name, String personalInfo, double baseSalary, String password) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, employeeId, name, personalInfo, baseSalary, password);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int employeeId, String name, String personalInfo, double baseSalary, String password) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s,%s,%s,%s,%s) VALUES(?,?,?,?,?)", tableName, COLUMN_EMPLOYEE_ID, COLUMN_NAME, COLUMN_PERSONAL_INFO, COLUMN_BASE_SALARY, COLUMN_PASSWORD);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, name);
        ps.setString(3, personalInfo);
        ps.setDouble(4, baseSalary);
        ps.setString(5, password);
        return ps;
    }

    //TODO: when loading the data back remember that driver extends employee, so we need to create Driver object instead of Employee
    public List<Employee> selectAll(){
        List<Employee> result = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int employeeId = rs.getInt(COLUMN_EMPLOYEE_ID);
                String name = rs.getString(COLUMN_NAME);
                String personalInfo = rs.getString(COLUMN_PERSONAL_INFO);
                double baseSalary = rs.getDouble(COLUMN_BASE_SALARY);
                String password = rs.getString(COLUMN_PASSWORD);
                Employee e = new Employee(name, Integer.toString(employeeId), personalInfo, baseSalary, password);
                result.add(e);
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

    public boolean update(int employeeId, String name, String personalInfo, double baseSalary, String password) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, employeeId, name, personalInfo, baseSalary, password);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int employeeId, String name, String personalInfo, double baseSalary, String password) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?", tableName, COLUMN_NAME, COLUMN_PERSONAL_INFO, COLUMN_EMPLOYEE_ID, COLUMN_BASE_SALARY, COLUMN_PASSWORD);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, personalInfo);
        ps.setDouble(3, baseSalary);
        ps.setString(4, password);
        ps.setInt(5, employeeId);
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
