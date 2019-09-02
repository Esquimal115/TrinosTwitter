package Servidor;

import Common.Usuario;
import Common.Utils;
import Common.ServicioAutenticacionInterface;
import Common.ServicioDatosInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface {

    private ServicioDatosInterface baseDeDatos;

    public ServicioAutenticacionImpl() throws RemoteException {
        Utils.arrancarRegistro(8888);
        String dirBaseDeDatos = "rmi://localhost:8888/ServicioDatos";

        try {
            this.baseDeDatos = (ServicioDatosInterface)Naming.lookup(dirBaseDeDatos);
        } catch (Exception e) {
            System.out.println(Utils.hora() + " ERROR: Fallo al conectar con la BD.\n" + e + "\n");
        }

    }

    public boolean registrarUsuario(Usuario usuario) throws RemoteException {
        String nick = usuario.getNick();
        if (this.baseDeDatos.getUsuario(nick) == null) {
            System.out.println(Utils.hora() + "Registrado el usuario: " + nick);
            this.baseDeDatos.addUsuario(usuario);
            return true;
        } else {
            return false;
        }
    }

    public int iniciarSesionUsuario(String nick, String password) throws RemoteException {
        if (this.baseDeDatos.getUserID(nick) != -1) {
            return -1;
        } else if (this.baseDeDatos.getUsuario(nick) == null) {
            return -2;
        } else if (password.equals(this.baseDeDatos.getUsuario(nick).getPassword())) {
            int idSesion = this.baseDeDatos.asigUserID(nick);
            return idSesion;
        } else {
            return -3;
        }
    }

    public void cerrarSesionUsuario(String nick) throws RemoteException {
        System.out.println(Utils.hora() + "Desconectado el usuario: " + nick);
        this.baseDeDatos.removeUserID(nick);
    }
}

