package Common;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote {


    boolean registrarUsuario(Usuario user) throws RemoteException;

    int iniciarSesionUsuario(String nick, String psw) throws RemoteException;

    void cerrarSesionUsuario(String nick) throws RemoteException;
}
