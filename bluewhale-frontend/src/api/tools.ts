import {axios} from '../utils/request'
import {API_MODULE} from './_prefix'

// 上传图片文件
export const uploadImage = (payload: any) => {
    return axios.post(`${API_MODULE}/images`, payload, {
        headers: {
            'Content-Type': "multipart/form-data;"
        }
    })
        .then(res => {
            return res
        })
}

//Lab5新增
// 获取excel的链接
export const getExcelUrl = () => {
    return axios.get(`${API_MODULE}/excel`, )
        .then(res => {
            return res
        })
}