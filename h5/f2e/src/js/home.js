/**
 * Created by CLiang on 2017/1/18.
 */
var user = require('./lib/user')
var ajax = require('./lib/ajax')
var weixin = require('./lib/weixin')
var queryString = require('query-string')
var decrypt = require('./lib/decrypt')
var config = require('./config')

var vueModel = new Vue({
    el:'#app',
    data:{
        isShowModal:true,
        modal:{
            realName: location.hash === 'realName',
            login: location.hash === 'login',
            charge: location.hash === 'charge',
            depositPay: location.hash === 'depositPay',
            firstUseCar: location.hash === 'firstUseCar',
            downloadTip: location.hash === 'downloadTip',
        },
        charge:{
            money:[[10, 20], [50, 100]],
            amount:10,
            channel: 1
        },
        deposit:{
            money: config.deposit,
            channel: 1
        },
        user:{
            avatar: '../img/default_avartar.png',
            userName:'',
            phone:'',
            randCode:'',
            face:'',
            gender:'',
            realName:'',
            icCardNo:'',
            type:1,
        },
        vcode:{
            second: 60,
            btnText:'获取验证码',
            disabled:false
        },
        scanBtn:true,
        userInfo: user.getUserInfo(),
        queryParam: queryString.parse(window.location.search),
        bikeNum:'',
        loading: false,
    },
    created:function(){
        var vm = this
        var number = vm.queryParam.number

        var weixinUser = weixin.getUserInfo()
        if (weixin.isWeixinBrowser()) {
            if (vm.queryParam.code) {
                weixin.storeOpenId(vm.queryParam.code, vm.initUserByWx)
            } else {
                if (!localStorage.OID || !weixinUser) {
                    window.location.href = weixin.redirectCode(location.pathname + location.search, 'snsapi_userinfo')
                    return
                }
            }
        }

        if (!!weixinUser) {
            this.user.avatar = weixinUser.avatar
            this.user.userName = weixinUser.nickname
            this.user.gender = weixinUser.sex
        }

        setTimeout(function(){
            vm.getOrder()
        }, 1500)

        if (!!number) {
            vm.checkUser()
        }
        if (user.getToken()) {
            vm.initUserInfo()
        }
    },
    methods:{
        isShow:function(modal) {
            return this.modal[modal] && this.isShowModal
        },
        showModal:function(modal) {
            var last
            $.each(this.modal, function(k, item){
                if (item) {
                    last = k
                    return false
                }
            })
            if (last) this.modal[last] = false
            this.modal[modal] = true
            this.isShowModal = true
        },
        wxscan:function(){
            var that = this
            this.scanBtn = true
            this.nextAction = this.wxscan
            this.isShowModal = false
            this.checkUser(function(){
                weixin.scanQR(that.checkAndUnlock)
            })
        },
        getCode: function() {
            var vm = this
            if (vm.user.phone.length !== 11) {
                window.alert('手机号错误!')
                return
            }
            _hmt.push(['_trackEvent', 'ubike_button', 'click', 'validatecode'])
            ajax.post({
                path:'/v1/validatecode',
                data: {
                    phone: vm.user.phone,
                    module: 'sign' + '_up' // 和js文件名相同的时候，会被全局替换掉，所以使用+号连接
                },
                success: function(data) {
                    vm.codeId = data
                },
                error: function(msg) {
                    console.log(msg)
                }
            })
            vm.vcode.disabled = true
            vm.timer(vm.vcode.second, function(second){
                if (second <= 0) {
                    vm.vcode.disabled = false
                    vm.vcode.btnText = '获取验证码'
                    return false
                }
                vm.vcode.btnText = second + '秒后重发'
                return true
            })
        },
        login: function(){
            var vm = this
            if (vm.user.phone.length !== 11) {
                window.alert('请输入正确的手机号码')
                return
            }
            if (vm.user.randCode.length !== 4) {
                window.alert('请输入正确的验证码')
                return
            }
            this.loading = true
            ajax.post({
                path: '/v1/register',
                data: vm.user,
                success: function(data, xhr) {
                    vm.userInfo = data
                    if (data.userFlag == 1) {
                        vm.userInfo.noOrder = 10
                    }

                    user.saveToken(xhr.getResponseHeader('X-Token'))
                    vm.checkUser(vm.nextAction)
                },
                error: function(msg) {
                    alert(msg)
                },
                done:function(){
                    vm.loading = false
                }
            })
        },
        verify: function() {
            var vm = this
            _hmt.push(['_trackEvent', 'ubike_button', 'click', 'verifyIC', user.getUserInfo().id])
            vm.loading = true
            ajax.put({
                path: '/v1/users',
                data: vm.user,
                success: function() {
                    vm.userInfo.status = 40
                    vm.showModal('firstUseCar')
                    vm.userInfo.noOrder = 11
                },
                error: function(msg) {
                    alert(msg)
                },
                done:function(){
                    vm.loading = false
                }
            })
        },
        getOrder:function(){
            if (!this.userInfo) {
                return
            }
            if (this.userInfo.status != 20 && this.userInfo.status != 40) {
                return
            }
            ajax.get({
                path: '/v1/order/user',
                success: function (data) {
                    if (!!data) {
                        user.saveOrderInfo({orderId: data.id, timestamp: parseInt(new Date().getTime() / 1000, 10), status: -1})
                        location.href = './riding_end.html?id=' + data.id
                    }
                },
                error: function(msg) {
                    alert(msg)
                }
            })
        },
        checkUser:function(callback){
            var vm = this
            if (!user.getToken()) {
                vm.showModal('login')
            } else if (vm.userInfo.status == 10) {
                vm.showModal('realName')
            } else if (vm.userInfo.status == 40) {
                if (vm.userInfo.noOrder == 0) {
                    vm.showModal('depositPay')
                } else if (vm.userInfo.noOrder == 10) {
                    vm.showModal('firstUseCar')
                } else if (vm.userInfo.noOrder == 11) {
                    callback()
                } else {
                    ajax.get({
                        path: '/v1/order/has/finished',
                        success: function (data) {
                            if(~~data > 0) {
                                vm.showModal('depositPay')
                            } else {
                                callback()
                            }
                        },
                        error: function (msg) {
                            alert(msg)
                        }
                    })
                }
            } else if (vm.userInfo.status == 20 && callback) {
                if (vm.userInfo.balance <= 0) {
                    vm.showModal('charge')
                } else {
                    callback()
                }
            }
        },
        timer: function(second, callback) {
            var vm = this
            var goOn = callback(second)
            if (goOn) {
                setTimeout(function () {
                    second = second - 1
                    vm.timer(second, callback)
                }, 1000)
            }
        },
        inputNumber:function() {
            var number = this.bikeNum + ''
            if (number.length >= 11) {
                this.bikeNum = number.substr(0, 11)
            }
        },
        goToProfile:function(){
            if (!user.getToken()) {
                this.nextAction = function(){
                    location.href = './profile.html'
                }
                this.showModal('login')
            } else {
                location.href = './profile.html'
            }
        },
        inputBtn:function(){
            var that = this
            this.nextAction = function(){
                that.isShowModal = false
                that.$nextTick(function () {
                    that.inputBtn()
                })
            }
            this.checkUser(function(){
                that.scanBtn = false
                that.$nextTick(function () {
                    $('#bikeNumber').trigger('focus')
                    $('#bikeNumber').trigger('touchstart')
                    document.querySelector('#bikeNumber').focus()
                })
            })
        },
        unlock:function(){
            if (!this.scanBtn) {
                var number = this.bikeNum + ''
                if (number.length != 11) {
                    alert('请输入正确的车辆编号')
                    return
                }
                this.checkAndUnlock(number)
            } else if (!this.queryParam.number) {
                this.wxscan()
            } else if (!!this.queryParam.number) {
                window.location.replace('/wx/unlock.html?bn=' + decrypt.encodeNum(this.queryParam.number))
            }
        },
        checkAndUnlock:function(number) {
            var that = this
            ajax.get({
                path: '/v1/locks/locktype?number='+number,
                success: function (data) {
                    if (data.type == 5) {
                        that.scanBtn = true
                        that.showModal('downloadTip')
                        // window.location.href = 'https://ubike.cn/v1/static/download.html'
                    } else {
                        window.location.href = '/wx/unlock.html?bn=' + decrypt.encodeNum(number)
                    }
                },
                error: function(msg) {
                    alert(msg)
                }
            })
        },
        goToDownload:function(){
            window.location.href = 'https://ubike.cn/v1/static/download.html'
        },
        initUserByWx:function(data) {
            data = data || weixin.getUserInfo()
            if (!!data) {
                this.user.avatar = data.avatar
                this.user.userName = data.nickname
                this.user.face = data.avatar
                this.user.gender = data.sex
            }
        },
        payDeposit: function() {
            var that = this
            if (this.loading == true) {
                return
            }
            this.loading = true
            _hmt.push(['_trackEvent', 'ubike_button', 'click', 'payDeposit', user.getUserInfo().id])
            setTimeout(function () {
                weixin.commonPay(that.deposit.channal, that.deposit.money, true, callback)
            }, 1)
        },
        chargeBalance: function(){
            var that = this
            if (this.charge.amount < 1) {
                window.alert('充值金额必须大于1元')
                return
            }
            if (this.loading == true) {
                return
            }
            this.loading = true
            _hmt.push(['_trackEvent', 'ubike_button', 'click', 'pay', user.getUserInfo().id])
            setTimeout(function () {
                weixin.commonPay(that.deposit.channal, that.charge.amount * 100, false, callback)
            }, 1)
        },
        initUserInfo:function(){
            var that = this
            ajax.get({
                path: '/v1/users/reload',
                success: function(data) {
                    that.userInfo = data
                },
                error: function(msg) {
                    alert(msg)
                }
            })
        },
        nextAction:null
    },
    watch:{
        'userInfo':{
            handler:function(val) {
                console.log(JSON.stringify(val))
                localStorage.user = JSON.stringify(val)
            },
            deep:true
        }
    }
})


weixin.WXinit(errorWXinit, finishWXinit)
function finishWXinit () {
    console.log('finishWXinit')
    vueModel.wxloaded = false
    weixin.shareCommon()
}
function errorWXinit () {
    console.log('finishWXiniterror')
    window.alert('微信初始化失败')
}

var callback = {
    endLoading: function(){
        vueModel.loading = false
    },
    success: function(orderId) {
        var amount = vueModel.userInfo.status == 20 ? vueModel.charge.amount : vueModel.deposit.money
        var playload = {
            amount: amount,
            channel: vueModel.deposit.channel,
            type: 1,
            orderId: orderId
        }
        ajax.post({
            path: '/v1/balances/deposit',
            data: playload,
            success: function(data) {
                console.log(data)
            },
            error: function(msg) {
                alert(msg)
            }
        })
        vueModel.isShowModal = false
        if (vueModel.userInfo.status == 20) {
            vueModel.userInfo.balance = parseFloat(vueModel.userInfo.balance) + amount
        } else if (vueModel.userInfo.status == 40) {
            vueModel.userInfo.status=20
        }
        console.log('success')
        vueModel.loading = false
        if (vueModel.nextAction) {
            console.log('next action')
            vueModel.nextAction()
        }
    },
    fail: function(content) {
        vueModel.loading = false
        console.log('fail')
    },
    cancel: function() {
        vueModel.loading = false
        console.log('cancel')
    }
}