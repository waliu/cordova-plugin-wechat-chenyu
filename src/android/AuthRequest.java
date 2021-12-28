package com.example.flutter_plugin_wechat;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
* Chen Yu 2021/12/28
**/
public class AuthRequest {

    private IWXAPI api;

    public AuthRequest(IWXAPI api) {
        this.api = api;
    }

    public void  auth(JSONObject options) throws JSONException {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = options.getString("scope");
        req.state = options.getString("state");
        api.sendReq(req);
    }
}
