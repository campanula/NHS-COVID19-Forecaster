import java.util.ArrayList;
import java.util.List;

public class NHS_Covid_Forecaster {

    public static void main(String[] args) throws Exception {
        System.out.println("Connecting To NHS API: \n");
        masterArray master = Load_Data_New.getMasterArray(); //Pulls from NHS API
        System.out.println("\n API Data Retrieval Complete. \n");

        System.out.println("\n Loading GUI: \n");
        new GUI(master); //Creates GUI
        System.out.println("\n GUI Complete. \n");
    }
}
