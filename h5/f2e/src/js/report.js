var user = require('./lib/user')
var ajax = require('./lib/ajax')
var upload = require('./lib/upload')
var weixin = require('./lib/weixin')

if (!user.ensurePresence()) {
  return
} 

var vm = new Vue({
  el: '#app',
  data: {
    showTxt: true,
    inputN: null,
    notetext: '',
    wxloaded: false,
    option: [],
    sel_opts: [],
    checkedRules: ['违规停车','忘记锁车但是被成功找回','忘记锁车导致车辆丢失','加装私锁','交警阻拦弃车','非法移动买车'],
    uploadimg: false,
    uploadProcess: 0,
    imgSrc: null,
    image: null
  },
  ready: function () {
    $('#uploaderInput').on("change", function(e){
      vm.uploadimg = true
      $('#myimg').addClass('weui-uploader__file_status')
      vm.uploadProcess = 10
      var src, url = window.URL || window.webkitURL || window.mozURL, files = e.target.files;
      if(files.length){
        var reader = new FileReader();
        reader.readAsDataURL(files[0]); 
        var up_file = this.files[0]
        reader.onload =function(e){
          vm.image = 'url(' + e.target.result + ')'
          $('#myimg').css('background-image', vm.image)
          $('#myimg').css('background-size', '100% 100%')
          vm.uploadProcess = 40
          upload.upload(up_file, imgCB)                             
        };  
      }
    });
  },
  methods: {
    inputNum: function() {
      vm.showTxt = false
    },
    getlength: function(txt) {
      var ll = 0
      var arr = txt.split("\n")
      for (single in arr) {
        ll += arr[single].length
      }
      return 142 - ll - arr.length*2
    },
    scan: function () {
      vm.showTxt = false
      weixin.scanQR(callback)
    },
    checklength: function () {
      if (vm.inputN.length > 11) {
        vm.inputN = vm.inputN.slice(0, 11)
      }
    },
    getChecked: function (id) {
      var f = false
      if($.inArray(id, vm.sel_opts) > -1) {
      } else {
        f = 1
      }
      return f
    },
    checked: function (id) {
      $('.weui-check').each(function(index){
        if (index === id) {
          if (String($(this).attr("checked")) == 'true') {
            $(this).prop("checked", false)
          } else {
            $(this).prop("checked", true)
          }
          return
        }
      })
      if($.inArray(id, vm.sel_opts) < 0) {
        // console.log('window')
        vm.sel_opts.push(id)
      }else {
        // console.log('service')
        vm.sel_opts.splice($.inArray(id, vm.sel_opts),1)
      }
    },
    postContent: function (){
      if( vm.inputN === null || vm.inputN.length !== 11 || vm.inputN.length !== 10){
        window.alert('请扫描或输入正确的车辆编号')
        return
      }
      if( vm.sel_opts.length == 0 ){
        window.alert('请选择违规项')
        return
      }
      var txt = []
      vm.sel_opts.map(function (single) {
        txt.push(vm.checkedRules[single])
      })
      var playload = {
        'behavior': txt.join('_'),
        'images': vm.imgSrc,
        'number': vm.inputN,
        'remark': vm.notetext
      }
      ajax.post ({
        path: '/v1/report',
        data: playload,
        async: false,
        success: function (data) {
          window.alert('提交成功')
          window.location.href = './home.html'
        },
        error: function (msg) {
          window.alert(msg)
        }
      })
    }
  }
})

weixin.WXinit(errorWXinit, finishWXinit)
function finishWXinit () {
  console.log('finishWXinit')
  vm.wxloaded = true
}
function errorWXinit () {
  console.log('finishWXinit')
  window.alert('微信初始化失败')
}

function callback (num) {
  vm.inputN = num
}

function imgCB (num, finish) {
  vm.uploadProcess = num
  if (num == 100) {
    $('#myimg').removeClass('weui-uploader__file_status')
    if (finish == 'error'){
      vm.imgSrc = null
    } else {
      vm.imgSrc = finish
    }
  }
}

