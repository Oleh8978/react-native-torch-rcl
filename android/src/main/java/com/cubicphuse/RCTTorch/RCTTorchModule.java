package com.cubicphuse.RCTTorch;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.hardware.Camera;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RCTTorchModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;
    private Camera camera;
    private boolean isTorchOn = false;

    public RCTTorchModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        // Export module as `Torch` to match JS wrapper
        return "Torch";
    }

    @ReactMethod
    public void switchState(boolean newState, Promise promise) {
        // On API >= M use CameraManager.setTorchMode (no preview needed)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager cameraManager = (CameraManager) reactContext.getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] ids = cameraManager.getCameraIdList();
                if (ids.length == 0) {
                    promise.reject("E_NO_CAMERA", "No camera available");
                    return;
                }
                String cameraId = ids[0];
                cameraManager.setTorchMode(cameraId, newState);
                promise.resolve(true);
            } catch (CameraAccessException e) {
                promise.reject("E_TORCH_FAILED", e.getMessage());
            } catch (Exception e) {
                promise.reject("E_TORCH_FAILED", e.getMessage());
            }
            return;
        }

        // Fallback for older devices using deprecated Camera API
        try {
            if (newState && !isTorchOn) {
                camera = Camera.open();
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(params);
                camera.startPreview();
                isTorchOn = true;
            } else if (!newState && isTorchOn) {
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
                camera.release();
                camera = null;
                isTorchOn = false;
            }
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("E_TORCH_FAILED", e.getMessage());
        }
    }

    @ReactMethod
    public void hasTorch(Promise promise) {
        try {
            boolean has = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.hardware.camera2.CameraManager cameraManager = (android.hardware.camera2.CameraManager) reactContext.getSystemService(Context.CAMERA_SERVICE);
                String[] ids = cameraManager.getCameraIdList();
                if (ids.length > 0) has = true;
            } else {
                android.hardware.Camera cam = null;
                try {
                    cam = android.hardware.Camera.open();
                    has = cam != null;
                } catch (Exception ignored) {
                } finally {
                    if (cam != null) cam.release();
                }
            }
            promise.resolve(has);
        } catch (Exception e) {
            promise.reject("E_HAS_TORCH_FAILED", e.getMessage());
        }
    }

    @ReactMethod
    public void hasCameraPermission(Promise promise) {
        boolean granted = ContextCompat.checkSelfPermission(reactContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        promise.resolve(granted);
    }
}