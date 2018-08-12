/**
 * Created by CLiang on 2017/1/18.
 */
var ajax = require('./lib/ajax')
var queryString = require('query-string')
var user = require('./lib/user')
var weixin = require('./lib/weixin')
var config = require('./config')

var vueModel = new Vue({
    el:'#app',
    data:{
        first:false,
        riding:true,
        orderInfo:{},
        count: 0,
        orderId: queryString.parse(location.search).id,
        userInfo: user.getUserInfo(),
    },
    created: function(){
        if (this.userInfo.noOrder == 0) {
        } else if (~~this.userInfo.noOrder > 10) {
            this.first = true
        } else {
            this.checkHasFinished()
        }
        this.getOrderStatus()
    },
    methods:{
        getOrderStatus: function() {
            var that = this
            if (!that.orderId) {
                return
            }
            ajax.get({
                path: '/v1/order/'+that.orderId,
                success: function (data) {
                    that.orderInfo = data
                    if (!!data && data.status === 20) {
                        that.riding = false
                        ajax.setTitle('骑行结束')
                        that.userInfo.noOrder = 0
                        localStorage.user = JSON.stringify(that.userInfo)
                    } else {
                        if (that.count > 24) {
                            location.reload()
                        }
                        that.count += 1
                        setTimeout(that.getOrderStatus, 3000)
                    }
                },
                error: function (msg) {
                    console.log(msg)
                    setTimeout(that.getOrderStatus, 3000)
                }
            })
        },
        checkHasFinished: function(){
            var that = this
            ajax.get({
                path: '/v1/order/has/finished',
                success: function (data) {
                    that.first = ~~data <= 0
                },
                error: function (msg) {
                    console.log(msg)
                }
            })
        }
    }
})

weixin.WXinit(errorWXinit, finishWXinit)
function finishWXinit () {
    console.log('finishWXinit')
    var param = queryString.parse(location.search)
    weixin.share('优拜单车 让骑行更简单', '优拜单车是短途出行的最佳选择，转向助力，随意变速，更多功能等你来体验', 'https://' + config.SITE_URL + '/wx/share.html?orderId='+vueModel.orderId, 'https://' + config.SITE_URL + '/img/share_icon.png', successCB, failCB)
}

function errorWXinit () {
    console.log('finishWXinit')
    window.alert('微信初始化失败')
}

function successCB () {}

function failCB () {}