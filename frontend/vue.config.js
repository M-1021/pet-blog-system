/**
 * Vue CLI 配置文件
 * 配置开发服务器端口和代理
 */
module.exports = {
  devServer: {
    port: 8081,  // 前端开发服务器端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端 API 地址
        changeOrigin: true  // 开启代理，解决跨域问题
      }
    }
  }
}
