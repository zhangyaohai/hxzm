module.exports = {
  set: function(name, value, expHour, domain, path) {
    document.cookie = name + "=" + encodeURIComponent(value == undefined ? "" : value) + (expHour ? "; expires=" + new Date(new Date().getTime() + (expHour - 0) * 3600000).toUTCString() : "") + "; domain=" + (domain ? domain : document.domain) + "; path=" + (path ? path : "/");
  },
  get: function(name) {
    return document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)")) == null ? null : decodeURIComponent(RegExp.$2);
  },
  remove: function(name) {
    if (this.get(name) != null) this.set(name, null, -1);
  }
}