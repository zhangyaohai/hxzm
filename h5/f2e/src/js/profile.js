var user = require('./lib/user')
var ajax = require('./lib/ajax')
var weixin = require('./lib/weixin')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    loaded: false,
    phone: '',
    status: 0,
    name: '',
    balance: 0,
    avatar: '../img/default_avartar.png',
    dot1color: '#fd9200',
    dot2color: '#fd9200',
    dot3color: '#fd9200',
    dot4color: '#fd9200',
    dot2border: '1px solid #fd9200',
    dot3border: '1px solid #fd9200',
    step1line: '#fd9200',
    step2line: '#fd9200',
    step3line: '#fd9200'
  },
  created: function () {
    ajax.setTitle('我的优拜')
      var weixinUser = weixin.getUserInfo()
      if (!!weixinUser) {
        this.avatar = weixinUser.avatar
      }
  }
})

ajax.get({
  path: '/v1/users/reload',
  success: function(data) {
    localStorage.user = JSON.stringify(data)
    vm.loaded = true
    if (data.realName){
      vm.name = data.realName
    } else {
      vm.name = data.phone
    }
    var weixinUser = weixin.getUserInfo()
    if (!!weixinUser) {
        this.avatar = weixinUser.avatar
    } else if (!!data.face) {
      vm.avartar = data.face
    } else if (data.gender === 1) {
      vm.avartar = '../img/avartar_male.png'
    } else if (data.gender === 2) {
      vm.avartar = '../img/avartar_female.png'
    }
    vm.status = data.status
    switch (vm.status) {
      case 10:
        vm.dot2color = '#fff'
        vm.dot3color = '#acabb3'
        vm.dot2border = '1px solid #fd9200'
        vm.dot3border = '1px solid #acabb3'
        vm.dot4color = '#acabb3'
        vm.step2line = '#acabb3'
        vm.step3line = '#acabb3'
        break
      case 40:
        vm.dot4color = '#acabb3'
        vm.step3line = '#acabb3'
        vm.dot3color = '#fff'
        break 
      default:
        break 

    }
    
    // vm.status = 40
    vm.phone = data.phone
    vm.balance = data.balance.toFixed(2)
  },
  error: function(msg) {
    alert(msg)
  }
})

