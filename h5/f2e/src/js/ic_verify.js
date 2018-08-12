var user = require('./lib/user')
var ajax = require('./lib/ajax')
var queryString = require('query-string')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    pickedType: 1,
    name: '',
    number: null
  },
  created: function () {
  },
  methods: {
    verify: function() {
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'verifyIC', user.getUserInfo().id])
      var playload = {}
      if (vm.pickedType === '1') {
        playload = {
          type: vm.pickedType,
          cardNo: vm.number,
          realName: vm.name
        }
      } else {
        playload = {
          type: vm.pickedType,
          passport: vm.number,
          realName: vm.name
        }
      }
      ajax.put({
        path: '/v1/users',
        data: playload,
        async: false,
        success: function(data) {
          var aa = document.createElement('a')
          aa.href = document.referrer
          if (aa.pathname === '/wx/profile.html') {
            window.location.replace('/wx/profile.html')
          } else {
            var userinfo = user.getUserInfo()
            userinfo.status = 40
            localStorage.user = JSON.stringify(userinfo)
            window.location.replace('/wx/deposit.html')
          }
        },
        error: function(msg) {
          alert(msg)
        }
      })
    }
  }
})
