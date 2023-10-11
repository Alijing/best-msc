<script lang="tsx">

import {useDesign} from "@/hooks/web/useDesign";
import {useAppStore} from "@/store/modules/app";
import {computed, unref} from "vue";

const {getPrefixCls} = useDesign();
const prefixCls = getPrefixCls('layout');

const appStore = useAppStore();

// 是否是移动端
const mobile = computed(() => appStore.getMobile);

// 菜单折叠
const collapse = computed(() => appStore.getCollapse);

const layout = computed(() => appStore.getLayout);

const handleClickOutside = () => {
  appStore.setCollapse(true)
}

const renderLayout = () => {
  switch (unref(layout)) {
    case 'classic':
      const {renderClassic} = useRenderLayout()
  }
}

</script>

<style scoped>

</style>