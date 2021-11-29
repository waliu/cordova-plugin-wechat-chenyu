package plugin.wechat.chenyu;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
/**
* Chen Yu 2021/11/25
**/
public class MiniProgram {
  private IWXAPI api;

  public MiniProgram(IWXAPI api) {
    this.api = api;
  }

  public void openMiniProgram(JSONObject jsonObject) throws RuntimeException, JSONException {
    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
    // 小程序id
    req.userName = jsonObject.getString("userName");
    //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
    req.path = jsonObject.getString("path");
    // 0 1 2
    req.miniprogramType = jsonObject.getInt("miniprogramType");
    boolean sendRes = api.sendReq(req);
    if (!sendRes) {
      throw new RuntimeException(Constants.Error506);
    }
  }
}
