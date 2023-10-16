<script lang="tsx">

import {useDesign} from "@/hooks/web/useDesign";
import {useAppStore} from "@/store/modules/app";
import {computed, unref, defineComponent} from "vue";
import {useRenderLayout} from "@/layout/components/useRenderLayout";
import {Backtop} from "@/components/Backtop";
import {Setting} from "@/components/Setting";

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
      return renderClassic()
    default:
      break
  }
}

export default defineComponent({
  name: 'Layout',
  components: {Setting, Backtop},
  setup() {
    return () => (
        <section class={[prefixCls, `${prefixCls}__${layout.value}`, 'w-[100%] h-[100%] relative']}>
          {
            mobile.value && !collapse.value ?
                (
                    <div class="absolute top-0 left-0 w-full h-full opacity-30 z-99 bg-[var(--el-color-black)]"
                         onClick={handleClickOutside}>
                    </div>
                )
                : undefined
          }

          {renderLayout()}

          <Backtop/>
          <Setting/>
        </section>
    )
  }
})

</script>

<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-layout';

.@{prefix-cls} {
  background-color: var(--app-content-bg-color);

  :deep(.@{elNamespace}-scrollbar__view) {
    height: 100% !important;
  }
}
</style>