var map = require('./lib/map')
var user = require('./lib/user')
var ajax = require('./lib/ajax')
var weixin = require('./lib/weixin')
var config = require('./config')
var decrypt = require('./lib/decrypt')
var queryString = require('query-string')

var vm = new Vue({
  el: '#app',
  data: {
    hasUser: user.isPresent(),
    loaded: true,
    wxloaded: true,
    showMarker: true,
    showNotify: false,
    user_login: false,
    inputScan: false,
    showDial: false,
    diaContent: '',
    si: null,
    balance: 1,
    isScan: false,
    statusContent: '微信初始化完成',
    bikeNum: null,
    screen_width: null,
    nearMark: null,
    running: false,
    showTab: true,
    rundata: null,
    getorderL: true,
    isNewUser: false,
    dialogShow: true
  },
  created: function () {
    // console.log('life created')
      this.dialogShow = new Date().getTime() < 1485388800000
    console.log('created')
    var parsed = queryString.parse(window.location.search)
    if (parsed.biz_content) {
      console.log(parsed)
      if (parsed.biz_content.error_code) {
        // console.log(parsed.biz_content)
        // console.log(1)
        window.location.href = '/wx/index.html'
      } else {
        console.log(parsed.biz_content)
        console.log(JSON.parse(JSON.parse(parsed.biz_content).invoke_state).number)
        getAlipayStatus(window.location.search, JSON.parse(JSON.parse(parsed.biz_content).invoke_state).number)
        console.log(JSON.parse(JSON.parse(parsed.biz_content).invoke_state).token)
        user.saveToken(JSON.parse(JSON.parse(parsed.biz_content).invoke_state).token)
      }
    }
    if (weixin.isWeixinBrowser()) {
      var parsed = queryString.parse(window.location.search)
      if (parsed.code) {
        weixin.storeOpenId(parsed.code)
      } else {
        if (!localStorage.OID) {
          window.location.href = weixin.redirectCode(window.location.pathname)
          return
        }
      }
    }
    var winWidth
    if (window.innerWidth) {
      winWidth = window.innerWidth
    } else if ((document.body) && (document.body.clientWidth)) {
      winWidth = document.body.clientWidth
    }
    this.screen_width = (winWidth - 300) / 2 + 'px'
    this.user_login = user.isPresent()
    Changes()
  },
  ready: function () {
    console.log('life ready')
  },
  methods: {
    relocation: function () {
      map.locationagain()
    },
    refresh: function () {
      searchAround(map.getCurrentPosition())
    },
    dismissInput: function () {
      vm.isScan = false
      vm.inputScan = false
    },
    dismiss: function () {
      vm.showDial = false
    },
    dismissNotify: function () {
      vm.showNotify = false
    },
    notify: function () {
      vm.showNotify = true
    },
    dismissNew: function () {
      vm.isNewUser = false
    },
    postNum: function () {
      vm.inputScan = false
      window.location.href = '/wx/unlock.html?bn=' + decrypt.encodeNum(vm.bikeNum)
    },
    goto: function (tab) {
      if (tab === 1) {
        window.location.href = '/wx/feedback.html'
      } else if (tab === 2) {
        window.location.href = '/wx/report.html'
      }
    },
    checklength: function () {
      if (vm.bikeNum.length > 11) {
        vm.bikeNum = vm.bikeNum.slice(0, 11)
      }
    },
    inputNum: function () {
      vm.isScan = true
      var info = user.getUserInfo()
      if (info) {
        if (info.status === 10) {
          window.location.href = '/wx/ic_verify.html'
        } else if (info.status === 40) {
          window.location.href = '/wx/deposit.html'
        } else if (info.status === 20) {
          vm.inputScan = true
        }
      } else {
        window.location.href = '/wx/sign_up.html?return_to=/wx/index.html'
      }
    },
    wxscan: function () {
      vm.isScan = true
      var info = user.getUserInfo()
      if (info) {
        if (info.status === 10) {
          window.location.href = '/wx/ic_verify.html'
        } else if (info.status === 40) {
          window.location.href = '/wx/deposit.html'
        } else if (info.status === 20) {
          weixin.scanQR(this.checkAndUnlock)
        }
      } else {
        window.location.href = '/wx/sign_up.html?return_to=/wx/index.html'
      }
    },
    checkAndUnlock:function(number) {
        ajax.get({
            path: '/v1/locks/locktype?number='+number,
            success: function (data) {
                if (data.type == 5) {
                    window.location.href = 'https://ubike.cn/v1/static/download.html'
                } else {
                    window.location.href = '/wx/unlock.html?bn=' + decrypt.encodeNum(number)
                }
            },
        })
    },
  }
})

getOrderStatus()

weixin.WXinit(errorWXinit, finishWXinit)
function finishWXinit () {
  console.log('finishWXinit')
  vm.wxloaded = false
  weixin.shareCommon()
}
function errorWXinit () {
  console.log('finishWXiniterror')
  window.alert('微信初始化失败')
}

map.relocation()

map.emitter.on('location_change', function (position) {
  // console.log(vm.nearMark)
  // console.log('location_change')
  if (vm.nearMark) {
    vm.nearMark.hide()
    vm.nearMark = null
  }
  searchAround(position)
})

map.emitter.on('location_refresh', function () {
  vm.statusContent = '定位完成'
  fadeshow()
})

map.emitter.on('search_route', function () {
  vm.showMarker = false
})

map.emitter.on('clear_route', function () {
  vm.showMarker = true
})

function changeUser (userinfo) {
  user.saveUserInfo(userinfo)
}

if (user.getUserInfo()) {
  if (user.getUserInfo().userFlag === 1) {
    var aduser = user.getUserInfo()
    aduser.userFlag = 0
    changeUser(aduser)
    vm.isNewUser = true
  }
}

vm.si = setInterval(getOrderStatus, 3000)

function getOrderStatus () {
  var adorder = {}
  if (user.getOrderInfo()) {
    console.log('getOrderStatus')
    adorder = user.getOrderInfo()
    if (adorder.status === -1) {
      if (vm.getorderL) {
        vm.showTab = false
        vm.getorderL = false
        ajax.get({
          path: '/v1/order/' + adorder.orderId,
          success: function (data) {
            // console.log(data)
            vm.rundata = {'distance': data.distance, 'fee': data.amount, 'time': data.time}
            if (!vm.isScan) {
              if (data.status === 20) {
                var pathdata = {
                  orderId: adorder.orderId,
                  fee: data.amount,
                  distance: data.distance,
                  during: data.time
                }
                adorder.status = 0
                changeinfo(adorder)
                vm.running = false
                vm.showTab = true
                finish(pathdata)
              } else if (data.status === 40) {
                vm.showTab = true
                vm.running = false
                vm.getorderL = true
                window.clearInterval(vm.si)
              } else {
                vm.getorderL = true
                vm.running = true
              }
            }
          },
          error: function (msg) {
            // console.log(msg)
            adorder.status = 0
            changeinfo(adorder)
            window.clearInterval(vm.si)
            vm.getorderL = true
          }
        })
      }
    } else {
      window.clearInterval(vm.si)
    }
  } else {
    // console.log('stop order check')
    window.clearInterval(vm.si)
  }
}

function changeinfo (order) {
  user.saveOrderInfo(order)
}

function fadeshow () {
  vm.wxloaded = true
  setTimeout(fade, 2000)
}

function fade () {
  vm.wxloaded = false
}

function finish (pathdata) {
  if (parseInt(localStorage.finishOrder, 10) !== pathdata.orderId) {
    window.location.href = './check_bill.html?id=' + decrypt.encodeNum(queryString.stringify(pathdata))
  }
}

function searchAround (position) {
  vm.statusContent = '刷新车辆完成'
  fadeshow()
  // console.log('searchAround')
  // console.log('/v1/lockw/search?lon=' + position[0] + '&lat=' + position[1] + '&r=2000')
  // console.log(config.SEARCH_Url)
  ajax.get({
    path: '/v1/lockw/search?lon=' + position[0] + '&lat=' + position[1] + '&r=2000',
    domain: config.SEARCH_Url,
    async: false,
    success: function (data) {
      if (data) {
        if (data.length !== 0) {
          var bikes = data.map(function (bike) {
            if (bike) {
              return {
                // last: '/img/last.png',
                icon: '/img/icon-bike.png',
                position: [bike.longitude, bike.latitude]
              }
            }
          })
          map.showMarkers(bikes)
          vm.nearMark = map.showNear({icon: '/img/last.png', position: [data[0].longitude, data[0].latitude]})
        }
      }
    },
    error: function(msg) {
      // alert(msg)
    }
  })
}

function getAlipayStatus (data, num) {
  window.location.href = '/wx/unlock.html?bn=' + decrypt.encodeNum(num) + '&alipay=' + data
}

function Changes () {
  console.log('Changes')
  if (user.getUserInfo()) {
    ajax.get({
      path: '/v1/order/user',
      async: false,
      success: function (data) {
        // console.log(data)
        if (data) {
          this.showTab = false
          user.saveOrderInfo({orderId: data.id, timestamp: parseInt(new Date().getTime() / 1000, 10), status: -1})
        } else {
          this.showTab = true
        }
      },
      error: function(msg) {
        alert(msg)
      }
    })
  } else {
    this.showTab = true
  }
}
