import {axios} from '../utils/request'
import {PRODUCT_MODULE} from './_prefix'

type ProductInfo = {
    storeId: number,
    name: string,
    category: string,
    price: number,
    photoUrlList: string[]
}

export type SearchInfo = {
    name: string|null,
    lowValue: number | null,
    highValue: number | null,
    category: string | null,
    page_size:number|null,
    page_index:number|null,
    reproductList:[]|null,
    page_num:number|null
    // 最高价格
}

// 创建商品
export const createProduct = (productInfo: ProductInfo) => {
    return axios.post(`${PRODUCT_MODULE}/`, productInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 获取商店下所有商品
export const getProductsByStoreId = (storeId: number) => {
    return axios.get(`${PRODUCT_MODULE}/?storeId=${storeId}`)
        .then(res => {
            return res
        })
}

// 根据商品Id获取商品信息
export const getProductById = (productId: number) => {
    return axios.get(`${PRODUCT_MODULE}/${productId}`)
        .then(res => {
            return res
        })
}

// 添加库存
export const addStock = (id: number, number: number) => {
    return axios.post(`${PRODUCT_MODULE}/${id}/stock?number=${number}`)
        .then(res => {
            return res
        })
}

//根据商品Id获取商品评论
export const getCommentsById = (productId: number) => {
    return axios.get(`${PRODUCT_MODULE}/comment/?productId=${productId}`)
        .then(res => {
            return res;
        })
}

export const searchByCondition = (searchInfo: SearchInfo) => {
    return axios.post(`${PRODUCT_MODULE}/condition`, searchInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res;
        })
}