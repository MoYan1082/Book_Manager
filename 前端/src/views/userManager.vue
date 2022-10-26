<template>
  <div>
    <Header></Header>

    <div class="user-manager-main">
      <div v-show="true">
        <el-table
          ref="multipleTable"
          :data="userData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column
            prop="userId"
            label="ID"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="username"
            label="用户名"
            width="120"
          ></el-table-column>
          <el-table-column label="操作" width="200px">
            <template slot-scope="scope">
              <!-- 展开所有的书 -->
              <el-popover placement="right" width="400" trigger="click">
                <el-table :data="userData[scope.$index]['books']">
                  <el-table-column
                    width="150"
                    property="title"
                    label="书名"
                  ></el-table-column>

                  <el-table-column
                    width="100"
                    property="author"
                    label="作者"
                  ></el-table-column>
                </el-table>
                <el-button type="text" slot="reference">显示借的书籍</el-button>
              </el-popover>

              &nbsp;&nbsp;&nbsp;

              <el-button
                type="text"
                style="color: #f56c6c"
                @click="delUser(scope.$index)"
                >删除</el-button
              >
            </template>
          </el-table-column>

          <el-table-column label="状态" width="120">
            <template slot-scope="scope" v-if="userData[scope.$index].admine">
              <span style="color: #e6a23c">管理员</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div>
        <el-dialog title="新增用户" :visible.sync="dialogFormVisible">
          <el-form :model="userForm" :rules="userRules" ref="userForm">
            <el-form-item
              label="账号"
              :label-width="formLabelWidth"
              prop="username"
            >
              <el-input
                autosize
                placeholder="请输入账号"
                v-model="userForm.username"
              >
              </el-input>
            </el-form-item>

            <el-form-item
              label="密码"
              :label-width="formLabelWidth"
              prop="password"
            >
              <el-input
                autosize
                placeholder="请输入密码"
                v-model="userForm.password"
              >
              </el-input>
            </el-form-item>

            <el-form-item label="" :label-width="formLabelWidth" prop="admine">
              <el-switch v-model="userForm.admine" active-text="设为管理员">
              </el-switch>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addUserPost('userForm')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
    </div>

    <el-footer class="footer" style="background-color: white">
      <el-button @click="delUsers">批量删除</el-button>

      <el-button type="primary" @click="dialogFormVisible = true"
        >增加新用户</el-button
      >
    </el-footer>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
export default {
  components: { Header },
  data() {
    return {
      dialogFormVisible: false,
      userData: [],
      multipleSelection: [],
      formLabelWidth: "70px",

      userForm: {
        title: "",
        content: "",
        admine: false,
      },
      userRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  methods: {
    addUserPost(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/user/add", this.userForm, {
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
    delUsers() {
      this.multipleSelection.forEach((element) => {
        this.$axios.post(
          "/user/del",
          { userId: element.userId },
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
    delUser(item) {
      // console.log("测试")
      this.$axios.post(
        "/user/del",
        { userId: this.userData[item].userId },
        {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        }
      );
      location.reload();
    },
  },
  async created() {
    let tmp = [];
    // 请求用户信息
    await this.$axios
      .get("/user/all", {
        headers: {
          Authorization: localStorage.getItem("token"),
        },
      })
      .then((res) => {
        res.data.data.forEach((element) => {
          tmp.push(element);
        });
      });
    tmp.forEach((element) => {
      this.userData.push(element);
    });
    // console.log(this.userData)
    // console.log(this.userData[3]['books'])
    // 请求用户借的书的信息
  },
};
</script>

<style>
.footer {
  background-color: white;
  width: 100%;
  position: fixed;
  bottom: 0px;
}

.user-manager-main {
  border: none;
  width: 620px;
  margin: 0 auto;
}
</style>