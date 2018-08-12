var user = require('./lib/user')
var ajax = require('./lib/ajax')
var weixin = require('./lib/weixin')
var queryString = require('query-string')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    loaded: false,
    fee: 29800,
    status: 0,
    payloaded: true,
    channal: 0,
    balance: 0
  },
  created: function () {
    if (weixin.isWeixinBrowser()){
      const parsed = queryString.parse(window.location.search)
      if (parsed.code) {
        weixin.storeOpenId(parsed.code)
        weixin.WXinit(errorWXinit, finishWXinit)
      } else {
        if (!localStorage.OID) {
          window.location.href = weixin.redirectCode(window.location.pathname)
        } else {
          weixin.WXinit(errorWXinit, finishWXinit)
        }
      }
    }
  },
  methods: {
    payDeposit: function() {
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'payDeposit', user.getUserInfo().id])
      var data = user.getUserInfo()
      weixin.commonPay(vm.channal, vm.fee, true, callback)
    }
  }
})

function finishWXinit () {
  console.log('finishWXinit')
}
function errorWXinit () {
  console.log('finishWXinit')
  window.alert('微信初始化失败')
}

var callback = {
  endLoading: function(){
    vm.payloaded = true
  },
  success: function(orderId) {
    var playload = {
      amount: 29800,
      channel: 1,
      type: 1,
      orderId: orderId
    }
    ajax.post({
      path: '/v1/balances/deposit',
      data: playload,
      async: false,
      success: function(data) {
        console.log(data)
      },
      error: function(msg) {
        alert(msg)
      }
    })
    vm.payloaded = true
    console.log('success')
    window.location.replace('/wx/profile.html')
  },
  fail: function(content) {
    vm.payloaded = true
    console.log('fail')
    window.location.replace('/wx/profile.html')
  },
  cancel: function() {
    vm.payloaded = true
    console.log('cancel')
  }
}
