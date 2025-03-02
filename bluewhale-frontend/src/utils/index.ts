//将身份转化为中文显示
export function parseRole(role: string | null) {
    if (role === 'MANAGER') {
        return "管理员"
    } else if (role === 'CUSTOMER') {
        return "顾客"
    } else if (role === 'STAFF') {
        return "商家"
    } else if (role === 'CEO') {
        return "CEO"
    }
}

//将时间转化为日常方式
export function parseTime(time: string) {
    if (time === null) {
        return "尚未完成"
    }
    let times = time.split(/T|\./)
    return times[0] + " " + times[1]
}

//将商品品类转化为中文显示
export function parseCategory(category: string) {
    if (category === 'FOOD') {
        return "食品"
    } else if (category === 'CLOTHES') {
        return "服饰"
    } else if (category === 'FURNITURE') {
        return "家具"
    } else if (category === 'ELECTRONICS') {
        return "电子产品"
    } else if (category === 'ENTERTAINMENT') {
        return "娱乐"
    } else if (category === 'SPORTS') {
        return "体育产品"
    } else if (category === 'LUXURY') {
        return "奢侈品"
    } else if (category === 'DRINK') {
        return "饮品"
    } else if (category === 'SNACK') {
        return "小吃"
    } else if (category === 'HEALTHY') {
        return "保健品"
    } else if (category === 'ORGANIC') {
        return "有机食品"
    }
}

//将订单类型转化为中文显示
export function parseOrderType(type: string) {
    if (type === "PICKUP") {
        return "到店自提"
    } else if (type === "DELIVERY") {
        return "快递到家"
    }
}

//控制评分的长度，保留一位小数
export function parseRating(rating: number|null) {
    if (rating === null) {
        return 0
    } else {
        return Number(rating.toFixed(1))
    }
}

//Lab4新增
//将优惠券类型转化为中文显示
export function parseCouponType(type: string) {
    if (type === "FULL_REDUCTION") {
        return "满减券"
    } else if (type === "SPECIAL") {
        return "蓝鲸券"
    }
}