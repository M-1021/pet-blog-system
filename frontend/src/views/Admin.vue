<template>
  <div class="admin">
    <h2>🔧 后台管理</h2>
    <el-tabs v-model="activeTab" @tab-click="loadData">
      <!-- 用户管理 -->
      <el-tab-pane label="用户管理" name="users">
        <el-table :data="users" style="width:100%">
          <el-table-column prop="id" label="ID" width="60"></el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column prop="nickname" label="昵称"></el-table-column>
          <el-table-column prop="role" label="角色" width="80">
            <template slot-scope="scope">{{ scope.row.role === 1 ? '管理员' : '用户' }}</template>
          </el-table-column>
          <el-table-column prop="createTime" label="注册时间" width="180"></el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" class="danger" @click="deleteUser(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 分类管理 -->
      <el-tab-pane label="分类管理" name="categories">
        <el-button type="primary" size="small" @click="showCatDialog = true" style="margin-bottom:15px">添加分类</el-button>
        <el-table :data="categories" style="width:100%">
          <el-table-column prop="id" label="ID" width="60"></el-table-column>
          <el-table-column prop="name" label="分类名称"></el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
          <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="editCategory(scope.row)">编辑</el-button>
              <el-button type="text" class="danger" @click="deleteCategory(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 举报管理 -->
      <el-tab-pane label="举报管理" name="reports">
        <el-table :data="reports" style="width:100%">
          <el-table-column prop="id" label="ID" width="60"></el-table-column>
          <el-table-column prop="articleTitle" label="被举报文章"></el-table-column>
          <el-table-column prop="userName" label="举报者"></el-table-column>
          <el-table-column prop="reason" label="举报原因"></el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">{{ scope.row.status === 0 ? '待处理' : '已处理' }}</template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope" v-if="scope.row.status === 0">
              <el-button type="text" @click="handleReport(scope.row.id, 1)">驳回</el-button>
              <el-button type="text" class="danger" @click="handleReport(scope.row.id, 2)">删除文章</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 分类编辑对话框 -->
    <el-dialog :title="catForm.id ? '编辑分类' : '添加分类'" :visible.sync="showCatDialog" width="400px">
      <el-form :model="catForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="catForm.name"></el-input></el-form-item>
        <el-form-item label="描述"><el-input v-model="catForm.description"></el-input></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="catForm.sortOrder" :min="0"></el-input-number></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="showCatDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { userApi, categoryApi, reportApi } from '../api'
export default {
  name: 'Admin',
  data() {
    return {
      activeTab: 'users',
      users: [], categories: [], reports: [],
      showCatDialog: false,
      catForm: { name: '', description: '', sortOrder: 0 }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      if (this.activeTab === 'users') {
        const res = await userApi.getList({ page: 1, size: 100 })
        this.users = res.data.records
      } else if (this.activeTab === 'categories') {
        const res = await categoryApi.getList()
        this.categories = res.data
      } else if (this.activeTab === 'reports') {
        const res = await reportApi.getList({ page: 1, size: 100 })
        this.reports = res.data.records
      }
    },
    async deleteUser(id) {
      await this.$confirm('确定删除该用户？', '提示', { type: 'warning' })
      await userApi.deleteUser(id)
      this.$message.success('删除成功')
      this.loadData()
    },
    editCategory(row) {
      this.catForm = { ...row }
      this.showCatDialog = true
    },
    async saveCategory() {
      if (this.catForm.id) await categoryApi.update(this.catForm)
      else await categoryApi.create(this.catForm)
      this.$message.success('保存成功')
      this.showCatDialog = false
      this.catForm = { name: '', description: '', sortOrder: 0 }
      this.loadData()
    },
    async deleteCategory(id) {
      await this.$confirm('确定删除该分类？', '提示', { type: 'warning' })
      await categoryApi.delete(id)
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleReport(id, action) {
      const msg = action === 1 ? '驳回该举报？' : '删除被举报的文章？'
      await this.$confirm(msg, '提示', { type: 'warning' })
      await reportApi.handle(id, action)
      this.$message.success('处理成功')
      this.loadData()
    }
  }
}
</script>

<style scoped>
.admin h2 { margin-bottom: 20px; }
.danger { color: #F56C6C !important; }
</style>
