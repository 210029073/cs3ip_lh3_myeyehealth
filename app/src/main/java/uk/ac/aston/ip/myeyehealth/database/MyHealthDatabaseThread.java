package uk.ac.aston.ip.myeyehealth.database;

import android.content.Context;

import androidx.room.Room;

public abstract class MyHealthDatabaseThread extends MyEyeHealthDatabase implements Runnable{

    private static Context context;

    @Override
    public void run() {
        getInstance();
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public static MyEyeHealthDatabase getInstance() {

        if(MyEyeHealthDatabase.instance == null) {
            MyEyeHealthDatabase.instance = Room.databaseBuilder(context.getApplicationContext(), MyEyeHealthDatabase.class, "myeyehealth.db")
                    .build();
        }
        return instance;
    }
}
