import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import NovelList from '@/components/NovelList'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'NovelList',
      component: NovelList
    }
  ]
})
