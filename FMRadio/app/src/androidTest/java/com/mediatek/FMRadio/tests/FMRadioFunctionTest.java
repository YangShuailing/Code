package com.mediatek.FMRadio.tests;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

import com.mediatek.FMRadio.FMRadioActivity;
import com.mediatek.FMRadio.FMRadioFavorite;
import com.mediatek.FMRadio.FMRadioStation;
import com.mediatek.FMRadio.R;

public class FMRadioFunctionTest extends ActivityInstrumentationTestCase2<FMRadioActivity> {
    private Instrumentation mInstrumentation = null;
    private Context mContext = null;
    private ActivityMonitor mActivityMonitor = null;
    private FMRadioActivity mFMRadioActivity = null;
    private FMRadioFavorite mFMRadioFavorite = null;
    // bottom bar
    private ImageButton mButtonDecrease = null;
    private ImageButton mButtonPrevStation = null;
    private ImageButton mButtonNextStation = null;
    private ImageButton mButtonIncrease = null;
    // top bar
    private ImageButton mButtonFavorite = null;
    private ImageButton mButtonPlayStop = null;
    private ImageButton mButtonEarLoud = null;
    // the star
    private ImageButton mButtonAddToFavorite = null;
    // record bar
    private ImageButton mButtonRecord = null;
    private ImageButton mButtonStop = null;
    private ImageButton mButtonPlayback = null;
    private AlertDialog mDialogRDSSetting = null;
    private static final String CONTENTURI = "content://com.mediatek.FMRadio.FMRadioContentProvider/station";

    private TextView mTextViewFrequency = null;
    private static final int CONVERT_RATE = 10;
    private static final long RECORDING_TIME = 30000;
    private static final long CHECK_TIME = 100;
    private static final long SHORT_TIME = 2000;
    private static final long TIMEOUT = 5000;
    private static final String TAG = "FMRadioFunctionTest";

    public FMRadioFunctionTest() {
        super("com.mediatek.FMRadio", FMRadioActivity.class);
    }

    public FMRadioFunctionTest(String pkg, Class<FMRadioActivity> activityClass) {
        super("com.mediatek.FMRadio", FMRadioActivity.class);
    }

    @Override
    protected void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, ">>>set up");
        setActivityInitialTouchMode(false);
        mInstrumentation = this.getInstrumentation();
        assertNotNull(mInstrumentation);
        mFMRadioActivity = getActivity();
        assertNotNull(mFMRadioActivity);
        waitForPowerupWithTimeout(TIMEOUT);
        waitForInitedWithTimeout(TIMEOUT);
        mButtonDecrease = (ImageButton) mFMRadioActivity.findViewById(R.id.button_decrease);
        mButtonPrevStation = (ImageButton) mFMRadioActivity.findViewById(R.id.button_prevstation);
        mButtonNextStation = (ImageButton) mFMRadioActivity.findViewById(R.id.button_nextstation);
        mButtonIncrease = (ImageButton) mFMRadioActivity.findViewById(R.id.button_increase);

        mButtonFavorite = (ImageButton) mFMRadioActivity.findViewById(R.id.button_favorite);
        mButtonPlayStop = (ImageButton) mFMRadioActivity.findViewById(R.id.button_play_stop);
        mButtonEarLoud = (ImageButton) mFMRadioActivity.findViewById(R.id.button_ear_loud);

        mButtonAddToFavorite = (ImageButton) mFMRadioActivity
                .findViewById(R.id.button_add_to_favorite);

        mButtonRecord = (ImageButton) mFMRadioActivity.findViewById(R.id.btn_record);
        mButtonStop = (ImageButton) mFMRadioActivity.findViewById(R.id.btn_stop);
        mButtonPlayback = (ImageButton) mFMRadioActivity.findViewById(R.id.btn_playback);

        mTextViewFrequency = (TextView) mFMRadioActivity.findViewById(R.id.station_value);

        Log.i(TAG, "<<<set up");

    }

    public void testCase01_PowerUpAndPowerDown() {
        boolean isPlaying = false;
        // test FM power up
        makeFMPowerDown();
        clickView(mButtonPlayStop);
        mInstrumentation.waitForIdleSync();
        assertTrue(mButtonEarLoud.isEnabled());
        assertTrue(mButtonDecrease.isEnabled());
        assertTrue(mButtonPrevStation.isEnabled());
        assertTrue(mButtonNextStation.isEnabled());
        assertTrue(mButtonIncrease.isEnabled());

        // test FM power down
        makeFMPowerUp();
        clickView(mButtonPlayStop);
        mInstrumentation.waitForIdleSync();
        assertFalse(mButtonEarLoud.isEnabled());
        assertFalse(mButtonDecrease.isEnabled());
        assertFalse(mButtonPrevStation.isEnabled());
        assertFalse(mButtonNextStation.isEnabled());
        assertFalse(mButtonIncrease.isEnabled());

    }

    public void testCase02_SwitchSpeakerAndEarphone() {
        if (!mButtonEarLoud.isEnabled()) {
            makeFMPowerUp();
        }
        // test FM switch to speaker
        boolean isEarphoneUsed = true;
        switchEarphone();
        clickView(mButtonEarLoud);
        mInstrumentation.waitForIdleSync();
        isEarphoneUsed = getBooleanFromMethod(mFMRadioActivity, "isEarphoneUsed");
        assertFalse(isEarphoneUsed);

        // test FM switch to earphone
        isEarphoneUsed = false;
        switchSpeaker();
        clickView(mButtonEarLoud);
        mInstrumentation.waitForIdleSync();
        isEarphoneUsed = getBooleanFromMethod(mFMRadioActivity, "isEarphoneUsed");
        assertTrue(isEarphoneUsed);

    }

    public void testCase03_AddDeleteFavoriteChannels() {
        int station = 0;
        station = getStationFromUI();
        // test add channel as favorite
        deleteChannelFromFavorite(station);
        clickView(mButtonAddToFavorite);
        mInstrumentation.waitForIdleSync();
        sleep(SHORT_TIME);
        assertTrue(FMRadioStation.isFavoriteStation(mFMRadioActivity, station));

        // test delete channel from favorite
        station = getStationFromUI();
        addChannelAsFavorite(station);
        clickView(mButtonAddToFavorite);
        mInstrumentation.waitForIdleSync();
        sleep(SHORT_TIME);
        assertFalse(FMRadioStation.isFavoriteStation(mFMRadioActivity, station));
    }

    public void testCase04_TuneFrequency() {
        int tuneStation = 0, currentStation = 0;
        if (!mButtonDecrease.isEnabled()) {
            makeFMPowerUp();
        }
        // test decrease 0.1 MHZ
        tuneStation = getStationFromUI();
        clickView(mButtonDecrease);
        mInstrumentation.waitForIdleSync();
        tuneStation--;
        if (tuneStation < FMRadioStation.LOWEST_STATION) {
            tuneStation = FMRadioStation.HIGHEST_STATION;
        }
        currentStation = FMRadioStation.getCurrentStation(mFMRadioActivity);
        assertEquals(tuneStation, currentStation);

        // test increase 0.1 MHZ
        tuneStation = getStationFromUI();
        clickView(mButtonIncrease);
        mInstrumentation.waitForIdleSync();
        tuneStation++;
        if (tuneStation > FMRadioStation.HIGHEST_STATION) {
            tuneStation = FMRadioStation.LOWEST_STATION;
        }
        currentStation = FMRadioStation.getCurrentStation(mFMRadioActivity);
        assertEquals(tuneStation, currentStation);
    }

    public void testCase05_SwitchChannel() {
        int oldStation = 0, stationFromUI = 0, stationFromDB = 0;
        if (!mButtonPrevStation.isEnabled()) {
            makeFMPowerUp();
        }

        // test seek previous station
        oldStation = getStationFromUI();
        clickView(mButtonPrevStation);
        mInstrumentation.waitForIdleSync();
        stationFromUI = getStationFromUI();
        stationFromDB = FMRadioStation.getCurrentStation(mFMRadioActivity);
        assertTrue(oldStation != stationFromUI);
        assertEquals(stationFromUI, stationFromDB);

        // test seek next station
        oldStation = getStationFromUI();
        clickView(mButtonNextStation);
        mInstrumentation.waitForIdleSync();
        stationFromUI = getStationFromUI();
        stationFromDB = FMRadioStation.getCurrentStation(mFMRadioActivity);
        assertTrue(oldStation != stationFromUI);
        assertEquals(stationFromUI, stationFromDB);
    }

    /*public void testCase06_SearchChannels() {
        if (!mButtonEarLoud.isEnabled()) {
            makeFMPowerUp();
        }
        mFMRadioActivity.closeOptionsMenu();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        mInstrumentation.waitForIdleSync();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        mInstrumentation.waitForIdleSync();
        sleep(10000);
        clickView(mButtonFavorite);
        mInstrumentation.waitForIdleSync();
        mActivityMonitor = new ActivityMonitor("com.mediatek.FMRadio.FMRadioFavorite", null, false);
        mInstrumentation.addMonitor(mActivityMonitor);
        mFMRadioFavorite = (FMRadioFavorite) mActivityMonitor.waitForActivityWithTimeout(15000);
        assertNotNull(mFMRadioFavorite);
        Uri uri = Uri.parse(CONTENTURI);
        Cursor c = mFMRadioFavorite.getContentResolver().query(
                uri,
                new String[] { FMRadioStation.Station._ID,
                        FMRadioStation.Station.COLUMN_STATION_FREQ }, null, null, null);
        assertTrue(c != null && c.getCount() > 0);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int station = c.getInt(1);
                assertTrue(isExistInChannelList(station));
                c.moveToNext();
            }
        }
        if (c != null) {
            c.close();
        }
    }*/

    public void testCase07_EnterRecordingMode() {
        enterRecordingMode();
        assertTrue(mButtonRecord.getVisibility() == View.VISIBLE);
        assertTrue(mButtonStop.getVisibility() == View.VISIBLE);
        assertTrue(mButtonPlayback.getVisibility() == View.VISIBLE);
    }

    /*public void testCase08_RecordingAndPlayback() {
        enterRecordingMode();
        assertTrue(mButtonRecord.getVisibility() == View.VISIBLE);
        // judge whether enter recording mode
        assertTrue(mButtonRecord.isEnabled());
        // test recording
        clickView(mButtonRecord);
        mInstrumentation.waitForIdleSync();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(mButtonStop.isEnabled());
        assertFalse(mButtonPlayback.isEnabled());
        // make recording time have 3 seconds
        try {
            Thread.sleep(RECORDING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // test stop reocording and save recording file
        clickView(mButtonStop);
        mInstrumentation.waitForIdleSync();
        // because the edit text of save recording dialog request focus, just sendkey
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_F);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_M);
        mInstrumentation.waitForIdleSync();
        // make focus at OK button of save recording dialog
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        // click OK button of save recording dialog
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        mInstrumentation.waitForIdleSync();
        assertTrue(mButtonPlayback.isEnabled());
        assertTrue(mButtonRecord.isEnabled());

        // test playback recording file clickView(mButtonPlayback);
        mInstrumentation.waitForIdleSync();
        assertTrue(mButtonStop.isEnabled());
        assertFalse(mButtonRecord.isEnabled());

        // test stop playback recording file

        clickView(mButtonStop);
        mInstrumentation.waitForIdleSync();
        assertTrue(mButtonRecord.isEnabled());
        assertTrue(mButtonPlayback.isEnabled());
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);

    }

    public void testCase09_EnterChannelList() {
        clickView(mButtonFavorite);
        mActivityMonitor = new ActivityMonitor("com.mediatek.FMRadio.FMRadioFavorite", null, false);
        mInstrumentation.addMonitor(mActivityMonitor);
        mFMRadioFavorite = (FMRadioFavorite) mActivityMonitor.waitForActivityWithTimeout(TIMEOUT);
        assertNotNull(mFMRadioFavorite);
        Uri uri = Uri.parse(CONTENTURI);
        Cursor c = mFMRadioFavorite.getContentResolver().query(
                uri,
                new String[] { FMRadioStation.Station._ID,
                        FMRadioStation.Station.COLUMN_STATION_FREQ }, null, null, null);
        assertTrue(c != null && c.getCount() > 0);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int stationFreq = c.getInt(1);
                assertTrue(isExistInChannelList(stationFreq));
                c.moveToNext();
            }
        }
        if (c != null) {
            c.close();
        }
    }*/

    private void enterRecordingMode() {
        if (View.INVISIBLE == mButtonRecord.getVisibility()) {
            makeFMPowerUp();
        }
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        mInstrumentation.waitForIdleSync();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_LEFT);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        mInstrumentation.waitForIdleSync();
    }

    private boolean isExistInChannelList(int station) {
        boolean find = false;
        float frequency = 0;
        int stationInList = 0;
        ListView listView = (ListView) mFMRadioFavorite.findViewById(R.id.station_list);
        assertNotNull(listView);
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            TextView textView = (TextView) view.findViewById(R.id.lv_station_freq);
            String frequencyStr = textView.getText().toString();
            try {
                frequency = Float.parseFloat(frequencyStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stationInList = (int) (frequency * CONVERT_RATE);
            if (station == stationInList) {
                return true;
            }
        }
        return false;

    }

    private int getStationFromUI() {
        int station = 0;
        float frequency = 0;
        mTextViewFrequency = (TextView) mFMRadioActivity.findViewById(R.id.station_value);
        String frequencyStr = mTextViewFrequency.getText().toString();
        try {
            frequency = Float.parseFloat(frequencyStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        station = (int) (frequency * CONVERT_RATE);
        return station;

    }

    private void switchEarphone() {
        if (!mButtonEarLoud.isEnabled()) {
            makeFMPowerUp();
        }
        boolean isEarphoneUsed = false;
        isEarphoneUsed = getBooleanFromMethod(mFMRadioActivity, "isEarphoneUsed");
        if (!isEarphoneUsed) {
            clickView(mButtonEarLoud);
            mInstrumentation.waitForIdleSync();
        }
    }

    private void switchSpeaker() {
        if (!mButtonEarLoud.isEnabled()) {
            makeFMPowerUp();
        }
        boolean isEarphoneUsed = true;
        isEarphoneUsed = getBooleanFromMethod(mFMRadioActivity, "isEarphoneUsed");
        if (isEarphoneUsed) {
            clickView(mButtonEarLoud);
            mInstrumentation.waitForIdleSync();
        }

    }

    private void deleteChannelFromFavorite(int station) {
        if (FMRadioStation.isFavoriteStation(mFMRadioActivity, station)) {
            clickView(mButtonAddToFavorite);
            mInstrumentation.waitForIdleSync();
            sleep(SHORT_TIME);
        }
    }

    private void addChannelAsFavorite(int station) {
        if (!FMRadioStation.isFavoriteStation(mFMRadioActivity, station)) {
            clickView(mButtonAddToFavorite);
            mInstrumentation.waitForIdleSync();
            sleep(SHORT_TIME);
        }

    }

    private void makeFMPowerUp() {
        boolean isPlaying = false;
        isPlaying = getBooleanFromVariable(mFMRadioActivity, "mbPlaying");
        if (!isPlaying) {
            clickView(mButtonPlayStop);
        }
        mInstrumentation.waitForIdleSync();
       
    }

    private void makeFMPowerDown() {
        boolean isPlaying = true;
        isPlaying = getBooleanFromVariable(mFMRadioActivity, "mbPlaying");
        if (isPlaying) {
            clickView(mButtonPlayStop);
        }
        mInstrumentation.waitForIdleSync();
        
    }

    private boolean getBooleanFromMethod(Activity activity, String method) {

        boolean value = false;
        Class c = mFMRadioActivity.getClass();
        try {
            Method m = (Method) c.getDeclaredMethod(method, new Class[] {});
            m.setAccessible(true);
            value = (Boolean) m.invoke(activity, new Object[] {});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    private boolean getBooleanFromVariable(Activity activity, String variable) {
        Field field = null;
        boolean value = false;
        try {
            field = FMRadioActivity.class.getDeclaredField(variable);
            field.setAccessible(true);
            value = ((Boolean) field.get(activity)).booleanValue();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void clickView(final View view) {
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.performClick();

                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForInitedWithTimeout(long timeOut) {
        Log.i(TAG, ">>>waitForInitedWithTimeout");

        long startTime = System.currentTimeMillis();
        while (!getBooleanFromVariable(mFMRadioActivity, "mbInited")) {
            if (System.currentTimeMillis() - startTime > timeOut) {
                break;
            }
            sleep(CHECK_TIME);
        }
        Log.i(TAG, "<<<waitForInitedWithTimeout");

    }

    private void waitForPowerupWithTimeout(long timeOut) {
        Log.i(TAG, ">>>waitForPowerupWithTimeout");

        long startTime = System.currentTimeMillis();
        while (!getBooleanFromVariable(mFMRadioActivity, "mbPlaying")) {
            if (System.currentTimeMillis() - startTime > timeOut) {
                break;
            }
            sleep(CHECK_TIME);
        }
        Log.i(TAG, "<<<waitForPowerupWithTimeout");
    }

    @Override
    protected void tearDown() {
        Log.i(TAG, ">>>tear down");
        if (mFMRadioActivity != null) {
            mFMRadioActivity.finish();
            mFMRadioActivity = null;
        }
        if (mFMRadioFavorite != null) {
            mFMRadioFavorite.finish();
            mFMRadioFavorite = null;
        }
        // mInstrumentation.removeMonitor(mActivityMonitor);
        if (mActivityMonitor != null) {
            mActivityMonitor = null;
        }
        if (mInstrumentation != null) {
            mInstrumentation = null;
        }
        try {
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, ">>>tear down");
    }

}
