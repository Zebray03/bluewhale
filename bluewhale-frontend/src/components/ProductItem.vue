<script setup lang="ts">
import {ref} from "vue"
import {getProductById} from "../api/product.ts"
import {parseCategory, parseRating} from "../utils"

// 使用props接收父界面传来的数据
const props = defineProps({
  productId: {
    type: Number,
    required: true
  }
})

const storeId = ref(0)
const name = ref('')
const photoUrlList = ref([])
const rating = ref(0)
const number = ref(0)
const salesAmount = ref(0)
const stock = ref(0)
const category = ref()
const price = ref()
const photoUrl = ref('')

getProductById(props.productId).then(res => {
  storeId.value = res.data.result.storeId
  name.value = res.data.result.name
  photoUrlList.value = res.data.result.photoUrlList
  rating.value = parseRating(res.data.result.rating)
  number.value = res.data.result.number
  salesAmount.value = res.data.result.salesAmount
  stock.value = res.data.result.stock
  category.value = res.data.result.category
  price.value = res.data.result.price

  photoUrl.value = photoUrlList.value[0]
})
</script>


<template>
  <el-card class="product-item-card" shadow="hover">
    <div class="photo-div">
      <el-image class="photo-image" :src="photoUrl"/>
    </div>
    <div style="padding: 14px">
      <h1> {{ name }} </h1>
      <el-descriptions :column="1">
        <el-descriptions-item style="font-size: 10px" label="品类">
          {{ parseCategory(category) }}
        </el-descriptions-item>
        <el-descriptions-item style="font-size: 10px" label="价格">
          {{ price }} 元
        </el-descriptions-item>

        <!--Lab3新增，商品评分&销量-->
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
        <el-descriptions-item style="font-size: 10px" label="销量">
          {{ salesAmount }} 件
        </el-descriptions-item>

        <el-descriptions-item style="font-size: 10px" label="库存">
          {{ stock }} 件
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>


<style scoped>
.product-item-card {
  margin: 8px;
  border-radius: 8px;
  min-width: 31%;
}

.photo-div {
  display: flex;
  justify-content: center;
}

.photo-image {
  height: 200px;
}
</style>
