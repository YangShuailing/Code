/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

package com.mediatek.FMRadio;

import java.util.Timer;
import java.util.TimerTask;

import com.mediatek.FMRadio.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import java.lang.Exception;
import java.io.File;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.text.TextWatcher;
import android.text.Editable;
import com.mediatek.featureoption.FeatureOption;


public class FMRadioActivity extends Activity {
    public static final String TAG = "FMRadioAPK";
    public static final int DLGID_NOANTENNA = 1;
    public static final int DLGID_SEARCHING = 2;
    public static final int DLGID_RDS_SETTING = 3;
    public static final int DLGID_SAVE_RECORDING=4;
    public static final String TYPE_MSGID = "MSGID";
    public static final String TYPE_FIRST_SEARCHED_STATION = "FIRST_SEARCHED_STATION";
    public static final String TYPE_SEEK_STATION = "SEEK_STATION";
    public static final String TYPE_TOAST_STRING = "TYPE_TOAST_STRING";
    public static final int MSGID_RETRY = 1;
    public static final int MSGID_OK = 2;
    public static final int MSGID_SEARCH_FINISH = 3;
    public static final int MSGID_UPDATE_RDS = 4;
    public static final int MSGID_UPDATE_CURRENT_STATION = 5;
    public static final int MSGID_SEEK_FINISH = 6;
    public static final int MSGID_SEEK_FAIL = 7;
    public static final int    MSGID_PLAY_FINISH = 8;
    public static final int MSGID_PLAY_FAIL = 9;
    public static final int MSGID_ANTENNA_UNAVAILABE = 10;
    public static final int MSGID_SHOW_TOAST = 11;
    
    private static final int MSGID_REFRESH = 12;
    
    private final int OPTMENUID_SEARCH = 1;
    private final int OPTMENUID_EXIT = 2;
    private final int OPTMENUID_RDS_SETTING = 3;
    private final int OPTMENUID_RECORD = 4;
    
    private final int REQUEST_CODE_FAVORITE = 1;
    
    private final String FM_SAVE_INSTANCE_STATE_INITED = "FM_SAVE_INSTANCE_STATE_INITED";
    private final String FM_SAVE_INSTANCE_STATE_PLAYING = "FM_SAVE_INSTANCE_STATE_PLAYING";
    private final String FM_SAVE_INSTANCE_STATE_EARPHONEUSED = "FM_SAVE_INSTANCE_STATE_EARPHONEUSED";
    private static final String FM_INSTANCE_STATE_RECORDING_DLGSTATE = "FM_INSTANCE_STATE_RECORDING_NAME";
    
    private static final String TIME_FORMAT_SHORT = "%02d:%02d";
    private static final String TIME_FORMAT_LONG = "%02d:%02d:%02d";
    
    private boolean mbServiceStarted = false;
    private boolean mbServiceBinded = false;
    private IFMRadioService mService = null;
    private ServiceConnection mServiceConnection = null;

    private AlertDialog mDialogNoAntenna = null;
    private AlertDialog mDialogRDSSetting = null;
    private ProgressDialog mDialogSearchProgress = null;
    private Handler mHandler = null;
    private boolean mbInited = false;
    
    private boolean mbUserCancelSearch = false; // Set to true if user cancel the searching progress.
    private boolean mbPlaying = false; // When start, the radio is not playing.
    private boolean mbExitPressed = false;
    
    private boolean mbIsRecording = false;
    
    // Record whether power up is on processing.
    private boolean mbOnPowerUp = false;
    // Record whether we are searching channels.
    private boolean mbSearching = false;
    // Record whether we are seeking channels.
    private boolean mbSeeking = false;
    // Record whether we are destroying.
    private boolean mbDestroying = false;

    // RDS settings
    private boolean mbPSRTEnabled = false;
    private boolean mbAFEnabled = false;
    private boolean mbTAEnabled = false;
    
    // Record whether RDS is supported.
    private boolean mbRDSSupported = false;
    // Record whether RDS is enabled.
    private boolean mbRDSEnabled = false;
    // Indicate whether exit RDS thread.
    private boolean mbExit = false;
    // RDS events
    private final int RDS_EVENT_FLAGS            = 0x0001;
    private final int RDS_EVENT_PI_CODE            = 0x0002;
    private final int RDS_EVENT_PTY_CODE        = 0x0004;
    private final int RDS_EVENT_PROGRAMNAME        = 0x0008;
    private final int RDS_EVENT_UTCDATETIME        = 0x0010;
    private final int RDS_EVENT_LOCDATETIME        = 0x0020;
    private final int RDS_EVENT_LAST_RADIOTEXT    = 0x0040;
    private final int RDS_EVENT_AF                = 0x0080;
    private final int RDS_EVENT_AF_LIST            = 0x0100;
    private final int RDS_EVENT_AFON_LIST        = 0x0200;
    private final int RDS_EVENT_TAON            = 0x0400;
    private final int RDS_EVENT_TAON_OFF        = 0x0800;
    private final int RDS_EVENT_RDS                = 0x2000;
    private final int RDS_EVENT_NO_RDS            = 0x4000;
    private final int RDS_EVENT_RDS_TIMER        = 0x8000;
    // Strings shown in RDS text view.
    private String msPS = "";
    private String msLRText = "";
    
    // The toast and its timer
    public static final long TOAST_TIMER_DELAY = 2000; // Timer delay 2 seconds.
    private Toast mToast = null;
    private Timer mTimer = null;
    
    private TextView mTextStationName = null;
    private TextView mTextStationValue = null;
    private TextView mTextRDS = null;
    private TextView mTextFM = null;
    private TextView mTextMHz = null;
    
    private ImageButton mButtonDecrease = null;
    private ImageButton mButtonPrevStation = null;
    private ImageButton mButtonNextStation = null;
    private ImageButton mButtonIncrease = null;
    private ImageButton mButtonFavorite = null;
    private ImageButton mButtonPlayStop = null;
    private ImageButton mButtonEarLoud = null;
    private ImageButton mButtonAddToFavorite = null;
  
    private ImageButton mBtnRecord = null;
    private ImageButton mBtnStop = null;
    private ImageButton mBtnPlayback = null;
    private RelativeLayout mRLRecordInfo = null;
    private TextView mTxtRecInfoLeft = null;
    private TextView mTxtRecInfoRight = null;
    private TextView mStorageWarningTextView;
    private LinearLayout mLLTopBar = null;
    private EditText mEdRecordingName = null;
    private Dialog mDlgSaveRecording = null;
    private int mPrevRecorderState = FMRecorder.STATE_INVALID;
    private String mSDDirectory = null;
    private boolean mIsSDListenerRegistered = false;
    
    
    private Animation mAnimation = null;
    private ImageView mAnimImage = null;
    
    private RelativeLayout mMainView = null;   
    
    private WakeLock mWakeLock = null;
    
    // Can not use float to record the station. Because there will be inaccuracy when increase/decrease 0.1
    private int miCurrentStation = FMRadioStation.FIXED_STATION_FREQ;
    private AudioManager mAM = null;

    private Bundle mSavedInstanceState = null;
    
    private boolean mIsStorageWarning = false;
    private boolean mIsActivityBackground = false;
    private int mRecordState = 0;
    private boolean mIsFreshRecordingStatus = false;
    // use shared preference to store start recording time or start playback time.
    private static final String REFS_NAME = "FMRecord";
    private static final String START_RECORD_TIME = "startRecordTime";
    private static final String START_PLAY_TIME = "startPlayTime";
    private long mRecordStartTime = 0;
    private long mPlayStartTime = 0;
    
    private BroadcastReceiver mSDListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
                if (mDlgSaveRecording != null) {
                    mDlgSaveRecording.dismiss();
                    try {
                        mService.saveRecording(null);
                    } catch (Exception ex) {
                        FMRadioLogUtils.e(TAG, "Exception while saveRecording(null)");
                    }
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_recording_lost_warning), Toast.LENGTH_SHORT);
                }
            }
        }
    };

    private class FMBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onReceive");
            String action = intent.getAction();
            FMRadioLogUtils.v(TAG, "Context: " + context);
            FMRadioLogUtils.v(TAG, "Action: " + action);
            if (action.equals(FMRadioService.ACTION_STATE_CHANGED)) {
                boolean bIsPowerUp = intent.getBooleanExtra(FMRadioService.EXTRA_FMRADIO_ISPOWERUP, false);
                if (bIsPowerUp) {
                    FMRadioLogUtils.d(TAG, "FM Radio is power up.");
                    mButtonPlayStop.setImageResource(R.drawable.btn_fm_stop);
                    mbPlaying = true;
                    refreshButtonStatus();
                }
                else {
                    FMRadioLogUtils.d(TAG, "FM Radio is power down.");
                    mButtonPlayStop.setImageResource(R.drawable.btn_fm_play);
                    mbPlaying = false;
                    refreshButtonStatus();
                }
            }
            else if (action.equals(FMRadioService.ACTION_RDS_PS_CHANGED)){
                msPS = intent.getStringExtra(FMRadioService.EXTRA_RDS_PS);
                FMRadioLogUtils.v(TAG, "getPS: " + msPS);
                
                // Update the RDS text view.
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_UPDATE_RDS);
                msg.setData(bundle);
                msg.sendToTarget();
            }
            else if (action.equals(FMRadioService.ACTION_RDS_RT_CHANGED)){
                msLRText = intent.getStringExtra(FMRadioService.EXTRA_RDS_RT);
                FMRadioLogUtils.v(TAG, "getLRText: " + msLRText);
                
                // Update the RDS text view.
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_UPDATE_RDS);
                msg.setData(bundle);
                msg.sendToTarget();
            }
            else if (action.equals(FMRadioService.ACTION_RDS_AF_ACTIVED)){
                int iFreq = intent.getIntExtra(FMRadioService.EXTRA_RDS_AF_ACTIVED, 0);
                if (iFreq >= FMRadioStation.LOWEST_STATION
                    && iFreq <= FMRadioStation.HIGHEST_STATION) {
                    // Valid alternative frequency. Should update the current station.
                    miCurrentStation = iFreq;
                    Message msg = new Message();
                    msg.setTarget(mHandler);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TYPE_MSGID, MSGID_UPDATE_CURRENT_STATION);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
                else {
                    FMRadioLogUtils.e(TAG, "Error: invalid alternative frequency");
                }
            }
            else if (action.equals(FMRadioService.ACTION_RDS_TA_ACTIVED)){
                int iFreq = intent.getIntExtra(FMRadioService.EXTRA_RDS_TA_ACTIVED, 0);
                if (iFreq >= FMRadioStation.LOWEST_STATION
                    && iFreq <= FMRadioStation.HIGHEST_STATION) {
                    // Valid alternative frequency. Should update the current station.
                    miCurrentStation = iFreq;
                    Message msg = new Message();
                    msg.setTarget(mHandler);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TYPE_MSGID, MSGID_UPDATE_CURRENT_STATION);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
                else {
                    FMRadioLogUtils.e(TAG, "Error: invalid activeTA frequency");
                }
            }
            else if (action.equals(FMRadioService.ACTION_RDS_TA_DEACTIVED)){
                int iFreq = intent.getIntExtra(FMRadioService.EXTRA_RDS_TA_DEACTIVED, 0);
                if (iFreq >= FMRadioStation.LOWEST_STATION
                    && iFreq <= FMRadioStation.HIGHEST_STATION) {
                    // Valid alternative frequency. Should update the current station.
                    miCurrentStation = iFreq;
                    Message msg = new Message();
                    msg.setTarget(mHandler);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TYPE_MSGID, MSGID_UPDATE_CURRENT_STATION);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
                else {
                    FMRadioLogUtils.e(TAG, "Error: invalid activeTA frequency");
                }
            } else if (action.equals(FMRadioService.ACTION_RECORDING_STATE_CHANGED)) {
                int recorderState = intent.getIntExtra(FMRadioService.EXTRA_RECORDING_STATE, FMRecorder.STATE_INVALID);
                mRecordState = recorderState;
                if (!mIsActivityBackground) {
                     refreshRecordingStatus(recorderState);
                } else {
                     mIsFreshRecordingStatus = true;
                }
                switch (recorderState) {
                case FMRecorder.STATE_RECORDING:
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_start_recording),Toast.LENGTH_SHORT).show();
                    /* falls through */
                case FMRecorder.STATE_PLAYBACK:
                    mRefresher.sendEmptyMessage(MSGID_REFRESH);
                    break;
                case FMRecorder.STATE_IDLE:
                    /* falls through */
                default:
                    mRefresher.removeMessages(MSGID_REFRESH);
                }
                
                
            } else if (FMRadioService.ACTION_RECORDER_ERROR.equals(action)) {
                // Recorder error
                int errorState = intent.getIntExtra(FMRadioService.EXTRA_RECORDER_ERROR_STATE, -1);
                FMRadioLogUtils.d(TAG, "ACTION_RECORDER_ERROR: " + errorState);
                switch (errorState) {
                case FMRecorder.ERROR_SDCARD_NOT_PRESENT:
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_sdcard_missing), Toast.LENGTH_SHORT).show();
                    break;
                case FMRecorder.ERROR_SDCARD_INSUFFICIENT_SPACE:
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_sdcard_insufficient_space), Toast.LENGTH_SHORT).show();
                    break;
                case FMRecorder.ERROR_RECORDER_INTERNAL:
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_recorder_internal_error), Toast.LENGTH_SHORT).show();
                    break;
                case FMRecorder.ERROR_PLAYER_INTERNAL:
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_player_internal_error), Toast.LENGTH_SHORT).show();
                    break;
                }
            } else if (FMRadioService.ACTION_RECORDING_MODE_CHANGED.equals(action)) {
                boolean recordingMode = intent.getBooleanExtra(FMRadioService.EXTRA_RECORDING_MODE, false);
                if (!recordingMode && mbIsRecording) {
                    // Service has already set recording mode to false, we need to modify UI here
                    mbIsRecording = false;
                    if (mIsSDListenerRegistered) {
                        unregisterReceiver(mSDListener);
                        mIsSDListenerRegistered = false;
                    }
                    LinearLayout top_bar = (LinearLayout)findViewById(R.id.top_bar);
                    LinearLayout rec_bar = (LinearLayout)findViewById(R.id.bottom_bar_recorder);
                    LinearLayout bottom_bar = (LinearLayout)findViewById(R.id.bottom_bar);
                    top_bar.setVisibility(mbIsRecording ? View.GONE : View.VISIBLE);
                    bottom_bar.setVisibility(mbIsRecording ? View.GONE : View.VISIBLE);
                    rec_bar.setVisibility(mbIsRecording ? View.VISIBLE : View.GONE);
                    mButtonAddToFavorite.setVisibility(mbIsRecording ? View.GONE : View.VISIBLE);
                }
            } else {
                FMRadioLogUtils.e(TAG, "Error: undefined action.");
            }
            FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onReceive");
        }
    }

    private FMBroadcastReceiver mBroadcastReceiver = null;
    
    private Handler mRefresher = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FMRadioLogUtils.d(TAG, "Refresher: " + msg.what);
            if (msg.what != MSGID_REFRESH || !mbIsRecording)
                return;
            
            try {
                if (FMRecorder.STATE_RECORDING == mRecordState ) {
                    int recordTime = (int) ((SystemClock.elapsedRealtime() - mRecordStartTime) / 1000);
                    int hour = recordTime / 3600;
                    int minute = (recordTime / 60) % 60;
                    int sec = recordTime % 60;
                    String timeString = null;
                    if (hour > 0) {
                        timeString = String.format(TIME_FORMAT_LONG, hour, minute, sec);
                    } else {
                        timeString = String.format(TIME_FORMAT_SHORT, minute, sec);
                    }
                    mTxtRecInfoLeft.setText(timeString);
                    FMRadioLogUtils.d(TAG, "Recording time = " + mTxtRecInfoLeft.getText());
                    if (!checkRemainingStorage()) {
                        // Insufficient storage
                        mService.stopRecording();
                    }
                } else if (FMRecorder.STATE_PLAYBACK == mRecordState) {
                    int playTime = (int) ((SystemClock.elapsedRealtime() - mPlayStartTime) / 1000);
                    int hour_play = playTime / 3600;
                    int minute_play = (playTime / 60) % 60;
                    int sec_play = playTime % 60;
                    String timeString_play = null;
                    if (hour_play > 0) {
                        timeString_play = String.format(TIME_FORMAT_LONG, hour_play, minute_play, sec_play);
                    } else {
                        timeString_play = String.format(TIME_FORMAT_SHORT, minute_play, sec_play);
                    }
                    mTxtRecInfoRight.setText(timeString_play);
                    FMRadioLogUtils.d(TAG, "Playing time = " + mTxtRecInfoRight.getText());
                } else {
                    return;
                }
            } catch (Exception ex) {
                FMRadioLogUtils.e(TAG, "Exception in refresher: ", ex);
                return;
            }
            mRefresher.sendEmptyMessageDelayed(MSGID_REFRESH, 1000);
        }
    };
    
    // Called when the activity is first created. 
    public void onCreate(Bundle savedInstanceState) {
        FMRadioLogUtils.i(TAG, ">>> FMRadioActivity.onCreate");
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        // Bind the activity to FM audio stream.
        setVolumeControlStream(AudioManager.STREAM_FM);
        setContentView(R.layout.main);
        //getWindow().setBackgroundDrawableResource(R.drawable.background);
        //int width = getWindowManager().getDefaultDisplay().getWidth();
        //int height = getWindowManager().getDefaultDisplay().getHeight();
        //FMRadioLogUtils.i(TAG, "Screen width is: " + width);
        //FMRadioLogUtils.i(TAG, "Screen height is: " + height);
        //DisplayMetrics outMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(outMetrics);


        mBtnRecord = (ImageButton)findViewById(R.id.btn_record);
        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FMRadioLogUtils.d(TAG, "btn record: CLICK!!");
                if (!mbPlaying) {
                    Toast.makeText(FMRadioActivity.this, getString(R.string.toast_powerup_before_record_warning), Toast.LENGTH_SHORT).show();
                    return;
                }
                mSDDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
                SharedPreferences sharedPreferences = getSharedPreferences(REFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                mRecordStartTime = SystemClock.elapsedRealtime();
                editor.putLong(START_RECORD_TIME, mRecordStartTime);
                editor.commit();
                try {
                    mService.startRecording();
                } catch (Exception ex) {
                    FMRadioLogUtils.e(TAG, "failed to startRecording: ", ex);
                }
            }
        });
        
        mBtnStop = (ImageButton)findViewById(R.id.btn_stop);
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FMRadioLogUtils.d(TAG, "btn stop: CLICK!!");
                try {
                    mService.stopRecording();
                    mService.stopPlayback();
                } catch (Exception ex) {
                    FMRadioLogUtils.e(TAG, "failed to stopRecording/stopPlayback");
                }
            }
        });
        
        mBtnPlayback = (ImageButton)findViewById(R.id.btn_playback);
        mBtnPlayback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FMRadioLogUtils.d(TAG, "btn playback: CLICK!!");
                SharedPreferences sharedPreferences = getSharedPreferences(REFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                mPlayStartTime = SystemClock.elapsedRealtime();
                editor.putLong(START_PLAY_TIME, mPlayStartTime);
                editor.commit();
                try {
                    mService.startPlayback();
                } catch (Exception ex) {
                    FMRadioLogUtils.e(TAG, "failed to startPlayback");
                }
            }
        });
        
        // put favorite button here since it might be used very early in changing recording mode
        miCurrentStation = FMRadioStation.getCurrentStation(this);
        boolean bIsFavoriteStation = FMRadioStation.isFavoriteStation(this, miCurrentStation);
        mButtonAddToFavorite = (ImageButton)findViewById(R.id.button_add_to_favorite);
        // If the current station is in favorite, set its icon to favorite icon;
        // else, set to none favorite icon.
        if (bIsFavoriteStation) {
            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_on);
        }
        else {
            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_off);
        }
        mButtonAddToFavorite.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN
                            || event.getAction() == MotionEvent.ACTION_UP) {
                            if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
                                changeBackground(v, event.getAction(), com.android.internal.R.drawable.btn_star_big_on_pressed, com.android.internal.R.drawable.btn_star_big_on, false);
                            }
                            else {
                                changeBackground(v, event.getAction(), com.android.internal.R.drawable.btn_star_big_off_pressed, com.android.internal.R.drawable.btn_star_big_off, false);
                            }
                        }
                        else {
                            // Do not handle other motion events.
                        }
                        return false;
                    }
                }
            );
        mButtonAddToFavorite.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    FMRadioLogUtils.d(TAG, ">>> onClick AddToFavorite");
                    if (!isToasting()) {
                        // Judge the current output and switch between the devices.
                        if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
                            // Need to delete this favorite channel.
                            FMRadioStation.deleteStationInDB(
                                FMRadioActivity.this, 
                                miCurrentStation, 
                                FMRadioStation.STATION_TYPE_FAVORITE
                            );
                            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_off);
                            mTextStationName.setText("");
                            showToast(getString(R.string.toast_channel_deleted));
                        }
                        else {
                            // Add the station to favorite if the favorite list is not full.
                            if (FMRadioStation.getStationCount(FMRadioActivity.this, FMRadioStation.STATION_TYPE_FAVORITE) >= FMRadioStation.MAX_FAVORITE_STATION_COUNT) {
                                showToast(getString(R.string.toast_favorite_full));
                            }
                            else {
                                // Add
                                FMRadioStation.insertStationToDB(
                                    FMRadioActivity.this,
                                    getString(R.string.default_station_name),
                                    miCurrentStation,
                                    FMRadioStation.STATION_TYPE_FAVORITE
                                );
                                mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_on);
                                mTextStationName.setText(FMRadioStation.getStationName(FMRadioActivity.this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE));
                                showToast(getString(R.string.toast_channel_added));
                            }
                        }
                    }
                    else {
                        // The toast is already displayed. Do nothing.
                    }
                    FMRadioLogUtils.d(TAG, "<<< onClick AddToFavorite");
                }
            }
        );
        mButtonAddToFavorite.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
                {                   
                    if(event.getAction() == KeyEvent.ACTION_UP || event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
                                        changeBackground(v, event.getAction(), com.android.internal.R.drawable.btn_star_big_on_pressed, com.android.internal.R.drawable.btn_star_big_on, false);
                                    }
                                else {
                                    changeBackground(v, event.getAction(), com.android.internal.R.drawable.btn_star_big_off_pressed, com.android.internal.R.drawable.btn_star_big_off, false);
                                }
                    }
              }
                else
                {
                    if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
                                    changeBackground(v, KeyEvent.ACTION_UP, com.android.internal.R.drawable.btn_star_big_on_pressed, com.android.internal.R.drawable.btn_star_big_on, false);
                                }
                                else {
                                    changeBackground(v, KeyEvent.ACTION_UP, com.android.internal.R.drawable.btn_star_big_off_pressed, com.android.internal.R.drawable.btn_star_big_off, false);
                                }
                }
                return false;
            }
        }
        );

        
        
        
        mTxtRecInfoLeft = (TextView) findViewById(R.id.txtRecInfoLeft);
        mTxtRecInfoRight = (TextView) findViewById(R.id.txtRecInfoRight);
        mRLRecordInfo = (RelativeLayout) findViewById(R.id.rl_recinfo);
        // Register broadcast receiver.
        IntentFilter filter = new IntentFilter();
        filter.addAction(FMRadioService.ACTION_STATE_CHANGED);
        filter.addAction(FMRadioService.ACTION_RDS_PS_CHANGED);
        filter.addAction(FMRadioService.ACTION_RDS_RT_CHANGED);
        filter.addAction(FMRadioService.ACTION_RDS_AF_ACTIVED);
        filter.addAction(FMRadioService.ACTION_RDS_TA_ACTIVED);
        filter.addAction(FMRadioService.ACTION_RDS_TA_DEACTIVED);
        if (FeatureOption.MTK_FM_RECORDING_SUPPORT) {
            filter.addAction(FMRadioService.ACTION_RECORDING_STATE_CHANGED);
            filter.addAction(FMRadioService.ACTION_RECORDER_ERROR);
            filter.addAction(FMRadioService.ACTION_RECORDING_MODE_CHANGED);
        }
        mBroadcastReceiver = new FMBroadcastReceiver();
        FMRadioLogUtils.d(TAG, "Register broadcast receiver.");
        registerReceiver(mBroadcastReceiver, filter);

        // Get all the views and set their actions.
        //miCurrentStation = FMRadioStation.getCurrentStation(this);

        mAnimation = (Animation)AnimationUtils.loadAnimation(this, R.drawable.anim);
        mAnimImage = (ImageView)findViewById(R.id.iv_anim);
        mAnimImage.setVisibility(View.INVISIBLE);
                
        mTextStationName = (TextView)findViewById(R.id.station_name);
        if (bIsFavoriteStation){
            mTextStationName.setText(FMRadioStation.getStationName(
                   this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE)
               );
        }
        
        mTextStationValue = (TextView)findViewById(R.id.station_value);
        mTextStationValue.setText(String.valueOf((float)miCurrentStation / 10));
        
        mTextRDS = (TextView)findViewById(R.id.text_rds);
        mTextRDS.setText("");
        
        mTextFM = (TextView)findViewById(R.id.text_fm);
        mTextFM.setText("FM");
        
        mTextMHz = (TextView)findViewById(R.id.text_mhz);
        mTextMHz.setText("MHz");
        
        mButtonDecrease = (ImageButton)findViewById(R.id.button_decrease);
        mButtonDecrease.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    FMRadioLogUtils.d(TAG, ">>> onClick Decrease");
                    // Decrease the current station value with 1.
                    int iStation = miCurrentStation - 1;
                    if (iStation < FMRadioStation.LOWEST_STATION) {
                        iStation = FMRadioStation.HIGHEST_STATION;
                    }
                    tuneToStation(iStation);
                    FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
                    FMRadioLogUtils.d(TAG, "<<< onClick Decrease");
                }
            }
        );
        
        mButtonPrevStation = (ImageButton)findViewById(R.id.button_prevstation);
        mButtonPrevStation.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    FMRadioLogUtils.d(TAG, ">>> onClick PrevStation");
                    // Search for the previous station.
                    seekStation(false, miCurrentStation, false);     //false: previous station   true: next station
                    
                    FMRadioLogUtils.d(TAG, "<<< onClick PrevStation");
                }
            }
        );
        
        mButtonNextStation = (ImageButton)findViewById(R.id.button_nextstation);
        mButtonNextStation.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    FMRadioLogUtils.d(TAG, ">>> onClick NextStation");
                    // Search for the next station.
                    seekStation(false, miCurrentStation, true);     //false: previous station   true: next station
                
                    FMRadioLogUtils.d(TAG, "<<< onClick NextStation");
                }
            }
        );
        
        mButtonIncrease = (ImageButton)findViewById(R.id.button_increase);
        mButtonIncrease.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    FMRadioLogUtils.d(TAG, ">>> onClick Increase");
                    // Increase the current station value with 1.
                    int iStation = miCurrentStation + 1;
                    if (iStation > FMRadioStation.HIGHEST_STATION) {
                        iStation = FMRadioStation.LOWEST_STATION;
                    }
                    tuneToStation(iStation);
                    FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
                    FMRadioLogUtils.d(TAG, "<<< onClick Increase");
                }
            }
        );
        
        mButtonFavorite = (ImageButton)findViewById(R.id.button_favorite);
        mButtonFavorite.setOnTouchListener(
            new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    changeBackground(v, event.getAction(), R.drawable.btn_fm_list_pressed, R.drawable.btn_fm_list, false);
                    return false;
                }
            }
        );
        mButtonFavorite.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    // Show favorite activity.
                    Intent intent = new Intent();
                    intent.setClass(FMRadioActivity.this, FMRadioFavorite.class);
                    startActivityForResult(intent, REQUEST_CODE_FAVORITE);
                }
            }
        );
         mButtonFavorite.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
                {    
                    if(event.getAction() == KeyEvent.ACTION_UP || event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        changeBackground(v, event.getAction(), R.drawable.btn_fm_list_pressed, R.drawable.btn_fm_list, false);
                       }
                }
                else
                {
                    changeBackground(v, KeyEvent.ACTION_UP, R.drawable.btn_fm_list_pressed, R.drawable.btn_fm_list, false);
                   }
                return false;
            }
        }
        );
        
        mButtonPlayStop = (ImageButton)findViewById(R.id.button_play_stop);
        if (mbPlaying) {
            mButtonPlayStop.setImageResource(R.drawable.btn_fm_stop);
        }
        else {
            mButtonPlayStop.setImageResource(R.drawable.btn_fm_play);
        }
        mButtonPlayStop.setOnTouchListener(
            new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (mbPlaying) {
                        changeBackground(v, event.getAction(), R.drawable.btn_fm_stop_pressed, R.drawable.btn_fm_stop, false);
                    }
                    else {
                        changeBackground(v, event.getAction(), R.drawable.btn_fm_play_pressed, R.drawable.btn_fm_play, false);
                    }
                    return false;
                }
            }
        );
        mButtonPlayStop.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    // Judge the current status and play/pause the radio.
                    if (mbPlaying) {
                        onPauseFM();
                    }
                    else {
                     onPlayFM();
                    }
                    //FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
                }
            }
        );
         mButtonPlayStop.setOnKeyListener(new View.OnKeyListener()
        {
            
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {    
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
                {    
                    if(event.getAction() == KeyEvent.ACTION_UP || event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if (mbPlaying) {
                           changeBackground(v, event.getAction(), R.drawable.btn_fm_stop_pressed, R.drawable.btn_fm_stop, false);
                        }
                        else {
                            changeBackground(v, event.getAction(), R.drawable.btn_fm_play_pressed, R.drawable.btn_fm_play, false);
                        }
                    }
                }
                else
                {    
                    if (mbPlaying) {
                      changeBackground(v, KeyEvent.ACTION_UP, R.drawable.btn_fm_stop_pressed, R.drawable.btn_fm_stop, false);
                    }
                    else {
                        changeBackground(v, KeyEvent.ACTION_UP, R.drawable.btn_fm_play_pressed, R.drawable.btn_fm_play, false);
                    }
                }
                return false;
            }
        }
        );
        
        mButtonEarLoud = (ImageButton)findViewById(R.id.button_ear_loud);
        // The button image will be updated when FM service is connected.
        mButtonEarLoud.setImageResource(R.drawable.btn_fm_loud);
        /*if (isEarphoneUsed()) {
            mButtonEarLoud.setImageResource(R.drawable.btn_fm_loud);
        }
        else {
            mButtonEarLoud.setImageResource(R.drawable.btn_fm_micro);
        }*/
        mButtonEarLoud.setOnTouchListener(
            new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (isEarphoneUsed()) {
                        changeBackground(v, event.getAction(), R.drawable.btn_fm_loud_pressed, R.drawable.btn_fm_loud, false);
                    }
                    else {
                        changeBackground(v, event.getAction(), R.drawable.btn_fm_micro_pressed, R.drawable.btn_fm_micro, false);
                    }
                    return false;
                }
            }
        );
        mButtonEarLoud.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    // Judge the current output and switch between the devices.
                    if (isEarphoneUsed()) {
                        onUseLoudspeaker();
                    }
                    else {
                        onUseEarphone();
                    }
                }
            }
        );
        mButtonEarLoud.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
                {    
                    if(event.getAction() == KeyEvent.ACTION_UP || event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if (isEarphoneUsed()) {
                            changeBackground(v, event.getAction(), R.drawable.btn_fm_loud_pressed, R.drawable.btn_fm_loud, false);
                        }
                        else {
                            changeBackground(v, event.getAction(), R.drawable.btn_fm_micro_pressed, R.drawable.btn_fm_micro, false);
                        }
                    }
                  }
                else
                {    
                    if (isEarphoneUsed()) {
                        changeBackground(v, KeyEvent.ACTION_UP, R.drawable.btn_fm_loud_pressed, R.drawable.btn_fm_loud, false);
                    }
                    else {
                        changeBackground(v, KeyEvent.ACTION_UP, R.drawable.btn_fm_micro_pressed, R.drawable.btn_fm_micro, false);
                    }
                }
                return false;
            }
        }
        );
        
        mMainView = (RelativeLayout)findViewById(R.id.main_view);
        if (FeatureOption.MTK_THEMEMANAGER_APP)
                    mMainView.setThemeContentBgColor(0xff000000);
        
        // When created, detect whether the antenna exists.
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                FMRadioLogUtils.d(TAG, ">>> handleMessage ID: " + msg.getData().getInt(TYPE_MSGID));
                if (mbDestroying) {
                    FMRadioLogUtils.d(TAG, "Warning: app is being destroyed.");
                    FMRadioLogUtils.d(TAG, "<<< handleMessage");
                    return;
                }
                
                if (MSGID_RETRY == msg.getData().getInt(TYPE_MSGID)) {
                    if (!isAntennaAvailable()) {
                        FMRadioLogUtils.d(TAG, "The antenna is still not pluged in.");
                        showDialog(DLGID_NOANTENNA);
                    }
                    else {
                        // Should automatically start FM Radio.
                        onPlayFM();
                    }
                }
                else if (MSGID_OK == msg.getData().getInt(TYPE_MSGID)) {
                    if (mbSearching) {
                        // A searching process is ongoing. Do nothing.
                        FMRadioLogUtils.d(TAG, "Warning: Already searching.");
                    }
                    else {
                        mbSearching = true;
                        FMRadioLogUtils.d(TAG, "Start searching.");
                        
                        // Start to search all the available stations. Use a progress dialog to show the progress.
                        mDialogSearchProgress = new ProgressDialog(FMRadioActivity.this);
                        mDialogSearchProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        String text = getString(R.string.dlg_progress_text);
                        mDialogSearchProgress.setMessage(text);
                        mDialogSearchProgress.setTitle(R.string.dlg_progress_title);
                        mDialogSearchProgress.setCancelable(true);
                        
                        mDialogSearchProgress.setOnCancelListener(
                            new DialogInterface.OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    FMRadioLogUtils.i(TAG, "User canceled searching progress.");
                                    mbUserCancelSearch = true;
                                    stopScan();
                                    
                                    mDialogSearchProgress = null;
                                }
                            }
                        );
                        mDialogSearchProgress.show();
                        
                        // Start the search thread.
                        Thread threadSearch = new Thread() {
                            public void run() {
                                FMRadioLogUtils.d(TAG, ">>> searchThread.run()");
                                
                                int[] iChannels = startScan();
                                int iFirstValidChannel = 0;

                                int iChannelNum = 0;
                                if (null != iChannels) {
                                    FMRadioLogUtils.v(TAG, "Stations found: " + iChannels.length);
                                    // Save the searched stations into data base.
                                    for (int i = 0; i < iChannels.length; i++) {
                                        FMRadioLogUtils.v(TAG, "Stations found " + i + ": " + iChannels[i]);
                                        if (iChannels[i] > FMRadioStation.HIGHEST_STATION
                                            ||  iChannels[i] < FMRadioStation.LOWEST_STATION) {
                                            FMRadioLogUtils.v(TAG, "Ignore the invalid station.");
                                        }
                                        else {
                                            if (iFirstValidChannel == 0) {
                                                iFirstValidChannel = iChannels[i];
                                            }
                                            FMRadioStation.insertStationToDB(
                                                FMRadioActivity.this,
                                                getString(R.string.default_station_name),
                                                iChannels[i],
                                                FMRadioStation.STATION_TYPE_SEARCHED
                                            );
                                            iChannelNum++;
                                        }
                                    }
                                }
                                
                                FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
                                if (!mbDestroying) {
                                    if (null != iChannels) {
                                        // After search finished, tune to the proper station.
                                        if (mbPlaying) {
                                            // When playing, we must tune to a station so that the FM chip
                                            // can start to work.
                                            int iFreq = 0;
                                            if (!mbUserCancelSearch) {
                                                // Searching finished successfully,
                                                // so tune to the first searched station.
                                                iFreq = iFirstValidChannel;
                                            }
                                            else {
                                                // User canceled the searching progress, so tune to the current station.
                                                iFreq = (short)miCurrentStation;
                                            }
                                            if (iFreq > 0) {
                                                Message msg = new Message();
                                                msg.setTarget(mHandler);
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(TYPE_MSGID, MSGID_SEARCH_FINISH);
                                                bundle.putInt(TYPE_FIRST_SEARCHED_STATION, iFreq);
                                                msg.setData(bundle);
                                                msg.sendToTarget();
                                                FMRadioLogUtils.d(TAG, "Playing. Send message to tune station: " + iFreq);
                                            }
                                        }
                                        else {
                                            // If not playing, only tune station when searching finished successfully.
                                            if (!mbUserCancelSearch && iFirstValidChannel > 0) {
                                                Message msg = new Message();
                                                msg.setTarget(mHandler);
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(TYPE_MSGID, MSGID_SEARCH_FINISH);
                                                bundle.putInt(TYPE_FIRST_SEARCHED_STATION, iFirstValidChannel);
                                                msg.setData(bundle);
                                                msg.sendToTarget();
                                                FMRadioLogUtils.d(
                                                    TAG, "Not playing. Send message to tune station: "
                                                    + iFirstValidChannel
                                                );
                                            }
                                            else {
                                                // Do nothing.
                                            }
                                        }
                                    }
                                    else {
                                        FMRadioLogUtils.d(TAG, "No stations found.");
                                        boolean mIsResumeAfterCall = false;
                                        try{
                                            mIsResumeAfterCall = mService.getResumeAfterCall();
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }
                                        if (mbPlaying && (!mIsResumeAfterCall)) {
                                            // need ap to unmute, because scan driver will mute, but not unmute when stopScan() 
                                            setMute(false);
                                            try {
                                                mService.resumeFMAudio();
                                            } catch (Exception ex) {
                                                FMRadioLogUtils.e(TAG, "Exception in resumeFMAudio: " + ex);
                                            }
                                        }
                                    }
                                    
                                    // Dismiss the search progress dialog.
                                    if (null != mDialogSearchProgress) {
                                        mDialogSearchProgress.dismiss();
                                        mDialogSearchProgress = null;
                                    }

                                    // Need to toast if user did not cancel searching.
                                    if (!mbUserCancelSearch) {
                                        if (!isToasting()) {
                                            String text = "";
                                            if (0 == iChannelNum) {
                                                text = getString(R.string.toast_cannot_search);
                                            }
                                            else {
                                                text = getString(R.string.toast_channel_searched) + " " + iChannelNum;
                                            }
                                            // Send message to show toast.
                                            Message msg = new Message();
                                            msg.setTarget(mHandler);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(TYPE_MSGID, MSGID_SHOW_TOAST);
                                            bundle.putString(TYPE_TOAST_STRING, text);
                                            msg.setData(bundle);
                                            msg.sendToTarget();
                                        }
                                    }

                                    mbUserCancelSearch = false;
                                    mbSearching = false;
                                }
                                else {
                                    // The application is being destroyed. Do nothing.
                                    FMRadioLogUtils.d(TAG, "FMRadio is being destroyed.");
                                }
                                
                                FMRadioLogUtils.d(TAG, "<<< searchThread.run()");
                            }
                        };
                        threadSearch.start();
                    }
                }
                else if (MSGID_SEARCH_FINISH == msg.getData().getInt(TYPE_MSGID)) {
                    boolean isSIMCardIdle = true;
                    try {
                        isSIMCardIdle = mService.isSIMCardIdle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isSIMCardIdle) {
                        tuneToStation(msg.getData().getInt(TYPE_FIRST_SEARCHED_STATION,
                                FMRadioStation.LOWEST_STATION));

                        try {
                            // start FM audio after tune finished to avoid previous station's sound
                            mService.resumeFMAudio();
                        } catch (Exception ex) {
                            FMRadioLogUtils.e(TAG, "Exception: resumeFMAudio");
                        }
                    }
                }
                else if (MSGID_UPDATE_RDS == msg.getData().getInt(TYPE_MSGID)) {
                    showRDS();
                }
                else if (MSGID_UPDATE_CURRENT_STATION == msg.getData().getInt(TYPE_MSGID)) {
                    // Save the current station frequency into data base.
                    FMRadioStation.setCurrentStation(FMRadioActivity.this, miCurrentStation);
                    // Change the station frequency displayed.
                    mTextStationValue.setText(String.valueOf((float)miCurrentStation / 10));
                    
                    if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
                        mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_on);

                        mTextStationName.setText(
                            FMRadioStation.getStationName(FMRadioActivity.this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE)
                        );
                    }
                    else {
                        mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_off);
                        
                        mTextStationName.setText("");
                    }
                }
                else if (MSGID_SEEK_FINISH == msg.getData().getInt(TYPE_MSGID)){
                    tuneToStation(msg.getData().getInt(TYPE_SEEK_STATION));
                    seekStation(true, 0, true);
                    FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
                   }
                else if (MSGID_SEEK_FAIL == msg.getData().getInt(TYPE_MSGID)){
                    seekStation(true, 0, true); 
                    FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
                    }
                else if (MSGID_PLAY_FINISH == msg.getData().getInt(TYPE_MSGID)){
                    mButtonPlayStop.setImageResource(R.drawable.btn_fm_stop);
                    refreshButtonStatus();
                
                    stopAnimation();
                    mButtonPlayStop.setEnabled(true);
                    mButtonFavorite.setEnabled(true);
                    mButtonAddToFavorite.setEnabled(true);
                }
                else if (MSGID_PLAY_FAIL == msg.getData().getInt(TYPE_MSGID)){
                    stopAnimation();
                    mButtonPlayStop.setEnabled(true);
                    mButtonFavorite.setEnabled(true);
                    mButtonAddToFavorite.setEnabled(true);
                }
                else if (MSGID_ANTENNA_UNAVAILABE == msg.getData().getInt(TYPE_MSGID)){
                    showDialog(DLGID_NOANTENNA);

                    stopAnimation();
                    mButtonPlayStop.setEnabled(true);
                    mButtonFavorite.setEnabled(true);
                    mButtonAddToFavorite.setEnabled(true);
                }
                else if (MSGID_SHOW_TOAST== msg.getData().getInt(TYPE_MSGID)){
                    String text = msg.getData().getString(TYPE_TOAST_STRING);
                    showToast(text);
                }
                else {
                    FMRadioLogUtils.e(TAG, "Error: undefined message ID.");
                }
                FMRadioLogUtils.d(TAG, "<<< handleMessage");
            }
        };
        
        mAM = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        //mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        //mWakeLock.setReferenceCounted(false);
        
        refreshButtonStatus();
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onCreate");
    }
    
    public void onStart() {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onStart");
        super.onStart();
        
        // Should start FM service first.
        ComponentName cn = startService(new Intent(FMRadioActivity.this, FMRadioService.class));
        if (null == cn) {
            FMRadioLogUtils.e(TAG, "Error: Cannot start FM service");
        }
        else {
            FMRadioLogUtils.d(TAG, "Start FM service successfully.");
            mbServiceStarted = true;

            mServiceConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder service) {
                    FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onServiceConnected");
                    mService = IFMRadioService.Stub.asInterface(service);
                    if (null == mService) {
                        FMRadioLogUtils.e(TAG, "Error: null interface");
                        finish();
                    }
                    else {
                        if (!isServiceInit()) {
                            FMRadioLogUtils.d(TAG, "FM service is not init.");
                            initService(miCurrentStation);

                            // Get RDS settings from database and set them into FM service.
                            mbPSRTEnabled = FMRadioStation.getEnablePSRT(FMRadioActivity.this);
                            mbAFEnabled= FMRadioStation.getEnableAF(FMRadioActivity.this);
                            //mbTAEnabled= FMRadioStation.getEnableTA(FMRadioActivity.this);
                            enablePSRT(mbPSRTEnabled);
                            enableAF(mbAFEnabled);
                            //enableTA(mbTAEnabled);
                            
                            // because setting running service just kill service, activity is still alive, so need to update UI
                            LinearLayout top_bar = (LinearLayout)findViewById(R.id.top_bar);
                            LinearLayout rec_bar = (LinearLayout)findViewById(R.id.bottom_bar_recorder);
                            LinearLayout bottom_bar = (LinearLayout)findViewById(R.id.bottom_bar);
                            RelativeLayout rec_info_bar = (RelativeLayout)findViewById(R.id.rl_recinfo);
                            mbIsRecording = false;
                            top_bar.setVisibility(View.VISIBLE);
                            bottom_bar.setVisibility(View.VISIBLE);
                            rec_bar.setVisibility(View.GONE);
                            mButtonAddToFavorite.setVisibility(View.VISIBLE);
                            rec_info_bar.setVisibility(View.GONE);
                            
                            refreshButtonStatus(false);
                            
                            InitialThread thread = new InitialThread(mSavedInstanceState);
                            mSavedInstanceState = null;
                            thread.start();
                            if (FeatureOption.MTK_MT519X_FM_SUPPORT) {
                                try {
                                    Thread.sleep(1000);
                                }
                                catch(InterruptedException e) {
                                    e.printStackTrace();
                                    FMRadioLogUtils.e(TAG, "Exception: Thread.sleep.");
                                }
                            }
                            startAnimation();
                        }
                        else {
                            FMRadioLogUtils.d(TAG, "FM service is already init.");
                            if (isDeviceOpen()) {
                                // Get the current frequency in service and save it into database.
                                int iFreq = getFrequency();
                                if (iFreq > FMRadioStation.HIGHEST_STATION
                                    || iFreq < FMRadioStation.LOWEST_STATION) {
                                    FMRadioLogUtils.e(TAG, "Error: invalid frequency in service.");
                                }
                                else {
                                    if (miCurrentStation != iFreq) {
                                        FMRadioLogUtils.d(TAG, "The frequency in FM service is not same as in database.");
                                        miCurrentStation = iFreq;

                                        // Save the current station frequency into data base.
                                        FMRadioStation.setCurrentStation(FMRadioActivity.this, miCurrentStation);
                                        // Change the station frequency displayed.
                                        mTextStationValue.setText(String.valueOf((float)miCurrentStation / 10));
                                        
                                        if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
                                            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_on);

                                            mTextStationName.setText(
                                                FMRadioStation.getStationName(FMRadioActivity.this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE)
                                            );
                                        }
                                        else {
                                            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_off);
                                            
                                            mTextStationName.setText("");
                                        }
                                    }
                                    else {
                                        FMRadioLogUtils.d(TAG, "The frequency in FM service is same as in database.");
                                    }
                                }
                                
                                mbPlaying = isPowerUp();
                                mbRDSSupported = (1 == isRDSSupported()? true : false);
                                //... Check if RDS is enabled.
                                if (mbRDSSupported) {
                                    mbRDSEnabled = true;
                                }
                                else {
                                    mbRDSEnabled = false;
                                }

                                if (mbPlaying) {
                                    FMRadioLogUtils.d(TAG, "FM is already power up.");
                                    mButtonPlayStop.setImageResource(R.drawable.btn_fm_stop);
                                    refreshButtonStatus();
                                }

                                if (mbRDSSupported) {
                                    FMRadioLogUtils.d(TAG, "RDS is supported.");
                                    
                                    // Get RDS settings from FM server.
                                    mbPSRTEnabled = isPSRTEnabled();
                                    mbAFEnabled = isAFEnabled();
                                    //mbTAEnabled = isTAEnabled();

                                    if (mbPlaying) {
                                        // Update the RDS text view.
                                        msPS = getPS();
                                        msLRText = getLRText();
                                        Message msg = new Message();
                                        msg.setTarget(mHandler);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(TYPE_MSGID, MSGID_UPDATE_RDS);
                                        msg.setData(bundle);
                                        msg.sendToTarget();
                                    }
                                }
                                else {
                                    FMRadioLogUtils.d(TAG, "RDS is not supported.");
                                }
                                
                                if (FeatureOption.MTK_FM_RECORDING_SUPPORT) {
                                    try {
                                        mbIsRecording = mService.getRecordingMode();
                                        changeRecordingMode(mbIsRecording);
                                        if (mbIsRecording) {
                                            refreshRecordingStatus(FMRecorder.STATE_INVALID);
                                        }
                                    } catch (Exception ex) {
                                        FMRadioLogUtils.e(TAG, "getRecordingMode: " + ex);
                                    }
                                }

                                // Now the activity is inited.
                                mbInited = true;
                            }
                            else {
                                // This is theoretically never happen.
                                FMRadioLogUtils.e(TAG, "Error: FM device is not open");
                            }
                        }
                        // Update the earphone button.
                        if (isEarphoneUsed()) {
                            mButtonEarLoud.setImageResource(R.drawable.btn_fm_loud);
                        }
                        else {
                            mButtonEarLoud.setImageResource(R.drawable.btn_fm_micro);
                        }
                        if (FeatureOption.MTK_FM_RECORDING_SUPPORT && mSavedInstanceState != null) {
                            boolean bIsSaveDlgShown = mSavedInstanceState.getBoolean(FM_INSTANCE_STATE_RECORDING_DLGSTATE, false);
                            if (bIsSaveDlgShown) {
                                mSDDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
                                mIsStorageWarning = !checkRemainingStorage();
                                removeAndShowDialog(DLGID_SAVE_RECORDING);
                            }
                        }
                    }
                    FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onServiceConnected");
                }

                public void onServiceDisconnected(ComponentName className) {
                    FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onServiceDisconnected");
                    mService = null;
                    FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onServiceDisconnected");
                }
            };
            mbServiceBinded = bindService(
                new Intent(FMRadioActivity.this, FMRadioService.class),
                mServiceConnection,
                Context.BIND_AUTO_CREATE);
        }
        if (!mbServiceBinded) {
            FMRadioLogUtils.e(TAG, "Error: Cannot bind FM service");
            finish();
            FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onCreate");
            return;
        }
        else {
            FMRadioLogUtils.d(TAG, "Bind FM service successfully.");
        }
        
        //after configuration change, need to reduction else the UI is abnormal
        if (null != getLastNonConfigurationInstance()) {
            FMRadioLogUtils.d(TAG, "Configration changes,activity restart,need to reset UI!");
            Bundle bundle = (Bundle)getLastNonConfigurationInstance();
            mSDDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (null != bundle) {
                mPrevRecorderState = bundle.getInt("mPrevRecorderState");
                mRecordState = bundle.getInt("mRecordState");
                mIsFreshRecordingStatus = bundle.getBoolean("mIsFreshRecordingStatus");
                //we doesn't get it from service because the service may be null because not bind
                boolean isInRecordingMode = bundle.getBoolean("isInRecordingMode");
                FMRadioLogUtils.d(TAG, "isInRecordingMode = " + isInRecordingMode + ";mPrevRecorderState = " + mPrevRecorderState);
                SharedPreferences sharedPreferences = getSharedPreferences(REFS_NAME, 0);
                mRecordStartTime = sharedPreferences.getLong(START_RECORD_TIME, 0);
                mPlayStartTime =sharedPreferences.getLong(START_PLAY_TIME, 0);
                //Reset recording UI
                LinearLayout top_bar = (LinearLayout)findViewById(R.id.top_bar);
                LinearLayout rec_bar = (LinearLayout)findViewById(R.id.bottom_bar_recorder);
                LinearLayout bottom_bar = (LinearLayout)findViewById(R.id.bottom_bar);
                RelativeLayout rec_info_bar = (RelativeLayout)findViewById(R.id.rl_recinfo);
                
                top_bar.setVisibility(isInRecordingMode ? View.GONE : View.VISIBLE);
                bottom_bar.setVisibility(isInRecordingMode ? View.GONE : View.VISIBLE);
                rec_bar.setVisibility(isInRecordingMode ? View.VISIBLE: View.GONE);
                if ((FMRecorder.STATE_RECORDING == mRecordState) || (FMRecorder.STATE_PLAYBACK == mRecordState)) {
                    rec_info_bar.setVisibility(View.VISIBLE);
                } else {
                    rec_info_bar.setVisibility(View.GONE);
                }
                mButtonAddToFavorite.setVisibility(isInRecordingMode ? View.GONE : View.VISIBLE);
                
                //Send a message to handler to refresh UI to display recording time or playing time 
                mRefresher.sendEmptyMessage(MSGID_REFRESH);
            }
        }
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onStart");
    }
    
    public void onResume() {
        super.onResume();
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onResume");
        mIsActivityBackground = false;
        if (mIsFreshRecordingStatus) {
            refreshRecordingStatus(mRecordState);
            mIsFreshRecordingStatus = false;
        } else {
            FMRadioLogUtils.d(TAG,"doesn't need to refresh recording status");
        }
        String fileName = null;
        File recordingFolderPath = null;
        File recordingFileToSave = null;
        // if recording file delete by user,play button disabled
        try {
            fileName = mService.getRecordingName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != fileName) {
            recordingFolderPath = new File(mSDDirectory, "FM Recording");
            recordingFileToSave = new File(recordingFolderPath, fileName 
                    + FMRecorder.RECORDING_FILE_EXTENSION);
            if (!recordingFileToSave.exists()) {
                mBtnPlayback.setEnabled(false);
            }
        }
        
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onResume");
    }
    
    public void onPause() {
        super.onPause();
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onPause");
        mIsActivityBackground = true;
        changeBackground((View)mButtonFavorite, KeyEvent.ACTION_UP, R.drawable.btn_fm_list_pressed, R.drawable.btn_fm_list, false);

        if (mbPlaying) {
            changeBackground((View)mButtonPlayStop, KeyEvent.ACTION_UP, R.drawable.btn_fm_stop_pressed, R.drawable.btn_fm_stop, false);
            }
        else {
            changeBackground((View)mButtonPlayStop, KeyEvent.ACTION_UP, R.drawable.btn_fm_play_pressed, R.drawable.btn_fm_play, false);
            }

        if (isEarphoneUsed()) {
            changeBackground((View)mButtonEarLoud, KeyEvent.ACTION_UP, R.drawable.btn_fm_loud_pressed, R.drawable.btn_fm_loud, false);
            }
        else {
            changeBackground((View)mButtonEarLoud, KeyEvent.ACTION_UP, R.drawable.btn_fm_micro_pressed, R.drawable.btn_fm_micro, false);
            }

        if (FMRadioStation.isFavoriteStation(FMRadioActivity.this, miCurrentStation)) {
            changeBackground((View)mButtonAddToFavorite, KeyEvent.ACTION_UP, com.android.internal.R.drawable.btn_star_big_on_pressed, com.android.internal.R.drawable.btn_star_big_on, false);
            }
        else {
            changeBackground((View)mButtonAddToFavorite, KeyEvent.ACTION_UP, com.android.internal.R.drawable.btn_star_big_off_pressed, com.android.internal.R.drawable.btn_star_big_off, false);
            }
            
        
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onPause");
    }
    
    public void onStop() {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onStop");
        super.onStop();
        
        if (mbExitPressed) {
            // Should powerdown FM.
            if (mbPlaying) {
                FMRadioLogUtils.d(TAG, "FM is Playing. So stop it.");
                setMute(true);
                rdsset(false);
                powerDown();
                mbPlaying = false;
            }
        }
        
        // Unbind the FM service.
        if (mbServiceBinded) {
            unbindService(mServiceConnection);
            mbServiceBinded = false;
        }
        
        if (mbExitPressed) {
            if (mbServiceStarted) {
                boolean bRes = stopService(new Intent(FMRadioActivity.this,FMRadioService.class));
                if (!bRes) {
                    FMRadioLogUtils.e(TAG, "Error: Cannot stop the FM service.");
                }
                mbServiceStarted = false;
            }
        }
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onStop");
    }
    
    public void onDestroy() {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onDestroy");
        mbDestroying = true;
        // If searching, we should stop it and dismiss the progress dialog.
        if (mbSearching) {
            stopScan();
            if (null != mDialogSearchProgress) {
                mDialogSearchProgress.dismiss();
                mDialogSearchProgress = null;
            }

            // Must tune to a station, or FM will not be played.
            tuneToStation(miCurrentStation);
        }
        
        // Unregister the broadcast receiver.
        if (null != mBroadcastReceiver) {
            FMRadioLogUtils.d(TAG, "Unregister broadcast receiver.");
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }

        if (FeatureOption.MTK_FM_RECORDING_SUPPORT && mIsSDListenerRegistered) {
            unregisterReceiver(mSDListener);
            mIsSDListenerRegistered = false;
        }

        
        //need to call this function because if doesn't do this,after configuration change
        //will have many instance and recording time or playing time will not refresh
        if(null != mRefresher) {
            mRefresher.removeCallbacksAndMessages(null);
        }
        
        // Release the wake lock.
        //mWakeLock.release();
        
        super.onDestroy();
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onDestroy");
    }
    
    class InitialThread extends Thread{
        private Bundle savedInstanceState;

        public InitialThread(Bundle params){
            savedInstanceState = params;
        }
        
        public void run(){
            if (!openDevice()) {
                //... If failed, exit?
                FMRadioLogUtils.e(TAG, "Error: opendev failed.");
            }
            else {
                FMRadioLogUtils.d(TAG, "opendev succeed.");
            }

            // Check if RDS is supported.
            mbRDSSupported = (1 == isRDSSupported()? true : false);

            //... Check if RDS is enabled.
            if (mbRDSSupported) {
                mbRDSEnabled = true;
            }
            else {
                mbRDSEnabled = false;
            }
            
            // The app maybe killed at the previous time. So after opendev, get the power state.
            mbPlaying = isPowerUp();
            if (mbPlaying) {
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_PLAY_FINISH);
                msg.setData(bundle);
                msg.sendToTarget();
            }
            else {
                if (!isAntennaAvailable()) {
                    int ret = -1;
                    try {
                        ret = mService.switchAntenna(1);
                    } catch (Exception ex) {
                        FMRadioLogUtils.e(TAG, "Exception: switchAntenna(1)");
                    }
                    if (ret != 0) {
                        // short antenna is not supported
                        FMRadioLogUtils.e(TAG, "failed to switch to short antenna: errorcode=" + ret);
                        FMRadioLogUtils.d(TAG, "Antenna is unavailable. Ask if plug in antenna.");
                        Message msg = new Message();
                        msg.setTarget(mHandler);
                        Bundle bundle = new Bundle();
                        bundle.putInt(TYPE_MSGID, MSGID_ANTENNA_UNAVAILABE);
                        msg.setData(bundle);
                        msg.sendToTarget();
                    } else {
                        // Successfully switched to short antenna, so start FM
                        playFM();
                    }
                }
                else {
                    // Should automatically start FM Radio.
                    playFM();
                }
            }
            
            // Now the activity is inited.
            mbInited = true;
        }
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onCreateOptionsMenu");
        int order = 0;
        menu.add(0, OPTMENUID_SEARCH, order++, R.string.optmenu_search).setIcon(R.drawable.ic_menu_search);
        if (mbRDSSupported) {
            menu.add(0, OPTMENUID_RDS_SETTING, order++, R.string.optmenu_rds_settings).setIcon(android.R.drawable.ic_menu_preferences);
        }
        if (FeatureOption.MTK_FM_RECORDING_SUPPORT) {
            menu.add(0, OPTMENUID_RECORD, order++, R.string.optmenu_record).setIcon(R.drawable.ic_menu_record);
        }
        menu.add(0, OPTMENUID_EXIT, order++, R.string.optmenu_exit).setIcon(R.drawable.ic_menu_close_clear_cancel);
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onCreateOptionsMenu");
        return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!FeatureOption.MTK_FM_RECORDING_SUPPORT)
            return true;
        
        if (mbIsRecording) {
            return false;
        } else {
            menu.findItem(OPTMENUID_SEARCH).setVisible(true).setEnabled(true);
            
            menu.findItem(OPTMENUID_EXIT).setVisible(true).setEnabled(true);
            if (mbRDSSupported) {
                menu.findItem(OPTMENUID_RDS_SETTING).setVisible(true).setEnabled(true);
            }
            boolean isFMOverBT = false;
            try {
                isFMOverBT = mService.isFMOverBTActive();
            } catch (Exception ex) {
                FMRadioLogUtils.e(TAG, "Exception while isFMOverBTActive(): ", ex);
            }
            if (isFMOverBT || !mbPlaying) {
                menu.findItem(OPTMENUID_RECORD).setVisible(false).setEnabled(false);
            } else {
                menu.findItem(OPTMENUID_RECORD).setVisible(true).setEnabled(true);
            }
        }
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onOptionsItemSelected");
        super.onOptionsItemSelected(item);
        boolean bRet = true;
        switch (item.getItemId()) {
            case OPTMENUID_SEARCH: {
                // Clean the searched stations in database.
                FMRadioStation.cleanSearchedStations(FMRadioActivity.this);
                
                // Start searching directly.
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_OK);
                msg.setData(bundle);
                msg.sendToTarget();
                break;
            }
            case OPTMENUID_EXIT: {
                mbExitPressed = true;
                finish();
                break;
            }
            case OPTMENUID_RDS_SETTING: {
                //Do not call showDialog because the dialog shown by showDialog will be restored after config changed.
                //showDialog(DLGID_RDS_SETTING);
                CharSequence[] items = new CharSequence[2];
                //CharSequence[] items = new CharSequence[3];
                //items[3] = getString(R.string.dlg_rds_settings_traffic_announcement);
                items[0] = getString(R.string.dlg_rds_settings_channel_information);
                items[1] = getString(R.string.dlg_rds_settings_alternative_frequency);
                boolean[] checkedItems = new boolean[2];
                //boolean[] checkedItems = new boolean[3];
                checkedItems[0] = mbPSRTEnabled;
                checkedItems[1] = mbAFEnabled;
                //checkedItems[2] = mbTAEnabled;
                mDialogRDSSetting = new AlertDialog.Builder(this)
                    .setTitle(R.string.optmenu_rds_settings)
                    .setMultiChoiceItems(items, checkedItems, 
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                FMRadioLogUtils.d(TAG, ">>> onClick RDS settings choice item: " + which + " checked: " + isChecked);
                                switch (which) {
                                    case 0: {
                                        // RDS PSRT
                                        mbPSRTEnabled = isChecked;
                                        FMRadioStation.setEnablePSRT(FMRadioActivity.this, mbPSRTEnabled);
                                        enablePSRT(mbPSRTEnabled);
                                        break;
                                    }
                                    case 1: {
                                        // RDS AF
                                        mbAFEnabled = isChecked;
                                        FMRadioStation.setEnableAF(FMRadioActivity.this, mbAFEnabled);
                                        enableAF(mbAFEnabled);
                                        break;
                                    }
                                    /*case 2: {
                                        // RDS TA
                                        mbTAEnabled= isChecked;
                                        FMRadioStation.setEnableTA(FMRadioActivity.this, mbTAEnabled);
                                        enableTA(mbTAEnabled);
                                        break;
                                    }*/
                                    default: {
                                        FMRadioLogUtils.e(TAG, "Error: invalid item");
                                        break;
                                    }
                                }
                                FMRadioLogUtils.d(TAG, "<<< onClick RDS settings choice item");
                            }
                        }
                    )
                    .setPositiveButton(R.string.btn_ok, 
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FMRadioLogUtils.d(TAG, ">>> onClick RDS settings Positive");
                                FMRadioLogUtils.d(TAG, "<<< onClick RDS settings Positive");
                            }
                        }
                    )
                    .setCancelable(false) // Only supply OK button.
                    /*.setNegativeButton(R.string.btn_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FMRadioLogUtils.i(TAG, ">>> onClick RDS settings Negative");
                                FMRadioLogUtils.i(TAG, "<<< onClick RDS settings Negative");
                            }
                        }
                    )*/
                    .create();
                mDialogRDSSetting.show();
                break;
            }
            case OPTMENUID_RECORD:
                if (FeatureOption.MTK_FM_RECORDING_SUPPORT) {
                    changeRecordingMode(true);
                    refreshRecordingStatus(FMRecorder.STATE_INVALID);
                }
                break;
            default: {
                FMRadioLogUtils.e(TAG, "Error: Invalid options menu item.");
                bRet = false;
                break;
            }
        }
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onOptionsItemSelected");
        return bRet;
    }
    
    private boolean isAntennaAvailable() {
        if (!FeatureOption.MTK_MT519X_FM_SUPPORT) {
            return mAM.isWiredHeadsetOn();
        }
        else {
            return true;
        }
    }
    
    protected Dialog onCreateDialog(int id) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onCreateDialog ID: " + id);
        Dialog dlgRet = null;
        
        if (id == DLGID_NOANTENNA) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mDialogNoAntenna = builder.setMessage(R.string.dlg_noantenna_text)
                .setTitle(R.string.dlg_noantenna_title)
                .setPositiveButton(R.string.btn_yes, 
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FMRadioLogUtils.d(TAG, ">>> onClick Positive");
                            // We let user use the app if no antenna.
                            // But we do not automatically start FM.
                            if (isAntennaAvailable()) {
                                onPlayFM();
                            } else {
                                int ret = -1;
                                try {
                                    ret = mService.switchAntenna(1);
                                } catch (Exception ex) {
                                    FMRadioLogUtils.e(TAG, "Exception: switchAntenna(1)");
                                }
                                if (ret == 0) {
                                    // Switch to short antenna success
                                    onPlayFM();
                                }
                            }
                        /*    Message msg = new Message();
                            msg.setTarget(mHandler);
                            Bundle bundle = new Bundle();
                            bundle.putInt(TYPE_MSGID, MSGID_RETRY);
                            msg.setData(bundle);
                            msg.sendToTarget();*/
                            FMRadioLogUtils.d(TAG, "<<< onClick Positive");
                        }
                    }
                )
                .setNegativeButton(R.string.btn_no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FMRadioLogUtils.d(TAG, ">>> onClick Negative");
                            dialog.cancel();
                            mbExitPressed = true;
                            finish();
                            FMRadioLogUtils.d(TAG, "<<< onClick Negative");
                        }
                    }
                )
                .create();
            dlgRet = mDialogNoAntenna;
        }
        else if (id == DLGID_RDS_SETTING) {
            CharSequence[] items = new CharSequence[2];
            //CharSequence[] items = new CharSequence[3];
            //items[3] = getString(R.string.dlg_rds_settings_traffic_announcement);
            items[0] = getString(R.string.dlg_rds_settings_channel_information);
            items[1] = getString(R.string.dlg_rds_settings_alternative_frequency);
            boolean[] checkedItems = new boolean[2];
            //boolean[] checkedItems = new boolean[3];
            checkedItems[0] = mbPSRTEnabled;
            checkedItems[1] = mbAFEnabled;
            //checkedItems[2] = mbTAEnabled;
            mDialogRDSSetting = new AlertDialog.Builder(this)
                .setTitle(R.string.optmenu_rds_settings)
                .setMultiChoiceItems(items, checkedItems, 
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            FMRadioLogUtils.d(TAG, ">>> onClick RDS settings choice item: " + which + " checked: " + isChecked);
                            switch (which) {
                                case 0: {
                                    // RDS PSRT
                                    mbPSRTEnabled = isChecked;
                                    FMRadioStation.setEnablePSRT(FMRadioActivity.this, mbPSRTEnabled);
                                    enablePSRT(mbPSRTEnabled);
                                    break;
                                }
                                case 1: {
                                    // RDS AF
                                    mbAFEnabled = isChecked;
                                    FMRadioStation.setEnableAF(FMRadioActivity.this, mbAFEnabled);
                                    enableAF(mbAFEnabled);
                                    break;
                                }
                                /*case 2: {
                                    // RDS TA
                                    mbTAEnabled= isChecked;
                                    FMRadioStation.setEnableTA(FMRadioActivity.this, mbTAEnabled);
                                    enableTA(mbTAEnabled);
                                    break;
                                }*/
                                default: {
                                    FMRadioLogUtils.e(TAG, "Error: invalid item");
                                    break;
                                }
                            }
                            FMRadioLogUtils.d(TAG, "<<< onClick RDS settings choice item");
                        }
                    }
                )
                .setPositiveButton(R.string.btn_ok, 
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FMRadioLogUtils.d(TAG, ">>> onClick RDS settings Positive");
                            FMRadioLogUtils.d(TAG, "<<< onClick RDS settings Positive");
                        }
                    }
                )
                .setCancelable(false) // Only supply OK button.
                /*.setNegativeButton(R.string.btn_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FMRadioLogUtils.i(TAG, ">>> onClick RDS settings Negative");
                            FMRadioLogUtils.i(TAG, "<<< onClick RDS settings Negative");
                        }
                    }
                )*/
                .create();
            dlgRet = mDialogRDSSetting;
        } else if (id == DLGID_SAVE_RECORDING){
            mDlgSaveRecording = new FMRecorderDialog(this, mService);
            FMRadioLogUtils.d(TAG, "Show save recording file dialog.");

            mDlgSaveRecording.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            dlgRet = mDlgSaveRecording;
        }
        else {
            FMRadioLogUtils.e(TAG, "Error: Invalid dialog id in main UI.");
        }
        
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onCreateDialog");
        return dlgRet;
    }
    
    protected void onPrepareDialog(int id, Dialog dlg) {
        if (id == DLGID_SAVE_RECORDING) {
            if (mIsStorageWarning) {
                mStorageWarningTextView = (TextView)mDlgSaveRecording.findViewById(R.id.save_recording_storage_warning);
                mStorageWarningTextView.setVisibility(View.VISIBLE);
            } else {
                FMRadioLogUtils.v(TAG,"mIsStorageWarning= "+mIsStorageWarning);
            }
        } else {
            FMRadioLogUtils.e(TAG, "Error: invalid id");
        }
    }
    
    private void removeAndShowDialog(int id) {
        removeDialog(id);
        showDialog(id);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (REQUEST_CODE_FAVORITE == requestCode) {
                int iStation = data.getIntExtra(FMRadioFavorite.ACTIVITY_RESULT, miCurrentStation);
                // Tune to this station.
                tuneToStation(iStation);
                FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
            }
            else {
                FMRadioLogUtils.e(TAG, "Error: Invalid requestcode.");
            }
        }
        else {
            // Need to update the station name and favorite state because it may be edited.
            //...
            //mTextStationName.setText(FMRadioStation.getStationName(
            //    this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE
            //));
            if (FMRadioStation.isFavoriteStation(this, miCurrentStation)) {
                mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_on);
                mTextStationName.setText(FMRadioStation.getStationName(
                this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE
                ));
            }
            else {
                mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_off);
                mTextStationName.setText("");
            }
            
            // Do not handle other result.
            FMRadioLogUtils.v(TAG, "The activity for requestcode " + requestCode + " does not return any data.");
        }
    }
    
    private void startAnimation(){
        mAnimImage.setAnimation(mAnimation);
        mAnimImage.setVisibility(View.VISIBLE);
    }

    private void stopAnimation(){
        mAnimImage.setVisibility(View.INVISIBLE);
        mAnimImage.setAnimation(null);
    }
    
    private void onPlayFM() {
        FMRadioLogUtils.v(TAG, ">>> onPlayFM");
        
        refreshButtonStatus(false);
        new Thread(){
            public void run(){
                playFM();
            }
        }.start();
        
        if (FeatureOption.MTK_MT519X_FM_SUPPORT) {
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
                FMRadioLogUtils.e(TAG, "Exception: Thread.sleep.");
            }
        }
        startAnimation();
        
        FMRadioLogUtils.v(TAG, "<<< onPlayFM");
    }
    
    private void playFM(){
        FMRadioLogUtils.v(TAG, ">>> PlayFM");
        
        setMute(true);
        rdsset(false);
        boolean bRes = powerUp((float)miCurrentStation / 10);
        if (bRes) {
            rdsset(true);
        
            mbPlaying = true;
            setMute(false);
            
            if (!isAntennaAvailable()) {
                // Antenna not ready, try short antenna
                int ret = -1;
                try {
                    ret = mService.switchAntenna(1);
                } catch (Exception ex) {
                    FMRadioLogUtils.e(TAG, "Exception: switchAntenna(1)");
                }
                if (ret != 0) {
                    FMRadioLogUtils.e(TAG, "Error while trying to switch to short antenna: " + ret);
                }
            }
            
            Message msg = new Message();
            msg.setTarget(mHandler);
            Bundle bundle = new Bundle();
            bundle.putInt(TYPE_MSGID, MSGID_PLAY_FINISH);
            msg.setData(bundle);
            msg.sendToTarget();
            
            // Hold the wake lock.
            //mWakeLock.acquire();
        }
        else {
            setMute(true);
            powerDown();
            
            Message msg = new Message();
            msg.setTarget(mHandler);
            Bundle bundle = new Bundle();
            bundle.putInt(TYPE_MSGID, MSGID_PLAY_FAIL);
            msg.setData(bundle);
            msg.sendToTarget();
            
            FMRadioLogUtils.e(TAG, "Error: Can not power up.");
        }
        FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
        FMRadioLogUtils.v(TAG, "<<< PlayFM");
    }
    
    private void onPauseFM() {
        FMRadioLogUtils.v(TAG, ">>> onPauseFM");
        setMute(true);
            
        rdsset(false);
        if (powerDown()) {
            mButtonPlayStop.setImageResource(R.drawable.btn_fm_play);
            mbPlaying = false;
            refreshButtonStatus();
            
            // Release the wake lock.
            //mWakeLock.release();
        }
        else {
            FMRadioLogUtils.e(TAG, "Error: Can not power down.");
        }
        FMRadioLogUtils.d("FMRadioPerformanceTest", "[mtk performance result]: " + System.currentTimeMillis());
        FMRadioLogUtils.v(TAG, "<<< onPauseFM");
    }
    
    private void onUseEarphone() {
        FMRadioLogUtils.v(TAG, ">>> onUseEarphone");
        useEarphone(true);
        mButtonEarLoud.setImageResource(R.drawable.btn_fm_loud);
        FMRadioLogUtils.v(TAG, "<<< onUseEarphone");
    }
    
    private void onUseLoudspeaker() {
        FMRadioLogUtils.v(TAG, ">>> onUseLoudspeaker");
        useEarphone(false);
        mButtonEarLoud.setImageResource(R.drawable.btn_fm_micro);
        FMRadioLogUtils.v(TAG, "<<< onUseLoudspeaker");
    }
    
    private void tuneToStation(int iStation) {
        FMRadioLogUtils.v(TAG, ">>> tuneToStation: " + (float)iStation / 10);
        if (mbPlaying) {
            FMRadioLogUtils.d(TAG, "FM is playing.");
            rdsset(false);
            boolean bRes = tune((float)iStation / 10);
            if (bRes) {
                FMRadioLogUtils.d(TAG, "Tune to the station succeeded.");
                rdsset(true);
                miCurrentStation = iStation;
                
                // Save the current station frequency into data base.
                FMRadioStation.setCurrentStation(this, miCurrentStation);
            }
            else {
                FMRadioLogUtils.e(TAG, "Error: Can not tune to the station.");
            }
        }
        else {
            FMRadioLogUtils.d(TAG, "FM is paused.");
            miCurrentStation = iStation;
            // Save the current station frequency into data base.
            FMRadioStation.setCurrentStation(this, miCurrentStation);
            
            onPlayFM();
        }

        // Change the station frequency displayed.
        mTextStationValue.setText(String.valueOf((float)miCurrentStation / 10));
        
        if (FMRadioStation.isFavoriteStation(this, miCurrentStation)) {
            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_on);

            mTextStationName.setText(
                FMRadioStation.getStationName(this, miCurrentStation, FMRadioStation.STATION_TYPE_FAVORITE)
            );
        }
        else {
            mButtonAddToFavorite.setImageResource(com.android.internal.R.drawable.btn_star_big_off);
            
            mTextStationName.setText("");
        }
        
        FMRadioLogUtils.v(TAG, "<<< tuneToStation");
    }
    
    private void seekStation(boolean enable, int station, boolean direction){
            if (!enable) {
            // Start seek. We should detect if there is another seek procedure.
            if (mbSeeking) {
                FMRadioLogUtils.w(TAG, "Warning: already seeking");
            }
            else {
                mbSeeking = true;
                        startAnimation();
                refreshButtonStatus(false);
                
                mSeekThread = new SeekThread(station, direction);
                mSeekThread.start();
            }
        }
               else {
            stopAnimation();
            refreshButtonStatus(true);
            mSeekThread = null;
            mbSeeking = false;
        }
    }
    
    private SeekThread mSeekThread = null;
    class SeekThread extends Thread{
       public int currentStation;
       public boolean seekDirection;

       public SeekThread(int station, boolean direction){
           currentStation = station;
           seekDirection = direction;
       }
        public void run(){
        rdsset(false);
                    
            float fStation = seek((float)currentStation / 10, seekDirection);
            int iStation = (int)(fStation * 10);

            if (iStation > FMRadioStation.HIGHEST_STATION || iStation < FMRadioStation.LOWEST_STATION) {
                // Loop to the highest frequency and continue to search valid station.
                if (!seekDirection){
                    fStation = seek((float)(FMRadioStation.HIGHEST_STATION) / 10, seekDirection);
                    iStation = (int)(fStation * 10);
                }
                else {
                    fStation = seek((float)(FMRadioStation.LOWEST_STATION) / 10, seekDirection);
                    iStation = (int)(fStation * 10);
                }
            }
         
            rdsset(true);
            if (iStation > FMRadioStation.HIGHEST_STATION || iStation < FMRadioStation.LOWEST_STATION) {
                // Can not find a valid station.
                FMRadioLogUtils.e(TAG, "Error: Can not search previous station.");
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_SEEK_FAIL);
                msg.setData(bundle);
                msg.sendToTarget();
                }
            else {
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_SEEK_FINISH);
                bundle.putInt(TYPE_SEEK_STATION, iStation);
                msg.setData(bundle);
                msg.sendToTarget();
                FMRadioLogUtils.d(TAG, "Send message to tune to recently seeked station: " + fStation);
              
            }
       }
    }
    
    private void changeBackground(View v, int event, int idDown, int idUp, boolean bBackground) {
        if (event == MotionEvent.ACTION_DOWN){
            if (bBackground) {
                v.setBackgroundResource(idDown);
            }
            else {
                // Set image button source
                ((ImageButton)v).setImageResource(idDown);
            }
        }
        else if (event == MotionEvent.ACTION_UP){
            if (bBackground) {
                v.setBackgroundResource(idUp);
            }
            else {
                // Set image button source 
                ((ImageButton)v).setImageResource(idUp);
            }
        }
    }
    
    private void refreshButtonStatus() {
        mButtonDecrease.setEnabled(mbPlaying);
        mButtonPrevStation.setEnabled(mbPlaying);
        mButtonNextStation.setEnabled(mbPlaying);
        mButtonIncrease.setEnabled(mbPlaying);
        mButtonEarLoud.setEnabled(mbPlaying);
    }
    
    private void refreshButtonStatus(boolean enable){
        mButtonDecrease.setEnabled(enable);
        mButtonPrevStation.setEnabled(enable);
        mButtonNextStation.setEnabled(enable);
        mButtonIncrease.setEnabled(enable);
        mButtonEarLoud.setEnabled(enable);
        mButtonPlayStop.setEnabled(enable);
        mButtonFavorite.setEnabled(enable);
        mButtonAddToFavorite.setEnabled(enable);
    }
    
    protected void onSaveInstanceState(Bundle outState) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putBoolean(FM_SAVE_INSTANCE_STATE_INITED, true);
        outState.putBoolean(FM_SAVE_INSTANCE_STATE_PLAYING, mbPlaying);
        if (FeatureOption.MTK_FM_RECORDING_SUPPORT && mbIsRecording && 
                mDlgSaveRecording != null && mDlgSaveRecording.isShowing()) {
            outState.putBoolean(FM_INSTANCE_STATE_RECORDING_DLGSTATE, true);
        }
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onSaveInstanceState");
    }
    
    public void onBackPressed() {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onBackPressed");
        /*mbDestroying = true;
        // If searching, we should stop it and dismiss the progress dialog.
        if (mbSearching) {
            stopScan();
            mDialogSearchProgress.dismiss();
        }*/
        if (FeatureOption.MTK_FM_RECORDING_SUPPORT && mbIsRecording) {
            changeRecordingMode(false);
        } else {
            super.onBackPressed();
        }

        
/*        // When press "BACK" key, do not exit the program.
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onBackPressed");
    }
    
    private int rdsset(boolean rdson) {
        int iRet = -1;
        if (mbRDSEnabled) {
            // When turn off RDS, set RDS text view to blank.
            /*if (!rdson) {
                msPS = "";
                msLRText = "";
                // Update the RDS text view.
                Message msg = new Message();
                msg.setTarget(mHandler);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_MSGID, MSGID_UPDATE_RDS);
                msg.setData(bundle);
                msg.sendToTarget();
            }*/
            iRet = setRDS(rdson);
        }
        else {
            // Do nothing.
        }
        return iRet;
    }
    
    public void onConfigurationChanged(Configuration newConfig) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onConfigurationChanged");
    }

    private void showToast(CharSequence text) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.showToast: " + text);
        // Schedule a timer to clear the toast.
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // Clear the timer and toast.
                cancelToast();
            }
        }, TOAST_TIMER_DELAY, TOAST_TIMER_DELAY);
        
        // Toast it.
        mToast = Toast.makeText(FMRadioActivity.this, text, Toast.LENGTH_SHORT);
        mToast.show();
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.showToast");
    }

    private void cancelToast() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.cancelToast");
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        else {
            FMRadioLogUtils.d(TAG, "Warning: The timer is null.");
        }
        if (null != mToast) {
            mToast.cancel();
            mToast = null;
        }
        else {
            FMRadioLogUtils.d(TAG, "Warning: The toast is null.");
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.cancelToast");
    }

    private boolean isToasting() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isToasting");
        boolean bRet = true;
        if (null == mToast && null == mTimer) {
            bRet = false;
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isToasting: " + bRet);
        return bRet;
    }

    private void showRDS() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.showRDS");
        String text = msPS;
        if (msLRText.length() > 0) {
            if (text.length() > 0) {
                text += "  ";
            }
            text += msLRText;
        }
        mTextRDS.setText(text);
        mTextRDS.setSelected(true);
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.showRDS");
    }

    public void onLowMemory() {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onLowMemory");
        super.onLowMemory();
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onLowMemory");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onKeyDown: " + keyCode);
        boolean bRet = true;
        if (KeyEvent.KEYCODE_MENU == keyCode) {
            if (mbInited && !mbOnPowerUp) {
                bRet = super.onKeyDown(keyCode, event);
            }
            else {
                // The activity has not been inited yet, do not handle MENU key.
            }
        }
        else {
            bRet = super.onKeyDown(keyCode, event);
        }
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onKeyDown: " + bRet);
        return bRet;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onKeyUp: " + keyCode);
        boolean bRet = true;
        if (KeyEvent.KEYCODE_MENU == keyCode) {
            if (mbInited && !mbOnPowerUp) {
                bRet = super.onKeyUp(keyCode, event);
            }
            else {
                // The activity has not been inited yet, do not handle MENU key.
            }
        }
        else {
            bRet = super.onKeyUp(keyCode, event);
        }
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onKeyUp: " + bRet);
        return bRet;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        FMRadioLogUtils.d(TAG, ">>> FMRadioActivity.onKeyLongPress: " + keyCode);
        boolean bRet = super.onKeyLongPress(keyCode, event);
        FMRadioLogUtils.d(TAG, "<<< FMRadioActivity.onKeyLongPress: " + bRet);
        return bRet;
    }
    
    // Wrap service functions.
    private boolean openDevice() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.openDevice");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.openDevice();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.openDevice: " + bRet);
        return bRet;
    }

    private boolean closeDevice() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.closeDevice");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.closeDevice();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.closeDevice: " + bRet);
        return bRet;
    }
    
    private boolean isDeviceOpen() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isDeviceOpen");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isDeviceOpen();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isDeviceOpen: " + bRet);
        return bRet;
    }
    
    private boolean powerUp(float frequency) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.powerUp");
        boolean bRet = false;
        mbOnPowerUp = true;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.powerUp(frequency);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        mbOnPowerUp = false;
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.powerUp: " + bRet);
        return bRet;
    }
    
    private boolean powerDown() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.powerDown");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.powerDown();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.powerDown: " + bRet);
        return bRet;
    }
    
    private boolean isPowerUp() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isPowerUp");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isPowerUp();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isPowerUp: " + bRet);
        return bRet;
    }
    
    private boolean tune(float frequency) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.tune");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.tune(frequency);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.tune: " + bRet);
        return bRet;
    }
    
    private float seek(float frequency, boolean isUp) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.seek");
        float fRet = 0;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                fRet = mService.seek(frequency, isUp);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.seek: " + fRet);
        return fRet;
    }
    
    private int[] startScan() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.startScan");
        int[] iChannels = null;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iChannels = mService.startScan();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.startScan: " + iChannels);
        return iChannels;
    }
    
    private boolean stopScan() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.stopScan");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.stopScan();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.stopScan: " + bRet);
        return bRet;
    }
    
    private int setRDS(boolean on) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.setRDS");
        int iRet = -1;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.setRDS(on);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.setRDS: " + iRet);
        return iRet;
    }
    
    private int readRDS() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.readRDS");
        int iRet = 0;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.readRDS();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.readRDS: " + iRet);
        return iRet;
    }
    
    private String getPS() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.getPS");
        String sPS = null;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                sPS = mService.getPS();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.getPS: " + sPS);
        return sPS;
    }
    
    private String getLRText() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.getLRText");
        String sRT = null;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                sRT = mService.getLRText();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.getLRText: " + sRT);
        return sRT;
    }
    
    private int activeAF() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.activeAF");
        int iRet = 0;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.activeAF();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.activeAF: " + iRet);
        return iRet;
    }
    
    private int activeTA() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.activeTA");
        int iRet = 0;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.activeTA();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.activeTA: " + iRet);
        return iRet;
    }
    
    private int deactiveTA() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.deactiveTA");
        int iRet = 0;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.deactiveTA();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.deactiveTA: " + iRet);
        return iRet;
    }
    
    private int setMute(boolean mute) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.setMute");
        int iRet = -1;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.setMute(mute);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.setMute: " + iRet);
        return iRet;
    }
    
    private int isRDSSupported() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isRDSSupported");
        int iRet = -1;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.isRDSSupported();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isRDSSupported: " + iRet);
        return iRet;
    }

    private void useEarphone(boolean use) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.useEarphone: " + use);
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                mService.useEarphone(use);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.useEarphone");
    }
    
    private boolean isEarphoneUsed() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isEarphoneUsed");
        boolean bRet = true;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isEarphoneUsed();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isEarphoneUsed: " + bRet);
        return bRet;
    }
    
    private void initService(int iCurrentStation) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.initService: " + iCurrentStation);
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                mService.initService(iCurrentStation);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.initService");
    }
    
    private boolean isServiceInit() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isServiceInit");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isServiceInit();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isServiceInit: " + bRet);
        return bRet;
    }

    private void enablePSRT(boolean enable) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.enablePSRT: " + enable);
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                mService.enablePSRT(enable);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.enablePSRT");
    }
        
    private void enableAF(boolean enable) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.enableAF: " + enable);
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                mService.enableAF(enable);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.enableAF");
    }
    
    private void enableTA(boolean enable) {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.enableTA: " + enable);
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                mService.enableTA(enable);
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.enableTA");
    }
    
    private boolean isPSRTEnabled() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isPSRTEnabled");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isPSRTEnabled();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isPSRTEnabled: " + bRet);
        return bRet;
    }
    
    private boolean isAFEnabled() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isAFEnabled");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isAFEnabled();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isAFEnabled: " + bRet);
        return bRet;
    }
    
    private boolean isTAEnabled() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.isTAEnabled");
        boolean bRet = false;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                bRet = mService.isTAEnabled();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.isTAEnabled: " + bRet);
        return bRet;
    }

    private int getFrequency() {
        FMRadioLogUtils.v(TAG, ">>> FMRadioActivity.getFrequency");
        int iRet = 0;
        if (null == mService) {
            FMRadioLogUtils.e(TAG, "Error: No service interface.");
        }
        else {
            try {
                iRet = mService.getFrequency();
            }
            catch (Exception e) {
                FMRadioLogUtils.e(TAG, "Exception: Cannot call service function.");
            }
        }
        FMRadioLogUtils.v(TAG, "<<< FMRadioActivity.getFrequency: " + iRet);
        return iRet;
    }
    
    private void changeRecordingMode(boolean recording) {
        FMRadioLogUtils.d(TAG, "changeRecordingMode: " + recording);
        if (mbIsRecording == recording) {
            FMRadioLogUtils.e(TAG, "FM already " + (recording ? "in" : "NOT in") + "recording mode!");
            return;
        }
        mbIsRecording = recording;
        if (mbIsRecording) {
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
            iFilter.addDataScheme("file");
            registerReceiver(mSDListener, iFilter);
            mIsSDListenerRegistered = true;
        } else {
            if (mIsSDListenerRegistered) {
                unregisterReceiver(mSDListener);
                mIsSDListenerRegistered = false;
            }
        }
        try {
            mService.setRecordingMode(recording);
            LinearLayout top_bar = (LinearLayout)findViewById(R.id.top_bar);
            LinearLayout rec_bar = (LinearLayout)findViewById(R.id.bottom_bar_recorder);
            LinearLayout bottom_bar = (LinearLayout)findViewById(R.id.bottom_bar);
            top_bar.setVisibility(recording ? View.GONE : View.VISIBLE);
            bottom_bar.setVisibility(recording ? View.GONE : View.VISIBLE);
            rec_bar.setVisibility(recording ? View.VISIBLE : View.GONE);
            mButtonAddToFavorite.setVisibility(recording ? View.GONE : View.VISIBLE);
        } catch (Exception ex) {
            FMRadioLogUtils.d(TAG, "setRecordingMode: ", ex);
        }
        
    }
    
    private void refreshRecordingStatus(int stateOverride) {
        if (null == mService) {
            FMRadioLogUtils.w(TAG, "mService is null when refreshRecordingStatus()");
            return;
        }
        try {
            int recorderState = FMRecorder.STATE_INVALID;

            if (stateOverride == FMRecorder.STATE_INVALID) {
                recorderState = mService.getRecorderState();
            } else {
                recorderState = stateOverride;
            }
            FMRadioLogUtils.d(TAG, "refreshRecordingStatus: state=" + recorderState);
            switch (recorderState) {
            case FMRecorder.STATE_IDLE:
                String fileName = null;
                File recordingFolderPath = null;
                File recordingFileToSave = null;
                fileName = mService.getRecordingName();

                if (mService.getRecordTime() > 0) {
                    //if recording file is delete by user, play button disabled
                    if (null != fileName){
                        recordingFolderPath = new File(mSDDirectory, "FM Recording");
                        recordingFileToSave = new File(recordingFolderPath, fileName 
                                + FMRecorder.RECORDING_FILE_EXTENSION);
                        if (recordingFileToSave.exists()) {
                            mBtnPlayback.setEnabled(true);
                        }
                    }
                    if (mPrevRecorderState == FMRecorder.STATE_RECORDING) {
                        if (checkRemainingStorage()) {
                            mIsStorageWarning = false;
                            removeAndShowDialog(DLGID_SAVE_RECORDING);
                        } else {
                            mIsStorageWarning = true;
                            removeAndShowDialog(DLGID_SAVE_RECORDING);
                        }
                    }
                } else {
                    mBtnPlayback.setEnabled(false);
                }
                mBtnStop.setEnabled(false);
                mBtnRecord.setEnabled(true);
                mRLRecordInfo.setVisibility(View.GONE);
                break;
            case FMRecorder.STATE_RECORDING:
                mTxtRecInfoLeft.setText("");
                mTxtRecInfoRight.setText("");
                mTxtRecInfoLeft.setSelected(false);
                mBtnStop.setEnabled(true);
                mBtnRecord.setEnabled(false);
                mBtnPlayback.setEnabled(false);
                mRLRecordInfo.setVisibility(View.VISIBLE);
                break;
            case FMRecorder.STATE_PLAYBACK:
                String recordingName = mService.getRecordingName();
                if (recordingName == null)
                    recordingName = "";
                mTxtRecInfoLeft.setText(recordingName);
                mTxtRecInfoRight.setText("");
                mTxtRecInfoLeft.setSelected(true);
                mBtnStop.setEnabled(true);
                mBtnRecord.setEnabled(false);
                mBtnPlayback.setEnabled(false);
                mRLRecordInfo.setVisibility(View.VISIBLE);
                break;
            
            case FMRecorder.STATE_INVALID:
                // FMRecorder is not started yet
                mBtnStop.setEnabled(false);
                mBtnRecord.setEnabled(true);
                mBtnPlayback.setEnabled(false);
                mRLRecordInfo.setVisibility(View.GONE);
                break;

            }
            mPrevRecorderState = recorderState;
        } catch (Exception ex) {
            FMRadioLogUtils.e(TAG, "refreshRecordingStatus: ", ex);
        }
    }
    

    
    private boolean checkRemainingStorage() {
        StatFs fs = new StatFs(mSDDirectory);
        long blocks = fs.getAvailableBlocks();
        long blockSize = fs.getBlockSize();
        long spaceLeft = blocks * blockSize;
        FMRadioLogUtils.d(TAG, "checkRemainingStorage: available space=" + spaceLeft);
        
        return (spaceLeft > FMRecorder.LOW_SPACE_THRESHOLD ? true : false);
    }
    //we use onRetainNonConfigurationInstance because after configuration change, activity will destroy and create
    //need use this function to save some important variables
    @Override
    public Object onRetainNonConfigurationInstance() {
        Bundle bundle = new Bundle();
        boolean isInRecordingMode = false;
        try {
            isInRecordingMode = mService.getRecordingMode();
  
        } catch (Exception e) {
            e.printStackTrace();
        }
        bundle.putBoolean("isInRecordingMode", isInRecordingMode);
        bundle.putInt("mPrevRecorderState", mPrevRecorderState);
        bundle.putBoolean("mIsFreshRecordingStatus", mIsFreshRecordingStatus);
        bundle.putInt("mRecordState", mRecordState);
        return bundle;
    }
    
}
