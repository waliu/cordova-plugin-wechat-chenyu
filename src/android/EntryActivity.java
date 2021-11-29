package com.jxm.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import plugin.wechat.chenyu.WeChat;

/**
* Chen Yu 2021/11/25
**/
public class EntryActivity extends Activity implements IWXAPIEventHandler {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IWXAPI api = WeChat.getWxAPI(this);

    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    IWXAPI api = WeChat.getWxAPI(this);
    api.handleIntent(intent, this);
  }

  @Override
  public void onResp(BaseResp resp) {

    Intent intent = new Intent("CordovaPlugin.WeChat");
    intent.putExtra("type", resp.getType());
    intent.putExtra("errCode", resp.errCode);
    intent.putExtra("errStr", resp.errStr);
    intent.putExtra("transaction", resp.transaction);
    intent.putExtra("openId", resp.openId);
    intent.putExtra("pluginType", "BaseResp");

    switch (resp.getType()) {
      case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
        share(intent);
        break;
      case ConstantsAPI.COMMAND_SENDAUTH:
        auth((SendAuth.Resp) resp, intent);
        break;
      case ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM:
        wxLaunchMiniProgram((WXLaunchMiniProgram.Resp) resp, intent);
        break;
      case ConstantsAPI.COMMAND_PAY_BY_WX:
        pay(intent);
        break;
    }
    sendBroadcast(intent);      //发送广播
    finish();
  }

  @Override
  public void onReq(BaseReq req) {
    finish();
  }

  protected Intent pay(Intent intent) {
    intent.putExtra("pluginType", "Pay");
    return intent;
  }

  protected Intent share(Intent intent) {
    intent.putExtra("pluginType", "Share");
    return intent;
  }

  protected Intent auth(SendAuth.Resp resp, Intent intent) {
    intent.putExtra("pluginType", "Auth");
    intent.putExtra("code", resp.code);
    intent.putExtra("state", resp.state);
    intent.putExtra("authResult", resp.authResult);
    intent.putExtra("url", resp.url);
    intent.putExtra("lang", resp.lang);
    intent.putExtra("country", resp.country);
    return intent;
  }


  protected Intent wxLaunchMiniProgram(WXLaunchMiniProgram.Resp resp, Intent intent) {
    intent.putExtra("pluginType", "WXLaunchMiniProgram");
    intent.putExtra("extraData", resp.extMsg);
    return intent;
  }

}
