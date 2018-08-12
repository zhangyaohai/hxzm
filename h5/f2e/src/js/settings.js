var user = require('./lib/user')
var ajax = require('./lib/ajax')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  methods: {
    logout: function() {
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'logout', user.getUserInfo().id])
      localStorage.removeItem('user')
      localStorage.removeItem('OID')
      localStorage.removeItem('ut')
      localStorage.removeItem('orderInfo')
      window.location.replace('/wx/home.html')
    },
    refund: function() {
      _hmt.push(['_trackEvent', 'ubike_button', 'click', 'refund', user.getUserInfo().id])
      var r=window.confirm("退还押金流程中将无法使用车辆, 确认要退还押金吗？")
      console.log(r)
      if (r==true){ 
        ajax.post({
          path: '/v1/deposit/refund',
          success: function(data) {
            alert('您的退款申请已经受理，退款将在3-7个工作日打回您的微信钱包')
            window.location.href = './profile.html'
          },
          error: function(msg) {
            alert(msg)
            window.location.href = './profile.html'
          }
        })
      } 
      else{ 
        window.location.href = './profile.html'
      } 
    }
  }
})
