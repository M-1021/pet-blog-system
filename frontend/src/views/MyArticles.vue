<template>
  <div class="my-articles">
    <div class="header-row">
      <h2>我的文章</h2>
      <el-button type="primary" icon="el-icon-edit" @click="$router.push('/publish')">写文章</el-button>
    </div>
    <el-table :data="articles" style="width:100%">
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" @click="$router.push('/article/' + scope.row.id)">查看</el-button>
          <el-button type="text" @click="$router.push('/publish?id=' + scope.row.id)">编辑</el-button>
          <el-button type="text" class="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="articles.length === 0" description="还没有文章">
      <el-button type="primary" @click="$router.push('/publish')">去写文章</el-button>
    </el-empty>
  </div>
</template>

<script>
import { articleApi } from '../api'
export default {
  name: 'MyArticles',
  data() { return { articles: [] } },
  async created() {
    const res = await articleApi.getMyList()
    this.articles = res.data
  },
  methods: {
    async handleDelete(id) {
      await this.$confirm('确定删除这篇文章吗？', '提示', { type: 'warning' })
      await articleApi.delete(id)
      this.$message.success('删除成功')
      this.articles = this.articles.filter(a => a.id !== id)
    }
  }
}
</script>

<style scoped>
.header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.danger { color: #F56C6C !important; }
</style>
