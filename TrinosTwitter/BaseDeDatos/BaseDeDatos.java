package BaseDeDatos;

import Common.Trino;
import Common.Usuario;
import Common.Utils;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class BaseDeDatos {


    private static ServicioDatosImpl baseDatos;
    private static String dirBaseDeDatos;

    public static void main(String[] argv) throws RemoteException {

        Utils.arrancarRegistro(8888);
        baseDatos = new ServicioDatosImpl();
        dirBaseDeDatos = "rmi://localhost:8888/ServicioDatos";

        try {
            System.out.println("\n" + Utils.hora() + "BBDD funcionando CORRECTAMENTE");
            Naming.rebind(dirBaseDeDatos, baseDatos);


            Scanner sc = new Scanner(System.in);
            int opcion;

            do{
                System.out.println(" \n===== Menu BBDD===== ");
                System.out.println("1. Información de la BBDD");
                System.out.println("2. Ver Usuarios Registrados");
                System.out.println("3. Lista de Trinos\n");


                opcion = sc.nextInt();
                System.out.println("\n");

                switch (opcion){

                    case 1:
                        infoBBDD();
                        break;

                    case 2:
                        registerUsersBBDD();
                        break;

                    case 3:
                        listaTrinos();
                        break;

                    case 4:
                        stopBBDD();
                }
            }while (opcion >= 1 && opcion < 4);

        } catch (Exception e) {
            System.out.println("*** ERROR: No se ha podido registrar la Base de Datos. " + e + " ***\n");

                System.exit(0);
        }
    }

    //Información general de la BBDD
    private static void infoBBDD() {
        System.out.println("\n=== Informacion de la BD ===\n");
        System.out.println("Actualmente el objeto se encuentra en la direccion:\n");
        System.out.println(dirBaseDeDatos + "\n");
        System.out.println("En la Base de Datos hay almacenado::\n");
        System.out.println("- Usuarios: " + baseDatos.getListaUsuario().size());
        System.out.println("- Trinos: " + baseDatos.getListaTrinos().size());
        System.out.println("- Trinos Pendientes de enviar: " + baseDatos.getListaTrinosPendientes().size());
    }

    //Lista de usuarios registrados
    private static void registerUsersBBDD() {
        System.out.println("\n=== Usuarios registrados ===\n");
        System.out.println("Nº de usuarios registrados: " + baseDatos.getListaUsuario().size());
        System.out.println("Nombre || Nick || Usuarios que le siguen\n");
        if (baseDatos.getListaUsuario() != null && baseDatos.getListaUsuario().size() != 0) {
            for(int i = 0; i < baseDatos.getListaUsuario().size(); ++i) {
                Usuario temp = baseDatos.getListaUsuario().get(i);
                System.out.println(temp.getNombre() +  " || " + temp.getNick() + " || " + temp.getSeguidores());
            }
        } else {
            System.out.println("No existen usuarios registrados\n");
        }

    }

    //Listado de los trinos
    private static void listaTrinos() {

        System.out.println("\n=== Trinos ===\n");
        if (baseDatos.getListaTrinos() != null && baseDatos.getListaUsuario().size() != 0) {
            for(int i = 0; i < baseDatos.getListaTrinos().size(); ++i) {
                Trino temp = baseDatos.getListaTrinos().get(i);
                System.out.println(i + 1 + ". ");
                System.out.println(temp.ObtenerNickPropietario() + "# " + temp.ObtenerTrino());
            }
        } else {
            System.out.println("No hay trinos almacenados\n");
        }
    }

    //Detencion de la BBDD
    private static void stopBBDD() {
        try {
            Naming.unbind(dirBaseDeDatos);
            UnicastRemoteObject.unexportObject(baseDatos, true);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.out.println("*** ERROR: No se ha podido hacer el unbind del objeto" + e + " ***");
        } finally {
            System.out.println("Base de Datos offline.\n");
        }

    }


}
