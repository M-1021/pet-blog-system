<template>
  <div class="knowledge">
    <h2 class="page-title">📖 宠物知识</h2>
    <div class="filter-bar">
      <div class="category-tabs">
        <button class="cat-tab" :class="{ active: selectedCategory === '' }" @click="selectCategory('')">全部</button>
        <button v-for="c in categories" :key="c.id" class="cat-tab" :class="{ active: selectedCategory === c.id }" @click="selectCategory(c.id)">{{ c.name }}</button>
      </div>
      <div class="category-select-wrap">
        <select class="category-select" :value="selectedCategory" @change="selectCategory($event.target.value)">
          <option value="">全部分类</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
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
  filters: { excerpt(val) { if (!val) return ''; const text = val.replace(/<[^>]*>/g, ''); return text.length > 100 ? text.substring(0, 100) + '...' : text } },
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
.category-select-wrap { display: none; }
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
.article-excerpt { font-size: 14px; color: var(--ink-secondary); line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; max-height: 3.2em; }
.pagination { display: flex; align-items: center; justify-content: center; gap: 16px; padding: 20px 0; }
.page-btn { padding: 8px 20px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: var(--surface); color: var(--ink); font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s; box-shadow: var(--shadow-sm); }
.page-btn:hover:not(:disabled) { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-info { font-size: 14px; color: var(--ink-secondary); font-weight: 500; }

@media (max-width: 768px) {
  .filter-bar { flex-direction: column; align-items: stretch; }
  .category-tabs { display: none; }
  .category-select-wrap { display: block; }
  .category-select {
    width: 100%;
    padding: 10px 14px;
    border: 1px solid var(--border);
    border-radius: var(--rounded-full);
    background: var(--surface);
    color: var(--ink);
    font-size: 14px;
    outline: none;
    cursor: pointer;
    appearance: none;
    -webkit-appearance: none;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%2365676b' d='M2 4l4 4 4-4'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 14px center;
    padding-right: 36px;
  }
  .category-select:focus { border-color: var(--primary); }
  .search-box { width: 100%; }
  .search-input { flex: 1; width: auto; }
  .article-item { padding: 12px; }
  .article-title { font-size: 15px; }
  .article-excerpt { font-size: 13px; -webkit-line-clamp: 2; max-height: 2.6em; }
}
</style>