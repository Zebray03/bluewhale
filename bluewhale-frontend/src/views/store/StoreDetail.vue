<script setup lang="ts">
import {ref} from "vue"
import {getStoreById} from "../../api/store.ts"
import {getProductsByStoreId} from "../../api/product.ts"
import {Back} from "@element-plus/icons-vue"
import ProductItem from "../../components/ProductItem.vue"
import {parseRating} from "../../utils";
import {useRouter} from "vue-router";

const router = useRouter()


const role = sessionStorage.getItem("role")
// 从当前router地址中获得storeId参数值
const storeId = router.currentRoute.value.params.storeId
const actualStoreId = sessionStorage.getItem("storeId")
const storeVO = ref()
const name = ref('')
const logoUrl = ref('')
const rating = ref(0)
const number = ref(0)
const location = ref('')
const productList = ref()

console.log(storeId === actualStoreId)
console.log(actualStoreId)

getStoreById(Number(storeId)).then(res => {
  storeVO.value = res.data.result
  name.value = storeVO.value.name
  logoUrl.value = storeVO.value.logoUrl
  rating.value = parseRating(storeVO.value.rating)
  number.value = storeVO.value.number
  location.value = storeVO.value.location
  console.log(number.value)
})

getProductsByStoreId(Number(storeId)).then(res => {
  productList.value = res.data.result
})

function toCreateProductPage() {
  router.push("/createProduct/" + storeId)
}

function toProductDetailPage(productId: number) {
  router.push("/productDetail/" + productId)
}

function toBackPage() {
  router.push("/allStore")
}
</script>


<template>
  <el-container>
    <el-aside width="25%" class="page-aside">
      <div class="store-detail-main">
        <el-button @click="toBackPage()"
                   type="primary" circle plain class="back-button">
          <el-icon>
            <Back/>
          </el-icon>
        </el-button>

        <el-image class="logo-image" :src="logoUrl"/>

        <span class="store-title">{{ name }}</span>

        <el-descriptions :column="1">

          <!--Lab3新增，商店评分-->
          <el-descriptions-item style="font-size: 10px" label="评分">
            <el-rate
                v-model="rating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value} 分"
                size="small"
            />
            （共 {{ number }} 人打分）
          </el-descriptions-item>

          <el-descriptions-item style="font-size: 10px" label="地址">
            {{ location }}
          </el-descriptions-item>
        </el-descriptions>

        <el-button class="create-product-button" type="primary" plain
                   @click="toCreateProductPage()" v-if="role === 'STAFF' && storeId === actualStoreId">新增商品
        </el-button>
      </div>
    </el-aside>

    <el-main>
      <div>
        <span class="product-list-title">商品列表</span>
      </div>
      <div class="all-product-main">
        <ProductItem class="product-item-list"
                     v-for="productVO in productList" :productId="productVO.id"
                     @click="toProductDetailPage(productVO.id)"/>
      </div>
    </el-main>
  </el-container>

</template>


<style scoped>
.page-aside {
  border-right: lightgrey solid 1px;
}

.back-button {
  margin-top: 20px;
  margin-bottom: 20px;
}

.store-title {
  font-size: 30px;
  margin-bottom: 20px;
}

.store-detail-main {
  width: 80%;
  margin-left: 10%;
  display: flex;
  flex-direction: column;
  justify-content: start;
}

.logo-image {
  width: 100%;
}

.create-product-button {
  margin-top: 20px;
}

.product-list-title {
  font-size: 30px;
  margin-top: 20px;
  margin-bottom: 20px;
  margin-left: 20px;
}

.all-product-main {
  display: flex;
  flex-direction: row;
  padding: 20px;
  flex-flow: wrap;
  align-content: start;
  justify-content: start;
}
</style>
