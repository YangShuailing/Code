/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\GitHub\\LeetCode\\FMRadio\\app\\src\\main\\aidl\\com\\mediatek\\FMRadio\\IFMRadioService.aidl
 */
package com.mediatek.FMRadio;
public interface IFMRadioService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.mediatek.FMRadio.IFMRadioService
{
private static final java.lang.String DESCRIPTOR = "com.mediatek.FMRadio.IFMRadioService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.mediatek.FMRadio.IFMRadioService interface,
 * generating a proxy if needed.
 */
public static com.mediatek.FMRadio.IFMRadioService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.mediatek.FMRadio.IFMRadioService))) {
return ((com.mediatek.FMRadio.IFMRadioService)iin);
}
return new com.mediatek.FMRadio.IFMRadioService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_openDevice:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.openDevice();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_closeDevice:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.closeDevice();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isDeviceOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isDeviceOpen();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_powerUp:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
boolean _result = this.powerUp(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_powerDown:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.powerDown();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isPowerUp:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPowerUp();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_tune:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
boolean _result = this.tune(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_seek:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
boolean _arg1;
_arg1 = (0!=data.readInt());
float _result = this.seek(_arg0, _arg1);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_startScan:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.startScan();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_stopScan:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.stopScan();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setRDS:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
int _result = this.setRDS(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_readRDS:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.readRDS();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getPS:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getPS();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getLRText:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getLRText();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_activeAF:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.activeAF();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_activeTA:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.activeTA();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_deactiveTA:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.deactiveTA();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setMute:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
int _result = this.setMute(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isRDSSupported:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.isRDSSupported();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_useEarphone:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.useEarphone(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isEarphoneUsed:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isEarphoneUsed();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_initService:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.initService(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isServiceInit:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isServiceInit();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_enablePSRT:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.enablePSRT(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_enableAF:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.enableAF(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_enableTA:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.enableTA(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isPSRTEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPSRTEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isAFEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAFEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isTAEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isTAEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getFrequency:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getFrequency();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_resumeFMAudio:
{
data.enforceInterface(DESCRIPTOR);
this.resumeFMAudio();
reply.writeNoException();
return true;
}
case TRANSACTION_readRssi:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.readRssi();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_readCapArray:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.readCapArray();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getChipId:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getChipId();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getStereoMono:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getStereoMono();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setStereoMono:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.setStereoMono(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_switchAntenna:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.switchAntenna(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_readRdsBler:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.readRdsBler();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_startRecording:
{
data.enforceInterface(DESCRIPTOR);
this.startRecording();
reply.writeNoException();
return true;
}
case TRANSACTION_stopRecording:
{
data.enforceInterface(DESCRIPTOR);
this.stopRecording();
reply.writeNoException();
return true;
}
case TRANSACTION_saveRecording:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.saveRecording(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_startPlayback:
{
data.enforceInterface(DESCRIPTOR);
this.startPlayback();
reply.writeNoException();
return true;
}
case TRANSACTION_stopPlayback:
{
data.enforceInterface(DESCRIPTOR);
this.stopPlayback();
reply.writeNoException();
return true;
}
case TRANSACTION_getRecorderState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getRecorderState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getRecordTime:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getRecordTime();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_setRecordingMode:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setRecordingMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getRecordingMode:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getRecordingMode();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getPlaybackPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPlaybackPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getRecordingName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getRecordingName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isFMOverBTActive:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isFMOverBTActive();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getResumeAfterCall:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getResumeAfterCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isSIMCardIdle:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isSIMCardIdle();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.mediatek.FMRadio.IFMRadioService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean openDevice() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openDevice, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean closeDevice() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_closeDevice, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isDeviceOpen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isDeviceOpen, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean powerUp(float frequency) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(frequency);
mRemote.transact(Stub.TRANSACTION_powerUp, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean powerDown() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_powerDown, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isPowerUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPowerUp, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean tune(float frequency) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(frequency);
mRemote.transact(Stub.TRANSACTION_tune, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public float seek(float frequency, boolean isUp) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(frequency);
_data.writeInt(((isUp)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_seek, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int[] startScan() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startScan, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean stopScan() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopScan, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setRDS(boolean on) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((on)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setRDS, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int readRDS() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_readRDS, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getPS() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPS, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getLRText() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLRText, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int activeAF() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_activeAF, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int activeTA() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_activeTA, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int deactiveTA() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_deactiveTA, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setMute(boolean mute) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((mute)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setMute, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int isRDSSupported() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isRDSSupported, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void useEarphone(boolean use) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((use)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_useEarphone, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isEarphoneUsed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isEarphoneUsed, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void initService(int iCurrentStation) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(iCurrentStation);
mRemote.transact(Stub.TRANSACTION_initService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isServiceInit() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isServiceInit, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void enablePSRT(boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_enablePSRT, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void enableAF(boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_enableAF, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void enableTA(boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_enableTA, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isPSRTEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPSRTEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAFEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAFEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isTAEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isTAEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getFrequency() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFrequency, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void resumeFMAudio() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_resumeFMAudio, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int readRssi() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_readRssi, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int readCapArray() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_readCapArray, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getChipId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getChipId, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean getStereoMono() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getStereoMono, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean setStereoMono(boolean isMono) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isMono)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setStereoMono, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int switchAntenna(int antenna) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(antenna);
mRemote.transact(Stub.TRANSACTION_switchAntenna, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int readRdsBler() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_readRdsBler, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startRecording() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startRecording, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopRecording() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopRecording, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void saveRecording(java.lang.String newName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(newName);
mRemote.transact(Stub.TRANSACTION_saveRecording, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startPlayback() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startPlayback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopPlayback() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopPlayback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getRecorderState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRecorderState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public long getRecordTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRecordTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setRecordingMode(boolean isRecording) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isRecording)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setRecordingMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean getRecordingMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRecordingMode, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getPlaybackPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlaybackPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getRecordingName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRecordingName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isFMOverBTActive() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isFMOverBTActive, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean getResumeAfterCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getResumeAfterCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isSIMCardIdle() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSIMCardIdle, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_openDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_closeDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isDeviceOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_powerUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_powerDown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_isPowerUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_tune = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_seek = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_startScan = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_stopScan = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_setRDS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_readRDS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getPS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getLRText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_activeAF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_activeTA = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_deactiveTA = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_setMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_isRDSSupported = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_useEarphone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_isEarphoneUsed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_initService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_isServiceInit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_enablePSRT = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_enableAF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_enableTA = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_isPSRTEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_isAFEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_isTAEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_getFrequency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_resumeFMAudio = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_readRssi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_readCapArray = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_getChipId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_getStereoMono = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_setStereoMono = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_switchAntenna = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_readRdsBler = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_startRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_stopRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_saveRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_startPlayback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_stopPlayback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_getRecorderState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_getRecordTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_setRecordingMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_getRecordingMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
static final int TRANSACTION_getPlaybackPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
static final int TRANSACTION_getRecordingName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
static final int TRANSACTION_isFMOverBTActive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
static final int TRANSACTION_getResumeAfterCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
static final int TRANSACTION_isSIMCardIdle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
}
public boolean openDevice() throws android.os.RemoteException;
public boolean closeDevice() throws android.os.RemoteException;
public boolean isDeviceOpen() throws android.os.RemoteException;
public boolean powerUp(float frequency) throws android.os.RemoteException;
public boolean powerDown() throws android.os.RemoteException;
public boolean isPowerUp() throws android.os.RemoteException;
public boolean tune(float frequency) throws android.os.RemoteException;
public float seek(float frequency, boolean isUp) throws android.os.RemoteException;
public int[] startScan() throws android.os.RemoteException;
public boolean stopScan() throws android.os.RemoteException;
public int setRDS(boolean on) throws android.os.RemoteException;
public int readRDS() throws android.os.RemoteException;
public java.lang.String getPS() throws android.os.RemoteException;
public java.lang.String getLRText() throws android.os.RemoteException;
public int activeAF() throws android.os.RemoteException;
public int activeTA() throws android.os.RemoteException;
public int deactiveTA() throws android.os.RemoteException;
public int setMute(boolean mute) throws android.os.RemoteException;
public int isRDSSupported() throws android.os.RemoteException;
public void useEarphone(boolean use) throws android.os.RemoteException;
public boolean isEarphoneUsed() throws android.os.RemoteException;
public void initService(int iCurrentStation) throws android.os.RemoteException;
public boolean isServiceInit() throws android.os.RemoteException;
public void enablePSRT(boolean enable) throws android.os.RemoteException;
public void enableAF(boolean enable) throws android.os.RemoteException;
public void enableTA(boolean enable) throws android.os.RemoteException;
public boolean isPSRTEnabled() throws android.os.RemoteException;
public boolean isAFEnabled() throws android.os.RemoteException;
public boolean isTAEnabled() throws android.os.RemoteException;
public int getFrequency() throws android.os.RemoteException;
public void resumeFMAudio() throws android.os.RemoteException;
public int readRssi() throws android.os.RemoteException;
public int readCapArray() throws android.os.RemoteException;
public int getChipId() throws android.os.RemoteException;
public boolean getStereoMono() throws android.os.RemoteException;
public boolean setStereoMono(boolean isMono) throws android.os.RemoteException;
public int switchAntenna(int antenna) throws android.os.RemoteException;
public int readRdsBler() throws android.os.RemoteException;
public void startRecording() throws android.os.RemoteException;
public void stopRecording() throws android.os.RemoteException;
public void saveRecording(java.lang.String newName) throws android.os.RemoteException;
public void startPlayback() throws android.os.RemoteException;
public void stopPlayback() throws android.os.RemoteException;
public int getRecorderState() throws android.os.RemoteException;
public long getRecordTime() throws android.os.RemoteException;
public void setRecordingMode(boolean isRecording) throws android.os.RemoteException;
public boolean getRecordingMode() throws android.os.RemoteException;
public int getPlaybackPosition() throws android.os.RemoteException;
public java.lang.String getRecordingName() throws android.os.RemoteException;
public boolean isFMOverBTActive() throws android.os.RemoteException;
public boolean getResumeAfterCall() throws android.os.RemoteException;
public boolean isSIMCardIdle() throws android.os.RemoteException;
}
