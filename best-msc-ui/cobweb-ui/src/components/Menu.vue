<template>
  <el-menu
    default-active="2"
    class="el-menu-vertical-demo"
    @open="handleOpen"
    @close="handleClose"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b">
    <el-menu-item v-for="it in menus">
      <i :class="it.icon"></i>
      <span slot="title">{{ it.name }}</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "LeftMenu",
  data() {
    return {
      menus: [
        {path: '', name: '小说列表', id: 1, icon: 'el-icon-document'},
        {path: '', name: '小说列表', id: 2, icon: ''},
        {path: '', name: '小说列表', id: 3, icon: ''},
        {path: '', name: '小说列表', id: 4, icon: ''},
      ]
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

</style>
