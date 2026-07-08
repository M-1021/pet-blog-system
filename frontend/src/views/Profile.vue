<template>
  <div class="profile">
    <h2 class="page-title">👤 个人中心</h2>
    <div class="profile-card">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input :value="form.username" disabled></el-input></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname"></el-input></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email"></el-input></el-form-item>
        <el-form-item label="手机"><el-input v-model="form.phone"></el-input></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.bio" type="textarea" :rows="3"></el-input></el-form-item>
        <el-form-item><button type="button" class="save-btn" @click="handleSave">💾 保存修改</button></el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { userApi } from '../api'
export default {
  name: 'Profile',
  data() { return { form: {} } },
  async created() { const res = await userApi.getInfo(); this.form = res.data },
  methods: { async handleSave() { await userApi.updateInfo(this.form); this.$store.commit('SET_USER_INFO', this.form); this.$message.success('保存成功') } }
}
</script>
<style scoped>
.page-title { font-size: 24px; font-weight: 700; color: var(--ink); margin-bottom: 20px; }
.profile-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 24px; box-shadow: var(--shadow); max-width: 520px; }
.save-btn { padding: 10px 24px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.save-btn:hover { background: var(--primary-hover); }
</style>
