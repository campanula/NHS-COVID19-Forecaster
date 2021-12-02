public class masterArray {
    DataList eData, nData, sData, wData, ukData;
    DataList current;
    static int index;

    public masterArray(){ //Data Structure to hold the 5 regional dataLists.
        this.eData = new DataList(); this.nData = new DataList(); this.sData = new DataList(); this.wData = new DataList(); this.ukData = new DataList();
        current = eData;
    }

    public DataList getE(){ return this.eData; }
    public DataList getN(){ return this.nData; }
    public DataList getS(){ return this.sData; }
    public DataList getW(){ return this.wData; }
    public DataList getUK(){ return this.ukData; }

    public void setE(DataList e) { this.eData = e; }
    public void setN(DataList n) { this.nData = n; }
    public void setS(DataList s) { this.sData = s; }
    public void setW(DataList w) { this.wData = w; }
    public void setUk(DataList uk) { this.ukData = uk; }

    public void reset(){
        current = eData;
    } //Used to reset the iteration if needed.

    public DataList getNext(){ //Used to iterate through all of the regions.
        if (current == eData){
            current = nData;
        }
        else if(current == nData){
            current = sData;
        }
        else if(current == sData){
            current = wData;
        }
        else if(current == wData){
            current = ukData;
        }
        else if(current == ukData){
            current = eData;
        }
        return current;
    }

    public DataList get(int index){ //Used to grab a specific region via index (Mostly for comboBoxes).
        current = eData;
        for (int i = 0; i < index; i++){
            getNext();
        }
        masterArray.index = index;
        return current;
    }
}
