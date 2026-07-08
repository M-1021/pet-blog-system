<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 slot="header">用户登录</h2>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width:100%">登 录</el-button>
        </el-form-item>
        <el-form-item>
          <span class="tip">还没有账号？<router-link to="/register">立即注册</router-link></span>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { userApi } from '../api'
export default {
  name: 'Login',
  data() {
    return {
      form: { username: '', password: '' },
      loading: false,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    async handleLogin() {
      try {
        await this.$refs.loginForm.validate()
        this.loading = true
        const res = await userApi.login(this.form)
        // 后端 data 直接就是 token 字符串
        const token = res.data
        // 先存 token，后续请求才能带上 Authorization
        this.$store.commit('SET_TOKEN', token)
        // 再用 token 请求用户信息
        const infoRes = await userApi.getInfo()
        this.$store.commit('SET_USER_INFO', infoRes.data)
        this.$message.success('登录成功')
        this.$router.push('/')
      } catch (e) { /* 验证或请求失败 */ } finally { this.loading = false }
    }
  }
}
</script>

<style scoped>
.login-page { display: flex; justify-content: center; align-items: center; min-height: 70vh; }
.login-card { width: 450px; }
.login-card h2 { text-align: center; color: #409EFF; }
.tip { font-size: 14px; color: #999; }
</style>