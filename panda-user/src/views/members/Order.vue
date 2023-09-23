<template>

  <div class="bg-fa of">
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">我的会员</span>
        </h2>
      </header>
      <el-table :data="userServie" border style="width: 100%">
        <el-table-column prop="userName" label="用户名" width="230"></el-table-column>
        <el-table-column prop="status" label="会员状态" width="230"></el-table-column>
        <el-table-column prop="serviceEndTime" label="到期时间"></el-table-column>
        <el-table-column prop="surplusDay" label="剩余天数"></el-table-column>
      </el-table>
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">订单记录</span>
        </h2>
      </header>
      <el-table :data="list" border style="width: 100%">
        <el-table-column label="序号" type="index" width="50"></el-table-column>
        <!-- <el-table-column prop="orderId" label="订单编号" width="230" ></el-table-column> -->
        <el-table-column prop="productName" label="订单标题"></el-table-column>
        <el-table-column prop="createTime" label="下单时间"></el-table-column>
        <el-table-column prop="totalFee" label="订单金额">
          <template slot-scope="scope">
            {{scope.row.payment}} 元
          </template>
        </el-table-column>
        <el-table-column label="订单状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === '未支付'">
              {{scope.row.status}}
            </el-tag>
            <el-tag v-if="scope.row.status === '支付成功'" type="success">
              {{ scope.row.status }}
            </el-tag>
            <el-tag v-if="scope.row.status  === '超时已关闭'" type="warning">
              {{scope.row.status }}
            </el-tag>
            <el-tag v-if="scope.row.status  === '用户已取消'" type="info">
              {{scope.row.status }}
            </el-tag>
            <el-tag v-if="scope.row.status  === '退款中'" type="danger">
              {{scope.row.status }}
            </el-tag>
            <el-tag v-if="scope.row.status  === '已退款'" type="info">
              {{scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="createTime" label="创建时间"></el-table-column> -->
  <!--      <el-table-column label="操作" width="100" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === '未支付'" type="text" @click="cancel(scope.row.orderId)">支付或取消
            </el-button>
            <el-button v-if="scope.row.status === '支付成功'" type="text" @click="refund(scope.row.orderId)">退款</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </section>

    <!-- 退款对话框 -->
    <el-dialog :visible.sync="refundDialogVisible" @close="closeDialog" width="350px" center>
      <el-form>
        <el-form-item label="退款原因">
          <el-select v-model="reason" placeholder="请选择退款原因">
            <el-option label="不喜欢" value="不喜欢"></el-option>
            <el-option label="买错了" value="买错了"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="toRefunds()" :disabled="refundSubmitBtnDisabled">确 定</el-button>
      </div>
    </el-dialog>
    <div class="pageHelper">
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          background
          layout="prev, pager, next"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
  export default {
    name: "project",
    data() {
      return {
        list: [], //订单列表
        refundDialogVisible: false, //退款弹窗
        orderNo: '', //退款订单号
        reason: '', //退款原因,
        refundSubmitBtnDisabled: false, //防止重复提交
        loginUser: JSON.parse(window.sessionStorage.getItem('loginUser')) || '',
        userServie:[],
        total: 0,
        pageSize: 2,
        pageNum: 1,
        movieList: [],
        sorry: false
      };
    },
    //页面加载时执行
    created() {
      if(this.loginUser != '' ) {
          this.getUserServiceDetail()
          this.showOrderList()
      }

    },

    methods: {
      //显示订单列表
      async showOrderList() {
        let param = {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          userId: this.loginUser.userId
        }
        const {
          data: res
        } = await axios.get('/product/getUserMemberOrder', {params: param})
        console.log(res)
        this.list = res.data.list,
        this.total = res.data.total
      },
      handleCurrentChange(newPage) {
        this.pageNum = newPage
        this.showOrderList()
      },
      async getUserServiceDetail() {
        const {
          data: res
        } = await axios.get('/product/getUserServiceDetail/' + this.loginUser.userId)
        this.userServie = res.data
        console.log(this.userServie)
      },
      //用户取消订单
      cancel(orderNo) {
        // wxPayApi.cancel(orderNo).then(response => {
        //   this.$message.success(response.message)
        //   //刷新订单列表
        //   this.showOrderList()
        // })
      },

      //退款对话框
      refund(orderNo) {
        this.refundDialogVisible = true
        this.orderNo = orderNo
      },

      //关闭退款对话框
      closeDialog() {
        console.log('close.................')
        this.refundDialogVisible = false
        //还原组件状态
        this.orderNo = ''
        this.reason = ''
        this.refundSubmitBtnDisabled = false
      },

      //确认退款
      toRefunds() {
        this.refundSubmitBtnDisabled = true //禁用按钮，防止重复提交
        // wxPayApi.refunds(this.orderNo, this.reason).then(response => {
        //   console.log('response', response)
        //   this.closeDialog()
        //   this.showOrderList()
        // })
      },
    },
  };
</script>
<style scoped>
  .container {
    margin-left: auto;
    margin-right: auto;
    width: 1160px;
  }

  .comm-title {
    overflow: hidden;
    clear: both;
    margin: 40px 0 30px;
  }

  .movie-list{
    display: flex;
    flex-wrap: wrap;
  }

  .pageHelper{
    display: flex;
    justify-content: center;
    margin: 40px 0px;
  }

  .no-cinema{
    display: flex;
    justify-content: center;
    margin-top: 40px;
    margin-bottom: 40px;
    font-size: 16px;
    color: #333;
  }
</style>
