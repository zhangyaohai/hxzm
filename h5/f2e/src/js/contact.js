var user = require('./lib/user')
var ajax = require('./lib/ajax')

var vm = new Vue({
  el: '#app',
  methods: {
    gotocall: function(callNum) {
      window.location.href = 'tel:' + callNum 
    }
  }
})
