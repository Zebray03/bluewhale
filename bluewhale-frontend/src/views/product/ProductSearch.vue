<!--Lab5新增，搜索界面-->
<script setup lang="ts">
import {Back} from "@element-plus/icons-vue";
import {searchByCondition, SearchInfo} from "../../api/product";
import {ref, computed} from "vue"
import {useRouter} from "vue-router";
import ProductItem from "../../components/ProductItem.vue";
import {parseCategory} from "../../utils";


const productList = ref()

const router = useRouter()

const name = ref()
const category = ref()
const price_low = ref()
const price_high= ref()
const page_size = ref()
const page_index = ref()
const SearchInfoStored = ref({
  name:"",
  price_low:0,
  price_high:0,
  category:""
})
const page_num = ref()
const possibleCategories = ['FOOD', 'CLOTHES', 'ELECTRONICS', 'FURNITURE', 'ENTERTAINMENT', 'SPORTS', 'LUXURY']
const searchDisabled = computed(() => !(name.value != null || category.value != null || price_low.value != null||price_high.value!=null))
const jumpDisabled=computed(()=>{
  if(page_num.value==null||page_index.value==null){
    return true
  }else{
    return page_num.value<page_index.value || page_index.value<=0
  }
})

const CategoryData = [
  {
    value: "FOOD",
    label: "食品",
    children: [
      {
        value: "DRINK",
        label: "饮品"
      },
      {
        value: "SNACK",
        label: "小吃"
      },
      {
        value: "HEALTHY",
        label: "保健品"
      },
      {
        value: "ORGANIC",
        label: "有机食品"
      }
    ]
  },
  {
    value: "CLOTHES",
    label: "服饰"
  },
  {
    value: "FURNITURE",
    label: "家具"
  },
  {
    value: "ELECTRONICS",
    label: "电子产品"
  },
  {
    value: "ENTERTAINMENT",
    label: "娱乐"
  },
  {
    value: "SPORTS",
    label: "体育产品"
  },
  {
    value: "LUXURY",
    label: "奢侈品"
  }
]

function SearchCondition(){
  const payload:SearchInfo = {
    name:name.value,
    lowValue:  price_low.value,
    highValue:  price_high.value,
    category:  category.value,
    page_size: 3,
    page_index:0,
    reproductList:null,
    page_num:null
  }
  searchByCondition(payload).then(res => {
    if(res.data.code==="000"){
      const results = res.data.result
      productList.value=results.reproductList
      SearchInfoStored.value.name = name.value
      SearchInfoStored.value.price_low=price_low.value
      SearchInfoStored.value.price_high=price_high.value
      SearchInfoStored.value.category=category.value
      page_num.value=results.page_num
      page_size.value=results.page_size
    }else{
      ElMessage({
        message:"搜索失败",
        type:"error",
        center:true
      })
    }
  })
}


function OnPageIndexChange(){


  const payload:SearchInfo = {
    name:SearchInfoStored.value.name,
    lowValue:  SearchInfoStored.value.price_low,
    highValue:  SearchInfoStored.value.price_high,
    category:  SearchInfoStored.value.category,
    page_size: 3,
    page_index:page_index.value,
    reproductList:null,
    page_num:null
  }
  searchByCondition(payload).then(res=>{
    if(res.data.code==="000"){
      console.log(res.data.result.reproductList)
    productList.value=res.data.result.reproductList}else{
      ElMessage({
        message:"切换分页失败",
        type:"error",
        center:true
      })
    }
  })
}

function clearData() {
  window.location.reload();
}

function toBackPage() {
  router.push("/allStore")
}

function toProductDetailPage(productId: number) {
  router.push("/productDetail/" + productId)
}

</script>

<template>
  <el-container>
    <el-main>

      <el-row :gutter="20">

        <el-col :span="1">
          <el-button @click="toBackPage()"
                     type="primary" circle plain class="back-button">
            <el-icon>
              <Back/>
            </el-icon>
          </el-button>
        </el-col>

        <el-col :span="8">
          <el-form-item label="商品名称">
            <el-input
                v-model="name"
                @keyup.enter.native="SearchCondition"
                placeholder="不限"
            >
            </el-input>

          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="商品品类">
            <el-tree-select v-model="category" :data="CategoryData" check-strictly placeholder="任意">
            </el-tree-select>
          </el-form-item>
        </el-col>

        <el-col :span="5">
          <el-form-item label="最高价格">
            <el-input-number v-model="price_high" placeholder="不限">

            </el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="最低">
            <el-input-number v-model="price_low" placeholder="不限">

            </el-input-number>
          </el-form-item>
        </el-col>


        <el-col :span="1.5">
          <el-button @click="clearData">清空</el-button>
        </el-col>

        <el-col :span="1">
          <el-button type="primary" @click="SearchCondition" :disabled="searchDisabled">搜索</el-button>
        </el-col>
      </el-row>

      <el-divider/>

      <div class="all-product-main">

        <ProductItem class="product-item-list"
                     v-for="productVO in productList"
                     :productId="productVO.id"
                     :key="productVO.id"
                     @click="toProductDetailPage(productVO.id)"/>
      </div>
      <el-text v-model="page_num">共{{page_num}}页，第</el-text><el-input v-model="page_index"></el-input><el-text>页</el-text>
      <br>
      <el-button @click="OnPageIndexChange" :disabled="jumpDisabled">跳转</el-button>
    </el-main>
  </el-container>
</template>

<style scoped>
.all-product-main {
  display: flex;
  flex-direction: row;
  padding: 20px;
  flex-flow: wrap;
  align-content: start;
  justify-content: start;
}
</style>