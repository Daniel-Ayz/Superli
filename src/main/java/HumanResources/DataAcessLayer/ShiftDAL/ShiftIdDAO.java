package HumanResources.DataAcessLayer.ShiftDAL;

import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShiftIdDAO extends DAO {
    private static final String TABLE_NAME = "ShiftId";

    public ShiftIdDAO() {
        super(TABLE_NAME);
    }

    //returns the next shiftId that hasn't been used
    public int getShiftID() {
        int shiftId=-1;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetByDate(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()) {
                shiftId = rs.getInt("shiftId");
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        //finally, return result
        return shiftId;
    }



    private PreparedStatement createPreparedStatementGetByDate(Connection con) throws SQLException {
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }

    public boolean insert(int id) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, id);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    //updates the table with the next shiftId needed
    public boolean update(int shiftId) {
        if(delete()) {
            try (Connection con = connect();
                 PreparedStatement ps = createPreparedStatementInsert(con, shiftId);) {

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private PreparedStatement createPreparedStatementInsert(Connection con,int shiftId) throws SQLException {
        String sql = String.format("INSERT INTO %s(shiftId) VALUES(?)", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftId);
        return ps;
    }

    public boolean delete() {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con) throws SQLException {
        String sql = String.format("DELETE FROM %s", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }
}
