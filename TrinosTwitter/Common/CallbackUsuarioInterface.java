package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackUsuarioInterface extends Remote {
    String notifica(Trino trino) throws RemoteException;
}
