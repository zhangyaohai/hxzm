var EventEmitter = require('events')
var emitter = new EventEmitter()

var map = new AMap.Map('map')

var currentPosition = []
var showingRoute = true
var currentLocationMarker
var near = null

var geolocation = new AMap.Geolocation({
  enableHighAccuracy: true, //是否使用高精度定位，默认:true
  timeout: 10000, //超过10秒后停止定位，默认：无穷大
  maximumAge: 3000, //定位结果缓存0毫秒，默认：0
  convert: true, //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
  showButton: false, //显示定位按钮，默认：true
  buttonPosition: 'LB', //定位按钮停靠位置，默认：'LB'，左下角
  buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
  showMarker: true, //定位成功后在定位到的位置显示点标记，默认：true
  showCircle: false, //定位成功后用圆圈表示定位精度范围，默认：true
  panToLocation: true, //定位成功后将定位到的位置作为地图中心点，默认：true
  zoomToAccuracy: true //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
})
map.addControl(geolocation)

var walking = new AMap.Walking({
  map: map,
  hideMarkers: true,
  panel: 'route'
})

AMap.event.addListener(geolocation, 'complete', function(data) {
  currentPosition = [data.position.lng, data.position.lat]
  map.setZoom(16)
})

AMap.event.addListener(geolocation, 'error', function(data) {
  window.alert('error')
  map.setZoom(16)
  console.log(data)
})

map.on('moveend', function() {
  if (showingRoute) {
    return
  }
  var center = map.getCenter()
  currentPosition = [center.lng, center.lat]
  emitter.emit('location_change', currentPosition)
})

map.on('click', function() {
  if (showingRoute) {
    clearRoute()
    map.setZoomAndCenter(17, currentPosition)    
  }
})

function showRouteToPosition(position) {
  walking.clear()
  walking.search(currentPosition, position)
  // map.setFitView()

  emitter.emit('search_route')
  currentLocationMarker && map.remove([currentLocationMarker])
  currentLocationMarker = new AMap.Marker({
    map: map,
    position: currentPosition,
    icon: new AMap.Icon({
        size: new AMap.Size(60, 60), 
        image: "/img/icon-current-location.png",
        imageOffset: new AMap.Pixel(0, 0),
        imageSize: new AMap.Size(22, 30)
    })
  })
  showingRoute = true
}

function clearRoute() {
  walking.clear()
  currentLocationMarker && map.remove([currentLocationMarker])
  emitter.emit('clear_route')
  showingRoute = false
}

exports.emitter = emitter

exports.relocation = function() {
  clearRoute()
  geolocation.getCurrentPosition()
}

exports.locationagain = function() {
  clearRoute()
  geolocation.getCurrentPosition()
  emitter.emit('location_refresh')
}

exports.getCurrentPosition = function() {
  return currentPosition
}

exports.showMarkers = function(markers) {
  markers.forEach(function(marker) {
    var m = new AMap.Marker({
      map: map,
      position: marker.position,
      icon: new AMap.Icon({
        size: new AMap.Size(60, 53),
        image: marker.icon,
        imageOffset: new AMap.Pixel(0, 21),
        imageSize: new AMap.Size(32, 32)
      })
    })
    m.on('click', function() {
      showRouteToPosition(marker.position)
    })
  })
}

exports.showMarkersRouter = function(markers) {
  markers.forEach(function(marker) {
    var m = new AMap.Marker({
      map: map,
      position: marker.position,
      icon: new AMap.Icon({
        size: new AMap.Size(20, 30),
        image: marker.icon,
        imageOffset: new AMap.Pixel(0, 0),
        imageSize: new AMap.Size(20, 30)
      })
    })
  })
}

exports.showNear = function (marker) {
  if (near != null) {
    near.hide()
  }
  var icon = new AMap.Icon({
    size: new AMap.Size(72, 64),
    image: marker.icon,
    imageOffset: new AMap.Pixel(2, 0),
    imageSize: new AMap.Size(28, 21)
  })
  var m = new AMap.Marker({
    map: map,
    position: marker.position,
    icon: icon
  })
  m.on('click', function() {
    showRouteToPosition(marker.position)
  })
  near = m
  return m
}

exports.polyline = function(lineArr) {
  if (lineArr.length <= 0) {
    exports.relocation()
    return
  }
  var polyline = new AMap.Polyline({
    path: lineArr,
    strokeColor: "#02c66d",
    strokeOpacity: 1,
    strokeWeight: 4,
    strokeStyle: "dashed",
    geodesic: true,
    strokeDasharray: [10, 5]
  })
  polyline.setMap(map)
  map.setCenter(lineArr[~~(lineArr.length / 2)])
  var zoom = 19 - ~~(lineArr.length / 50)
  map.setZoom(zoom)
}
