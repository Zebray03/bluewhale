import {axios} from '../utils/request'
import {ORDER_MODULE} from './_prefix'

type OrderInfo = {
    productId: number,
    amount: number,
    type: string,
    phone?: string,
    address?: string
}

type CommentInfo = {
    orderId: number,
    comment: string,
    rating: number
}

// 创建订单
export const createOrder = (orderInfo: OrderInfo) => {
    return axios.post(`${ORDER_MODULE}/`, orderInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 订单支付（你可能需要修改这个方法，把优惠券加进来）
export const payOrder = (orderId: number, couponId: number) => {
    return axios.post(`${ORDER_MODULE}/pay/?orderId=${orderId}&couponId=${couponId}`)
        .then(res => {
            return res
        })
}

// 预计算价格
export const calculateOrder = (orderId: number, couponId: number) => {
    return axios.get(`${ORDER_MODULE}/calculate/?orderId=${orderId}&couponId=${couponId}`)
        .then(res => {
            return res
        })
}

// 获取全部订单（用户下订单/门店下订单/全部订单）
export const getAllOrder = () => {
    return axios.get(`${ORDER_MODULE}/`,)
        .then(res => {
            return res
        })
}

// 根据订单Id获取单个订单
export const getOrderById = (orderId: number) => {
    return axios.get(`${ORDER_MODULE}/${orderId}`,)
        .then(res => {
            return res
        })
}

// 订单发货
export const deliverOrder = (orderId: number) => {
    return axios.post(`${ORDER_MODULE}/deliver/?orderId=${orderId}`)
        .then(res => {
            return res
        })
}

// 订单收货
export const getOrder = (orderId: number) => {
    return axios.post(`${ORDER_MODULE}/get/?orderId=${orderId}`)
        .then(res => {
            return res
        })
}

// 评价订单
export const commentOrder = (commentInfo: CommentInfo) => {
    return axios.post(`${ORDER_MODULE}/comment`, null, {params: commentInfo})
        .then(res => {
            return res
        })
}