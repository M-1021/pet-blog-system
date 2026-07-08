/**
 * Vuex 状态管理
 * 管理全局共享的状态：用户登录信息
 */
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: '',           // 用户 Token
    userInfo: {}         // 用户信息
  },
  getters: {
    // 是否已登录
    isLoggedIn: state => !!state.token,
    // 获取用户信息
    userInfo: state => state.userInfo
  },
  mutations: {
    // 设置 Token
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    // 设置用户信息
    SET_USER_INFO(state, info) {
      state.userInfo = info
      localStorage.setItem('userInfo', JSON.stringify(info))
    },
    // 清除登录状态
    CLEAR_AUTH(state) {
      state.token = ''
      state.userInfo = {}
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  },
  actions: {
    // 登录成功后保存信息
    login({ commit }, { token, userInfo }) {
      commit('SET_TOKEN', token)
      commit('SET_USER_INFO', userInfo)
    },
    // 退出登录
    logout({ commit }) {
      commit('CLEAR_AUTH')
    },
    // 从 localStorage 恢复登录状态（页面刷新时使用）
    restoreLogin({ commit }) {
      const token = localStorage.getItem('token')
      const userInfo = localStorage.getItem('userInfo')
      if (token) {
        commit('SET_TOKEN', token)
        if (userInfo) {
          commit('SET_USER_INFO', JSON.parse(userInfo))
        }
      }
    }
  }
})
