import java.time.LocalDate; //Programmed By Adam
import java.time.temporal.ChronoUnit;

public class Stack {  //Stack used to store API Data.
    private final String[] lines = new String[(int)(ChronoUnit.DAYS.between(LocalDate.parse("2020-01-01"), LocalDate.now()))]; //Size is equal to the amount of days it will store.
    private int current = 0;

    public void push (String s) { //Adds Strings
        lines[current] = s;
        current++;
    }

    public String pop () { //Removes Strings
        if (current == 0)
            return null;
        else {
            current--;
            return lines[current];
        }
    }

    public boolean notEmpty () { return current != 0; } //Checks if there are any left in the list.
}
