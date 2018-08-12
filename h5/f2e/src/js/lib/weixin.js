var ajax = require('./ajax')
var queryString = require('query-string')
var decrypt = require('./decrypt')
var config = require('../config')

const APP_ID = 'wx801722fdf3ad8e84'
const NONCE_STR = 'IloveUbike2016'
const API_LIST = [
  'checkJsApi',
  'chooseWXPay',
  'onMenuShareAppMessage',
  'onMenuShareTimeline',
  'onMenuShareQQ',
  'onMenuShareQZone',
  'onMenuShareWeibo',
  'scanQRCode',
  'getLocation'
]

function init (options, callError, callReady) {
  console.log('weixin init')
  window.wx.config({
    debug: false,
    appId: APP_ID,
    nonceStr: NONCE_STR,
    timestamp: options.datetime,
    signature: options.signature,
    jsApiList: API_LIST
  })
  window.wx.error(function (res) {
    console.log(res)
    callError()
    return
  })
  window.wx.ready(function () {
    console.log('weixin ready')
    callReady()
  })
}

function WXinit (callError, callReady) {
  if (!isWeixinBrowser()) {
    return
  }
  var timeStamp = parseInt(new Date().getTime() / 1000, 10)
  var playload = {
    'nonceStr': NONCE_STR,
    'timestamp': timeStamp,
    'url': window.location.href
  }
  var options = undefined
  ajax.post ({
    path: '/v1/wxpay/initialsigns',
    data: playload,
    async: false,
    success: function (data) {
      options = {
        datetime: data.timestamp,
        signature: data.sign
      }
      init(options, callError, callReady)
    },
    error: function (msg) {
      window.alert(msg)
    }
  })
}

function scanQR (cb) {
  window.wx.checkJsApi({
    jsApiList: ['scanQRCode'], // 需要检测的JS接口列表,
    success: function (res) {
      if (res.checkResult.scanQRCode) {
        window.wx.scanQRCode({
          needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
          scanType: ['qrCode', 'barCode'], // 可以指定扫二维码还是一维码，默认二者都有
          success: function (res) {
            console.log(res)
            var result = res.resultStr
            var a = document.createElement('a')
            a.href = result
            var data = queryString.parse(a.search)
            var check = a.hostname.split('.')
            console.log(check)
            if (data.number) {
              if (check.pop() === 'cn') {
                if (check.pop() === 'ubike') {
                  if (cb) {
                    cb(data.number)
                    return
                  }
                  window.location.href = '/wx/unlock.html?bn=' + decrypt.encodeNum(data.number)
                }
              }
            }
          }
        })
      } else {
        WXinit()
        window.alert('微信授权失败，请重新点击尝试.')
      }
    }
  })
}

function redirectCode (redirectUrl, scope) {
  var playload = {
    'appid': APP_ID,
    'redirect_uri': 'https://wx.ubike.cn/redirect/' + config.SITE_URL + redirectUrl,
    'response_type': 'code',
    'scope': scope || 'snsapi_base',
    'state': 'STATE'
  }
  var req = playload ? '?' + queryString.stringify(playload) : ''
  req = 'https://open.weixin.qq.com/connect/oauth2/authorize' + req + '#wechat_redirect'
  return req
}

function WXPay (fee, type, callback) {
  var dt = parseInt(new Date().getTime() / 1000, 10)
  const playload = {
    'nonceStr': NONCE_STR,
    'body': type ? '优拜单车押金' : '优拜单车充值',
    'totalFee': parseInt(fee, 10),
    'timestamp': dt,
    'type': 1,
    'payType': type ? 10 : 20,
    'ip': '29.123.164.23',
    // openId: 'oMUJfwme_bDjZ6C3h30FDSSdYEy0'
    'openId': getOpenId()
  }
  // console.log(playload.payType)
  ajax.post({
    path: '/v1/wxpay/weixin',
    // domain: 'http://preapi.xiaomayundong.com/xmyd-api-web/v1', // need to change to api server
    data: playload,
    // token: 'cEVVnCpLWILbBDdkndYv3g==',
    async: false,
    success: function (data) {
      // console.log(data)
      _WXPay(data, callback)
    },
    error: function (msg) {
      callback.endLoading()
      window.alert(msg)
    }
  })
}

function pay (channal, fee, type, callback) {
  WXPay(fee, type, callback)
}

function _WXPay (resdata, callback) {
  window.wx.chooseWXPay({
    timestamp: resdata.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
    nonceStr: resdata.nonceStr,           // 支付签名随机串，不长于 32 位
    package: 'prepay_id=' + resdata.prepayId, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
    signType: 'MD5',
    paySign: resdata.sign,
    success: function (res) {
      callback.success(resdata.orderId)
      var playload = {
        orderId: resdata.orderId,
        amount: resdata.totalFee / 100,
        channel: 1,
        type: 1
      }
      ajax.post({
        path: '/v1/balances/charge',
        data: playload,
        async: false,
        success: function (data) {
        },
        error: function (msg) {
          // window.alert(msg)
        }
      })
      // 支付成功后的回调函数
    },
    fail: function (res) {
      callback.fail()
      window.alert(res.errMsg)
    },
    cancel: function (res) {
      callback.cancel()
    }
  })
}

function storeOpenId (code, callback) {
  ajax.get({
    path: '/v1/weixin/user/h5/openid?code=' + code,
    success: function (data) {
        localStorage.OID = data.openId || data.openid
        localStorage.setItem('WX_USERINFO', "{\"avatar\":\""+data.avatar+"\", \"nickname\":\""+data.nickname+"\", \"sex\":"+data.sex+"}")
        if (!!callback) {
          callback(data)
        }
    },
    error: function (msg) {
      // window.alert(msg)
    }
  })
}

function getOpenId () {
  return localStorage.OID
}

function getUserInfo() {
  var userInfo = localStorage.getItem('WX_USERINFO');
  try {
    return userInfo && JSON.parse(userInfo)
  } catch (e) {
    console.log(e + ' : ' + userInfo)
      localStorage.removeItem('WX_USERINFO')
    return null
  }
}

function isWeixinBrowser () {
  var ua = navigator.userAgent.toLowerCase()
  if (ua.match(/micromessenger/i)) {
    return true
  } else {
    return false
  }
}

function share (title, description, link, imgUrl, successCB, failCB) {
  window.wx.onMenuShareAppMessage({
    title: title,      // 分享标题
    desc: description, // 分享描述
    link: link,        // 分享链接
    imgUrl: imgUrl,    // 分享图标
    type: '',      // 分享类型,music、video或link，不填默认为link
    dataUrl: '',       // 如果type是music或video，则要提供数据链接，默认为空
    success: successCB(),
    cancel: failCB()
  })
  window.wx.onMenuShareTimeline({
    title: title,      // 分享标题
    link: link,        // 分享链接
    imgUrl: imgUrl,    // 分享图标
    success: successCB(),
    cancel: failCB()
  })
  window.wx.onMenuShareQQ({
    title: title,      // 分享标题
    desc: description, // 分享描述
    link: link,        // 分享链接
    imgUrl: imgUrl,    // 分享图标
    success: successCB(),
    cancel: failCB()
  })
  window.wx.onMenuShareWeibo({
    title: title,      // 分享标题
    desc: description, // 分享描述
    link: link,        // 分享链接
    imgUrl: imgUrl,    // 分享图标
    success: successCB(),
    cancel: failCB()
  })
  window.wx.onMenuShareQZone({
    title: title,      // 分享标题
    desc: description, // 分享描述
    link: link,        // 分享链接
    imgUrl: imgUrl,    // 分享图标
    success: successCB(),
    cancel: failCB()
  })
}

function shareCommon () {
  share('优拜单车 让骑行更简单', '优拜单车是短途出行的最佳选择，转向助力，随意变速，更多功能等你来体验', 'https://' + config.SITE_URL + '/wx', 'http://' + config.SITE_URL + '/img/share_icon.png', successCB, failCB)
}

function successCB () {

}

function failCB () {

}

module.exports = {
  commonPay: pay,
  shareCommon: shareCommon,
  share: share,
  isWeixinBrowser: isWeixinBrowser,
  redirectCode: redirectCode,
  init: init,
  storeOpenId: storeOpenId,
  WXinit: WXinit,
  scanQR: scanQR,
  getUserInfo: getUserInfo,
}
