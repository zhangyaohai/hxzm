var user = require('./lib/user')
var ajax = require('./lib/ajax')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    loaded: false,
    nofee: true,
    history: []
  },
  created: function () {
    ajax.setTitle('消费记录')
  },
  methods: {
    formatTime: function(timestamp) {
      timestamp = new Date(timestamp)
      var date = (timestamp.getFullYear()) + "-" + 
        (timestamp.getMonth() + 1) + "-" +
        (timestamp.getDate()) + " " + 
        (timestamp.getHours()) + ":" + 
        (timestamp.getMinutes()) + ":" + 
        (timestamp.getSeconds());
      return date
    },
    goto: function(id) {
      if( id > 0){
        window.location.href = '/wx/order_detail.html?orderId=' + id
      }
    },
    returnS: function(type) {
      if (type === 10){
        return '支出'
      } else if (type === 21) {
        return '红包'
      } else {
        return '充值'
      }
    }
  }
})

ajax.get({
  path: '/v1/balances?pageSize=100000',
  success: function(data) {
    vm.loaded = true
    vm.history = data.data
    if (data.data.length !== 0) {
      vm.nofee = false
    }
  },
  error: function(msg) {
    alert(msg)
  }
})

