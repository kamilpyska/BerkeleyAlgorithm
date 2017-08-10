package berkeleyalgorithm;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Kamil Pyska
 */
public class Client0 extends UnicastRemoteObject implements ITimeService {

    Clock clock = new Clock(0, 1, 40, 0, 1100);

    public Client0() throws RemoteException {
    }

    public static void main(String[] args) {

        try {
            Client0 client = new Client0();
            UnicastRemoteObject.unexportObject(client, true);
            System.out.println("Exporting...");
            ITimeService iTimeService = (ITimeService) UnicastRemoteObject.exportObject(client, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("New registry created on localhost.");

            registry.rebind("Client0", iTimeService);
            System.out.println("Client0 bound, ready");
            client.clock.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCurrentTimeAsMs() {
        return this.clock.getCurrentTimeAsMs();
    }

    @Override
    public void sendTimeDifference(int diff) throws RemoteException {
        if (diff < 0) {
            this.clock.setTimeToWait(Math.abs(diff));
        } else {
            this.clock.addTime(diff);
        }
    }
}
