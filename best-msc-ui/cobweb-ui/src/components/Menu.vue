<template>
  <el-container>
    <el-menu default-active="/"
             class="el-menu-vertical-demo"
             @open="handleOpen"
             @close="handleClose"
             :collapse="isCollapse"
             background-color="#304156"
             text-color="#BFCBD9"
             active-text-color="#ffd04b" unique-opened router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.index">
            <template #title>
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                <template #title>{{ subItem.title }}</template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.title }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="item.icon"></i>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-container>
</template>

<script>
export default {
  name: "LeftMenu",
  props: {
    isCollapse: {
      type: Boolean,
      required: true
    },
  },
  data() {
    return {
      items: [
        {
          icon: "el-icon-s-home",
          index: "/",
          title: "系统首页",
        },
        {
          icon: "el-icon-dessert",
          index: "1",
          title: "斌斌",
          subs: [
            {
              index: "/deliveryOrderMerge",
              title: "配送单合并",
            },
          ],
        },
        {
          icon: "el-icon-document",
          index: "9",
          title: "小说模块",
          subs: [
            {
              index: "/novel",
              title: "小说列表",
            },
          ],
        },
        {
          icon: "el-icon-lx-cascades",
          index: "/table",
          title: "基础表格",
        },
        {
          icon: "el-icon-lx-copy",
          index: "/tabs",
          title: "tab选项卡",
        },
        {
          icon: "el-icon-lx-calendar",
          index: "3",
          title: "表单相关",
          subs: [
            {
              index: "/form",
              title: "基本表单",
            },
            {
              index: "/upload",
              title: "文件上传",
            },
            {
              index: "4",
              title: "三级菜单",
              subs: [
                {
                  index: "/editor",
                  title: "富文本编辑器",
                },
              ],
            },
          ],
        },
        {
          icon: "el-icon-lx-emoji",
          index: "/icon",
          title: "自定义图标",
        },
        {
          icon: "el-icon-pie-chart",
          index: "/charts",
          title: "schart图表",
        },
        {
          icon: "el-icon-lx-global",
          index: "/i18n",
          title: "国际化功能",
        },
        {
          icon: "el-icon-lx-warn",
          index: "7",
          title: "错误处理",
          subs: [
            {
              index: "/permission",
              title: "权限测试",
            },
            {
              index: "/404",
              title: "404页面",
            },
          ],
        },
        {
          icon: "el-icon-lx-redpacket_fill",
          index: "/donate",
          title: "支持作者",
        },
      ],
    }
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style scoped>
.el-container {
  height: 100%;
  margin: 0;
  padding: 0;
}

.el-menu {
  border-right: solid 0 #e6e6e6;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
