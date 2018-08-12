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
    wxloaded: true,
    payloaded: true,
    balance: 0,
    fee: null,
    channal: 0
  },
  created: function () {
    ajax.setTitle('优拜账户')
    if (weixin.isWeixinBrowser()){
      const parsed = queryString.parse(window.location.search)
      if (parsed.code) {
        weixin.storeOpenId(parsed.code)
      } else {
        if (!localStorage.OID) {
          window.location.href = weixin.redirectCode(window.location.pathname)
        }
      }
    }
  },
  methods: {
    pay: function() {
      if (vm.fee < 1) {
        window.alert('充值金额必须大于1元')
        return
      }
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'pay', user.getUserInfo().id])
      vm.payloaded = false
      weixin.commonPay(vm.channal, vm.fee * 100, false, callback)
    },
    setfee: function(fee) {
      vm.fee = fee
    }
  }
})

weixin.WXinit(errorWXinit, finishWXinit)
function finishWXinit () {
  console.log('finishWXinit')
  vm.wxloaded = false
}
function errorWXinit () {
  console.log('finishWXinit')
  window.alert('微信初始化失败')
}

var callback = {
  endLoading: function(){
    vm.payloaded = true
  },
  success: function() {
    vm.payloaded = true
    console.log('success')
    window.location.href = '/wx/profile.html'
  },
  fail: function(content) {
    vm.payloaded = true
    console.log('fail')
    window.location.href = '/wx/profile.html'
  },
  cancel: function() {
    vm.payloaded = true
    console.log('cancel')
  }
}

ajax.get({
  path: '/v1/users/reload',
  success: function(data) {
    vm.loaded = true
    vm.balance = data.balance.toFixed(2)
  },
  error: function(msg) {
    alert(msg)
  }
})

