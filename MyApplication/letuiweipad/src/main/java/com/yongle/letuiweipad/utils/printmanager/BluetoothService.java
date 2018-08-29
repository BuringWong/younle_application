/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yongle.letuiweipad.utils.printmanager;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {
    // Debugging
    private static final String TAG = "BluetoothService";
    private static final boolean D = true;

    // Name for the SDP record when creating server socket
    private static final String NAME = "BTPrinter";

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");	//change by chongqing jinou
    // Member fields
    private final BluetoothAdapter mAdapter;
    private Handler mHandler;
    private  int mState;

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

//    public static final String[] items = { "复位打印机", "标准ASCII字体", "压缩ASCII字体", "字体大小:00","字体大小:02","字体大小:06",   
//            "字体大小:11", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",    
//            "取消顺时针旋转90°", "选择顺时针旋转90°","左对齐","居中","右对齐" };    
    public static final byte[][] byteCommands = { { 0x1b, 0x40 },// 复位打印机    
            { 0x1b, 0x4d, 0x00 },// 标准ASCII字体    
            { 0x1b, 0x4d, 0x01 },// 压缩ASCII字体    
            { 0x1d, 0x21, 0x00 },// 字体不放大    
            { 0x1d, 0x21, 0x02 },// 宽高加倍    
            { 0x1d, 0x21, 0x11 },// 宽高加倍    
//            { 0x1d, 0x21, 0x11 },// 宽高加倍    
            { 0x1b, 0x45, 0x00 },// 取消加粗模式
            { 0x1b, 0x45, 0x01 },// 选择加粗模式
            { 0x1b, 0x7b, 0x00 },// 取消倒置打印
            { 0x1b, 0x7b, 0x01 },// 选择倒置打印
            { 0x1d, 0x42, 0x00 },// 取消黑白反显
            { 0x1d, 0x42, 0x01 },// 选择黑白反显
            { 0x1b, 0x56, 0x00 },// 取消顺时针旋转90°
            { 0x1b, 0x56, 0x01 },// 选择顺时针旋转90°
            
            { 0x1b, 0x61, 0x48 },// 左对齐
            { 0x1b, 0x61, 0x49 },// 居中对齐
            { 0x1b, 0x61, 0x50 },// 右对齐
//            { 0x1b, 0x69 },// 切纸
    };

    public static final byte[] FORMAT_HT = {0X09};
    public static final byte[] FORMAT_LEFT = {0x1b, 0x61, 0x48};
    public static final byte[] FORMAT_CENTER = {0x1b, 0x61, 0x49};
    public static final byte[] FORMAT_RIGHT = {0x1b, 0x61, 0x50};
    public static final byte[] CHAR_FONT_NORMAL = {0x1B, 0x21, 0x00};
    public static final byte[] CHAR_FONT_BIG = {0x1b, 0x21, 0x11};
    public static final byte[] FORMAT_LF = {0X0A};
    public static final byte[] RESET = { 0x1b, 0x40 };

    public void print(int i){
    	write(byteCommands[i],null);
    }
    
    public synchronized void printReset(){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
            return;
        }
    	write(byteCommands[0],null);
        write(FORMAT_HT,null);
    }
    public static BluetoothService instance;
    public static BluetoothService getInstance(){
        if(instance==null) {
            instance=new BluetoothService();
        }
        return  instance;
    }


    public synchronized void printSize(int size){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
            return;
        }
    	switch (size) {
            case 0:
                write(CHAR_FONT_NORMAL,null);
                break;
            case 1:
                write(CHAR_FONT_BIG,null);
                break;
		default:
			write(byteCommands[3],null);
			break;
		}
    }
    
    public synchronized void printLeft(){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
            return;
        }
    	write(FORMAT_LEFT,null);
    }
    public void printRight(){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
    		return;
    	}
    	write(FORMAT_RIGHT,null);
    }
    public synchronized void printCenter(){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
    		return;
    	}
    	write(FORMAT_CENTER,null);
    }
    public synchronized void printLF(){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
    		return;
    	}
        write(FORMAT_LF,null);
    }
    public synchronized  void printLF(BtPrintFinshCallBack callBack){
    	if (getState() != BluetoothService.STATE_CONNECTED) {
    		return;
    	}
        write(FORMAT_LF,callBack);
    }


    /**
     * Constructor. Prepares a new BTPrinter session.
     * @param
     * @param
     */
    public BluetoothService() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }
    public void setHandler(Handler handler){
        this.mHandler=handler;
    }

    /**
     * Set the current state of the connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) Log.e(TAG, "setState() " + mState + " -> " + state);
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constant.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
     }

    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
        if (D) Log.e(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.e(TAG, "connect to: " + device);
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.e(TAG, "连接上了connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Cancel the accept thread because we only want to  one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        // Send the name of the connected device back to the UI Activity
        /*Message msg = mHandler.obtainMessage(Constant.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.e(TAG, "stop");
        setState(STATE_NONE);
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     */
   /* public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        synchronized (this){
            r.write(out,null);
        }
    }*/

    public synchronized void write(byte[] out,BtPrintFinshCallBack callBack) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out,callBack);
    }
    public synchronized void writeTest(List<byte[]> out, BtPrintFinshCallBack callBack) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        LogUtils.Log("out是否为空==" + (out == null));
        LogUtils.Log("out=="+out.size());
        r.writeTest(out, callBack);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);
        
        // Send a failure message back to the Activity
     /*   Message msg = mHandler.obtainMessage(Constant.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_LISTEN);
 
      /*  // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constant.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (D) Log.e(TAG, "BEGIN mAcceptThread" + this);
            setName("AcceptThread");
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    if(mmServerSocket!=null) {
                        socket = mmServerSocket.accept();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "accept() failed", e);
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                        }
                    }
                }
            }
            if (D) Log.i(TAG, "END mAcceptThread");
        }

        public void cancel() {
            if (D) Log.e(TAG, "cancel " + this);
            try {
                if(mmServerSocket!=null) {
                    mmServerSocket.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.e(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");
            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

               /* new Thread(){
                    public void run(){
                        try {
                        }catch (Exception e){
                            LogUtils.e(TAG,"再起分线程连接依旧异常"+e.toString());
                        }
                        synchronized (BluetoothService.this) {
                            mConnectThread = null;
                        }
                        // Start the connected thread
                        connected(mmSocket, mmDevice);
                    }
                }.start();*/
            } catch (Exception e) {
                LogUtils.e(TAG,"连接异常:"+e.toString());
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                BluetoothService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }
            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;

            public ConnectedThread(BluetoothSocket socket) {
                Log.e(TAG, "create ConnectedThread");
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;
                // Get the BluetoothSocket input and output streams
                try {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) {
                    Log.e(TAG, "temp sockets not created", e);
                }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run() {
                Log.e(TAG, "BEGIN mConnectedThread");
                int bytes;
                // Keep listening to the InputStream while connected
                while (true) {
                    try {
                        byte[] buffer = new byte[256];
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);
                        if(bytes>0){
                            // Send the obtained bytes to the UI Activity
	                    /*mHandler.obtainMessage(Constant.MESSAGE_READ, bytes, -1, buffer)
	                            .sendToTarget();*/
                        }else{
                            Log.e(TAG, "disconnected");
                            connectionLost();
                            //add by chongqing jinou
                            if(mState != STATE_NONE){
                                Log.e(TAG, "蓝牙无连接");
                                // Start the service over to restart listening mode
                        	BluetoothService.this.start();
                            }
                            break;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "disconnected", e);
                        connectionLost();
                        //add by chongqing jinou
                        if(mState != STATE_NONE){
                            Log.e(TAG, "蓝牙无连接", e);
                            // Start the service over to restart listening mode
                    	BluetoothService.this.start();
                        }
                        break;
                    }
                }
            }

        /**
         * Write to the connected OutStream.
         */
        public  void write(byte[] buffer,BtPrintFinshCallBack callBack) {
            try {
                    for (int i = 0; i < buffer.length; i++) {
                        mmOutStream.write(buffer[i]);
                        if(i==buffer.length-1&&callBack!=null) {
                            callBack.onFinish();
                        }
                    }
                } catch (IOException e) {
                    LogUtils.Log("Exception during write"+e.toString());
                }
        }
        /**
         * Write to the connected OutStream.
         */
        public  void writeTest(List<byte[]> data,BtPrintFinshCallBack callBack) {
                try {
                    while (data.size()>0){
                        byte[] buffer = data.get(0);
                        data.remove(0);
                        for (int i = 0; i < buffer.length; i++) {
                            mmOutStream.write(buffer[i]);
                            /*if(i==buffer.length-1&&callBack!=null) {
                                callBack.onFinish();
                            }*/
                        }
                    }

                } catch (IOException e) {
                    LogUtils.e(TAG, "Exception during write"+ e);
                }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
    public  interface BtPrintFinshCallBack{
        void onError();
        void onFinish();
    }
}
