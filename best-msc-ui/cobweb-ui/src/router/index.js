import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import NovelTable from '@/components/NovelTable'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'NovelTable',
      component: NovelTable
    }
  ]
})
