var map = require('./lib/map')
var ajax = require('./lib/ajax')
var weixin = require('./lib/weixin')
var queryString = require('query-string')
var config = require('./config')

var vm = new Vue({
    el:'#app',
    data:{
        avartar: '../img/default_avartar.png',
        orderInfo:null,
        show: false,
        gender: 1
    },
    created: function(){

        var param = queryString.parse(location.search)
        if (!!param.orderId) {
            this.getOrderDetail(parseInt(param.orderId))
            this.getLineDetail(parseInt(param.orderId))
        }
    },
    methods:{
        getOrderDetail: function(orderId){
            ajax.get({
                path:'/v1/order/' + orderId + '/share',
                success: function(data){
                    vm.orderInfo = data
                    var gender = data.gender
                    if (gender === 1) {
                        vm.avartar = '../img/avartar_male.png'
                    } else if (gender === 2) {
                        vm.avartar = '../img/avartar_female.png'
                    }
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
                    var markers = [{icon:'../img/icon-qi.png', position: lineArr[0]}, {icon: '../img/icon-zhong.png', position: lineArr[lineArr.length - 1]}]

                    map.polyline(lineArr)
                    map.showMarkersRouter(markers)
                }
            })
        },
        formatDistance: function(distance){
            return (parseFloat(distance) * 1000).toFixed(1)
        },
        formatTime: function(timestamp) {
            timestamp = new Date(timestamp)
            var date = (timestamp.getFullYear()) + "-" +
                this.fillZero(timestamp.getMonth() + 1) + "-" +
                this.fillZero(timestamp.getDate()) + " " +
                this.fillZero(timestamp.getHours()) + ":" +
                this.fillZero(timestamp.getMinutes());
            return date
        },
        fillZero: function(number) {
            return ~~number < 10 ? '0' + number : number;
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