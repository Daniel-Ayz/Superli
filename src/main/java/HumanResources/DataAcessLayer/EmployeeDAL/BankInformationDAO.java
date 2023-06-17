package HumanResources.DataAcessLayer.EmployeeDAL;

import HumanResources.BusinessLayer.EmployeeModule.BankInformation;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BankInformationDAO extends DAO {
    private static final String TABLE_NAME = "BankInformation";
    private static final String COLUMN_EMPLOYEE_ID = "employeeId";
    private static final String COLUMN_BANK_NAME = "bankName";
    private static final String COLUMN_BANK_ACCOUNT_NUMBER = "bankAccountNumber";

    public BankInformationDAO() {
        super(TABLE_NAME);
    }

    public BankInformation getById(int employeeId) {
        BankInformation result = null;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, employeeId);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                String bankName = rs.getString(COLUMN_BANK_NAME);
                String bankAccountNumber = rs.getString(COLUMN_BANK_ACCOUNT_NUMBER);
                result = new BankInformation(bankName, bankAccountNumber);
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

    public boolean insert(int employeeId, String bankName, String bankAccountNumber) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, employeeId, bankName, bankAccountNumber);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int employeeId, String bankName, String bankAccountNumber) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s,%s,%s) VALUES(?,?,?)", tableName, COLUMN_EMPLOYEE_ID, COLUMN_BANK_NAME, COLUMN_BANK_ACCOUNT_NUMBER);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, bankName);
        ps.setString(3, bankAccountNumber);
        return ps;
    }

    public Map<Integer, BankInformation> selectAll(){
        Map<Integer, BankInformation> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int employeeId = rs.getInt(COLUMN_EMPLOYEE_ID);
                String bankName = rs.getString(COLUMN_BANK_NAME);
                String bankAccountNumber = rs.getString(COLUMN_BANK_ACCOUNT_NUMBER);
                result.put(employeeId, new BankInformation(bankName, bankAccountNumber));
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

    public boolean update(int employeeId, String bankName, String bankAccountNumber) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, employeeId, bankName, bankAccountNumber);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int employeeId, String bankName, String bankAccountNumber) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tableName, COLUMN_BANK_NAME, COLUMN_BANK_ACCOUNT_NUMBER, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, bankName);
        ps.setString(2, bankAccountNumber);
        ps.setInt(3, employeeId);
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
