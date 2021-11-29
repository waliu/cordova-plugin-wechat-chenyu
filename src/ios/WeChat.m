/********* WeChat.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface WeChat : CDVPlugin {
  // Member variables go here.
}

- (void)share:(CDVInvokedUrlCommand*)command;
- (void)auth:(CDVInvokedUrlCommand*)command;
- (void)sendPaymentRequest:(CDVInvokedUrlCommand*)command;
- (void)openMiniProgram:(CDVInvokedUrlCommand*)command;
@end

@implementation WeChat

- (void)share:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
