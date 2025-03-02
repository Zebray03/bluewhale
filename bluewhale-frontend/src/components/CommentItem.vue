<script setup lang="ts">
import {ref} from "vue"
import {parseTime} from "../utils"

const props = defineProps({
  commentVO: {
    type: Object,
    required: true
  }
})

const userName = ref('')
const time = ref('')
const rating = ref(0)
const comment = ref('')

userName.value = props.commentVO.userName
time.value = parseTime(props.commentVO.time)
rating.value = props.commentVO.rating
comment.value = props.commentVO.comment
</script>


<template>
  <el-card class="comment-item-card" :body-style="{ padding: '0px' }" shadow="hover">
    <div class="comment-item-main">
      <el-row>
        用户“{{ userName }}”于 {{ time }} 为商品打分：
        <el-rate
            v-model="rating"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value} 分"
            size="small"
        />
      </el-row>
      并发布评论：{{ comment }}
    </div>
  </el-card>

</template>


<style scoped>
.comment-item-card {
  margin: 20px;
  border-radius: 8px;
  width: 60%;
}

.comment-item-main {
  margin: 20px;
  line-height: 30px;
}
</style>