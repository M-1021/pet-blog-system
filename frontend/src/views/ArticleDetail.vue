<template>
  <div class="article-page" v-if="article.id">
    <div class="article-layout">
      <!-- 左侧主内容 -->
      <div class="article-main">
        <div class="detail-card">
          <!-- 标题区域 + 作者头像 -->
          <div class="article-header">
            <div class="author-avatar-lg" v-if="article.userAvatar">
              <img :src="article.userAvatar" @error="handleAvatarError" />
            </div>
            <div class="author-avatar-lg" v-else :class="'avatar-' + getAvatarColor(article.authorName)">
              {{ (article.authorName || '').charAt(0) }}
            </div>
            <div class="header-info">
              <h1 class="article-title">{{ article.title }}</h1>
              <div class="article-meta">
                <span class="meta-author">{{ article.authorName }}</span>
                <span class="meta-sep">·</span>
                <span class="meta-category" v-if="article.categoryName">{{ article.categoryName }}</span>
                <span class="meta-sep">·</span>
                <span class="meta-time">{{ article.createTime }}</span>
                <span class="meta-sep">·</span>
                <span class="meta-views">{{ article.viewCount || 0 }} 浏览</span>
              </div>
            </div>
          </div>

          <!-- 文章正文 -->
          <div class="article-content" v-html="article.content"></div>

          <!-- 操作栏 -->
          <div class="actions">
            <button class="action-btn" :class="{ liked: isLiked }" @click="handleLike">
              {{ isLiked ? '❤️' : '🤍' }} {{ article.likeCount || 0 }} 赞
            </button>
            <button class="action-btn" @click="showReport = true">⚠️ 举报</button>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comment-section">
          <h3 class="section-title">💬 评论 ({{ comments.length }})</h3>
          <div class="comment-input" v-if="$store.getters.isLoggedIn">
            <el-input v-model="newComment" type="textarea" :rows="3" placeholder="发表你的评论..."></el-input>
            <button class="submit-btn" @click="submitComment">发表评论</button>
          </div>
          <p v-else class="login-tip"><router-link to="/login">登录</router-link>后才能评论</p>
          <div v-for="c in comments" :key="c.id" class="comment-item">
            <div class="comment-avatar" :class="'avatar-' + getAvatarColor(c.userName)">{{ c.userName.charAt(0) }}</div>
            <div class="comment-body">
              <div class="comment-header">
                <strong class="comment-author">{{ c.userName }}</strong>
                <span class="comment-time">{{ c.createTime }}</span>
              </div>
              <p class="comment-text">{{ c.content }}</p>
            </div>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无评论"></el-empty>
        </div>
      </div>

      <!-- 右侧目录导航 -->
      <div class="article-sidebar" v-if="headings.length > 0">
        <div class="toc-card">
          <h4 class="toc-title">📑 目录导航</h4>
          <nav class="toc-nav">
            <a v-for="(h, i) in headings" :key="i"
               :href="'#heading-' + i"
               class="toc-item"
               :class="{ active: activeHeading === i }"
               :style="{ paddingLeft: (h.level - 1) * 12 + 'px' }">
              {{ h.text }}
            </a>
          </nav>
        </div>
      </div>
    </div>

    <!-- 举报对话框 -->
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
  data() {
    return {
      article: {}, comments: [], newComment: '', isLiked: false,
      showReport: false, reportReason: '',
      headings: [], activeHeading: 0
    }
  },
  async created() {
    await this.loadArticle()
    await this.loadComments()
    this.$nextTick(() => { this.parseHeadings(); this.setupScrollSpy() })
  },
  beforeDestroy() {
    if (this._scrollHandler) window.removeEventListener('scroll', this._scrollHandler)
  },
  methods: {
    async loadArticle() {
      const res = await articleApi.getDetail(this.$route.params.id)
      this.article = res.data.article || res.data
      this.isLiked = res.data.liked || false
    },
    async loadComments() {
      const res = await commentApi.getList(this.$route.params.id)
      this.comments = res.data
    },
    parseHeadings() {
      const contentEl = this.$el.querySelector('.article-content')
      if (!contentEl) return
      const headingEls = contentEl.querySelectorAll('h1, h2, h3, h4, h5, h6')
      this.headings = Array.from(headingEls).map((el, i) => {
        el.id = 'heading-' + i
        return { level: parseInt(el.tagName.charAt(1)), text: el.textContent.trim() }
      })
    },
    setupScrollSpy() {
      this._scrollHandler = () => {
        const contentEl = this.$el.querySelector('.article-content')
        if (!contentEl) return
        const headingEls = contentEl.querySelectorAll('h1, h2, h3, h4, h5, h6')
        let current = 0
        headingEls.forEach((el, i) => {
          if (el.getBoundingClientRect().top <= 120) current = i
        })
        this.activeHeading = current
      }
      window.addEventListener('scroll', this._scrollHandler, { passive: true })
    },
    getAvatarColor(name) {
      const colors = ['blue', 'green', 'purple', 'orange', 'pink', 'teal']
      let hash = 0
      for (let i = 0; i < (name || '').length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash)
      return colors[Math.abs(hash) % colors.length]
    },
    handleAvatarError(e) { e.target.style.display = 'none' },
    async handleLike() {
      if (!this.$store.getters.isLoggedIn) return this.$message.warning('请先登录')
      try {
        const res = await articleApi.toggleLike(this.article.id)
        this.isLiked = res.data.liked
        this.article.likeCount = res.data.likeCount
      } catch (e) { /* handled */ }
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
.article-page { padding: 20px 0; }

.article-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.article-main { flex: 1; min-width: 0; }

.detail-card {
  background: var(--surface);
  border-radius: var(--rounded-lg);
  padding: 28px;
  box-shadow: var(--shadow);
  margin-bottom: 16px;
}

/* 标题区域 */
.article-header {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border);
}

.author-avatar-lg {
  width: 48px;
  height: 48px;
  min-width: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  overflow: hidden;
}

.author-avatar-lg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.header-info { flex: 1; }

.article-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 8px;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--ink-muted);
  flex-wrap: wrap;
}

.meta-author { font-weight: 600; color: var(--primary); }
.meta-sep { color: var(--border); }

/* 文章正文 */
.article-content {
  line-height: 1.8;
  font-size: 16px;
  color: var(--ink);
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.article-content :deep(h1),
.article-content :deep(h2),
.article-content :deep(h3),
.article-content :deep(h4),
.article-content :deep(h5),
.article-content :deep(h6) {
  font-weight: 700;
  color: var(--ink);
  margin: 24px 0 12px;
  line-height: 1.4;
}

.article-content :deep(h1) { font-size: 24px; }
.article-content :deep(h2) { font-size: 20px; }
.article-content :deep(h3) { font-size: 17px; }
.article-content :deep(h4) { font-size: 16px; }

.article-content :deep(p) {
  margin-bottom: 14px;
  line-height: 1.8;
}

.article-content :deep(ul),
.article-content :deep(ol) {
  padding-left: 24px;
  margin-bottom: 14px;
}

.article-content :deep(li) {
  margin-bottom: 6px;
  line-height: 1.7;
}

.article-content :deep(strong) {
  font-weight: 700;
  color: var(--ink);
}

.article-content :deep(a) {
  color: var(--primary);
  text-decoration: none;
}

.article-content :deep(a:hover) {
  text-decoration: underline;
}

.article-content :deep(code) {
  background: var(--canvas);
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 14px;
  color: var(--critical);
}

.article-content :deep(pre) {
  background: var(--canvas);
  padding: 16px;
  border-radius: var(--rounded-md);
  overflow-x: auto;
  margin-bottom: 14px;
  border: 1px solid var(--border);
}

.article-content :deep(pre code) {
  background: none;
  padding: 0;
  color: var(--ink);
}

.article-content :deep(blockquote) {
  border-left: 4px solid var(--primary);
  padding: 12px 16px;
  margin: 14px 0;
  background: var(--primary-soft);
  border-radius: 0 var(--rounded-md) var(--rounded-md) 0;
  color: var(--ink-secondary);
}

/* 图片样式 - 关键修复 */
.article-content :deep(img) {
  max-width: 100%;
  height: auto;
  max-height: 600px;
  object-fit: contain;
  border-radius: var(--rounded-lg);
  margin: 16px 0;
  display: block;
  box-shadow: var(--shadow);
  border: 1px solid var(--border);
}

/* 如果图片被包裹在段落或div中，也限制 */
.article-content :deep(p img),
.article-content :deep(div img) {
  max-width: 100%;
  max-height: 600px;
}

.article-content :deep(video) {
  max-width: 100%;
  max-height: 500px;
  border-radius: var(--rounded-lg);
  margin: 16px 0;
  display: block;
}

/* 操作栏 */
.actions {
  display: flex;
  gap: 10px;
  padding-top: 20px;
  margin-top: 20px;
  border-top: 1px solid var(--border);
}

.action-btn {
  padding: 8px 18px;
  border: 1px solid var(--border);
  border-radius: var(--rounded-full);
  background: var(--surface);
  color: var(--ink-secondary);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
}

.action-btn:hover {
  background: var(--primary-soft);
  border-color: var(--primary);
  color: var(--primary);
}

.action-btn.liked {
  border-color: var(--critical);
  color: var(--critical);
  background: var(--critical-soft);
}

/* 评论区 */
.comment-section {
  background: var(--surface);
  border-radius: var(--rounded-lg);
  padding: 20px;
  box-shadow: var(--shadow);
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 16px;
}

.comment-input { margin-bottom: 20px; }

.submit-btn {
  margin-top: 10px;
  padding: 8px 18px;
  border: none;
  border-radius: var(--rounded-full);
  background: var(--primary);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}

.submit-btn:hover { background: var(--primary-hover); }

.login-tip { color: var(--ink-secondary); margin-bottom: 16px; }
.login-tip a { color: var(--primary); font-weight: 500; }

.comment-item {
  display: flex;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid var(--border-light);
}

.comment-item:last-child { border-bottom: none; }

.comment-avatar {
  width: 36px;
  height: 36px;
  min-width: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  color: #fff;
}

.comment-body { flex: 1; }
.comment-header { display: flex; align-items: center; gap: 8px; margin-bottom: 4px; }
.comment-author { font-size: 14px; color: var(--ink); }
.comment-time { font-size: 12px; color: var(--ink-muted); }
.comment-text { font-size: 14px; color: var(--ink-secondary); line-height: 1.6; }

/* 头像颜色 */
.avatar-blue { background: linear-gradient(135deg, #1877f2, #42a5f5); }
.avatar-green { background: linear-gradient(135deg, #42b72a, #66bb6a); }
.avatar-purple { background: linear-gradient(135deg, #a033c0, #ce93d8); }
.avatar-orange { background: linear-gradient(135deg, #f7b928, #ffb74d); }
.avatar-pink { background: linear-gradient(135deg, #e41e3f, #ef5350); }
.avatar-teal { background: linear-gradient(135deg, #00838f, #4dd0e1); }

/* 右侧目录导航 */
.article-sidebar {
  width: 220px;
  min-width: 220px;
  position: sticky;
  top: 20px;
  flex-shrink: 0;
}

.toc-card {
  background: var(--surface);
  border-radius: var(--rounded-lg);
  padding: 16px;
  box-shadow: var(--shadow);
}

.toc-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border);
}

.toc-nav {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.toc-item {
  display: block;
  padding: 6px 8px;
  font-size: 13px;
  color: var(--ink-secondary);
  text-decoration: none;
  border-radius: var(--rounded-sm);
  transition: all 0.15s;
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.toc-item:hover {
  background: var(--primary-soft);
  color: var(--primary);
  text-decoration: none;
}

.toc-item.active {
  background: var(--primary-soft);
  color: var(--primary);
  font-weight: 600;
}

/* 对话框按钮 */
.dialog-btn {
  padding: 8px 20px;
  border-radius: var(--rounded-full);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  margin-left: 8px;
}

.dialog-btn.cancel {
  border: 1px solid var(--border);
  background: var(--surface);
  color: var(--ink);
}

.dialog-btn.danger {
  border: none;
  background: var(--critical);
  color: #fff;
}

/* 响应式：小屏幕隐藏右侧目录 */
@media (max-width: 1024px) {
  .article-sidebar { display: none; }
}
</style>
