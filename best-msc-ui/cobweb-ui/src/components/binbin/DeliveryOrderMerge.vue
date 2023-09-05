<template>
  <div>
    <el-row type="flex">
      <el-col :span="11">
        <el-card class="box-card" shadow="always">
          <span>配送单信息合并</span>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="配送单地址" prop="path">
              <template slot-scope="scope">
                <div v-for="(domain, index) in form.deliveryOrderPath" style="display: flex; margin-bottom: 8px">
                  <el-input v-model="domain.path" style="margin-right: 10px;"/>
                  <el-button type="primary" icon="el-icon-plus" @click="addDomain"/>
                  <el-button type="primary" icon="el-icon-delete" @click.prevent="removeDomain(index)"/>
                </div>
              </template>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="onSubmit">立即创建</el-button>
              <el-button>取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="7">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>卡片名称</span>
            <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
          </div>
          <div v-for="o in 4" :key="o" class="text item">
            {{ '列表内容 ' + o }}
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>卡片名称</span>
            <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
          </div>
          <div v-for="o in 4" :key="o" class="text item">
            {{ '列表内容 ' + o }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>卡片名称</span>
            <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
          </div>
          <div v-for="o in 4" :key="o" class="text item">
            {{ '列表内容 ' + o }}
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {deliveryOrderMerge} from '@/request/api'; // 导入自定义api接口

export default {
  name: "DeliveryOrderMerge",
  data() {
    let validatePath = (rule, value, callback) => {
      let isNull = this.form.deliveryOrderPath.length === 0;
      if (isNull) {
        return callback(new Error('请输入配送单文件的地址'));
      }

      for (let i = 0; i < this.form.deliveryOrderPath.length; i++) {
        let item = this.form.deliveryOrderPath[i];
        if (item.path.length === 0) {
          return callback(new Error('第' + (i + 1) + '个配送单文件的地址不正确'));
        }
      }
      callback();
    };
    return {
      form: {
        deliveryOrderPath: [{
          path: 'C:\\Users\\lenovo\\Desktop\\8.11\\会展\\XiaoCaiData\\tmp\\2023-08-18'
        }]
      },
      rules: {
        path: [
          {required: true, validator: validatePath, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    onSubmit() {
      let self = this;
      self.$refs.form.validate((valid) => {
        if (valid) {
          let orderPath = [];
          self.form.deliveryOrderPath.forEach(it => {
            orderPath.push(it.path);
          });

          deliveryOrderMerge({deliveryOrderPath: orderPath}, (resp, err) => {
            console.log('-- >>>> resp : ' + resp + ', err : ' + err);
          }, 2 * 1000 * 1000);

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    removeDomain(index) {
      if (this.form.deliveryOrderPath.length === 1) {
        this.$message({message: '最后一条啦，不能再删咯', type: 'warning'});
        return;
      }
      if (index !== -1) {
        this.form.deliveryOrderPath.splice(index, 1)
      }
    },
    addDomain() {
      this.$nextTick(() => {
        this.form.deliveryOrderPath.push({path: ''});
      })
    }
  }
}
</script>

<style scoped>
.el-form {
  background-color: white;
  padding: 30px;
}

.el-row {
  margin-bottom: 20px;
}

.el-row :last-child {
  margin-bottom: 0;
}

.el-col {
  margin: 20px;
}

</style>
