<template>
  <div class="post-detail" v-if="post.id">
    <div class="detail-card">
      <h1 class="post-title">{{ post.title }}</h1>
      <p class="post-meta">发布者：{{ post.userName }} · {{ post.createTime }}</p>
      <div class="post-content" v-html="post.content"></div>
    </div>
    <div class="reply-section">
      <h3 class="section-title">💬 回复 ({{ replies.length }})</h3>
      <div class="reply-input" v-if="$store.getters.isLoggedIn">
        <el-input v-model="newReply" type="textarea" :rows="2" placeholder="发表你的回复..."></el-input>
        <button class="submit-btn" @click="submitReply">发表回复</button>
      </div>
      <p v-else class="login-tip"><router-link to="/login">登录</router-link>后才能回复</p>
      <div v-for="r in replies" :key="r.id" class="reply-item">
        <div class="reply-avatar">{{ r.userName.charAt(0) }}</div>
        <div class="reply-body">
          <p><strong class="reply-author">{{ r.userName }}</strong> <span class="reply-time">{{ r.createTime }}</span></p>
          <p class="reply-text">{{ r.content }}</p>
        </div>
      </div>
      <el-empty v-if="replies.length === 0" description="暂无回复"></el-empty>
    </div>
  </div>
</template>
<script>
import { postApi } from '../api'
export default {
  name: 'PostDetail',
  data() { return { post: {}, replies: [], newReply: '' } },
  async created() { await this.loadPost(); await this.loadReplies() },
  methods: {
    async loadPost() { const res = await postApi.getDetail(this.$route.params.id); this.post = res.data },
    async loadReplies() { const res = await postApi.getReplies(this.$route.params.id); this.replies = res.data },
    async submitReply() { if (!this.newReply.trim()) return this.$message.warning('请输入回复内容'); await postApi.addReply({ postId: this.post.id, content: this.newReply }); this.newReply = ''; this.$message.success('回复成功'); this.loadReplies(); this.post.replyCount++ }
  }
}
</script>
<style scoped>
.detail-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 24px; box-shadow: var(--shadow); margin-bottom: 16px; }
.post-title { font-size: 22px; font-weight: 700; color: var(--ink); margin-bottom: 10px; }
.post-meta { font-size: 13px; color: var(--ink-muted); margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid var(--border); }
.post-content { line-height: 1.8; font-size: 15px; color: var(--ink); word-wrap: break-word; overflow-wrap: break-word; }
.post-content img { max-width: 100%; border-radius: 8px; margin: 8px 0; }
.post-content video { max-width: 100%; border-radius: 8px; margin: 8px 0; }
.reply-section { background: var(--surface); border-radius: var(--rounded-lg); padding: 20px; box-shadow: var(--shadow); }
.section-title { font-size: 16px; font-weight: 700; color: var(--ink); margin-bottom: 16px; }
.reply-input { margin-bottom: 20px; }
.submit-btn { margin-top: 10px; padding: 8px 18px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.submit-btn:hover { background: var(--primary-hover); }
.login-tip { color: var(--ink-secondary); margin-bottom: 16px; }
.login-tip a { color: var(--primary); font-weight: 500; }
.reply-item { display: flex; gap: 12px; padding: 12px 0; border-bottom: 1px solid var(--border-light); }
.reply-item:last-child { border-bottom: none; }
.reply-avatar { width: 36px; height: 36px; min-width: 36px; border-radius: 50%; background: linear-gradient(135deg, var(--success), var(--primary)); color: #fff; display: flex; align-items: center; justify-content: center; font-size: 14px; font-weight: 700; }
.reply-body { flex: 1; }
.reply-author { font-size: 14px; color: var(--ink); }
.reply-time { font-size: 12px; color: var(--ink-muted); margin-left: 8px; }
.reply-text { font-size: 14px; color: var(--ink-secondary); margin-top: 4px; }
</style>
