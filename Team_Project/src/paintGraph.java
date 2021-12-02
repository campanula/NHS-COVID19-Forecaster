import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import static java.awt.Color.*;

public class paintGraph extends JPanel { //Variables used to determine that state of the graph and store important info.
    int p; //p is cases prediction.
    Boolean predictionState, labelState;
    DataList regionData;
    LocalDate latestDate;
    int caseState;
    int forecastLength, previousLength;

    public paintGraph(masterArray master){ //Set to defaults when first called
        this.regionData = master.get(4);
        latestDate = regionData.getLast().getDate().minusDays(1);
        this.predictionState = true;
        this.labelState = true;
        this.caseState = 2;
        this.forecastLength = 7;
        this.previousLength = 7;

        setBackground(black);
    }

    //sets up paint object, calls drawgraph
    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        drawgraph(g2d);
    }

    public void drawgraph(Graphics2D g2d){
        //fontmetrics for centering labels
        FontMetrics fm = g2d.getFontMetrics();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
        String str;

        //draw axes
        g2d.setColor(white);
        int height = getHeight();
        int width = getWidth();
        int graphh = height - 100; //graph height (including 50px border on all sides)
        g2d.drawLine(50, 50, 50, height-50);
        g2d.drawLine(50, height-50, width-50, height-50);

        str = "A graph showing COVID deaths and infections over "+ previousLength + " days in " + regionData.getLast().getAreaName();
        g2d.drawString(str, (width-fm.stringWidth(str))/2, 20);

        str = "Y Axis Shows Number of Cases (Blue) And / Or Number of Deaths (Red)";
        g2d.drawString(str, (width-fm.stringWidth(str))/2, 40);

        ArrayList<Integer> previousData; //Used to grab the data needed to draw the graph.
        boolean caseMode;
        if (caseState == 1){
            caseMode = false;
            previousData = getData(false, previousLength);
        }
        else
        {
            caseMode = true;
            previousData = getData(true, previousLength);
        }

        int max = Collections.max(previousData); //get highest cases / deaths number for use in scaling

        if (predictionState){
            getPredictions(caseMode);
            if(max<p) {max= p;}
        }

        int step = (width-100)/(previousLength+forecastLength); //step is horizontal distance between days, (width-100)/(forecastLengthPeriod*2)

        if(!predictionState){ step = (width-100)/(previousLength); }

        float hstep = max / (float)(graphh - 100);

        if (hstep > 1f){ hstep = Math.round(hstep); }

        drawLines(caseMode, g2d, step, hstep, height, previousData);

        if (caseState == 2){
            getPredictions(false);
            drawLines(false, g2d, step, hstep, height, getData(false, previousLength));
        }

        //adding day labels
        g2d.setColor(white);

        String last = "0";
        g2d.drawString(last, 52 - width - fm.stringWidth(str),  height - 50);
        for(int i = 0; i < (Math.floor(graphh / 20f)) ; i++){
            str = String.valueOf((int)(i * hstep * 20));
            if (!str.equals(last)){
                g2d.drawString(str, (45 - fm.stringWidth(str)), (i * -20) + height - 50 );
                last = str;
            }
        }

        int split = previousLength;
        if(predictionState){
            split += forecastLength;
        }

        boolean count = true;
        for(int i = 0; i<(split); i++){
            if ((i+1)%(int)(previousLength/7f) == 0f){
                str = latestDate.plusDays(forecastLength).minusDays(i).format(dateFormatter);
                int x = width - (i * step) - 107;
                int y = height - 10;
                if (count){
                    g2d.drawString(str,  x ,y);
                    g2d.drawLine(x + 15, y - 12, x + 15, y - 40);
                }
                else {
                    g2d.drawString(str,  x ,y - 20);
                    g2d.drawLine(x + 15, y - 32, x + 15, y - 40);
                }
                count = !count;
            }
        }
    }

    private ArrayList<Integer> getData(boolean caseMode, int timeFrame){ //Used to a specified amount of days previous.
        ArrayList<Integer> data = new ArrayList<>();
        for(int i = timeFrame + 1; i>1; i--){
            dailyData temp = regionData.get(regionData.size() - i);
            if (caseMode) {
                data.add(temp.getNewCases());
            }
            else
            {
                data.add(temp.getNewDeaths());
            }
        }
        return data;
    }

    private void drawLines(boolean caseMode, Graphics2D g2d, int step, float hstep, int height, ArrayList<Integer> data){ //Used to draw lines.
        Color myblue = new Color(65,105,225);
        for(int i = 0; i<previousLength-1; i++){
            if (caseMode){
                g2d.setColor(cyan);
            }
            else {
                g2d.setColor(red);
            }

            if ((i+1)%(int)(previousLength/7f) == 0f && labelState){
                g2d.drawString(String.valueOf(data.get(i)), 50 + (step * i), height - (Math.round((float) (data.get(i)) / hstep) + 60));
            }

            g2d.drawLine(50+(step*i), height-(Math.round((float)(data.get(i))/hstep)+50), 50+(step*(i+1)), height-(Math.round((float)(data.get(i+1))/hstep)+50));
            if(i == previousLength-2){
                if (labelState){ g2d.drawString(String.valueOf(data.get(i+1)), 50 + (step * (i+1)), height - (Math.round((float) (data.get(i+1)) / hstep) + 60)); }
                if (predictionState){
                    //on the last iteration of the loop, prints the prediction line
                    if (caseMode){
                        g2d.setColor(myblue);
                    }
                    else{
                        g2d.setColor(orange);
                    }
                    if (labelState) { g2d.drawString(String.valueOf(p), 50+(step*((previousLength+forecastLength)-1)), height-(Math.round((float)(p)/hstep)+60)); }
                    g2d.drawLine(50+(step*(i+1)), height-(Math.round((float)(data.get(i+1))/hstep)+50), 50+(step*((previousLength+forecastLength)-1)), height-(Math.round((float)(p)/hstep)+50) );
                }
            }
        }

    }


    private void getPredictions(boolean caseMode){
        //create linear regression object for cases and deaths
        LinearRegressionClassifier Regression = new LinearRegressionClassifier(caseMode, regionData);
        p = Regression.predictValue(forecastLength);
    }


    public void predictionButton(JButton but){ //Called when the prediction toggle button is called.
        predictionState = !predictionState;
        if (predictionState){
            but.setText("Turn Off Predictions");
        }
        else{
            but.setText("Turn On Predictions");
        }
    }

    public void labelButton(JButton but){ //Called when the label toggle button is called.
        labelState = !labelState;
        if (labelState){
            but.setText("Turn Off Labels");
        }
        else{
            but.setText("Turn On Labels");
        }
    }

    public void setRegionData(DataList newRegionData){ //Sets region
        regionData = newRegionData;
    }

    public void setCaseState(int index){ //Sets whether cases, deaths or both are displayed.
        caseState = index;
    }

    public void setForecast(String entry){
        String[] strings = entry.split(" ");
        forecastLength = Integer.parseInt(strings[0]);
    }

    public void setPrevious(int index){
        switch (index){
            case 0:
                previousLength = (int) ChronoUnit.DAYS.between(latestDate.minusWeeks(1), latestDate);
                break;
            case 1:
                previousLength = (int) ChronoUnit.DAYS.between(latestDate.minusWeeks(2), latestDate);
                break;
            case 2:
                previousLength = (int) ChronoUnit.DAYS.between(latestDate.minusMonths(1), latestDate);
                break;
            case 3:
                previousLength = (int) ChronoUnit.DAYS.between(latestDate.minusMonths(6), latestDate);
                break;
            case 4:
                previousLength = (int) ChronoUnit.DAYS.between(latestDate.minusYears(1), latestDate);
                break;
            case 5:
                previousLength = regionData.size() - 1;
                break;
        }
    }
}
