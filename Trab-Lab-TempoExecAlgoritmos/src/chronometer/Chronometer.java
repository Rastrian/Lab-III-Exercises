package chronometer;

public class Chronometer {

    private long timeStart;
    private long timeEnd;
    private long time;
    private Integer value;

    public Chronometer(){
        time = 0;
    }

    public Chronometer(long start, long end) {
        timeStart = start;
        timeEnd = end;
        time = getTimeEnd() - getTimeStart();
        timeStart = 0;
    }

    public void start(){
        timeStart = System.nanoTime();
    }

    public long stop(){
        timeEnd = System.nanoTime();
        time = getTimeEnd() - getTimeStart();
        timeStart = 0;
        return time;
    }

    public long mark(){
        return System.nanoTime() - timeStart;
    }

    public long restart(){
        timeEnd = System.nanoTime();
        time = getTimeEnd() - getTimeStart();
        timeStart = System.nanoTime();
        timeEnd = 0;
        return time;
    }

    public void setValue(Integer val) {
        value = val;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public long getTime() {
        return time;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Chronometer{" +
                " Started At =" + timeStart +
                ", Ended At =" + timeEnd +
                ", Duration =" + time +
                '}';
    }
}