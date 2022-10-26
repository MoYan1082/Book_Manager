<template>
  <div>
    <el-container>
      <el-header class="el-header" style="padding: 0">
        <Header></Header>
      </el-header>

      <el-row :gutter="20">
        <el-col :span="14">
          <div class="grid-content bg-purple">
            <!-- 通知栏 -->
            <div class="inform">
              <el-carousel class="index-inform">
                <el-carousel-item v-for="item in informs_length" :key="item">
                  <span class="inform-title">
                    <span style="color: #f56c6c; font-size: 35px">
                      {{ informs[item - 1].title }}
                    </span>
                    <br />
                    <span>
                      {{ informs[item - 1].content }}
                    </span>
                  </span>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="grid-content bg-purple">
            <!-- 问题反馈栏 -->
            <div class="problem">
              <div v-for="item in problems.length" :key="item">
                <el-card
                  :body-style="{ padding: '0px' }"
                  class="problem-card"
                  v-if="check_problem_index(item)"
                >
                  <div style="padding: 14px">
                    <div>{{ problems[item - 1].title }}</div>
                    <br />
                    <div>{{ problems[item - 1].content }}</div>
                    <!-- 问题回复栏 -->
                    <el-popover placement="right" width="400" trigger="click">
                      <el-table :data="problems[item - 1]['reply']">
                        <el-table-column
                          width="150"
                          property="userId"
                          label="用户"
                        ></el-table-column>

                        <el-table-column
                          width="100"
                          property="content"
                          label="内容"
                        ></el-table-column>
                      </el-table>
                      <el-button type="text" slot="reference"
                        >查看所有回复</el-button
                      >
                    </el-popover>

                    <!-- 空格 -->
                    &nbsp;&nbsp;&nbsp;&nbsp;

                    <el-button
                      type="text"
                      @click="addReply(item - 1)"
                      v-show="hasLogin"
                      >回复</el-button
                    >

                    <el-dialog
                      title="新增回复"
                      :visible.sync="dialogFormVisible"
                    >
                      <el-form
                        :model="ruleFormReply"
                        :rules="rules"
                        ref="ruleFormReply"
                      >
                        <el-form-item
                          label="内容"
                          :label-width="formLabelWidth"
                          prop="content"
                        >
                          <el-input
                            type="textarea"
                            autosize
                            placeholder="请输入内容"
                            v-model="ruleFormReply.content"
                          >
                          </el-input>
                        </el-form-item>
                      </el-form>

                      <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false"
                          >取 消</el-button
                        >
                        <el-button
                          type="primary"
                          @click="addReplyPost('ruleFormReply')"
                          >确 定</el-button
                        >
                      </div>
                    </el-dialog>

                    <!-- 空格 -->
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <el-button
                      type="text"
                      style="color: #f56c6c"
                      v-show="
                        problems[item - 1].userId ==
                        $store.getters.getUser.userId
                      "
                      @click="delProblem(item - 1)"
                      >删除当前问题</el-button
                    >
                  </div>
                </el-card>
              </div>

              <el-pagination
                background
                layout="prev, pager, next"
                :current-page="problems_cur"
                :page-size="2"
                :total="problems_length"
                @current-change="problemChange"
                class="left-right-page"
              >
              </el-pagination>
            </div>
          </div>
        </el-col>
      </el-row>

      <div style="margin-top: 30px">
        <el-row :gutter="20">
          <el-col :span="14" style="margin-left: 30px">
            <!-- 书籍搜索的结果 -->
            <div v-show="true">
              <el-table
                ref="multipleTable"
                :data="bookData"
                tooltip-effect="dark"
                style="width: 100%"
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="55"> </el-table-column>

                <el-table-column label="封面" width="120">
                  <template slot-scope="scope">
                    <el-image
                      style="width: 100px; height: 100px"
                      :src="bookData[scope.$index].photo"
                      fit
                    ></el-image>
                  </template>
                </el-table-column>

                <el-table-column prop="title" label="书名" width="120">
                </el-table-column>
                <el-table-column prop="author" label="作者" width="120">
                </el-table-column>

                <el-table-column label="操作" width="120">
                  <template slot-scope="scope">
                    <el-button type="text" @click="open(scope.$index)"
                      >详情</el-button
                    >
                  </template>
                </el-table-column>
                <el-table-column label="状态" width="120">
                  <template
                    slot-scope="scope"
                    v-if="bookData[scope.$index].status"
                  >
                    <span style="color: #e6a23c">已经被借走</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
          <el-col :span="6" style="margin-left: -30px">
            <!-- 书籍筛选条件 -->
            <div>
              <el-row>
                <el-col :span="12"
                  ><div class="grid-content bg-purple">
                    <el-input
                      v-model="factor"
                      placeholder="请输入书名"
                    ></el-input>

                    <el-tree
                      ref="tree"
                      :data="tagData"
                      show-checkbox
                      node-key="id"
                    >
                    </el-tree></div
                ></el-col>
                <el-col :span="12"
                  ><div class="grid-content bg-purple-light">
                    <el-button
                      icon="el-icon-search"
                      circle
                      @click="findBook"
                      style="margin-left: 20px"
                    ></el-button></div
                ></el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 新增问题 -->
      <div>
        <el-dialog title="新增问题" :visible.sync="problemFormVisible">
          <el-form :model="problemForm" :rules="problemRules" ref="problemForm">
            <el-form-item
              label="标题"
              :label-width="formLabelWidth"
              prop="title"
            >
              <el-input
                type="textarea"
                autosize
                placeholder="请输入标题"
                v-model="problemForm.title"
              >
              </el-input>
            </el-form-item>
            <el-form-item
              label="内容"
              :label-width="formLabelWidth"
              prop="content"
            >
              <el-input
                type="textarea"
                autosize
                placeholder="请输入内容"
                v-model="problemForm.content"
              >
              </el-input>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="problemFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addProblem('problemForm')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>

      <!-- 新增公告 -->
      <div v-show="isAdmine">
        <el-dialog title="新增公告" :visible.sync="informFormVisible">
          <el-form :model="informForm" :rules="informRules" ref="informForm">
            <el-form-item
              label="标题"
              :label-width="formLabelWidth"
              prop="title"
            >
              <el-input
                type="textarea"
                autosize
                placeholder="请输入标题"
                v-model="informForm.title"
              >
              </el-input>
            </el-form-item>
            <el-form-item
              label="内容"
              :label-width="formLabelWidth"
              prop="content"
            >
              <el-input
                type="textarea"
                autosize
                placeholder="请输入内容"
                v-model="informForm.content"
              >
              </el-input>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="informFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addInform('informForm')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>

      <!-- 按钮 -->
      <el-footer v-show="true" class="footer" style="background-color: white">
        <span v-show="hasLogin">
          <el-button @click="borrowBook()">借书</el-button>
        </span>

        <span style="margin-left: 20px" v-show="hasLogin">
          <el-button type="primary" @click="problemFormVisible = true"
            >提问</el-button
          >
        </span>

        <span style="margin-left: 20px" v-show="isAdmine">
          <el-button type="primary" @click="informFormVisible = true"
            >发布公告</el-button
          >
        </span>

        <!-- 删除公告 -->
        <span style="margin-left: 20px" v-show="isAdmine">
          <el-select
            v-model="informDel"
            slot="prepend"
            placeholder="请选择需要删除的通知"
          >
            <el-option
              v-for="item in informs"
              :key="item.informId"
              :value="item.informId"
              :label="item.title"
            />
          </el-select>

          <el-button
            type="danger"
            icon="el-icon-delete"
            @click="delInform"
          ></el-button>
        </span>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
export default {
  name: "index.vue",
  components: { Header },
  data() {
    return {
      tagData: [
        {
          id: 1,
          label: "小说",
          children: [
            {
              id: 3,
              label: "玄幻",
            },
            {
              id: 4,
              label: "恐怖",
            },
            {
              id: 5,
              label: "悬疑",
            },
            {
              id: 6,
              label: "致郁",
            },
            {
              id: 7,
              label: "案件",
            },
            {
              id: 8,
              label: "冒险",
            },

            {
              id: 9,
              label: "魔法",
            },
          ],
        },
        {
          id: 2,
          label: "哲学",
          children: [
            {
              id: 10,
              label: "人性",
            },
            {
              id: 11,
              label: "马克思",
            },
          ],
        },
      ],
      factor: "",

      bookData: [],
      multipleSelection: [],

      isAdmine: false,
      hasLogin: false,

      informs: [],
      informs_length: 0,
      problems: [],
      problems_length: 0,
      problems_cur: 1,

      dialogFormVisible: false,
      problemFormVisible: false,
      informFormVisible: false,
      formLabelWidth: "70px",

      cur_reply_id: 0,
      ruleFormReply: {
        content: "",
        problemId: 0,
      },
      rules: {
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },

      problemForm: {
        title: "",
        content: "",
      },
      problemRules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },

      informForm: {
        title: "",
        content: "",
      },
      informRules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },

      informDel: null,
    };
  },
  methods: {
    async findBook() {
      let tar = [];
      this.$refs.tree.getCheckedNodes().forEach((item) => {
        if (!item.children) {
          tar.push(item.label);
        }
      });
      // console.log(this.$refs.tree.getCheckedNodes());
      // console.log(tar)

      let tmp = [];
      await this.$axios
        .post("/find", {
          tags: tar,
          factor: this.factor,
        })
        .then((res) => {
          tmp = res.data.data;

          // location.reload()
          // console.log(res)
        });

      this.bookData = [];
      // console.log(tmp)
      tmp.forEach((item) => {
        this.bookData.push(item);
      });
    },

    open(index) {
      const h = this.$createElement;
      this.$msgbox({
        title: this.bookData[index].title,
        message: h("p", null, [
          h("span", null, "作者："),
          h("i", { style: "color: teal" }, this.bookData[index].author),
          h("br"),
          h("span", null, "简介："),
          h("i", { style: "color: teal" }, this.bookData[index].description),
          h("br"),
          h("span", null, "标签："),
          h("i", { style: "color: teal" }, this.bookData[index].tags),
        ]),

        confirmButtonText: "确定",
      });
    },
    borrowBook() {
      console.log(this.multipleSelection);
      // 发送借书请求
      this.multipleSelection.forEach((item, index, array) => {
        this.$axios.post(
          "/book/borrow",
          { bookId: item.bookId },
          {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          }
        );
      });
      location.reload();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delInform() {
      this.$axios.post(
        "/inform/del",
        { informId: this.informDel },
        {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        }
      );
      this.informFormVisible = false;
      location.reload();
    },
    addInform(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/inform/add", this.informForm, {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          });
          this.informFormVisible = false;
          location.reload();
          return true;
        } else {
          return false;
        }
      });
    },
    addProblem(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/problem/add", this.problemForm, {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          });
          this.problemFormVisible = false;
          location.reload();
          return true;
        } else {
          return false;
        }
      });
    },

    async delProblem(index) {
      await this.$axios.post(
        "/problem/del",
        { problemId: this.problems[index].problemId },
        {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        }
      );

      location.reload();
    },

    problemChange(index) {
      this.problems_cur = index;
      // console.log((this.problems_cur-1)*2)
    },
    check_problem_index(item) {
      // console.log(item)
      // console.log(2*(this.problems_cur-1)+1)

      if (item >= 2 * (this.problems_cur - 1) + 1) {
        if (item <= 2 * (this.problems_cur - 1) + 2) return true;
        else return false;
      }
      return false;
    },

    addReply(item) {
      this.cur_reply_id = item;
      this.dialogFormVisible = true;
    },
    addReplyPost(formName) {
      this.ruleFormReply.problemId = this.problems[this.cur_reply_id].problemId;
      this.$refs[formName][0].validate((valid) => {
        if (valid) {
          this.$axios.post("/reply/add", this.ruleFormReply, {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          });
          this.dialogFormVisible = false;
          location.reload();
          return true;
        } else {
          return false;
        }
      });
    },
  },
  async created() {
    if (
      this.$store.getters.getUser != null &&
      this.$store.getters.getUser != ""
    ) {
      if (
        this.$store.getters.getUser.admine != null &&
        this.$store.getters.getUser.admine == true
      ) {
        this.isAdmine = true;
      }

      this.hasLogin = true;
    }

    await this.$axios.get("/inform/all").then((res) => {
      res.data.data.forEach((element) => {
        this.informs.push(element);
        this.informs_length += 1;
      });
    });

    // console.log(this.informs)
    let tmp = [];

    await this.$axios.get("/problem/all").then((res) => {
      res.data.data.forEach((element) => {
        this.problems_length += 1;
        tmp.push(element);
      });
    });
    for (let i = 0; i < this.problems_length; i++) {
      let element = tmp[i];
      await this.$axios
        .post("/reply/find", { problemId: element.problemId })
        .then((a) => {
          element["reply"] = a.data.data;
        });
    }

    this.problems = tmp;

    // 请求图书信息
    this.$axios.post("/find", { factor: "" }).then((res) => {
      res.data.data.forEach((item, index, array) => {
        this.bookData.push(item);
      });
    });
  },
};
</script>

<style>
.problem-title {
  font-size: 22px;
}

.problem-card {
  height: 120px;
}

.el-carousel__item h3 {
  color: red;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(5n) {
  background-image: url("https://school.app.fjnu.edu.cn/inte-service-platform-hall-schoolweb/img/0(1).fd7ec570.png");
}
.el-carousel__item:nth-child(5n + 1) {
  background-image: url("https://school.app.fjnu.edu.cn/inte-service-platform-hall-schoolweb/img/0(2).731c04da.png");
}
.el-carousel__item:nth-child(5n + 2) {
  background-image: url("https://school.app.fjnu.edu.cn/inte-service-platform-hall-schoolweb/img/0(3).6a67f9aa.png");
}
.el-carousel__item:nth-child(5n + 3) {
  background-image: url("https://school.app.fjnu.edu.cn/inte-service-platform-hall-schoolweb/img/0(4).bc0a574c.png");
}
.el-carousel__item:nth-child(5n + 4) {
  background-image: url("https://school.app.fjnu.edu.cn/inte-service-platform-hall-schoolweb/img/0(5).ae1150e2.png");
}

.index-inform {
  padding-top: 0px;
  height: 284px;
  text-align: center;
  overflow: hidden;
}

.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.problem {
  width: 500px;
}

.left-right-page {
  margin-top: 10px;
  margin-left: -10px;
}

.inform-title {
  display: block;
  width: 600px;
  margin: 0 auto;
  margin-top: 40px;
  color: white;
  font-size: 30px;
  background-color: rgba(103, 155, 213, 0.5);
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.footer {
  background-color: white;
  width: 100%;
  position: fixed;
  bottom: 0px;
}
</style>