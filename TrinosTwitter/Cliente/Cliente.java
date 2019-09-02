package Cliente;

import Common.Trino;
import Common.Usuario;
import Common.Utils;
import Common.CallbackUsuarioInterface;
import Common.ServicioAutenticacionInterface;
import Common.ServicioGestorInterface;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class Cliente {

    private static ServicioAutenticacionInterface servAutentificacion;
    private static ServicioGestorInterface servGestor;
    private static CallbackUsuarioInterface callback;
    private static String dirServAutentificacion;
    private static String dirServGestor;
    private static int logID = -1;
    private static String nick = "";

    public static void main(String[] args) throws IOException {

        dirServAutentificacion = "rmi://localhost:8888/ServicioAutenticacion";
        dirServGestor = "rmi://localhost:8888/ServicioGestor";
        Utils.arrancarRegistro(8888);

        try {
            servAutentificacion = (ServicioAutenticacionInterface)Naming.lookup(dirServAutentificacion);
            servGestor = (ServicioGestorInterface)Naming.lookup(dirServGestor);
        } catch (NotBoundException var2) {
            JOptionPane.showMessageDialog(null, "No se puede conectar con el servidor.");
                System.exit(0);

        }

        callback = new CallbackUsuarioImpl();

        Scanner sc = new Scanner(System.in);
        int opcion;

        do{
            System.out.println(" \n===== Menu Usuario===== ");
            System.out.println("1. Registrarse");
            System.out.println("2. Login");
            System.out.println("3. Cerrar\n");


            opcion = sc.nextInt();

            switch (opcion){

                case 1:

                    System.out.print("Nombre de Usuario: ");
                    String nombre = sc.next();
                    System.out.print("Nick: ");
                    String apodo = sc.next();
                    System.out.print("Password: ");
                    String contra = sc.next();
                    registrar(nombre, apodo, contra);
                    break;

                case 2:

                    System.out.print("Nick: ");
                    apodo = sc.next();
                    System.out.print("Password: ");
                    contra = sc.next();
                    login(apodo, contra);
                    if (logID >= 0){
                        trinosPendientes();
                        menuUser();
                    }
                    break;

                case 3:
                    System.exit(1);
                    break;

                case 4:

            }
        }while (opcion >= 1 && opcion < 4);

    }

    private static void menuUser(){
        Scanner sc = new Scanner(System.in);
        int opcion;
        String trino;

        do{
            System.out.println(" \n===== Menu Usuario===== ");
            System.out.println("1. Información del Usuario");
            System.out.println("2. Enviar Trino");
            System.out.println("3. Seguir A...       4. Dejar de Seguir A...");
            System.out.println("5. Mostrar Usuarios");
            System.out.println("6. Logout\n");


            opcion = sc.nextInt();
            System.out.println("\n");

            switch (opcion){

                case 1:
                    informacionUsuario();
                    break;

                case 2:
                    trino = JOptionPane.showInputDialog("Escriba su Trino: ");
                    enviarTrino(trino);
                    break;

                case 3:
                    System.out.print("A quien desea seguir? ");
                    String seguidor = sc.next();
                    follow(seguidor);
                    break;

                case 4:
                    System.out.print("A quien desea dejar de seguir? ");
                    seguidor = sc.next();
                    unfollow(seguidor);
                    break;

                case 5:
                    System.out.print("Los usuarios son: \n");
                    obtenerTodosUsuarios();

                case 6:
                    logout();
                    break;
            }
        }while (opcion >= 1 && opcion < 6);
    }

    private static void registrar(String nombre, String nick, String pass) {
        try {
            if (servAutentificacion.registrarUsuario(new Usuario(nombre, nick, pass))) {
                JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "No se ha podido registrar al usuario, " +
                        "nick en uso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: No se puede conectar con el servidor");
        }

    }

    private static boolean login(String usuario, String password) {
        try {
            logID = servAutentificacion.iniciarSesionUsuario(usuario, String.valueOf(password));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: No se puede conectar con el servidor," +
                    "\npara iniciar sesion.", "Inicio Sesion", 1);
            return false;
        }

        if (logID == -1) {
            JOptionPane.showMessageDialog(null, "El usuario ya esta online.",
                    "Inicio Sesion", 1);
            return false;
        } else if (logID == -2) {
            JOptionPane.showMessageDialog(null, "Usuario/Contraseña incorrecta.",
                    "Inicio Sesion", 1);
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "Sesion iniciada correctamente.",
                    "Inicio Sesion", 1);
            nick = usuario;

            try {
                servGestor.registrarCallback(nick, callback);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "No se ha podido registrar el callback\n"
                        + e, "ERROR", 0);
            }

            return true;
        }
    }


    private static void informacionUsuario() {
        System.out.println("\n=== Informacion sobre el Usuario ===\n");

        try {
            Usuario usuario = servGestor.getInfoUsuario(nick);
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Nick: " + usuario.getNick());
        } catch (Exception var1) {
            System.out.println("ERROR: No se puede conectar con el servidor\n");
        }

    }

    private static void enviarTrino(String textoTrino) {
        try {
            servGestor.addTrino(nick, textoTrino);
        } catch (Exception e) {
            System.out.println("ERROR. No se puede conectar con el servidor," +
                    "\nno se ha almacenado ningun trino.\n");
            return;
        }

        String trinoCompleto = "> " + nick + "# " + textoTrino;
        System.out.println(trinoCompleto);
    }

    private static void trinosPendientes() {
        try {
            Trino trino = servGestor.trinosPendientes(nick);
            if (trino == null) {
               System.out.println("\n\nSin mensajes...\n");
            } else {
                System.out.println("\n\nMensajes pendientes: \n");

                while(trino != null) {
                    System.out.println(trino.ObtenerTrino());
                    trino = servGestor.trinosPendientes(nick);
                }
            }
        } catch (Exception e) {
            System.out.println("Fallo al conectar con el servidor,\n" +
                    "no se pueden obtener los trinos pendientes\n.");
        }

    }


    private static void obtenerTodosUsuarios() {
        String listaUsuario = "\n=== Lista de Usuarios del Sistema: ===\n";

        try {
            String[] listaUsuarios = servGestor.getAllUsers();

            for(int i = 0; i < listaUsuarios.length; ++i) {
                listaUsuario = listaUsuario + (i+1) + " - " + listaUsuarios[i] + "\n";
            }
        } catch (RemoteException var3) {
            System.out.println("ERROR: No se puede conectar con el servidor,\n" +
                    "no se ha podido recuperar la lista de los usuarios.\n");
            return;
        }

        System.out.println(listaUsuario);
    }

    private static void follow(String follow) {
        if (nick.equals(follow)) {
            System.out.println("ERROR: No te puedes seguir a ti mismo.\n");
        } else {
            try {
                if (servGestor.addSeguidor(nick, follow)) {
                    System.out.println("Ahora sigues al usuario: " + follow + "\n");
                } else {
                    System.out.println("No se ha podigo seguir al usuario " + follow + "\n");
                }

            } catch (RemoteException e) {
                System.out.println("ERROR: No se puede conectar con el servidor," +
                        "\n no se ha seguido al usuario " + follow + ".\n");
            }
        }
    }

    private static void unfollow(String unfollow) {
        if (nick.equals(unfollow)) {
            System.out.println("\nERROR: No te puedes seguir a ti mismo\n");
        } else {
            try {
                if (servGestor.removeSeguidor(nick, unfollow)) {
                    System.out.println("Dejas de seguir al usuario: " + unfollow + "\n");
                } else {
                    System.out.println("No se ha podigo dejar de seguir al usuario " + unfollow + "\n");
                }

            } catch (RemoteException e) {
                System.out.println("ERROR: No se puede conectar con el servidor," +
                        "\nno se puede dejar de seguir al usuario: " + unfollow + ".\n");
            }
        }
    }

    public static void mostrarTrino(String texto) {
        System.out.println(texto);
    }

    private static void logout() {
        try {
            servAutentificacion.cerrarSesionUsuario(nick);
            servGestor.removeRegistroCallback(nick);
        } catch (Exception var1) {
            JOptionPane.showMessageDialog(null, "ERROR: No se ha podido realizar el logout"
                    , "Fin de Sesion", 1);
        }

    }
}

