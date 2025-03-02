<script setup lang="ts">
import {ref} from "vue";
import {getPhoneAddressCandidate, PhoneAndAddressCandidateVO, updatePhoneNAddressCandidate} from "../api/user.ts";

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

const props = defineProps({
  telephone:{
    type:String,
    required:true
  },
  address:{
    type:String,
    required:true
  }
})

const phone = ref(props.telephone)
const addr = ref(props.address)
const newCandidateInfo = ref<PhoneAndAddressCandidateVO[]>([])
const userId = Number(sessionStorage.getItem("userId"))

//删除备选地址
function handledelete(){
  for (let i = 0; i < candidates.value.length; i++) {
    if (candidates.value[i].userId != userId.valueOf() || candidates.value[i].phoneCandidate != phone.value || candidates.value[i].addressCandidate != addr.value) {
      newCandidateInfo.value.push(candidates.value[i])
    }
  }
  updatePhoneNAddressCandidate(newCandidateInfo.value).then(
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
  <el-card class="store-item-card" :body-style="{ padding: '0px' }" shadow="hover">
    <div class="comment-item-main">
      <el-descriptions :column="1">
        <el-descriptions-item style="font-size: 15px" label="电话">
          {{ phone }}
        </el-descriptions-item>
        <el-descriptions-item style="font-size: 15px" label="地址">
          {{ addr }}
        </el-descriptions-item>
      </el-descriptions>

      <span class="button-group">
        <el-button @click="handledelete()"  size="small" type="primary">
        删除地址
      </el-button>
      </span>

    </div>

  </el-card>
</template>


<style scoped>
.store-item-card {
  margin: 20px;
  border-radius: 8px;
  min-width: 25%;
  max-width: 30px;
}

.comment-item-main {
  margin: 20px;
  line-height: 30px;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 60px;
  align-items: center;
  justify-content: right;
}
</style>
