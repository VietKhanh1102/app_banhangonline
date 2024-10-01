package com.example.fashionapp.dataLocal;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fashionapp.Model.Product;

@androidx.room.Database( entities = {Product.class},version = 1)
@TypeConverters(ConvertersString.class)
public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME="dior.db";
    private static Database instance;

    public static synchronized Database getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),Database.class,DATABASE_NAME)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract DAO favouriteDAO();
}
