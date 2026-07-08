<template>
  <div class="profile">
    <h2>个人中心</h2>
    <el-card>
      <el-form :model="form" label-width="80px" style="max-width:500px">
        <el-form-item label="用户名"><el-input :value="form.username" disabled></el-input></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname"></el-input></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email"></el-input></el-form-item>
        <el-form-item label="手机"><el-input v-model="form.phone"></el-input></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.bio" type="textarea" :rows="3"></el-input></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { userApi } from '../api'
export default {
  name: 'Profile',
  data() { return { form: {} } },
  async created() {
    const res = await userApi.getInfo()
    this.form = res.data
  },
  methods: {
    async handleSave() {
      await userApi.updateInfo(this.form)
      this.$store.commit('SET_USER_INFO', this.form)
      this.$message.success('保存成功')
    }
  }
}
</script>

<style scoped>
.profile h2 { margin-bottom: 20px; }
</style>
