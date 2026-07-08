<template>
  <div class="community">
    <div class="header-row">
      <h2>💬 在线交流</h2>
      <el-button type="primary" icon="el-icon-edit" @click="showPublish = true" v-if="$store.getters.isLoggedIn">发布帖子</el-button>
    </div>
    <!-- 帖子列表 -->
    <el-card v-for="item in posts" :key="item.id" class="post-item" shadow="hover" @click.native="$router.push('/post/' + item.id)">
      <h3>{{ item.title }}</h3>
      <p class="meta">{{ item.userName }} · {{ item.replyCount }}条回复 · {{ item.createTime }}</p>
      <p class="desc">{{ item.content | excerpt }}</p>
    </el-card>
    <el-pagination v-if="total > 0" layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page.sync="currentPage" @current-change="loadPosts" style="text-align:center;margin-top:20px"></el-pagination>
    <el-empty v-if="posts.length === 0" description="暂无帖子"></el-empty>
    <!-- 发布帖子对话框 -->
    <el-dialog title="发布帖子" :visible.sync="showPublish" width="600px">
      <el-form :model="postForm" label-width="60px">
        <el-form-item label="标题"><el-input v-model="postForm.title" placeholder="请输入标题"></el-input></el-form-item>
        <el-form-item label="内容"><el-input v-model="postForm.content" type="textarea" :rows="6" placeholder="请输入内容"></el-input></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="showPublish = false">取消</el-button>
        <el-button type="primary" @click="publishPost">发布</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { postApi } from '../api'
export default {
  name: 'Community',
  data() {
    return { posts: [], currentPage: 1, pageSize: 10, total: 0, showPublish: false, postForm: { title: '', content: '' } }
  },
  filters: { excerpt(val) { return val ? val.substring(0, 100) + '...' : '' } },
  created() { this.loadPosts() },
  methods: {
    async loadPosts() {
      const res = await postApi.getList({ page: this.currentPage, size: this.pageSize })
      this.posts = res.data.records
      this.total = res.data.total
    },
    async publishPost() {
      if (!this.postForm.title || !this.postForm.content) return this.$message.warning('请填写完整')
      await postApi.create(this.postForm)
      this.$message.success('发布成功')
      this.showPublish = false
      this.postForm = { title: '', content: '' }
      this.loadPosts()
    }
  }
}
</script>

<style scoped>
.header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.post-item { margin-bottom: 15px; cursor: pointer; }
.post-item h3 { font-size: 17px; margin-bottom: 6px; }
.meta { font-size: 13px; color: #999; margin-bottom: 6px; }
.desc { font-size: 14px; color: #666; }
</style>
