package plugin.wechat.chenyu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
* Chen Yu 2021/12/28
**/
public class WeChat extends CordovaPlugin {

  private static final String TAG = "CordovaPlugin.WeChat";
  // APP_ID 替换为你的应用从官方网站申请到的合法appID
  private String APP_ID = null;
  // IWXAPI 是第三方app和微信通信的openApi接口
  public static IWXAPI api;
  // 分享对象
  private Share share;
  //支付
  private Pay pay;
  //小程序类
  private MiniProgram miniProgram;
  // 认证类
  public AuthRequest authRequest;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    //判断微信是否安装
    isWXAppInstalled(callbackContext);
    try {
      switch (action) {
        case "share":
          this.share(args, callbackContext);
          return true;
        case "auth":
          this.auth(args, callbackContext);
          return true;
        case "sendPaymentRequest":
          this.sendPaymentRequest(args, callbackContext);
          return true;
        case "chooseInvoiceFromWX":
          this.chooseInvoiceFromWX(args, callbackContext);
          break;
        case "openMiniProgram":
          this.openMiniProgram(args, callbackContext);
          return true;
        default:
          return false;
      }
    } catch (RuntimeException e) {
      errorCallback(500, e.getMessage(), callbackContext);
      return true;
    }
    return false;
  }


  /**
   * @param ctx
   * @return
   */
  public static IWXAPI getWxAPI(Context ctx) {
    if (api == null) {
      throw new RuntimeException("微信api 初始化失败!");
    }
    return api;
  }


  protected void pluginInitialize() {
    this.APP_ID = preferences.getString("WECHAT_APPID", "");

    // 通过WXAPIFactory工厂，获取IWXAPI的实例
    api = WXAPIFactory.createWXAPI(cordova.getActivity(), APP_ID, true);
    // 将应用的appId注册到微信
    api.registerApp(APP_ID);
    //创建分享对象
    share = new Share(api);
    //支付对象
    pay = new Pay(api);
    //小程序类
    miniProgram = new MiniProgram(api);
    //认证类
    authRequest = new AuthRequest(api);
    //初始化监听类
    IntentFilter intentFilter = new IntentFilter();
    //这个ACTION和后面activity的ACTION一样就行，要不然收不到的
    intentFilter.addAction(TAG);
    //创建监听类
    cordova.getActivity().registerReceiver(myBroadcastReceive, intentFilter);
  }

  private void isWXAppInstalled(CallbackContext callbackContext) throws JSONException {
    if (!api.isWXAppInstalled()) {
      this.errorCallback(500, Constants.Error500, callbackContext);
    }
  }

  private void share(JSONArray args, CallbackContext callbackContext) throws JSONException, RuntimeException {
    JSONObject options = args.getJSONObject(0);
    //获取参数
    JSONObject message = options.getJSONObject("message");
    JSONObject mediaObject = message.getJSONObject("mediaObject");

    switch (options.getString("type")) {
      case "text":
        share.shareText(options, message, mediaObject);
        break;
      case "img":
        share.shareImg(options, message, mediaObject);
        break;
      case "music":
        share.shareMusic(options, message, mediaObject);
        break;
      case "video":
        share.shareVideo(options, message, mediaObject);
        break;
      case "webpage":
        share.shareWebpage(options, message, mediaObject);
        break;
      default:
        throw new RuntimeException(Constants.Error501);
    }
    //返回成功的值
    callbackContext.success();

  }

  private void auth(JSONArray args, CallbackContext callbackContext) {
    // send oauth request
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = "wechat_sdk_demo_test";
    api.sendReq(req);
  }

  private void sendPaymentRequest(JSONArray args, CallbackContext callbackContext) throws JSONException {
    JSONObject options = args.getJSONObject(0);
    //支付
    pay.sendPaymentRequest(options, APP_ID);
    //返回成功的值
    callbackContext.success();
  }

  private void chooseInvoiceFromWX(JSONArray args, CallbackContext callbackContext) {

  }

  private void openMiniProgram(JSONArray args, CallbackContext callbackContext) throws JSONException, RuntimeException {
    JSONObject options = args.getJSONObject(0);
    miniProgram.openMiniProgram(options);
  }

  public void errorCallback(int code, String string, CallbackContext callbackContext) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    jsonObject.put("msg", string);
    callbackContext.error(jsonObject);
  }

  public void successCallback(CallbackContext callbackContext, JSONObject jsonObject) {
    callbackContext.success(jsonObject);
  }

  BroadcastReceiver myBroadcastReceive = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      JSONObject jsonObject = new JSONObject();

      try {
        jsonObject.put("type", intent.getIntExtra("type", 500));
        jsonObject.put("errCode", intent.getIntExtra("errCode", 500));
        jsonObject.put("errStr", intent.getStringExtra("errStr"));
        jsonObject.put("transaction", intent.getStringExtra("transaction"));
        jsonObject.put("openId", intent.getStringExtra("openId"));
        jsonObject.put("pluginType", intent.getStringExtra("pluginType"));

        switch (Objects.requireNonNull(intent.getStringExtra("pluginType"))) {
          case "Auth":
            jsonObject.put("code", intent.getStringExtra("code"));
            jsonObject.put("state", intent.getStringExtra("state"));
            jsonObject.put("authResult", intent.getBooleanExtra("authResult", false));
            jsonObject.put("url", intent.getStringExtra("url"));
            jsonObject.put("lang", intent.getStringExtra("lang"));
            jsonObject.put("country", intent.getStringExtra("country"));
            break;
          case "WXLaunchMiniProgram":
            jsonObject.put("extraData", intent.getStringExtra("extraData"));
            break;
          case "Pay":
            break;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      webView.getView().post(new Runnable() {
        @Override
        public void run() {
          webView.loadUrl("javascript: WeChat.resp('" + jsonObject.toString() + "')");
        }
      });
    }
  };


}
