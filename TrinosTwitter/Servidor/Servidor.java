package Servidor;

import Common.Utils;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;


public class Servidor {

    private static ServicioAutenticacionImpl servAutentificacion;
    private static ServicioGestorImpl servGestor;
    private static String DirAutentificador;
    private static String DirGestor;

    public static void main(String[] args) throws RemoteException {

        Utils.arrancarRegistro(8888);
        servAutentificacion = new ServicioAutenticacionImpl();
        DirAutentificador = "rmi://localhost:8888/ServicioAutenticacion";
        servGestor = new ServicioGestorImpl();
        DirGestor = "rmi://localhost:8888/ServicioGestor";

        try {
            Naming.rebind(DirAutentificador, servAutentificacion);
            Naming.rebind(DirGestor, servGestor);
            System.out.println(Utils.hora() + "*** Servidor funcionando CORRECTAMENTE ***\n");


            Scanner sc = new Scanner(System.in);
            int opcion;

        do{
            System.out.println(" \n===== Server Menu ===== ");
            System.out.println("1. Información del servidor");
            System.out.println("2. Ver Usuarios Conectados");
            System.out.println("3. Logout / Desconectarse\n");


            opcion = sc.nextInt();
            System.out.println("\n");

            switch (opcion){

                case 1:
                    serverInfo();
                    break;

                case 2:
                    conectedUsers();
                    break;

                case 3:
                    stopServer();
                    break;
            }
        }while (opcion >= 1 && opcion < 3);

        } catch (Exception e) {
            System.out.println(Utils.hora() + " ERROR en el bind\n" + e);

                System.exit(0);
        }
    }

    //Lista de usuarios conectados
    private static void conectedUsers() {
        System.out.println("\n=== Usuarios Conectados ===\n");
        Set<String> usuariosConectados = servGestor.getUsuarioOnline();
        if (usuariosConectados.isEmpty()) {
            System.out.println("No hay usuarios conectados\n");
        } else {
            Iterator iterator = usuariosConectados.iterator();

            while(iterator.hasNext()) {
                String user = (String)iterator.next();
                System.out.println(user);
            }
        }
    }

    //Información del Server
    private static void serverInfo() {
        System.out.println("\n=== Informacion del Servidor ===\n");
        System.out.println("Los objetos registrados se encuentra en la direccion:\n");
        System.out.println(DirAutentificador);
        System.out.println(DirGestor + "\n");
        System.out.println("Actualmente hay " + servGestor.getUsuarioOnline().size() + " usuarios online\n");
    }

    //Detención del Server
    private static void stopServer() {
        try {
            Naming.unbind(DirAutentificador);
            Naming.unbind(DirGestor);
            UnicastRemoteObject.unexportObject(servAutentificacion, true);
            UnicastRemoteObject.unexportObject(servGestor, true);
            System.out.println(Utils.hora() + "Servidor DETENIDO\n");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println(Utils.hora() + "ERROR: No se puede hacer el unbind de los objetos.\n" + e);
        }

    }
}

