package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioGestorInterface extends Remote {

    String[] getAllUsers() throws RemoteException;

    Usuario getInfoUsuario(String user) throws RemoteException;

    boolean addSeguidor(String nick, String seguidor) throws RemoteException;

    boolean removeSeguidor(String nick, String seguidor) throws RemoteException;

    void addTrino(String nick, String trino) throws RemoteException;

    Trino trinosPendientes(String trinos) throws RemoteException;

    void registrarCallback(String nick, CallbackUsuarioInterface callback) throws RemoteException;

    void removeRegistroCallback(String nick) throws RemoteException;
}
