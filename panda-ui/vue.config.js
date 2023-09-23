// module.exports = {
//     devServer: {
//         port: 8888
//     },
//     publicPath:process.env.NODE_ENV === 'production' ? '/panda-ui' : '/'
// }
'use strict'
const path = require('path')


function resolve(dir) {
  return path.join(__dirname, dir)
}
const proxy = require('http-proxy-middleware');
// const TARGET = 'http://www.moreluck.cc:8181';//服务器ip地址
module.exports = {
  // publicPath: '/',
  // outputDir: 'dist',
  // assetsDir: 'static',
  // publicPath: '/boss',
  // devServer: {
  //   port: '8081', // 开发模式默认端口
  //   proxy: {
  //     '/api': {
  //       target: 'http://www.moreluck.cc:8000',
  //       changeOrigin: true,
  //       secure: false,
  //       pathRewrite: {
  //         "^/api": "/"
  //       },
  //     }
  //   }

  // },
  chainWebpack(config) {

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
  }
}
