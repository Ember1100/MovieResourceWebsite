<template>
  <el-container>
    <el-main width="1320px">
      <div>
        <video class="el-icon-video-camera" controls="controls" autoplay="" name="media">
          <!-- <source :src="this.publicPath+ this.filmUrl" type="video/mp4"> -->
          <source :src="this.getFilmUrl()" type="video/mp4">
        </video>
      </div>
    </el-main>
    <el-aside width="460px">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="视频" name="first">
          <div><span style="font-size: 30px;">{{movieInfo.movieName}}
            </span>
            <P><span>{{movieInfo.movieArea}} · </span>
              <span v-for="item in movieInfo.categoryVoList"><span>{{item.movieCategoryName}}· </span></span>
              <span style="font-size: 2px;  padding: 0px;" class="el-icon-video-play">{{movieInfo.playNum}}</span>
            </P>
            <p style="font-size: 20px;">剧情简介:<span style="font-size: 15px;">{{movieInfo.movieIntroduction}}</span></p>
          </div>
          <div>
            <span style="font-size: 30px; margin-top: 90px;">猜你喜欢</span>
            <div style="padding: 0px; margin-left: 0px;">
              <div class="movie-list">
                <movie-item :movieItem="item" v-for="item in movieList" :key="item.movieId"></movie-item>
              </div>

              <div class="no-cinema" v-if="sorry">
                <p>抱歉，没有找到相关结果，请尝试用其他条件筛选。</p>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <!--                             -->
        <el-tab-pane label="评论" name="second">
          <comment :comments="commentData"></comment>
        </el-tab-pane>
      </el-tabs>
    </el-aside>

  </el-container>
</template>

<script>
  import movieItem from "../../components/movie/movie-item";
  import moment from "moment";
  import comment from '../../components/comment.vue';
  import qs from "qs";
  export default {
    components: {
      comment,
      movieItem
    },

    data() {
      return {
        filmUrl: this.$route.query.url,
        publicPath: 'http://120.77.42.122:9001/video/',
        commentData: [],
        activeName: 'first',
        movieId: this.$route.query.id,
        loginUser: JSON.parse(window.sessionStorage.getItem('loginUser')) || '',
        movieInfo: {
          moviePictures: [],

        },
       total: 0,
       movieList: [],
       sorry: false
      }

    },
    created() {
      // let b = this.$route.params;
      // let obj = qs.parse(b)
      // console.log(obj)
      // console.log(this.$route.query.id)
      // console.log(this.$route.query.url)
      console.log(11)
      this.getMovieInfo()
      // this.getCommentData();
      this.getMovieList()
    },
    methods: {
      init() {

      },
      handleClick(tab, event) {
        console.log(111)
        console.log(tab.index);
        if (tab.index == 0) { // 0是视频 1是评论
          console.log("视频")
        } else if (tab.index == 1) {
          console.log("评论")
          this.getCommentData();
        }
      },
      getFilmUrl() {
        var url = this.publicPath + this.filmUrl
        return url;
      },

      async getCommentData() {
        let param = {
          ownerId: this.movieId,
          userId: this.loginUser.userId
        }
        const {
          data: res
        } = await axios.post('/getCommentsByOwnerId', param)
        this.commentData = res.data
        console.log(res.data)
      },
      async getMovieInfo() {
        const {
          data: res
        } = await axios.get('sysMovie/getMovieInfo/' + this.movieId)
        console.log(res)
        this.movieInfo = res.data
      },
      async getMovieList() {
        const {
          data: res
        } = await axios.get('sysMovie/getYourLike/' + this.movieId)
        this.movieList = res.data
        this.total = res.total
        if (this.movieList.length === 0) this.sorry = true
        else this.sorry = false
      },
      handleCurrentChange(newPage) {
        this.pageNum = newPage
        this.getMovieList()
      }

    }
  }
</script>

<style scoped>
  .el-icon-video-camera {
    width: 100%;
    /* height: 100%; */
    object-fit: fill
  }


  .el-aside {
    background-color: #ffffff;
    color: #000000;
    height: 700px;
    padding: 15px;
  }

  .el-main {
    overflow-y: scroll;
    background-color: #ffffff;
    height: 100%;
    padding: 0px;
  }

  .movie-list {
    display: flex;
    flex-wrap: wrap;
  }

  .pageHelper {
    display: flex;
    justify-content: center;
    margin: 40px 0px;
  }

  .no-cinema {
    display: flex;
    justify-content: center;
    margin-top: 40px;
    margin-bottom: 40px;
    font-size: 16px;
    color: #333;
  }
</style>
