package proyecto_desarrollo_movil;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Parques {

    @PrimaryKey
    @NonNull
    public String idParque;

    public String Nombre;
    public String Direccion;

    public Parques(@NonNull String idParque, String Nombre, String Direccion){
        this.idParque = idParque;
        this.Nombre = Nombre;
        this.Direccion = Direccion;
    }
}
