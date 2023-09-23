<template>
<el-container>
    <el-header>
      <el-menu :default-active="$route.path"
               class="nav-menu"
               mode="horizontal"
               :router="true"
               background-color="#545c64"
               text-color="#fff"
               active-text-color="#ffd04b">
        <el-menu-item :index="item.path" v-for="item in menuList" :key="item.id">{{item.name}}</el-menu-item>
      </el-menu>
    </el-header>
     <router-view :movieCategoryId="categoryRadio" :orderByColumn="orderByColumn"></router-view>
    </el-main>
  </el-container>
</template>
<script>
  export default {
    name: "project",
    data() {
      return {
        menuList: [
          {
            id: '1',
            name: '会员详情',
            path: '/member/pay'
          },
          {
            id: '2',
            name: '我的详情',
            path: '/member/order'
          }
        ],
         categoryRadio: 0,
         orderByColumn: 'releaseDate'

      };
    },
    created() {

    },
    methods: {

    },
  }
</script>
<style scoped>
  .el-header{
    padding: 0px;
  }

  .nav-menu{
    display: flex;
    justify-content: space-around;
  }

  .el-menu-item{
    font-size: 16px;
  }

  .el-menu-item:hover{
    color: #FFFFFF !important;
  }

  .el-main{
    width: 1200px;
    margin: 0 auto;
  }

  .tags-container{
    border: solid #EEEEEE 1px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }

  .tags-line{
    width: 100%;
    display: flex;
    padding: 10px;
    box-sizing: border-box;
    border-bottom: solid #EEEEEE 1px;
  }

  .tags-line:last-child{
    border-bottom: 0;
  }

  .tags-title {
    width: 5%;
    height: 24px;
    line-height: 24px;
    float: left;
    color: #999;
    font-size: 14px;
  }

  .el-radio-group{
    width: 92%;
    margin-left: 20px;
  }

  .el-radio-button {
    margin-right: 10px;
  }

  .el-radio-button:last-child{
    margin-right: 0;
  }

  .el-radio-button >>> .el-radio-button__inner {
    border: solid #eee 0px !important;
    border-radius: 50px !important;
  }

  .order-by-container{
    margin: 30px 10px 0 10px;
  }
</style>
