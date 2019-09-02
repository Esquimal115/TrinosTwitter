package Common;

import java.io.Serializable;
import java.util.Date;

public class Trino implements Serializable {

    private String trino;
    private String nickPropietario;
    private long timestamp;

    public Trino(String trino, String nickPropietario) {
        this.trino = trino;
        this.nickPropietario = nickPropietario;
        Date date = new Date();
        this.timestamp = date.getTime();
    }

    public String ObtenerTrino() {
        return this.trino;
    }

    public String ObtenerNickPropietario() {
        return this.nickPropietario;
    }

    public long ObtenerTimestamp() {
        return this.timestamp;
    }

    public String toString() {
        return this.getClass().getName() + "@" + this.trino + "|" + this.nickPropietario + "|" + this.timestamp + "|";
    }
}
