<template>
  <div class="emoji-picker" v-show="visible">
    <div class="emoji-tabs">
      <span v-for="(group, idx) in emojiGroups" :key="idx"
            class="emoji-tab" :class="{ active: activeTab === idx }"
            @click="activeTab = idx">{{ group.icon }}</span>
    </div>
    <div class="emoji-grid">
      <span v-for="(emoji, i) in emojiGroups[activeTab].items" :key="i"
            class="emoji-item" @click="select(emoji)">{{ emoji }}</span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'EmojiPicker',
  props: { visible: { type: Boolean, default: false } },
  data() {
    return {
      activeTab: 0,
      emojiGroups: [
        { icon: '😀', items: ['😀','😃','😄','😁','😆','😅','🤣','😂','🙂','😊','😇','🥰','😍','🤩','😘','😗','😚','😋','😛','😜','🤪','😝','🤑','🤗','🤭','🤫','🤔','🤐','🤨','😐','😑','😶','😏','😒','🙄','😬','😮‍💨','🤥','😌','😔','😪','🤤','😴','😷','🤒','🤕','🤢','🤮','🥵','🥶','🥴','😵','🤯','🤠','🥳','🥸','😎','🤓','🧐'] },
        { icon: '❤️', items: ['❤️','🧡','💛','💚','💙','💜','🖤','🤍','🤎','💔','❤️‍🔥','❤️‍🩹','💕','💞','💓','💗','💖','💘','💝','💟','☮️','✝️','☪️','🕉️','☸️','✡️','🔯','🕎','☯️','☦️','🛐','⛎','♈','♉','♊','♋','♌','♍','♎','♏','♐','♑','♒','♓','🆔','⚛️'] },
        { icon: '🐶', items: ['🐶','🐱','🐭','🐹','🐰','🦊','🐻','🐼','🐻‍❄️','🐨','🐯','🦁','🐮','🐷','🐸','🐵','🙈','🙉','🙊','🐒','🐔','🐧','🐦','🐤','🐣','🐥','🦆','🦅','🦉','🦇','🐺','🐗','🐴','🦄','🐝','🪱','🐛','🦋','🐌','🐞','🐜','🪰','🪲','🪳','🦟','🦗','🕷️','🦂','🐢','🐍','🦎','🦖','🦕','🐙','🦑','🦐','🦞','🦀','🐡','🐠','🐟','🐬','🐳','🐋','🦈','🦭','🐊'] },
        { icon: '🍕', items: ['🍏','🍎','🍐','🍊','🍋','🍌','🍉','🍇','🍓','🫐','🍈','🍒','🍑','🥭','🍍','🥥','🥝','🍅','🍆','🥑','🥦','🥬','🥒','🌶️','🫑','🌽','🥕','🧄','🧅','🥔','🍠','🥐','🥯','🍞','🥖','🥨','🧀','🥚','🍳','🧈','🥞','🧇','🥓','🥩','🍗','🍖','🌭','🍔','🍟','🍕','🫓','🥪','🥙','🧆','🌮','🌯','🫔','🥗','🥘'] },
        { icon: '⚽', items: ['⚽','🏀','🏈','⚾','🥎','🎾','🏐','🏉','🥏','🎱','🪀','🏓','🏸','🏒','🏑','🥍','🏏','🪃','🥅','⛳','🪁','🏹','🎣','🤿','🥊','🥋','🎽','🛹','🛼','🛷','⛸️','🥌','🎿','⛷️','🏂','🪂','🏋️','🤸','🤺','⛹️','🤾','🏌️','🏇','🧘','🏄','🏊','🤽','🚣','🧗','🚵','🚴','🏆','🥇','🥈','🥉','🏅','🎖️','🏵️','🎗️'] },
        { icon: '🏠', items: ['🏠','🏡','🏢','🏣','🏤','🏥','🏦','🏨','🏩','🏪','🏫','🏬','🏭','🏯','🏰','💒','🗼','🗽','⛪','🕌','🛕','🕍','⛩️','🕋','⛲','⛺','🌁','🌃','🏙️','🌄','🌅','🌆','🌇','🌉','🏔️','⛰️','🌋','🗻','🏕️','🏖️','🏜️','🏝️','🏞️','🛤️','🛣️','🗾','🌍','🌎','🌏','🌐','🗺️'] },
        { icon: '👋', items: ['👋','🤚','🖐️','✋','🖖','👌','🤌','🤏','✌️','🤞','🤟','🤘','🤙','👈','👉','👆','🖕','👇','☝️','👍','👎','✊','👊','🤛','🤜','👏','🙌','👐','🤲','🤝','🙏','✍️','💅','🤳','💪','🦾','🦿','🦵','🦶','👂','🦻','👃','🧠','🫀','🫁','🦷','🦴','👀','👁️','👅','👄'] }
      ]
    }
  },
  methods: {
    select(emoji) { this.$emit('select', emoji) }
  }
}
</script>
<style scoped>
.emoji-picker { position: absolute; bottom: 100%; left: 0; z-index: 100; background: var(--surface); border: 1px solid var(--border); border-radius: var(--rounded-lg); box-shadow: var(--shadow-lg); padding: 8px; width: 340px; margin-bottom: 8px; }
.emoji-tabs { display: flex; gap: 4px; padding-bottom: 6px; border-bottom: 1px solid var(--border); margin-bottom: 6px; }
.emoji-tab { cursor: pointer; padding: 4px 6px; border-radius: var(--rounded-sm); font-size: 18px; transition: background 0.15s; }
.emoji-tab:hover, .emoji-tab.active { background: var(--primary-soft); }
.emoji-grid { display: grid; grid-template-columns: repeat(8, 1fr); gap: 2px; max-height: 200px; overflow-y: auto; }
.emoji-item { cursor: pointer; padding: 4px; text-align: center; font-size: 20px; border-radius: var(--rounded-sm); transition: background 0.15s; }
.emoji-item:hover { background: var(--primary-soft); }
.emoji-grid::-webkit-scrollbar { width: 6px; }
.emoji-grid::-webkit-scrollbar-thumb { background: var(--border); border-radius: 3px; }
</style>
