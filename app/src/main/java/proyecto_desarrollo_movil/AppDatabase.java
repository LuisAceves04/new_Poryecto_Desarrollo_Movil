package proyecto_desarrollo_movil;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {proyecto_desarrollo_movil.Parques.class},
        version = 1
)

public abstract class AppDatabase extends RoomDatabase {
    public abstract proyecto_desarrollo_movil.DaoParque daoParque();


}
