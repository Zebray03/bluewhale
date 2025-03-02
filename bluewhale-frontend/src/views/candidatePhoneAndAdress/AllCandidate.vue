<script setup lang="ts">

import CandidateItem from "../../components/CandidateItem.vue";
import {computed, ref} from "vue";
import {getPhoneAddressCandidate, PhoneAndAddressCandidateVO, updatePhoneNAddressCandidate} from "../../api/user.ts";
const candidates = ref<PhoneAndAddressCandidateVO[]>([])
getPhoneAddressCandidate().then(res=>{
  if(res.data.code==='000'){
    candidates.value=res.data.result
  }else{
    ElMessage({
      message:"拉取地址电话失败",
      type:"error",
      center:true
    })
  }
})
const addedPhoneNum = ref('')
const addedAddr = ref('')
const hasTelInput = computed(() => addedPhoneNum.value != '')
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(addedPhoneNum.value))
const addrnotnull = computed(()=>
   addedAddr.value!=''
)
const  adddisabled = computed (()=>{
  return !(hasTelInput.value && addrnotnull.value && telLegal.value)
})
const addcandidateInfo = ref<PhoneAndAddressCandidateVO>()
function addCandidate(){
  addcandidateInfo.value = {
    userId: Number(sessionStorage.getItem("userId")),
    phoneCandidate: addedPhoneNum.value,
    addressCandidate:addedAddr.value
  }
  candidates.value.push(addcandidateInfo.value)
  updatePhoneNAddressCandidate(candidates.value).then(
      res=>{
        if(res.data.code==='000'){
          ElMessage({
            message:"添加成功",
            center:true,
            type:"success"
          })
          window.location.reload()
        }else{
          ElMessage({
            message:"添加失败",
            center:true,
            type:"success"
          })
        }
      }
  )
}
</script>

<template>
  <el-main>
    <el-card class="add-card">
      <div>
        <h1>添加收货地址</h1>

        <el-form>
          <el-row>
            <el-col :span="11">
              <el-form-item>
                <label for="addedAddr">地址</label>
                <el-input id="addedAddr" v-model="addedAddr" placeholder="请输入地址"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="2"></el-col>

            <el-col :span="11">
              <el-form-item>
                <label v-if="!hasTelInput" for="addedPhoneNum">
                  手机号
                </label>
                <label v-else-if="!telLegal" for="addedPhoneNum" class="error-warn">
                  手机号不合法
                </label>
                <label v-else for="addedPhoneNum">
                  手机号
                </label>

                <el-input id="addedPhoneNum" v-model="addedPhoneNum" :class="{'error-warn-input' :(hasTelInput && !telLegal)}" placeholder="请输入电话号码"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <span class="button-group">
            <el-button @click="addCandidate" :disabled="adddisabled" type="primary">
              添加
            </el-button>
          </span>
        </el-form>

      </div>
    </el-card>
    <el-divider></el-divider>

    <el-card class="add-card">
      <div>
        <h1>地址薄</h1>
      </div>
      <div class="order-item-list">
        <CandidateItem
            v-for="orderVO in candidates" :address="orderVO.addressCandidate" :telephone="orderVO.phoneCandidate"/>
      </div>
    </el-card>
  </el-main>
</template>

<style scoped>
.add-card {
  width: 99%;
  padding: 3px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}

.order-item-list {
  display: flex;
  padding: 2px;
  flex-flow: wrap;
  justify-content: center;
}
</style>