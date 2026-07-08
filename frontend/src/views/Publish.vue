<template>
  <div class="publish">
    <h2 class="page-title">{{ isEdit ? '✏️ 编辑文章' : '📝 发布文章' }}</h2>
    <div class="publish-card">
      <el-form :model="form" :rules="rules" ref="pubForm" label-width="80px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" placeholder="请输入文章标题"></el-input></el-form-item>
        <el-form-item label="分类" prop="categoryId"><el-select v-model="form.categoryId" placeholder="请选择分类"><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id"></el-option></el-select></el-form-item>
        <el-form-item label="内容" prop="content">
          <rich-editor ref="editor" v-model="form.content" placeholder="请输入文章内容，支持插入图片、视频和表情" />
        </el-form-item>
        <el-form-item>
          <button type="button" class="submit-btn" @click="handleSubmit">{{ isEdit ? '保存修改' : '发布文章' }}</button>
          <button type="button" class="cancel-btn" @click="$router.back()">取消</button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { articleApi, categoryApi } from '../api'
import RichEditor from '../components/RichEditor'
export default {
  name: 'Publish',
  components: { RichEditor },
  data() {
    const validateContent = (rule, value, callback) => {
      if (!this.form.content || this.form.content.replace(/<[^>]*>/g, '').trim() === '') {
        callback(new Error('请输入内容'))
      } else {
        callback()
      }
    }
    return {
      form: { title: '', content: '', categoryId: '' },
      categories: [],
      isEdit: false,
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        content: [{ validator: validateContent, trigger: 'blur' }]
      }
    }
  },
  async created() {
    const catRes = await categoryApi.getList()
    this.categories = catRes.data
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
      } catch (e) {}
    }
  }
}
</script>
<style scoped>
.page-title { font-size: 24px; font-weight: 700; color: var(--ink); margin-bottom: 20px; }
.publish-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 24px; box-shadow: var(--shadow); max-width: 700px; }
.submit-btn { padding: 10px 24px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; margin-right: 8px; }
.submit-btn:hover { background: var(--primary-hover); }
.cancel-btn { padding: 10px 24px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: transparent; color: var(--ink-secondary); font-size: 14px; cursor: pointer; transition: all 0.15s; }
.cancel-btn:hover { background: var(--canvas); }
</style>
