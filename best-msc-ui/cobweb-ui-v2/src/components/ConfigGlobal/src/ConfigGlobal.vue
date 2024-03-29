<script setup lang="ts">
import {provide, computed, watch, onMounted} from 'vue'
import {propTypes} from '@/utils/propTypes'
import {ComponentSize, ElConfigProvider} from 'element-plus'
import {useAppStore} from "@/store/modules/app";
import {useWindowSize} from '@vueuse/core'
import {setCssVar} from "@/utils";
import {useDesign} from "@/hooks/web/useDesign";
import {useLocaleStore} from "@/store/modules/locale";

const {variables} = useDesign();

const appStore = useAppStore()

const props = defineProps({
  size: propTypes.oneOf<ComponentSize>(['default', 'small', 'large']).def('default')
})

provide('configGlobal', props)

// 初始化所有主题颜色
onMounted(() => {
  appStore.setCssVarTheme()
})

const {width} = useWindowSize();

watch(
    () => width.value,
    (width) => {
      if (width < 768) {
        !appStore.getMobile ? appStore.setMobile(true) : undefined
        setCssVar('--left-menu-min-width', '0')
        appStore.setCollapse(true)
        appStore.getLayout !== 'classic' ? appStore.setLayout('classic') : undefined
      } else {
        appStore.getMobile ? appStore.setMobile(false) : undefined
        setCssVar('--left-menu-min-width', '64px')
      }
    },
    {
      immediate: true
    }
)

// 多语言相关
const localeStore = useLocaleStore();

const currentLocale = computed(() => localeStore.currentLocale);

</script>
<template>
  <ElConfigProvider
      :namespace="variables.elNamespace"
      :locale="currentLocale.elLocale"
      :message="{ max : 1 }"
      :size="size">
    <slot></slot>
  </ElConfigProvider>
</template>