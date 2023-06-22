import FaceRecognition.FaceRecognition;
import HR_Delivery.HRDeliveryMainMenu;
import HR_Delivery.LicenseType;
import HumanResources.CLI.LoginScreenWindow;
import HumanResources.DataAcessLayer.EmployeeDAL.DriverDAO;
import HumanResources.GUI.LoginScreen;

import java.io.*;

import static FaceRecognition.DetectFaces.detectFaces;
import static HumanResources.GUI.LoginScreen.CreateLoginScreen;

import java.io.File;

import TextToSpeech.QuickstartSample;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("GUI")) {
            CreateLoginScreen();
        }
        if(args[0].equals("CLI")) {
            new LoginScreenWindow();
        }

    }
}
