// 引入 windi css
import '@/plugins/unocss'

// 导入全局的svg图标
import '@/plugins/svgIcon'

// 初始化多语言
import {setupI18n} from "@/plugins/vueI18n";

// 引入状态管理
import {setupStore} from "@/store";

// 全局组件
import {setupGlobCom} from "@/components";

// 引入 element-plus
import {setupElementPlus} from '@/plugins/elementPlus'

// 引入全局样式
import '@/styles/index.less'

// 引入动画
import '@/plugins/animate.css'

// 路由
import {setupRouter} from './router'

import {createApp} from 'vue'

import App from './App.vue'

import './permission'

// 创建实例
const setupAll = async () => {
    let app = createApp(App);

    await setupI18n(app)

    setupStore(app)

    setupGlobCom(app)

    setupElementPlus(app)

    setupRouter(app)

    // app.config.globalProperties.$echarts = echarts; //vue3的挂载方式 https://blog.csdn.net/weixin_42728767/article/details/130401563
    // app.provide('$echarts', echarts); // vue3采用provide, inject方式使用

    app.mount('#app')
}

setupAll()