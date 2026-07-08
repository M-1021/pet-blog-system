<template>
  <div class="article-detail" v-if="article.id">
    <div class="detail-card">
      <h1 class="article-title">{{ article.title }}</h1>
      <p class="article-meta">作者：{{ article.authorName }} · 分类：{{ article.categoryName }} · {{ article.createTime }}</p>
      <div class="article-content" v-html="article.content"></div>
      <div class="actions">
        <button class="action-btn" :class="{ liked: isLiked }" @click="handleLike">{{ isLiked ? '❤️' : '🤍' }} {{ article.likeCount }} 赞</button>
        <button class="action-btn" @click="showReport = true">⚠️ 举报</button>
      </div>
    </div>
    <div class="comment-section">
      <h3 class="section-title">💬 评论 ({{ comments.length }})</h3>
      <div class="comment-input" v-if="$store.getters.isLoggedIn">
        <el-input v-model="newComment" type="textarea" :rows="2" placeholder="发表你的评论..."></el-input>
        <button class="submit-btn" @click="submitComment">发表评论</button>
      </div>
      <p v-else class="login-tip"><router-link to="/login">登录</router-link>后才能评论</p>
      <div v-for="c in comments" :key="c.id" class="comment-item">
        <div class="comment-avatar">{{ c.userName.charAt(0) }}</div>
        <div class="comment-body">
          <p><strong class="comment-author">{{ c.userName }}</strong> <span class="comment-time">{{ c.createTime }}</span></p>
          <p class="comment-text">{{ c.content }}</p>
        </div>
      </div>
      <el-empty v-if="comments.length === 0" description="暂无评论"></el-empty>
    </div>
    <el-dialog title="举报文章" :visible.sync="showReport" width="400px">
      <el-input v-model="reportReason" type="textarea" :rows="3" placeholder="请描述举报原因..."></el-input>
      <span slot="footer">
        <button class="dialog-btn cancel" @click="showReport = false">取消</button>
        <button class="dialog-btn danger" @click="submitReport">提交举报</button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { articleApi, commentApi, reportApi } from '../api'
export default {
  name: 'ArticleDetail',
  data() { return { article: {}, comments: [], newComment: '', isLiked: false, showReport: false, reportReason: '' } },
  async created() { await this.loadArticle(); await this.loadComments() },
  methods: {
    async loadArticle() { const res = await articleApi.getDetail(this.$route.params.id); this.article = res.data },
    async loadComments() { const res = await commentApi.getList(this.$route.params.id); this.comments = res.data },
    async handleLike() { if (!this.$store.getters.isLoggedIn) return this.$message.warning('请先登录'); const res = await articleApi.toggleLike(this.article.id); this.isLiked = res.data; this.article.likeCount += this.isLiked ? 1 : -1 },
    async submitComment() { if (!this.newComment.trim()) return this.$message.warning('请输入评论内容'); await commentApi.create({ articleId: this.article.id, content: this.newComment }); this.newComment = ''; this.$message.success('评论成功'); this.loadComments(); this.article.commentCount++ },
    async submitReport() { if (!this.reportReason.trim()) return this.$message.warning('请输入举报原因'); await reportApi.create({ articleId: this.article.id, reason: this.reportReason }); this.$message.success('举报已提交'); this.showReport = false; this.reportReason = '' }
  }
}
</script>
<style scoped>
.detail-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 24px; box-shadow: var(--shadow); margin-bottom: 16px; }
.article-title { font-size: 24px; font-weight: 700; color: var(--ink); margin-bottom: 10px; }
.article-meta { font-size: 13px; color: var(--ink-muted); margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid var(--border); }
.article-content { line-height: 1.8; font-size: 15px; color: var(--ink); margin-bottom: 20px; word-wrap: break-word; overflow-wrap: break-word; }
.article-content img { max-width: 100%; border-radius: 8px; margin: 8px 0; }
.article-content video { max-width: 100%; border-radius: 8px; margin: 8px 0; }
.actions { display: flex; gap: 10px; padding-top: 16px; border-top: 1px solid var(--border); }
.action-btn { padding: 8px 18px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: var(--surface); color: var(--ink-secondary); font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s; }
.action-btn:hover { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.action-btn.liked { border-color: var(--critical); color: var(--critical); background: var(--critical-soft); }
.comment-section { background: var(--surface); border-radius: var(--rounded-lg); padding: 20px; box-shadow: var(--shadow); }
.section-title { font-size: 16px; font-weight: 700; color: var(--ink); margin-bottom: 16px; }
.comment-input { margin-bottom: 20px; }
.submit-btn { margin-top: 10px; padding: 8px 18px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.submit-btn:hover { background: var(--primary-hover); }
.login-tip { color: var(--ink-secondary); margin-bottom: 16px; }
.login-tip a { color: var(--primary); font-weight: 500; }
.comment-item { display: flex; gap: 12px; padding: 12px 0; border-bottom: 1px solid var(--border-light); }
.comment-item:last-child { border-bottom: none; }
.comment-avatar { width: 36px; height: 36px; min-width: 36px; border-radius: 50%; background: linear-gradient(135deg, var(--primary), var(--accent-purple)); color: #fff; display: flex; align-items: center; justify-content: center; font-size: 14px; font-weight: 700; }
.comment-body { flex: 1; }
.comment-author { font-size: 14px; color: var(--ink); }
.comment-time { font-size: 12px; color: var(--ink-muted); margin-left: 8px; }
.comment-text { font-size: 14px; color: var(--ink-secondary); margin-top: 4px; }
.dialog-btn { padding: 8px 20px; border-radius: var(--rounded-full); font-size: 14px; font-weight: 600; cursor: pointer; margin-left: 8px; }
.dialog-btn.cancel { border: 1px solid var(--border); background: var(--surface); color: var(--ink); }
.dialog-btn.danger { border: none; background: var(--critical); color: #fff; }
</style>
