<script lang="tsx">
import {computed, defineComponent} from "vue";
import {useDesign} from "@/hooks/web/useDesign";
import {useAppStore} from "@/store/modules/app";
import {Collapse} from "@/components/Collapse";
import {Breadcrumb} from "@/components/Breadcrumb";
import {Screenfull} from "@/components/Screenfull";
import {SizeDropdown} from "@/components/SizeDropdown";
import {LocaleDropdown} from "@/components/LocaleDropdown";
import {UserInfo} from "@/components/UserInfo";

const {getPrefixCls, variables} = useDesign();
const prefixCls = getPrefixCls('tool-header');

const appStore = useAppStore();

// 布局
const layout = computed(() => appStore.getLayout);

// 折叠图标
const hamburger = computed(() => appStore.getHamburger);

// 面包屑
const breadcrumb = computed(() => appStore.getBreadcrumb);

// 全屏图标
const screenfull = computed(() => appStore.getScreenfull)

// 尺寸图标
const size = computed(() => appStore.getSize)

// 多语言图标
const locale = computed(() => appStore.getLocale)

export default defineComponent({
  name: 'ToolHeader',
  components: {Breadcrumb, Collapse},
  setup() {
    return () => (
        <div id={`${variables.namespace}-tool-header`} class={[
          prefixCls,
          'h-[var(--top-tool-height)] relative px-[var(--top-tool-p-x)] flex items-center justify-between',
          'dark:bg-[var(--el-bg-color)]'
        ]}>
          {
            layout.value !== 'top' ? (
                <div class="h-full flex items-center">
                  {
                    hamburger.value && layout.value !== 'cutMenu' ? (
                        <Collapse class="custom-hover" color="var(--top-header-text-color)"/>
                    ) : undefined
                  }
                  {
                    breadcrumb.value ? <Breadcrumb class="<md:hidden"/> : undefined
                  }
                </div>
            ) : undefined
          }

          <div class="h-full flex items-center">
            {screenfull.value ? (
                <Screenfull class="custom-hover" color="var(--top-header-text-color)"></Screenfull>
            ) : undefined}
            {size.value ? (
                <SizeDropdown class="custom-hover" color="var(--top-header-text-color)"></SizeDropdown>
            ) : undefined}
            {locale.value ? (
                <LocaleDropdown
                    class="custom-hover"
                    color="var(--top-header-text-color)"
                ></LocaleDropdown>
            ) : undefined}
            <UserInfo/>
          </div>
        </div>
    )
  }
})

</script>

<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-tool-header';

.@{prefix-cls} {
  transition: left var(--transition-time-02);
}
</style>