/**
 * Created by CLiang on 2017/1/24.
 */
var user = require('./lib/user')
var ajax = require('./lib/ajax')
var queryString = require('query-string')
var decrypt = require('./lib/decrypt')

new Vue({
    el: '#app',
    data:{
        text:'疯狂开锁中...',
        result: 1,
        urlParam: queryString.parse(location.search),
        orderId:0,
        failText:''
    },
    created: function () {
        var vm = this
        var rfUrl = document.referrer
        if (!rfUrl) {
            location.replace('/wx/home.html')
            return
        } else if (rfUrl.indexOf('/wx/home.html') == -1) {
            location.replace('/wx/home.html')
            return
        }
        if (vm.urlParam.bn) {
            var number = vm.urlParam.bn
            ajax.post({
                path: '/v1/bikes/unlock',
                data: { number: decrypt.decodeNum(number), sourceType:1 },
                success: function(data) {
                    vm.orderId = data.id
                    vm.getLockStatus(data.id)
                },
                error: vm.unlockFail
            })
        }
    },
    methods:{
        getLockStatus:function(orderId){
            var vm = this
            ajax.get({
                path: '/v1/order/' + orderId + '/status',
                success: function (data) {
                    if(data.status === 10 || data.status === 20) {
                        user.saveOrderInfo({orderId: orderId, timestamp: parseInt(new Date().getTime() / 1000, 10), status: -1})
                        vm.text = '开锁成功'
                        vm.result = 2
                        setTimeout(function(){
                            location.replace('/wx/riding_end.html?id='+orderId)
                        }, 1000)
                        return
                    }
                    setTimeout(function(){
                        vm.getLockStatus(orderId)
                    }, 1000)
                },
                error: vm.unlockFail
            })
        },
        unlockFail:function(msg){
            this.text = '开锁失败'
            this.failText = msg
            this.result = 0
            setTimeout(function(){
                location.replace('./home.html')
            }, 5000)
        }
    }
})