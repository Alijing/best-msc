<template>
  <el-container id="app">
    <el-aside width="auto">
      <LeftMenu :isCollapse="isCollapse"/>
    </el-aside>

    <el-container>
      <el-header>
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="isCollapse = !isCollapse">
          <i v-if="!isCollapse" class="el-icon-back"></i>
          <i v-else class="el-icon-right"></i>
        </div>
      </el-header>
      <el-main ref="mainContainer">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import LeftMenu from "./components/Menu";

export default {
  name: 'App',
  components: {LeftMenu},
  data() {
    return {
      isCollapse: false,
    }
  },
}
</script>

<style>
.el-header, .el-footer {
  background-color: #1890FF;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #304156;
  color: rgb(191, 203, 217);
  height: 100%;
  /* 隐藏滚动条 */
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
}

.el-aside::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
}

body > .el-container {
  height: 100%;
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.collapse-btn {
  float: left;
  color: #fff;
  font-size: 24px;
  cursor: pointer;
  line-height: 60px;
  width: 25px;
}
</style>
