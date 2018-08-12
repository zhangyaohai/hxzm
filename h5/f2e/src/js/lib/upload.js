var ajax = require('./ajax')
var md5 = require('blueimp-md5')

const bucket = 'ubike-bucket'
const region = 'oss-cn-shanghai'
const url = 'http://ubike-bucket.oss-cn-shanghai.aliyuncs.com/'

function upload (file, cb) {
  ajax.get({
    path: '/v1/oss/token',
    // domain: 'http://preapi.xiaomayundong.com/xmyd-api-web/v1', // need to change to api server
    async: false,
    success: function (data) {
      cb(60, true)
      uploadImg(data.credentials.accessKeyId, data.credentials.accessKeySecret, data.credentials.securityToken, file, cb)
    },
    error: function (msg) {
      cb(100, 'error')
      // window.alert(msg)
    }
  })
}

function uploadImg (accessKeyId, accessKeySecret, sts, file, cb) {
  var client = new OSS.Wrapper({
    region: region,
    accessKeyId: accessKeyId,
    accessKeySecret: accessKeySecret,
    stsToken: sts,
    bucket: bucket
  })

  var dt = parseInt(new Date().getTime() / 1000, 10)
  var store_key = md5(dt + file.path) + '.' + file.type.split('/')[1]
  client.multipartUpload(store_key, file).then(function (result) {
    cb(100, url + store_key)
    // console.log(result)
  }).catch(function (err) {
    cb(100, 'error')
    console.log(err)
  })
}

module.exports = {
  upload: upload,
  uploadImg: uploadImg
}
