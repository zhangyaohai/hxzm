function isPresent() {
  return getToken() != null
}

function saveToken(token) {
  if (token && token != 'null') {
    console.log('save token: ', token)
    localStorage.setItem('ut', token)
  }
}

function deleteToken() {
  localStorage.removeItem('ut')
}

function getToken(token) {
  return localStorage.getItem('ut')
}

function ensurePresence() {
  if (isPresent()) {
    return true
  } else {
    location.href = '/wx/sign_up.html?return_to=' + location.pathname + location.search
    return false
  }
}

function saveOrderInfo (order) {
  localStorage.orderInfo = JSON.stringify(order)
}

function getOrderInfo () {
  return localStorage.orderInfo && JSON.parse(localStorage.orderInfo)
}

function saveUserInfo (user) {
  localStorage.user = JSON.stringify(user)
}

function getUserInfo () {
  return localStorage.user && JSON.parse(localStorage.user)
}

module.exports = {
  getOrderInfo: getOrderInfo,
  isPresent: isPresent,
  saveOrderInfo: saveOrderInfo,
  saveUserInfo: saveUserInfo,
  ensurePresence: ensurePresence,
  deleteToken: deleteToken,
  getToken: getToken,
  getUserInfo: getUserInfo,
  saveToken: saveToken
}


