import {axios} from '../utils/request'
import {RECOMMEND_MODULE} from './_prefix'

export const getRecommendationByProductId = (productId:number)=>{
    return axios.get(`${RECOMMEND_MODULE}/product/${productId}`).then(
        res=>{
            return res
        }
    )
}

export const getRecommendationOfCurrentUser = ()=>{
    return axios.get(`${RECOMMEND_MODULE}/`).then(
        res=>{
            return res
        }
    )
}