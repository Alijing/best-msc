<template>
  <el-dialog title="小说信息" :visible.sync="dialogVisible" @closed="dialogClosed">
    <el-form :model="novelConfig" :rules="rules" ref="novelConfigForm" label-width="140px" class="demo-ruleForm">
      <el-row>
        <el-col :span="12">
          <el-form-item label="小说名称" prop="name">
            <el-input v-model="novelConfig.name"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="小说地址" prop="path">
            <el-input v-model="novelConfig.path"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">章节</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="目录样式" prop="chapterStyle">
            <el-input v-model="novelConfig.chapterStyle"/>
            <span class="add-editor-formitem-tips" style="font-size: 12px;">
          <i class="el-icon-warning-outline"></i>&nbsp;&nbsp;//div[@id=list-chapterAll]/dd
        </span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目录文本样式" prop="chapterValueStyle">
            <el-input v-model="novelConfig.chapterValueStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="下一页样式" prop="nextChapterStyle">
            <el-input v-model="novelConfig.nextChapterStyle"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="下一页链接样式" prop="nextChapterValueStyle">
            <el-input v-model="novelConfig.nextChapterValueStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">章节内容</el-divider>

      <el-row>
        <el-col :span="12">
          <el-form-item label="获取方式" prop="type">
            <el-select v-model="novelConfig.type" clearable placeholder="请选择" @change="novelTypeChange">
              <el-option
                v-for="item in novelTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="defaultType">
          <el-form-item label="内容样式" prop="contentStyle">
            <el-input v-model="novelConfig.contentStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="defaultType">
        <el-col :span="12">
          <el-form-item label="下一页样式" prop="nextContentStyle">
            <el-input v-model="novelConfig.nextContentStyle"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="下一页链接样式" prop="nextContentValueStyle">
            <el-input v-model="novelConfig.nextContentValueStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="!defaultType">
        <el-col :span="8">
          <el-form-item label="接口地址" prop="contentStyle">
            <el-input v-model="novelConfig.ifPath"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="请求方式" prop="nextContentStyle">
            <el-select v-model="novelConfig.method" clearable placeholder="请选择">
              <el-option
                v-for="item in methods"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="参数获取" prop="nextContentStyle">
            <el-select v-model="novelConfig.param" clearable placeholder="请选择">
              <el-option
                v-for="item in params"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item>
        <el-button type="primary" @click="submitForm('novelConfigForm')">保存</el-button>
        <el-button @click="resetForm('novelConfigForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>

import {crawlConfigUpdate, crawlConfigByNovelId} from '@/request/api'; // 导入自定义api接口

export default {
  name: "NovelEdit",
  props: {
    novel: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      defaultType: true,
      novelTypes: [
        {value: 0, label: '网页'},
        {value: 1, label: 'API接口'}
      ],
      methods: [
        {value: 0, label: 'GET'},
        {value: 1, label: 'POST'}
      ],
      params: [
        {value: 0, label: '地址栏获取'},
      ],
      novelConfig: {
        name: '',
        path: '',
        chapterStyle: '',
        chapterValueStyle: '',
        nextChapterStyle: '',
        nextChapterValueStyle: '',

        type: null,
        contentStyle: '',
        nextContentStyle: '',
        nextContentValueStyle: '',

        ifPath: null,
        method: null,
        param: null
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
        // nextChapterStyle: [
        //   {required: true, message: '请输入下一页样式', trigger: 'blur'}
        // ],
        // nextChapterValueStyle: [
        //   {required: true, message: '请输入下一页链接样式', trigger: 'blur'}
        // ],
        type: [
          {required: true, message: '请输入章节内容获取方式', trigger: 'blur'}
        ],
        contentStyle: [
          {required: true, message: '请输入章节内容样式', trigger: 'blur'}
        ],
      }
    };
  },
  mounted() {
    if (this.novel) {
      if (this.novel) {
        this.novelConfig = Object.assign({}, this.novel);
        this.novelConfig.novelId = this.novel.id;
      }
      this.loadNovelConfig();
    }
  },
  methods: {
    loadNovelConfig() {
      let self = this;
      crawlConfigByNovelId(self.novel.id).then(resp => {
        if (resp.data) {
          self.novelConfig = Object.assign({}, self.novel, resp.data);
        }
      }, err => {
        self.$message.error('错了哦，' + err.msg);
      });
    },
    submitForm(formName) {
      let self = this;
      self.$refs[formName].validate((valid) => {
        if (valid) {
          crawlConfigUpdate(self.novelConfig).then(resp => {
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
    novelTypeChange(it) {
      if (0 === it) {
        this.$nextTick(() => {
          this.defaultType = true;
        })
      } else if (1 === it) {
        this.$nextTick(() => {
          this.defaultType = false;
        })
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    dialogClosed() {
      this.novelConfig = null;
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
