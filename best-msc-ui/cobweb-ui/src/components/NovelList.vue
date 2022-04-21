<template>
  <div>
    <el-form :model="queryPar" ref="queryForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="小说名称" prop="name">
        <el-input v-model="queryPar.name"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doQuery('queryForm')">查询</el-button>
        <el-button @click="reset('queryForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="path" label="地址"/>
      <el-table-column prop="chapterStyle" label="章节条目样式"/>
      <el-table-column prop="chapterValueStyle" label="章节名称样式"/>
      <el-table-column prop="nextChapterStyle" label="下一页样式"/>
      <el-table-column prop="nextChapterValueStyle" label="下一页链接样式"/>
      <el-table-column prop="contentStyle" label="章节内容样式"/>
      <el-table-column prop="createTime" label="创建时间"/>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="editNovelDialogShow(scope.row)" type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <novel-edit v-if="dialogVisible" :novelId="novelId"
                @editDialogClosed="editDialogClosed"
                @refreshNovelTable="refreshNovelTable"/>
  </div>
</template>

<script>
import {novels} from '@/request/api'; // 导入自定义api接口
import NovelEdit from "./NovelEdit";

export default {
  name: "NovelList",
  components: {NovelEdit},
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      novelId: null,
      queryPar: {
        name: null
      }
    }
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      let self = this;
      novels().then(resp => {
        if (resp.data) {
          self.tableData = resp.data;
        }
      }, err => {
        console.log(err);
      });
    },
    editNovelDialogShow(row) {
      if (!row.id) {
        return;
      }
      this.dialogVisible = true;
      this.novelId = row.id;
    },
    editDialogClosed() {
      this.dialogVisible = false;
    },
    refreshNovelTable() {
      this.initData();
    },
    doQuery(formName) {
      let self = this;
      novels().then(resp => {
        if (resp.data) {
          self.tableData = resp.data;
        }
      }, err => {
        console.log(err);
      });
    },
    reset(formName) {
      this.$refs[formName].resetFields();
    },
  }
}
</script>

<style scoped>

</style>
