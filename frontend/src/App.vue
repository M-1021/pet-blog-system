<template>
  <div id="app" class="app-layout">
    <!-- 移动端顶部栏 -->
    <div class="mobile-header">
      <button class="hamburger" @click="mobileMenuOpen = !mobileMenuOpen">☰</button>
      <span class="mobile-logo">🐾 宠物论坛</span>
    </div>

    <!-- 左侧导航栏 -->
    <aside class="sidebar" :class="{ 'mobile-open': mobileMenuOpen }">
      <div class="sidebar-logo" @click="$router.push('/')">
        <span class="logo-icon">🐾</span>
        <span class="logo-text">宠物论坛</span>
      </div>
      <nav class="sidebar-nav">
        <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }" @click.native="mobileMenuOpen = false">
          <i class="nav-icon">🏠</i><span>首页</span>
        </router-link>
        <router-link to="/knowledge" class="nav-item" :class="{ active: $route.path === '/knowledge' }" @click.native="mobileMenuOpen = false">
          <i class="nav-icon">📚</i><span>宠物知识</span>
        </router-link>
        <router-link to="/community" class="nav-item" :class="{ active: $route.path === '/community' }" @click.native="mobileMenuOpen = false">
          <i class="nav-icon">💬</i><span>在线交流</span>
        </router-link>
        <template v-if="isLoggedIn">
          <router-link to="/publish" class="nav-item" :class="{ active: $route.path === '/publish' }" @click.native="mobileMenuOpen = false">
            <i class="nav-icon">✏️</i><span>发布文章</span>
          </router-link>
          <router-link to="/my-articles" class="nav-item" :class="{ active: $route.path === '/my-articles' }" @click.native="mobileMenuOpen = false">
            <i class="nav-icon">📄</i><span>我的文章</span>
          </router-link>
          <router-link to="/profile" class="nav-item" :class="{ active: $route.path === '/profile' }" @click.native="mobileMenuOpen = false">
            <i class="nav-icon">👤</i><span>个人中心</span>
          </router-link>
          <router-link v-if="userInfo.role === 1" to="/admin" class="nav-item" :class="{ active: $route.path === '/admin' }" @click.native="mobileMenuOpen = false">
            <i class="nav-icon">⚙️</i><span>后台管理</span>
          </router-link>
        </template>
      </nav>
      <div class="sidebar-footer">
        <template v-if="isLoggedIn">
          <div class="user-info">
            <div class="user-avatar">{{ (userInfo.nickname || userInfo.username || '').charAt(0) }}</div>
            <span class="user-name">{{ userInfo.nickname || userInfo.username }}</span>
          </div>
          <button class="btn-logout" @click="handleLogout">退出登录</button>
        </template>
        <template v-else>
          <router-link to="/login" class="login-btn" @click.native="mobileMenuOpen = false">
            <i class="nav-icon">→</i><span>去登录</span>
          </router-link>
        </template>
      </div>
    </aside>

    <!-- 移动端菜单遮罩 -->
    <div class="mobile-overlay" v-if="mobileMenuOpen" @click="mobileMenuOpen = false"></div>

    <!-- 中间主内容 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 右侧信息栏 -->
    <aside class="right-sidebar">
      <div class="sidebar-widget search-widget">
        <div class="search-box">
          <input type="text" v-model="searchKeyword" placeholder="🔍 搜索..." class="search-input" @keyup.enter="handleSearch" />
        </div>
      </div>
      <div class="sidebar-widget">
        <h4 class="widget-title">话题筛选</h4>
        <select class="filter-select" v-model="filterCategory" @change="handleFilter">
          <option value="">全部分类</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
      </div>
      <div class="sidebar-widget">
        <h4 class="widget-title">最新评论</h4>
        <div v-for="comment in latestComments" :key="comment.id" class="comment-item">
          <div class="comment-avatar">{{ comment.userName.charAt(0) }}</div>
          <div class="comment-content">
            <span class="comment-user">{{ comment.userName }}</span>
            <p class="comment-text">{{ comment.content }}</p>
            <span class="comment-source">来自：{{ comment.articleTitle || comment.postTitle }}</span>
          </div>
        </div>
        <p v-if="latestComments.length === 0" class="empty-tip">暂无评论</p>
      </div>
      <div class="sidebar-widget tips-widget">
        <h4 class="widget-title">📢 公告</h4>
        <p class="tips-text">欢迎来到宠物论坛！这里是爱宠人士的交流社区，分享养宠经验、学习宠物知识。</p>
      </div>
      <div class="sidebar-footer-text">
        <p>&copy; 2024 宠物博客系统 - 赣南科技学院课程设计</p>
      </div>
    </aside>
  </div>
</template>

<script>
import { categoryApi, commentApi } from './api'
export default {
  name: 'App',
  data() {
    return {
      searchKeyword: '',
      filterCategory: '',
      categories: [],
      latestComments: [],
      mobileMenuOpen: false
    }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn },
    userInfo() { return this.$store.getters.userInfo }
  },
  async created() {
    this.$store.dispatch('restoreLogin')
    try {
      const catRes = await categoryApi.getList()
      this.categories = catRes.data || []
    } catch (e) { /* ignore */ }
  },
  methods: {
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    },
    handleSearch() {
      if (this.searchKeyword.trim()) {
        this.$router.push({ path: '/knowledge', query: { keyword: this.searchKeyword } })
      }
    },
    handleFilter() {
      this.$router.push({ path: '/knowledge', query: { category: this.filterCategory } })
    }
  }
}
</script>

<style>
.app-layout {
  display: flex;
  min-height: 100vh;
  background: var(--canvas);
}

/* ========== 左侧导航栏 ========== */
.sidebar {
  width: 220px;
  min-width: 220px;
  background: var(--surface);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  height: 100vh;
  padding: 16px 12px;
  flex-shrink: 0;
  overflow-y: auto;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  padding: 8px 10px 20px;
  cursor: pointer;
}

.logo-icon { font-size: 28px; margin-right: 10px; }
.logo-text { font-size: 20px; font-weight: 800; color: var(--primary); letter-spacing: -0.3px; }

.sidebar-nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  color: var(--ink);
  text-decoration: none;
  font-size: 15px;
  font-weight: 500;
  border-radius: var(--rounded-md);
  transition: background 0.15s;
  gap: 12px;
}

.nav-item:hover {
  background: var(--canvas);
  text-decoration: none;
}

.nav-item.active {
  background: var(--primary-soft);
  color: var(--primary);
  font-weight: 600;
}

.nav-icon { font-size: 20px; width: 28px; text-align: center; }

.sidebar-footer {
  padding: 12px 10px 0;
  border-top: 1px solid var(--border);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
  padding: 6px 0;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--accent-purple));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
}

.user-name { font-size: 14px; color: var(--ink); font-weight: 600; }

.btn-logout {
  width: 100%;
  padding: 8px;
  border: 1px solid var(--border);
  border-radius: var(--rounded-md);
  background: var(--surface);
  color: var(--ink-secondary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-logout:hover { background: var(--canvas); color: var(--ink); }

.login-btn {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  background: var(--primary);
  color: #fff;
  text-decoration: none;
  border-radius: var(--rounded-full);
  font-size: 14px;
  font-weight: 600;
  justify-content: center;
  transition: background 0.15s;
}

.login-btn:hover { background: var(--primary-hover); color: #fff; text-decoration: none; }

/* ========== 主内容区 ========== */
.main-content {
  flex: 1;
  min-width: 0;
  padding: 0 20px;
}

/* ========== 右侧信息栏 ========== */
.right-sidebar {
  width: 280px;
  min-width: 280px;
  padding: 20px 0 20px 16px;
  position: sticky;
  top: 0;
  height: 100vh;
  overflow-y: auto;
  flex-shrink: 0;
}

.sidebar-widget {
  background: var(--surface);
  border-radius: var(--rounded-lg);
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: var(--shadow);
}

.search-widget { padding: 8px; }

.search-input {
  width: 100%;
  padding: 8px 14px;
  border: 1px solid var(--border);
  border-radius: var(--rounded-full);
  background: var(--canvas);
  color: var(--ink);
  font-size: 14px;
  outline: none;
  transition: all 0.2s;
}

.search-input:focus { border-color: var(--primary); box-shadow: 0 0 0 2px var(--primary-soft); }
.search-input::placeholder { color: var(--ink-muted); }

.widget-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 12px;
}

.filter-select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid var(--border);
  border-radius: var(--rounded-md);
  background: var(--canvas);
  color: var(--ink);
  font-size: 14px;
  outline: none;
  margin-bottom: 8px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.filter-select:focus { border-color: var(--primary); }
.filter-select option { background: var(--surface); color: var(--ink); }

.comment-item {
  display: flex;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid var(--border-light);
}

.comment-item:last-child { border-bottom: none; }

.comment-avatar {
  width: 36px;
  height: 36px;
  min-width: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-green), var(--primary));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
}

.comment-content { flex: 1; min-width: 0; }
.comment-user { font-size: 13px; font-weight: 600; color: var(--ink); }
.comment-text { font-size: 12px; color: var(--ink-secondary); margin: 2px 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.comment-source { font-size: 11px; color: var(--ink-muted); }

.empty-tip { font-size: 13px; color: var(--ink-muted); text-align: center; padding: 12px 0; }

.tips-widget .tips-text { font-size: 13px; color: var(--ink-secondary); line-height: 1.6; }

.sidebar-footer-text {
  padding: 12px 0;
  font-size: 12px;
  color: var(--ink-muted);
  text-align: center;
}

/* ==================== 移动端响应式 ==================== */
.mobile-header { display: none; }
.mobile-overlay { display: none; }

@media (max-width: 1024px) {
  .right-sidebar { display: none; }
}

@media (max-width: 768px) {
  .mobile-header {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    background: var(--surface);
    border-bottom: 1px solid var(--border);
    position: sticky;
    top: 0;
    z-index: 1001;
  }
  .hamburger {
    background: none;
    border: 1px solid var(--border);
    border-radius: var(--rounded-md);
    font-size: 22px;
    padding: 4px 10px;
    cursor: pointer;
    color: var(--ink);
  }
  .mobile-logo { font-size: 18px; font-weight: 700; color: var(--primary); }

  .app-layout { flex-direction: column; }

  .sidebar {
    position: fixed;
    left: -260px;
    top: 0;
    height: 100vh;
    z-index: 1002;
    transition: left 0.3s ease;
    box-shadow: none;
    width: 260px;
    min-width: 260px;
  }
  .sidebar.mobile-open {
    left: 0;
    box-shadow: 4px 0 20px rgba(0,0,0,0.15);
  }

  .mobile-overlay {
    display: block;
    position: fixed;
    inset: 0;
    background: rgba(0,0,0,0.4);
    z-index: 1001;
  }

  .right-sidebar { display: none; }

  .main-content {
    padding: 12px;
    width: 100%;
  }
}
</style>