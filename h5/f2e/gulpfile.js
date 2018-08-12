var fs = require('fs');
var path = require('path');
var gulp = require('gulp');
var browserify = require('gulp-browserify');
var jade = require('gulp-jade');
var RevAll = require('gulp-rev-all');
var revReplace = require("gulp-rev-replace");
var browserSync = require('browser-sync').create();
var stylus = require('gulp-stylus');
var nib = require('nib');
var _ = require('lodash');
var gutil = require('gulp-util');
var minimist = require('minimist');

// 默认development环境
var knowOptions = {
  string: 'env',
  default: {
    env: process.env.NODE_ENV || 'development'
  }
};
var options = minimist(process.argv.slice(2), knowOptions);
// 生成filename文件，存入string内容
function string_src(filename, string) {
  var src = require('stream').Readable({ objectMode: true })
  src._read = function () {
    this.push(new gutil.File({ cwd: "", base: "", path: filename, contents: new Buffer(string) }))
    this.push(null)
  }
  return src
}

gulp.task('constants', function () {
  // 读入config.json文件
  var myConfig = require('./config.json');
  // 取出对应的配置信息
  var envConfig = myConfig[options.env];
  var conConfig = 'module.exports = { API_URL: "' + envConfig.apiUrl + '", SITE_URL: "' + envConfig.siteUrl + '", SEARCH_Url: "' + envConfig.searchUrl + '", deposit:'+envConfig.deposit+' }'

  console.log(conConfig)
  // 生成config.js文件
  return string_src("config.js", conConfig)
      .pipe(gulp.dest('src/js/'))
});

gulp.task('browserify', function() {
  gulp.src('src/js/static/*.js')
   .pipe(gulp.dest('build/js'))
  return gulp.src('src/js/*.js')
    .pipe(browserify({
      insertGlobals: false,
      debug: false
    }))
    .pipe(gulp.dest('build/js'))
});

gulp.task('stylus', function() {
  return gulp.src(['src/css/*.styl'])
    .pipe(stylus({
      compress: false,
      use: nib(),
      'import': ['nib']
    }))
    .pipe(gulp.dest('build/css'))
    .pipe(browserSync.stream());
});

gulp.task('vendor', function() {
  return gulp.src('src/vendor/**/*')
    .pipe(gulp.dest('build/vendor/'))
})


gulp.task('img', function() {
  return gulp.src('src/img/**/*')
    .pipe(gulp.dest('build/img/'))
})


gulp.task('jade', function() {
  return gulp.src('src/template/*.jade')
    .pipe(jade({
      pretty: true
    }))
    .pipe(gulp.dest('build/wx'));
});


gulp.task('watch', function() {
  gulp.watch(['src/js/**/*'], ['browserify']);
  gulp.watch(['src/template/**/*'], ['jade']);
  gulp.watch(['src/css/**/*'], ['stylus']);
  gulp.watch(['src/img/**/*'], ['img']);
});


gulp.task('serve', function() {
  browserSync.init({
    open: false,
    port: 80,
    server: {
      baseDir: "build",
    }
  });
  gulp.watch(['build/wx/**/*']).on('change', function() {
    setTimeout(browserSync.reload, 1000)
  });
});

// build for test or production

gulp.task('revision', ['build'], function() {
  var rev = new RevAll();
  return gulp.src('build/**/*.+(js|css|jpg|jpeg|ico|png|gif|svg)')
    .pipe(rev.revision())
    .pipe(gulp.dest('final/'))
    .pipe(rev.manifestFile())
    .pipe(gulp.dest('final/'));
})

gulp.task('revreplace', ['revision'], function() {
  var manifest = gulp.src('final/rev-manifest.json');
  return gulp.src('build/wx/**/*')
    .pipe(revReplace({
      manifest: manifest,
      replaceInExtensions: ['.html']
    }))
    .pipe(gulp.dest('final/wx'));
});

gulp.task('build', ['constants', 'browserify', 'stylus', 'jade', 'vendor', 'img']);
gulp.task('preview', ['constants', 'build', 'watch', 'serve'])
