package berkeleyalgorithm;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Kamil Pyska
 */
public class Client1 extends UnicastRemoteObject implements ITimeService {

    Clock clock = new Clock(0, 0, 0, 0, 900);

    public Client1() throws RemoteException {
    }

    public static void main(String[] args) {

        try {
            Client1 client = new Client1();
            UnicastRemoteObject.unexportObject(client, true);
            System.out.println("Exporting...");
            ITimeService iTimeService = (ITimeService) UnicastRemoteObject.exportObject(client, 0);
            Registry registry = LocateRegistry.getRegistry(1099);
            System.out.println("Registry located on localhost.");

            registry.rebind("Client1", iTimeService);
            System.out.println("Client1 bound, ready.");
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
