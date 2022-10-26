<template>
  <div>
    <Header></Header>

    <div class="user-manager-main">
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

          <el-table-column label="操作" width="200px">
            <template slot-scope="scope">
              <el-button type="text" @click="open(scope.$index)"
                >详情</el-button
              >
              <el-button type="text" @click="updataId(scope.$index)"
                >修改</el-button
              >
              <el-button
                style="color: #e6a23c"
                type="text"
                @click="delBook(scope.$index)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div>
      <el-dialog title="新增图书" :visible.sync="dialogFormVisible">
        <el-form :model="bookForm" :rules="bookRules" ref="bookForm">
          <el-form-item label="书名" :label-width="formLabelWidth" prop="title">
            <el-input
              autosize
              placeholder="请输入书名"
              v-model="bookForm.title"
            >
            </el-input>
          </el-form-item>
          <el-form-item
            label="作者"
            :label-width="formLabelWidth"
            prop="author"
          >
            <el-input
              autosize
              placeholder="请输入作者"
              v-model="bookForm.author"
            >
            </el-input>
          </el-form-item>
          <el-form-item
            label="封面url"
            :label-width="formLabelWidth"
            prop="photo"
          >
            <el-input
              autosize
              placeholder="请输入封面url"
              v-model="bookForm.photo"
            >
            </el-input>
          </el-form-item>
          <el-form-item
            label="简介"
            :label-width="formLabelWidth"
            prop="discription"
          >
            <el-input
              autosize
              type="textarea"
              placeholder="请输入简介"
              v-model="bookForm.discription"
            >
            </el-input>
          </el-form-item>
          <div>
            <el-checkbox-group
              v-model="bookForm.tags"
              style="margin-left: 30px"
            >
              <el-checkbox
                v-for="item in labels"
                :key="item"
                :label="item"
              ></el-checkbox>

              <!-- <el-checkbox label="恐怖"></el-checkbox>
              <el-checkbox label="悬疑"></el-checkbox>
              <el-checkbox label="致郁"></el-checkbox>
              <el-checkbox label="案件"></el-checkbox>
              <el-checkbox label="冒险"></el-checkbox>
              <el-checkbox label="魔法"></el-checkbox>
              <el-checkbox label="人性"></el-checkbox>
              <el-checkbox label="马克思"></el-checkbox> -->
            </el-checkbox-group>
          </div>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addBook('bookForm')"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </div>

    <div>
      <el-dialog title="更新图书" :visible.sync="dialogFormVisible2">
        <el-form
          :model="changeBookForm"
          :rules="changeBookRules"
          ref="changeBookForm"
        >
          <el-form-item label="书名" :label-width="formLabelWidth" prop="title">
            <el-input
              autosize
              placeholder="请输入书名"
              v-model="changeBookForm.title"
            >
            </el-input>
          </el-form-item>
          <el-form-item
            label="作者"
            :label-width="formLabelWidth"
            prop="author"
          >
            <el-input
              autosize
              placeholder="请输入作者"
              v-model="changeBookForm.author"
            >
            </el-input>
          </el-form-item>
          <el-form-item
            label="封面url"
            :label-width="formLabelWidth"
            prop="photo"
          >
            <el-input
              autosize
              placeholder="请输入封面url"
              v-model="changeBookForm.photo"
            >
            </el-input>
          </el-form-item>
          <el-form-item
            label="简介"
            :label-width="formLabelWidth"
            prop="discription"
          >
            <el-input
              autosize
              type="textarea"
              placeholder="请输入简介"
              v-model="changeBookForm.discription"
            >
            </el-input>
          </el-form-item>
          <div>
            <el-checkbox-group
              v-model="changeBookForm.tags"
              style="margin-left: 30px"
            >
              <el-checkbox
                v-for="item in labels"
                :key="item"
                :label="item"
              ></el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="updateBook('changeBookForm')"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </div>

    <el-footer class="footer" style="background-color: white">
      <el-button @click="delBooks">批量删除</el-button>

      <el-button type="primary" @click="dialogFormVisible = true"
        >增加新书</el-button
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
      labels: [
        "玄幻",
        "恐怖",
        "悬疑",
        "致郁",
        "案件",
        "冒险",
        "魔法",
        "人性",
        "马克思",
      ],

      bookData: [],
      multipleSelection: [],
      formLabelWidth: "70px",

      dialogFormVisible: false,
      dialogFormVisible2: false,

      bookForm: {
        title: "",
        author: "",
        photo: "",
        description: "",
        tags: [],
      },
      changeBookForm: {
        bookId: 0,
        title: "",
        author: "",
        photo: "",
        description: "",
        tags: [],
      },
      bookRules: {
        title: [{ required: true, message: "请输入书名", trigger: "blur" }],
        author: [{ required: true, message: "请输入作者", trigger: "blur" }],
      },
      changeBookRules: {
        title: [{ required: true, message: "请输入书名", trigger: "blur" }],
        author: [{ required: true, message: "请输入作者", trigger: "blur" }],
      },
    };
  },
  methods: {
    addBook(formName) {
      // console.log(this.bookForm)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/book/add", this.bookForm, {
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
    updataId(item) {
      this.dialogFormVisible2 = true
      this.changeBookForm.bookId = this.bookData[item].bookId
    },

    updateBook(formName) {
      // console.log(this.changeBookForm.tags);

      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/book/update", this.changeBookForm, {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          });
          this.informFormVisible2 = false;
          location.reload();
          return true;
        } else {
          return false;
        }
      });

    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delBooks() {
      // console.log(this.multipleSelection);
      this.multipleSelection.forEach((item) => {
        this.$axios.post(
          "/book/del",
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
    delBook(item) {
      this.$axios.post(
        "/book/del",
        { bookId: this.bookData[item].bookId },
        {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        }
      );
      location.reload();
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
  },
  async created() {
    await this.$axios.get("/book/all").then((res) => {
      res.data.data.forEach((item, index, array) => {
        this.bookData.push(item);
      });
    });
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
</style>