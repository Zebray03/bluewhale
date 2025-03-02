<script setup lang="ts">
import {ref, computed} from "vue"
import PayDialog from "./PayDialog.vue"
import {deliverOrder, getOrder, getOrderById, commentOrder} from "../api/order.ts"
import {parseOrderType, parseRating, parseTime} from "../utils"

const props = defineProps({
  orderId: {
    type: Number,
    required: true
  }
})

const role = sessionStorage.getItem("role")

//订单详细信息
const userId = ref(0)
const productId = ref(0)
const productName = ref('')   //商品名字
const price = ref(0)          //商品单价
const amount = ref(0)         //购买数量
const type = ref('')          //订单类型
const rating = ref(0)         //订单评分
const content = ref('')       //订单评论
const status = ref('')        //订单状态
const createTime = ref('')
const finishTime = ref('')
const storeId = ref(0)
const paid = ref(null)           //Lab4新增，实付金额
//订单总价（折扣前）
const totalPrice = ref(0)
//评论dialog
const commentDialogVisible = ref(false)

const dialogRef = ref()
const startTime = ref<Date>()
const endTime = ref<Date>()

getOrderDetail()

function getOrderDetail() {
  getOrderById(props.orderId).then(res => {
    userId.value = res.data.result.userId
    productId.value = res.data.result.productId
    productName.value = res.data.result.productName
    amount.value = res.data.result.amount
    price.value = res.data.result.price
    type.value = res.data.result.type
    content.value = res.data.result.content
    rating.value = parseRating(res.data.result.rating)
    status.value = res.data.result.status
    createTime.value = res.data.result.createTime
    startTime.value=new Date(createTime.value)
    endTime.value=new Date(startTime.value?.getTime()+10*60*1000)
    finishTime.value = res.data.result.finishTime
    storeId.value = res.data.result.storeId
    paid.value = res.data.result.paid
    //根据单价和购买数量计算总价
    totalPrice.value = price.value * amount.value
  })
}

function handlePay() {
  //触发支付订单弹窗，传入当前orderId
  dialogRef.value.open(props.orderId)
}

//注册回调，用于更新当前订单详情（如果不用这个，可能只能强制刷新整个界面了，用户体验不好）
function handleConfirmOrder(success: boolean) {
  if (success) {
    getOrderDetail()
  }
}

//发货
function handleDeliver() {
  deliverOrder(props.orderId).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '订单发货成功！',
        type: 'success',
        center: true,
      })
      getOrderDetail()
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

//收货
function handleGet() {
  getOrder(props.orderId).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '订单收货成功！',
        type: 'success',
        center: true,
      })
      getOrderDetail()
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

//打开评论dialog
function handleComment() {
  commentDialogVisible.value = true
}

//评论
function handleSendComment() {
  const payload = {
    orderId: props.orderId,
    comment: content.value,
    rating: rating.value
  }

  commentOrder(payload).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '评价成功！',
        type: 'success',
        center: true,
      })
      getOrderDetail()
      commentDialogVisible.value = false
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

// 对于变化的内容使用计算属性返回响应式结果
const statusText = computed(() => {
  switch (status.value) {
    case 'UNPAID':
      return "待顾客支付"
    case 'UNSEND':
      return "待商家发货"
    case 'UNGET':
      return "待顾客收货"
    case 'UNCOMMENT':
      return "待顾客评价"
    case 'DONE':
      return "已完成"
    default:
      return "状态标签"
  }
})
</script>


<template>
  <el-card class="order-item-card" shadow="hover">

    <template #header>
      <div class="card-header">
        <div>
          <span> 订单号 {{ props.orderId }}</span>
          <el-tag style="margin-left: 8px" v-if="status!=='DONE'" type="info"> {{ statusText }}</el-tag>
          <el-tag style="margin-left: 8px" v-if="status==='DONE'" type="success"> {{ statusText }}</el-tag>
        </div>

        <el-button @click="handlePay" v-if="status==='UNPAID' && role==='CUSTOMER'"
                   class="status-change-button" size="small" type="primary">
          支付
        </el-button>
        <el-button @click="handleDeliver" v-if="status==='UNSEND' && role==='STAFF'"
                   class="status-change-button" size="small" type="primary">
          发货
        </el-button>
        <el-button @click="handleGet" v-if="status==='UNGET' && role==='CUSTOMER'"
                   class="status-change-button" size="small" type="primary">
          收货
        </el-button>
        <el-button @click="handleComment" v-if="status==='UNCOMMENT'&& role==='CUSTOMER'"
                   class="status-change-button" size="small" type="primary">
          评价
        </el-button>
      </div>
    </template>

    <el-descriptions
        :column="1"
    >
      <el-descriptions-item style="font-size: 15px" label="商品">
        {{ productName }}
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="数量">
        {{ amount }} 件
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="原价">
        {{ totalPrice }} 元
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="实付金额" v-if="status!=='UNPAID'">
        {{ paid }} 元
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="取货类型">
        <el-tag> {{ parseOrderType(type) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="创建时间">
        {{ parseTime(createTime) }}
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="完成时间">
        {{ parseTime(finishTime) }}
      </el-descriptions-item>
      <el-descriptions-item v-if="status=='UNPAID'">
        <el-countdown format="DD [days] HH:mm:ss" :value="endTime"/>
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="评分" v-if="status==='DONE'">
        <el-rate
            v-model="rating"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value} 分"
            size="small"
        />
      </el-descriptions-item>
      <el-descriptions-item style="font-size: 15px" label="评价内容" v-if="status==='DONE'">
        {{ content }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>

  <PayDialog ref="dialogRef" @operation-finish="handleConfirmOrder"/>

  <el-dialog v-model="commentDialogVisible" title="发起评价">
    <el-form>
      <el-form-item>
        <span style="margin-right: 30px">商品满意度</span>
        <el-rate v-model="rating" clearable
                 :texts="['非常差', '差', '普通', '好', '非常好']"
                 show-text/>
      </el-form-item>

      <el-form-item>
        <label>文字评价</label>
        <el-input v-model="content" type="textarea" rows="10" placeholder="说点什么吧">

        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSendComment">确定评价</el-button>
        <el-button @click="commentDialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>


<style scoped>
.order-item-card {
  margin: 20px;
  border-radius: 8px;
  min-width: max-content;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.status-change-button {
  margin-left: 10px;
}
</style>