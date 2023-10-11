<script setup lang="ts">
import {LoginForm} from './components'

import {underlineToHump} from "@/utils";
import {useDesign} from "@/hooks/web/useDesign";
import {useAppStore} from "@/store/modules/app";
import {ref} from "vue";

const {getPrefixCls} = useDesign();
const prefixCls = getPrefixCls('login');

const appStore = useAppStore();

const isLogin = ref(true);

const toRegister = () => {
  isLogin.value = false
}

const toLogin = () => {
  isLogin.value = true
}

</script>

<template>
  <div :class="prefixCls"
       class="h-[100%] relative lt-xl:bg-[var(--login-bg-color)] lt-sm:px-10px lt-xl:px-10px lt-md:px-10px">
    <ElScrollbar class="h-full">
      <div class="relative flex mx-auto h-100vh">
        <div :class="`${prefixCls}__left flex-1 bg-gray-500 bg-opacity-20 relative p-30px lt-xl:hidden`">
          <div class="flex items-center relative text-white">
            <img src="@/assets/svg/logo.svg" alt="" class="w-48px h-48px mr-10px"/>
            <span class="text-20px font-bold">{{ underlineToHump(appStore.getTitle) }}</span>
          </div>
          <div class="flex justify-center items-center h-[calc(100%-60px)]">
            <TransitionGroup appear tag="div" enter-active-class="animate__animated animate__bounceInLeft">
              <img src="@/assets/svg/login-box-bg.svg" alt="" key="1" class="w-350px"/>
              <div class="text-3xl text-white">欢迎使用本系统</div>
            </TransitionGroup>
          </div>
        </div>
        <div class="flex-1 p-30px lt-sm:p-10px dark:bg-[var(--login-bg-color)] relative">
          <Transition appear enter-active-class="animate__animated animate__bounceInRight">
            <div
                class="h-full flex items-center m-auto w-[100%] at-2xl:max-w-500px at-xl:max-w-500px at-md:max-w-500px at-lg:max-w-500px">
              <LoginForm v-if="isLogin"
                         class="p-20px h-auto m-auto lt-xl:rounded-3xl lt-xl:light:bg-white"
                         @to-register="toRegister"/>
            </div>
          </Transition>
        </div>
      </div>
    </ElScrollbar>
  </div>
</template>
