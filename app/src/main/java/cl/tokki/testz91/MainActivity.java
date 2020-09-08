package cl.tokki.testz91;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zcs.sdk.Beeper;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Led;
import com.zcs.sdk.LedLightModeEnum;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.Sys;
import com.zcs.sdk.card.CardInfoEntity;
import com.zcs.sdk.print.PrnStrFormat;
import com.zcs.sdk.print.PrnTextFont;
import com.zcs.sdk.print.PrnTextStyle;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static DriverManager sDriverManager;
    public static Printer sPrinter;
    public static Sys sSys;
    public static Beeper sBeep;
    public static Led led;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sDriverManager = DriverManager.getInstance();
        sSys = sDriverManager.getBaseSysDevice();
        sPrinter = sDriverManager.getPrinter();
        sBeep = sDriverManager.getBeeper();
        led = sDriverManager.getLedDriver();

        int statue = sSys.getFirmwareVer(new String[1]);
        if (statue != SdkResult.SDK_OK) {
            int sysPowerOn = sSys.sysPowerOn();
            Log.i("SYS", "sysPowerOn: " + sysPowerOn);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int i = sSys.sdkInit();
        if (i == SdkResult.SDK_OK) {
            Log.i("INIT", "initializing");
        } else {
            Log.i("INIT FAILED", "Failed "+ i);
        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int status = sSys.getFirmwareVer(new String[1]);
                int printerStatus = sPrinter.getPrinterStatus();
                led.setLed(LedLightModeEnum.YELLOW, true);
                int beepp = sBeep.beep(4000, 600);

                PrnStrFormat format = new PrnStrFormat();
                format.setTextSize(25);
                format.setStyle(PrnTextStyle.NORMAL);
                format.setAli(Layout.Alignment.ALIGN_NORMAL);
                sPrinter.setPrintAppendString(" ", format);
                sPrinter.setPrintAppendString("Testing", format);
                sPrinter.setPrintAppendString("Las Kgo soy un genio", format);
                sPrinter.setPrintStart();

                Snackbar.make(view, "Replace with your own action "+printerStatus, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}