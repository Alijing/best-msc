<template>
  <div ref="novelTable">
    <el-form :model="queryPar" ref="queryForm" label-width="100px" :inline="true" class="demo-form-inline">
      <el-form-item label="小说名称" prop="name">
        <el-input v-model="queryPar.name" @blur="initData()"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doQuery('queryForm')">查询</el-button>
        <el-button type="primary" @click="editNovelDialogShow()">新增</el-button>
        <el-button @click="reset('queryForm')">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" style="width: 100%" height="720" v-loading="loading" stripe>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="path" label="地址"/>
      <!--      <el-table-column prop="chapterStyle" label="章节条目样式"/>-->
      <!--      <el-table-column prop="chapterValueStyle" label="章节名称样式"/>-->
      <!--      <el-table-column prop="nextChapterStyle" label="下一页样式"/>-->
      <!--      <el-table-column prop="nextChapterValueStyle" label="下一页链接样式"/>-->
      <!--      <el-table-column prop="contentStyle" label="章节内容样式"/>-->
      <el-table-column prop="createTime" label="创建时间"/>
      <el-table-column fixed="right" label="操作" width="350">
        <template slot-scope="scope">
          <el-button @click="editNovelDialogShow(scope.row)" type="text">编辑</el-button>
          <el-button @click="copyNovel(scope.row)" type="text">复制</el-button>
          <el-button @click="crawlChapter(scope.row)" type="text">获取章节</el-button>
          <el-button @click="changeChapterName(scope.row)" type="text">修改章节名称</el-button>
          <el-button @click="crawlChapterContent(scope.row)" type="text">获取内容</el-button>
          <el-button @click="download(scope.row)" type="text">下载</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="total, prev, pager, next, jumper"
      :total="queryPar.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="queryPar.currentPage"
      :page-size="queryPar.pageSize">
    </el-pagination>

    <novel-edit v-if="dialogVisible" :novelId="novelId"
                @editDialogClosed="editDialogClosed"
                @refreshNovelTable="refreshNovelTable"/>
  </div>
</template>

<script>
import {novels, copyNovel, crawlingChapter, changeChapterName, crawlingChapterContent} from '@/request/api'; // 导入自定义api接口
import NovelEdit from "./NovelEdit";

export default {
  name: "NovelTable",
  components: {NovelEdit},
  data() {
    return {
      loading: true,
      tableData: [],
      dialogVisible: false,
      novelId: null,
      queryPar: {
        name: null,
        total: 500,
        pageSize: 30,
        currentPage: 1,
      },
    }
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      let self = this;
      novels(self.queryPar).then(resp => {
        self.loading = false;
        if (resp.data) {
          self.tableData = resp.data;
        }
      }, err => {
        self.loading = false;
        self.$message.error('错了哦，' + err.message);
      });
    },
    editNovelDialogShow(row) {
      this.dialogVisible = true;
      if (row) {
        this.novelId = row.id;
      }
    },
    copyNovel(novel) {
      let self = this;
      copyNovel(novel.id).then(resp => {
        if (resp.data) {
          self.$message.info('复制成功！');
          self.refreshNovelTable();
        }
      }, err => {
        self.$message.error('错了哦，' + err.msg);
      });
    },
    editDialogClosed() {
      this.dialogVisible = false;
      this.novelId = null;
    },
    refreshNovelTable() {
      this.initData();
    },
    doQuery(formName) {
      this.initData();
    },
    reset(formName) {
      this.$refs[formName].resetFields();
      this.initData();
    },
    crawlChapter(novel) {
      let self = this;
      crawlingChapter(novel.id).then(resp => {
        self.$message({
          message: '恭喜你，后台已开始爬取章节',
          type: 'success'
        });
        self.initData();
      }, err => {
        self.loading = false;
        self.$message.error('错了哦，' + err.message);
      });
    },
    changeChapterName(novel) {
      let self = this;
      changeChapterName(novel.id).then(resp => {
        self.$message({
          message: '恭喜你，章节名称已经修改成功',
          type: 'success'
        });
      }, err => {
        self.loading = false;
        self.$message.error('错了哦，' + err.message);
      });
    },
    crawlChapterContent(novel) {
      let self = this;
      crawlingChapterContent(novel.id).then(resp => {
        self.$message({
          message: '恭喜你，后台已开始爬取章节内容',
          type: 'success'
        });
        self.initData();
      }, err => {
        self.loading = false;
        self.$message.error('错了哦，' + err.message);
      });
    },
    download(novel) {
      let url = this.axios.defaults.baseURL + '/novel/download/' + novel.id;
      window.open(url, '_black');
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
  }
}
</script>

<style scoped>
.el-pagination {
  margin-top: 15px;
}
</style>
