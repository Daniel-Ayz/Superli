import FaceRecognition.FaceRecognition;
import HR_Delivery.HRDeliveryMainMenu;
import HR_Delivery.LicenseType;
import HumanResources.DataAcessLayer.EmployeeDAL.DriverDAO;
import HumanResources.GUI.LoginScreen;


public class Main {
    public static void main(String[] args) {

//        new HRDeliveryMainMenu();
        new LoginScreen();
//        LicenseType lt = new DriverDAO().getById(3286);
//        if (lt == null)
//            System.out.println("oh no");
//        else
//            System.out.println(lt);
    }
}
