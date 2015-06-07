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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.content.res.Configuration;

public class FMRadioFavorite extends Activity {
	public static final String TAG = "FMRadioAPK";
	public static final String ACTIVITY_RESULT = "ACTIVITY_RESULT";
	
	private final String LV_COLUMN_STATION_TYPE = "STATION_TYPE";
	private final String LV_COLUMN_STATION_NAME = "STATION_NAME";
	private final String LV_COLUMN_STATION_FREQ = "STATION_FREQ";
	private final String LV_COLUMN_VALUE_FREQ = "STATION_FREQ_VALUE"; // Save the frequency value into hash map.
	private final String FM_SAVE_INSTANCE_STATE_FAVORITE_NAME = "FM_SAVE_INSTANCE_STATE_FAVORITE_NAME";
	private final String FM_SAVE_INSTANCE_STATE_FAVORITE_FREQ = "FM_SAVE_INSTANCE_STATE_FAVORITE_FREQ";
	private final int LV_CAPACITY = 1024;
	private final int CONTMENU_ID_EDIT = 1;
	private final int CONTMENU_ID_DELETE = 2;
	private final int CONTMENU_ID_ADD = 3;
	
	// The max count of characters limited by edit text in add_edit_dlg.
	private final int MAX_STATION_NAME_LENGTH = 20;
	
	private ListView mlvFavorites = null;
	private SimpleAdapter msimpleAdapter = null;
	private ArrayList<HashMap<String, Object>> mlistStations = null;
	private int miStationCount = 0; // Record how many stations displayed in list view.
	private int DLGID_ADD_EDIT_STATION = 1;
	private int DLGID_DELETE_CONFIRM = 2;
	
	// Dialog to edit or add station.
	private int miPosition = 0; // Record the long clicked station position in the list view.
	private String msDlgStationName = null; // Record the long clicked station name.
	private int miDlgStationFreq = 0; // Record the long clicked station frequency.
	private int miStationType = 0; // Record the long clicked station type.
	private int miStationOperation = 0; // Record the long clicked station operation.
	private AlertDialog mDialogAddEdit = null;
	
	public void onCreate(Bundle savedInstanceState) {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onCreate");
        super.onCreate(savedInstanceState);
        // Bind the activity to FM audio stream.
        setVolumeControlStream(AudioManager.STREAM_FM);
        setContentView(R.layout.favorite);
        
        //Get saved instance state
        if(savedInstanceState != null)
        {
			msDlgStationName = savedInstanceState.getString(FM_SAVE_INSTANCE_STATE_FAVORITE_NAME);
			miDlgStationFreq = savedInstanceState.getInt(FM_SAVE_INSTANCE_STATE_FAVORITE_FREQ);
        }
        
        mlistStations = new ArrayList<HashMap<String, Object>>(LV_CAPACITY);
        msimpleAdapter = new SimpleAdapter(
        	this,
        	mlistStations,
        	R.layout.simpleadapter,
        	new String[]{LV_COLUMN_STATION_TYPE, LV_COLUMN_STATION_FREQ, LV_COLUMN_STATION_NAME},
        	new int[]{R.id.lv_station_type, R.id.lv_station_freq, R.id.lv_station_name}
        );
        msimpleAdapter.setViewBinder(
        	new SimpleAdapter.ViewBinder() {
				
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					if (view instanceof ImageView) {
						((ImageView)view).setBackgroundResource(0);
						// If the station is a favorite station, set its icon;
						// otherwise, it does not have an icon.
						if (FMRadioStation.STATION_TYPE_FAVORITE == (Integer)data) {
							((ImageView)view).setImageResource(com.android.internal.R.drawable.btn_star_big_on);
						}
						else {
							((ImageView)view).setImageResource(0);
						}
						
						return true;
					}
					return false;
				}
			}
        );
        mlvFavorites = (ListView)findViewById(R.id.station_list);
        mlvFavorites.setAdapter(msimpleAdapter);
        initListView();
        mlvFavorites.setOnItemClickListener(
        	new AdapterView.OnItemClickListener() {
        		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        			// Set the selected frequency to main UI and finish the favorite manager.
        			HashMap<String, Object> hashmap = mlistStations.get(position);
        			int iStation = (Integer)hashmap.get(LV_COLUMN_VALUE_FREQ);
        			Intent intentResult = new Intent();
        			intentResult.putExtra(ACTIVITY_RESULT, iStation);
        			setResult(RESULT_OK, intentResult);
        			finish();
        		}
        	}
        );
        mlvFavorites.setOnCreateContextMenuListener(
        	new View.OnCreateContextMenuListener() {
        		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        			menu.setHeaderTitle(R.string.contmenu_title);
        			HashMap<String, Object> hashmap = mlistStations.get(((AdapterView.AdapterContextMenuInfo)menuInfo).position);
        			int iType = (Integer)hashmap.get(LV_COLUMN_STATION_TYPE);
        			if (FMRadioStation.STATION_TYPE_SEARCHED == iType) {
        				// Searched station.
        				menu.add(0, CONTMENU_ID_ADD, 0, R.string.add_to_favorite);
        			}
        			else {
        				// Favorite station.
        				menu.add(0, CONTMENU_ID_EDIT, 0, R.string.contmenu_item_edit);
            			menu.add(0, CONTMENU_ID_DELETE, 0, R.string.contmenu_item_delete);
        			}
        		}
        	}
        );
        
        FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onCreate");
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		miPosition = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
		HashMap<String, Object> hashmap = mlistStations.get(miPosition);
		msDlgStationName = (String)hashmap.get(LV_COLUMN_STATION_NAME);
		miDlgStationFreq = (Integer)hashmap.get(LV_COLUMN_VALUE_FREQ);
		miStationType = (Integer)hashmap.get(LV_COLUMN_STATION_TYPE);
		miStationOperation = item.getItemId();
		switch (miStationOperation) {
			case CONTMENU_ID_ADD: 
			case CONTMENU_ID_EDIT: {
				if (CONTMENU_ID_ADD == miStationOperation) {
					if (FMRadioStation.getStationCount(this, FMRadioStation.STATION_TYPE_FAVORITE) >= FMRadioStation.MAX_FAVORITE_STATION_COUNT) {
						// Favorite list is full. Toast it.
						Toast.makeText(this, R.string.toast_favorite_full, Toast.LENGTH_SHORT).show();
						break;
					}
					else if (FMRadioStation.isStationExist(this, miDlgStationFreq, FMRadioStation.STATION_TYPE_FAVORITE)) {
						// The station is already in favorite list.
						Toast.makeText(this, R.string.toast_already_favorite, Toast.LENGTH_SHORT).show();
						break;
					}
				}
				
				// Pop up a dialog to edit the new station.
				//Do not call showDialog because the dialog shown by showDialog will be restored after config changed.
				//showDialog(DLGID_ADD_EDIT_STATION);
				View v = View.inflate(this, R.layout.addeditstation, null);
				EditText editTextStationName = (EditText)v.findViewById(R.id.dlg_addedit_station_name_text);
				editTextStationName.setFilters(
					new InputFilter[]{new InputFilter.LengthFilter(MAX_STATION_NAME_LENGTH)}
				);
				mDialogAddEdit = new AlertDialog.Builder(this)
					// Must call setTitle here or the title will not be displayed.
					.setTitle(R.string.app_name)
					.setView(v)
					.setPositiveButton(R.string.btn_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								msDlgStationName = ((TextView)mDialogAddEdit.findViewById(R.id.dlg_addedit_station_name_text)).getText().toString();
								if (0 == msDlgStationName.length()) {
									msDlgStationName = getString(R.string.default_station_name);
								}
								if (CONTMENU_ID_ADD == miStationOperation) {
									// Insert the new station to data base and display it in list view.
									FMRadioStation.insertStationToDB(
										FMRadioFavorite.this, msDlgStationName,
										miDlgStationFreq, FMRadioStation.STATION_TYPE_FAVORITE
									);
									
									HashMap<String, Object> hashmap = new HashMap<String, Object>();
									hashmap.put(LV_COLUMN_STATION_TYPE, FMRadioStation.STATION_TYPE_FAVORITE);
							        hashmap.put(LV_COLUMN_STATION_FREQ, String.valueOf((float)miDlgStationFreq / 10));
							        hashmap.put(LV_COLUMN_STATION_NAME, msDlgStationName);
							        hashmap.put(LV_COLUMN_VALUE_FREQ, miDlgStationFreq);
							        // Sort the favorite stations.
							        mlistStations.add(getInsertIndex(miDlgStationFreq), hashmap);
							        miStationCount++;
							        msimpleAdapter.notifyDataSetChanged();
								}
								else {
									// Update the record in data base.
									FMRadioStation.updateStationToDB(
										FMRadioFavorite.this, msDlgStationName, miDlgStationFreq, FMRadioStation.STATION_TYPE_FAVORITE
									);
									
									// Update the station displayed in list view.
									HashMap<String, Object> hashmap = null;
									int location = 0;
									do {
										if (location >= miStationCount) {
											// Must break here, or mlistStations.get(location) will cause an exception.
											// Theoretically, this is unreachable code because we can find the station.
											break;
										}
										
										hashmap = mlistStations.get(location);
										if (null == hashmap) {
											break;
										}
										else {
											location++;
											if (miDlgStationFreq == (Integer)hashmap.get(LV_COLUMN_VALUE_FREQ)) {
												// This is the station displayed in list view.
												hashmap.put(LV_COLUMN_STATION_NAME, msDlgStationName);
												msimpleAdapter.notifyDataSetChanged();
												break;
											}
											else {
												continue;
											}
										}
									} while (true);
								}
								
								mDialogAddEdit.cancel();
							}
					    }
					)
					.setNegativeButton(R.string.btn_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								mDialogAddEdit.cancel();
							}
						}
					)
					.create();
				mDialogAddEdit.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				((TextView)v.findViewById(R.id.dlg_addedit_station_freq_text))
					.setText(String.valueOf((float)miDlgStationFreq / 10));
				((TextView)v.findViewById(R.id.dlg_addedit_station_name_text))
					.setHint(R.string.default_station_name);
				editTextStationName.requestFocus();
				editTextStationName.requestFocusFromTouch();
				if (CONTMENU_ID_ADD == miStationOperation) {
					mDialogAddEdit.setTitle(R.string.add_to_favorite);
					((TextView)v.findViewById(R.id.dlg_addedit_station_name_text)).setText("");
				}
				else {
					// Edit
					mDialogAddEdit.setTitle(R.string.dlg_addedit_title_edit);
					((TextView)v.findViewById(R.id.dlg_addedit_station_name_text)).setText(
						msDlgStationName
					);
					Editable text = editTextStationName.getText();
					int index = text.length();
					Selection.setSelection(text, index);
				}
				mDialogAddEdit.show();
				break;
			}
			case CONTMENU_ID_DELETE: {
				//Do not call showDialog because the dialog shown by showDialog will be restored after config changed.
				//showDialog(DLGID_DELETE_CONFIRM);
				AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.dlg_delete_confirm_title)
					.setMessage(R.string.dlg_delete_confirm_text)
					.setPositiveButton(R.string.btn_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Delete the station in data base.
								HashMap<String, Object> hashmap = mlistStations.get(miPosition);
								FMRadioStation.deleteStationInDB(
									FMRadioFavorite.this,
									(Integer)hashmap.get(LV_COLUMN_VALUE_FREQ),
									miStationType
								);
								
								// Delete the station in list view.
								mlistStations.remove(miPosition);
								miStationCount--;
								msimpleAdapter.notifyDataSetChanged();
							}
						}
					)
					.setNegativeButton(R.string.btn_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}
					)
					.create();
				dialog.show();
				break;
			}
			default: {
				FMRadioLogUtils.e(TAG, "Error: Invalid menu item.");
				break;
			}
		}
		return false;
	}
	
	protected Dialog onCreateDialog(int id) {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onCreateDialog");
		Dialog dlgRet = null;
		if (DLGID_ADD_EDIT_STATION == id) {
			View v = View.inflate(this, R.layout.addeditstation, null);
			EditText editTextStationName = (EditText)v.findViewById(R.id.dlg_addedit_station_name_text);
			editTextStationName.setFilters(
				new InputFilter[]{new InputFilter.LengthFilter(MAX_STATION_NAME_LENGTH)}
			);
			mDialogAddEdit = new AlertDialog.Builder(this)
			// Must call setTitle here or the title will not be displayed.
			.setTitle(R.string.app_name)
			.setView(v)
			.setPositiveButton(R.string.btn_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						msDlgStationName = ((TextView)mDialogAddEdit.findViewById(R.id.dlg_addedit_station_name_text)).getText().toString();
						if (0 == msDlgStationName.length()) {
							msDlgStationName = getString(R.string.default_station_name);
						}
						if (CONTMENU_ID_ADD == miStationOperation) {
							// Insert the new station to data base and display it in list view.
							FMRadioStation.insertStationToDB(
								FMRadioFavorite.this, msDlgStationName,
								miDlgStationFreq, FMRadioStation.STATION_TYPE_FAVORITE
							);
							
							HashMap<String, Object> hashmap = new HashMap<String, Object>();
							hashmap.put(LV_COLUMN_STATION_TYPE, FMRadioStation.STATION_TYPE_FAVORITE);
					        hashmap.put(LV_COLUMN_STATION_FREQ, String.valueOf((float)miDlgStationFreq / 10));
					        hashmap.put(LV_COLUMN_STATION_NAME, msDlgStationName);
					        hashmap.put(LV_COLUMN_VALUE_FREQ, miDlgStationFreq);
					        // Sort the favorite stations.
					        mlistStations.add(getInsertIndex(miDlgStationFreq), hashmap);
					        miStationCount++;
					        msimpleAdapter.notifyDataSetChanged();
						}
						else {
							// Update the record in data base.
							FMRadioStation.updateStationToDB(
								FMRadioFavorite.this, msDlgStationName, miDlgStationFreq, FMRadioStation.STATION_TYPE_FAVORITE
							);
							
							// Update the station displayed in list view.
							HashMap<String, Object> hashmap = null;
							int location = 0;
							do {
								if (location >= miStationCount) {
									// Must break here, or mlistStations.get(location) will cause an exception.
									// Theoretically, this is unreachable code because we can find the station.
									break;
								}
								
								hashmap = mlistStations.get(location);
								if (null == hashmap) {
									break;
								}
								else {
									location++;
									if (miDlgStationFreq == (Integer)hashmap.get(LV_COLUMN_VALUE_FREQ)) {
										// This is the station displayed in list view.
										hashmap.put(LV_COLUMN_STATION_NAME, msDlgStationName);
										msimpleAdapter.notifyDataSetChanged();
										break;
									}
									else {
										continue;
									}
								}
							} while (true);
						}
						
						mDialogAddEdit.cancel();
					}
			    }
			)
			.setNegativeButton(R.string.btn_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mDialogAddEdit.cancel();
					}
				}
			)
			.create();
			dlgRet = mDialogAddEdit;
			mDialogAddEdit.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
		else if (DLGID_DELETE_CONFIRM == id) {
			dlgRet = new AlertDialog.Builder(this)
				.setTitle(R.string.dlg_delete_confirm_title)
				.setMessage(R.string.dlg_delete_confirm_text)
				.setPositiveButton(R.string.btn_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Delete the station in data base.
							HashMap<String, Object> hashmap = mlistStations.get(miPosition);
							FMRadioStation.deleteStationInDB(
								FMRadioFavorite.this,
								(Integer)hashmap.get(LV_COLUMN_VALUE_FREQ),
								miStationType
							);
							
							// Delete the station in list view.
							mlistStations.remove(miPosition);
							miStationCount--;
							msimpleAdapter.notifyDataSetChanged();
						}
					}
				)
				.setNegativeButton(R.string.btn_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}
				)
				.create();
		}
		else {
			FMRadioLogUtils.e(TAG, "Error: Invalid dialog id.");
		}
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onCreateDialog");
		return dlgRet;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onPrepareDialog");
		if (DLGID_ADD_EDIT_STATION == id) {
			((TextView)dialog.findViewById(R.id.dlg_addedit_station_freq_text)).setText(
				String.valueOf((float)miDlgStationFreq / 10)
			);
			((TextView)dialog.findViewById(R.id.dlg_addedit_station_name_text)).setHint(
					R.string.default_station_name
				);
			EditText editTextStationName = (EditText)dialog.findViewById(R.id.dlg_addedit_station_name_text);
			editTextStationName.requestFocus();
			editTextStationName.requestFocusFromTouch();
			if (CONTMENU_ID_ADD == miStationOperation) {
				dialog.setTitle(R.string.add_to_favorite);
				((TextView)dialog.findViewById(R.id.dlg_addedit_station_name_text)).setText("");
			}
			else {
				// Edit
				dialog.setTitle(R.string.dlg_addedit_title_edit);
				((TextView)dialog.findViewById(R.id.dlg_addedit_station_name_text)).setText(
					msDlgStationName
				);
			  Editable text = editTextStationName.getText();
			  int index = text.length();
			  Selection.setSelection(text, index);
			}
			
		}
		else {
			FMRadioLogUtils.e(TAG, "Error: Invalid dialog id.");
		}
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onPrepareDialog");
	}
	
	private void initListView() {
        HashMap<String, Object> hashmap = null;
        // Display all the stations in the data base.
		Uri uri = FMRadioStation.Station.CONTENT_URI;
		Cursor cur = managedQuery(uri, FMRadioStation.columns, null, null, null);
		if (null != cur) {
			// Add favorite stations into list.
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				int iType = cur.getInt(cur.getColumnIndex(FMRadioStation.Station.COLUMN_STATION_TYPE));
				if (FMRadioStation.STATION_TYPE_FAVORITE == iType) {
					String sStationName = cur.getString(cur.getColumnIndex(FMRadioStation.Station.COLUMN_STATION_NAME));
					int iStation = cur.getInt(cur.getColumnIndex(FMRadioStation.Station.COLUMN_STATION_FREQ));
					String sStationFreq = String.valueOf((float)iStation / 10);
					hashmap = new HashMap<String, Object>();
					hashmap.put(LV_COLUMN_STATION_TYPE, iType);
			        hashmap.put(LV_COLUMN_STATION_FREQ, sStationFreq);
			        hashmap.put(LV_COLUMN_STATION_NAME, sStationName);
			        hashmap.put(LV_COLUMN_VALUE_FREQ, iStation);
			        mlistStations.add(getInsertIndex(iStation), hashmap);
			        miStationCount++;
				}
				
				cur.moveToNext();
			}
			
			// Add searched stations into list.
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				int iType = cur.getInt(cur.getColumnIndex(FMRadioStation.Station.COLUMN_STATION_TYPE));
				if (FMRadioStation.STATION_TYPE_SEARCHED == iType) {
					String sStationName = cur.getString(cur.getColumnIndex(FMRadioStation.Station.COLUMN_STATION_NAME));
					int iStation = cur.getInt(cur.getColumnIndex(FMRadioStation.Station.COLUMN_STATION_FREQ));
					String sStationFreq = String.valueOf((float)iStation / 10);
					hashmap = new HashMap<String, Object>();
					hashmap.put(LV_COLUMN_STATION_TYPE, iType);
			        hashmap.put(LV_COLUMN_STATION_FREQ, sStationFreq);
			        hashmap.put(LV_COLUMN_STATION_NAME, sStationName);
			        hashmap.put(LV_COLUMN_VALUE_FREQ, iStation);
			        mlistStations.add(hashmap);
			        miStationCount++;
				}
				
				cur.moveToNext();
			}
		}
        
        msimpleAdapter.notifyDataSetChanged();
	}
	
	private int getInsertIndex(int stationFreq) {
		int iRet = 0;
		int iFavoriteCount = mlistStations.size();
		HashMap<String, Object> hashmap = null;
		for (; iRet < iFavoriteCount; iRet++) {
			hashmap = mlistStations.get(iRet);
			if (null == hashmap) {
				break;
			}
			if (FMRadioStation.STATION_TYPE_FAVORITE != (Integer)hashmap.get(LV_COLUMN_STATION_TYPE)
				|| (Integer)hashmap.get(LV_COLUMN_VALUE_FREQ) >= stationFreq) {
				break;
			}
		}
		return iRet;
	}
	
	//Save instance state when being destroyed
	protected void onSaveInstanceState(Bundle outState) {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onSaveInstanceState");
		super.onSaveInstanceState(outState);
		outState.putString(FM_SAVE_INSTANCE_STATE_FAVORITE_NAME, msDlgStationName);
		outState.putInt(FM_SAVE_INSTANCE_STATE_FAVORITE_FREQ, miDlgStationFreq);
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onSaveInstanceState");
	}

	public void onStart() {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onStart");
    	super.onStart();
    	FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onStart");
    }

	public void onResume() {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onResume");
		super.onResume();
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onResume");
	}

	public void onPause() {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onPause");
		super.onPause();
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onPause");
	}

	public void onStop() {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onStop");
		super.onStop();
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onStop");
	}

	public void onDestroy() {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onDestroy");
		super.onDestroy();
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onDestroy");
	}

	public void onConfigurationChanged(Configuration newConfig) {
		FMRadioLogUtils.d(TAG, ">>> FMRadioFavorite.onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
		FMRadioLogUtils.d(TAG, "<<< FMRadioFavorite.onConfigurationChanged");
	}

}
