package com.example.memorypract.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.memorypract.data.modelos.MetricaEntity;
import com.example.memorypract.data.modelos.Usuario;

@Database(entities = {Usuario.class, MetricaEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    public abstract UsuarioDao usuarioDao();
    public abstract MetricaDao metricaDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.i(TAG, "Ejecutando migración MIGRATION_1_2: creando tabla_metricas");
            database.execSQL("CREATE TABLE IF NOT EXISTS `tabla_metricas` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`usuario_correo` TEXT, " +
                    "`categoria` TEXT, " +
                    "`aciertos` INTEGER NOT NULL, " +
                    "`fallos` INTEGER NOT NULL, " +
                    "`fecha_timestamp` INTEGER NOT NULL)");
        }
    };

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    Log.d(TAG, "Creando e inicializando la instancia de base de datos Room: memorypract_database");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "memorypract_database")
                            .addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration()
                            .build();
                    Log.i(TAG, "Base de datos Room creada exitosamente con MIGRATION_1_2");
                }
            }
        }
        return INSTANCE;
    }
}
