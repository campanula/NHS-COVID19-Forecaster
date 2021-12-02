import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.zip.GZIPInputStream;

public class Load_Data_New {
    private static void populate(masterArray master){ //Calls API Data and stores it in master for each region.
        System.out.println("Getting England Data:");
        master.setE(fillRegion("England","filters=areaType=nation;areaCode=E92000001", master.getE()));
        System.out.println(master.getE().getLast().getAreaName() + " Data Complete. Entries: " + master.getE().size() + " Earliest Date: " + master.getE().get(0).getDate() + " Latest Date: " + master.getE().getLast().getDate() + "\n");

        System.out.println("Getting Northern Ireland Data:");
        master.setN(fillRegion("Northern Ireland","filters=areaType=nation;areaCode=N92000002", master.getN()));
        System.out.println(master.getN().getLast().getAreaName() + " Data Complete. Entries: " + master.getN().size() + " Earliest Date: " + master.getN().get(0).getDate() + " Latest Date: " + master.getN().getLast().getDate() + "\n");

        System.out.println("Getting Scotland Data:");
        master.setS(fillRegion("Scotland","filters=areaType=nation;areaCode=S92000003", master.getS()));
        System.out.println(master.getS().getLast().getAreaName() + " Data Complete. Entries: " + master.getS().size() + " Earliest Date: " + master.getS().get(0).getDate() + " Latest Date: " + master.getS().getLast().getDate() + "\n");

        System.out.println("Getting Wales Data:");
        master.setW(fillRegion("Wales","filters=areaType=nation;areaCode=W92000004", master.getW()));
        System.out.println(master.getW().getLast().getAreaName() + " Data Complete. Entries: " + master.getW().size() + " Earliest Date: " + master.getW().get(0).getDate() + " Latest Date: " + master.getW().getLast().getDate() + "\n");

        System.out.println("Getting United Kingdom Data:");
        master.setUk(fillRegion("United Kingdom","filters=areaType=overview", master.getUK()));
        System.out.println(master.getUK().getLast().getAreaName() + " Data Complete. Entries: " + master.getUK().size() + " Earliest Date: " + master.getUK().get(0).getDate() + " Latest Date: " + master.getUK().getLast().getDate() + "\n");
    }

    private static DataList fillRegion(String regionString,String URLString, DataList regionList){ //This is responsible for filling in all 5 DataLists.
        Stack APIData = NHS_API(URLString);

        for (int i = 0; i < fillStartDates(APIData); i++){ //This fills in blank values from the 01/01/2020 so all the lists line up.
            regionList.insert(new dailyData(LocalDate.parse("2020-01-01").plusDays(i),regionString,0,0,0,0));
        }

        while(APIData.notEmpty()){ //This adds new entries based off data pulled from the API.
            String[] API_Data = processString(APIData.pop(), regionList);
            regionList.insert(new dailyData(LocalDate.parse(API_Data[0]),regionString, Integer.parseInt(API_Data[1]),Integer.parseInt(API_Data[2]),Integer.parseInt(API_Data[3]),Integer.parseInt(API_Data[4]))); //Currently not setting entries right
        }
        return regionList;
    }


    public static masterArray getMasterArray(){ //Simply creates a new master array to pass back to main.
        masterArray master = new masterArray();
        populate(master);
        return master;
    }

    public static Stack NHS_API(String filter){ //Written By Kameron
        Stack APIData = new Stack();
        try { //Connect to the API and queries the information given to it
            String urlString  = "https://api.coronavirus.data.gov.uk/v1/data?format=csv;" + filter + "&structure={%22date%22:%22date%22," +
                    "%22newCases%22:%22newCasesByPublishDate%22,%22cumCases%22:%22cumCasesByPublishDate%22" +
                    ",%22newDeaths%22:%22newDeaths28DaysByDeathDate%22,%22cumDeaths%22:%22cumDeaths28DaysByDeathDate%22}";
            URLEncoder.encode(urlString, StandardCharsets.UTF_8);
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(4000);
            connection.connect();

            int respCode  = connection.getResponseCode();
            if (respCode != 200){
                System.out.println("couldn't connect");
            }
            else{
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
                reader.readLine();
                while ((line = reader.readLine()) != null){
                    APIData.push(line); //Adds strings to a stack for most efficient processing.
                }
                reader.close();
            }
        }
        catch  (IOException e) {
            e.printStackTrace();
        }

        return APIData;
    }

    private static String[] processString(String APIString, DataList regionList){ //This will ensure there are no empty values
        if (APIString.endsWith(",")){ //Deaths are one day behind so this makes it so the data keeps the cumulative deaths correct.
            APIString = APIString.concat(String.valueOf(regionList.getLast().getCumDeaths()));
        }
        String[] API_Data = APIString.split(",");
        for (int i = 1; i < 5; i++){
            if (API_Data[i].equals("")){
                API_Data[i] = "0";
            }
        }
        return API_Data;
    }

    private static int fillStartDates(Stack APIData){ //Used to fill in the dates before data is recorded (Starting at 01/01/2020).
        String first_API = APIData.pop();
        String earliest = (String) first_API.subSequence(0,10);
        APIData.push(first_API);

        LocalDate startDate = LocalDate.parse(earliest);
        return (int)(ChronoUnit.DAYS.between(LocalDate.parse("2020-01-01"), startDate));
    }
}
