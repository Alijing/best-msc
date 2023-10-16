<script setup lang="ts">

import {usePermissionStore} from "@/store/modules/permission";
import {useRouter} from "vue-router";
import {Error} from "@/components/Error";
import {useStorage} from "@/hooks/web/useStorage";
import {useAppStore} from "@/store/modules/app";

const {setStorage} = useStorage();

const appStore = useAppStore();

const {push} = useRouter();
const permissionStore = usePermissionStore();

const errorClick = () => {
  push(permissionStore.addRouters[0]?.path as string)

  setStorage(appStore.getUserInfo, null)
}

</script>

<template>
  <Error @error-click="errorClick"/>
</template>
