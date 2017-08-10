package berkeleyalgorithm;

/**
 * @author Kamil Pyska
 */
public class Clock extends Thread {

    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;
    private int driftInMs;
    private int timeToWait;

    public Clock(int hours, int minutes, int seconds, int milliseconds, int driftInMs) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
        this.driftInMs = driftInMs;
        this.timeToWait = 0;
    }

    private String giveTime() {
        String strHours;
        String strMinutes;
        String strSeconds;
        String strMilliseconds;

        if (this.hours < 10) {
            strHours = "0" + this.hours;
        } else {
            strHours = Integer.toString(this.hours);
        }
        if (this.minutes < 10) {
            strMinutes = "0" + this.minutes;
        } else {
            strMinutes = Integer.toString(this.minutes);
        }
        if (this.seconds < 10) {
            strSeconds = "0" + this.seconds;
        } else {
            strSeconds = Integer.toString(this.seconds);
        }
        if (this.milliseconds >= 10 && this.milliseconds < 100) {
            strMilliseconds = "0" + this.milliseconds;
        } else {
            strMilliseconds = Integer.toString(this.milliseconds);
        }
        if (this.milliseconds < 10) {
            strMilliseconds = "00" + this.milliseconds;
        }
        return strHours + ":" + strMinutes + ":" + strSeconds + ":" + strMilliseconds;
    }

    public int getCurrentTimeAsMs() {
        int timeInMs = 0;
        timeInMs += this.hours * 3600000;
        timeInMs += this.minutes * 60000;
        timeInMs += this.seconds * 1000;
        timeInMs += this.milliseconds;
        return timeInMs;
    }

    public void addTime(int timeToAdd) {
        this.milliseconds += timeToAdd;
        //sekundy plus sekundy z milisekund  
        this.seconds += this.milliseconds / 1000;
        //sekundy z milisekund zostaly juz wliczone, wiec obecne milisekundy to reszta z dzielenia przez 1000. 
        this.milliseconds %= 1000;
        this.minutes += this.seconds / 60;
        this.seconds %= 60;
        this.hours += this.minutes / 60;
        this.minutes %= 60;

        if (this.hours >= 24) {
            this.hours %= 24;
        }
    }

    public void setTimeToWait(int time) {
        this.timeToWait = time;
    }

    public void setDrift(int drift) {
        this.driftInMs = drift;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (this.timeToWait >= this.driftInMs) {
                    this.timeToWait -= this.driftInMs;
                } else {
                    addTime(this.driftInMs - this.timeToWait);
                    this.timeToWait = 0;
                }

                System.out.println(giveTime());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Exception");
        }
    }
}
