package FaceRecognition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FaceRecognition {
    public static String recognizeFaces() {
        String s = null;
        String name = "ERROR";
        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
//            Process p = Runtime.getRuntime().exec("python AttendanceProject/AttendanceProject.py");
            File file = new File(System.getProperty("user.dir") + "\\AttendanceProject\\");

            Process p = Runtime.getRuntime().exec("python " + "AttendanceProject.py",
                    null, file);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
//            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
//                System.out.println("a"+s);
                name = s;
            }

//             read any errors from the attempted command
//            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }


//            System.exit(0);
        } catch (
                IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return name;
    }
}
