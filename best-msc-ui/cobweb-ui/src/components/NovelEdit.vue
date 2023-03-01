<template>
  <el-dialog title="小说信息" :visible.sync="dialogVisible" @closed="dialogClosed">
    <el-form :model="novelInfo" :rules="rules" ref="novelInfoForm" label-width="140px" class="demo-ruleForm">
      <el-row>
        <el-col :span="12">
          <el-form-item label="小说名称" prop="name">
            <el-input v-model="novelInfo.name"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="小说地址" prop="path">
            <el-input v-model="novelInfo.path"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">章节</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="目录样式" prop="chapterStyle">
            <el-input v-model="novelInfo.chapterStyle"/>
            <span class="add-editor-formitem-tips" style="font-size: 12px;">
          <i class="el-icon-warning-outline"></i>&nbsp;&nbsp;//div[@id=list-chapterAll]/dd
        </span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目录文本样式" prop="chapterValueStyle">
            <el-input v-model="novelInfo.chapterValueStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="下一页样式" prop="nextChapterStyle">
            <el-input v-model="novelInfo.nextChapterStyle"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="下一页链接样式" prop="nextChapterValueStyle">
            <el-input v-model="novelInfo.nextChapterValueStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">章节内容</el-divider>

      <el-row>
        <el-col :span="12">
          <el-form-item label="获取方式" prop="type">
            <el-select v-model="novelInfo.type" clearable placeholder="请选择" @change="novelTypeChange">
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
            <el-input v-model="novelInfo.contentStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="defaultType">
        <el-col :span="12">
          <el-form-item label="下一页样式" prop="nextContentStyle">
            <el-input v-model="novelInfo.nextContentStyle"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="下一页链接样式" prop="nextContentValueStyle">
            <el-input v-model="novelInfo.nextContentValueStyle"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="!defaultType">
        <el-col :span="8">
          <el-form-item label="接口地址" prop="contentStyle">
            <el-input v-model="novelInfo.ifPath"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="请求方式" prop="nextContentStyle">
            <el-select v-model="novelInfo.method" clearable placeholder="请选择">
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
            <el-select v-model="novelInfo.param" clearable placeholder="请选择">
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
      novelInfo: {
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
