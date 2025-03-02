<script setup lang="ts">
import {ref, onMounted} from "vue"
import {ElTable} from "element-plus"
import {getPhoneAddressCandidate, PhoneAndAddressCandidateVO} from "../api/user.ts";
import {router} from "../router";
import {createOrder} from "../api/order.ts";
import PayDialog from "./PayDialog.vue";


const orderDialogVisible = ref(false)
const emit = defineEmits(['operationFinish'])

const saveAmount = ref(0)
const saveProductId = ref(0)
const saveType = ref('')

//通过父组件传来的orderId获取订单详情
function open(productId: number,amount: number,type: string) {
  saveAmount.value = amount
  saveProductId.value = productId
  saveType.value = type
  orderDialogVisible.value=true
}

defineExpose({
  open
})

const phone = ref('')
const addr = ref('')
const dialogRef = ref()
const orderId = ref(0)

getPhoneAddressCandidate().then(res=>{
  if(res.data.code==='000'){
    availableCandidateList.value = res.data.result
  }else{
    ElMessage({
      message:"",
      type:"success",
      center:true
    })
  }
})






const availableCandidateList = ref<PhoneAndAddressCandidateVO[]>([])
const currentRow = ref()
const singleTableRef = ref<InstanceType<typeof ElTable>>()





const handleCurrentChange = (val: PhoneAndAddressCandidateVO | undefined) => {
  if (val) {
    phone.value = val.phoneCandidate
    addr.value = val.addressCandidate
  } else {
    phone.value = ''
    addr.value = ''
  }
  currentRow.value = val
}



//Lab5新增
const showForm = ref(false)
const isLoading = ref(false)
const refreshButtonVisible = ref(false)

function handleConfirmOrder() {
  createOrder({
    productId: saveProductId.value,
    amount: saveAmount.value,
    type: saveType.value,
    phone: phone.value,
    address: addr.value
  }).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '创建订单成功！',
        type: 'success',
        center: true,
      })
      //触发支付订单弹窗，传入当前orderId
      orderId.value = res.data.result.id
      dialogRef.value.open(orderId.value)
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

//关闭支付弹窗时，要重置一些值
function handlePayDialogClose() {
  orderDialogVisible.value = false
  isLoading.value = false
  refreshButtonVisible.value = false
  saveType.value=''
  saveAmount.value = 0
  saveProductId.value=0
}

function add(){
  router.push({path:'/allCandidate'})
}

//刷新页面
//其实我在批改作业的时候也跟不少组讲过，应该尽量避免界面刷新，更别说设置手动刷新了，
//一方面是会影响用户体验，另一方面是其实你只修改了页面里一个很小的东西，没必要刷新，重新加载所有元素会很耗费性能
//而这边设置手动刷新，是因为没办法让商城系统自动识别支付宝支付有没有成功。
//其实大家应该也有经历，不少网站都是需要在支付后手动刷新的。


onMounted(() => {
  showForm.value = true // 组件加载后显示表单
})


</script>


<template>
  <el-dialog v-model="orderDialogVisible" :before-close="handlePayDialogClose">
    <el-row>
      <span class="pay-dialog-title">
        选择地址
      </span>
    </el-row>

    <div v-loading="isLoading">
      <el-form>
        <el-form-item>
          <label for="type">所有电话地址：</label>
          <el-button @click="add">去编辑</el-button>
          <el-table
              ref="singleTableRef"
              :data="availableCandidateList"
              highlight-current-row
              style="width: 100%"
              @current-change="handleCurrentChange"
          >
            <el-table-column type="index" width="65"/>
            <el-table-column prop="phoneCandidate" label="电话" width="250">
              <template #default="scope">
                {{scope.row.phoneCandidate}}
              </template>
            </el-table-column>
            <el-table-column prop="addressCandidate" label="地址" width="250">
              <template #default="scope">
                {{ scope.row.addressCandidate}}
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>

      <el-button @click="handleConfirmOrder" type="primary" plain>确认选项</el-button>
    </div>
    <PayDialog ref="dialogRef"/>
  </el-dialog>
</template>


<style scoped>
.pay-dialog-title {
  font-size: 30px;
  margin-bottom: 20px;
}
</style>