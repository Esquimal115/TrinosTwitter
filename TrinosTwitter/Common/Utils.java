package Common;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public Utils() {
    }

    public static String hora() {
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");
        return hourdateFormat.format(date) + ": ";
    }

    public static void arrancarRegistro(int puertoRMI) throws RemoteException {
        try {
            Registry registro = LocateRegistry.getRegistry(puertoRMI);
            registro.list();
        } catch (RemoteException var2) {
            LocateRegistry.createRegistry(puertoRMI);
        }

    }
}