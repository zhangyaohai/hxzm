var user = require('./lib/user')
var ajax = require('./lib/ajax')
var decrypt = require('./lib/decrypt')
var queryString = require('query-string')
var weixin = require('./lib/weixin')
var config = require('./config')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    fee: null,
    distance: null,
    during: null,
    orderId: null,
    wxloaded: true,
    show: false
  },
  created: function () {
    if (queryString.parse(window.location.search).id) {
      var p = queryString.parse(decrypt.decodeNum(queryString.parse(window.location.search).id))
      this.orderId = p.orderId
      this.fee = p.fee
      this.distance = p.distance
      this.during = p.during
      localStorage.removeItem('orderInfo')
      localStorage.finishOrder = p.orderId
    }
  },
  methods: {
    share: function() {
      vm.show = true
    },
    dismiss: function() {
      vm.show = false
    }
  }
})

weixin.WXinit(errorWXinit, finishWXinit)
function finishWXinit () {
  console.log('finishWXinit')
  vm.wxloaded = false
  const param = queryString.parse(decrypt.decodeNum(queryString.parse(window.location.search).id))
  weixin.share('优拜单车 让骑行更简单', '优拜单车是短途出行的最佳选择，转向助力，随意变速，更多功能等你来体验', 'http://' + config.SITE_URL + '/wx/share.html?orderId='+param.orderId, 'http://' + config.SITE_URL + '/img/share_icon.png', successCB, failCB)
}

function errorWXinit () {
  console.log('finishWXinit')
  window.alert('微信初始化失败')
}

function successCB () {}

function failCB () {}
