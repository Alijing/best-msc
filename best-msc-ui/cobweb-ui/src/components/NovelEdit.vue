<template>
  <el-dialog title="小说信息" :visible.sync="dialogVisible" @closed="dialogClosed">
    <el-form :model="novelInfo" :rules="rules" ref="novelInfoForm" label-width="140px" class="demo-ruleForm">
      <el-form-item label="小说名称" prop="name">
        <el-input v-model="novelInfo.name"/>
      </el-form-item>
      <el-form-item label="小说地址" prop="path">
        <el-input v-model="novelInfo.path"/>
      </el-form-item>
      <el-form-item label="章节条目样式" prop="chapterStyle">
        <el-input v-model="novelInfo.chapterStyle"/>
        <span class="add-editor-formitem-tips" style="font-size: 12px;">
          <i class="el-icon-warning-outline"></i>&nbsp;&nbsp;//div[@class='bdsub']/dl/dd/table/tbody/tr/td[@class='L']
        </span>
      </el-form-item>
      <el-form-item label="章节名称样式" prop="chapterValueStyle">
        <el-input v-model="novelInfo.chapterValueStyle"/>
      </el-form-item>
      <el-form-item label="下一页样式" prop="nextChapterStyle">
        <el-input v-model="novelInfo.nextChapterStyle"/>
      </el-form-item>
      <el-form-item label="下一页链接样式" prop="nextChapterValueStyle">
        <el-input v-model="novelInfo.nextChapterValueStyle"/>
      </el-form-item>
      <el-form-item label="章节内容样式" prop="contentStyle">
        <el-input v-model="novelInfo.contentStyle"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('novelInfoForm')">保存</el-button>
        <el-button @click="resetForm('novelInfoForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>

import {novelUpdate, novelInfoById} from '@/request/api'; // 导入自定义api接口

export default {
  name: "NovelEdit",
  props: {
    novelId: {
      type: Number,
      required: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      novelInfo: {
        name: '',
        path: '',
        chapterStyle: '',
        chapterValueStyle: '',
        nextChapterStyle: '',
        nextChapterValueStyle: '',
        contentStyle: '',
      },
      rules: {
        name: [
          {required: true, message: '请输入小说名称', trigger: 'blur'},
          {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'}
        ],
        path: [
          {required: true, message: '请输入小说地址', trigger: 'blur'}
        ],
        chapterStyle: [
          {required: true, message: '请输入章节条目样式', trigger: 'blur'}
        ],
        chapterValueStyle: [
          {required: true, message: '请输入章节名称样式', trigger: 'blur'}
        ],
        nextChapterStyle: [
          {required: true, message: '请输入下一页样式', trigger: 'blur'}
        ],
        nextChapterValueStyle: [
          {required: true, message: '请输入下一页链接样式', trigger: 'blur'}
        ],
        contentStyle: [
          {required: true, message: '请输入章节内容样式', trigger: 'blur'}
        ],
      }
    };
  },
  mounted() {
    if (this.novelId) {
      this.loadNovelInfo();
    }
  },
  methods: {
    loadNovelInfo() {
      let self = this;
      novelInfoById(self.novelId).then(resp => {
        if (resp.data) {
          self.novelInfo = resp.data;
        }
      }, err => {
        self.$message.error('错了哦，' + err.msg);
      });
    },
    submitForm(formName) {
      let self = this;
      self.$refs[formName].validate((valid) => {
        if (valid) {
          let self = this;
          novelUpdate(self.novelInfo).then(resp => {
            self.$message({
              message: '恭喜你，这是一条成功消息',
              type: 'success'
            });
            if (resp.data) {
              self.dialogVisible = false;
              self.$emit('refreshNovelTable');
            }
          }, err => {
            self.$message.error('错了哦，' + err.msg);
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    dialogClosed() {
      this.novelInfo = null;
      this.$emit('editDialogClosed');
    }
  },
  created() {
    this.dialogVisible = true;
  },
  destroyed() {
    this.dialogVisible = false;
  },
}
</script>

<style scoped>

</style>
