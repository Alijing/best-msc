<script setup lang="tsx">
import {reactive, ref, watch} from 'vue'
import {ElCheckbox, ElLink, ElButton} from 'element-plus'
import {Form, FormSchema} from '@/components/Form'
import {useI18n} from '@/hooks/web/useI18n'
import {loginApi, getTestRoleApi, getAdminRoleApi} from '@/api/login'
import {useForm} from '@/hooks/web/useForm'
import {useValidator} from '@/hooks/web/useValidator'
import {usePermissionStore} from '@/store/modules/permission'
import {useRouter} from 'vue-router'
import type {RouteLocationNormalizedLoaded, RouteRecordRaw} from 'vue-router'
import {UserType} from '@/api/login/types'
import {useStorage} from '@/hooks/web/useStorage'
import {useAppStore} from '@/store/modules/app'

const {required} = useValidator();

const emits = defineEmits(['to-register'])

const appStore = useAppStore();

const {t} = useI18n();

const {formRegister, formMethods} = useForm();
const {getElFormExpose, getFormData} = formMethods

const permissionStore = usePermissionStore();

const {push, addRoute, currentRoute} = useRouter()

const {setStorage} = useStorage();

const rules = {
  username: [required()],
  password: [required()],
}
const remember = ref(false);

const loading = ref(false);

const redirect = ref<string>('');

const schema = reactive<FormSchema[]>([
  {
    field: 'title',
    colProps: {span: 24},
    formItemProps: {
      slots: {
        default: () => {
          return <h2 class="text-2xl font-bold text-center w-[100%]">{t('login.login')}</h2>
        }
      }
    }
  },
  {
    field: 'username',
    label: t('login.username'),
    value: 'admin',
    component: 'Input',
    colProps: {span: 24},
    componentProps: {
      placeholder: t('login.usernamePlaceholder'),
    }
  },
  {
    field: 'password',
    label: t('login.password'),
    value: 'admin',
    component: 'InputPassword',
    colProps: {span: 24},
    componentProps: {
      style: {width: '100%'},
      placeholder: t('login.passwordPlaceholder')
    }
  },
  {
    field: 'tool',
    colProps: {span: 24},
    formItemProps: {
      slots: {
        default: () => {
          return (
              <>
                <div class="flex justify-between items-center w-[100%]">
                  <ElCheckbox v-model={remember.value} label={t('login.remember')} size="small"/>
                  <ElLink type="primary" underline={false}>
                    {t('login.forgetPassword')}
                  </ElLink>
                </div>
              </>
          )
        }
      }
    }
  },
  {
    field: 'login',
    colProps: {span: 24},
    formItemProps: {
      slots: {
        default: () => {
          return (
              <>
                <div class="w-[100%]">
                  <ElButton loading={loading.value} type="primary" class="w-[100%]" onClick={signIn}>
                    {t('router.login')}
                  </ElButton>
                </div>
              </>
          )
        }
      }
    }
  }
])

watch(() => currentRoute.value, (route: RouteLocationNormalizedLoaded) => {
  redirect.value = route?.query?.redirect as string
}, {immediate: true})

// 登录
const signIn = async () => {
  await permissionStore.generateRoutes('none').catch(() => {
  })
  permissionStore.getAddRouters.forEach((route) => {
    addRoute(route as RouteRecordRaw)
  })
  permissionStore.setIsAddRouters(true)
  push({path: redirect.value || permissionStore.addRouters[0].path})

  const formRef = await getElFormExpose();
  await formRef?.validate(async (isValid) => {
    if (isValid) {
      loading.value = true
      const formData = await getFormData<UserType>()
      try {
        formData.account = formData.username
        const resp = await loginApi(formData)
        if (resp) {
          setStorage(appStore.getUserInfo, resp)
        }
        // 是否使用动态路由
        if (appStore.getDynamicRouter) {
          // TODO
          getRole()
        } else {
          await permissionStore.generateRoutes('none').catch(() => {
          })
          permissionStore.getAddRouters.forEach(route => {
            // 动态添加可访问的路由表
            addRoute(route as RouteRecordRaw)
          })
          permissionStore.setIsAddRouters(true)
          push({path: redirect.value || permissionStore.addRouters[0].path})
        }
      } finally {
        loading.value = false
      }
    }
  })
}

// 获取角色信息
const getRole = async () => {
  const formData = await getFormData<UserType>()
  const params = {
    roleName: formData.username
  }
  // admin - 模拟后端过滤菜单
  // test - 模拟前端过滤菜单
  // const res = formData.username === 'admin' ? await getAdminRoleApi(params) : await getTestRoleApi(params)
  // if (res) {
  //   const routers = res.data || []
  //   setStorage('roleRouters', routers)
  //
  //   formData.username === 'admin'
  //       ? await permissionStore.generateRoutes('admin', routers).catch(() => {
  //       })
  //       : await permissionStore.generateRoutes('test', routers).catch(() => {
  //       })
  //
  //   permissionStore.getAddRouters.forEach((route) => {
  //     addRoute(route as RouteRecordRaw) // 动态添加可访问路由表
  //   })
  //   permissionStore.setIsAddRouters(true)
  //   push({path: redirect.value || permissionStore.addRouters[0].path})
  // }
}

// 去注册页面
const toRegister = () => {
  emits('to-register')
}
</script>

<template>
  <Form :schema="schema"
        :rules="rules"
        label-position="top"
        hide-required-asterisk
        size="large"
        class="dark:(border-1 border-[var(--el-border-color)] border-solid )"
        @register="formRegister"/>
</template>
