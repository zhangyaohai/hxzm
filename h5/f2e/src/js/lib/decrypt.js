module.exports = {
  encodeNum: function (str) {
    return window.btoa(str)
  },
  decodeNum: function (str) {
    return window.atob(str)
  }
}
