<template>
  <div class="post-detail" v-if="post.id">
    <el-card>
      <h1>{{ post.title }}</h1>
      <p class="meta">发布者：{{ post.userName }} · {{ post.createTime }}</p>
      <div class="content">{{ post.content }}</div>
    </el-card>
    <!-- 回复区 -->
    <el-card class="reply-section">
      <h3 slot="header">回复 ({{ replies.length }})</h3>
      <div class="reply-input" v-if="$store.getters.isLoggedIn">
        <el-input v-model="newReply" type="textarea" :rows="2" placeholder="发表你的回复..."></el-input>
        <el-button type="primary" size="small" @click="submitReply" style="margin-top:10px">发表回复</el-button>
      </div>
      <p v-else class="login-tip"><router-link to="/login">登录</router-link>后才能回复</p>
      <div v-for="r in replies" :key="r.id" class="reply-item">
        <p><strong>{{ r.userName }}</strong> <span class="time">{{ r.createTime }}</span></p>
        <p>{{ r.content }}</p>
      </div>
      <el-empty v-if="replies.length === 0" description="暂无回复"></el-empty>
    </el-card>
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
    async submitReply() {
      if (!this.newReply.trim()) return this.$message.warning('请输入回复内容')
      await postApi.addReply({ postId: this.post.id, content: this.newReply })
      this.newReply = ''
      this.$message.success('回复成功')
      this.loadReplies()
      this.post.replyCount++
    }
  }
}
</script>

<style scoped>
.post-card h1 { font-size: 22px; margin-bottom: 10px; }
.meta { font-size: 13px; color: #999; margin-bottom: 15px; }
.content { line-height: 1.8; font-size: 15px; white-space: pre-wrap; }
.reply-section { margin-top: 20px; }
.reply-input { margin-bottom: 20px; }
.login-tip { color: #999; margin-bottom: 15px; }
.reply-item { padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.reply-item .time { font-size: 12px; color: #999; margin-left: 10px; }
</style>
