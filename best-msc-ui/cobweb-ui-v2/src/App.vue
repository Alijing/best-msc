<script setup>
import { RouterView} from 'vue-router'
import {ConfigGlobal} from "@/components/ConfigGlobal";
import {useDesign} from "@/hooks/web/useDesign";
import {useAppStore} from "@/store/modules/app";
import {computed} from "vue";
import {useStorage} from "@/hooks/web/useStorage";
import {isDark} from '@/utils/is'

const {getPrefixCls} = useDesign();
const prefixCls = getPrefixCls('app');

const appStore = useAppStore();
const currentSize = computed(() => appStore.getCurrentSize);
const greyMode = computed(() => appStore.getGreyMode);

const {getStorage} = useStorage();
const setDefaultTheme = () => {

  if (getStorage('isDark') !== null) {
    appStore.setIsDark(getStorage('isDark'));
    return
  }
  const dark = isDark();
  appStore.setIsDark(dark);
}

setDefaultTheme()
</script>

<template>
  <ConfigGlobal :size="currentSize">
    <RouterView :class="greyMode ? `${prefixCls}-grey-mode` : ''"/>
  </ConfigGlobal>
</template>

<style lang="less">
@prefix-cls: ~'@{namespace}-app';

.size {
  width: 100%;
  height: 100%;
}

html, body {
  padding: 0 !important;
  margin: 0;
  overflow: hidden;
  .size;

  #app {
    .size;
  }
}

.@{prefix-cls}-grey-mode {
  filter: grayscale(100%);
}

</style>
