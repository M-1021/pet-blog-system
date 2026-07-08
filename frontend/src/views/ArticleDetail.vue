<template>
  <div class="article-detail" v-if="article.id">
    <!-- 文章内容 -->
    <el-card class="article-card">
      <h1>{{ article.title }}</h1>
      <p class="meta">作者：{{ article.authorName }} · 分类：{{ article.categoryName }} · {{ article.createTime }}</p>
      <div class="content" v-html="article.content"></div>
      <!-- 操作栏 -->
      <div class="actions">
        <el-button :type="isLiked ? 'danger' : 'default'" icon="el-icon-star-off" @click="handleLike">{{ article.likeCount }} 赞</el-button>
        <el-button icon="el-icon-warning" @click="showReport = true">举报</el-button>
      </div>
    </el-card>
    <!-- 评论区 -->
    <el-card class="comment-section">
      <h3 slot="header">评论 ({{ comments.length }})</h3>
      <!-- 发表评论 -->
      <div class="comment-input" v-if="$store.getters.isLoggedIn">
        <el-input v-model="newComment" type="textarea" :rows="2" placeholder="发表你的评论..."></el-input>
        <el-button type="primary" size="small" @click="submitComment" style="margin-top:10px">发表评论</el-button>
      </div>
      <p v-else class="login-tip"><router-link to="/login">登录</router-link>后才能评论</p>
      <!-- 评论列表 -->
      <div v-for="c in comments" :key="c.id" class="comment-item">
        <p><strong>{{ c.userName }}</strong> <span class="time">{{ c.createTime }}</span></p>
        <p>{{ c.content }}</p>
      </div>
      <el-empty v-if="comments.length === 0" description="暂无评论"></el-empty>
    </el-card>
    <!-- 举报对话框 -->
    <el-dialog title="举报文章" :visible.sync="showReport" width="400px">
      <el-input v-model="reportReason" type="textarea" :rows="3" placeholder="请描述举报原因..."></el-input>
      <span slot="footer">
        <el-button @click="showReport = false">取消</el-button>
        <el-button type="danger" @click="submitReport">提交举报</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { articleApi, commentApi, reportApi } from '../api'
export default {
  name: 'ArticleDetail',
  data() {
    return { article: {}, comments: [], newComment: '', isLiked: false, showReport: false, reportReason: '' }
  },
  async created() {
    await this.loadArticle()
    await this.loadComments()
  },
  methods: {
    async loadArticle() {
      const res = await articleApi.getDetail(this.$route.params.id)
      this.article = res.data
    },
    async loadComments() {
      const res = await commentApi.getList(this.$route.params.id)
      this.comments = res.data
    },
    async handleLike() {
      if (!this.$store.getters.isLoggedIn) return this.$message.warning('请先登录')
      const res = await articleApi.toggleLike(this.article.id)
      this.isLiked = res.data
      this.article.likeCount += this.isLiked ? 1 : -1
    },
    async submitComment() {
      if (!this.newComment.trim()) return this.$message.warning('请输入评论内容')
      await commentApi.create({ articleId: this.article.id, content: this.newComment })
      this.newComment = ''
      this.$message.success('评论成功')
      this.loadComments()
      this.article.commentCount++
    },
    async submitReport() {
      if (!this.reportReason.trim()) return this.$message.warning('请输入举报原因')
      await reportApi.create({ articleId: this.article.id, reason: this.reportReason })
      this.$message.success('举报已提交')
      this.showReport = false
      this.reportReason = ''
    }
  }
}
</script>

<style scoped>
.article-card { margin-bottom: 20px; }
.article-card h1 { font-size: 24px; margin-bottom: 10px; }
.meta { font-size: 13px; color: #999; margin-bottom: 20px; }
.content { line-height: 1.8; font-size: 15px; color: #333; margin-bottom: 20px; white-space: pre-wrap; }
.actions { border-top: 1px solid #eee; padding-top: 15px; }
.comment-section { margin-bottom: 20px; }
.comment-input { margin-bottom: 20px; }
.login-tip { color: #999; margin-bottom: 15px; }
.comment-item { padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.comment-item .time { font-size: 12px; color: #999; margin-left: 10px; }
</style>
