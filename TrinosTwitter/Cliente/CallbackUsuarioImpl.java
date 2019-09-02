package Cliente;

import Common.Trino;
import Common.CallbackUsuarioInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackUsuarioImpl extends UnicastRemoteObject implements CallbackUsuarioInterface {

    protected CallbackUsuarioImpl() throws RemoteException {
    }

    public String notifica(Trino trino){
        String returnMensaje = "> " + trino.ObtenerNickPropietario() + "# " + trino.ObtenerTrino();
        Cliente.mostrarTrino(returnMensaje + "\n");
        return returnMensaje;
    }
}