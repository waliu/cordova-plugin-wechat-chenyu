var exec = require('cordova/exec');
const weChatName = "WeChat";

var WeChat = {
  /**
   * 分享
   * @param args
   * @param success
   * @param error
   * @version 1.0.1
   */
  share: (args, success, error) => {
    exec(success, error, weChatName, 'share', [args]);
  },
  /**
   * 登录
   * @param args
   * @param success
   * @param error
   * @version 1.0.1
   */
  auth(args, success, error) {
    exec(success, error, weChatName, 'auth', [args]);
  },
  /**
   * 支付
   * @param args
   * @param success
   * @param error
   * @version 1.0.1
   */
  sendPaymentRequest: (args, success, error) => {
    exec(success, error, weChatName, 'sendPaymentRequest', [args]);
  },
  /**
   *
   * 打开小程序
   * @param args
   * @param success
   * @param error
   * @version 1.0.1
   */
  openMiniProgram: (args, success, error) => {
    exec(success, error, weChatName, 'openMiniProgram', [args]);
  },
  /**
   * 委托发起人。
   * java/oc 执行的方法 发送订阅 给 on
   * @param contentJson
   * @version 1.0.1
   */
  resp(contentJson) {
    let content = null;
    let respEvent = new CustomEvent('respEvent', {
      'detail': {  //可携带额外的数据
        message: null
      },
      'bubbles': true,//是否冒泡    回调函数中调用，e.stopPropagation();可以阻止冒泡
      'cancelable': false,//是否可以取消  为true时，event.preventDefault();才可以阻止默认动作行为
    });
    try {
      content = JSON.parse(contentJson);
      respEvent.detail.message = content;
      window.dispatchEvent(respEvent);
    } catch (e) {
      console.log("JSON格式错误");
    }
  },
  /**
   * 委托接收人。
   * 监听微信 sdk 传送过来的值
   * @param listener 回调函数
   * @version 1.0.1
   */
  onResp: (listener) => {
    window.addEventListener("respEvent", (e) => {
      listener(e.detail.message);
    });
  }
}
module.exports = WeChat;

