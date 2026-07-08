<template>
  <div class="admin">
    <h2 class="page-title">⚙️ 后台管理</h2>
    <div class="admin-card">
      <el-tabs v-model="activeTab" @tab-click="loadData">
        <el-tab-pane label="用户管理" name="users">
          <el-table :data="users" style="width:100%">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="nickname" label="昵称"></el-table-column>
            <el-table-column prop="role" label="角色" width="80"><template slot-scope="scope">{{ scope.row.role === 1 ? '管理员' : '用户' }}</template></el-table-column>
            <el-table-column prop="createTime" label="注册时间" width="180"></el-table-column>
            <el-table-column label="操作" width="100"><template slot-scope="scope"><button class="table-btn danger" @click="deleteUser(scope.row.id)">删除</button></template></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="分类管理" name="categories">
          <button class="add-btn" @click="showCatDialog = true">➕ 添加分类</button>
          <el-table :data="categories" style="width:100%">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="name" label="分类名称"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
            <el-table-column label="操作" width="150"><template slot-scope="scope"><button class="table-btn" @click="editCategory(scope.row)">编辑</button><button class="table-btn danger" @click="deleteCategory(scope.row.id)">删除</button></template></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="举报管理" name="reports">
          <el-table :data="reports" style="width:100%">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="articleTitle" label="被举报文章"></el-table-column>
            <el-table-column prop="userName" label="举报者"></el-table-column>
            <el-table-column prop="reason" label="举报原因"></el-table-column>
            <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope">{{ scope.row.status === 0 ? '待处理' : '已处理' }}</template></el-table-column>
            <el-table-column label="操作" width="200"><template slot-scope="scope" v-if="scope.row.status === 0"><button class="table-btn" @click="handleReport(scope.row.id, 1)">驳回</button><button class="table-btn danger" @click="handleReport(scope.row.id, 2)">删除文章</button></template></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog :title="catForm.id ? '编辑分类' : '添加分类'" :visible.sync="showCatDialog" width="400px">
      <el-form :model="catForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="catForm.name"></el-input></el-form-item>
        <el-form-item label="描述"><el-input v-model="catForm.description"></el-input></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="catForm.sortOrder" :min="0"></el-input-number></el-form-item>
      </el-form>
      <span slot="footer">
        <button class="dialog-btn cancel" @click="showCatDialog = false">取消</button>
        <button class="dialog-btn primary" @click="saveCategory">保存</button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { userApi, categoryApi, reportApi } from '../api'
export default {
  name: 'Admin',
  data() { return { activeTab: 'users', users: [], categories: [], reports: [], showCatDialog: false, catForm: { name: '', description: '', sortOrder: 0 } } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      if (this.activeTab === 'users') { const res = await userApi.getList({ page: 1, size: 100 }); this.users = res.data.records }
      else if (this.activeTab === 'categories') { const res = await categoryApi.getList(); this.categories = res.data }
      else if (this.activeTab === 'reports') { const res = await reportApi.getList({ page: 1, size: 100 }); this.reports = res.data.records }
    },
    async deleteUser(id) { await this.$confirm('确定删除该用户？', '提示', { type: 'warning' }); await userApi.deleteUser(id); this.$message.success('删除成功'); this.loadData() },
    editCategory(row) { this.catForm = { ...row }; this.showCatDialog = true },
    async saveCategory() { if (this.catForm.id) await categoryApi.update(this.catForm); else await categoryApi.create(this.catForm); this.$message.success('保存成功'); this.showCatDialog = false; this.catForm = { name: '', description: '', sortOrder: 0 }; this.loadData() },
    async deleteCategory(id) { await this.$confirm('确定删除该分类？', '提示', { type: 'warning' }); await categoryApi.delete(id); this.$message.success('删除成功'); this.loadData() },
    async handleReport(id, action) { const msg = action === 1 ? '驳回该举报？' : '删除被举报的文章？'; await this.$confirm(msg, '提示', { type: 'warning' }); await reportApi.handle(id, action); this.$message.success('处理成功'); this.loadData() }
  }
}
</script>
<style scoped>
.page-title { font-size: 24px; font-weight: 700; color: var(--ink); margin-bottom: 20px; }
.admin-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 20px; box-shadow: var(--shadow); }
.add-btn { padding: 8px 18px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; margin-bottom: 16px; transition: background 0.15s; }
.add-btn:hover { background: var(--primary-hover); }
.table-btn { padding: 4px 12px; border: 1px solid var(--border); border-radius: var(--rounded-full); background: transparent; color: var(--ink-secondary); font-size: 13px; cursor: pointer; transition: all 0.15s; margin-right: 4px; }
.table-btn:hover { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.table-btn.danger { color: var(--critical); border-color: var(--critical); }
.table-btn.danger:hover { background: var(--critical-soft); }
.dialog-btn { padding: 8px 20px; border-radius: var(--rounded-full); font-size: 14px; font-weight: 600; cursor: pointer; margin-left: 8px; }
.dialog-btn.cancel { border: 1px solid var(--border); background: var(--surface); color: var(--ink); }
.dialog-btn.primary { border: none; background: var(--primary); color: #fff; }
</style>
