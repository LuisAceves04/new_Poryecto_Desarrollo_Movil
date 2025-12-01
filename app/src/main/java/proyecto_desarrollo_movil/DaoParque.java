package proyecto_desarrollo_movil;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface DaoParque {
    @Query("SELECT * FROM Parques")
    List<Parques> ObtenerParques();
    @Query("SELECT * FROM Parques where Nombre = :nombreParque")
    Parques ObtenerParques(String nombreParque);
    @Insert
    void insertarParque(Parques...parques);
    @Query("UPDATE Parques set Nombre = :nuevoNombre, Direccion = :nuevaDireccion where Nombre = :nombreParqueActual")
    void ActualizarParque(String nombreParqueActual, String nuevoNombre, String nuevaDireccion);

    @Query("DELETE FROM Parques WHERE Nombre = :nombreParque")
    void BorrarParque(String nombreParque);


    List<Parques> obtenerParques();
}