package zxing.decoding;/*
 * Copyright (C) 2008 ZXing authors
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



import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.younle.younle624.myapplication.R;

import java.util.Vector;

import zxing.camera.CameraManager;
import zxing.view.ViewfinderResultPointCallback;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 */
public final class CaptureActivityHandler extends Handler {

  private static final String TAG = CaptureActivityHandler.class.getSimpleName();

  private final DecodeHandlerInterface handlerInterface;
  private final DecodeThread decodeThread;
  public State state;

  public enum State {
    PREVIEW, SUCCESS, DONE
  }

  public CaptureActivityHandler(DecodeHandlerInterface handlerInterface,
                                Vector<BarcodeFormat> decodeFormats, String characterSet) {
    this.handlerInterface = handlerInterface;
    decodeThread = new DecodeThread(handlerInterface, decodeFormats,
            characterSet, new ViewfinderResultPointCallback(
            handlerInterface.getViewfinderView()));
    decodeThread.start();
    state = State.SUCCESS;
    // Start ourselves capturing previews and decoding.
    CameraManager.get().startPreview();
    restartPreviewAndDecode();
  }

  @Override
  public void handleMessage(Message message) {
    switch (message.what) {
      case R.id.auto_focus:
        // Log.d(TAG, "Got auto-focus message");
        // When one auto focus pass finishes, start another. This is the
        // closest thing to
        // continuous AF. It does seem to hunt a bit, but I'm not sure what
        // else to do.
        if (state == State.PREVIEW) {
          CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
        break;
      case R.id.restart_preview:
        restartPreviewAndDecode();
        break;
      case R.id.decode_succeeded:
        state = State.SUCCESS;
        Bundle bundle = message.getData();
        Bitmap barcode = bundle == null ? null : (Bitmap) bundle
                .getParcelable(DecodeThread.BARCODE_BITMAP);
        handlerInterface.handleDecode((Result) message.obj, barcode);
        break;
      case R.id.decode_failed:
        // We're decoding as fast as possible, so when one decode fails,
        // start another.
        state = State.PREVIEW;
        CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
                R.id.decode);
        break;
      case R.id.return_scan_result:
        Log.d(TAG, "Got return scan result message");
        handlerInterface.resturnScanResult(
                DecodeHandlerInterface.RESULT_STATE_OK, (Intent) message.obj);
        break;
      case R.id.launch_product_query:
        Log.d(TAG, "Got product query message");
        String url = (String) message.obj;
        handlerInterface.launchProductQuary(url);
        break;
    }
  }

  public void quitSynchronously() {
    state = State.DONE;
    CameraManager.get().stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try {
      decodeThread.join();
    } catch (InterruptedException e) {
      // continue
    }

    // Be absolutely sure we don't send any queued up messages
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }

  public void restartPreviewAndDecode() {
    if (state == State.SUCCESS) {
      state = State.PREVIEW;
      CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
              R.id.decode);
      CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
      handlerInterface.drawViewfinder();
    }
  }
}