var ajax = require('./lib/ajax')
var user = require('./lib/user')
var url = require('./lib/url')

var vm = new Vue({
  el: '#app',
  data: {
    isLogin: true,
    phoneNo: '',
    code: '',
    btnText: '获取',
    counter: 0,
    timer: '',
    codeId: ''
  },
  created: function () {
    ajax.setTitle('优拜单车')
  },
  methods: {
    switchToLogin: function() {
      vm.isLogin = true
    },
    switchToSignup: function() {
      vm.isLogin = false
    },
    getCode: function() {
      if (vm.phoneNo.length !== 11) {
        window.alert('手机号错误!')
        return
      }
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'validatecode'])
      vm.counter = 60
      vm.btnText = '60秒后重发'
      vm.timer = setInterval(function() {
        vm.counter--
        if (vm.counter == 0) {
          clearInterval(vm.timer)
          vm.btnText = '获取'
          return
        }
        vm.btnText = vm.counter + '秒后重发'
      }, 1000)
      ajax.post({
        path:'/v1/validatecode',
        data: {
          phone: vm.phoneNo,
          module: 'sign_up'
        },
        success: function(data) {
          vm.codeId = data
        },
        error: function(msg) {
          console.log(msg)
        }
      })
    },
    login: function() {
      ajax.post({
        path: '/v1/login',
        data: {
          randCode: vm.code,
          phone: vm.phoneNo,
          codeId: vm.codeId
        },
        success: function(data, xhr) {
          localStorage.user = JSON.stringify(data)
          user.saveToken(xhr.getResponseHeader('X-Token'))
          // getOrder()
          next()
        },
        error: function(msg) {
          alert(msg)
        }
      })
    },
    signup: function () {
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'login'])
      ajax.post({
        path: '/v1/register',
        data: {
          randCode: vm.code,
          phone: vm.phoneNo,
          codeId: vm.codeId
        },
        success: function(data, xhr) {
          localStorage.user = JSON.stringify(data)
          user.saveToken(xhr.getResponseHeader('X-Token'))
          getOrder()
          next()
        },
        error: function(msg) {
          alert(msg)
        }
      })
    }
  }
})

function getOrder () {
  ajax.get({
    path: '/v1/order/user',
    async: false,
    success: function (data) {
      if (data) {
        user.saveOrderInfo({orderId: data.id, timestamp: parseInt(new Date().getTime() / 1000, 10), status: -1})
      }
    },
    error: function(msg) {
      alert(msg)
    }
  })
}

function next () {
  if (url.getParam('return_to')) {
    window.location.replace(url.getParam('return_to'))
  } else {
    window.location.replace('/wx/home.html')
  }
}
