package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDatosInterface extends Remote {

    void addUsuario(Usuario var1) throws RemoteException;

    Usuario getUsuario(String var1) throws RemoteException;

    String[] getAllUsers() throws RemoteException;

    void addSeguidor(String var1, String var2) throws RemoteException;

    void removeSeguidor(String var1, String var2) throws RemoteException;

    void addTrino(Trino var1) throws RemoteException;

    void addTrinoPendiente(Trino var1) throws RemoteException;

    Trino getTrinoPendiente(String var1) throws RemoteException;

    int asigUserID(String var1) throws RemoteException;

    int getUserID(String var1) throws RemoteException;

    int removeUserID(String var1) throws RemoteException;
}

