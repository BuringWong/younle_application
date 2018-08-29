package com.younle.younle624.myapplication.utils.scanbar;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class HidConncetUtil {
	private BluetoothDevice device;
	Context context;
	ArrayList<BluetoothDevice> hidConncetList = new ArrayList<BluetoothDevice>();
	GetHidConncetListListener getHidConncetListListener;

	public HidConncetUtil(Context context) {
		this.context = context;
	}

	/**
	 * 获取BluetoothProfile中hid的profile，"INPUT_DEVICE"类型隐藏，需反射获取
	 * @return
     */
	@SuppressLint("NewApi")
	public static int getInputDeviceHiddenConstant() {
		Class<BluetoothProfile> clazz = BluetoothProfile.class;
		for (Field f : clazz.getFields()) {
			int mod = f.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod)
					&& Modifier.isFinal(mod)) {
				try {
					if (f.getName().equals("INPUT_DEVICE")) {
						return f.getInt(null);
					}
				} catch (Exception e) {
				}
			}
		}
		return -1;
	}

	/**
	 * 通过getHidConncetListListener.getSuccess(hidConncetList);回调
	 */
	private BluetoothProfile.ServiceListener getList = new BluetoothProfile.ServiceListener() {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			try {
				if (profile == getInputDeviceHiddenConstant()) {
					hidConncetList.clear();
					List<BluetoothDevice> connectedDevices = proxy
							.getConnectedDevices();
					for (BluetoothDevice bluetoothDevice : connectedDevices) {
							hidConncetList.add(bluetoothDevice);
					}
				}
				getHidConncetListListener.getSuccess(hidConncetList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}

		@Override
		public void onServiceDisconnected(int profile) {

		}
	};
	/**
	 *查看BluetoothInputDevice源码，connect(BluetoothDevice device)该方法可以连接HID设备，但是查看BluetoothInputDevice这个类
	 * 是隐藏类，无法直接使用，必须先通过BluetoothProfile.ServiceListener回调得到BluetoothInputDevice，然后再反射connect方法连接
	 *
	 */
	public BluetoothProfile.ServiceListener connect = new BluetoothProfile.ServiceListener() {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			//BluetoothProfile proxy这个已经是BluetoothInputDevice类型了
			try {
				if (profile == getInputDeviceHiddenConstant()) {
					if (device != null) {
						//得到BluetoothInputDevice然后反射connect连接设备
						Method method = proxy.getClass().getMethod("connect",
								new Class[] { BluetoothDevice.class });
						method.invoke(proxy, device);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(int profile) {

		}
	};
	private BluetoothProfile.ServiceListener disConnect = new BluetoothProfile.ServiceListener() {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			try {
				if (profile == getInputDeviceHiddenConstant()) {
					List<BluetoothDevice> connectedDevices = proxy.getConnectedDevices();
					for (BluetoothDevice bluetoothDevice : connectedDevices) {
							hidConncetList.add(bluetoothDevice);
					}
					
					if (device != null) {
						Method method = proxy.getClass().getMethod("disconnect",
								new Class[] { BluetoothDevice.class });
						method.invoke(proxy, device);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		
		@Override
		public void onServiceDisconnected(int profile) {
			
		}
	};

	/**
	 * 连接设备
	 * @param bluetoothDevice
     */
	public void connect(final BluetoothDevice bluetoothDevice) {
		device = bluetoothDevice;
		try {
			BluetoothAdapter.getDefaultAdapter().getProfileProxy(context,
					connect, getInputDeviceHiddenConstant());
		} catch (Exception e) {
			LogUtils.Log("hid设备连接失败 "+e.toString());
		}
	}

	/**
	 * 断开连接
	 * @param bluetoothDevice
     */
	public void disConnect(BluetoothDevice bluetoothDevice) {
		device = bluetoothDevice;
		try {
			BluetoothAdapter.getDefaultAdapter().getProfileProxy(context,
					disConnect, getInputDeviceHiddenConstant());
		} catch (Exception e) {
			
		}
	}


	/**
	 * 配对
	 * @param bluetoothDevice
     */
	public void pair(BluetoothDevice bluetoothDevice) {
		device = bluetoothDevice;
		Method createBondMethod;
		try {
			createBondMethod = BluetoothDevice.class.getMethod("createBond");
			createBondMethod.invoke(device);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 取消配对
	 * @param bluetoothDevice
     */
	public void unPair(BluetoothDevice bluetoothDevice) {
		device = bluetoothDevice;
		Method createBondMethod;
		try {
			createBondMethod = BluetoothDevice.class.getMethod("removeBond");
			createBondMethod.invoke(device);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void initState(Context context,TextView notice,ImageView icon,String connectedMsg){
		int hidState = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(HidConncetUtil.getInputDeviceHiddenConstant());
		if(hidState==2) {
			notice.setText(connectedMsg);
			icon.setBackgroundResource(R.drawable.scanner_connected);
			icon.clearAnimation();
		}else if(hidState==0){
			notice.setText("连接失败,请按码枪上的扳机,启动扫码枪");
			icon.setBackgroundResource(R.drawable.scanner_disconnected);
			icon.clearAnimation();
		}else if(hidState==1) {
			notice.setText("正在连接蓝牙扫码枪");
			icon.setBackgroundResource(R.drawable.scanner_connecting);
			Utils.pbAnimation(context, icon);
		}
	}

	/**
	 * 返回true代表修改成功 返回false代表修改失败
	 * */
	public Boolean rename(BluetoothDevice bluetoothDevice, String name) {
		device = bluetoothDevice;
		Method createBondMethod;
		try {
			createBondMethod = BluetoothDevice.class.getMethod("setAlias",
					String.class);
			Boolean Issuccess = (Boolean) createBondMethod.invoke(device, name);
			return Issuccess;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 得到所有HID连接的设备列表
	 * @param getHidConncetListListener
     */
	public void getHidConncetList(
			GetHidConncetListListener getHidConncetListListener) {
		this.getHidConncetListListener = getHidConncetListListener;
		try {
			BluetoothAdapter.getDefaultAdapter().getProfileProxy(context,
					getList, getInputDeviceHiddenConstant());
		} catch (Exception e) {

		}

	}

	public interface GetHidConncetListListener {
		 void getSuccess(ArrayList<BluetoothDevice> list);
	}

	public void setConfig(){
		Configuration cfg=context.getResources().getConfiguration();
		cfg.keyboard=Configuration.KEYBOARD_QWERTY;
	}
}
