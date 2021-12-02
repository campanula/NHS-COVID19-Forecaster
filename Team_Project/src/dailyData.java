import java.time.LocalDate;

public class dailyData { //Class used to store a daily entry for covid data in each region.
    private final String areaName;
    private final LocalDate date;
    private final int newCases, cumCases, newDeaths, cumDeaths;

    public dailyData(LocalDate date, String areaName, int newCases, int cumCases, int newDeaths, int cumDeaths) {
        this.areaName = areaName;
        this.date = date;
        this.newCases = newCases; this.cumCases = cumCases; this.newDeaths = newDeaths; this.cumDeaths = cumDeaths;
    }

    public String getAreaName(){
        return areaName;
    }
    public LocalDate getDate(){
        return date;
    }

    public int getNewCases() {return newCases;}
    public int getCumCases() { return cumCases; }
    public int getNewDeaths() { return newDeaths; }
    public int getCumDeaths() { return cumDeaths; }
}
