package berkeleyalgorithm;

import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kamil Pyska
 */
public class TimeServer {

    List<ITimeService> clients = new ArrayList<>();
    int respondTime = 10;
    Clock clock = new Clock(0, 0, 40, 0, 1000);

    public static void main(String[] args) {

        try {
            for (String s : Naming.list("//localhost")) {
                System.out.println(s);
            }

            TimeServer server = new TimeServer();
            server.clock.start();

            Registry reg = LocateRegistry.getRegistry("localhost");

            ITimeService iTimeService = (ITimeService) reg.lookup("Client0");
            server.clients.add(iTimeService);

            ITimeService iTimeService1 = (ITimeService) reg.lookup("Client1");
            server.clients.add(iTimeService1);

            while (true) {
                server.synchronizeTimes();

                Thread.sleep(5000);
            }

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public void synchronizeTimes() throws RemoteException {
        int currentServerTime = this.clock.getCurrentTimeAsMs();
        //Do sumy od razu dodaję czas serwera, gdyż będzie brał udział w liczeniu średniej.
        int sumOfTimes = currentServerTime;
        int numberOfTimes = 1;
        int average;

        Map<ITimeService, Integer> clientsMap = new HashMap<>();
        for (ITimeService client : clients) {
            int time = client.getCurrentTimeAsMs()+respondTime;
            clientsMap.put(client, time);

            if (!isTooDifferentFromServerTime(currentServerTime, time)) {
                sumOfTimes += time;
                numberOfTimes++;
            }
        }

        average = sumOfTimes / numberOfTimes;
        System.out.println("Średnia " + numberOfTimes + " czasów: " + formatThisTime(average));

        int i = 0;
        for (Map.Entry<ITimeService, Integer> record : clientsMap.entrySet()) {
            int difference = average - record.getValue() - respondTime;

            System.out.println("Różnica średniej i czasu klient" + i + ": " + formatThisTime(difference));
            i++;
            record.getKey().sendTimeDifference(difference);
        }

        this.sendTimeDifference(average - currentServerTime);
    }

    private boolean isTooDifferentFromServerTime(int serverTime, int clientTime) throws RemoteException {
        int diff = Math.abs(serverTime - clientTime - respondTime);
        if (diff >= 60000) {
            return true;
        } else {
            return false;
        }
    }

    private void sendTimeDifference(int diff) throws RemoteException {
        System.out.println("Różnica średniej i czasu serwera: " + formatThisTime(diff));
        if (diff < 0) {
            this.clock.setTimeToWait(Math.abs(diff));
        } else {
            this.clock.addTime(diff);
        }
    }

    private String formatThisTime(int mSec) {
        char plusOrMinus = '+';
        if (mSec < 0) {
            plusOrMinus = '-';
        }
        int milliseconds = Math.abs(mSec);
        int seconds = milliseconds / 1000;
        milliseconds %= 1000;
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;

        if (hours >= 24) {
            hours %= 24;
        }

        String strHours;
        String strMinutes;
        String strSeconds;
        String strMilliseconds;

        if (hours < 10) {
            strHours = "0" + hours;
        } else {
            strHours = Integer.toString(hours);
        }
        if (minutes < 10) {
            strMinutes = "0" + minutes;
        } else {
            strMinutes = Integer.toString(minutes);
        }
        if (seconds < 10) {
            strSeconds = "0" + seconds;
        } else {
            strSeconds = Integer.toString(seconds);
        }
        if (milliseconds >= 10 && milliseconds < 100) {
            strMilliseconds = "0" + milliseconds;
        } else {
            strMilliseconds = Integer.toString(milliseconds);
        }
        if (milliseconds < 10) {
            strMilliseconds = "00" + milliseconds;
        }
        return plusOrMinus + strHours + ":" + strMinutes + ":" + strSeconds + ":" + strMilliseconds;
    }
}
