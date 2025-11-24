#import "RCTTorch.h"
#import <AVFoundation/AVFoundation.h>

@implementation RCTTorch

RCT_EXPORT_MODULE(Torch);

RCT_EXPORT_METHOD(switchState:(BOOL)newState
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
  if (!device) {
    reject(@"E_NO_DEVICE", @"No video capture device found", nil);
    return;
  }

  if (![device hasTorch] || ![device isTorchAvailable]) {
    reject(@"E_NO_TORCH", @"Device has no torch", nil);
    return;
  }

  NSError *error = nil;
  if ([device lockForConfiguration:&error]) {
    AVCaptureTorchMode mode = newState ? AVCaptureTorchModeOn : AVCaptureTorchModeOff;
    if ([device respondsToSelector:@selector(setTorchMode:)]) {
      [device setTorchMode:mode];
      [device unlockForConfiguration];
      resolve(@(YES));
    } else {
      [device unlockForConfiguration];
      reject(@"E_TORCH_UNSUPPORTED", @"Torch mode not supported on this device", nil);
    }
  } else {
    reject(@"E_LOCK_FAILED", error.localizedDescription, error);
  }
}

RCT_EXPORT_METHOD(hasTorch:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
  BOOL hasTorch = device != nil && [device hasTorch];
  resolve(@(hasTorch));
}

@end