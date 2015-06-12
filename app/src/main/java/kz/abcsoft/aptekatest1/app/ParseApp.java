package kz.abcsoft.aptekatest1.app;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "yMnjSWvajnQ10B9oDidOmBA2dH7FrFh4SXeFaXiy", "TvTkVIFkKxRaWKAxk0po4lhOp3TqNoSTkBIXFik1");
    }
}
