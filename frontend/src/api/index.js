/**
 * API 请求封装模块
 * 使用 axios 封装所有后端 API 接口调用
 * 自动携带 Token，统一处理错误
 */
import axios from 'axios'
import { Message } from 'element-ui'
import router from '../router'

// 创建 axios 实例
const request = axios.create({
  baseURL: '/api',   // 基础路径（通过 vue.config.js 代理到后端）
  timeout: 10000     // 请求超时时间：10秒
})

// 请求拦截器：自动在请求头中添加 Token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

// 响应拦截器：统一处理错误
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      Message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    if (error.response && error.response.status === 401) {
      Message.error('请先登录')
      router.push('/login')
    } else {
      Message.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

// ==================== 用户相关接口 ====================
export const userApi = {
  login: (data) => request.post('/user/login', data),
  register: (data) => request.post('/user/register', data),
  getInfo: () => request.get('/user/info'),
  updateInfo: (data) => request.put('/user/info', data),
  getList: (params) => request.get('/user/list', { params }),
  deleteUser: (id) => request.delete('/user/' + id)
}

// ==================== 文章相关接口 ====================
export const articleApi = {
  getList: (params) => request.get('/article/list', { params }),
  getDetail: (id) => request.get('/article/detail/' + id),
  create: (data) => request.post('/article', data),
  update: (data) => request.put('/article', data),
  delete: (id) => request.delete('/article/' + id),
  toggleLike: (id) => request.post('/article/like/' + id),
  getMyList: () => request.get('/article/my')
}

// ==================== 分类相关接口 ====================
export const categoryApi = {
  getList: () => request.get('/category/list'),
  create: (data) => request.post('/category', data),
  update: (data) => request.put('/category', data),
  delete: (id) => request.delete('/category/' + id)
}

// ==================== 评论相关接口 ====================
export const commentApi = {
  getList: (articleId) => request.get('/comment/list/' + articleId),
  create: (data) => request.post('/comment', data),
  delete: (id) => request.delete('/comment/' + id)
}

// ==================== 帖子相关接口 ====================
export const postApi = {
  getList: (params) => request.get('/post/list', { params }),
  getDetail: (id) => request.get('/post/detail/' + id),
  create: (data) => request.post('/post', data),
  delete: (id) => request.delete('/post/' + id),
  getReplies: (postId) => request.get('/post/reply/' + postId),
  addReply: (data) => request.post('/post/reply', data)
}

// ==================== 举报相关接口 ====================
export const reportApi = {
  create: (data) => request.post('/report', data),
  getList: (params) => request.get('/report/list', { params }),
  handle: (id, action) => request.put('/report/handle/' + id, null, { params: { action } })
}
