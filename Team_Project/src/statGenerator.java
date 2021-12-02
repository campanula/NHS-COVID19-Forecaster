import java.text.DecimalFormat;

public class statGenerator {

    public static Stack getStats(DataList regionData, int interval){ //Used to produce a stat readout for both the stat panel and PDF.
        Stack strings = new Stack();

        int size = regionData.size() - 1;
        DecimalFormat df =  new DecimalFormat("0");

        double currentCaseInterval = (regionData.getLast().getCumCases()) - (regionData.get(size - interval).getCumCases());
        double previousCaseInterval = (regionData.get(size - interval).getCumCases()) - (regionData.get(size - interval - interval).getCumCases());

        double percentageCase = (currentCaseInterval - previousCaseInterval) / previousCaseInterval;

        double currentDeathInterval = (regionData.getLast().getCumDeaths()) - (regionData.get(size - interval).getCumDeaths());
        double previousDeathInterval = (regionData.get(size - interval).getCumDeaths()) - (regionData.get(size - interval - interval).getCumDeaths());

        double percentageDeath = (currentDeathInterval - previousDeathInterval) / previousDeathInterval;

        strings.push("*");
        strings.push("*");
        strings.push(caseChange(percentageDeath, "Deaths", interval));
        strings.push("Deaths In Past " + interval + " Day Interval: " + df.format(previousDeathInterval) + "\n");
        strings.push("New Deaths In " + interval + " Day Interval: " + df.format(currentDeathInterval) + "\n");
        strings.push("*");
        strings.push(caseChange(percentageCase, "Cases", interval));
        strings.push("Cases In Past " + interval + " Day Interval: " + df.format(previousCaseInterval) + "\n");
        strings.push("New Cases In " + interval + " Day Interval: " + df.format(currentCaseInterval) + "\n");
        strings.push("Statistics Summary: " + regionData.getLast().getAreaName() + " " + interval + " Day Summary\n");

        return strings;
    }

    private static String caseChange(double percentage, String name, int interval){
        DecimalFormat df =  new DecimalFormat("0.00");
        if (percentage > 0) {
            return  name + " Increase From Past " + interval + " Day Interval: " + df.format(percentage * 100) + "%\n";
        } else {
            percentage *= -1;
            return name + " Decrease From Past " + interval + " Day Interval: " + df.format(percentage * 100) + "%\n";
        }
    }
}
