<template>
  <div>
    <!--面包屑导航区域-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/cinema' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>仪表盘</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="dashboard-editor-container">
      <div>
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            {{select}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="有史以来">有史以来</el-dropdown-item>
            <el-dropdown-item command="近一周">近一周</el-dropdown-item>
            <el-dropdown-item command="近一个月">近一个月</el-dropdown-item>
            <el-dropdown-item command="近半年">近半年</el-dropdown-item>
            <el-dropdown-item command="近一年">近一年</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <panel-group @handleSetLineChartData="handleSetLineChartData" :endVal="endVal" />
      </div>

      <div  class="huadong">
        <el-row>
          <line-chart :chart-data="lineChartData" />
        </el-row>
      </div>
 <!--     <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <raddar-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <pie-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <bar-chart />
          </div>
        </el-col>
      </el-row> -->

    </div>

  </div>
</template>

<script>
  import PanelGroup from '../dashboard/PanelGroup'
  import LineChart from '../dashboard/LineChart'
  import RaddarChart from '../dashboard/RaddarChart'
  import PieChart from '../dashboard/PieChart'
  import BarChart from '../dashboard/BarChart'

  const lineChartData = {
    newVisitis: {
      expectedData: [100, 120, 161, 134, 105, 160, 165],
      actualData: [120, 82, 91, 154, 162, 140, 145],
      date: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun' ],
    },
    messages: {
      expectedData: [200, 192, 120, 144, 160, 130, 140],
      actualData: [180, 160, 151, 106, 145, 150, 130],
      date: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    },
    purchases: {
      expectedData: [80, 100, 121, 104, 105, 90, 100],
      actualData: [120, 90, 100, 138, 142, 130, 130],
      date: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    },
    shoppings: {
      expectedData: [130, 140, 141, 142, 145, 150, 160],
      actualData: [120, 82, 91, 154, 162, 140, 130],
      date: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    }
  }

  export default {
    name: 'Index',
    components: {
      PanelGroup,
      LineChart,
      RaddarChart,
      PieChart,
      BarChart
    },
    data() {
      return {
        // lineChartData: lineChartData.newVisitis,
        lineChartData: {
          expectedData: [],
          actualData: [],
          date: []
        },
        select: '近一周',
        TotalChartData: '',
        endVal: [],
      }
    },
    created() {
      this.getTotal(this.select)
      this.handleSetLineChartData('newVisitis')
    },
    methods: {
      handleSetLineChartData2(type) {
        this.lineChartData = lineChartData[type]
      },
      async handleSetLineChartData(type) {
        const {
          data: res
        } = await axios.get('/dashboard/getLineData/' + type)
        console.log(res)
        this.lineChartData = res.data
      },
      handleCommand(command) {
        this.select = command
        console.log('click on item ' + command)
        this.getTotal(command)
      },
      async getTotal(command) {
        const {
          data: res
        } = await axios.get('/dashboard/getTotal/' + command)
        console.log(res)
        this.endVal = res.data
        console.log(this.endVal)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }
  .huadong {
    background:#fff;
    padding:16px 16px 0;
    margin-bottom:32px;
    overflow-x: auto;
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }

  .el-dropdown-link {
    font-size: 20px;
    cursor: pointer;
    color: #00aa00;
  }

  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
