<script setup lang="ts">
import {ref, computed} from "vue"
import {router} from '../../router'
import {Back} from "@element-plus/icons-vue"
import CommentItem from "../../components/CommentItem.vue"
import {addStock, getProductById} from "../../api/product.ts"
import {getCommentsById} from "../../api/product.ts"
import {parseCategory, parseRating} from "../../utils"
import ProductItem from "../../components/ProductItem.vue";
import {getRecommendationByProductId} from "../../api/recommend.ts";
import ChooseCandidateDialogue from "../../components/ChooseCandidateDialogue.vue";

const role = sessionStorage.getItem("role")
const actualStoreId = Number(sessionStorage.getItem("storeId"))

const productId = ref(Number(router.currentRoute.value.params.productId))
const productVO = ref()
const storeId = ref(0)
const name = ref('')
const photoUrlList = ref([])
const rating = ref(0)          //评分
const number = ref(0)          //评分人数
const salesAmount = ref(0)     //销量
const stock = ref(10)          //库存
const category = ref('')       //品类
const price = ref(0)           //单价

const type = ref()                      //订单提货方式
const addStockNumber = ref()            //添加库存量
const amount = ref(1)             //购买数量
const totalPrice = ref(price.value)     //总价（折扣前）
const commentList = ref([])       //评论列表

//关于支付订单弹窗

const dialogRef = ref()
const recommendProductList = ref([])

//实时计算当前总价
const handleChange = () => {
  totalPrice.value = amount.value * price.value
}

//验证创建订单按钮可用性
const hasTypeInput = computed(() => type.value != null)
const amountLegal = computed(() => amount.value <= stock.value)
const createOrderDisabled = computed(() => {
  return !(amountLegal.value && hasTypeInput.value)
})
//验证添加库存按钮可用性
const hasAddStockInput = computed(() => addStockNumber.value != '')
const addStockDisabled = computed(() => {
  return !(hasAddStockInput.value)
})

getCommentsById(productId.value).then(res => {
  commentList.value = res.data.result
})

getProductDetail()

getRecommend()

function toProductDetailPage(productId: number) {
  router.push("/productDetail/" + productId)
}

function getProductDetail() {
  getProductById(productId.value).then(res => {
    productVO.value = res.data.result
    storeId.value = productVO.value.storeId
    name.value = productVO.value.name
    photoUrlList.value = productVO.value.photoUrlList
    rating.value = parseRating(productVO.value.rating)
    number.value = productVO.value.number
    salesAmount.value = productVO.value.salesAmount
    stock.value = productVO.value.stock
    category.value = productVO.value.category
    price.value = productVO.value.price

    totalPrice.value = price.value
  })
}

function getRecommend(){
  getRecommendationByProductId(productId.value).then(
      res=>{
        if(res.data.code==='000'){
        recommendProductList.value=res.data.result}else{
          ElMessage({
            message:"无法获取推荐",
            type:"error",
            center:true
          })
        }
      }
  )
}


// 添加库存按钮
function AddStock() {
  addStock(productId.value, addStockNumber.value).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '添加库存成功！',
        type: 'success',
        center: true,
      })
      addStockNumber.value = null
      //更新商品详情
      getProductDetail()
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

// 创建订单按钮
function handleCreateOrder() {
  console.log("createOrder")
  console.log(amount.value)
  console.log(productId.valueOf())
  console.log(type.value)
  dialogRef.value.open(productId.valueOf(),amount.value,type.value)
}

function toBackPage() {
  router.push("/storeDetail/" + storeId.value)
}
</script>


<template>
  <el-container>
    <el-aside width="25%" class="page-aside">
      <div class="product-detail-aside">
        <el-button @click="toBackPage()"
                   type="primary" circle plain class="back-button">
          <el-icon>
            <Back/>
          </el-icon>
        </el-button>

        <el-carousel trigger="click" arrow="always" indicator-position="outside">
          <el-carousel-item v-for="item in photoUrlList" :key="item">
            <el-image class="logo-image" :src="item"/>
          </el-carousel-item>
        </el-carousel>

        <span class="product-title">{{ name }}</span>

        <el-descriptions :column="1">
          <el-descriptions-item style="font-size: 10px" label="品类">
            {{ parseCategory(category) }}
          </el-descriptions-item>

          <el-descriptions-item style="font-size: 10px" label="价格">
            {{ price }} 元
          </el-descriptions-item>

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
    </el-aside>

    <el-main>
      <div v-if="role === 'CUSTOMER'">
        <div>
          <span class="main-title">购买商品</span>
        </div>
        <el-form class="buy-form">
          <el-form-item>
            <label for="amount">购买数量：</label>
            <el-input-number v-model="amount" :min="1" :max="stock" @change="handleChange"/>
          </el-form-item>
          <el-form-item>
            <label for="type">提货方式：</label>
            <el-select id="type"
                       v-model="type"
                       placeholder="请选择"
            >
              <el-option value="PICKUP" label="到店自提"/>
              <el-option value="DELIVERY" label="快递到家"/>
            </el-select>
          </el-form-item>

          <el-form-item>
            <label for="totalPrice">总价：</label>
            {{ totalPrice }} 元
          </el-form-item>
        </el-form>
        <el-button @click="handleCreateOrder" :disabled="createOrderDisabled"
                   class="buy-button" type="primary" plain>创建订单
        </el-button>
      </div>

      <div v-if="role === 'STAFF' && storeId === actualStoreId">
        <div>
          <span class="main-title">添加库存</span>
        </div>
        <div class="add-stock-main">
          <el-input class="add-stock-number-input"
                    v-model="addStockNumber" placeholder="请输入添加库存数">
            <template #append>件</template>
          </el-input>
          <br>
          <el-button @click="AddStock" :disabled="addStockDisabled"
                     class="add-stock-button" type="primary" plain>
            新增库存
          </el-button>
        </div>
      </div>

      <div>
        <div>
          <span class="main-title">评论区</span>
        </div>
        <CommentItem v-for="commentVO in commentList" :commentVO="commentVO">
        </CommentItem>
      </div>
      <div v-if="role==='CUSTOMER'">
        <div>
          <span class="main-title">推荐商品</span>
        </div>
        <ProductItem v-for="ProductVo in recommendProductList" :productId="ProductVo.id" @click="toProductDetailPage(ProductVo.id)"></ProductItem>
      </div>
    </el-main>

    <!--支付弹窗组件-->
    <ChooseCandidateDialogue ref="dialogRef"></ChooseCandidateDialogue>
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

.product-title {
  font-size: 30px;
  margin-bottom: 20px;
}

.product-detail-aside {
  width: 80%;
  margin-left: 10%;
  display: flex;
  flex-direction: column;
  justify-content: start;
}

.logo-image {
  width: 100%;
}

.add-stock-number-input {
  width: 300px;
  margin-bottom: 20px;
}

.add-stock-button {
  margin-bottom: 40px;
}

.main-title {
  font-size: 30px;
  margin-top: 20px;
  margin-bottom: 20px;
  margin-left: 20px;
}

.buy-form {
  margin-top: 20px;
  margin-left: 30px;
  width: 20%;
}

.add-stock-main {
  margin-top: 20px;
  margin-left: 30px;
}

.buy-button {
  margin-left: 30px;
  margin-bottom: 40px;
}
</style>
