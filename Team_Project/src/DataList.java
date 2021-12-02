public class DataList {
    private static class dataEntry { //Custom Data Structure used for storing dailyData
        dailyData data;
        dataEntry prev, next;

        dataEntry(dailyData data, dataEntry prev, dataEntry next) { //Used to form a double-linked list
            this.data = data;
            this.prev = prev; this.next = next;
        }
    }

    private dataEntry head = null;
    private dataEntry tail = null;
    private int size = 0;

    public dailyData get(int index) { //Returns an entry by an index
        if (index < 0 || index >= size)
            return null;
        else
            return getDataEntry(index).data;
    }

    public void insert(dailyData data) { //Used to add a file to the end of the list, as no data should be inserted elsewhere.
        dataEntry e = new dataEntry(data, tail, null);
        tail = e;
        if (e.prev == null)
            head = e;
        else
            e.prev.next = e;
        size++;
    }

    private dataEntry getDataEntry(int index) { //Used to facilitate the get command.
        dataEntry current = head;
        for (int i = 0; i < index; i++)
            current = current.next;
        return current;
    }

    private dataEntry current = null; //Used in iterative Parts.

    public void reset() {
        current = null;
    } //Used to reset the current if iterator is used.

    public dailyData getNext() { //Used to iterate over all the data.
        if (current == null) current = head;
        else current = current.next;
        return current == null ? null : current.data;
    }

    public boolean hasNext() { //Used to set up a while statement.
        if (current == null) return head != null;
        else return current.next != null;
    }

    public dailyData getLast(){ return tail.data; } //Used mainly for grabbing latest dates, implemented to save processing time.

    public int size() { return size; } //Used to show size of the list.

}
