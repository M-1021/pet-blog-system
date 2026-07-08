<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="card-title">用户登录</h2>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin"></el-input></el-form-item>
        <el-form-item><button type="button" class="login-btn" :class="{ loading: loading }" @click="handleLogin">{{ loading ? '登录中...' : '登 录' }}</button></el-form-item>
        <el-form-item><span class="tip">还没有账号？<router-link to="/register">立即注册</router-link></span></el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { userApi } from '../api'
export default {
  name: 'Login',
  data() { return { form: { username: '', password: '' }, loading: false, rules: { username: [{ required: true, message: '请输入用户名', trigger: 'blur' }], password: [{ required: true, message: '请输入密码', trigger: 'blur' }] } } },
  methods: {
    async handleLogin() { try { await this.$refs.loginForm.validate(); this.loading = true; const res = await userApi.login(this.form); this.$store.commit('SET_TOKEN', res.data); const infoRes = await userApi.getInfo(); this.$store.commit('SET_USER_INFO', infoRes.data); this.$message.success('登录成功'); this.$router.push('/') } catch (e) {} finally { this.loading = false } }
  }
}
</script>
<style scoped>
.login-page { display: flex; justify-content: center; align-items: center; min-height: 60vh; }
.login-card { width: 420px; background: var(--surface); border-radius: var(--rounded-lg); padding: 32px; box-shadow: var(--shadow-lg); }
.card-title { text-align: center; color: var(--primary); font-size: 24px; font-weight: 700; margin-bottom: 24px; }
.login-btn { width: 100%; padding: 10px 20px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 15px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.login-btn:hover { background: var(--primary-hover); }
.login-btn.loading { opacity: 0.6; cursor: not-allowed; }
.tip { font-size: 14px; color: var(--ink-secondary); }
.tip a { color: var(--primary); font-weight: 500; }
</style>
