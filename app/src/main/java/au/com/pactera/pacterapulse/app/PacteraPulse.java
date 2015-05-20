package au.com.pactera.pacterapulse.app;

import android.app.Application;

/**
 * Application context
 * Created by kai on 20/05/15.
 */
public class PacteraPulse extends Application{

    private static PacteraPulse instance;

    public String getTOKEN()
    {
        return TOKEN;
    }

    public void setTOKEN(String token)
    {
        TOKEN = token;
    }

    private String TOKEN = "";

    @Override
    public void onCreate() {
        super.onCreate();
        new Config(this);
        instance = this;
    }

    public static PacteraPulse getInstance() {
        return instance;
    }
}
