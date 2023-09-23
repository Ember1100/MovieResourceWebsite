<template>
  <div>
    <section id="index" class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">会员列表</span>
        </h2>
      </header>
      <ul>
        <li v-for="product in productList" :key="product.productId">
          <a :class="['orderBtn', {current:payOrder.productId === product.productId}]"
            @click="selectItem(product.productId,product.productName,product.price)" href="javascript:void(0);">
            {{product.productName}}
            ¥{{product.price}}
          </a>
        </li>
      </ul>

      <div class="PaymentChannel_payment-channel-panel">
        <h3 class="PaymentChannel_title">
          选择支付方式
        </h3>
        <div class="PaymentChannel_channel-options">

          <!-- 选择微信 -->
        <!--  <div :class="['ChannelOption_payment-channel-option', {current:payOrder.payType === 'wxpay'}]"
            @click="selectPayType('wxpay')">
            <div class="ChannelOption_channel-icon"> -->
              <!-- <img src="../assets/img/wxpay.png" class="ChannelOption_icon"> -->
    <!--          <img src="../../assets/img/wxpay.png" class="ChannelOption_icon">
            </div>
            <div class="ChannelOption_channel-info">
              <div class="ChannelOption_channel-label">
                <div class="ChannelOption_label">微信支付</div>
                <div class="ChannelOption_sub-label"></div>
                <div class="ChannelOption_check-option"></div>
              </div>
            </div>
          </div> -->

          <!-- 选择支付宝 -->
          <div :class="['ChannelOption_payment-channel-option', {current:payOrder.payType === 'alipay'}]"
            @click="selectPayType('alipay')">
            <div class="ChannelOption_channel-icon">
              <!-- <img src="../assets/img/alipay.png" class="ChannelOption_icon"> -->
              <img src="../../assets/img/alipay.png" class="ChannelOption_icon">
            </div>
            <div class="ChannelOption_channel-info">
              <div class="ChannelOption_channel-label">
                <div class="ChannelOption_label">支付宝</div>
                <div class="ChannelOption_sub-label"></div>
                <div class="ChannelOption_check-option"></div>
              </div>
            </div>
          </div>

        </div>
      </div>

      <div class="payButtom">
        <el-button type="warning" round style="width: 180px;height: 44px;font-size: 18px;" @click="toBuyer()">
          购买
        </el-button>
        <el-dialog title="" :visible.sync="centerDialogVisible" width="350px"  center>
          <span style="font-size: 20px; color: #000000; ">支付宝扫一扫</span>
         <!-- <qriously :value="codeUrl" :size="300"/> -->
              <img :src="qrsrc" alt="" style="width:100%"><br>
          </el-dialog>
      </div>
    </section>
  </div>

</template>
<script>
  import qs from  'qs'
  export default {
    name: "project",
    data() {
      return {
        isShowPop: false,
        vipSelect: 0,
        payBtnDisabled: false, //确认支付按钮是否禁用
        codeDialogVisible: false, //微信支付二维码弹窗
        productList: [], //商品列表
        payOrder: { //订单信息
          productId: '', //商品id
          payType: 'alipay', //支付方式，
          productName: '',
          price: '',
          userId: '',
          sign: '',
        },
        codeUrl: '', // 二维码
        orderNo: '', //订单号
        timer: null, // 定时器
        centerDialogVisible: false,
        loginUser: JSON.parse(window.sessionStorage.getItem('loginUser')),
        qrsrc: '',

      };
    },
    //页面加载时执行
    created() {
      this.getProductList()
    },
    methods: {
      //获取商品列表
      async getProductList() {
        const {
          data: res
        } = await axios.get('/product/getList')

        this.productList = res.data
        console.log(this.productList)
      },
      //选择商品
      selectItem(productId, productName, price) {
        console.log('商品id：' + productId)
        this.payOrder.productId = productId
        this.payOrder.productName = productName
        this.payOrder.price = price
        this.payOrder.userId = this.loginUser.userId
        console.log(this.payOrder)
        //this.$router.push({ path: '/order' })
      },
      //关闭微信支付二维码对话框时让“确认支付”按钮可用
      closeDialog() {
        console.log('close.................')
        this.payBtnDisabled = false
        console.log('清除定时器')
        clearInterval(this.timer)
      },
      //选择支付方式
      selectPayType(type) {
        console.log('支付方式：' + type)
        this.payOrder.payType = type
        //this.$router.push({ path: '/order' })
      },


      /**
      * 循环查询订单响应
      */
      orderResponse2() {
        this.axios.get('http://www.moreluck.cc:8000/pay/searchOrder/'+this.payOrder.sign).then((response) => {
          if(response.data.code == 201 && this.centerDialogVisible != false ) {
            console.log('未付款')
            setTimeout(() => this.orderResponse2() , 1000)
          }else if(response.data.code==200){
            this.centerDialogVisible = false
            //支付成功，显示提示消息
            this.$message({
              message: '支付成功',
              type: 'success'
            });
          }

        })
      },

      //打算购买
      async toBuyer() {
        this.qrsrc=''
        if (this.loginUser != null) {
          if (this.payOrder.productId != '') {

           /**
            * 获取一个二维码
           */
         this.payOrder.sign = new Date().getTime()
         console.log( this.payOrder.sign )
         let payOrder = qs.stringify(this.payOrder)
         this.qrsrc= this.qrsrc = "http://www.moreluck.cc:8000/pay/getQr?"+payOrder
            //开始轮询查询订单
             this.orderResponse2()
             this.centerDialogVisible = true
          }
        } else {
          this.$message('请先登录');
        }

      },

      //支付成功
      async toPay() {
        this.centerDialogVisible = false
        const {
          data: res
        } = await axios.post('/product/buyProduct', this.payOrder)
        console.log(res)
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

  .PaymentChannel_payment-channel-panel {
    position: relative;
    padding: 22px 0 0;
    margin-top: 20px;
    background: #fff;
    border-radius: 5px;
    box-shadow: 0 4px 30px 0 rgb(238 242 245 / 80%);
  }

  .PaymentChannel_channel-options {
    display: flex;
    flex-wrap: wrap;
    padding: 22px 20px 12px;
  }

  .ChannelOption_payment-channel-option {
    position: relative;
    display: flex;
    align-items: center;
    width: 207px;
    height: 58px;
    margin: 0 10px 16px;
    border: 1px solid #e7ecf0;
    border-radius: 4px;
    cursor: pointer;
  }


  .ChannelOption_payment-channel-option.current {
    border-color: #fa8919;
  }

  .ChannelOption_payment-channel-option.current:after {
    content: "";
    display: block;
    position: absolute;
    right: -1px;
    bottom: -1px;
    width: 28px;
    height: 28px;
    background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADgAAAA4CAYAAACohjseAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAOKADAAQAAAABAAAAOAAAAAANV2hTAAADnUlEQVRoBd3ZzUsUYRwH8O/zzK6llaZkm5mkWdEhN4KEgqKkDhpU9KJpNy/hKch/QK9BdRM7SAQeSnujDgUWXqKgyBBqISKDlswwETXzbZ15eubRGbZ1dp3dmdndmQd255nnZef34Tf7G2UBD7fIta3txKs+FcfA2jwJ1HBq8jwHjMZ5DhiL8xTQCOcZYDycJ4CJcK4HroZzNdAMzrVAszhXApPBuQ6YLM5VwFRwrgGminMF0Aou64FWcVkNtAOXtUC7cFkJtBOXdUC7cVkFdAKXNUCncFkBdBKXcaDTuIwC04HLGDBduIwA04lLOzDduLQCM4FLGzBTuLQAM4lTgZL65lRzGkerWyCduQX41oL9eGvIcOzXJcdxNe2Qqi8LFPv7G4sd+wyBPsNRi4OO4giFVHsdtKpRj1IZ7Nb7sR3bM+gojvohne4E3X1Sd8ivb0Dhr3jNVqCjOH8upLO3QcuPCgtjDEp/G5SBrng2MW4b0BRuTQEwP5kwIMNJvk+60A1aekBMM0WG/LwVLHTfcHn0oC1V1AxOqrsJ6VQnSH4p2NcX0TEk7q8rhu9iL2jJUhFhi/OQn7aAfX6SeN/yrOUiYwanXotsPwxCCEiwiZ9QkQGAJQ4yfxvH9YAUVoh1bGEG8uNmsO+vEu+LmrWUQbM49Xpsbgp0V624NAnsBdlQwjPZFxVKTLdoJ3xND0EKysQEm5uA3NsU93kXs1s/TRmYDE5cbTQEzIyDVh4XpyRQBbI+ADa08nYlgSB8jb18frNYy6ZHsXivHhj9qAdutpMSMGnccjTs1yAwNwm6o0aMkC1BIK8Y7NtLPV5SdhBS/V2Q3I1ijE2EOe48MD6kr0mmkzQwVZwWFBv5wCvpH9CKY2JIFI/cIo7sB6k8AencHZCcPDHHxr4s4aaGte1JH5MCWsVp0bGfA0BkRn+m0ZL9IPxFD10BkXLEMmVkEHJPA7+tx7RtKR1NA+3CaVGy4fcAL/m0/IgYUisl4dVVbUr4DeQHl3imp8S5lTdTQLtxWsBs+B0gR0D5I0RrCq+s8qNmjp/VhiwdVwU6hdOiFv/mRGZBNu2B8qkHyrOrPIURbdryMeGfak7jLEe/ygdw3PTSTW+w0O04lcQIwoZAL+BUIGG0b8Ut6hkc/yb7/P7gfxn0Ck5kj5BO0hoO6UCP4fql8sJWFSqAXsHx75tCCenwlRfWkYbQgsik23Hqo0CtlmpB8fmlLvW2VGFa+wcOncY5YWRXPQAAAABJRU5ErkJggg==) no-repeat;
    background-size: 28px 28px;
  }

  .ChannelOption_channel-icon {
    margin: 0 11px 0 15px;
  }

  .ChannelOption_icon {
    display: block;
    width: 24px;
    height: 24px;
  }

  .ChannelOption_channel-info {
    position: relative;
    flex: 1;
  }

  .ChannelOption_channel-label {
    align-items: center;
    height: 21px;
  }

  .ChannelOption_channel-label {
    display: flex;
    position: relative;
  }

  .ChannelOption_label {
    line-height: 1;
    font-weight: 400;
  }

  .ChannelOption_label {
    font-size: 16px;
    color: #353535;
  }

  .ChannelOption_sub-label {
    flex: 1;
    display: flex;
    align-items: center;
  }

  .ChannelOption_check-option {
    margin-right: 15px;
  }

  .payButtom {
    margin: 30px 0;
    float: right;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a.orderBtn {
    border: 1px solid #f3e2c6;
    background-color: #ffffff;
    color: #f39800;
    border-radius: 5px;
    width: 180px;
    height: 60px;
    line-height: 60px;
    font-size: 20px;
    display: inline-block;
    text-align: center;
    text-decoration: none;
  }

  a.current {
    border: 1px solid;
    background-color: #fffcf5;
  }

  .PaymentChannel_title {
    position: relative;
    display: flex;
    height: 25px;
    padding-left: 30px;
    line-height: 25px;
    font-size: 18px;
    font-weight: 500;
    color: #353535;
  }

  .img_box {
    width: 260px;
    height: 260px;
    background-color: #adadad;
  }
</style>
