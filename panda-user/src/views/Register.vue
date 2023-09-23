<template>
  <div class="register-container">
    <div class="header" @click="toWelcome">
      <img src="http://120.77.42.122:9001/images/img-RycQKv9KlrHjg7nu9YJq4A5e.png"
        style="height: 90px; width: 90px; left: ;">
      <span style="color: aqua;">欢迎加入！非常高兴能够为您提供优质的服务和体验!</span>
    </div>
    <div class="register-body">

      <el-form class="register-form" ref="registerFormRef" :model="registerForm" :rules="registerFormRules"
        label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="registerForm.userName" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请输入确认密码" show-password>
          </el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phoneNumber">
          <el-input v-model="registerForm.phoneNumber" type="text" aria-placeholder="请输入手机号" clearable></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="gender" label="1">男</el-radio>
          <el-radio v-model="gender" label="0">女</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register">同意以下协议并注册</el-button>
        </el-form-item>
        <el-form-item id="protocal">
          <!--          这里跳转到电影协议-->
          <el-checkbox v-model="checked"></el-checkbox>
          <el-link type="primary" style="line-height: 14px;" href="https://www.1905.com/about/service/">《电影网服务协议》
          </el-link>
          <!-- <el-link type="primary" style="line-height: 14px;" >《XX协议》</el-link> -->
        </el-form-item>
      </el-form>
      <img src="http://1.12.42.181:9001/images/33.png">
      <!-- <div class="el-icon-user"></div> -->
    </div>
    <div class="footer-mini"></div>
    <div class="footer">
      <!-- <img style="width: 500px; height: 200px" src="http://120.77.42.122:9001/images/img-lu0lKdRBGS3Nah0vqdMaUkLt.png"> -->
    </div>
  </div>
</template>

<script>
  export default {
    name: "Register",
    data() {
      let validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'))
          // password 是表单上绑定的字段
        } else if (value !== this.registerForm.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
      let checkMobile = (rule, value, cb) => {
        const regMobile = /^(0|86|17951)?(13[0-9]|15[0123456789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
        if (regMobile.test(value)) {
          return cb()
        }
        cb(new Error('请输入合法的手机号码'))
      }
      return {
        gender: '1',
        checked: false,
        registerForm: {
          userName: '',
          password: '',
          confirmPassword: '',
          phoneNumber: '',
          sex: ''
        },
        registerFormRules: {
          userName: [{
              required: true,
              message: "请输入用户名称",
              trigger: "blur"
            },
            {
              min: 2,
              max: 20,
              message: "用户名称长度在2到20个字符之间",
              trigger: "blur"
            }
          ],
          password: [{
              required: true,
              message: "请输入密码",
              trigger: "blur"
            },
            {
              min: 6,
              max: 16,
              message: "长度在6到16个字符之间",
              trigger: "blur"
            }
          ],
          confirmPassword: [{
            required: true,
            validator: validatePass,
            message: "两次密码输入不一致",
            trigger: "blur"
          }, ],
          phoneNumber: [{
              required: true,
              message: '请输入手机号码',
              trigger: 'blur'
            },
            {
              validator: checkMobile,
              trigger: 'blur'
            }
          ]
        }
      }
    },
    methods: {
      register() {
        this.$refs.registerFormRef.validate(async valid => {
          if (!valid) return
          if(this.checked == false) {
              this.$message('请同意电影网服务协议')
             return
          }
          this.registerForm.sex = this.gender === '1'
          axios.defaults.headers.post['Content-Type'] = 'application/json'
          const {
            data: res
          } = await axios.post('sysUser/register', JSON.stringify(this.registerForm));
          if (res.code !== 200) return this.$message.error(res.msg);

          this.$message.success("注册完成！");
          this.$router.push('/login')
          this.$refs.registerFormRef.resetFields()
        })
      },
      toWelcome() {
        this.$router.push('/welcome')
      }
    }
  }
</script>

<style scoped>
  .register-container {
    width: 100%;
    height: 100%;
  }

  .header {
    height: 100px;
    border-bottom: deepskyblue solid 2px;
    padding-top: 6px;
    padding-left: 50px;
    padding-bottom: 0;
    cursor: pointer;
  }

  .register-body {
    display: flex;
    padding-top: 50px;
  }

  .register-form {
    width: 20%;
    margin-left: 25%;
    margin-right: 5%;
  }

  .footer-mini {
    border-top: 1px solid #EEE;
    padding-top: 20px;
    text-align: center;
  }

  .footer {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
</style>
