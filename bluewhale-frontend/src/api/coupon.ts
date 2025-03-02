import {axios} from '../utils/request'
import {COUPON_MODULE} from './_prefix'

type CouponInfo = {
    type: string,
    storeId:number,
    satisfaction?: number,
    minus?: number,
    rest?: number
}

// 获取已有优惠券
export const getCoupons = () => {
    return axios.get(`${COUPON_MODULE}`,)
        .then(res => {
            return res
        })
}

// 查看优惠券组（剩余数量大于0、未领取过的优惠券组/门店下优惠券组/全部优惠券组）
export const getCouponGroups = () => {
    return axios.get(`${COUPON_MODULE}/group`,)
        .then(res => {
            return res
        })
}

// 创建优惠券
export const createCoupon = (couponInfo: CouponInfo) => {
    return axios.post(`${COUPON_MODULE}/`, couponInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 领取优惠券
export const receiveCouponGroup = (couponGroupId: number) => {
    return axios.post(`${COUPON_MODULE}/receive/?couponGroupId=${couponGroupId}`)
        .then(res => {
            return res
        })
}

// 获取门店下可用优惠券
export const getAvailableCouponsByStoreId = (storeId: number) => {
    return axios.get(`${COUPON_MODULE}/available/?storeId=${storeId}`)
        .then(res => {
            return res
        })
}