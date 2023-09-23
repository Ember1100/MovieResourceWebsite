<template>
  <div>
      <CrayonClock style="height: 100%;" />
 <TongJi/>
    <p style="color: darkred; font-size: 40px;">网站访问量统计</p>
    <el-container style="color: aqua;">
      <el-main>
        <ve-line :data="chartData"></ve-line>
        <!-- <ve-ring :data="chartData2"></ve-ring> -->
      </el-main>
      <el-aside width="50%">
        <CrayonClock style="height: 100%;" />
      </el-aside>
    </el-container>

    <p style="color: darkred; font-size: 40px;">用户数量统计</p>
    <el-container>
      <el-aside width="50%">
        <p>hh</p>

      </el-aside>
      <el-main>
        <ve-pie :data="chartData2"></ve-pie>
      </el-main>
    </el-container>
    <!-- <ve-gauge :data="chartData3"></ve-gauge> -->
    <p style="color: darkred; font-size: 40px;">用户下单数量统计</p>
    <el-container style="color: aqua;">
      <el-aside width="50%">
        <ve-histogram :data="chartData4"></ve-histogram>
      </el-aside>
      <el-main>
        <ve-pie :data="chartData2"></ve-pie>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import VeLine from 'v-charts/lib/line.common'
  import VeRing from 'v-charts/lib/ring.common'
  import VePie from 'v-charts/lib/pie.common'
  import VeGauge from 'v-charts/lib/gauge.common'
  import VeHistogram from 'v-charts/lib/histogram.common'
  import ElementUI from 'element-ui';
  import 'element-ui/lib/theme-chalk/index.css';
  import CrayonClock from '../../components/clock.vue'
  import TongJi from '../../components/tongji.vue'
  export default {
    components: {
      VeLine,
      VeRing,
      VePie,
      VeGauge,
      VeHistogram,
      CrayonClock,
      TongJi,
    },
    data() {
      return {
        tableData: [{
          userNum: 160,
          allNum: 200,
          vipNum: 40,
        }, ],
        chartData: {
          columns: ['日期', '访问量'],
          rows: [{
              '日期': '1月1日',
              '访问量': 123
            },
            {
              '日期': '1月2日',
              '访问量': 1223
            },
            {
              '日期': '1月3日',
              '访问量': 2123
            },
            {
              '日期': '1月4日',
              '访问量': 4123
            },
            {
              '日期': '1月5日',
              '访问量': 3123
            },
            {
              '日期': '1月6日',
              '访问量': 7123
            }
          ]
        },
        chartData2: {
          columns: ['用户类型', '数量'],
          rows: [{
              '用户类型': '普通用户',
              '数量': 160
            },
            {
              '用户类型': '会员用户',
              '数量': 40
            },

          ]
        },
        chartData3: {
          columns: ['type', 'a', 'b', 'value'],
          rows: [{
            type: '速度',
            value: 80,
            a: 1,
            b: 2
          }]
        },
        chartData4: {
          columns: ['日期', '访问用户', '下单用户', '下单率'],
          rows: [{
              '日期': '1/1',
              '访问用户': 1393,
              '下单用户': 1093,
              '下单率': 0.32
            },
            {
              '日期': '1/2',
              '访问用户': 3530,
              '下单用户': 3230,
              '下单率': 0.26
            },
            {
              '日期': '1/3',
              '访问用户': 2923,
              '下单用户': 2623,
              '下单率': 0.76
            },
            {
              '日期': '1/4',
              '访问用户': 1723,
              '下单用户': 1423,
              '下单率': 0.49
            },
            {
              '日期': '1/5',
              '访问用户': 3792,
              '下单用户': 3492,
              '下单率': 0.323
            },
            {
              '日期': '1/6',
              '访问用户': 4593,
              '下单用户': 4293,
              '下单率': 0.78
            }
          ]
        },
      }
    },
    methods: {

    }
  }
</script>
<style scoped lang="scss">

  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }

  .el-header,
  .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }



  body>.el-container {
    margin-bottom: 40px;
  }
</style>
