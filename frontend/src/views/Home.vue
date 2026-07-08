<template>
  <div class="home">
    <!-- 欢迎横幅 -->
    <div class="welcome-header">
      <h1 class="forum-title">🐾 宠物论坛</h1>
      <p class="forum-subtitle">分享宠物知识，交流养宠心得</p>
      <div class="stats-bar">
        <span class="stat-item">
          <span class="stat-dot green"></span>
          话题 {{ stats.topicCount || 0 }}
        </span>
        <span class="stat-sep">/</span>
        <span class="stat-item">
          <span class="stat-dot blue"></span>
          帖子 {{ stats.postCount || 0 }}
        </span>
        <span class="stat-sep">/</span>
        <span class="stat-item">
          <span class="stat-dot purple"></span>
          用户 {{ stats.userCount || 0 }}
        </span>
        <span class="stat-sep">/</span>
        <span class="stat-item">
          <span class="stat-dot orange"></span>
          互动 {{ stats.interactionCount || 0 }}
        </span>
      </div>
    </div>

    <!-- 内容切换标签 -->
    <div class="content-tabs">
      <button v-for="tab in tabs" :key="tab.key" class="tab-btn" :class="{ active: activeTab === tab.key }" @click="activeTab = tab.key">{{ tab.label }}</button>
    </div>

    <!-- 今日标记 -->
    <div class="day-marker" v-if="activeTab === 'latest'">
      <span class="marker-icon">✨</span> 今天
    </div>

    <!-- 话题列表 -->
    <div class="topic-list">
      <div class="topic-header">
        <span class="col-title">话题</span>
        <span class="col-author">作者</span>
        <span class="col-replies">回复</span>
        <span class="col-views">浏览量</span>
        <span class="col-activity">活动</span>
      </div>

      <div v-for="item in filteredArticles" :key="item.id" class="topic-row" @click="$router.push('/article/' + item.id)">
        <div class="topic-main">
          <h3 class="topic-title">
            <span class="topic-pin" v-if="item.pinned">📌</span>
            {{ item.title }}
          </h3>
          <div class="topic-tags" v-if="item.categoryName">
            <span class="tag" :class="'tag-' + getTagColor(item.categoryName)">{{ item.categoryName }}</span>
          </div>
          <p class="topic-excerpt">{{ item.content | excerpt }}</p>
        </div>
        <div class="topic-author">
          <div class="author-avatar" :class="'avatar-' + getTagColor(item.authorName)">{{ (item.authorName || '匿').charAt(0) }}</div>
          <span class="author-name">{{ item.authorName }}</span>
        </div>
        <div class="topic-replies">{{ item.commentCount || 0 }}</div>
        <div class="topic-views">{{ item.viewCount || 0 }}</div>
        <div class="topic-activity">{{ formatTime(item.createTime) }}</div>
      </div>

      <el-empty v-if="filteredArticles.length === 0" description="暂无内容"></el-empty>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > pageSize">
      <button class="page-btn" :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">上一页</button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button class="page-btn" :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)">下一页</button>
    </div>
  </div>
</template>

<script>
import { articleApi } from '../api'
export default {
  name: 'Home',
  data() {
    return {
      articles: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      activeTab: 'latest',
      stats: { topicCount: 0, postCount: 0, userCount: 0, interactionCount: 0 },
      tabs: [
        { key: 'latest', label: '最新动态' },
        { key: 'newest', label: '最新发布' },
        { key: 'hot', label: '社区推荐' }
      ]
    }
  },
  filters: {
    excerpt(val) { return val ? val.substring(0, 80) + '...' : '' }
  },
  computed: {
    filteredArticles() {
      if (this.activeTab === 'hot') {
        return [...this.articles].sort((a, b) => (b.likeCount || 0) - (a.likeCount || 0))
      }
      return this.articles
    },
    totalPages() { return Math.ceil(this.total / this.pageSize) }
  },
  async created() {
    await this.loadArticles()
    this.stats.topicCount = this.total || 0
  },
  methods: {
    async loadArticles() {
      const res = await articleApi.getList({ page: this.currentPage, size: this.pageSize })
      this.articles = res.data.records || []
      this.total = res.data.total || 0
    },
    changePage(page) {
      this.currentPage = page
      this.loadArticles()
    },
    formatTime(time) {
      if (!time) return ''
      const diff = Date.now() - new Date(time).getTime()
      const days = Math.floor(diff / 86400000)
      if (days > 0) return days + '天前'
      const hours = Math.floor(diff / 3600000)
      if (hours > 0) return hours + '小时前'
      const mins = Math.floor(diff / 60000)
      return mins > 0 ? mins + '分钟前' : '刚刚'
    },
    getTagColor(name) {
      const colors = ['blue', 'green', 'purple', 'orange', 'pink', 'teal']
      let hash = 0
      for (let i = 0; i < (name || '').length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash)
      return colors[Math.abs(hash) % colors.length]
    }
  }
}
</script>

<style scoped>
.home { padding: 0; }

.welcome-header {
  text-align: center;
  padding: 40px 20px 24px;
  background: var(--surface);
  border-radius: var(--rounded-lg);
  box-shadow: var(--shadow);
  margin-bottom: 20px;
}

.forum-title {
  font-size: 36px;
  font-weight: 800;
  color: var(--ink);
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.forum-subtitle {
  font-size: 16px;
  color: var(--ink-secondary);
  margin-bottom: 20px;
}

.stats-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 14px;
  color: var(--ink-secondary);
}

.stat-item { display: flex; align-items: center; gap: 5px; font-weight: 500; }

.stat-dot { width: 8px; height: 8px; border-radius: 50%; }
.stat-dot.green { background: var(--success); }
.stat-dot.blue { background: var(--primary); }
.stat-dot.purple { background: var(--accent-purple); }
.stat-dot.orange { background: var(--accent-orange); }

.stat-sep { color: var(--ink-muted); }

.content-tabs {
  display: flex;
  justify-content: center;
  gap: 0;
  background: var(--surface);
  border-radius: var(--rounded-lg);
  box-shadow: var(--shadow);
  margin-bottom: 20px;
  padding: 4px;
}

.tab-btn {
  padding: 10px 28px;
  border: none;
  background: none;
  color: var(--ink-secondary);
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  border-radius: var(--rounded-md);
  transition: all 0.15s;
}

.tab-btn:hover { background: var(--canvas); color: var(--ink); }

.tab-btn.active {
  background: var(--primary);
  color: #fff;
  font-weight: 600;
}

.day-marker {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 14px 0;
  font-size: 14px;
  font-weight: 600;
  color: var(--ink);
  border-bottom: 1px solid var(--border);
  margin-bottom: 0;
}

.marker-icon { font-size: 16px; }

.topic-header {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--border);
  font-size: 13px;
  font-weight: 600;
  color: var(--ink-secondary);
}

.col-title { flex: 1; }
.col-author { width: 100px; text-align: center; }
.col-replies { width: 70px; text-align: center; }
.col-views { width: 80px; text-align: center; }
.col-activity { width: 80px; text-align: center; }

.topic-row {
  display: flex;
  align-items: flex-start;
  padding: 16px 0;
  border-bottom: 1px solid var(--border-light);
  cursor: pointer;
  transition: background 0.1s;
}

.topic-row:hover { background: var(--primary-soft); border-radius: var(--rounded-md); }

.topic-main { flex: 1; min-width: 0; padding-right: 16px; }

.topic-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--ink);
  margin-bottom: 6px;
  line-height: 1.4;
}

.topic-pin { margin-right: 4px; }

.topic-tags { display: flex; gap: 6px; margin-bottom: 6px; flex-wrap: wrap; }

.tag {
  display: inline-block;
  padding: 2px 10px;
  border-radius: var(--rounded-full);
  font-size: 12px;
  font-weight: 500;
}

.tag-blue { background: var(--primary-soft); color: var(--primary); }
.tag-green { background: var(--success-soft); color: var(--success); }
.tag-purple { background: #f3e8ff; color: var(--accent-purple); }
.tag-orange { background: #fff8e1; color: #e67700; }
.tag-pink { background: var(--critical-soft); color: var(--accent-pink); }
.tag-teal { background: #e0f7fa; color: #00838f; }

.topic-excerpt { font-size: 13px; color: var(--ink-secondary); line-height: 1.5; }

.topic-author {
  width: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.author-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: #fff;
}

.avatar-blue { background: linear-gradient(135deg, #1877f2, #42a5f5); }
.avatar-green { background: linear-gradient(135deg, #42b72a, #66bb6a); }
.avatar-purple { background: linear-gradient(135deg, #a033c0, #ce93d8); }
.avatar-orange { background: linear-gradient(135deg, #f7b928, #ffb74d); }
.avatar-pink { background: linear-gradient(135deg, #e41e3f, #ef5350); }
.avatar-teal { background: linear-gradient(135deg, #00838f, #4dd0e1); }

.author-name { font-size: 12px; color: var(--ink-secondary); text-align: center; word-break: break-all; }

.topic-replies, .topic-views, .topic-activity {
  width: 70px;
  text-align: center;
  font-size: 14px;
  color: var(--ink-secondary);
  padding-top: 6px;
}

.topic-views { width: 80px; }
.topic-activity { width: 80px; font-size: 12px; color: var(--ink-muted); }

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 20px 0;
}

.page-btn {
  padding: 8px 20px;
  border: 1px solid var(--border);
  border-radius: var(--rounded-full);
  background: var(--surface);
  color: var(--ink);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
  box-shadow: var(--shadow-sm);
}

.page-btn:hover:not(:disabled) { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-info { font-size: 14px; color: var(--ink-secondary); font-weight: 500; }
</style>
