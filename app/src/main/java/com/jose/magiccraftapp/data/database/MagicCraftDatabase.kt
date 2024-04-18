package com.jose.magiccraftapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jose.magiccraftapp.data.dao.CartaDao
import com.jose.magiccraftapp.data.dao.MazoDao
import com.jose.magiccraftapp.data.dao.UsuarioDao
import com.jose.magiccraftapp.data.entity.Carta
import com.jose.magiccraftapp.data.entity.Mazo
import com.jose.magiccraftapp.data.entity.Usuario

@Database(entities = [Carta::class, Mazo::class, Usuario::class], version = 1)
abstract class MagicCraftDatabase: RoomDatabase() {

    abstract fun cartaDao(): CartaDao

    abstract fun mazoDao(): MazoDao

    abstract fun usuarioDao(): UsuarioDao

    companion object{
        private const val DATABASE_NAME = "magicCraft"
        @Volatile
        private var INSTANCE: MagicCraftDatabase? = null

        fun getInstance(context: Context): MagicCraftDatabase? {
            INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MagicCraftDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }
}