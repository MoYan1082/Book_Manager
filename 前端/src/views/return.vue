<template>
  <div>
    <Header></Header>
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

      <el-table-column prop="title" label="书名" width="120"> </el-table-column>
      <el-table-column prop="author" label="作者" width="120">
      </el-table-column>

      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button type="text" @click="open(scope.$index)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-footer class="footer" style="background-color: white;">
      <el-button type="primary" @click="returnBook()">还书</el-button>
    </el-footer>
  </div>
</template>

<script>
import Header from "../components/Header.vue";

export default {
  components: { Header },

  data() {
    return {
      bookData: [],
      multipleSelection: [],
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    returnBook() {
      // 发送还书请求
      this.multipleSelection.forEach((item, index, array) => {
        this.$axios.post("/book/return", {"bookId": item.bookId}, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        });
      });
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
  created() {
    this.$axios
      .get("/findbyid", {
        headers: {
          Authorization: localStorage.getItem("token"),
        },
      })
      .then((res) => {
        // value数组中的当前项, index当前项的索引, array原始数组
        res.data.data.forEach((item, index, array) => {
          this.bookData.push(item);
        });
      });
      // console.log(this.bookData)
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