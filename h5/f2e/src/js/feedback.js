var user = require('./lib/user')
var ajax = require('./lib/ajax')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    fbcontent: null
  },
  methods: {
    postContent: function() {
      var playload = {
        'content': vm.fbcontent,
        'type': 'string',
        'userId': user.getUserInfo().id
      }
      ajax.post({
        path: '/v1/feedback',
        data: playload,
        async: false,
        success: function(data) {
          window.alert('提交成功')
          window.location.href = './index.html'
        },
        error: function(msg) {
          alert(msg)
        }
      })
    }
  }
})
