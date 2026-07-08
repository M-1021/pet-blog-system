<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <el-header class="app-header">
      <div class="header-content">
        <!-- Logo 和标题 -->
        <div class="logo" @click="$router.push('/')">
          <span class="logo-icon">🐾</span>
          <span class="logo-text">宠物博客</span>
        </div>
        <!-- 导航菜单 -->
        <el-menu mode="horizontal" :default-active="$route.path" router class="nav-menu">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/knowledge">宠物知识</el-menu-item>
          <el-menu-item index="/community">在线交流</el-menu-item>
        </el-menu>
        <!-- 用户操作区域 -->
        <div class="user-area">
          <!-- 已登录状态 -->
          <template v-if="isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                {{ userInfo.nickname || userInfo.username }}
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="myArticles">我的文章</el-dropdown-item>
                <el-dropdown-item v-if="userInfo.role === 1" command="admin">后台管理</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <!-- 未登录状态 -->
          <template v-else>
            <el-button type="text" @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" size="small" @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    <!-- 主内容区域 -->
    <el-main class="app-main">
      <router-view />
    </el-main>
    <!-- 底部信息 -->
    <el-footer class="app-footer">
      <p>© 2024 宠物博客系统 - 赣南科技学院课程设计</p>
    </el-footer>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    // 从 Vuex 获取登录状态和用户信息
    isLoggedIn() { return this.$store.getters.isLoggedIn },
    userInfo() { return this.$store.getters.userInfo }
  },
  methods: {
    // 处理下拉菜单命令
    handleCommand(command) {
      switch (command) {
        case 'profile': this.$router.push('/profile'); break
        case 'myArticles': this.$router.push('/my-articles'); break
        case 'admin': this.$router.push('/admin'); break
        case 'logout':
          this.$store.dispatch('logout')
          this.$message.success('已退出登录')
          this.$router.push('/')
          break
      }
    }
  },
  created() {
    // 应用启动时，尝试从 localStorage 恢复登录状态
    this.$store.dispatch('restoreLogin')
  }
}
</script>

<style>
/* 全局样式 */
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: 'Microsoft YaHei', sans-serif; background: #f5f5f5; }
.app-header { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); position: sticky; top: 0; z-index: 100; height: 60px !important; line-height: 60px; }
.header-content { max-width: 1200px; margin: 0 auto; display: flex; align-items: center; height: 60px; }
.logo { cursor: pointer; display: flex; align-items: center; margin-right: 40px; }
.logo-icon { font-size: 28px; margin-right: 8px; }
.logo-text { font-size: 20px; font-weight: bold; color: #409EFF; }
.nav-menu { flex: 1; border-bottom: none !important; }
.nav-menu .el-menu-item { height: 60px; line-height: 60px; }
.user-area { display: flex; align-items: center; gap: 10px; }
.user-dropdown { cursor: pointer; color: #606266; font-size: 14px; }
.app-main { max-width: 1200px; margin: 0 auto; min-height: calc(100vh - 180px); padding: 20px; }
.app-footer { text-align: center; color: #999; font-size: 14px; padding: 20px; }
</style>
