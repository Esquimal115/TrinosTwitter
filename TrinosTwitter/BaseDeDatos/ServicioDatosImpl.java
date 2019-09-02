package BaseDeDatos;

import Common.Trino;
import Common.Usuario;
import Common.Utils;
import Common.ServicioDatosInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface {
    private HashMap<String, Integer> usuarioConectado = new HashMap();
    private ArrayList<Usuario> listaUsuario = new ArrayList();
    private ArrayList<Trino> listaTrinos = new ArrayList();
    private ArrayList<Trino> listaTrinosPendientes = new ArrayList();
    private int userID = 0;

    public ServicioDatosImpl() throws RemoteException {
        super();
    }

    public void addUsuario(Usuario usuario) {
        this.getListaUsuario().add(usuario);
        System.out.println(Utils.hora() + "Nuevo usuario registrado");
    }

    public Usuario getUsuario(String nick) {

        for (int i = 0; i < this.getListaUsuario().size(); ++i) {
            if (( this.getListaUsuario().get(i)).getNick().equals(nick)) {
                return this.getListaUsuario().get(i);
            }
        }

        return null;
    }

    public String[] getAllUsers() {

        System.out.println(Utils.hora() + "*** Lista de usuarios ***\n");
        String[] todosUsuarios = new String[this.getListaUsuario().size()];

        for (int i = 0; i < this.getListaUsuario().size(); ++i) {
            todosUsuarios[i] = (this.getListaUsuario().get(i)).getNick();
        }
        return todosUsuarios;
    }

    public ArrayList<Usuario> getListaUsuario() {
        return this.listaUsuario;
    }

    public void addSeguidor(String nick, String seguirA)  {
        this.getUsuario(seguirA).addSeguidor(nick);
        System.out.println(Utils.hora() + "Actualizada la información de " + seguirA);
    }

    public void removeSeguidor(String nick, String dejarA){
        this.getUsuario(dejarA).removeSeguidor(nick);
        System.out.println(Utils.hora() + "Actualizada la información de " + nick);
    }

    public void addTrino(Trino trino) {
        this.getListaTrinos().add(trino);
        System.out.println(Utils.hora() + trino.ObtenerNickPropietario() + " ha escrito un mensaje");
    }

    public void addTrinoPendiente(Trino trinoPendiente) {
        this.listaTrinosPendientes.add(trinoPendiente);
        System.out.println(Utils.hora() + "Se añade un trino a la listaTrinosPendientes.");
    }

    public Trino getTrinoPendiente(String nick) {
        for (int i = 0; i < this.listaTrinosPendientes.size(); ++i) {
            Trino trino = this.listaTrinosPendientes.get(i);
            String texto = trino.ObtenerNickPropietario();
            if (trino != null && texto.equals(nick)) {
                System.out.println(Utils.hora() + "Se elimina un trino de TrinosPendiente.");
                this.listaTrinosPendientes.remove(i);
                return trino;
            }
        }

        return null;
    }

    public ArrayList<Trino> getListaTrinos() {
        return this.listaTrinos;
    }

    public ArrayList<Trino> getListaTrinosPendientes() {
        return this.listaTrinosPendientes;
    }

    public int asigUserID(String nick) {
        this.usuarioConectado.put(nick, ++this.userID);
        return this.userID;
    }

    public int getUserID(String nick) {
        return this.usuarioConectado.get(nick) == null ? -1 : this.usuarioConectado.get(nick);
    }

    public int removeUserID(String nick) {
        return this.usuarioConectado.remove(nick);
    }
}
