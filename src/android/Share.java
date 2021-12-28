package plugin.wechat.chenyu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
* Chen Yu 2021/11/25
**/
public class Share {
  private IWXAPI api;

  public Share(IWXAPI api) {
    this.api = api;
  }

  public void shareText(JSONObject options, JSONObject message, JSONObject mediaObject) throws RuntimeException, JSONException {
    //初始化一个 WXTextObject 对象，填写分享的文本内容
    WXTextObject textObj = new WXTextObject();
    textObj.text = mediaObject.getString("text");
    sendShare(options, message,mediaObject, textObj);

  }

  private static final int THUMB_SIZE = 150;

  public void shareImg(JSONObject options, JSONObject message, JSONObject mediaObject) throws RuntimeException, JSONException {
    //创建分享图片的二进制流
    Bitmap bmp = getOnLineBitmap(mediaObject.getString("imagePath"));
    //初始化 WXImageObject 和 WXMediaMessage 对象
    WXImageObject imgObj = new WXImageObject(bmp);
    sendShare(options, message,mediaObject, imgObj);
  }

  public void shareMusic(JSONObject options, JSONObject message, JSONObject mediaObject) throws RuntimeException, JSONException {
    //初始化一个WXMusicObject，填写url
    WXMusicObject music = new WXMusicObject();
    music.musicUrl = mediaObject.getString("musicUrl");
    sendShare(options, message,mediaObject, music);
  }

  public void shareVideo(JSONObject options, JSONObject message, JSONObject mediaObject) throws RuntimeException, JSONException {
    //初始化一个WXVideoObject，填写url
    WXVideoObject video = new WXVideoObject();
    video.videoUrl = mediaObject.getString("videoUrl");
    sendShare(options, message,mediaObject, video);
  }

  public void shareWebpage(JSONObject options, JSONObject message, JSONObject mediaObject) throws RuntimeException, JSONException {
    //初始化一个WXWebpageObject，填写url
    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = mediaObject.getString("webpageUrl");
    sendShare(options, message,mediaObject, webpage);
  }

  private void sendShare(JSONObject options, JSONObject message, JSONObject mediaObject, WXMediaMessage.IMediaObject var1) throws JSONException {
    //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
    WXMediaMessage msg = new WXMediaMessage(var1);
    if (!options.getString("type").equals("text")){
      msg.title = message.getString("title");
      msg.description = message.getString("description");
      //普通图
      Bitmap bmp = getOnLineBitmap(message.getString("thumbURL"));
      //转换为缩略图
      Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
      msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
    }else {
      //
      msg.description = mediaObject.getString("text");
    }
    //构造一个Req
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction(options.getString("type"));
    req.message = msg;
    req.scene = options.getInt("scene");
    //req.userOpenId = getOpenId();
    //调用api接口，发送数据到微信
    boolean sendRes = api.sendReq(req);
    if (!sendRes) {
      throw new RuntimeException(Constants.Error502);
    }
  }

  private void checkArgs(byte[] thumbData){
    if (thumbData!= null && thumbData.length != 0){
      Log.e("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, thumbData should not be null when send emoji");
    }
  }

  /**
   * 获取线上图片
   *
   * @param url
   * @return
   * @throws RuntimeException
   */
  public Bitmap getOnLineBitmap(String url) throws RuntimeException {
    Bitmap bm = null;
    URL imageUrl = null;
    try {
      imageUrl = new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException(Constants.Error504);
    }
    try {
      HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
      conn.setDoInput(true);
      conn.connect();
      InputStream is = conn.getInputStream();
      bm = BitmapFactory.decodeStream(is);
      is.close();
    } catch (IOException e) {
      throw new RuntimeException(Constants.Error505);
    }
    return bm;
  }


  private String buildTransaction() {
    return String.valueOf(System.currentTimeMillis());
  }

  private String buildTransaction(final String type) {
    return type + System.currentTimeMillis();
  }

}
