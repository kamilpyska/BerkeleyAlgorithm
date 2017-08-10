package berkeleyalgorithm;

import java.rmi.*;

/**
 * @author Kamil Pyska
 */
public interface ITimeService extends Remote {

    int getCurrentTimeAsMs() throws RemoteException;

    void sendTimeDifference(int ms) throws RemoteException;
}
