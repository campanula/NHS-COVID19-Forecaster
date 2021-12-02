import java.util.ArrayList;
import java.util.TreeMap;

public class LinearRegressionClassifier {
    private final ArrayList<Integer> Data;
    private final Boolean caseMode;

    public LinearRegressionClassifier(boolean caseMode, DataList regionData) {
        this.caseMode = caseMode;
        this.Data = getData(caseMode,regionData);
    }

    public Float getXMean (ArrayList<Integer> Data) { //Gets the mean of the days (number of entries).
        float xMean = 0f;
        for (int i = 0; i < Data.size(); i++){
            xMean += i + 1;
        }
        xMean /= Data.size();
        return xMean;
    }
    public Float getYMean (ArrayList<Integer> Data) { //Gets the mean of the cases or deaths
        float yMean = 0.0f ;
        for (Integer datum : Data) {
            yMean = yMean + datum;
        }
        yMean /= Data.size();
        return yMean;
    }

    public Float findM (Float xMean, Float yMean, int x1, int y1) { //Calculates the x coefficient in y = mx + c
        float sum1 = x1 - xMean;
        float sum2 = y1 - yMean;
        float sum3 = (x1 - xMean);
        return (sum1 * sum2) / (sum3 * sum3);
    }

    public float findC (Float xMean, Float yMean, Float m) { // Calculates the y intercept in y = mx + c
        return yMean - (m * xMean);
    }
    public ArrayList<Float> returnValue () { //processes the data in order to give a prediction for the prediction day.
        ArrayList<Float> returnValues = new ArrayList<>();
        int y1 = Data.get(0);
        Float xMean = getXMean(Data);
        Float yMean = getYMean(Data);
        Float m = findM(xMean, yMean, 1, y1);
        Float c = findC(xMean, yMean, m);
        returnValues.add(m);
        returnValues.add(c);
        return returnValues;
    }

    //processes the data in order to give a prediction for the prediction day.
    public int predictValue ( int predictionDay ) {
        float mScale;
        int regionState = masterArray.index;
        if (caseMode){
            if(regionState == 0 || regionState == 4){
                if (predictionDay == 7){
                    mScale = 65;
                }
                else if(predictionDay == 28){
                    mScale = 2;
                }
                else{
                    mScale = 20;
                }
            }
            else{
                if(predictionDay==7){
                    mScale = 5.5f;
                }
                else if(predictionDay==28){
                    mScale = 0.1f;
                }
                else{
                    mScale = 0.75f;
                }

            }
        }
        else{
            if(regionState == 0 || regionState == 4){
                if (predictionDay == 7){
                    mScale = 1;
                }
                else if (predictionDay==28){
                    mScale = 0.1f;
                }
                else{
                    mScale = 0.5f;
                }
            }
            else{
                if(predictionDay==7){
                    mScale = 0.15f;
                }
                else if(predictionDay == 28){
                    mScale = 0.01f;
                }
                else{
                    mScale = 0.025f;
                }
            }
        }

        ArrayList<Float> val = returnValue();
        TreeMap<Integer, Float> treeMap = new TreeMap<>();
        float c = val.get(1);
        int index = Data.size() - predictionDay;

        float m1 = 0.00f;
        for (int x = 0; x < predictionDay; x++) {
            if (index > Data.size() - 1){
                break;
            }
            int distanceTotal = 0;
            int actualY = Data.get(index);
            int predictionY = (int) ((m1 * x) + c);
            int distance = Math.abs(actualY - predictionY);
            distanceTotal += distance;
            treeMap.put(distanceTotal, m1);
            index++;
            m1+=mScale;
        }


        int key = treeMap.firstKey();
        float m2 = treeMap.get(key);
        return (int) ((m2 * (predictionDay + predictionDay))+ c);
    }


    public static ArrayList<Integer> getData(boolean caseMode, DataList regionData){ //Used to grab all the data from one region.
        ArrayList<Integer> data = new ArrayList<>();
        while (regionData.hasNext()){
            if (caseMode) {
                data.add(regionData.getNext().getNewCases());
            }
            else
            {
                data.add(regionData.getNext().getNewDeaths());
            }
        }

        regionData.reset();
        return data;
    }
}
