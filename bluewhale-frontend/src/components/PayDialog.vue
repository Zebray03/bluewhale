<script setup lang="ts">
import {ref, onMounted} from "vue"
import {calculateOrder, getOrderById, payOrder} from "../api/order.ts"
import {getAvailableCouponsByStoreId} from "../api/coupon.ts"
import {parseOrderType, parseCouponType} from "../utils"
import {ElTable} from "element-plus"
import {RefreshRight} from "@element-plus/icons-vue"

const saveOrderId = ref(0)
const orderDialogVisible = ref(false)
const emit = defineEmits(['operationFinish'])

//通过父组件传来的orderId获取订单详情
function open(orderId: number) {
  //存储当前订单orderId
  saveOrderId.value = orderId
  getOrderDetail(orderId)
  orderDialogVisible.value = true
}

defineExpose({
  open
})

const amount = ref(0)
const price = ref(0)
const type = ref('')
const storeId = ref(0)
const totalPrice = ref(0)
const couponId = ref(0)
const discountPrice = ref(0)

function getOrderDetail(orderId: number) {
  getOrderById(orderId).then(res => {
    amount.value = res.data.result.amount
    type.value = res.data.result.type
    price.value = res.data.result.price
    storeId.value = res.data.result.storeId
    //根据商品单价和购买数量计算总价
    totalPrice.value = price.value * amount.value
    discountPrice.value = totalPrice.value

    getAvailableCouponsByStoreId(storeId.value).then(res => {
      availableCouponList.value = res.data.result
    })
  })
}



// 处理优惠券
interface Coupon {
  id: number
  groupId: number
  type: string
  satisfaction: number
  minus: number
}

const availableCouponList = ref<Coupon[]>([])
const currentRow = ref()
const singleTableRef = ref<InstanceType<typeof ElTable>>()



const setCurrent = (row?: Coupon) => {
  singleTableRef.value!.setCurrentRow(row)
}

const handleCurrentChange = (val: Coupon | undefined) => {
  if (val) {
    couponId.value = val.id
  } else {
    couponId.value = 0
  }
  currentRow.value = val
  calculateOrder(saveOrderId.value, couponId.value).then(res => {
    discountPrice.value = res.data.result
  })
}



//Lab5新增
const showForm = ref(false)
const isLoading = ref(false)
const refreshButtonVisible = ref(false)

function handleConfirmOrder() {
  payOrder(saveOrderId.value, couponId.value).then(res => {
    if (res.data.code === '000') {
      //以下为Lab5新增，打开一个新的支付界面
      isLoading.value = true
      refreshButtonVisible.value = true
      const paymentWindow = window.open('', '_blank')
      if (paymentWindow) {   //只有当paymentWindow不是null时，才写入内容
        paymentWindow.document.write(res.data.result);
        paymentWindow.document.forms[0].submit() // 提交表单
        showForm.value = false // 隐藏表单
        //在本项目中，传回的参数没有实际作用
        emit("operationFinish", true)
      } else {
        ElMessage({
          message: '无法打开支付窗口',
          type: 'error',
          center: true,
        })
      }
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
      //在本项目中，传回的参数没有实际作用
      emit("operationFinish", false)
    }
  })
}

//关闭支付弹窗时，要重置一些值
function handlePayDialogClose() {
  orderDialogVisible.value = false
  couponId.value = 0
  isLoading.value = false
  refreshButtonVisible.value = false
}

//刷新页面
//其实我在批改作业的时候也跟不少组讲过，应该尽量避免界面刷新，更别说设置手动刷新了，
//一方面是会影响用户体验，另一方面是其实你只修改了页面里一个很小的东西，没必要刷新，重新加载所有元素会很耗费性能
//而这边设置手动刷新，是因为没办法让商城系统自动识别支付宝支付有没有成功。
//其实大家应该也有经历，不少网站都是需要在支付后手动刷新的。
function refreshPage() {
  location.reload()
}

onMounted(() => {
  showForm.value = true // 组件加载后显示表单
})


</script>


<template>
  <el-dialog v-model="orderDialogVisible" :before-close="handlePayDialogClose">
    <el-row>
      <span class="pay-dialog-title">订单支付</span>
      <!--Lab5新增，一个支付后的刷新按钮-->
      <el-button v-if="refreshButtonVisible" @click="refreshPage()"
                 type="primary" plain class="pay-dialog-refresh-button">
        已完成支付，刷新界面
        <el-icon>
          <RefreshRight/>
        </el-icon>
      </el-button>
    </el-row>

    <div v-loading="isLoading">
      <el-form>
        <el-form-item>
          <label for="amount">购买数量：</label>
          {{ amount }} 件
        </el-form-item>
        <el-form-item>
          <label for="type">提货方式：</label>
          {{ parseOrderType(type) }}
        </el-form-item>
        <el-form-item>
          <label for="totalPrice">总价：</label>
          {{ totalPrice }} 元
        </el-form-item>

        <el-form-item>
          <label for="type">优惠券：</label>
          <el-button @click="setCurrent()">不使用优惠券</el-button>
          <el-table
              ref="singleTableRef"
              :data="availableCouponList"
              highlight-current-row
              style="width: 100%"
              @current-change="handleCurrentChange"
          >
            <el-table-column type="index" width="50"/>
            <el-table-column prop="type" label="类型" width="180">
              <template #default="scope">
                <el-tag v-if="scope.row.type === 'FULL_REDUCTION'">
                  {{ parseCouponType(scope.row.type) }}
                </el-tag>
                <el-tag v-if="scope.row.type === 'SPECIAL'" type="success">
                  {{ parseCouponType(scope.row.type) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="satisfaction" label="满（元）" width="180">
              <template #default="scope">
                {{ scope.row.satisfaction }}
              </template>
            </el-table-column>
            <el-table-column prop="minus" label="减（元）" width="180">
              <template #default="scope">
                {{ scope.row.minus }}
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item>
          <label>折扣后金额：</label>
          {{ discountPrice.toFixed(2) }} 元
        </el-form-item>

      </el-form>

      <el-button @click="handleConfirmOrder" type="primary" plain>确认支付</el-button>
    </div>
  </el-dialog>
</template>


<style scoped>
.pay-dialog-title {
  font-size: 30px;
  margin-bottom: 20px;
}
</style>