<template>
  <div class="profile-page">
    <!-- 用户卡片 -->
    <div class="profile-header">
      <div class="header-left">
        <div class="avatar-wrapper" @click="triggerAvatarUpload">
          <img v-if="form.avatar" :src="form.avatar" class="avatar-img" @error="handleAvatarError" />
          <div v-else class="avatar-placeholder" :class="'bg-' + getAvatarColor(form.nickname || form.username)">
            {{ (form.nickname || form.username || '?').charAt(0) }}
          </div>
          <div class="avatar-overlay">
            <span>📷 更换头像</span>
          </div>
          <input ref="avatarInput" type="file" accept="image/*" style="display:none" @change="handleAvatarChange" />
        </div>
        <div class="header-info">
          <h1 class="display-name">{{ form.nickname || form.username }}</h1>
          <p class="join-date">加入于 {{ formatDate(form.createTime) }}</p>
          <button class="edit-btn" @click="activeTab = 'settings'">⚙️ 编辑资料</button>
        </div>
      </div>
      <div class="header-stats">
        <div class="stat-item"><span class="stat-num">{{ stats.topics || 0 }}</span><span class="stat-label">话题</span></div>
        <div class="stat-item"><span class="stat-num">{{ stats.replies || 0 }}</span><span class="stat-label">回复</span></div>
        <div class="stat-item"><span class="stat-num">{{ stats.likes || 0 }}</span><span class="stat-label">点赞</span></div>
        <div class="stat-item"><span class="stat-num">{{ stats.favorites || 0 }}</span><span class="stat-label">收藏</span></div>
        <div class="stat-item"><span class="stat-num highlight">{{ stats.points || 10 }}</span><span class="stat-label">积分</span></div>
      </div>
    </div>

    <!-- 一级标签栏 -->
    <div class="nav-tabs">
      <button v-for="tab in mainTabs" :key="tab.key" class="nav-tab" :class="{ active: activeTab === tab.key }" @click="activeTab = tab.key">
        <span class="tab-icon">{{ tab.icon }}</span> {{ tab.label }}
      </button>
    </div>

    <!-- 二级标签栏（设置页内） -->
    <div class="sub-tabs" v-if="activeTab === 'settings'">
      <button v-for="tab in settingTabs" :key="tab.key" class="sub-tab" :class="{ active: activeSettingTab === tab.key }" @click="activeSettingTab = tab.key">
        <span class="tab-icon">{{ tab.icon }}</span> {{ tab.label }}
      </button>
    </div>

    <!-- 设置内容：账户 -->
    <div class="settings-content" v-if="activeTab === 'settings' && activeSettingTab === 'account'">
      <div class="section-card">
        <h2 class="section-title">账户</h2>
        <p class="section-desc">管理你的账户信息和关联账号</p>
        <h3 class="sub-title">基本信息</h3>

        <!-- 头像上传 -->
        <div class="field-group">
          <div class="avatar-upload-area">
            <div class="avatar-preview" @click="triggerAvatarUpload">
              <img v-if="form.avatar" :src="form.avatar" @error="handleAvatarError" />
              <div v-else class="avatar-placeholder-sm" :class="'bg-' + getAvatarColor(form.nickname || form.username)">
                {{ (form.nickname || form.username || '?').charAt(0) }}
              </div>
              <div class="upload-overlay">📷</div>
            </div>
            <div class="avatar-upload-info">
              <p class="upload-label">头像</p>
              <p class="upload-hint">上传头像</p>
              <button class="upload-btn" @click="triggerAvatarUpload" :disabled="uploading">
                {{ uploading ? '⏳ 上传中...' : '📤 上传头像' }}
              </button>
            </div>
          </div>
        </div>

        <!-- 用户名 -->
        <div class="field-group">
          <label class="field-label">用户名</label>
          <input class="field-input" v-model="form.username" disabled />
          <p class="field-hint">用户名不可修改</p>
        </div>

        <!-- 昵称 -->
        <div class="field-group">
          <label class="field-label">昵称</label>
          <input class="field-input" v-model="form.nickname" />
          <p class="field-hint">显示在个人主页和评论中</p>
        </div>

        <!-- 邮箱 -->
        <div class="field-group">
          <label class="field-label">邮箱</label>
          <input class="field-input" v-model="form.email" placeholder="请输入邮箱" />
          <p class="field-hint">邮箱用于登录和接收通知</p>
        </div>

        <!-- 手机 -->
        <div class="field-group">
          <label class="field-label">手机</label>
          <input class="field-input" v-model="form.phone" placeholder="请输入手机号" />
          <p class="field-hint">绑定手机可用于找回密码</p>
        </div>

        <!-- 个人简介 -->
        <div class="field-group">
          <label class="field-label">个人简介</label>
          <textarea class="field-textarea" v-model="form.bio" rows="3" placeholder="写一段简介，让大家更了解你..."></textarea>
          <p class="field-hint">最多 500 字</p>
        </div>

        <div class="form-actions">
          <button class="save-btn" @click="handleSave">💾 保存更改</button>
        </div>
      </div>
    </div>

    <!-- 个人资料设置 -->
    <div class="settings-content" v-if="activeTab === 'settings' && activeSettingTab === 'profile'">
      <div class="section-card">
        <h2 class="section-title">个人资料</h2>
        <p class="section-desc">管理你公开显示的个人信息</p>
        <div class="field-group">
          <label class="field-label">个人简介</label>
          <textarea class="field-textarea" v-model="form.bio" rows="4" placeholder="写一段简介，让大家更了解你..."></textarea>
        </div>
        <div class="form-actions">
          <button class="save-btn" @click="handleSave">💾 保存更改</button>
        </div>
      </div>
    </div>

    <!-- 概览页 -->
    <div class="settings-content" v-if="activeTab === 'overview'">
      <div class="section-card">
        <h2 class="section-title">📋 概览</h2>
        <p class="section-desc">你的个人活动概览</p>
        <div class="overview-grid">
          <div class="overview-item"><span class="overview-icon">📝</span><span class="overview-num">{{ stats.topics || 0 }}</span><span class="overview-label">发布话题</span></div>
          <div class="overview-item"><span class="overview-icon">💬</span><span class="overview-num">{{ stats.replies || 0 }}</span><span class="overview-label">发表回复</span></div>
          <div class="overview-item"><span class="overview-icon">❤️</span><span class="overview-num">{{ stats.likes || 0 }}</span><span class="overview-label">获得点赞</span></div>
          <div class="overview-item"><span class="overview-icon">⭐</span><span class="overview-num">{{ stats.points || 10 }}</span><span class="overview-label">积分</span></div>
        </div>
      </div>
    </div>

    <!-- 其他标签页占位 -->
    <div class="settings-content" v-if="!['settings', 'overview'].includes(activeTab)">
      <div class="section-card">
        <h2 class="section-title">{{ getTabLabel(activeTab) }}</h2>
        <p class="section-desc">功能开发中，敬请期待...</p>
        <div class="coming-soon">🚧</div>
      </div>
    </div>
  </div>
</template>

<script>
import { userApi, mediaApi } from '../api'
export default {
  name: 'Profile',
  data() {
    return {
      form: {},
      stats: { topics: 0, replies: 0, likes: 0, favorites: 0, points: 10 },
      activeTab: 'settings',
      activeSettingTab: 'account',
      uploading: false,
      mainTabs: [
        { key: 'overview', icon: '👤', label: '概览' },
        { key: 'activity', icon: '📈', label: '活动' },
        { key: 'badges', icon: '🏅', label: '徽章' },
        { key: 'follow', icon: '👥', label: '关注' },
        { key: 'notifications', icon: '🔔', label: '通知' },
        { key: 'invite', icon: '✉️', label: '邀请' },
        { key: 'settings', icon: '⚙️', label: '设置' },
      ],
      settingTabs: [
        { key: 'account', icon: '👤', label: '账户' },
        { key: 'profile', icon: '📝', label: '个人资料' },
        { key: 'security', icon: '🔒', label: '安全' },
        { key: 'interface', icon: '🎨', label: '界面' },
        { key: 'notif_pref', icon: '🔔', label: '通知偏好' },
        { key: 'privacy', icon: '🛡️', label: '隐私' },
      ]
    }
  },
  async created() {
    const res = await userApi.getInfo()
    this.form = res.data
  },
  methods: {
    triggerAvatarUpload() {
      this.$refs.avatarInput.click()
    },
    async handleAvatarChange(e) {
      const file = e.target.files[0]
      if (!file) return
      if (!file.type.startsWith('image/')) {
        return this.$message.warning('请选择图片文件')
      }
      if (file.size > 5 * 1024 * 1024) {
        return this.$message.warning('图片不能超过 5MB')
      }
      // 通过后端上传接口上传头像
      this.uploading = true
      try {
        const res = await mediaApi.uploadImage(file)
        // res.data 是上传后的 URL，如 /uploads/image/xxx.jpg
        this.form.avatar = res.data
        this.$message.success('头像上传成功')
      } catch (err) {
        this.$message.error('头像上传失败：' + (err.message || '未知错误'))
      } finally {
        this.uploading = false
        e.target.value = '' // 清空 input，允许重新选择同一文件
      }
    },
    handleAvatarError(e) {
      e.target.style.display = 'none'
    },
    async handleSave() {
      try {
        // 构造要发送的数据，排除敏感字段
        const saveData = {
          nickname: this.form.nickname,
          avatar: this.form.avatar,
          email: this.form.email,
          phone: this.form.phone,
          bio: this.form.bio
        }
        await userApi.updateInfo(saveData)
        this.$store.commit('SET_USER_INFO', saveData)
        this.$message.success('保存成功')
      } catch (e) { /* handled */ }
    },
    getAvatarColor(name) {
      const colors = ['blue', 'green', 'purple', 'orange', 'pink', 'teal']
      let hash = 0
      for (let i = 0; i < (name || '').length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash)
      return colors[Math.abs(hash) % colors.length]
    },
    formatDate(date) {
      if (!date) return '未知'
      const d = new Date(date)
      return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
    },
    getTabLabel(key) {
      const tab = this.mainTabs.find(t => t.key === key)
      return tab ? tab.label : key
    }
  }
}
</script>

<style scoped>
.profile-page { max-width: 800px; margin: 0 auto; padding: 20px 0; }

.profile-header {
  background: var(--surface); border-radius: var(--rounded-lg); padding: 28px; box-shadow: var(--shadow);
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;
}
.header-left { display: flex; gap: 20px; align-items: center; }
.avatar-wrapper { position: relative; width: 88px; height: 88px; min-width: 88px; border-radius: 50%; overflow: hidden; cursor: pointer; }
.avatar-wrapper .avatar-img { width: 100%; height: 100%; object-fit: cover; border-radius: 50%; }
.avatar-placeholder { width: 100%; height: 100%; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 36px; font-weight: 700; color: #fff; }
.avatar-overlay { position: absolute; bottom: 0; left: 0; right: 0; background: rgba(0,0,0,0.6); color: #fff; text-align: center; padding: 4px; font-size: 11px; opacity: 0; transition: opacity 0.2s; }
.avatar-wrapper:hover .avatar-overlay { opacity: 1; }
.header-info { flex: 1; }
.display-name { font-size: 24px; font-weight: 700; color: var(--ink); margin-bottom: 4px; }
.join-date { font-size: 13px; color: var(--ink-muted); margin-bottom: 10px; }
.edit-btn { padding: 6px 14px; border: 1px solid var(--border); border-radius: var(--rounded-md); background: var(--surface); color: var(--ink-secondary); font-size: 13px; cursor: pointer; transition: all 0.15s; }
.edit-btn:hover { background: var(--canvas); border-color: var(--ink-muted); color: var(--ink); }
.header-stats { display: flex; gap: 24px; }
.stat-item { text-align: center; }
.stat-num { display: block; font-size: 22px; font-weight: 700; color: var(--ink); }
.stat-num.highlight { color: var(--primary); }
.stat-label { font-size: 12px; color: var(--ink-muted); }

.nav-tabs { display: flex; gap: 0; background: var(--surface); border-radius: var(--rounded-lg); padding: 4px; box-shadow: var(--shadow); margin-bottom: 16px; overflow-x: auto; }
.nav-tab { padding: 10px 16px; border: none; background: none; color: var(--ink-secondary); font-size: 14px; font-weight: 500; cursor: pointer; border-radius: var(--rounded-md); transition: all 0.15s; white-space: nowrap; display: flex; align-items: center; gap: 4px; }
.nav-tab:hover { background: var(--canvas); color: var(--ink); }
.nav-tab.active { background: var(--primary); color: #fff; font-weight: 600; }
.tab-icon { font-size: 14px; }

.sub-tabs { display: flex; gap: 0; background: var(--surface); border-radius: var(--rounded-lg); padding: 4px; box-shadow: var(--shadow); margin-bottom: 16px; overflow-x: auto; }
.sub-tab { padding: 8px 14px; border: none; background: none; color: var(--ink-secondary); font-size: 13px; font-weight: 500; cursor: pointer; border-radius: var(--rounded-md); transition: all 0.15s; white-space: nowrap; display: flex; align-items: center; gap: 4px; }
.sub-tab:hover { background: var(--canvas); color: var(--ink); }
.sub-tab.active { background: var(--ink); color: var(--canvas); font-weight: 600; }

.settings-content { margin-top: 0; }
.section-card { background: var(--surface); border-radius: var(--rounded-lg); padding: 28px; box-shadow: var(--shadow); }
.section-title { font-size: 22px; font-weight: 700; color: var(--ink); margin-bottom: 4px; }
.section-desc { font-size: 14px; color: var(--ink-muted); margin-bottom: 24px; }
.sub-title { font-size: 17px; font-weight: 700; color: var(--ink); margin-bottom: 20px; }

.avatar-upload-area { display: flex; align-items: center; gap: 16px; margin-bottom: 28px; }
.avatar-preview { width: 64px; height: 64px; min-width: 64px; border-radius: 50%; overflow: hidden; position: relative; cursor: pointer; border: 2px solid var(--border); }
.avatar-preview img { width: 100%; height: 100%; object-fit: cover; }
.avatar-placeholder-sm { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 24px; font-weight: 700; color: #fff; }
.upload-overlay { position: absolute; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; font-size: 20px; opacity: 0; transition: opacity 0.2s; }
.avatar-preview:hover .upload-overlay { opacity: 1; }
.avatar-upload-info { flex: 1; }
.upload-label { font-size: 15px; font-weight: 600; color: var(--ink); margin-bottom: 2px; }
.upload-hint { font-size: 13px; color: var(--ink-muted); margin-bottom: 8px; }
.upload-btn { padding: 6px 14px; border: 1px solid var(--border); border-radius: var(--rounded-md); background: var(--surface); color: var(--ink-secondary); font-size: 13px; cursor: pointer; transition: all 0.15s; }
.upload-btn:hover:not(:disabled) { background: var(--canvas); border-color: var(--primary); color: var(--primary); }
.upload-btn:disabled { opacity: 0.5; cursor: not-allowed; }

.field-group { margin-bottom: 24px; }
.field-label { display: block; font-size: 14px; font-weight: 600; color: var(--ink); margin-bottom: 6px; }
.field-input { width: 100%; padding: 10px 14px; border: 1px solid var(--border); border-radius: var(--rounded-md); background: var(--surface); color: var(--ink); font-size: 15px; outline: none; transition: border-color 0.15s; }
.field-input:focus { border-color: var(--primary); }
.field-input::placeholder { color: var(--ink-muted); }
.field-input:disabled { background: var(--canvas); color: var(--ink-muted); cursor: not-allowed; }
.field-textarea { width: 100%; padding: 10px 14px; border: 1px solid var(--border); border-radius: var(--rounded-md); background: var(--surface); color: var(--ink); font-size: 15px; outline: none; resize: vertical; font-family: inherit; transition: border-color 0.15s; }
.field-textarea:focus { border-color: var(--primary); }
.field-textarea::placeholder { color: var(--ink-muted); }
.field-hint { font-size: 12px; color: var(--ink-muted); margin-top: 4px; }

.form-actions { padding-top: 16px; border-top: 1px solid var(--border); margin-top: 8px; }
.save-btn { padding: 10px 28px; border: none; border-radius: var(--rounded-full); background: var(--primary); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.15s; }
.save-btn:hover { background: var(--primary-hover); }

.overview-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-top: 16px; }
.overview-item { text-align: center; padding: 20px; background: var(--canvas); border-radius: var(--rounded-lg); }
.overview-icon { font-size: 24px; display: block; margin-bottom: 8px; }
.overview-num { font-size: 28px; font-weight: 700; color: var(--ink); display: block; }
.overview-label { font-size: 13px; color: var(--ink-muted); }
.coming-soon { text-align: center; font-size: 48px; padding: 40px; }

.bg-blue { background: linear-gradient(135deg, #1877f2, #42a5f5); }
.bg-green { background: linear-gradient(135deg, #42b72a, #66bb6a); }
.bg-purple { background: linear-gradient(135deg, #a033c0, #ce93d8); }
.bg-orange { background: linear-gradient(135deg, #f7b928, #ffb74d); }
.bg-pink { background: linear-gradient(135deg, #e41e3f, #ef5350); }
.bg-teal { background: linear-gradient(135deg, #00838f, #4dd0e1); }
</style>
