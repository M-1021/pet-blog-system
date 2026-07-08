<template>
  <div class="knowledge">
    <h2 class="page-title">📖 宠物知识</h2>
    <div class="filter-bar">
      <div class="category-tabs">
        <button class="cat-tab" :class="{ active: selectedCategory === '' }" @click="selectCategory('')">全部</button>
        <button v-for="c in categories" :key="c.id" class="cat-tab" :class="{ active: selectedCategory === c.id }" @click="selectCategory(c.id)">{{ c.name }}</button>
      </div>
      <div class="search-box">
        <input v-model="keyword" placeholder="🔍 搜索文章..." class="search-input" @keyup.enter="loadArticles" />
        <button class="search-btn" @click="loadArticles">搜索</button>
      </div>
    </div>
    <div class="article-list">
      <div v-for="item in articles" :key="item.id" class="article-item" @click="$router.push('/article/' + item.id)">
        <div class="article-main">
          <h3 class="article-title">{{ item.title }}</h3>
          <div class="article-meta">
            <span class="meta-author">{{ item.authorName }}</span>
            <span class="meta-sep">·</span>
            <span class="meta-category">{{ item.categoryName }}</span>
            <span class="meta-sep">·</span>
            <span class="meta-likes">❤️ {{ item.likeCount }}</span>
            <span class="meta-sep">·</span>
            <span class="meta-comments">💬 {{ item.commentCount }}</span>
          </div>
          <p class="article-excerpt">{{ item.content | excerpt }}</p>
        </div>
      </div>
    </div>
    <el-empty v-if="articles.length === 0" description="暂无文章"></el-empty>
    <div class="pagination" v-if="total > pageSize">
      <button class="page-btn" :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">上一页</button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button class="page-btn" :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)">下一页</button>
    </div>
  </div>
</template>
<script>
import { articleApi, categoryApi } from '../api'
export default {
  name: 'Knowledge',
  data() {
    return { articles: [], categories: [], selectedCategory: '', keyword: '', currentPage: 1, pageSize: 10, total: 0 }
  },
  filters: { excerpt(val) { return val ? val.substring(0, 100) + '...' : '' } },
  computed: { totalPages() { return Math.ceil(this.total / this.pageSize) } },
  async created() {
    if (this.$route.query.keyword) this.keyword = this.$route.query.keyword
    if (this.$route.query.categoryId) this.selectedCategory = this.$route.query.categoryId
    const catRes = await categoryApi.getList()
    this.categories = catRes.data
    this.loadArticles()
    this.$root.$on('search', (kw) => { this.keyword = kw; this.currentPage = 1; this.loadArticles() })
    this.$root.$on('filter-category', (catId) => { this.selectedCategory = catId; this.currentPage = 1; this.loadArticles() })
  },
  beforeDestroy() { this.$root.$off('search'); this.$root.$off('filter-category') },
  methods: {
    selectCategory(id) { this.selectedCategory = id; this.currentPage = 1; this.loadArticles() },
    async loadArticles() {
      const res = await articleApi.getList({ page: this.currentPage, size: this.pageSize, categoryId: this.selectedCategory || '', keyword: this.keyword })
      this.articles = res.data.records || []; this.total = res.data.total || 0
    },
    changePage(page) { this.currentPage = page; this.loadArticles() }
  }
}
</script>
<style scoped>
.knowledge { padding: 16px 0; }
.page-title { font-size: 24px; font-weight: 700; color: var(--ink); margin-bottom: 16px; }
.filter-bar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; gap: 12px; flex-wrap: wrap; }
.category-tabs { display: flex; gap: 6px; flex-wrap: wrap; }
.cat-tab { padding: 6px 16px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: var(--surface); color: var(--ink-secondary); font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s; box-shadow: var(--shadow-sm); }
.cat-tab:hover { border-color: var(--primary); color: var(--primary); }
.cat-tab.active { background: var(--primary); color: #fff; border-color: var(--primary); }
.search-box { display: flex; gap: 6px; }
.search-input { padding: 8px 14px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: var(--surface); color: var(--ink); font-size: 14px; outline: none; width: 200px; transition: all 0.2s; box-shadow: var(--shadow-sm); }
.search-input:focus { border-color: var(--primary); box-shadow: 0 0 0 2px var(--primary-soft); }
.search-input::placeholder { color: var(--ink-muted); }
.search-btn { padding: 8px 16px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; box-shadow: var(--shadow-sm); }
.search-btn:hover { background: var(--primary-hover); }
.article-list { margin: 0; }
.article-item { padding: 16px; background: var(--surface); border-radius: var(--rounded-lg); margin-bottom: 12px; cursor: pointer; transition: all 0.15s; box-shadow: var(--shadow); border: 1px solid transparent; }
.article-item:hover { box-shadow: var(--shadow-lg); border-color: var(--primary-soft); }
.article-title { font-size: 16px; font-weight: 600; color: var(--ink); margin-bottom: 6px; line-height: 1.4; }
.article-meta { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--ink-muted); margin-bottom: 6px; }
.meta-sep { color: var(--border); }
.article-excerpt { font-size: 14px; color: var(--ink-secondary); line-height: 1.6; }
.pagination { display: flex; align-items: center; justify-content: center; gap: 16px; padding: 20px 0; }
.page-btn { padding: 8px 20px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: var(--surface); color: var(--ink); font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s; box-shadow: var(--shadow-sm); }
.page-btn:hover:not(:disabled) { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-info { font-size: 14px; color: var(--ink-secondary); font-weight: 500; }
</style>
