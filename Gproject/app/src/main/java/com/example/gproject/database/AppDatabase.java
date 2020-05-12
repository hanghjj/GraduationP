package com.example.gproject.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.gproject.R;
import com.example.gproject.database.busstop.BusStopDAO;
import com.example.gproject.database.busstop.BusStopEntity;
import com.example.gproject.database.menu.MenuDAO;
import com.example.gproject.database.menu.MenuEntity;
import com.example.gproject.database.subwaystation.SubwayStationDAO;
import com.example.gproject.database.subwaystation.SubwayStationEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MenuEntity.class, SubwayStationEntity.class, BusStopEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // 싱글톤
    private static AppDatabase INSTANCE;

    public abstract MenuDAO menuDAO();

    public abstract SubwayStationDAO subwayStationDAO();

    public abstract BusStopDAO busStopDAO();

    // DB 객체생성 가져오기
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.sharedPreferences_name), Activity.MODE_PRIVATE);
            // 어플리케이션 최초 실행시에만 외부 DB를 가져온다
            if (pref.getBoolean("firstDB", true)) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("firstDB", false);
                editor.apply();

                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "bixiri-db").createFromAsset("bixiri-db").build();
            } else {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "bixiri-db").build();
            }
        }
        return INSTANCE;
    }

    // DB 객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
