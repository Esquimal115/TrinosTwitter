package Common;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String nombre;
    private String nick;
    private String password;
    private ArrayList<String> seguidores;

    public Usuario(String nombre, String nick, String password) {
        this.nombre = nombre;
        this.nick = nick;
        this.password = password;
        this.seguidores = new ArrayList();
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getNick() {
        return this.nick;
    }

    public String getPassword() {
        return this.password;
    }

    public ArrayList<String> getSeguidores() {
        return this.seguidores;
    }

    public boolean addSeguidor(String seguirA) {
        if (this.seguidores.contains(seguirA)) {
            return false;
        } else {
            this.seguidores.add(seguirA);
            return true;
        }
    }

    public boolean removeSeguidor(String seguirA) {
        if (this.seguidores.contains(seguirA)) {
            this.seguidores.remove(seguirA);
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return this.nombre + " | " + this.nick + " | " + this.seguidores.toString();
    }
}
