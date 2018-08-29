package com.younle.younle624.myapplication.utils.printmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 作者：Create by 我是奋斗 on2017/4/26 18:04
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class ConnectBtUtils extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private final BluetoothAdapter mBluetoothAdapter;
    private final UUID MY_UUID;


    public ConnectBtUtils(BluetoothAdapter bluetoothAdapter) {

        mBluetoothAdapter = bluetoothAdapter;
        MY_UUID = UUID.fromString("2da12563-e314-1211-7789-" + mBluetoothAdapter.getAddress().replace(":", ""));
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            //get the server socket listenning the client's connection requests.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MyBLTServer", MY_UUID);
        } catch (IOException e) { }
         mmServerSocket = tmp;
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                /*Start listening for connection requests by calling accept().
                 *This is a blocking call. A connection is accepted only when a remote device has sent
                 * a connection request with a UUID matching the one registered with this listening server socket.
                 * When successful, accept() will return a connected BluetoothSocket.
                 */
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                System.out.println("Unable to accept; close the socket and get out");
                cancel();
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                BltConnectionThread bltConnectionThread = new BltConnectionThread(socket);
                bltConnectionThread.start();
                EventBus.getDefault().post(bltConnectionThread);
                //if you want to connect only one bluetooth device,
                //should break here, close the server socket.
                //cancle(); break;
            }
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

    public class BltClientConnectionThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID;

        public BltClientConnectionThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            //The same as the server's uuid.
            MY_UUID = UUID.fromString("2da12563-e314-1211-7789-" + device.getAddress().replace(":", ""));

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }
        public void run() {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                cancel();
                return;
            }

            // Do work to manage the connection (in a separate thread)
            BltConnectionThread bltConnectionThread = new BltConnectionThread(mmSocket);
            bltConnectionThread.start();
            EventBus.getDefault().post(bltConnectionThread);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            } finally {

            }
        }
    }
    public class BltConnectionThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public BltConnectionThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {

            int bufferSize = 1024;
            int bytesRead = -1;
            byte[] buffer = new byte[bufferSize];

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    final StringBuilder sb = new StringBuilder();
                    // Read from the InputStream
                    bytesRead = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    if (bytesRead != -1) {
                        String result = "";
                        while ((bytesRead == bufferSize) && (buffer[bufferSize] != 0)) {
                            result = result + new String(buffer, 0, bytesRead);
                            bytesRead = mmInStream.read(buffer);
                        }
                        result = result + new String(buffer, 0, bytesRead);
                        sb.append(result);
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

    }
    }
