<template>
  <div class="publish">
    <h2>{{ isEdit ? '编辑文章' : '发布文章' }}</h2>
    <el-card>
      <el-form :model="form" :rules="rules" ref="pubForm" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="12" placeholder="请输入文章内容"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">{{ isEdit ? '保存修改' : '发布文章' }}</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { articleApi, categoryApi } from '../api'
export default {
  name: 'Publish',
  data() {
    return {
      form: { title: '', content: '', categoryId: '' },
      categories: [],
      isEdit: false,
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  async created() {
    const catRes = await categoryApi.getList()
    this.categories = catRes.data
    // 如果是编辑模式，加载文章数据
    const editId = this.$route.query.id
    if (editId) {
      this.isEdit = true
      const res = await articleApi.getDetail(editId)
      this.form = { id: res.data.id, title: res.data.title, content: res.data.content, categoryId: res.data.categoryId }
    }
  },
  methods: {
    async handleSubmit() {
      try {
        await this.$refs.pubForm.validate()
        if (this.isEdit) {
          await articleApi.update(this.form)
          this.$message.success('修改成功')
        } else {
          await articleApi.create(this.form)
          this.$message.success('发布成功')
        }
        this.$router.push('/my-articles')
      } catch (e) { /* validation failed */ }
    }
  }
}
</script>

<style scoped>
.publish h2 { margin-bottom: 20px; }
</style>
