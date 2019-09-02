package Servidor;

import Common.Trino;
import Common.Usuario;
import Common.Utils;
import Common.CallbackUsuarioInterface;
import Common.ServicioDatosInterface;
import Common.ServicioGestorInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {

    private ServicioDatosInterface baseDeDatos;
    private static HashMap<String, CallbackUsuarioInterface> listaUsuarios = new HashMap();

    public ServicioGestorImpl() throws RemoteException {
        Utils.arrancarRegistro(8888);
        String servGestor = "rmi://localhost:8888/ServicioDatos";

        try {
            baseDeDatos = (ServicioDatosInterface)Naming.lookup(servGestor);
            System.out.println("\n" + Utils.hora() + "Conexión CORRECTA con la BBDD");
        } catch (Exception e) {
            System.out.println(Utils.hora() + " ERROR: No se puede conectar a la BD\n" + e);
            System.out.println(Utils.hora() + "Conexión finalizada");
            System.exit(1);
        }

    }

    public String[] getAllUsers() throws RemoteException {
        System.out.println(Utils.hora() + "Lista de todos los usuarios.\n");
        return baseDeDatos.getAllUsers();
    }

    public Usuario getInfoUsuario(String nick) throws RemoteException {
        return baseDeDatos.getUsuario(nick);
    }

    public Set<String> getUsuarioOnline(){
        return listaUsuarios.keySet();
    }

    public boolean addSeguidor(String nick, String seguirA) throws RemoteException {
        if (baseDeDatos.getUsuario(seguirA) == null) {
            return false;
        } else {
            System.out.println(Utils.hora() + "El usuario " + nick + " ha empezado a seguir a " + seguirA);
            baseDeDatos.addSeguidor(nick, seguirA);
            return true;
        }
    }

    public boolean removeSeguidor(String nick, String dejarA) throws RemoteException {
        if (baseDeDatos.getUsuario(dejarA) == null) {
            return false;
        } else {
            System.out.println(Utils.hora() + "El usuario " + nick + " ha dejado de seguir a " + dejarA);
            baseDeDatos.removeSeguidor(nick, dejarA);
            return true;
        }
    }

    public void addTrino(String nick, String trino) throws RemoteException {
        Trino trinoPublicar = new Trino(trino, nick);
        baseDeDatos.addTrino(trinoPublicar);
        System.out.println(Utils.hora() + "El usuario " + nick + " ha publicado un trino");
        ArrayList<String> seguidores = baseDeDatos.getUsuario(nick).getSeguidores();

        for(int i = 0; i < seguidores.size(); ++i) {
            String seguidor = seguidores.get(i);
            if (listaUsuarios.containsKey(seguidor)) {
                CallbackUsuarioInterface callbackTrino = listaUsuarios.get(seguidor);
                callbackTrino.notifica(trinoPublicar);
                System.out.println("*** Se envía un Trino al usuario " + seguidor);
            } else {
                String trinoGuardar = ">" + nick + "# " + trino;
                this.baseDeDatos.addTrinoPendiente(new Trino(trinoGuardar, seguidor));
                System.out.println("*** Trino pendiente para el usuario: " + seguidor);
            }
        }
    }

    public Trino trinosPendientes(String nick) throws RemoteException {
        Trino trino = this.baseDeDatos.getTrinoPendiente(nick);
        if (trino != null) {
            System.out.println(Utils.hora() + "Trino pendiente mandado al usuario " + nick);
            return trino;
        } else {
            return null;
        }
    }

    public synchronized void registrarCallback(String nick, CallbackUsuarioInterface callbackClient){
        if (!listaUsuarios.containsKey(nick)) {
            System.out.println(Utils.hora() + "El usuario " + nick + " ha iniciado la sesion");
            listaUsuarios.put(nick, callbackClient);
        }

    }

    public synchronized void removeRegistroCallback(String nick){
        if (listaUsuarios.containsKey(nick)) {
            System.out.println(Utils.hora() + "El usuario " + nick + " se ha desconectado del servidor");
            listaUsuarios.remove(nick);
        } else {
            System.out.println(Utils.hora() + "ERROR: No hay ningun callback del usuario: " + nick);
        }

    }
}
