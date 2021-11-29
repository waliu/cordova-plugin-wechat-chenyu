#### 分享示例

```ts
// 分享文字
shareText()
{
  let wxTextObject = {
    text: "测试文字"
  }
  let option = {
    message: {
      mediaObject: wxTextObject
    },
    scene: 0,
    type: "text"
  }

  window["WeChat"].share(option, (res) => {
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    this.presentToast(error["msg"]);
  });
}
// 分享图片
shareImg()
{
  // 一定要用线上地址
  let WXImageObject = {
    imagePath: "https://img0.baidu.com/it/u=3436810468,4123553368&fm=26&fmt=auto"
  }
  let option = {
    message: {
      title: "",
      description: "",
      thumbURL: "https://img0.baidu.com/it/u=3436810468,4123553368&fm=26&fmt=auto",
      mediaObject: WXImageObject
    },
    scene: 0,
    type: "img"
  }

  window["WeChat"].share(option, (res) => {
    //请求成功的反馈
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    //错误反馈
    this.presentToast(error["msg"]);
  });
}

// 分享音乐
shareMusic()
{
  let WXMusicObject = {
    musicUrl: "https://el-sycdn.kuwo.cn/3999d8ffb2687b7819e4bd138036bba3/619f300e/resource/n2/40/71/3964435247.mp3"
  }
  let option = {
    message: {
      title: "处处吻",
      description: "你风华绝代、惊艳时光你深情不悔、倾倒众生于梓贝以全新的方式演绎单曲《处处吻》你小心一吻便偷一人心一吻便救一个人",
      thumbURL: "https://img2.kuwo.cn/star/albumcover/500/40/46/2455952158.jpg",
      mediaObject: WXMusicObject
    },
    scene: 0,
    type: "music"
  }

  window["WeChat"].share(option, (res) => {
    //请求成功的反馈
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    //错误反馈
    this.presentToast(error["msg"]);
  });
}

//分享视频
shareVideo()
{
  let WXVideoObject = {
    videoUrl: "https://vd4.bdstatic.com/mda-kbic5h12n3ivuuhk/hd/mda-kbic5h12n3ivuuhk.mp4?v_from_s=hkapp-haokan-suzhou&auth_key=1637054891-0-0-86bc1255af6737160702a2846b0c26a8&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=3000187_1&klogid=3491684487"
  }
  let option = {
    message: {
      title: "处处吻",
      description: "你风华绝代、惊艳时光你深情不悔、倾倒众生于梓贝以全新的方式演绎单曲《处处吻》你小心一吻便偷一人心一吻便救一个人",
      thumbURL: "https://img2.kuwo.cn/star/albumcover/500/23/70/1613890609.jpg",
      mediaObject: WXVideoObject
    },
    scene: 0,
    type: "video"
  }

  window["WeChat"].share(option, (res) => {
    //请求成功的反馈
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    //错误反馈
    this.presentToast(error["msg"]);
  });
}
//分享网页

shareWebpage()
{
  let WXVideoObject = {
    webpageUrl: "https://www.baidu.com/"
  }
  let option = {
    message: {
      title: "百度",
      description: "百度是拥有强大互联网基础的领先AI公司。百度愿景是：成为最懂用户，并能帮助人们成长的全球顶级高科技公司。",
      thumbURL: "https://www.baidu.com/img/flexible/logo/pc/peak-result.png",
      mediaObject: WXVideoObject
    },
    scene: 0,
    type: "webpage"
  }

  window["WeChat"].share(option, (res) => {
    //请求成功的反馈
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    //错误反馈
    this.presentToast(error["msg"]);
  });
}
```

#### 支付示例

```ts
  // 支付
sendPaymentRequest()
{
  let option = {
    partnerId: "1601185215",
    prepayId: "up_wx17170930904580a8e12533ee7eeda90000",
    nonceStr: "SzcHvmcdY8NLA7bWVNxNzgcH745WV83B",
    timestamp: "1637140170",
    packageValue: "Sign=WXPay",
    sign: "E/nz0ldrpXdvDq8RrAH7gyHNT/4vrZnKn+td1j8lzke7gVxOcE8cSRo5y8akoQoT4wmvAoab2tljDnkFrobLGSLrXd6ftqn+mNb26FRxlEicFvyM9lZaUxXzaqPoyd7q6g1Hl4fFQy/Nkg7TZxT6UUwp4z2OokcGYaU5tsI/3PzliIcQJyd0xgPp9OZhRc+/LSVeT5o0pdaYGBbCmQTG06EH7gOAabpaIkEDzhOwlJ3wrP1j84iiHWJ6AVzQzxAFOlxGA4OwqHkrVdRogfUY48qAWsR7T8zWtdXCZ48sDMlK5soZTirpu8ftrGGdI0hmbXHOb2OkGPQX0fLHqFzJig==",
  }
  window["WeChat"].sendPaymentRequest(option, (res) => {
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    this.presentToast(error["msg"]);
  });
}
```

#### 登录示例

```ts
auth()
{
  let option = {
    scope: "snsapi_userinfo",
    state: new Date().getTime().toString(),
  }
  window["WeChat"].auth(option, (res) => {
    //请求成功的反馈
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    //错误反馈
    this.presentToast(error["msg"]);
  });
}
```

####  打开小程序示例

```ts

openMiniProgram()
{
  let option = {
    userName: "gh_f01f85672b87",
    path: "",
    miniprogramType: 0 // 环境
  }
  window["WeChat"].openMiniProgram(option, (res) => {
    //请求成功的反馈
    console.log("正确信息", JSON.stringify(res));
  }, (error) => {
    //错误反馈
    this.presentToast(error["msg"]);
  });
}

```
