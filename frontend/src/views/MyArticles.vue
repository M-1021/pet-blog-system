<template>
  <div class="my-articles">
    <div class="page-header">
      <h2 class="page-title">📄 我的文章</h2>
      <button class="publish-btn" @click="$router.push('/publish')">✏️ 写文章</button>
    </div>
    <div class="table-card">
      <el-table :data="articles" style="width:100%">
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <button class="table-btn" @click="$router.push('/article/' + scope.row.id)">查看</button>
            <button class="table-btn" @click="$router.push('/publish?id=' + scope.row.id)">编辑</button>
            <button class="table-btn danger" @click="handleDelete(scope.row.id)">删除</button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-empty v-if="articles.length === 0" description="还没有文章">
      <button class="publish-btn" @click="$router.push('/publish')">去写文章</button>
    </el-empty>
  </div>
</template>
<script>
import { articleApi } from '../api'
export default {
  name: 'MyArticles',
  data() { return { articles: [] } },
  async created() { const res = await articleApi.getMyList(); this.articles = res.data },
  methods: {
    async handleDelete(id) { await this.$confirm('确定删除这篇文章吗？', '提示', { type: 'warning' }); await articleApi.delete(id); this.$message.success('删除成功'); this.articles = this.articles.filter(a => a.id !== id) }
  }
}
</script>
<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 24px; font-weight: 700; color: var(--ink); }
.publish-btn { padding: 10px 20px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.publish-btn:hover { background: var(--primary-hover); }
.table-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 16px; box-shadow: var(--shadow); }
.table-btn { padding: 4px 12px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: transparent; color: var(--ink-secondary); font-size: 13px; cursor: pointer; transition: all 0.15s; margin-right: 4px; }
.table-btn:hover { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.table-btn.danger { color: var(--critical); border-color: var(--critical); }
.table-btn.danger:hover { background: var(--critical-soft); }
</style>
