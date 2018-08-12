var user = require('./user')
var config = require('../config')

var locks = {}

var domain = config.API_URL

function send(method, options) {
  var path = options.path;
  var data = options.data;
  var done = options.done || function() {};
  var success = options.success || function() {};
  var error = options.error || function() {};

  var m_domain = options.domain ? options.domain : domain
  var m_token = options.token ? options.token : user.getToken()
  var async = !options.async && options.async !== undefined ? false : true

  var lock = locks[path];
  if (lock) {
    return;
  }
  locks[path] = true
  ajaxOptions = {
    type: method,
    url: m_domain + path,
    contentType: 'application/json',
    async: async,
    headers: {
      'X-TOKEN': m_token
    },
    success: function(data, status, xhr) {
      done()
      locks[path] = false;
      if (m_domain === config.SEARCH_Url) {
        return success(data, xhr);
      }
      if (data.code == 401) {
            send('GET', {
                path: '/v1/users/refreshtoken',
                success: function(data, xhr) {
                    user.saveToken(xhr.getResponseHeader('X-Token'))
                    send(method, options)
                }
            })
          return
      }
      if (data.code == 200 || data.code == 0) {
        if (data.obj) {
          return success(data.obj, xhr);
        }
        if (data.data) {
          return success(data.data, xhr);
        }
        return success(data.obj, xhr);
      } else if (data.code === 401) {
        // console.log(data)
        // user.deleteToken()
        // window.location.href = '/wx/sign_up.html?return_to=' + window.location.pathname
      } else {
        if (data.msg.indexOf('invalid user id') > -1) {
          location.replace('/wx/home.html')
          return
        }
        return error(data.msg);
      }
    },
    error: function(xhr, type) {
      done()
      locks[path] = false;
      if (type === 'timeout') {
        return error('请检查网络设置')
      } else if (type === 'abort') {
        return
      }
      return error('Error')
    }
  }

  if (data) {
    ajaxOptions.data = JSON.stringify(data)
  }
  try {
    $.ajax(ajaxOptions)
  } catch (e) {
    return error('Error')
  }
}

exports.setTitle = function(title) {
  window.document.title = title
}

exports.get = function(options) {
  send('GET', options)
}

exports.post = function(options) {
  send('POST', options)
}

exports.put = function(options) {
  send('PUT', options)
}
