/**
 * Vue 应用入口文件
 * 初始化 Vue 实例，注册 Element UI 组件库和路由
 */
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/global.css'

// 注册 Element UI 组件库（全局可用）
Vue.use(ElementUI)

// 关闭生产环境提示
Vue.config.productionTip = false

// 创建 Vue 实例
new Vue({
  router,  // 路由
  store,   // 状态管理
  render: h => h(App)
}).$mount('#app')
