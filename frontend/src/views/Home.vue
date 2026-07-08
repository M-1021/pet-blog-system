<template>
  <div class="home">
    <!-- 欢迎横幅 -->
    <div class="banner">
      <h1>🐾 欢迎来到宠物博客</h1>
      <p>分享宠物知识，交流养宠心得</p>
      <el-button type="primary" size="medium" @click="$router.push('/knowledge')">浏览宠物知识</el-button>
    </div>
    <!-- 最新文章 -->
    <div class="section">
      <h2>📖 最新文章</h2>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in articles" :key="item.id">
          <el-card class="article-card" shadow="hover" @click.native="$router.push('/article/' + item.id)">
            <h3>{{ item.title }}</h3>
            <p class="article-meta">{{ item.authorName }} · {{ item.categoryName }} · {{ item.likeCount }} 赞</p>
            <p class="article-desc">{{ item.content | excerpt }}</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { articleApi } from '../api'
export default {
  name: 'Home',
  data() { return { articles: [] } },
  filters: {
    // 截取文章前100个字符作为摘要
    excerpt(val) { return val ? val.substring(0, 100) + '...' : '' }
  },
  async created() {
    const res = await articleApi.getList({ page: 1, size: 6 })
    this.articles = res.data.records
  }
}
</script>

<style scoped>
.banner { text-align: center; padding: 60px 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff; border-radius: 8px; margin-bottom: 30px; }
.banner h1 { font-size: 36px; margin-bottom: 10px; }
.banner p { font-size: 18px; margin-bottom: 20px; opacity: 0.9; }
.section { margin-bottom: 30px; }
.section h2 { margin-bottom: 20px; color: #333; }
.article-card { cursor: pointer; margin-bottom: 20px; }
.article-card h3 { font-size: 16px; color: #333; margin-bottom: 8px; }
.article-meta { font-size: 12px; color: #999; margin-bottom: 8px; }
.article-desc { font-size: 14px; color: #666; line-height: 1.6; }
</style>
