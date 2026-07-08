<template>
  <div class="community">
    <div class="page-header">
      <h2 class="page-title">💬 在线交流</h2>
      <button class="publish-btn" v-if="$store.getters.isLoggedIn" @click="showPublish = true">✏️ 发布帖子</button>
    </div>
    <div class="post-list">
      <div v-for="item in posts" :key="item.id" class="post-item" @click="$router.push('/post/' + item.id)">
        <div class="post-main">
          <h3 class="post-title">{{ item.title }}</h3>
          <div class="post-meta">
            <span class="meta-user">{{ item.userName }}</span>
            <span class="meta-sep">·</span>
            <span class="meta-replies">💬 {{ item.replyCount }}条回复</span>
            <span class="meta-sep">·</span>
            <span class="meta-time">{{ formatTime(item.createTime) }}</span>
          </div>
          <p class="post-excerpt">{{ item.content | excerpt }}</p>
        </div>
      </div>
    </div>
    <el-empty v-if="posts.length === 0" description="暂无帖子"></el-empty>
    <div class="pagination" v-if="total > pageSize">
      <button class="page-btn" :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">上一页</button>
      <span class="page-info">{{ currentPage }} / {{ Math.ceil(total / pageSize) }}</span>
      <button class="page-btn" :disabled="currentPage >= Math.ceil(total / pageSize)" @click="changePage(currentPage + 1)">下一页</button>
    </div>
    <el-dialog title="发布帖子" :visible.sync="showPublish" width="600px">
      <el-form :model="postForm" label-width="60px">
        <el-form-item label="标题"><el-input v-model="postForm.title" placeholder="请输入标题"></el-input></el-form-item>
        <el-form-item label="内容"><el-input v-model="postForm.content" type="textarea" :rows="6" placeholder="请输入内容"></el-input></el-form-item>
      </el-form>
      <span slot="footer">
        <button class="dialog-btn cancel" @click="showPublish = false">取消</button>
        <button class="dialog-btn primary" @click="publishPost">发布</button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { postApi } from '../api'
export default {
  name: 'Community',
  data() { return { posts: [], currentPage: 1, pageSize: 10, total: 0, showPublish: false, postForm: { title: '', content: '' } } },
  filters: { excerpt(val) { return val ? val.substring(0, 80) + '...' : '' } },
  created() { this.loadPosts() },
  methods: {
    async loadPosts() { const res = await postApi.getList({ page: this.currentPage, size: this.pageSize }); this.posts = res.data.records || []; this.total = res.data.total || 0 },
    async publishPost() {
      if (!this.postForm.title || !this.postForm.content) return this.$message.warning('请填写完整')
      await postApi.create(this.postForm); this.$message.success('发布成功'); this.showPublish = false; this.postForm = { title: '', content: '' }; this.loadPosts()
    },
    changePage(page) { this.currentPage = page; this.loadPosts() },
    formatTime(time) { if (!time) return ''; const diff = Date.now() - new Date(time).getTime(); const days = Math.floor(diff / 86400000); if (days > 0) return days + '天前'; const hours = Math.floor(diff / 3600000); if (hours > 0) return hours + '小时前'; const mins = Math.floor(diff / 60000); return mins > 0 ? mins + '分钟前' : '刚刚' }
  }
}
</script>
<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-title { font-size: 24px; font-weight: 700; color: var(--ink); }
.publish-btn { padding: 10px 20px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; box-shadow: var(--shadow-sm); }
.publish-btn:hover { background: var(--primary-hover); }
.post-list { margin: 0; }
.post-item { padding: 16px; background: var(--surface); border-radius: var(--rounded-lg); margin-bottom: 12px; cursor: pointer; transition: all 0.15s; box-shadow: var(--shadow); border: 1px solid transparent; }
.post-item:hover { box-shadow: var(--shadow-lg); border-color: var(--primary-soft); }
.post-title { font-size: 16px; font-weight: 600; color: var(--ink); margin-bottom: 6px; line-height: 1.4; }
.post-meta { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--ink-muted); margin-bottom: 6px; }
.meta-sep { color: var(--border); }
.post-excerpt { font-size: 14px; color: var(--ink-secondary); line-height: 1.6; }
.pagination { display: flex; align-items: center; justify-content: center; gap: 16px; padding: 20px 0; }
.page-btn { padding: 8px 20px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: var(--surface); color: var(--ink); font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s; box-shadow: var(--shadow-sm); }
.page-btn:hover:not(:disabled) { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-info { font-size: 14px; color: var(--ink-secondary); font-weight: 500; }
.dialog-btn { padding: 8px 20px; border-radius: var(--rounded-full); font-size: 14px; font-weight: 600; cursor: pointer; margin-left: 8px; transition: all 0.15s; }
.dialog-btn.cancel { border: 1px solid var(--border); background: var(--surface); color: var(--ink); }
.dialog-btn.cancel:hover { background: var(--canvas); }
.dialog-btn.primary { border: none; background: var(--primary); color: #fff; }
.dialog-btn.primary:hover { background: var(--primary-hover); }
</style>
