<template>
<div class="abb">
  <div style="color: #fff;">
      <div class="crayon-clock-time">{{ time }}</div>
      <div class="crayon-clock-date">
          <span class="date">{{ date }}</span>
          <span class="week">{{ week }}</span>
      </div>
  </div>
</div>
</template>

<script>
export default {
    name: "CrayonClock",
    components: {},
    data() {
        return {
            time: "",
            date: "",
            week: "",
            timerHelper: false,
        };
    },
    mounted() {
        this.timerHelper = setInterval(this.updateTime, 1000);
    },
    beforeDestroy() {
        if (this.timerHelper) {
            clearInterval(this.timerHelper);
            this.timerHelper = false;
        }
    },
    methods: {
        zeroPadding(num, digit) {
            let zero = "";
            for (let i = 0; i < digit; i++) {
                zero += "0";
            }
            return (zero + num).slice(-digit);
        },
        updateTime() {
            const week = [
                "星期日",
                "星期一",
                "星期二",
                "星期三",
                "星期四",
                "星期五",
                "星期六",
            ];
            const cd = new Date();
            this.time =
                this.zeroPadding(cd.getHours(), 2) +
                ":" +
                this.zeroPadding(cd.getMinutes(), 2) +
                ":" +
                this.zeroPadding(cd.getSeconds(), 2);
            this.date =
                this.zeroPadding(cd.getFullYear(), 4) +
                "-" +
                this.zeroPadding(cd.getMonth() + 1, 2) +
                "-" +
                this.zeroPadding(cd.getDate(), 2);

            this.week = week[cd.getDay()];
        },
    },
};
</script>
<style>

/* body {
  height: 100%;
  margin: 0;
} */
.abb{
  background: #0f3854;
  background: radial-gradient(ellipse at center, #0a2e38 0%, #000000 100%);
  background-size: 100%;
  margin: 0px;
}

</style>
