var map = require('./lib/map')
var user = require('./lib/user')
var ajax = require('./lib/ajax')
var weixin = require('./lib/weixin')
var queryString = require('query-string')
var config = require('./config')

if (!user.ensurePresence()) {
    return
}

var vm = new Vue({
    el:'#app',
    data:{
        user: user.getUserInfo(),
        avartar: '../img/default_avartar.png',
        orderInfo:null,
        wxloaded: false,
        show: false
    },
    created: function(){
        if (this.user.gender === 1) {
            this.avartar = '../img/avartar_male.png'
        } else if (this.user.gender === 2) {
            this.avartar = '../img/avartar_female.png'
        }
        var param = queryString.parse(location.search)
        if (!!param.orderId) {
            this.getOrderDetail(param.orderId)
            this.getLineDetail(param.orderId)
        }
    },
    methods:{
        getOrderDetail: function(orderId){
            ajax.get({
                path:'/v1/order/' + orderId,
                success: function(data){
                    console.log(data)
                    vm.orderInfo = data
                }
            })
        },
        getLineDetail: function(orderId) {
            ajax.get({
                path: '/v1/lines/' + orderId + '/order',
                success: function(data) {
                    var lineArr = []
                    $(data).each(function(ind, item){
                        var line = item.split(' ')
                        var latlng = [parseFloat(line[0]), parseFloat(line[1])]
                        lineArr.push(latlng)
                    })
                    var markers = [{icon:'../img/icon-qi.png', position: lineArr[0]}]
                    console.log(lineArr)
                    console.log(markers)
                    if(lineArr.length !== 0) {
                        map.polyline(lineArr)
                        map.showMarkersRouter(markers)
                    }
                }
            })
        },
        formatDistance: function(distance){
            return (parseFloat(distance) * 1000).toFixed(1)
        },
        formatTime: function(timestamp) {
            timestamp = new Date(timestamp)
            var date = (timestamp.getFullYear()) + "-" +
                (timestamp.getMonth() + 1) + "-" +
                (timestamp.getDate()) + " " +
                (timestamp.getHours()) + ":" +
                (timestamp.getMinutes());
            return date
        },
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
  vm.wxloaded = true
  var param = queryString.parse(location.search)
  weixin.share('优拜单车 让骑行更简单', '优拜单车是短途出行的最佳选择，转向助力，随意变速，更多功能等你来体验', 'https://' + config.SITE_URL + '/wx/share.html?orderId='+param.orderId, 'https://' + config.SITE_URL + '/img/share_icon.png', successCB, failCB)
}

function errorWXinit () {
  console.log('finishWXinit')
  window.alert('微信初始化失败')
}

function successCB () {}

function failCB () {}
