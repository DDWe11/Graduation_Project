<template>
  <div class="chat-modal">
    <el-drawer v-model="isDrawerVisible" :size="isMobile ? '100%' : '480px'" :with-header="false">
      <div class="header">
        <div class="header-left">
          <span class="name">Art Bot</span>
          <div class="status">
            <div class="dot" :class="{ online: isOnline, offline: !isOnline }"></div>
            <span class="status-text">{{ isOnline ? '在线' : '离线' }}</span>
          </div>
        </div>
        <div class="header-right">
          <el-icon class="icon-close" :size="20" @click="closeChat">
            <Close />
          </el-icon>
        </div>
      </div>
      <div class="chat-container">
        <!-- 聊天消息区域 -->
        <div class="chat-messages" ref="messageContainer">
          <template v-for="(message, index) in messages" :key="index">
            <div :class="['message-item', message.isMe ? 'message-right' : 'message-left']">
              <el-avatar :size="32" :src="message.avatar" class="message-avatar" />
              <div class="message-content">
                <div class="message-info">
                  <span class="sender-name">{{ message.sender }}</span>
                  <span class="message-time">{{ message.time }}</span>
                </div>
                <div class="message-text">{{ message.content }}</div>
              </div>
            </div>
          </template>
          <!-- 加载状态指示 -->
          <div v-if="isLoading" class="loading-indicator">
            <el-icon class="is-loading" :size="20">
              <Loading />
            </el-icon>
            <span>AI正在思考中...</span>
          </div>
        </div>

        <!-- 聊天输入区域 -->
        <div class="chat-input">
          <el-input
            v-model="messageText"
            type="textarea"
            :rows="2"
            placeholder="输入消息"
            resize="none"
            :disabled="isLoading"
            @keyup.enter.prevent="sendMessage"
          >
            <template #append>
              <div class="input-actions">
                <el-button :icon="Paperclip" circle plain />
                <el-button :icon="Picture" circle plain />
                <el-button type="primary" @click="sendMessage" :loading="isLoading" v-ripple>
                  {{ isLoading ? '处理中...' : '发送' }}
                </el-button>
              </div>
            </template>
          </el-input>
          <div class="chat-input-actions">
            <div class="left">
              <i class="iconfont-sys">&#xe634;</i>
              <i class="iconfont-sys">&#xe809;</i>
            </div>
            <el-button type="primary" @click="sendMessage" :loading="isLoading" v-ripple>
              {{ isLoading ? '处理中...' : '发送' }}
            </el-button>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
  import { ref, onMounted, computed } from 'vue'
  import { Picture, Paperclip, Loading, Close } from '@element-plus/icons-vue'
  import axios from 'axios'
  import mittBus from '@/utils/mittBus'
  import meAvatar from '@/assets/img/avatar/avatar5.jpg'
  import aiAvatar from '@/assets/img/avatar/avatar10.jpg'
  import { useWindowSize } from '@vueuse/core'

  const { width } = useWindowSize()
  const isMobile = computed(() => width.value < 500)
  const getCurrentTime = () => {
    return new Date().toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    })
  }
  // API配置
  const apiKey = import.meta.env.VITE_DEEPSEEK_API_KEY
  const apiUrl = 'https://api.deepseek.com/chat/completions'

  // 状态管理
  const isDrawerVisible = ref(false)
  const isOnline = ref(true)
  const isLoading = ref(false)
  const messageText = ref('')
  const messages = ref([
    {
      id: 1,
      sender: 'Art Bot',
      content: '你好！我是你的AI助手，有什么我可以帮你的吗？',
      time: getCurrentTime(),
      isMe: false,
      avatar: aiAvatar
    }
  ])

  const messageId = ref(2)
  const userAvatar = ref(meAvatar)
  const messageContainer = ref<HTMLElement | null>(null)

  const sendMessage = async () => {
    const text = messageText.value.trim()
    if (!text || isLoading.value) return

    // 添加用户消息
    addUserMessage(text)
    messageText.value = ''
    scrollToBottom()

    try {
      isLoading.value = true
      isOnline.value = true

      // 构建对话历史
      const history = messages.value.map((msg) => ({
        role: msg.isMe ? 'user' : 'assistant',
        content: msg.content
      }))

      // API请求
      const response = await axios.post(
        apiUrl,
        {
          model: 'deepseek-chat',
          messages: [...history, { role: 'user', content: text }],
          temperature: 0.7,
          max_tokens: 1000
        },
        {
          headers: {
            Authorization: `Bearer ${apiKey}`,
            'Content-Type': 'application/json'
          }
        }
      )

      // 处理响应
      const aiResponse = response.data.choices[0].message.content
      addBotMessage(aiResponse)
    } catch (error) {
      console.error('API调用失败:', error)
      addErrorMessage()
      isOnline.value = false
    } finally {
      isLoading.value = false
      scrollToBottom()
    }
  }

  const addUserMessage = (text: string) => {
    messages.value.push({
      id: messageId.value++,
      sender: 'Ricky',
      content: text,
      time: getCurrentTime(),
      isMe: true,
      avatar: userAvatar.value
    })
  }

  const addBotMessage = (text: string) => {
    messages.value.push({
      id: messageId.value++,
      sender: 'Art Bot',
      content: text,
      time: getCurrentTime(),
      isMe: false,
      avatar: aiAvatar
    })
  }

  const addErrorMessage = () => {
    messages.value.push({
      id: messageId.value++,
      sender: 'System',
      content: '暂时无法连接到AI服务，请稍后再试',
      time: getCurrentTime(),
      isMe: false,
      avatar: aiAvatar
    })
  }

  const scrollToBottom = () => {
    setTimeout(() => {
      if (messageContainer.value) {
        messageContainer.value.scrollTop = messageContainer.value.scrollHeight
      }
    }, 100)
  }

  const openChat = () => {
    isDrawerVisible.value = true
  }

  const closeChat = () => {
    isDrawerVisible.value = false
  }

  onMounted(() => {
    scrollToBottom()
    mittBus.on('openChat', openChat)
  })
</script>

<style lang="scss">
  .chat-modal {
    .el-overlay {
      background-color: rgb(0 0 0 / 20%) !important;
    }
  }
</style>

<style lang="scss" scoped>
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;

    .header-left {
      .name {
        font-size: 16px;
        font-weight: 500;
      }

      .status {
        display: flex;
        gap: 4px;
        align-items: center;
        margin-top: 6px;

        .dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;

          &.online {
            background-color: var(--el-color-success);
          }

          &.offline {
            background-color: var(--el-color-danger);
          }
        }

        .status-text {
          font-size: 12px;
          color: var(--art-gray-500);
        }
      }
    }

    .header-right {
      .icon-close {
        cursor: pointer;
      }
    }
  }

  .chat-container {
    display: flex;
    flex-direction: column;
    height: calc(100% - 40px);

    .chat-messages {
      flex: 1;
      padding: 30px 16px;
      overflow-y: auto;
      border-top: 1px solid var(--el-border-color-lighter);

      &::-webkit-scrollbar {
        width: 5px !important;
      }

      .message-item {
        display: flex;
        flex-direction: row;
        gap: 8px;
        align-items: flex-start;
        width: 100%;
        margin-bottom: 30px;

        .message-text {
          font-size: 14px;
          color: var(--art-gray-900);
          border-radius: 6px;
        }

        &.message-left {
          justify-content: flex-start;

          .message-content {
            align-items: flex-start;

            .message-info {
              flex-direction: row;
            }

            .message-text {
              background-color: #f8f5ff;
            }
          }
        }

        &.message-right {
          flex-direction: row-reverse;

          .message-content {
            align-items: flex-end;

            .message-info {
              flex-direction: row-reverse;
            }

            .message-text {
              background-color: #e9f3ff;
            }
          }
        }

        .message-avatar {
          flex-shrink: 0;
        }

        .message-content {
          display: flex;
          flex-direction: column;
          max-width: 70%;

          .message-info {
            display: flex;
            gap: 8px;
            margin-bottom: 4px;
            font-size: 12px;

            .message-time {
              color: var(--el-text-color-secondary);
            }

            .sender-name {
              font-weight: 500;
            }
          }

          .message-text {
            padding: 10px 14px;
            line-height: 1.4;
          }
        }
      }
    }

    .chat-input {
      padding: 16px;

      .input-actions {
        display: flex;
        gap: 8px;
        padding: 8px 0;
      }

      .chat-input-actions {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 12px;

        .left {
          display: flex;
          align-items: center;

          i {
            margin-right: 20px;
            font-size: 16px;
            color: var(--art-gray-500);
            cursor: pointer;
          }
        }

        el-button {
          min-width: 80px;
        }
      }
    }
  }

  .loading-indicator {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px;
    color: #666;
    font-size: 14px;
    background: #f5f7fa;
    border-radius: 4px;
    margin: 8px 16px;
    transition: all 0.3s;

    .is-loading {
      animation: rotating 2s linear infinite;
    }
  }

  @keyframes rotating {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }

  .dark {
    .chat-container {
      .chat-messages {
        .message-item {
          &.message-left {
            .message-text {
              background-color: #232323 !important;
            }
          }

          &.message-right {
            .message-text {
              background-color: #182331 !important;
            }
          }
        }
      }
    }

    .loading-indicator {
      background: #2c2c2c;
      color: #aaa;
    }
  }
</style>
