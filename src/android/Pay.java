package plugin.wechat.chenyu;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;
/**
* Chen Yu 2021/11/25
**/
public class Pay {
  private IWXAPI api;

  public Pay(IWXAPI api) {
    this.api = api;
  }

  public void sendPaymentRequest(JSONObject json,String appId) throws RuntimeException, JSONException {
    PayReq req = new PayReq();
    req.appId = appId;  // appId
    req.partnerId = json.getString("partnerId");
    req.prepayId = json.getString("prepayId");
    req.nonceStr = json.getString("nonceStr");
    req.timeStamp = json.getString("timestamp");
    req.packageValue = json.getString("packageValue");
    req.sign = json.getString("sign");
    req.extData = "app data"; // optional
    boolean sendRes = api.sendReq(req);
    if (!sendRes) {
      throw new RuntimeException(Constants.Error503);
    }
  }
}
