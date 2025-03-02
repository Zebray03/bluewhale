<script setup lang="ts">
import {ref} from "vue"
import {getAllOrder} from "../../api/order.ts"
import OrderItem from "../../components/OrderItem.vue"
import {getExcelUrl} from "../../api/tools.ts";

const role = sessionStorage.getItem("role")

const orderList = ref()

getAllOrder().then(res => {
  orderList.value = res.data.result
  console.log(res.data.result)
})

function downloadOrderExcel () {
  getExcelUrl().then(res => {
    const link = document.createElement('a')
    link.href = res.data.result
    link.setAttribute('download', '')
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  })

}

</script>


<template>
  <el-main>
    <!--Lab5新增，工作人员和CEO可以下载订单报表查看-->
    <div v-if="role === 'STAFF' || role === 'MANAGER'">
        <el-button @click="downloadOrderExcel" class="get-excel-button" type="success" plain>
          下载订单报表
        </el-button>
    </div>

    <div class="order-item-list">
      <OrderItem
          v-for="orderVO in orderList" :orderId="orderVO.id"/>
    </div>
  </el-main>
</template>


<style scoped>
.order-item-list {
  display: flex;
  padding: 2px;
  flex-flow: wrap;
  justify-content: center;
}
</style>
