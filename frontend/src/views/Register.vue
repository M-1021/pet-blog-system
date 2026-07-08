<template>
  <div class="register-page">
    <div class="register-card">
      <h2 class="card-title">用户注册</h2>
      <el-form :model="form" :rules="rules" ref="regForm" label-width="80px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" placeholder="3-20个字符" prefix-icon="el-icon-user"></el-input></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password" placeholder="6-20个字符" prefix-icon="el-icon-lock"></el-input></el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd"><el-input v-model="form.confirmPwd" type="password" placeholder="再次输入密码" prefix-icon="el-icon-lock"></el-input></el-form-item>
        <el-form-item label="昵称" prop="nickname"><el-input v-model="form.nickname" placeholder="选填"></el-input></el-form-item>
        <el-form-item><button type="button" class="register-btn" :class="{ loading: loading }" @click="handleRegister">{{ loading ? '注册中...' : '注 册' }}</button></el-form-item>
        <el-form-item><span class="tip">已有账号？<router-link to="/login">去登录</router-link></span></el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { userApi } from '../api'
export default {
  name: 'Register',
  data() {
    const confirmRule = (rule, value, callback) => { if (value !== this.form.password) callback(new Error('两次密码不一致')); else callback() }
    return { form: { username: '', password: '', confirmPwd: '', nickname: '' }, loading: false, rules: { username: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 3, max: 20, message: '3-20个字符', trigger: 'blur' }], password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, max: 20, message: '6-20个字符', trigger: 'blur' }], confirmPwd: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: confirmRule, trigger: 'blur' }] } }
  },
  methods: {
    async handleRegister() { try { await this.$refs.regForm.validate(); this.loading = true; await userApi.register({ username: this.form.username, password: this.form.password, nickname: this.form.nickname }); this.$message.success('注册成功，请登录'); this.$router.push('/login') } catch (e) {} finally { this.loading = false } }
  }
}
</script>
<style scoped>
.register-page { display: flex; justify-content: center; align-items: center; min-height: 60vh; }
.register-card { width: 420px; background: var(--surface); border-radius: var(--rounded-lg); padding: 32px; box-shadow: var(--shadow-lg); }
.card-title { text-align: center; color: var(--primary); font-size: 24px; font-weight: 700; margin-bottom: 24px; }
.register-btn { width: 100%; padding: 10px 20px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 15px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.register-btn:hover { background: var(--primary-hover); }
.register-btn.loading { opacity: 0.6; cursor: not-allowed; }
.tip { font-size: 14px; color: var(--ink-secondary); }
.tip a { color: var(--primary); font-weight: 500; }
</style>
