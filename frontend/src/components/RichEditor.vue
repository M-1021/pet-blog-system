<template>
  <div class="rich-editor">
    <div class="editor-toolbar">
      <button type="button" class="toolbar-btn" title="插入图片" @click="$refs.imageInput.click()">
        🖼️ 图片
      </button>
      <button type="button" class="toolbar-btn" title="插入视频" @click="$refs.videoInput.click()">
        🎬 视频
      </button>
      <div class="toolbar-emoji" style="position:relative;display:inline-block;">
        <button type="button" class="toolbar-btn" title="插入表情" @click="showEmoji = !showEmoji">
          😊 表情
        </button>
        <emoji-picker :visible="showEmoji" @select="insertEmoji" />
      </div>
      <span class="toolbar-hint">支持插入图片、视频和表情</span>
      <input ref="imageInput" type="file" accept="image/*" style="display:none" @change="handleImageUpload" />
      <input ref="videoInput" type="file" accept="video/*" style="display:none" @change="handleVideoUpload" />
    </div>
    <div class="editor-area" ref="editor" contenteditable="true"
         :data-placeholder="placeholder"
         @input="onInput" @paste="onPaste"
         @focus="showEmoji = false"></div>
    <div class="upload-status" v-if="uploading">
      <span class="upload-spinner"></span> 正在上传...
    </div>
  </div>
</template>
<script>
import { mediaApi } from '../api'
import EmojiPicker from './EmojiPicker'
export default {
  name: 'RichEditor',
  components: { EmojiPicker },
  props: {
    value: { type: String, default: '' },
    placeholder: { type: String, default: '请输入内容...' }
  },
  data() { return { showEmoji: false, uploading: false } },
  watch: {
    value(val) {
      if (this.$refs.editor && val !== this.$refs.editor.innerHTML) {
        this.$refs.editor.innerHTML = val
      }
    }
  },
  mounted() {
    if (this.value) {
      this.$refs.editor.innerHTML = this.value
    }
    document.addEventListener('click', this.handleDocClick)
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleDocClick)
  },
  methods: {
    handleDocClick(e) {
      if (this.showEmoji && !this.$el.contains(e.target)) {
        this.showEmoji = false
      }
    },
    onInput() {
      this.$emit('input', this.$refs.editor.innerHTML)
    },
    onPaste(e) {
      const items = e.clipboardData && e.clipboardData.items
      if (!items) return
      for (const item of items) {
        if (item.type.startsWith('image/')) {
          e.preventDefault()
          const file = item.getAsFile()
          this.uploadAndInsert(file, 'image')
          return
        }
      }
    },
    insertEmoji(emoji) {
      this.showEmoji = false
      this.$refs.editor.focus()
      document.execCommand('insertText', false, emoji)
      this.$emit('input', this.$refs.editor.innerHTML)
    },
    async handleImageUpload(e) {
      const file = e.target.files[0]
      if (!file) return
      await this.uploadAndInsert(file, 'image')
      e.target.value = ''
    },
    async handleVideoUpload(e) {
      const file = e.target.files[0]
      if (!file) return
      await this.uploadAndInsert(file, 'video')
      e.target.value = ''
    },
    async uploadAndInsert(file, type) {
      this.uploading = true
      try {
        let res
        if (type === 'image') {
          res = await mediaApi.uploadImage(file)
        } else {
          res = await mediaApi.uploadVideo(file)
        }
        const url = res.data
        this.$refs.editor.focus()
        if (type === 'image') {
          document.execCommand('insertHTML', false,
            '<img src="' + url + '" style="max-width:100%;border-radius:8px;margin:8px 0;" />')
        } else {
          document.execCommand('insertHTML', false,
            '<video src="' + url + '" controls style="max-width:100%;border-radius:8px;margin:8px 0;"></video>')
        }
        this.$emit('input', this.$refs.editor.innerHTML)
        this.$message.success('上传成功')
      } catch (err) {
        this.$message.error('上传失败')
      } finally {
        this.uploading = false
      }
    },
    clear() {
      this.$refs.editor.innerHTML = ''
      this.$emit('input', '')
    }
  }
}
</script>
<style scoped>
.rich-editor { border: 1px solid var(--border); border-radius: var(--rounded-md); overflow: hidden; }
.editor-toolbar { display: flex; align-items: center; gap: 4px; padding: 8px 12px; background: var(--canvas); border-bottom: 1px solid var(--border); flex-wrap: wrap; }
.toolbar-btn { padding: 4px 10px; border: 1px solid var(--border); border-radius: var(--rounded-md); background: var(--surface); color: var(--ink-secondary); font-size: 13px; cursor: pointer; transition: all 0.15s; white-space: nowrap; }
.toolbar-btn:hover { background: var(--primary-soft); border-color: var(--primary); color: var(--primary); }
.toolbar-hint { font-size: 12px; color: var(--ink-muted); margin-left: auto; }
.editor-area { min-height: 200px; max-height: 500px; overflow-y: auto; padding: 12px; font-size: 15px; line-height: 1.8; color: var(--ink); outline: none; }
.editor-area:empty::before { content: attr(data-placeholder); color: var(--ink-muted); pointer-events: none; }
.editor-area img, .editor-area video { display: block; max-width: 100%; border-radius: 8px; margin: 8px 0; }
.upload-status { display: flex; align-items: center; gap: 8px; padding: 8px 12px; font-size: 13px; color: var(--primary); background: var(--primary-soft); }
.upload-spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid var(--border); border-top-color: var(--primary); border-radius: 50%; animation: spin 0.6s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
</style>
