<template>
  <div class="knowledge">
    <h2>📖 宠物知识</h2>
    <!-- 分类筛选 -->
    <div class="filter-bar">
      <el-radio-group v-model="selectedCategory" @change="loadArticles">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button v-for="c in categories" :key="c.id" :label="c.id">{{ c.name }}</el-radio-button>
      </el-radio-group>
      <el-input v-model="keyword" placeholder="搜索文章..." style="width:250px;margin-left:20px" @keyup.enter.native="loadArticles" clearable>
        <el-button slot="append" icon="el-icon-search" @click="loadArticles"></el-button>
      </el-input>
    </div>
    <!-- 文章列表 -->
    <el-card v-for="item in articles" :key="item.id" class="article-item" shadow="hover" @click.native="$router.push('/article/' + item.id)">
      <div class="article-info">
        <h3>{{ item.title }}</h3>
        <p class="meta">{{ item.authorName }} · {{ item.categoryName }} · {{ item.likeCount }}赞 · {{ item.commentCount }}评论</p>
        <p class="desc">{{ item.content | excerpt }}</p>
      </div>
    </el-card>
    <!-- 分页 -->
    <el-pagination v-if="total > 0" layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page.sync="currentPage" @current-change="loadArticles" style="text-align:center;margin-top:20px">
    </el-pagination>
    <el-empty v-if="articles.length === 0" description="暂无文章"></el-empty>
  </div>
</template>

<script>
import { articleApi, categoryApi } from '../api'
export default {
  name: 'Knowledge',
  data() {
    return { articles: [], categories: [], selectedCategory: '', keyword: '', currentPage: 1, pageSize: 10, total: 0 }
  },
  filters: { excerpt(val) { return val ? val.substring(0, 120) + '...' : '' } },
  async created() {
    const catRes = await categoryApi.getList()
    this.categories = catRes.data
    this.loadArticles()
  },
  methods: {
    async loadArticles() {
      const res = await articleApi.getList({ page: this.currentPage, size: this.pageSize, categoryId: this.selectedCategory || '', keyword: this.keyword })
      this.articles = res.data.records
      this.total = res.data.total
    }
  }
}
</script>

<style scoped>
.knowledge h2 { margin-bottom: 20px; }
.filter-bar { display: flex; align-items: center; margin-bottom: 20px; }
.article-item { margin-bottom: 15px; cursor: pointer; }
.article-item h3 { font-size: 17px; color: #333; margin-bottom: 6px; }
.meta { font-size: 13px; color: #999; margin-bottom: 6px; }
.desc { font-size: 14px; color: #666; line-height: 1.6; }
</style>
