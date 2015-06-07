package com.mediatek.FMRadio.tests;

import android.app.Instrumentation;
import android.database.Cursor;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mediatek.FMRadio.FMRadioActivity;
import com.mediatek.FMRadio.FMRadioStation;
import com.mediatek.FMRadio.R;
import com.mediatek.android.performance.util.ServiceBindHelper;
import com.mediatek.android.performance.util.ServiceBindHelper.ServiceHelperListener;

public class FMRadioTestCase1 extends ActivityInstrumentationTestCase2<FMRadioActivity> implements
        ServiceHelperListener {

    private static final String LOGTAG = "FMRadioPerformanceTest";
    private static final int HIGHSTATION = 1080;
    private static final int LOWSTATION = 875;
    private static final int STATIONMAXNUM = 100;
    private static final int TEN = 10;
    private static final long TIMEOUT = 5000;
    private static final long CHECKTIME = 100;

    private FMRadioActivity mActivity;
    private ServiceBindHelper mBindHelper;
    private Instrumentation mInstrumentation;

    private boolean mIsBindSucceeded = false;

    public FMRadioTestCase1(Class<FMRadioActivity> activityClass) {
        super(activityClass);
    }

    public FMRadioTestCase1() {
        super(FMRadioActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Log.i(LOGTAG, "super.setUp");
        setActivityInitialTouchMode(false);
        Log.i(LOGTAG, "setActivityInitialTouchMode");
        mActivity = getActivity();
        assertNotNull(mActivity);
        Log.i(LOGTAG, "getActivity");
        mInstrumentation = getInstrumentation();
        assertNotNull(mInstrumentation);
        Log.i(LOGTAG, "getInstrumentation");
        mBindHelper = ServiceBindHelper.newInstance(mInstrumentation.getTargetContext());
        assertNotNull(mBindHelper);
        Log.i(LOGTAG, "binding helper service");
        mBindHelper.setServiceHelperListener(this);
        Log.i(LOGTAG, "mBindHelper = " + mBindHelper);
        // Makesure FMRadio is playing and initialed
        waitForPowerupWithTimeout(TIMEOUT);
        boolean mbPlaying = getBooleanValue("mbPlaying");
        assertTrue(mbPlaying);
        waitForInitedWithTimeout(TIMEOUT);
        boolean mbInited = getBooleanValue("mbInited");
        assertTrue(mbInited);
        synchronized (this) {
            Log.i(LOGTAG, "-> bindService");
            mBindHelper.bindService();
            Log.i(LOGTAG, "-> mBindHelper.wait");
            while (!mIsBindSucceeded) {
                this.wait(TIMEOUT);
            }

            if (!mIsBindSucceeded) {
                fail("bind performance helper service failed!!");
            }
        }
        mBindHelper.reset();
    }

    public void onServiceBindComplete() {
        Log.i(LOGTAG, "onServiceBindComplete: mBindHelper=" + mBindHelper);
        synchronized (this) {
            mIsBindSucceeded = true;
            this.notify();
        }
    }

    public void onBindFail() {
        Log.i(LOGTAG, "onBindFail: mBindHelper=" + mBindHelper);
        synchronized (this) {
            mIsBindSucceeded = false;
            this.notify();
        }
    }

    @Override
    public void tearDown() throws Exception {
        Log.i(LOGTAG, "tearDown");
        mBindHelper.reset();
        mBindHelper.unBind();
        mActivity.finish();
        super.tearDown();
    }

    private void waitForPowerupWithTimeout(long timeOut) throws Exception {
        Log.i(LOGTAG, ">> waitForPowerupWithTimeout(" + timeOut + " ms)");
        long startTime = System.currentTimeMillis();
        while (!getBooleanValue("mbPlaying")) {
            if (System.currentTimeMillis() - startTime > timeOut) {
                break;
            }
            Thread.sleep(CHECKTIME);
        }
        Log.i(LOGTAG, "<< waitForPowerupWithTimeout(" + (System.currentTimeMillis() - startTime)
                + " ms)");
    }

    private void waitForInitedWithTimeout(long timeOut) throws Exception {
        Log.i(LOGTAG, ">> waitForInitedWithTimeout(" + timeOut + ")");
        long startTime = System.currentTimeMillis();
        while (!getBooleanValue("mbInited")) {
            if (System.currentTimeMillis() - startTime > timeOut) {
                break;
            }
            Thread.sleep(CHECKTIME);
        }
        Log.i(LOGTAG, "<< waitForInitedWithTimeout(" + (System.currentTimeMillis() - startTime)
                + " ms)");
    }
    /**
     * Test scan channel performance.
     */
    public void testCase00_ScanChannelPerformance() throws Exception {
        Log.i(LOGTAG, ">> testScanChannelPerformance");
        mBindHelper.setDescription("test for scan channel performance");

        mActivity.closeOptionsMenu();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        Log.i(LOGTAG, "KEYCODE_MENU");
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        Log.i(LOGTAG, "KEYCODE_DPAD_DOWN");
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
        Log.i(LOGTAG, "KEYCODE_DPAD_UP");
        mInstrumentation.waitForIdleSync();

        // Clear previous powerup logs
        mBindHelper.reset();
        long startTime = System.currentTimeMillis();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        mInstrumentation.waitForIdleSync();
        Log.i(LOGTAG, "KEYCODE_DPAD_CENTER");

        // / TODO add performance log after searchThread is about to finish
        long endTime = mBindHelper.getTimeResult();
        long costTime = endTime - startTime;
        Log.i(LOGTAG, "channel scanning time is: " + costTime + " ms");

        // count the scan results
        Uri uri = Uri.parse("content://com.mediatek.FMRadio.FMRadioContentProvider/station");
        Cursor c = mActivity.getContentResolver().query(
                uri,
                new String[] { FMRadioStation.Station._ID,
                        FMRadioStation.Station.COLUMN_STATION_TYPE }, null, null, null);
        assertTrue(c != null);
        try {
            if (c.moveToFirst()) {
                boolean hasValidChannel = false;
                int stationType = 0;
                do {
                    stationType = c.getInt(1);
                    if (stationType == FMRadioStation.STATION_TYPE_FAVORITE
                            || stationType == FMRadioStation.STATION_TYPE_SEARCHED) {
                        hasValidChannel = true;
                        break;
                    }
                } while (c.moveToNext());
                assertTrue(hasValidChannel);
            }
        } finally {
            c.close();
        }
        mBindHelper.dumpResult("testScanChannelPerformance", (int) costTime);
        Log.i(LOGTAG, "<< testScanChannelPerformance");
    }
    /**
     * Test open channel performance.
     */
    public void testCase01_ChannelOpenPerformance() throws Exception {
        Log.i(LOGTAG, ">> testChannelOpenPerformance");
        mBindHelper.setDescription("test for open channel performance");
        // make sure we have data in DB first
        Uri uri = Uri.parse("content://com.mediatek.FMRadio.FMRadioContentProvider/station");
        Cursor c = mActivity.getContentResolver().query(
                uri,
                new String[] { FMRadioStation.Station._ID,
                        FMRadioStation.Station.COLUMN_STATION_TYPE }, null, null, null);
        assertTrue(c != null);
        if (c == null) {
            Log.e(LOGTAG, "no channels in DB!!");
        } else {
            Log.i(LOGTAG, "channel count=" + c.getCount());
            boolean mHasValidChannel = false;
            int stationType = 0;
            try {
                if (c.moveToFirst()) {
                    Log.i(LOGTAG, "start checking...");
                    do {
                        stationType = c.getInt(1);
                        Log.i(LOGTAG, "stationType=" + stationType);
                        if (stationType == FMRadioStation.STATION_TYPE_FAVORITE
                                || stationType == FMRadioStation.STATION_TYPE_SEARCHED) {
                            mHasValidChannel = true;
                            break;
                        }
                    } while (c.moveToNext());
                }
                assertTrue(mHasValidChannel);
            } finally {
                c.close();
            }
        }

        final View favorite = mActivity.findViewById(R.id.button_favorite);

        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    favorite.performClick();
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }

        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
        mInstrumentation.waitForIdleSync();

        long startTime = System.currentTimeMillis();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        mInstrumentation.waitForIdleSync();
        // / TODO add performance log after tuneToStation()
        long endTime = mBindHelper.getTimeResult();
        long costTime = endTime - startTime;
        Log.i(LOGTAG, "channel opening time is: " + costTime + " ms");
        mBindHelper.dumpResult("testChannelOpenPerformance", (int) costTime);
        Log.i(LOGTAG, "<< testChannelOpenPerformance");
    }

    /**
     * Test switch channel performance.
     */
    public void testCase04_SwitchingChannelPerformance() throws Exception {
        Log.i(LOGTAG, ">> testSwitchingChannelPerformance");
        mBindHelper.setDescription("test switching channel performance");
        final View prevstation = mActivity.findViewById(R.id.button_prevstation);
        final View nextstation = mActivity.findViewById(R.id.button_nextstation);

        // Next direction
        // Test performance for swtching next channel from fequency 87.5-108 MHz
        // wholetime is the whole time by searching 87.5-108 MHz
        // connt is the numbers of searching channels
        // ChannelRange is the FMRadio working frequency range

        long[] switchchannelTime = new long[STATIONMAXNUM];
        int[] switchchannelRange = new int[STATIONMAXNUM];
        int count = 0;
        long startTime = 0;
        long endTime = 0;
        long costTime = 0;
        int diffwholeband = 0;
        int diffprevtocurrband = 0;
        int prevfreqency = 0;
        int channelRange = HIGHSTATION - LOWSTATION;

        // makesure the current station frequency is 87.5 MHz
        tuneToStation(LOWSTATION);
        mInstrumentation.waitForIdleSync();
        // get the current start frequency
        int currentFrequency = getCurrentStation();
        diffwholeband = currentFrequency - LOWSTATION;
        prevfreqency = currentFrequency;
        Log.i(LOGTAG, " The start station frequency is (next): " + (float) currentFrequency / TEN
                + " MHz");

        // Begin to test switch channel performance in whole band in next direct
        Log.i(LOGTAG, ">> Test switch next channel");
        while (diffwholeband < channelRange) {
            mBindHelper.reset();
            startTime = System.currentTimeMillis();
            try {
                runTestOnUiThread(new Runnable() {
                    public void run() {
                        nextstation.performClick();
                    }
                });
            } catch (Throwable t) {
                Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
            }
            mInstrumentation.waitForIdleSync();
            // / TODO add performance log after MSGID_SEEK_FINISH/SEEK_FAIL
            endTime = mBindHelper.getTimeResult();
            // / TODO should also be able to distinguish between success/failure
            costTime = endTime - startTime;
            count++;
            switchchannelTime[count - 1] = costTime;
            // Log.i(LOGTAG, "One channel switching time is (next) : " + costTime + " ms");

            // get the searched channels range, if range is big than FMRadio working frequency
            // range, searching is over.
            currentFrequency = getCurrentStation();
            diffprevtocurrband = currentFrequency - prevfreqency;
            diffwholeband = diffwholeband + Math.abs(diffprevtocurrband);
            prevfreqency = currentFrequency;
            switchchannelRange[count - 1] = Math.abs(diffprevtocurrband);
            // Log.i(LOGTAG, " The current station frequency is (next) : " + (float)
            // currentFrequency/10 + " MHz");
            // Log.i(LOGTAG, "The search count is (next) : " + count + " stations");
            mInstrumentation.waitForIdleSync();
        }
        Log.i(LOGTAG, "<<< Test switch next channel");

        // Minus the fianl search result ,because the search frequency is beyond highest frequency
        switchchannelTime[count - 1] = 0;
        switchchannelRange[count - 1] = 0;
        count--;
        diffwholeband = diffwholeband - Math.abs(diffprevtocurrband);

        // Begin to caculate switch channel time per 1 MHz
        if (count > STATIONMAXNUM) {
            count = STATIONMAXNUM;
        }// The max search channel num is stationmaxnum
        int countNext = count;
        float costTimeNext = switchChannelTimePerMHz(switchchannelRange, switchchannelTime,
                countNext, diffwholeband);

        Log.i(LOGTAG, "The search count is (next) : " + countNext + " stations");
        Log.i(LOGTAG, "Channel switching time is (next) : " + costTimeNext + " ms");

        // Prevous direction
        // Test performance for switching prev channel from fequency 108-87.5 MHz
        // wholetime is the whole time by searching 108-87.5 MHz
        // connt is the numbers of searching channels
        // ChannelRange is the FMRadio working frequency range

        count = 0;
        startTime = 0;
        endTime = 0;
        costTime = 0;
        diffwholeband = 0;
        diffprevtocurrband = 0;
        prevfreqency = 0;
        channelRange = HIGHSTATION - LOWSTATION;

        // makesure the current station frequency is 108 MHz
        tuneToStation(HIGHSTATION);
        mInstrumentation.waitForIdleSync();
        // get the current start frequency
        currentFrequency = getCurrentStation();
        diffwholeband = HIGHSTATION - currentFrequency;
        prevfreqency = currentFrequency;
        Log.i(LOGTAG, " The start station frequency is (next): " + (float) currentFrequency / TEN
                + " MHz");

        // Begin to test switch channel performance in whole band in prev direct
        Log.i(LOGTAG, ">>> Test switch prev channel");
        while (diffwholeband < channelRange) {
            mBindHelper.reset();
            startTime = System.currentTimeMillis();
            try {
                runTestOnUiThread(new Runnable() {
                    public void run() {
                        prevstation.performClick();
                    }
                });
            } catch (Throwable t) {
                Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
            }
            mInstrumentation.waitForIdleSync();
            // / TODO add performance log after MSGID_SEEK_FINISH/SEEK_FAIL
            endTime = mBindHelper.getTimeResult();
            // / TODO should also be able to distinguish between success/failure
            costTime = endTime - startTime;
            count++;
            switchchannelTime[count - 1] = costTime;
            // Log.i(LOGTAG, "One channel switching time is (prev) : " + costTime + " ms");

            // get the current frequency
            currentFrequency = getCurrentStation();
            diffprevtocurrband = prevfreqency - currentFrequency;
            diffwholeband = diffwholeband + Math.abs(diffprevtocurrband);
            prevfreqency = currentFrequency;
            switchchannelRange[count - 1] = Math.abs(diffprevtocurrband);
            // Log.i(LOGTAG, " The current station frequency is (prev) : " + (float)
            // currentFrequency/10 + " MHz");
        }
        Log.i(LOGTAG, "<<< Test switch prev channel");

        // Minus the fianl search result ,because the search frequency is beyond high frequency
        switchchannelTime[count - 1] = 0;
        switchchannelRange[count - 1] = 0;
        count--;
        diffwholeband = diffwholeband - Math.abs(diffprevtocurrband);

        // Begin to calculate the performance of switching to prev channel in average per 1 MHz
        if (count > STATIONMAXNUM) {
            count = STATIONMAXNUM;
        }// The max search channel num is stationmaxnum
        int countPrev = count;
        float costTimePrev = switchChannelTimePerMHz(switchchannelRange, switchchannelTime,
                countPrev, diffwholeband);
        Log.i(LOGTAG, "The search count is (prev) : " + countPrev + " stations");
        Log.i(LOGTAG, "Channel switching time is (prev) : " + costTimePrev + " ms");

        // dump the performance test time to files
        costTime = ((long) costTimeNext + (long) costTimePrev) / 2;
        Log.i(LOGTAG, "Channel switching time is : " + costTime + " ms");
        count = (countNext + countPrev) / 2;
        Log.i(LOGTAG, "Channel switching number is : " + count + " stations");
        mBindHelper.dumpResult("testSwitchingChannelPerformance", (int) costTime);

        // mBindHelper.setDescription("test switching channel number performance");
        // mBindHelper.dumpResult("testSwitchingChannelNumber", (int) count);

        Log.i(LOGTAG, "<< testSwitchingChannelPerformance");
    }

    /**
     * Test power up and power down channel performance.
     */
    public void testCase03_SuspendResumePerformance() throws Exception {
        Log.i(LOGTAG, ">> testSuspendResumePerformance");
        mBindHelper.setDescription("test suspend <-> resume performance");

        final View playstop = mActivity.findViewById(R.id.button_play_stop);
        long startTime = System.currentTimeMillis();
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    playstop.performClick();
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }
        mInstrumentation.waitForIdleSync();
        long endTime = mBindHelper.getTimeResult();
        // / TODO add performance log after powerdown
        // / note that we should distinguish between success and failure
        long costTime = endTime - startTime;
        Log.i(LOGTAG, "playing -> stopped time is: " + costTime + " ms");

        mBindHelper.reset();
        startTime = System.currentTimeMillis();
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    playstop.performClick();
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }
        mInstrumentation.waitForIdleSync();
        endTime = mBindHelper.getTimeResult();
        // / TODO add performance log after powerup
        // / note that we should distinguish between success and failure

        costTime = endTime - startTime;
        Log.i(LOGTAG, "stopped -> playing time is: " + costTime + " ms");
        mBindHelper.dumpResult("testSuspendResumePerformance", (int) costTime);
        Log.i(LOGTAG, "<< testSuspendResumePerformance");
    }

    /**
     * Test increase and decrease 0.1 MHz performance.
     */
    public void testCase02_FrequencyChangePerformance() throws Exception {
        Log.i(LOGTAG, ">> testFrequencyChangePerformance");
        mBindHelper.setDescription("test increase/decrease frequency time");
        final View increase = mActivity.findViewById(R.id.button_increase);
        final View decrease = mActivity.findViewById(R.id.button_decrease);
        Log.i(LOGTAG, "increase=" + increase + ", decrease=" + decrease);
        long startTime = System.currentTimeMillis();
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    increase.performClick();
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }
        mInstrumentation.waitForIdleSync();
        // / TODO: add performance log in onClick of mButtonIncrease
        long endTime = mBindHelper.getTimeResult();
        long costTime = endTime - startTime;
        Log.i(LOGTAG, "Increase frequency time is: " + costTime + " ms");

        mBindHelper.reset();
        startTime = System.currentTimeMillis();

        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    decrease.performClick();
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }
        mInstrumentation.waitForIdleSync();
        // / TODO: add performance log in onClick of mButtonIncrease
        endTime = mBindHelper.getTimeResult();
        costTime = endTime - startTime;
        Log.i(LOGTAG, "Decrease frequency time is: " + costTime + " ms");

        mBindHelper.dumpResult("testFrequencyChangePerformance", (int) costTime);
        Log.i(LOGTAG, "<< testFrequencyChangePerformance");
    } // testFrequencyChangePerformance ends...

    /**
     * Test switch earphone and speaker performance.
     */
    public void testCase05_SwitchSpeakerEarphone() throws Exception {
        Log.i(LOGTAG, ">> testSwitchSpeakerEarphone");
        mBindHelper.setDescription("test speaker/earphone switch time");
        final View earphonesperker = mActivity.findViewById(R.id.button_ear_loud);

        // Makesure sperker/earphone start with earphone
        Method methodIsEarphoneUsed = mActivity.getClass().getDeclaredMethod("isEarphoneUsed",
                new Class[] {});
        methodIsEarphoneUsed.setAccessible(true);
        boolean bIsEarphoneUsed = (Boolean) methodIsEarphoneUsed.invoke(mActivity, new Object[] {});
        if (!bIsEarphoneUsed) {
            try {
                runTestOnUiThread(new Runnable() {
                    public void run() {
                        earphonesperker.performClick();
                        Log.i(LOGTAG, "switch sperker -> earphone!");
                    }
                });
            } catch (Throwable t) {
                Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
            }
        }
        mInstrumentation.waitForIdleSync();
        Log.i(LOGTAG, "Is earphone used : " + bIsEarphoneUsed);

        long startTime = System.currentTimeMillis();
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    earphonesperker.performClick();
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }
        mInstrumentation.waitForIdleSync();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        Log.i(LOGTAG, "earphone <-> speaker switch time is: " + costTime + " ms");

        // When exit FMRadio,turn the audio path to earphone and wait the earphone ui refresh
        bIsEarphoneUsed = (Boolean) methodIsEarphoneUsed.invoke(mActivity, new Object[] {});
        if (!bIsEarphoneUsed) {
            try {
                runTestOnUiThread(new Runnable() {
                    public void run() {
                        earphonesperker.performClick();
                        Log.i(LOGTAG, "switch sperker -> earphone!");
                    }
                });
            } catch (Throwable t) {
                Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
            }
        }
        Thread.sleep(CHECKTIME);
        bIsEarphoneUsed = (Boolean) methodIsEarphoneUsed.invoke(mActivity, new Object[] {});
        assertTrue(bIsEarphoneUsed);
        mBindHelper.dumpResult("testSwitchSpeakerEarphone", (int) costTime);
        Log.i(LOGTAG, "<< testSwitchSpeakerEarphone");
    }

    /**
     * Get boolean by string which is member variable qith boolean value in activity.
     * 
     * @param string
     *            The member variable in activity with voolean value
     * @return The boolean value of member variable in activity
     */
    public boolean getBooleanValue(String string) {
        try {
            Field field = FMRadioActivity.class.getDeclaredField(string.toString());
            field.setAccessible(true);
            return ((Boolean) field.get(mActivity)).booleanValue();
        } catch (NoSuchFieldException ex) {
            Log.e(LOGTAG, "No such field in runTestOnUiThread:", ex);
            return false;
        } catch (IllegalAccessException ex) {
            Log.e(LOGTAG, "Illegal access exception in runTestOnUiThread:", ex);
            return false;
        } catch (IllegalArgumentException ex) {
            Log.e(LOGTAG, "Illegal argument exception in runTestOnUiThread:", ex);
            return false;
        }
    }

    /**
     * Tune to the station with frequency.
     * 
     * @param frequency
     *            The frequency of station
     */
    public void tuneToStation(final int frequency) {
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Method mtuneToStation = mActivity.getClass().getDeclaredMethod(
                                "tuneToStation", int.class);
                        mtuneToStation.setAccessible(true);
                        Log.i(LOGTAG, "get method mtunetostation success");
                        mtuneToStation.invoke(mActivity, frequency);
                        Log.i(LOGTAG, "Tune to " + frequency + " MHz station success");
                    } catch (NoSuchMethodException ex) {
                        Log.e(LOGTAG, "No such method in runTestOnUiThread:", ex);
                    } catch (IllegalAccessException ex) {
                        Log.e(LOGTAG, "Illegal access exception in runTestOnUiThread:", ex);
                    } catch (InvocationTargetException ex) {
                        Log.e(LOGTAG, "Invocation target exception in runTestOnUiThread:", ex);
                    }
                }
            });
        } catch (Throwable t) {
            Log.e(LOGTAG, "Throwable in runTestOnUiThread:", t);
        }
    }

    /**
     * Get the current station frequency.
     * 
     * @return The current station frequency
     */
    public int getCurrentStation() {
        try {
            Field mCurrentStation = FMRadioActivity.class.getDeclaredField("miCurrentStation");
            mCurrentStation.setAccessible(true);
            mInstrumentation.waitForIdleSync();
            return (Integer) mCurrentStation.get(mActivity);
        } catch (NoSuchFieldException ex) {
            Log.e(LOGTAG, "No such field in runTestOnUiThread:", ex);
            return LOWSTATION;
        } catch (IllegalAccessException ex) {
            Log.e(LOGTAG, "Illegal access exception in runTestOnUiThread:", ex);
            return LOWSTATION;
        } catch (IllegalArgumentException ex) {
            Log.e(LOGTAG, "Illegal argument exception in runTestOnUiThread:", ex);
            return LOWSTATION;
        }
    }

    /**
     * Calculate the performance of switching to next channel in average per 1 MHz.
     * 
     * @param switchchannelRange
     *            Switch channel range between nearly stations
     * @param switchchannelTime
     *            Switch channel time
     * @param count
     *            The whole channel number
     * @param diffwholeband
     *            The whole switch band
     * @return Switch channel time per 1 MHz
     */
    public float switchChannelTimePerMHz(int[] switchchannelRange, long[] switchchannelTime,
            int count, int diffwholeband) {

        long seekTime = 0;// The native seek time
        int seekRange = 0;// The seek different range
        long wholeTime = switchchannelTime[0];
        for (int i = 1; i < count; i++) {
            seekTime = seekTime + Math.abs(switchchannelTime[i] - switchchannelTime[i - 1]);
            seekRange = seekRange + Math.abs(switchchannelRange[i] - switchchannelRange[i - 1]);
            wholeTime = wholeTime + switchchannelTime[i];
        }

        float seekTimeperMHz = (float) seekTime / seekRange * TEN;
        float switchChannelTimePerMHz = (wholeTime - seekTimeperMHz * diffwholeband / TEN) / count
                + seekTimeperMHz;
        Log.i(LOGTAG, "The whole time is : " + wholeTime + " ms");
        Log.i(LOGTAG, "The search band is : " + (float) diffwholeband / TEN + " MHz");
        Log.i(LOGTAG, "The seek time per 1 MHz is : " + seekTimeperMHz + " ms");
        return switchChannelTimePerMHz;

    }

}
