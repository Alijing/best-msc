import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import NovelTable from '@/components/NovelTable'
import DeliveryOrderMerge from '@/components/binbin/DeliveryOrderMerge'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/novel',
      name: 'NovelTable',
      component: NovelTable
    },
    {
      path: '/deliveryOrderMerge',
      name: 'DeliveryOrderMerge',
      component: DeliveryOrderMerge
    }
  ]
})
