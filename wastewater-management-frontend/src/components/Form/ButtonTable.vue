<template>
  <div :class="['btn-text', `btn-${props.type}`, useColor ? buttonColor : '']" @click="handleClick">
    <i v-if="props.type" class="iconfont-sys" v-html="getIcon(props.type)"></i>
    <span v-if="props.text">{{ props.text }}</span>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'

  type ButtonType = 'add' | 'edit' | 'delete' | 'more' | 'reset'

  const props = withDefaults(
    defineProps<{
      text?: string
      icon?: string
      type?: ButtonType
      buttonClass?: string
      useColor?: boolean
    }>(),
    {
      useColor: true
    }
  )

  const emit = defineEmits<{
    (e: 'click'): void
  }>()

  const defaultButtons = [
    { type: 'add', icon: '&#xe602;', color: 'bg-primary' },
    { type: 'edit', icon: '&#xe642;', color: 'bg-secondary' },
    { type: 'delete', icon: '&#xe783;', color: 'bg-error' },
    { type: 'more', icon: '&#xe6df;', color: '' },
    { type: 'reset', icon: '&#xe817;', color: 'bg-warning-light' } // 重置按钮（淡黄）
  ] as const

  const getIcon = (type: ButtonType) => {
    return defaultButtons.find((btn) => btn.type === type)?.icon
  }

  const getButtonColor = (type: ButtonType) => {
    return defaultButtons.find((btn) => btn.type === type)?.color
  }

  const buttonColor = computed(() => {
    if (!props.useColor) return ''
    return props.buttonClass || (props.type ? getButtonColor(props.type) : '')
  })

  const handleClick = () => {
    emit('click')
  }
</script>

<style scoped lang="scss">
  .btn-text {
    display: inline-block;
    min-width: 34px;
    height: 34px;
    padding: 0 10px;
    margin-right: 10px;
    font-size: 13px;
    line-height: 34px;
    color: #666;
    cursor: pointer;
    background-color: rgba(var(--art-gray-200-rgb), 0.7);
    border-radius: 6px;

    &:hover {
      color: var(--main-color);
      background-color: rgba(var(--art-gray-300-rgb), 0.5);
    }
  }

  // 自定义 reset 类型按钮颜色
  .btn-reset {
    background-color: #fff6d5; // 淡黄色
    color: #8a6d3b;

    &:hover {
      background-color: #ffeeba;
      color: #7c5f27;
    }
  }
</style>
