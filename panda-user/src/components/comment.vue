<!--评论模块-->
<template>

	<div class="container">
		<div class="comment">
			<div class="write-reply" @click="showCommentInput()">
				<i class="el-icon-edit"></i>
				<span class="add-comment">添加新评论</span>
			</div>
		<transition name="fade">
			<div style="width: 260px;" v-if="showItemId === 100">
				<el-input class="gray-bg-input" v-model="inputComment" type="textarea" :rows="3" autofocus
					placement='top' placeholder="写下你的评论">
				</el-input>
				<div style="text-align: right; margin: 5px;">
					<el-button class="btn" type="info" round @click="cancel">取消</el-button>
					<el-button class="btn" type="success" round @click="commitComment">确定</el-button>
				</div>
			</div>
		</transition>
		</div>
		<div class="comment" v-for="item in comments.list">
			<div class="info">
				<img class="avatar" :src="item.fromAvatar" width="36" height="36" />
				<div class="right">
					<div class="name">{{item.fromName}}</div>
					<div class="date">{{item.createTime}}</div>
				</div>
			</div>
			<div class="content">{{item.content}}</div>
			<div class="control">
				<span class="comment-reply" @click="showCommentInput(item)">
					<span>回复</span>
				</span>
				<span style="margin-left: 50px;" class="el-icon-chat-dot-round">{{item.list.length}}</span>
				<span class="like" :class="{active: item.isLike}" @click="likeClick(item)">
					<i class="iconfont icon-like"></i>
					<span style="margin-left:50px;"
						class="like-num">{{item.likeNum > 0 ? item.likeNum + '人赞' : '赞'}}</span>
				</span>
			</div>
			<el-collapse accordion>
				<el-collapse-item title="展开">
					<div class="reply">
						<div class="item" v-for="reply in item.list">
							<div class="reply-content">
								<img class="avatar" :src="reply.replyFromAvatar" width="23" height="23" />
								<span class="from-name">{{reply.replyFromName}}</span><span class="el-icon-caret-right">
								</span>
								<span class="to-name">@{{reply.replyToName}}</span>
								<span>{{reply.replyContent}}</span>
							</div>
							<div class="reply-bottom">
								<span>{{reply.replyCreateTime}}</span>
								<span class="reply-text" @click="showCommentInput(item, reply)">
									<i class="iconfont icon-comment"></i>
									<span>回复</span>
								</span>
							</div>
						</div>
						<!-- 		<transition name="fade">
							<div style="width: 260px;" v-if="showItemId === item.id">
								<el-input class="gray-bg-input" v-model="inputComment" type="textarea" :rows="3"
									autofocus placement='top' placeholder="写下你的评论">
								</el-input>
								<div style="text-align: right; margin: 5px;">
									<el-button class="btn" type="info" round @click="cancel">取消</el-button>
									<el-button class="btn" type="success" round @clomment">确定<ick="commitC/el-button>
								</div>
							</div>
						</transition> -->
					</div>
				</el-collapse-item>
			</el-collapse>
			<transition name="fade">
				<div style="width: 260px;" v-if="showItemId === item.id">
					<el-input class="gray-bg-input" v-model="inputComment" type="textarea" :rows="3" autofocus
						placement='top' placeholder="写下你的评论">
					</el-input>
					<div style="text-align: right; margin: 5px;">
						<el-button class="btn" type="info" round @click="cancel">取消</el-button>
						<el-button class="btn" type="success" round @click="commitComment">确定</el-button>
					</div>
				</div>
			</transition>
		</div>
	</div>
</template>
<script>
	import Vue from 'vue'
	import qs from "qs"
	export default {
		props: {
			comments: {
			type: Array,
			required: true
			}
		},

		components: {},
		created() {
				console.log(666655)
			},
		
		data() {
			return {
				showItemId: '',
				replyToName: '',
				replyToId: '',
				replyToAvatar: '',
				commentId: '',
				id: 0,
				ownerId: '',
				activeName: '1',
				showComment: false,
				userPicture: JSON.parse(JSON.parse(window.sessionStorage.getItem('loginUser')).userPicture)[0] || '',
				loginUser: JSON.parse(window.sessionStorage.getItem('loginUser')) || null,
				inputComment: '',
			}
		},

		computed: {},
	
		methods: {

			handleChange(val) {
				console.log(val);
			},
			getCurrentTime() {
				//获取当前时间并打印
				let yy = new Date().getFullYear();
				let mm = new Date().getMonth() + 1;
				let dd = new Date().getDate();
				let hh = new Date().getHours();
				let mf = new Date().getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes();
				let ss = new Date().getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds();
				let time = yy + '-' + mm + '-' + dd + ' ' + hh + ':' + mf + ':' + ss;
				return time;
			},

			/**
			 * 点赞
			 */
			async likeClick(item) {
				if (this.loginUser != null) {
					console.log(item)
					if (item.isLike === null) {
						Vue.$set(item, "isLike", true);
						item.likeNum++
					} else {
						if (item.isLike) {
							item.likeNum--
						} else {
							item.likeNum++
						}
						item.isLike = !item.isLike;
					}
					let param = {
						id: item.id,
						createTime: this.getCurrentTime(),
						userId: this.loginUser.userId,
						ownerId: this.comments.ownerId,
						isLike: item.isLike
					}
					console.log(param)
					const {
						data: res
					} = await axios.post('/addLikeRecord', param)
					console.log(res)
				} else {
					this.$message('请登录');
				}
			},

			/**
			 * 点击取消按钮
			 */
			cancel() {
				this.showItemId = ''
			},

			comment() {
				console.log()
				console.log(this.inputComment);

			},
			/**
			 * 提交评论
			 */
			commitComment() {
				axios.post('/comment', {
					content: this.inputComment,
					ownerId: this.comments.ownerId,
					fromId: this.loginUser.userId,
					fromName: this.loginUser.userName,
					fromAvatar: this.userPicture,
					replyToName: this.replyToName,
					replyToId: this.replyToId,
					replyToAvatar: this.replyToAvatar,
					commentId: this.commentId,
					createTime: this.getCurrentTime()
				}).then(res => {
					console.log(res)
					console.log(this.comments)
					if (res.data.code = 200) {
						let param = {
						  ownerId: this.comments.ownerId,
						  userId: this.loginUser.userId
						}
					    axios.post('/getCommentsByOwnerId', param).then(res => {
							this.comments = res.data.data
							console.log(res.data.data)
							this.showItemId = ''
						})
						
						// this.showItemId = ''
						// if (this.commentId != '') { //评论别人
						// 	for (let i = 0; i < this.comments.list.length; i++) {
						// 		if (this.commentId == this.comments.list[i].id) {
						// 			this.comments.list[i].list.push({
						// 				replyContent: this.inputComment,
						// 				replyCreateTime: this.getCurrentTime(),
						// 				commentId: this.commentId,
						// 				replyFromId: this.loginUser.userId,
						// 				replyFromName: this.loginUser.userName,
						// 				replyFromAvatar: this.userPicture,
						// 				replyToAvatar: this.replyToAvatar,
						// 				replyToId: this.replyToId,
						// 				replyToName: this.replyToName,
						// 				replyUpdateTime: this.getCurrentTime()
						// 			})
						// 		}
						// 	}
						// } else { //评论电影
						// 	this.comments.list.unshift({
						// 		content: this.inputComment,
						// 		id: this.id--,
						// 		fromId: this.loginUser.userId,
						// 		fromName: this.loginUser.userName,
						// 		fromAvatar: this.userPicture,
						// 		createTime: this.getCurrentTime(),
						// 		ownerId: this.comments.ownerId,
						// 		likeNum: 0,
						// 		list: []
						// 	})
						// }
					}

				})

			},


			/**
			 * 点击评论按钮显示输入框
			 * item: 当前大评论
			 * reply: 当前回复的评论
			 */
			showCommentInput(item, reply) {
				if (this.loginUser != null) {
					if (item) {
						console.log(item)
						console.log(reply)
						if (reply) {
							//二级回复
							console.log(111)
							this.replyToName = reply.replyFromName
							this.replyToId = reply.replyFromId
							this.replyToAvatar = reply.replyFromAvatar
							this.commentId = reply.commentId
							this.inputComment = ''
						} else {
							//一级回复
							console.log(222)
							this.replyToName = item.fromName
							this.replyToId = item.fromId
							this.commentId = item.id
							this.replyToAvatar = item.fromAvatar
							this.inputComment = ''
						}
						this.showItemId = item.id
					} else {
						console.log(333)
						// this.getMaxShowItemId()
						this.replyToName = ''
						this.replyToId = ''
						this.replyToAvatar = ''
						this.commentId = ''
						this.inputComment = ''
						this.showItemId = 100
						this.ownerId = this.comments.ownerId
					}
					console.log(this.ownerId)
					console.log(this.loginUser)
				} else {
					console.log(this.loginUser)
					this.$message('请登录');
				}
			},
		},

	}
</script>

<style scoped lang="scss">
	@import "../../public/scss/index";

	.container {
		padding: 0 10px;
		box-sizing: border-box;

		.comment {
			display: flex;
			flex-direction: column;
			padding: 10px;
			border-bottom: 1px solid $border-fourth;

			.info {
				display: flex;
				align-items: center;

				.avatar {
					border-radius: 50%;
				}

				.right {
					display: flex;
					flex-direction: column;
					margin-left: 10px;

					.name {
						font-size: 16px;
						color: $text-main;
						margin-bottom: 5px;
						font-weight: 500;
					}

					.date {
						font-size: 12px;
						color: $text-minor;
					}
				}
			}

			.content {
				font-size: 16px;
				color: $text-main;
				line-height: 20px;
				padding: 10px 0;
			}

			.control {
				display: flex;
				align-items: center;
				font-size: 14px;
				color: $text-minor;

				.like {
					display: flex;
					align-items: center;
					margin-right: 20px;
					cursor: pointer;

					&.active,
					&:hover {
						color: $color-main;
					}

					.iconfont {
						font-size: 14px;
						margin-right: 5px;
					}
				}

				.comment-reply {
					display: flex;
					align-items: center;
					cursor: pointer;

					&:hover {
						color: $text-333;
					}

					.iconfont {
						font-size: 16px;
						margin-right: 5px;
					}
				}

			}

			.reply {
				margin: 10px 0;
				border-left: 2px solid $border-first;

				.item {
					margin: 0 10px;
					padding: 10px 0;
					border-bottom: 1px dashed $border-third;

					.reply-content {
						display: flex;
						align-items: center;
						font-size: 14px;
						color: $text-main;

						.from-name {
							color: $color-main;
						}

						.to-name {
							color: $color-main;
							margin-left: 5px;
							margin-right: 5px;
						}
					}

					.reply-bottom {
						display: flex;
						align-items: center;
						margin-top: 6px;
						font-size: 12px;
						color: $text-minor;

						.reply-text {
							display: flex;
							align-items: center;
							margin-left: 10px;
							cursor: pointer;

							&:hover {
								color: $text-333;
							}

							.icon-comment {
								margin-right: 5px;
							}
						}
					}
				}

				.write-reply {
					display: flex;
					align-items: center;
					font-size: 14px;
					color: $text-minor;
					padding: 10px;
					cursor: pointer;

					&:hover {
						color: $text-main;
					}

					.el-icon-edit {
						margin-right: 5px;
					}
				}

				.fade-enter-active,
				fade-leave-active {
					transition: opacity 0.5s;
				}

				.fade-enter,
				.fade-leave-to {
					opacity: 0;
				}

				.input-wrapper {
					padding: 10px;

					.gray-bg-input,
					.el-input__inner {
						background-color: #67C23A;

					}

					.btn-control {
						display: flex;
						justify-content: flex-end;
						align-items: center;
						padding-top: 10px;

						.cancel {
							font-size: 16px;
							color: $text-normal;
							margin-right: 20px;
							cursor: pointer;

							&:hover {
								color: $text-333;
							}
						}

						.confirm {
							font-size: 16px;
						}
					}
				}
			}
		}
	}
</style>
