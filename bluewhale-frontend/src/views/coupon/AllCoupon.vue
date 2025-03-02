<!--Lab4新增，全部优惠券界面-->
<script setup lang="ts">
import {computed, ref} from "vue"
import {createCoupon, getCouponGroups, getCoupons, receiveCouponGroup} from "../../api/coupon"
import {userInfo} from "../../api/user"
import {parseCouponType} from "../../utils"

const role = sessionStorage.getItem("role")
const storeId = ref(0)
if (role === 'STAFF'){
  storeId.value = Number(sessionStorage.getItem("storeId"))
}
const myCouponList = ref()
const receiveCouponList = ref()
const type = ref()
const satisfaction = ref(undefined)
const minus = ref(undefined)
const rest = ref(undefined)
const storeName = ref()

//从用户信息中获取到当前用户的店铺名（可能为空）
userInfo().then(res => {
  storeName.value = res.data.result.storeName
})

getReceiveCouponList()
getMyCouponList()

function getReceiveCouponList() {
  getCouponGroups().then(res => {
    receiveCouponList.value = res.data.result
  })
}

function getMyCouponList() {
  getCoupons().then(res => {
    myCouponList.value = res.data.result
  })
}

const hasTypeInput = computed(() => type.value != null)
const hasSatisfactionInput = computed(() => satisfaction.value != '')
const hasMinusInput = computed(() => minus.value != '')
const hasRestInput = computed(() => rest.value != '')
//确定创建优惠券按钮是否可用
const createCouponDisabled = computed(() => {
  if (type.value === 'FULL_REDUCTION') {
    return !(hasTypeInput.value && hasSatisfactionInput.value && hasMinusInput.value && hasRestInput.value);
  } else if (type.value === 'SPECIAL') {
    return !(hasTypeInput.value && hasRestInput.value);
  } else {
    return true;
  }
})

const couponDialogVisible = ref(false)

//打开创建优惠券弹窗
function toCreateCoupon() {
  couponDialogVisible.value = true
}

//关闭创建优惠券弹窗，需要将这些参数都置为默认值
function handleCloseCouponDialog() {
  type.value = null
  satisfaction.value = undefined
  minus.value = undefined
  rest.value = undefined
  couponDialogVisible.value = false
}

//确定创建优惠券
function handleCreateCoupon() {
  couponDialogVisible.value = false
  createCoupon({
    type: type.value,
    storeId: storeId.value,
    satisfaction: satisfaction.value,
    minus: minus.value,
    rest: rest.value
  }).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '创建优惠券成功！',
        type: 'success',
        center: true,
      })
      //更新可获取优惠券列表
      getReceiveCouponList()
      type.value = ''
      satisfaction.value = undefined
      minus.value = undefined
      rest.value = undefined
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

//顾客获取优惠券
function receiveCoupon(couponGroupId: number) {
  receiveCouponGroup(couponGroupId).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '获取优惠券成功！',
        type: 'success',
        center: true,
      })
      //更新我的优惠券列表
      getMyCouponList()
      //更新可获取优惠券列表
      getReceiveCouponList()
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}
</script>


<template>
  <div>
    <el-button @click="toCreateCoupon()" v-if="role === 'MANAGER' || role === 'STAFF'"
               class="create-coupon-button" type="primary" plain>
      新增优惠券
    </el-button>

    <el-dialog v-model="couponDialogVisible" title="创建优惠券" :before-close="handleCloseCouponDialog">
      <el-form label-width="100px" class="create-coupon-form">
        <el-form-item label="应用范围：">
          <span v-if="role === 'MANAGER'">全平台</span>
          <span v-if="role === 'STAFF'">{{ storeName }}</span>
        </el-form-item>

        <el-form-item label="类型：" for="type">
          <el-select id="identity"
                     v-model="type"
                     placeholder="请选择"
          >
            <el-option value="FULL_REDUCTION" label="满减"/>
            <el-option value="SPECIAL" label="蓝鲸券"/>
          </el-select>
        </el-form-item>

        <el-form-item label="满：" for="satisfaction" v-if="type === 'FULL_REDUCTION'">
          <el-input id="satisfaction" v-model="satisfaction"
                    required placeholder="请输入金额" class="create-coupon-dialog-input">
            <template #append>元</template>
          </el-input>
        </el-form-item>

        <el-form-item label="减：" for="minus" v-if="type === 'FULL_REDUCTION'">
          <el-input id="minus" v-model="minus" required placeholder="请输入金额"
                    class="create-coupon-dialog-input">
            <template #append>元</template>
          </el-input>
        </el-form-item>

        <el-alert v-if="type === 'SPECIAL'"
                  title="“蓝鲸券”使用规则：" type="success" :closable="false" class="special-alert">
          * 0-100元区间打九五折；<br>
          * 200-300元区间打八五折；<br>
          * 300-400元区间打八折；<br>
          * 400-500元区间打七五折；<br>
          * 500元以上区间打七折。<br>
        </el-alert>

        <el-form-item label="数量：" for="rest">
          <el-input id="rest" v-model="rest" required placeholder="请输入数量"
                    class="create-coupon-dialog-input">
            <template #append>张</template>
          </el-input>
        </el-form-item>

        <el-button @click.prevent="handleCreateCoupon()" :disabled="createCouponDisabled"
                   type="primary" plain>
          创建
        </el-button>

      </el-form>
    </el-dialog>

    <div class="all-coupon-main">
      <div>
        <span class="this-title" v-if="role === 'CUSTOMER'">可领优惠券（按发放时间排序）</span>
        <span class="this-title" v-if="role === 'MANAGER'">全部优惠券</span>
        <span class="this-title" v-if="role === 'STAFF'">门店下优惠券</span>
        <el-table :data="receiveCouponList" stripe class="coupon-table">
          <el-table-column type="index" width="50"/>
          <el-table-column prop="storeName" label="所属商店" width="180"/>
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
          <el-table-column prop="rest" label="剩余数量（张）" width="180"/>
          <el-table-column label="操作" v-if="role === 'CUSTOMER'">
            <template #default="scope">
              <el-button @click="receiveCoupon(scope.row.id)"
                         size="small" type="primary" plain>
                领取
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-if="role === 'CUSTOMER'">
        <span class="this-title">我的优惠券（按领取时间排序）</span>
        <el-table :data="myCouponList" stripe class="coupon-table">
          <el-table-column type="index" width="50"/>
          <el-table-column prop="storeName" label="所属商店" width="180"/>
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
          <el-table-column prop="used" label="状态" width="180">
            <template #default="scope">
              <el-tag v-if="scope.row.used === false" type="success">待使用</el-tag>
              <el-tag v-if="scope.row.used === true" type="info">已使用</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>


<style scoped>
.create-coupon-button {
  width: 100px;
  margin-left: 20px;
  margin-top: 30px;
}

.create-coupon-form {
  width: 60%;
}

.special-alert {
  margin-bottom: 15px;
}

.all-coupon-main {
  margin-left: 80px;
  margin-top: 30px;
}

.this-title {
  font-size: large;
  margin-top: 20px;
  margin-bottom: 20px;
}

.coupon-table {
  margin-bottom: 30px;
}
</style>
